package com.cn.lv.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.frame.data.BaseData;
import com.cn.frame.data.bean.DataDictBean;
import com.cn.frame.data.bean.RolesBean;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.glide.GlideHelper;
import com.cn.lv.R;
import com.cn.lv.SweetApplication;
import com.cn.lv.utils.ImageUrlUtil;
import com.cn.lv.utils.TimeUtil;

import java.util.List;
import java.util.Objects;

public class RecentlyAdapter extends BaseQuickAdapter<RolesBean, BaseViewHolder> implements BaseData {
    private DataDictBean dataDictBean;

    public RecentlyAdapter(int layoutResId, @Nullable List<RolesBean> data) {
        super(layoutResId, data);
        dataDictBean = SweetApplication.getInstance().getDataDictBean();
    }


    @Override
    protected void convert(BaseViewHolder helper, RolesBean item) {
        RelativeLayout layout = helper.getView(R.id.layout);
        TextView name = helper.getView(R.id.tv_name);
        name.setText(item.getNickname());
        ImageView ivVip = helper.getView(R.id.iv_vip);
        ImageView attention = helper.getView(R.id.iv_attention);
        ImageView ivHeader = helper.getView(R.id.iv_header);
        Glide.with(mContext).load(ImageUrlUtil.addTokenToUrl(item.getHead_portrait())).apply(GlideHelper.getOptions(BaseUtils.dp2px(Objects.requireNonNull(mContext), 4))).into(ivHeader);
        TextView tvSex = helper.getView(R.id.tv_sex);
        TextView tvTime = helper.getView(R.id.tv_time);
        tvSex.setText(BASE_ONE == item.getSex() ? R.string.txt_male : R.string.txt_female);
        tvSex.setSelected(BASE_ONE != item.getSex());
        helper.setText(R.id.tv_age, String.valueOf(item.getAge()))
                .setText(R.id.tv_sign, item.getIndividuality_signature())
                .setText(R.id.tv_job, dataDictBean.getOccupationInfo().get(item.getOccupation()));
        int vip = item.getUsage_state();
        if (vip == BASE_ONE) {
            layout.setBackground(null);
            ivVip.setVisibility(View.GONE);
            name.setSelected(false);
        } else {
            layout.setBackgroundResource(R.drawable.corner5_211d1d_bg);
            ivVip.setVisibility(View.VISIBLE);
            name.setSelected(true);
        }
        //关注
        int att = item.getCollection_state();
        if (att == BASE_ONE) {
            attention.setSelected(true);
        } else {
            attention.setSelected(false);
        }
        helper.addOnClickListener(R.id.iv_attention);

        if (item.getAttribute() == 2) {
            helper.setText(R.id.tv_address, SweetApplication.getInstance().getCity());
            tvTime.setText("当前在线");
        } else {
            helper.setText(R.id.tv_address, item.getLocation());
            tvTime.setText(TimeUtil.getTimeString(item.getLast_login_timestamp() * 1000));
        }
    }

}
