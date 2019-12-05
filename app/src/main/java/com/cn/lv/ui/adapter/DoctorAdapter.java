package com.cn.lv.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.frame.data.bean.DoctorBean;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @description 居民适配器
 */
public class DoctorAdapter extends BaseQuickAdapter<DoctorBean, BaseViewHolder> {
    public DoctorAdapter(int layoutResId, @Nullable List<DoctorBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DoctorBean item) {
    }
}
