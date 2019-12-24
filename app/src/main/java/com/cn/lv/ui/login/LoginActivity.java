package com.cn.lv.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.cn.frame.data.BaseData;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.Tasks;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseActivity;
import com.cn.lv.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/5/24 15:20
 * @description
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    private String phone, verifyCode;

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
        RequestUtils.login(this, "", phone, verifyCode, BaseData.ADMIN, this);
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
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            default:
                break;
        }
    }
}
