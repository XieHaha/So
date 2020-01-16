package com.cn.lv.ui.main.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.Tasks;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseFragment;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.glide.GlideHelper;
import com.cn.frame.widgets.dialog.HintDialog;
import com.cn.lv.R;
import com.cn.lv.utils.FileUrlUtil;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MyFragment extends BaseFragment {
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

    @Override
    public int getLayoutID() {
        return R.layout.fragment_my;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        tvName.setText(userInfo.getNickname());
        tvSign.setText(userInfo.getIndividuality_signature());
        Glide.with(this).load(FileUrlUtil.addTokenToUrl(userInfo.getHead_portrait())).apply(GlideHelper.getOptions(BaseUtils.dp2px(Objects.requireNonNull(getContext()), 4))).into(ivHeader);
        tvSex.setText(BASE_ONE == userInfo.getSex() ? R.string.txt_male : R.string.txt_female);
        tvAge.setText(String.valueOf(userInfo.getAge()));
        tvAddress.setText(userInfo.getAddress());
        tvJob.setText(dataDictBean.getOccupationInfo().get(userInfo.getOccupation()));
        tvFollowedNum.setText(String.valueOf(userInfo.getCollection_num()));
        tvFollowNum.setText(String.valueOf(userInfo.getAttention_num()));
        tvBrowseNum.setText(String.valueOf(userInfo.getVisitor_number()));
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    private void signOut() {
        RequestUtils.signOut(getContext(), BaseUtils.signSpan(getContext(),
                userInfo.getMobile_number(), loginBean.getSession_id(), InterfaceName.SIGN_OUT)
                , this);
    }

    @OnClick({R.id.tv_edit, R.id.layout_followed, R.id.layout_follow, R.id.layout_browse,
            R.id.iv_vip, R.id.layout_set, R.id.layout_help, R.id.layout_about, R.id.layout_black,
            R.id.layout_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_edit:
                break;
            case R.id.layout_followed:
                break;
            case R.id.layout_follow:
                break;
            case R.id.layout_browse:
                break;
            case R.id.iv_vip:
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
        switch (task) {
            case SIGN_OUT:
                exit();
                break;
            default:
                break;
        }
    }
}
