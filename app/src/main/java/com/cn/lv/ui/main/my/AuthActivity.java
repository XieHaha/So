package com.cn.lv.ui.main.my;

import com.cn.frame.ui.BaseActivity;
import com.cn.lv.R;

import butterknife.OnClick;

public class AuthActivity extends BaseActivity {
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
        return R.layout.act_auth;
    }


    @OnClick(R.id.iv_next)
    public void onViewClicked() {
    }
}
