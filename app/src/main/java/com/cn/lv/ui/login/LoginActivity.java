package com.cn.lv.ui.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;

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

import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/5/24 15:20
 * @description
 */
public class LoginActivity extends BaseActivity {
    private ScheduledExecutorService executorService;
    private String phone, verifyCode;
    /**
     * 验证码计时
     */
    private int time = 0;
    /**
     * 协议更新时间
     */
    private String ptotocolUpdateTime;
    /**
     * 是否获取过验证码
     */
    private boolean isSendVerifyCode = false;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_login;
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (time <= 0) {
            } else {
            }
            return true;
        }
    });

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            ptotocolUpdateTime = getIntent().getStringExtra(CommonData.KEY_PUBLIC_STRING);
        }
        phone = sharePreferenceUtil.getAlwaysString(CommonData.KEY_LOGIN_ACCOUNT);
    }

    @Override
    public void initListener() {
        super.initListener();
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


    /**
     * 验证码再次获取倒计时
     */
    private void startVerifyCodeTimer() {
        time = BaseData.BASE_MAX_RESEND_TIME;
        executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern(
                        "yht-thread-pool-%d").daemon(true).build());
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

    @OnClick({R.id.tv_login_obtain_code, R.id.tv_login_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login_obtain_code:
                break;
            case R.id.tv_login_next:
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}
