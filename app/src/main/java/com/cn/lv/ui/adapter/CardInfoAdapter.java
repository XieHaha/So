package com.cn.lv.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.frame.data.BaseData;
import com.cn.frame.data.bean.CardInfoBean;
import com.cn.lv.R;

import java.text.DecimalFormat;
import java.util.List;

public class CardInfoAdapter extends BaseQuickAdapter<CardInfoBean, BaseViewHolder> implements BaseData {

    public CardInfoAdapter(int layoutResId, @Nullable List<CardInfoBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CardInfoBean item) {
        helper.setText(R.id.tv_time, item.getCard_duration2())
                .setText(R.id.tv_total_price, "￥" + item.getCard_money())
                .setText(R.id.tv_unit_price, item.getCard_name()).addOnClickListener(R.id.iv_one);
    }

    private String getUnitPrice(float cardMoney, long cardDuration) {
        int day = (int) (cardDuration / (60 * 60 * 24));
        return String.format(mContext.getString(R.string.txt_unit_price),
                getPrice(cardMoney / day));
    }

    /**
     * 保留两位小数
     */
    private static String getPrice(float price) {
        try {
            //#.00 表示两位小数
            DecimalFormat df = new DecimalFormat("#0.00");
            return df.format(price / 100f);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
