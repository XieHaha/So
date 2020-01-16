package com.cn.lv.ui.main.my;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.AboutUsBean;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseActivity;
import com.cn.lv.R;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.tv_app_name)
    TextView tvAppName;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;

    private AboutUsBean aboutUsBean;

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
        return R.layout.act_about;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        aboutUs();
    }

    private void aboutUs() {
        RequestUtils.aboutUs(this, signSession(InterfaceName.ABOUT_US), this);
    }

    private void bindData() {
        tvAppName.setText(aboutUsBean.getClient_app_name());
        tvVersion.setText(aboutUsBean.getClient_app_version());
        tvName.setText(aboutUsBean.getClient_nickname());
        tvPhone.setText(aboutUsBean.getClient_customer_service());
    }

    @OnClick({R.id.layout_pri, R.id.layout_protocol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_pri:
                break;
            case R.id.layout_protocol:
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.ABOUT_US) {
            aboutUsBean = (AboutUsBean) response.getData();
            bindData();
        }
    }
}
