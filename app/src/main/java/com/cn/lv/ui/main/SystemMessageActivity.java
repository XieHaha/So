package com.cn.lv.ui.main;

import com.cn.frame.ui.BaseActivity;
import com.cn.lv.R;

/**
 * @date 20/3/20 15:30
 * @des
 */
public class SystemMessageActivity extends BaseActivity {
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
        return R.layout.act_system_message;
    }
}
