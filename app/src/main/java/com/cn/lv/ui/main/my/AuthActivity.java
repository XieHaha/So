package com.cn.lv.ui.main.my;

import android.view.View;

import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.ToastUtil;
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

    @OnClick({R.id.iv_one, R.id.iv_two, R.id.iv_three})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_one:
                ToastUtil.toast(this, "开通");
                break;
            case R.id.iv_two:
                break;
            case R.id.iv_three:
                break;
            default:
                break;
        }
    }
}
