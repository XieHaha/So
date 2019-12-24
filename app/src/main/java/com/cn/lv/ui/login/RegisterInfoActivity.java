package com.cn.lv.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cn.frame.data.CommonData;
import com.cn.frame.data.InfoType;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.ToastUtil;
import com.cn.lv.R;
import com.cn.lv.ui.login.fragment.AddressInfoFragment;
import com.cn.lv.ui.login.fragment.BirthdayInfoFragment;
import com.cn.lv.ui.login.fragment.HeaderInfoFragment;
import com.cn.lv.ui.login.fragment.HeightInfoFragment;
import com.cn.lv.ui.login.fragment.InterestInfoFragment;
import com.cn.lv.ui.login.fragment.JobInfoFragment;
import com.cn.lv.ui.login.fragment.NicknameInfoFragment;
import com.cn.lv.ui.login.fragment.SexInfoFragment;
import com.cn.lv.ui.login.fragment.WhoInfoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 */
public class RegisterInfoActivity extends BaseActivity implements InfoType {
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.frame_content)
    FrameLayout frameContent;

    private FragmentManager manager;
    private SexInfoFragment sexInfoFragment;
    private NicknameInfoFragment nicknameInfoFragment;
    private BirthdayInfoFragment birthdayInfoFragment;
    private HeightInfoFragment heightInfoFragment;
    private HeaderInfoFragment headerInfoFragment;
    private JobInfoFragment jobInfoFragment;
    private AddressInfoFragment addressInfoFragment;
    private WhoInfoFragment whoInfoFragment;
    private InterestInfoFragment interestInfoFragment;
    private List<Fragment> fragments;
    /**
     * 默认性别
     */
    private int info = SEX;
    /**
     * 背景图
     */
    private int images[] = {R.mipmap.pic_sex, R.mipmap.pic_nickname, R.mipmap.pic_birthday,
            R.mipmap.pic_height, R.mipmap.pic_job, R.mipmap.pic_address, R.mipmap.pic_header,
            R.mipmap.pic_who, R.mipmap.pic_interest};

    @Override
    protected boolean isInitStatusBar() {
        return false;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_register_info;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        manager = getSupportFragmentManager();
        initFragment();
        replaceFragment(fragments.get(info));
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        sexInfoFragment = new SexInfoFragment();
        nicknameInfoFragment = new NicknameInfoFragment();
        birthdayInfoFragment = new BirthdayInfoFragment();
        heightInfoFragment = new HeightInfoFragment();
        jobInfoFragment = new JobInfoFragment();
        addressInfoFragment = new AddressInfoFragment();
        headerInfoFragment = new HeaderInfoFragment();
        whoInfoFragment = new WhoInfoFragment();
        interestInfoFragment = new InterestInfoFragment();
        fragments.add(sexInfoFragment);
        fragments.add(nicknameInfoFragment);
        fragments.add(birthdayInfoFragment);
        fragments.add(heightInfoFragment);
        fragments.add(jobInfoFragment);
        fragments.add(addressInfoFragment);
        fragments.add(headerInfoFragment);
        fragments.add(whoInfoFragment);
        fragments.add(interestInfoFragment);
    }

    @OnClick({R.id.iv_back, R.id.layout_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.layout_next:
                infoPage();
                break;
            default:
                break;
        }
    }

    private void infoPage() {
        switch (info) {
            case SEX:
                break;
            case NICKNAME:
                if (nicknameInfoFragment != null) {
                    String nickname = nicknameInfoFragment.getNickname();
                    if (TextUtils.isEmpty(nickname)) {
                        ToastUtil.toast(this, "昵称不能为空");
                        return;
                    }
                }
                break;
            case BIRTHDAY:
                break;
            case HEIGHT:
                if (heightInfoFragment != null) {
                    String height = heightInfoFragment.getHeight();
                    String weight = heightInfoFragment.getWeight();
                    if (TextUtils.isEmpty(height)) {
                        ToastUtil.toast(this, "身高不能为空");
                        return;
                    }
                    if (TextUtils.isEmpty(weight)) {
                        ToastUtil.toast(this, "体重不能为空");
                        return;
                    }
                }
                break;
            case JOB:
                if (jobInfoFragment != null) {
                    String job = jobInfoFragment.getJob();
                    if (TextUtils.isEmpty(job)) {
                        ToastUtil.toast(this, "职业不能为空");
                        return;
                    }
                }
                break;
            case ADDRESS:
                if (addressInfoFragment != null) {
                    String address = addressInfoFragment.getAddress();
                    if (TextUtils.isEmpty(address)) {
                        ToastUtil.toast(this, "常住地址不能为空");
                        return;
                    }
                }
                break;
            case HEADER:
                if (headerInfoFragment != null) {
                    String address = headerInfoFragment.getHeader();
                    if (TextUtils.isEmpty(address)) {
                        ToastUtil.toast(this, "头像不能为空");
                        return;
                    }
                }
                break;
            case WHO:
                break;
            case INTEREST:
                Intent intent = new Intent(this, RegisterAndModifyPwdActivity.class);
                intent.putExtra(CommonData.KEY_INTENT_BOOLEAN, true);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        updatePage(true);
    }

    protected final void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_content, fragment);
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }


    private void updatePage(boolean add) {
        if (add) {
            info++;
        } else {
            info--;
        }
        ivBg.setImageResource(images[info]);
        if (add) {
            replaceFragment(fragments.get(info));
        }
    }

    /**
     * info 回退
     */
    private void goBack() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            updatePage(false);
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }
}
