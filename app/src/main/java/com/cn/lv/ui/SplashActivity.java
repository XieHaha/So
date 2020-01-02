package com.cn.lv.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.cn.frame.ui.BaseActivity;
import com.cn.lv.R;
import com.cn.lv.ui.login.LoginActivity;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * 启动界面
 *
 */
public class SplashActivity extends BaseActivity {
    @BindView(R.id.gif_image)
    GifImageView gifImage;
    private ScheduledExecutorService executorService;
    private int time = 0;

    @Override
    public int getLayoutID() {
        return R.layout.act_splash;
    }

    private Handler handler = new Handler(message -> {
        if (time <= 0) {
            startLoginPage();
        }
        return true;
    });

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        try {
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.mipmap.pic_splash_gif);
            initScheduledThread(gifDrawable.getDuration());
            gifImage.setImageDrawable(gifDrawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }


    /**
     * 跳转登录界面
     */
    private void startLoginPage() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
    }

    private void initScheduledThread(int duration) {
        time = Math.round(duration / 1000f) + 2;
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
}
