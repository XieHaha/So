package com.cn.lv.ui.main.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.cn.frame.api.ApiManager;
import com.cn.frame.ui.BaseFragment;
import com.cn.lv.R;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @description 好友
 */
public class FriendsFragment extends BaseFragment {
    /**
     * 居民列表
     */
    private PatientFragment patientFragment;
    /**
     * 医生列表
     */
    private DoctorFragment doctorFragment;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_friends;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
    }

    @Override
    public void initListener() {
        super.initListener();
    }

}
