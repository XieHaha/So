package com.cn.lv.ui.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.cn.frame.data.CommonData;
import com.cn.frame.ui.BaseFragment;
import com.cn.frame.utils.SweetLog;
import com.cn.lv.R;
import com.cn.lv.ui.main.UserInfoActivity;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.model.Conversation;

public class MessageFragment extends BaseFragment {

    private ConversationListFragment mConversationListFragment;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_message;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(),
                        "false")
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(),
                        "false")
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")
                .build();
        mConversationListFragment.setUri(uri);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, mConversationListFragment);
        transaction.commit();
    }

    @Override
    public void initListener() {
        super.initListener();

        RongIM.setConversationListBehaviorListener(new RongIM.ConversationListBehaviorListener() {
            @Override
            public boolean onConversationPortraitClick(Context context,
                                                       Conversation.ConversationType conversationType, String targetId) {
                Intent intent = new Intent(getContext(), UserInfoActivity.class);
                SweetLog.i(TAG, "id:" + targetId);
                intent.putExtra(CommonData.KEY_PUBLIC, Integer.valueOf(targetId));
                context.startActivity(intent);
                return true;
            }

            @Override
            public boolean onConversationPortraitLongClick(Context context,
                                                           Conversation.ConversationType conversationType, String targetId) {
                return false;
            }

            @Override
            public boolean onConversationLongClick(Context context, View view,
                                                   UIConversation conversation) {
                return false;
            }

            @Override
            public boolean onConversationClick(Context context, View view,
                                               UIConversation conversation) {
                return false;
            }
        });


    }

    public void setConversationListFragment(ConversationListFragment mConversationListFragment) {
        this.mConversationListFragment = mConversationListFragment;
    }
}
