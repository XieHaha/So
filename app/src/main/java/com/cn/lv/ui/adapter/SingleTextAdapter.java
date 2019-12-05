package com.cn.lv.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des
 */
public class SingleTextAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SingleTextAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
    }

}
