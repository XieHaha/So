package com.cn.lv.ui.main.my;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.CardInfoBean;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseActivity;
import com.cn.lv.R;
import com.cn.lv.ui.adapter.CardInfoAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class VipActivity extends BaseActivity {
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
        return R.layout.act_vip;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardInfoAdapter = new CardInfoAdapter(R.layout.item_card_info, cardInfoBeans);
        recyclerView.setAdapter(cardInfoAdapter);
        getCardInfo();
    }

    private void getCardInfo() {
        RequestUtils.getCardInfo(this, signSession(InterfaceName.GET_CARD_INFO), this);
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
        }
    }
}
