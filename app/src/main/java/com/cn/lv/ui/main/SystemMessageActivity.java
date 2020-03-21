package com.cn.lv.ui.main;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.cn.frame.ui.BaseActivity;
import com.cn.lv.R;

import io.rong.imkit.fragment.SubConversationListFragment;
import io.rong.imlib.model.Conversation;

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

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        FragmentManager fragmentManage = getSupportFragmentManager();
        SubConversationListFragment fragement = (SubConversationListFragment) fragmentManage.findFragmentById(R.id.subconversationlist);
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("subconversationlist")
                .appendQueryParameter("type", Conversation.ConversationType.PRIVATE.getName())
                .build();
        fragement.setUri(uri);
    }
}
