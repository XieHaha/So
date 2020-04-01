package com.cn.lv.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.UserBaseBean;
import com.cn.frame.data.bean.UserInfoBean;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.listener.ResponseListener;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.SharePreferenceUtil;
import com.cn.lv.R;
import com.cn.lv.SweetApplication;
import com.cn.lv.ui.login.LoginActivity;
import com.cn.lv.ui.main.MainActivity;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 启动界面
 */
public class SplashActivity extends AppCompatActivity implements ResponseListener<BaseResponse> {
    private ScheduledExecutorService executorService;
    /**
     * 轻量级存储
     */
    protected SharePreferenceUtil sharePreferenceUtil;
    private UserBaseBean loginBean;
    private UserInfoBean userInfo;
    private int time = 0;
    private Handler handler = new Handler(message -> {
        if (time <= 0) {
            jump();
        }
        return true;
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        sharePreferenceUtil = new SharePreferenceUtil(this);
        loginBean = SweetApplication.getInstance().getLoginBean();
        if (loginBean != null) {
            userInfo = loginBean.getUserInfo();
        }
        updateSession();
    }

    /**
     * 登录
     */
    private void login() {
        String pwd = sharePreferenceUtil.getAlwaysString(CommonData.KEY_LOGIN_PWD);
        RequestUtils.login(this, BaseUtils.signSpan(this, userInfo.getMobile_number(),
                InterfaceName.SIGN_IN), pwd,
                "", "", this);
    }


    /**
     * 计算过期时间
     */
    private void updateSession() {
        if (loginBean != null) {
            long time = loginBean.getDue_time() - System.currentTimeMillis() / 1000;
            if (time <= 0) {
                //已过期就重新登录来更新session
                login();
            } else {
                initScheduledThread();
            }
        } else {
            initScheduledThread();
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        if (task == Tasks.LOGIN) {
            loginBean = (UserBaseBean) response.getData();
            //存储登录结果
            SweetApplication.getInstance().setLoginBean(loginBean);
        }
    }

    @Override
    public void onResponseError(Tasks task, Exception e) {

    }

    @Override
    public void onResponseCode(Tasks task, BaseResponse response) {

    }

    @Override
    public void onResponseStart(Tasks task) {

    }

    @Override
    public void onResponseCancel(Tasks task) {

    }

    @Override
    public void onResponseEnd(Tasks task) {
        initScheduledThread();
    }

    private void jump() {
        if (isEmpty()) {
            startLoginPage();
        } else {
            startMainPage();
        }
    }

    private boolean isEmpty() {
        if (loginBean == null) {
            return true;
        } else {
            if (TextUtils.isEmpty(loginBean.getUserInfo().getHead_portrait())
                    || TextUtils.isEmpty(loginBean.getUserInfo().getNickname())
                    || TextUtils.isEmpty(loginBean.getUserInfo().getIndividuality_signature())) {
                return true;
            } else {
                return false;
            }
        }
    }

    private void startMainPage() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
    }

    /**
     * 跳转登录界面
     */
    private void startLoginPage() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
    }

    private void initScheduledThread() {
        time = 2;
        executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("sweet").daemon(true).build());
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
}
