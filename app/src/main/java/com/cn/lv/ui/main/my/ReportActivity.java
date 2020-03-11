package com.cn.lv.ui.main.my;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.Tasks;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.ToastUtil;
import com.cn.frame.widgets.dialog.DownDialog;
import com.cn.frame.widgets.dialog.listener.OnMediaItemClickListener;
import com.cn.lv.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 举报
 */
public class ReportActivity extends BaseActivity implements OnMediaItemClickListener {
    @BindView(R.id.et_title)
    TextView etTitle;
    @BindView(R.id.et_content)
    EditText etContent;

    private ArrayList<String> data;

    private int userId;
    private String title, content;

    @Override
    protected boolean isInitStatusBar() {
        return false;
    }

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }


    @Override
    public int getLayoutID() {
        return R.layout.act_report;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            userId = getIntent().getIntExtra(CommonData.KEY_PUBLIC, -1);
        }
        data = new ArrayList<>();
        data.add("语言暴力");
        data.add("性别不符合");
        data.add("广告欺诈");
        data.add("淫秽色情");
        data.add("政治反动");
        data.add("其他内容");
    }

    /**
     * 提交
     */
    private void commit() {
        RequestUtils.report(this, signSession(InterfaceName.REPORT), title, content, this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.REPORT) {
            ToastUtil.toast(this, response.getMsg());
            finish();
        }
    }

    @OnClick({R.id.et_title, R.id.iv_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_title:
                new DownDialog(this).setData(data)
                        .setOnMediaItemClickListener(R.id.et_title, this).show();
                break;
            case R.id.iv_commit:
                title = etTitle.getText().toString().trim();
                content = etContent.getText().toString().trim();

                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
                    ToastUtil.toast(this, "内容不能为空");
                    return;
                }
                commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onMediaItemClick(int type, int position) {
        etTitle.setText(data.get(position));
    }
}
