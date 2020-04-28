package com.cn.lv.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.InfoType;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.UserBaseBean;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.permission.Permission;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.ToastUtil;
import com.cn.frame.utils.glide.GlideHelper;
import com.cn.lv.R;
import com.cn.lv.SweetApplication;
import com.cn.lv.utils.MatisseUtils;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterBaseActivity extends BaseActivity implements InfoType {
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.et_nickname)
    EditText etNickname;
    @BindView(R.id.layout_next)
    LinearLayout layoutNext;

    private String name;
    private File headerFile;

    @Override
    protected boolean isInitStatusBar() {
        return false;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_register_base;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }


    /**
     * 登录
     */
    private void login() {
        String phone;
        String pwd = sharePreferenceUtil.getAlwaysString(CommonData.KEY_LOGIN_PWD);
        if (userInfo == null) {
            phone = sharePreferenceUtil.getAlwaysString(CommonData.KEY_LOGIN_ACCOUNT);
        } else {
            phone = userInfo.getMobile_number();
        }
        RequestUtils.login(this, BaseUtils.signSpan(this, phone, InterfaceName.SIGN_IN), pwd,
                String.valueOf(SweetApplication.getInstance().getLat()),
                String.valueOf(SweetApplication.getInstance().getLng()), this);
    }

    @OnClick({R.id.iv_header, R.id.layout_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_header:
                permissionHelper.request(new String[]{Permission.CAMERA, Permission.STORAGE_WRITE});
                break;
            case R.id.layout_next:
                name = etNickname.getText().toString().trim();
                if (TextUtils.isEmpty(name) || headerFile == null) {
                    ToastUtil.toast(this, "请完善信息");
                    return;
                }
                RequestUtils.edit(this, signSession(InterfaceName.EDIT_USER_INFO), name,
                        headerFile, this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.EDIT_USER_INFO) {
            login();
        } else if (task == Tasks.LOGIN) {
            loginBean = (UserBaseBean) response.getData();
            //存储登录结果
            SweetApplication.getInstance().setLoginBean(loginBean);
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        List<String> paths;
        if (requestCode == RC_PICK_IMG) {
            paths = Matisse.obtainPathResult(data);
            if (paths != null && paths.size() > 0) {
                headerFile = new File(paths.get(0));
                Glide.with(this).load(paths.get(0)).apply(GlideHelper.getOptionsPic()).into(ivHeader);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        if (permissionName instanceof String[]) {
            if (isSamePermission(Permission.CAMERA, ((String[]) permissionName)[0])) {
                openPhoto();
            }
        }
    }

    /**
     * 打开图片库
     */
    private void openPhoto() {
        MatisseUtils.open(this, true, 1);
    }
}
