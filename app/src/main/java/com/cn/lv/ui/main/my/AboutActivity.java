package com.cn.lv.ui.main.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.cn.frame.data.BaseNetConfig;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.AboutUsBean;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.widgets.dialog.HintDialog;
import com.cn.lv.R;
import com.cn.lv.ui.WebViewActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.tv_app_name)
    TextView tvAppName;
    @BindView(R.id.tv_version)
    TextView tvVersion;

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
    }

    @OnClick({R.id.layout_pri, R.id.layout_protocol, R.id.layout_contact})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layout_pri:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(CommonData.KEY_TITLE, getString(R.string.txt_pri));
                intent.putExtra(CommonData.KEY_PUBLIC,
                        BaseNetConfig.BASE_BASIC_PRIVATE_PROTOCOL_URL);
                startActivity(intent);
                break;
            case R.id.layout_protocol:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(CommonData.KEY_TITLE, getString(R.string.txt_protocol));
                intent.putExtra(CommonData.KEY_PUBLIC, BaseNetConfig.BASE_BASIC_USER_PROTOCOL_URL);
                startActivity(intent);
                break;
            case R.id.layout_contact:
                HintDialog dialog =
                        new HintDialog(this).setTitleString(R.string.txt_official_service)
                                .setContentString(String.format(getString(R.string.txt_official_contact),
                                        aboutUsBean.getClient_customer_qq(),
                                        aboutUsBean.getClient_customer_wechat(),
                                        aboutUsBean.getClient_customer_service()))
                                .setCancelBtnGone(true);
                dialog.show();
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
