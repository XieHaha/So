package com.cn.lv.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.frame.data.BaseData;
import com.cn.frame.data.bean.RolesBean;
import com.cn.frame.utils.glide.GlideHelper;
import com.cn.lv.R;
import com.cn.lv.utils.ImageUrlUtil;

import java.util.List;

public class FollowMeAdapter extends BaseQuickAdapter<RolesBean, BaseViewHolder> implements BaseData {

    public FollowMeAdapter(int layoutResId, @Nullable List<RolesBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RolesBean item) {
        TextView name = helper.getView(R.id.tv_name);
        name.setText(item.getNickname());
        ImageView attention = helper.getView(R.id.iv_attention);
        ImageView ivHeader = helper.getView(R.id.iv_header);
        Glide.with(mContext).load(ImageUrlUtil.addTokenToUrl(item.getHead_portrait())).apply(GlideHelper.getOptionsPic()).into(ivHeader);
        //互相关注
        int att = item.getFollow_state();
        if (att == BASE_ONE) {
            attention.setSelected(false);
        } else {
            attention.setSelected(true);
        }
        helper.addOnClickListener(R.id.iv_attention)
                .addOnClickListener(R.id.iv_message);
    }

}
