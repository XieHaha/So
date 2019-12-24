package com.cn.lv.ui.login.fragment;

import android.widget.EditText;

import com.cn.frame.ui.BaseFragment;
import com.cn.lv.R;

import butterknife.BindView;

/**
 * @创建者 顿顿
 */
public class NicknameInfoFragment extends BaseFragment {

    @BindView(R.id.et_nickname)
    EditText etNickname;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_nickname;
    }

    public String getNickname() {
        return etNickname.getText().toString();
    }

}
