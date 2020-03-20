package com.cn.lv.ui.main.my;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chinapnr.android.adapay.AdaPay;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.CardInfoBean;
import com.cn.frame.data.bean.PaymentBean;
import com.cn.frame.data.bean.UserBaseBean;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.ToastUtil;
import com.cn.lv.R;
import com.cn.lv.SweetApplication;
import com.cn.lv.ui.adapter.CardInfoAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OneActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener, com.cn.frame.data.PayResult {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private CardInfoAdapter cardInfoAdapter;
    private List<CardInfoBean> cardInfoBeans = new ArrayList<>();

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
        return R.layout.act_one;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardInfoAdapter = new CardInfoAdapter(R.layout.item_card_info, cardInfoBeans);
        cardInfoAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(cardInfoAdapter);
        getCardInfo();
    }

    private void getCardInfo() {
        RequestUtils.getCardInfo(this, signSession(InterfaceName.GET_CARD_INFO), this);
    }

    private void upgradeMembership(int cardId, float cardMoney, long cardDuration) {
        RequestUtils.upgradeMembership(this, signSession(InterfaceName.UPGRADE_MEMBERSHIP),
                cardId, cardMoney, cardDuration, this);
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

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        CardInfoBean bean = cardInfoBeans.get(position);
        upgradeMembership(bean.getCard_id(), bean.getCard_money(), bean.getCard_duration());
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        cardInfoBeans.clear();
        if (task == Tasks.GET_CARD_INFO) {
            List<CardInfoBean> list = (List<CardInfoBean>) response.getData();
            if (list != null && list.size() > 0) {
                for (CardInfoBean bean : list) {
                    if (BASE_ONE == bean.getFlag()) {
                        cardInfoBeans.add(bean);
                    }
                }
            }
            cardInfoAdapter.setNewData(cardInfoBeans);
        } else if (task == Tasks.UPGRADE_MEMBERSHIP) {

        } else if (task == Tasks.LOGIN) {
            loginBean = (UserBaseBean) response.getData();
            //存储登录结果
            SweetApplication.getInstance().setLoginBean(loginBean);
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onResponseCode(Tasks task, BaseResponse response) {
        super.onResponseCode(task, response);
        if (task == Tasks.UPGRADE_MEMBERSHIP) {
            if (response.getCode() == 202) {
                PaymentBean paymentBean = (PaymentBean) response.getData();
                pay(new Gson().toJson(paymentBean));
            }
        }
    }

    private void pay(String url) {
        if (TextUtils.isEmpty(url)) {
            ToastUtil.toast(this, "发生未知错误，请稍候再试");
            return;
        }
        AdaPay.doPay(OneActivity.this, url, payResult -> {
            ToastUtil.toast(OneActivity.this, payResult.getResultMsg());
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
    }
}
