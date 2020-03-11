package com.cn.lv.ui.main;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.widget.TextView;

import com.cn.frame.data.CommonData;
import com.cn.frame.ui.BaseActivity;
import com.cn.lv.R;

import butterknife.BindView;
import io.rong.imkit.fragment.ConversationFragment;

public class ChatActivity extends BaseActivity {

    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    private String title, targetId;

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
        return R.layout.act_chat;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            targetId = getIntent().getStringExtra(CommonData.KEY_CHAT_ID);
            title = getIntent().getStringExtra(CommonData.KEY_CHAT_TITLE);
            if (TextUtils.isEmpty(title)) {
                title = getIntent().getData().getQueryParameter("title");
            }
            if (TextUtils.isEmpty(targetId)) {
                targetId = getIntent().getData().getQueryParameter("targetId");
            }
            publicTitleBarTitle.setText(title);
        }
        FragmentManager fragmentManage = getSupportFragmentManager();
        ConversationFragment fragement =
                (ConversationFragment) fragmentManage.findFragmentById(R.id.conversation);
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath("private")
                .appendQueryParameter("title", title)
                .appendQueryParameter("targetId", targetId).build();
        fragement.setUri(uri);
    }

}
