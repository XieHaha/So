package com.cn.lv.ui.login.fragment;

import android.widget.EditText;

import com.cn.frame.ui.BaseFragment;
import com.cn.lv.R;

import butterknife.BindView;

/**
 * @author 顿顿
 */
public class AddressInfoFragment extends BaseFragment {
    @BindView(R.id.et_address)
    EditText etAddress;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_address;
    }

    public String getAddress() {
        return etAddress.getText().toString();
    }

}
