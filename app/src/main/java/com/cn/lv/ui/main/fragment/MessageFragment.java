package com.cn.lv.ui.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.cn.frame.data.CommonData;
import com.cn.frame.ui.BaseFragment;
import com.cn.lv.R;
import com.cn.lv.ui.main.UserInfoActivity;
import com.cn.lv.ui.main.my.AuthActivity;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

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
                try {
                    Intent intent = new Intent(getContext(), UserInfoActivity.class);
                    intent.putExtra(CommonData.KEY_PUBLIC, targetId);
                    context.startActivity(intent);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
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
                if (userInfo.getIs_auth() == 1) {
                    startActivity(new Intent(getContext(), AuthActivity.class));
                    return true;
                }
                return false;
            }
        });

        RongIM.setConversationClickListener(new RongIM.ConversationClickListener() {
            @Override
            public boolean onUserPortraitClick(Context context,
                                               Conversation.ConversationType conversationType,
                                               UserInfo userInfo, String s) {
                if (TextUtils.equals(userInfo.getUserId(), loginBean.getUserInfo().getUser_id())) {
                    return true;
                }
                try {
                    Intent intent = new Intent(getContext(), UserInfoActivity.class);
                    intent.putExtra(CommonData.KEY_PUBLIC, userInfo.getUserId());
                    context.startActivity(intent);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context,
                                                   Conversation.ConversationType conversationType
                    , UserInfo userInfo, String s) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });
    }

    public void setConversationListFragment(ConversationListFragment mConversationListFragment) {
        this.mConversationListFragment = mConversationListFragment;
    }
}
