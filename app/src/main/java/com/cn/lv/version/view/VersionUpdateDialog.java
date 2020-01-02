package com.cn.lv.version.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.frame.api.DirHelper;
import com.cn.frame.ui.AppManager;
import com.cn.frame.utils.SweetLog;
import com.cn.lv.R;
import com.cn.lv.ZycApplication;
import com.cn.lv.version.ConstantsVersionMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

public class VersionUpdateDialog extends Dialog implements ConstantsVersionMode, View.OnClickListener {
    private static final String TAG = "VersionUpdateDialog";
    private TextView tvTitle, tvCancel, tvUpdate, tvPercent, tvContent;
    private LinearLayout llUpdateContentLayout;
    private RelativeLayout rlDownloadLayout;
    private Context context;
    /**
     * 更新模式   （强制更新还是选择更新）
     */
    private int upDateMode;
    /**
     * 是否需要下载apk (检查本地是否有已下载好的最新的apk)
     */
    private boolean isDownNewAPK = true;
    /**
     * 更新内容概要
     */
    private List<String> list;
    private String[] contentArray;

    public VersionUpdateDialog(Context context) {
        this(context, R.style.normal_dialog);
    }

    public VersionUpdateDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_version);
        initView();
        initUpdateData();
    }

    private void initView() {
        tvTitle = findViewById(R.id.act_update_version_title);
        tvCancel = findViewById(R.id.act_update_version_content_cancel);
        tvUpdate = findViewById(R.id.act_update_version_content_update);
        tvPercent = findViewById(R.id.act_update_version_content_percent);
        tvContent = findViewById(R.id.act_update_version_content);
        tvCancel.setOnClickListener(this);
        tvUpdate.setOnClickListener(this);
        llUpdateContentLayout = findViewById(R.id.act_update_version_content_layout);
        rlDownloadLayout = findViewById(R.id.act_update_version_content_down_layout);
    }

    /**
     * 初始化更新数据
     */
    private void initUpdateData() {
        switch (upDateMode) {
            case UPDATE_NONE:
            case UPDATE_CHOICE:
//                tvCancel.setText(R.string.txt_version_ignore);
                break;
            case UPDATE_MUST:
//                tvCancel.setText(R.string.txt_exit);
                break;
            default:
                break;
        }
        if (list != null && list.size() > 0) {
            llUpdateContentLayout.setVisibility(View.VISIBLE);
            tvContent.setVisibility(View.VISIBLE);
            for (int i = 0; i < list.size(); i++) {
                TextView textView = new TextView(context);
                textView.setText(list.get(i));
                textView.setTextColor(context.getResources().getColor(R.color.color_a1a8b3));
                textView.setTextSize(12);
                textView.setPadding(0, 0, 0, 10);
                llUpdateContentLayout.addView(textView);
            }
        }
        else {
            llUpdateContentLayout.setVisibility(View.GONE);
            tvContent.setVisibility(View.GONE);
        }
        if (isDownNewAPK) {
//            tvUpdate.setText(R.string.txt_version_update);
        }
        else {
//            tvUpdate.setText(R.string.txt_version_install);
        }
    }

    /**
     * 更新模式    强制更新  选择更新
     *
     * @param upDateMode
     * @return
     */
    public VersionUpdateDialog setUpdateMode(int upDateMode) {
        this.upDateMode = upDateMode;
        return this;
    }

    /**
     * 是否需要下载apk
     *
     * @param isDownNewAPK
     * @return
     */
    public VersionUpdateDialog setIsDownNewAPK(boolean isDownNewAPK) {
        this.isDownNewAPK = isDownNewAPK;
        return this;
    }

    /**
     * 版本更新内容概要
     *
     * @return
     */
    public VersionUpdateDialog setContent(String content) {
        list = new ArrayList<>();
        if (!TextUtils.isEmpty(content)) {
            try {
                contentArray = content.split("##");
            }
            catch (PatternSyntaxException e) {
                SweetLog.w(TAG, "Exception error!", e);
                return this;
            }
        }
        if (contentArray != null && contentArray.length > 0) {
            for (int i = 0; i < contentArray.length; i++) {
                list.add(contentArray[i]);
            }
        }
        return this;
    }

    /**
     * 下载的进度值
     *
     * @return
     */
    public void setProgressValue(long total, long current) {
        if (total == current) {
            isDownNewAPK = false;
            tvCancel.setEnabled(true);
            tvUpdate.setVisibility(View.VISIBLE);
//            tvUpdate.setText(R.string.txt_version_install);
            rlDownloadLayout.setVisibility(View.GONE);
        }
        else {
            tvPercent.setText((int)(current / (float)total * 100) + "%");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_update_version_content_cancel:
                switch (upDateMode) {
                    case UPDATE_NONE:
                    case UPDATE_CHOICE:
                        dismiss();
                        break;
                    case UPDATE_MUST:
                        dismiss();
                        AppManager.getInstance().finishAllActivity();
                        break;
                    default:
                        break;
                }
                break;
            case R.id.act_update_version_content_update:
                if (isDownNewAPK && onEnterClickListener != null) {
                    switch (upDateMode) {
                        //选择更新
                        case UPDATE_CHOICE:
                            onEnterClickListener.onEnter(false);
                            dismiss();
                            break;
                        //强制更新 不让用户操作
                        case UPDATE_MUST:
                            tvCancel.setEnabled(false);
                            tvUpdate.setVisibility(View.GONE);
                            rlDownloadLayout.setVisibility(View.VISIBLE);
                            onEnterClickListener.onEnter(true);
                            break;
                        default:
                            break;
                    }
                }
                else {
                    File file = new File(DirHelper.getPathFile(), "ZYC.apk");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        uri = FileProvider.getUriForFile(context, ZycApplication.getInstance().getPackageName() +
                                                                  ".fileprovider", file);
                    }
                    else {
                        uri = Uri.fromFile(file);
                    }
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    context.startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    private OnEnterClickListener onEnterClickListener;

    public interface OnEnterClickListener {
        void onEnter(boolean isMustUpdate);
    }

    public void setOnEnterClickListener(OnEnterClickListener onEnterClickListener) {
        this.onEnterClickListener = onEnterClickListener;
    }
}

