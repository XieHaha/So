package com.cn.lv.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cn.frame.data.CommonData;
import com.cn.frame.data.InfoType;
import com.cn.frame.ui.BaseActivity;
import com.cn.lv.R;
import com.cn.lv.ui.login.fragment.InterestInfoFragment;
import com.cn.lv.ui.login.fragment.SexInfoFragment;
import com.cn.lv.ui.login.fragment.WhoInfoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterInfoActivity extends BaseActivity implements InfoType {
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.frame_content)
    FrameLayout frameContent;

    private FragmentManager manager;
    private SexInfoFragment sexInfoFragment;
    private WhoInfoFragment whoInfoFragment;
    private InterestInfoFragment interestInfoFragment;
    private List<Fragment> fragments;
    /**
     * 默认性别
     */
    private int info = SEX;
    private int sex, who, interest;
    /**
     * 背景图
     */
    private int[] images = {R.mipmap.pic_sex, R.mipmap.pic_who, R.mipmap.pic_interest};

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
        whoInfoFragment = new WhoInfoFragment();
        interestInfoFragment = new InterestInfoFragment();
        fragments.add(sexInfoFragment);
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
                sex = sexInfoFragment.getType();
                updatePage(true);
                break;
            case WHO:
                who = whoInfoFragment.getType();
                updatePage(true);
                break;
            case INTEREST:
                interest = interestInfoFragment.getType();
                Intent intent = new Intent();
                intent.putExtra(CommonData.KEY_INTENT_BOOLEAN, true);
                intent.putExtra(CommonData.KEY_SEX, sex);
                intent.putExtra(CommonData.KEY_WHO, who);
                intent.putExtra(CommonData.KEY_INTEREST, interest);
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
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
            replaceFragment(fragments.get(info));
        } else {
            info--;
        }
        ivBg.setImageResource(images[info]);
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
