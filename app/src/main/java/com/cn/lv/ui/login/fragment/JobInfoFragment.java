package com.cn.lv.ui.login.fragment;

import android.widget.EditText;

import com.cn.frame.ui.BaseFragment;
import com.cn.lv.R;

import butterknife.BindView;

/**
 * @author 顿顿
 */
public class JobInfoFragment extends BaseFragment {
    @BindView(R.id.et_job)
    EditText etJob;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_job;
    }

    public String getJob() {
        return etJob.getText().toString();
    }

}
