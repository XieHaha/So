package com.cn.lv.ui.login.fragment;

import android.view.View;
import android.widget.ImageView;

import com.cn.frame.ui.BaseFragment;
import com.cn.lv.R;

import butterknife.BindView;
import butterknife.OnClick;

public class InterestInfoFragment extends BaseFragment {
    @BindView(R.id.iv_male_bg)
    ImageView ivMaleBg;
    @BindView(R.id.iv_female_bg)
    ImageView ivFemaleBg;
    @BindView(R.id.iv_neutral_bg)
    ImageView ivNeutralBg;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_interest;
    }

    @OnClick({R.id.layout_male, R.id.layout_female, R.id.layout_neutral})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_male:
                ivMaleBg.setVisibility(View.VISIBLE);
                ivFemaleBg.setVisibility(View.INVISIBLE);
                ivNeutralBg.setVisibility(View.INVISIBLE);
                break;
            case R.id.layout_neutral:
                ivMaleBg.setVisibility(View.INVISIBLE);
                ivFemaleBg.setVisibility(View.INVISIBLE);
                ivNeutralBg.setVisibility(View.VISIBLE);
                break;
            case R.id.layout_female:
                ivMaleBg.setVisibility(View.INVISIBLE);
                ivFemaleBg.setVisibility(View.VISIBLE);
                ivNeutralBg.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }
}
