package com.cn.lv.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.rong.imkit.RongExtension;
import io.rong.imkit.fragment.ConversationFragment;

/**
 * @date 20/3/13 15:46
 * @des
 */
public class ChatFragment extends ConversationFragment {

    private RongExtension mRongExtension;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRongExtension = view.findViewById(io.rong.imkit.R.id.rc_extension);
        mRongExtension.getInputEditText();
        super.onViewCreated(view, savedInstanceState);
    }
}
