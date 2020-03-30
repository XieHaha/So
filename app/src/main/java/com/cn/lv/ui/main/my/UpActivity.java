package com.cn.lv.ui.main.my;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.chinapnr.android.adapay.AdaPay;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.PaymentBean;
import com.cn.frame.data.bean.UserBaseBean;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.ToastUtil;
import com.cn.lv.R;
import com.cn.lv.SweetApplication;
import com.google.gson.Gson;

import butterknife.OnClick;

public class UpActivity extends BaseActivity implements com.cn.frame.data.PayResult {

    private PaymentBean paymentBean;

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
        return R.layout.act_up;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            paymentBean = (PaymentBean) getIntent().getSerializableExtra(CommonData.KEY_PUBLIC);
        }
        if (paymentBean == null) {
            edit();
        }
    }

    private void edit() {
        RequestUtils.edit(this, signSession(InterfaceName.AUTH), this);
    }

    /**
     * 登录
     */
    private void login() {
        String pwd = sharePreferenceUtil.getAlwaysString(CommonData.KEY_LOGIN_PWD);
        RequestUtils.login(this, BaseUtils.signSpan(this, userInfo.getMobile_number(),
                InterfaceName.SIGN_IN), pwd,
                String.valueOf(SweetApplication.getInstance().getLat()),
                String.valueOf(SweetApplication.getInstance().getLng()), this);
    }

    @OnClick(R.id.iv_next)
    public void onViewClicked() {
        if (paymentBean == null) {
            ToastUtil.toast(this, "发生未知错误，请稍候再试");
            return;
        }
        if (!BaseUtils.isAliPayInstalled(this)) {
            ToastUtil.toast(this, "无法调用支付宝，请确认是否安装支付宝！");
            return;
        }
        try {
            AdaPay.doPay(UpActivity.this, new Gson().toJson(paymentBean), payResult -> {
                ToastUtil.toast(UpActivity.this, payResult.getResultMsg());
                //处理支付结果
                String code = payResult.getResultCode();
                switch (code) {
                    case ORDER_SUCCESS:
                        login();
                        break;
                    case ORDER_FAILED:
                        break;
                    case ORDER_PAYING:
                        break;
                    case ORDER_CANCEL:
                        break;
                    case ORDER_PARAM_ERROR:
                        break;
                    case ORDER_NETWORK_ERROR:
                        break;
                    case ORDER_OTHER_ERROR:
                        break;
                    default:
                        break;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.toast(this, "发生未知错误，请稍候再试");
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.LOGIN) {
            loginBean = (UserBaseBean) response.getData();
            //存储登录结果
            SweetApplication.getInstance().setLoginBean(loginBean);
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onResponseCode(Tasks task, BaseResponse response) {
        if (response.getCode() == 202) {
            if (task == Tasks.AUTH) {
                paymentBean = (PaymentBean) response.getData();
            }
        }
    }
}
