package com.cn.lv.ui.main.my;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.cn.frame.data.CommonData;
import com.cn.frame.ui.BaseActivity;
import com.cn.lv.R;

import butterknife.BindView;

public class ProtocolActivity extends BaseActivity {
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;

    private String content;
    private boolean isProtocol;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    protected boolean isInitStatusBar() {
        return false;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_protocol;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            isProtocol = getIntent().getBooleanExtra(CommonData.KEY_INTENT_BOOLEAN, false);
            content = getIntent().getStringExtra(CommonData.KEY_PUBLIC_STRING);
        }

        if (isProtocol) {
            publicTitleBarTitle.setText(R.string.txt_protocol);
        } else {
            publicTitleBarTitle.setText(R.string.txt_pri);
        }
        tvContent.setText(content);
    }

}
