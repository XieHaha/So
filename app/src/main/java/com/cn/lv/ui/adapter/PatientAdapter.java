package com.cn.lv.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.frame.data.bean.PatientBean;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 居民适配器
 */
public class PatientAdapter extends BaseQuickAdapter<PatientBean, BaseViewHolder> {
    public PatientAdapter(int layoutResId, @Nullable List<PatientBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PatientBean item) {
    }
}
