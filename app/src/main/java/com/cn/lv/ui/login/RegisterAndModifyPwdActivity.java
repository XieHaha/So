package com.cn.lv.ui.login;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.frame.data.BaseData;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.Tasks;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseActivity;
import com.cn.lv.R;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 */
public class RegisterAndModifyPwdActivity extends BaseActivity {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_verify)
    EditText etVerify;
    @BindView(R.id.tv_get_verify_code)
    TextView tvGetVerifyCode;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_pwd_again)
    EditText etPwdAgain;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title1)
    TextView tvTitle1;
    @BindView(R.id.tv_title2)
    TextView tvTitle2;
    @BindView(R.id.layout_register_hint)
    LinearLayout layoutRegisterHint;
    private ScheduledExecutorService executorService;
    private String phone, verifyCode;
    /**
     * 验证码计时
     */
    private int time = 0;
    /**
     * 是否获取过验证码
     */
    private boolean isSendVerifyCode = false;
    /**
     * true为注册；false为忘记密码
     */
    private boolean mode;

    @Override
    protected boolean isInitStatusBar() {
        return false;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_register_and_modify_pwd;
    }

    private Handler handler = new Handler(message -> {
        if (time <= 0) {
        } else {
        }
        return true;
    });

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            mode = getIntent().getBooleanExtra(CommonData.KEY_INTENT_BOOLEAN, false);
        }

        if (mode) {
            ivBack.setVisibility(View.GONE);
            tvTitle1.setText(R.string.txt_hello);
            tvTitle2.setVisibility(View.VISIBLE);
            layoutRegisterHint.setVisibility(View.VISIBLE);
        } else {
            ivBack.setVisibility(View.VISIBLE);
            tvTitle1.setText(R.string.txt_find_pwd);
            tvTitle2.setVisibility(View.GONE);
            layoutRegisterHint.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        RequestUtils.getVerifyCode(this, phone, BaseData.ADMIN, this);
    }

    /**
     * 登录
     */
    private void login() {
        RequestUtils.login(this, "", phone, verifyCode, BaseData.ADMIN, this);
    }

    @OnClick({R.id.iv_back, R.id.tv_get_verify_code, R.id.layout_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_get_verify_code:
                break;
            case R.id.layout_next:
                break;
            default:
                break;
        }
    }

    /**
     * 验证码再次获取倒计时
     */
    private void startVerifyCodeTimer() {
        time = BaseData.BASE_MAX_RESEND_TIME;
        executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("social").daemon(true).build());
        executorService.scheduleAtFixedRate(() -> {
            time--;
            if (time < 0) {
                time = 0;
                executorService.shutdownNow();
            } else {
                handler.sendEmptyMessage(0);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }


    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}
