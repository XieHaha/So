package com.cn.lv.ui.main.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.frame.api.ApiManager;
import com.cn.frame.api.notify.IChange;
import com.cn.frame.api.notify.RegisterType;
import com.cn.frame.data.bean.VipDetailsBean;
import com.cn.frame.ui.BaseFragment;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.glide.GlideHelper;
import com.cn.lv.R;
import com.cn.lv.SweetApplication;
import com.cn.lv.utils.ImageUrlUtil;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MyFragment extends BaseFragment implements IChange<String> {
    private static final int REQUEST_CODE_AUTH = 100;
    private static final int REQUEST_CODE_VIP = 200;
    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_job)
    TextView tvJob;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_followed_num)
    TextView tvFollowedNum;
    @BindView(R.id.tv_follow_num)
    TextView tvFollowNum;
    @BindView(R.id.tv_browse_num)
    TextView tvBrowseNum;
    @BindView(R.id.vip)
    ImageView vip;
    @BindView(R.id.iv_verify_green)
    ImageView ivVerifyGreen;
    @BindView(R.id.layout_name)
    RelativeLayout layoutName;

    private static final long TEN_DAY = 60 * 60 * 24 * 1000 * 10;

    @Override
    public void onChange(String data) {
        updateFollowNum();
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_my;
    }

    @Override
    public void onResume() {
        super.onResume();
        bindData();
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
    }

    @Override
    public void initListener() {
        super.initListener();
        iNotifyChangeListenerServer.registerFollowNumChangeListener(this, RegisterType.REGISTER);
    }

    private void bindData() {
        userInfo = getLoginBean().getUserInfo();
        tvName.setText(userInfo.getNickname());
        tvSign.setText(userInfo.getIndividuality_signature());
        Glide.with(this).load(ImageUrlUtil.addTokenToUrl(userInfo.getHead_portrait())).apply(GlideHelper.getOptions(BaseUtils.dp2px(Objects.requireNonNull(getContext()), 4))).into(ivHeader);
        tvSex.setText(BASE_ONE == userInfo.getSex() ? R.string.txt_male : R.string.txt_female);
        tvSex.setSelected(BASE_ONE != userInfo.getSex());
        tvAge.setText(userInfo.getAge() + "岁");
        tvAddress.setText(SweetApplication.getInstance().getCity());
        if (dataDictBean != null) {
            tvJob.setText(dataDictBean.getOccupationInfo().get(userInfo.getOccupation()));
        }
        tvFollowedNum.setText(String.valueOf(userInfo.getCollection_num()));
        tvFollowNum.setText(String.valueOf(userInfo.getAttention_num()));
        tvBrowseNum.setText(String.valueOf(userInfo.getVisitor_number()));
        uiStatus();
    }

    private void uiStatus() {
        int authState = userInfo.getIs_auth();
        if (authState == BASE_ONE) {
            tvText.setText("去认证>>");
            ivVerifyGreen.setVisibility(View.GONE);
        } else {
            tvText.setText("升级成为VIP>>");
            ivVerifyGreen.setVisibility(View.VISIBLE);
            VipDetailsBean detailsBean = loginBean.getVipDetails();
            if (detailsBean != null) {
                String endTime = detailsBean.getEnd_time_format();
                long time =
                        BaseUtils.date2TimeStamp(endTime, BaseUtils.YYYY_MM_DD_HH_MM_SS) - System.currentTimeMillis();
                if (TextUtils.equals(detailsBean.getUsage_state(), "2")) {
                    layoutName.setBackgroundResource(R.drawable.corner5_211d1d_bg);
                    tvName.setSelected(true);
                    vip.setVisibility(View.VISIBLE);
                    if (time > TEN_DAY) {
                        tvText.setText("已经是高级会员");
                    } else {
                        tvText.setText("升级续费");
                    }
                } else {
                    layoutName.setBackground(null);
                    vip.setVisibility(View.GONE);
                    tvName.setSelected(false);
                }
            }
        }
    }

    /**
     * 更新
     */
    private void updateFollowNum() {
        loginBean = SweetApplication.getInstance().getLoginBean();
        userInfo = loginBean.getUserInfo();
        tvFollowNum.setText(String.valueOf(userInfo.getAttention_num()));
    }

    @OnClick({R.id.tv_edit, R.id.layout_followed, R.id.layout_follow, R.id.layout_browse,
            R.id.iv_vip, R.id.layout_set, R.id.layout_help, R.id.layout_about, R.id.layout_black})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_edit:
                startActivityForResult(new Intent(getContext(), PersonalActivity.class),
                        REQUEST_CODE_AUTH);
                break;
            case R.id.layout_followed:
                break;
            case R.id.layout_follow:
                break;
            case R.id.layout_browse:
                break;
            case R.id.iv_vip:
                if (userInfo.getIs_auth() == BASE_ONE) {
                    startActivityForResult(new Intent(getContext(), UpActivity.class),
                            REQUEST_CODE_AUTH);
                } else {
                    startActivityForResult(new Intent(getContext(), OneActivity.class),
                            REQUEST_CODE_VIP);
                }
                break;
            case R.id.layout_set:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.layout_help:
                startActivity(new Intent(getContext(), HelpActivity.class));
                break;
            case R.id.layout_about:
                startActivity(new Intent(getContext(), AboutActivity.class));
                break;
            case R.id.layout_black:
                startActivity(new Intent(getContext(), BlackActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        iNotifyChangeListenerServer.registerFollowNumChangeListener(this, RegisterType.UNREGISTER);
    }

}