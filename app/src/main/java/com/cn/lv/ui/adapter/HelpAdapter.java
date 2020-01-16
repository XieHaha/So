package com.cn.lv.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.frame.data.BaseData;
import com.cn.frame.data.bean.HelpBean;
import com.cn.lv.R;

import java.util.List;

public class HelpAdapter extends BaseQuickAdapter<HelpBean, BaseViewHolder> implements BaseData {

    public HelpAdapter(int layoutResId, @Nullable List<HelpBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HelpBean item) {
        helper.setText(R.id.tv_title, item.getTitle()).setText(R.id.tv_content, item.getReply());
    }

}
