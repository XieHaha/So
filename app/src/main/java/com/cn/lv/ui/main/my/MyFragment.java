package com.cn.lv.ui.main.my;

import android.app.Activity;
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
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.VipDetailsBean;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseFragment;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.ToastUtil;
import com.cn.frame.utils.glide.GlideHelper;
import com.cn.frame.widgets.dialog.HintDialog;
import com.cn.lv.R;
import com.cn.lv.SweetApplication;
import com.cn.lv.utils.ImageUrlUtil;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MyFragment extends BaseFragment implements IChange<String> {
    private static final int REQUEST_CODE_AUTH = 100;
    private static final int REQUEST_CODE_VIP = 200;
    @BindView(R.id.iv_vip)
    ImageView ivVip;
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

    private long TEN_DAY = 60 * 60 * 24 * 1000;

    @Override
    public void onChange(String data) {
        updateFollowNum();
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_my;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        tvName.setText(userInfo.getNickname());
        tvSign.setText(userInfo.getIndividuality_signature());
        Glide.with(this).load(ImageUrlUtil.addTokenToUrl(userInfo.getHead_portrait())).apply(GlideHelper.getOptions(BaseUtils.dp2px(Objects.requireNonNull(getContext()), 4))).into(ivHeader);
        tvSex.setText(BASE_ONE == userInfo.getSex() ? R.string.txt_male : R.string.txt_female);
        tvAge.setText(String.valueOf(userInfo.getAge()));
        tvAddress.setText(userInfo.getAddress());
        tvJob.setText(dataDictBean.getOccupationInfo().get(userInfo.getOccupation()));
        tvFollowedNum.setText(String.valueOf(userInfo.getCollection_num()));
        tvFollowNum.setText(String.valueOf(userInfo.getAttention_num()));
        tvBrowseNum.setText(String.valueOf(userInfo.getVisitor_number()));
        int authState = userInfo.getIs_auth();
        if (authState == BASE_ONE) {
            ivVip.setImageResource(R.mipmap.pic_my_bg1);
        } else {
            VipDetailsBean detailsBean = loginBean.getVipDetails();
            if (detailsBean != null) {
                String endTime = detailsBean.getEnd_time_format();
                long time =
                        BaseUtils.date2TimeStamp(endTime, BaseUtils.YYYY_MM_DD_HH_MM_SS) - System.currentTimeMillis();
                if (time > TEN_DAY) {
                    ivVip.setVisibility(View.GONE);
                } else {
                    ivVip.setVisibility(View.VISIBLE);
                }
                if (TextUtils.equals(detailsBean.getUsage_state(), "2")) {
                    layoutName.setBackgroundResource(R.drawable.corner5_211d1d_bg);
                    tvName.setSelected(true);
                    vip.setVisibility(View.VISIBLE);
                } else {
                    layoutName.setBackground(null);
                    vip.setVisibility(View.GONE);
                    tvName.setSelected(false);
                }
            }
            ivVip.setImageResource(R.mipmap.pic_my_bg);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        iNotifyChangeListenerServer.registerFollowNumChangeListener(this, RegisterType.REGISTER);
    }

    private void signOut() {
        RequestUtils.signOut(getContext(), BaseUtils.signSpan(getContext(),
                userInfo.getMobile_number(), loginBean.getSession_id(), InterfaceName.SIGN_OUT)
                , this);
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
            R.id.iv_vip, R.id.layout_set, R.id.layout_help, R.id.layout_about, R.id.layout_black,
            R.id.layout_exit})
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
                    startActivityForResult(new Intent(getContext(), AuthActivity.class),
                            REQUEST_CODE_AUTH);
                } else {
                    startActivityForResult(new Intent(getContext(), VipActivity.class),
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
            case R.id.layout_exit:
                new HintDialog(getContext()).setTitleString(getString(R.string.APP_NAME))
                        .setContentString(getString(R.string.txt_exit_sure))
                        .setEnterBtnTxt(getString(R.string.txt_exit))
                        .setEnterSelect(true)
                        .setOnEnterClickListener(this::signOut)
                        .show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.SIGN_OUT) {
            exit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_AUTH) {
            update();
        } else if (requestCode == REQUEST_CODE_VIP) {
            ToastUtil.toast(getContext(), "最贵的vip");
        }
    }

    /**
     * 认证后更新
     */
    private void update() {
        loginBean = SweetApplication.getInstance().getLoginBean();
        userInfo = loginBean.getUserInfo();
        int authState = userInfo.getIs_auth();
        if (authState == BASE_ONE) {
            ivVip.setImageResource(R.mipmap.pic_my_bg1);
        } else {
            ivVip.setImageResource(R.mipmap.pic_my_bg);
        }
        tvName.setText(userInfo.getNickname());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        iNotifyChangeListenerServer.registerFollowNumChangeListener(this, RegisterType.UNREGISTER);
    }

}