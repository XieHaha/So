package com.cn.lv.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.Tasks;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.ToastUtil;
import com.cn.lv.R;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    private String phone, pwd;

    @Override
    protected boolean isInitStatusBar() {
        return false;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_login;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        phone = sharePreferenceUtil.getAlwaysString(CommonData.KEY_LOGIN_ACCOUNT);
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    /**
     * 登录
     */
    private void login() {
        RequestUtils.login(this, BaseUtils.signSpan(this, phone, InterfaceName.signIn), pwd, this);
    }


    @OnClick({R.id.tv_forgot_pwd, R.id.tv_register, R.id.layout_login_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forgot_pwd:
                startActivity(new Intent(this, RegisterAndModifyPwdActivity.class));
                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterInfoActivity.class));
                break;
            case R.id.layout_login_next:
                phone = etPhone.getText().toString().trim();
                if (!BaseUtils.isMobileNumber(phone)) {
                    ToastUtil.toast(this, R.string.txt_input_phone_hint);
                    return;
                }
                pwd = etPwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtil.toast(this, R.string.txt_input_pwd_hint);
                    return;
                }
                login();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case LOGIN:
                break;
            default:
                break;
        }
    }
}
