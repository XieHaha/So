package com.cn.lv.ui.main.my;

import android.app.Activity;
import android.content.Intent;

import com.cn.frame.ui.BaseActivity;
import com.cn.lv.R;

import butterknife.OnClick;

public class AuthActivity extends BaseActivity {
    private static final int REQUEST_CODE_AUTH = 100;

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
        startActivityForResult(new Intent(this, PersonalActivity.class), REQUEST_CODE_AUTH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_AUTH) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
