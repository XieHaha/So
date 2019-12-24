package com.cn.lv.ui.login.fragment;

import android.widget.EditText;

import com.cn.frame.ui.BaseFragment;
import com.cn.lv.R;

import butterknife.BindView;

/**
 * @author 顿顿
 */
public class HeightInfoFragment extends BaseFragment {
    @BindView(R.id.et_height)
    EditText etHeight;
    @BindView(R.id.et_weight)
    EditText etWeight;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_height;
    }

    public String getHeight() {
        return etHeight.getText().toString();
    }

    public String getWeight() {
        return etWeight.getText().toString();
    }

}
