package com.cn.lv.ui.main.attention;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.frame.ui.BaseFragment;
import com.cn.frame.utils.ScreenUtils;
import com.cn.frame.widgets.AbstractOnPageChangeListener;
import com.cn.lv.R;
import com.cn.lv.ui.adapter.ViewPagerAdapter;
import com.cn.lv.ui.main.fragment.IFollowFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FollowFragment extends BaseFragment {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.layout_title)
    LinearLayout layoutTitle;
    @BindView(R.id.view_bar)
    View viewBar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_attention;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        initFragment();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    public void initListener() {
        super.initListener();
        viewPager.addOnPageChangeListener(new AbstractOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                int width = calcTitleBarWidth();
                int offset = (width / 2 - viewBar.getWidth()) / 2;
                int screenWidth = ScreenUtils.getScreenWidth(getContext());
                viewBar.setTranslationX((screenWidth - width) / 2f + (position * viewBar.getWidth() + offset + position * offset * 2) +
                        (positionOffset * (offset * 2 + viewBar.getWidth())));
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                titleBar(position);
            }
        });
    }


    /**
     * 碎片初始化
     */
    private void initFragment() {
        FollowMeFragment followMeFragment = new FollowMeFragment();
        IFollowFragment iFollowFragment = new IFollowFragment();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(followMeFragment);
        fragmentList.add(iFollowFragment);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(),
                fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        titleBar(BASE_ZERO);
    }

    /**
     * titlebar处理
     */
    private void titleBar(int index) {
        switch (index) {
            case BASE_ZERO:
                tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                tvLeft.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tvRight.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case BASE_ONE:
                tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tvLeft.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                tvRight.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
            default:
                break;
        }
        viewPager.setCurrentItem(index);
    }

    @OnClick({R.id.tv_left, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                titleBar(0);
                break;
            case R.id.tv_right:
                titleBar(1);
                break;
            case R.id.tv_menu:
                break;
            default:
                break;
        }
    }

    /**
     * 计算游标位移量
     */
    private int calcTitleBarWidth() {
        //获取控件宽度
        return layoutTitle.getMeasuredWidth();
    }

}
