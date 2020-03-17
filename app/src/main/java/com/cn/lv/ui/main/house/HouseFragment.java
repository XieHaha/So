package com.cn.lv.ui.main.house;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cn.frame.permission.Permission;
import com.cn.frame.ui.BaseFragment;
import com.cn.frame.utils.ScreenUtils;
import com.cn.frame.utils.SweetLog;
import com.cn.frame.widgets.AbstractOnPageChangeListener;
import com.cn.lv.R;
import com.cn.lv.SweetApplication;
import com.cn.lv.ui.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HouseFragment extends BaseFragment {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_center)
    TextView tvCenter;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.layout_title)
    LinearLayout layoutTitle;
    @BindView(R.id.view_bar)
    View viewBar;
    @BindView(R.id.tv_menu)
    TextView tvMenu;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private NearbyFragment nearbyFragment;
    private OnlineFragment onlineFragment;
    private RecentlyFragment recentlyFragment;
    /**
     * 定位
     */
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    /**
     * 坐标
     */
    private double lat = 0.0f, lng = 0.0f;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_house;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        permissionHelper.request(new String[]{Permission.FINE_LOCATION});
        initFragment();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    public void initListener() {
        super.initListener();
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new AbstractOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                int width = calcTitleBarWidth();
                int offset = (width / 3 - viewBar.getWidth()) / 2;
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
        nearbyFragment = new NearbyFragment();
        onlineFragment = new OnlineFragment();
        recentlyFragment = new RecentlyFragment();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(nearbyFragment);
        fragmentList.add(onlineFragment);
        fragmentList.add(recentlyFragment);
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
                tvCenter.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tvCenter.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tvRight.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case BASE_ONE:
                tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tvLeft.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvCenter.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                tvCenter.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tvRight.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case BASE_TWO:
                tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tvCenter.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tvCenter.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                tvRight.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
            default:
                break;
        }
        viewPager.setCurrentItem(index);
    }

    @OnClick({R.id.tv_left, R.id.tv_center, R.id.tv_right, R.id.tv_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                titleBar(0);
                break;
            case R.id.tv_center:
                titleBar(1);
                break;
            case R.id.tv_right:
                titleBar(2);
                break;
            case R.id.tv_menu:
                break;
            default:
                break;
        }
    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            String city = location.getCity();    //获取城市
            lat = location.getLatitude();
            lng = location.getLongitude();
            SweetApplication.getInstance().setLat(lat);
            SweetApplication.getInstance().setLng(lng);
            SweetApplication.getInstance().setCity(city);
            SweetLog.i(TAG, "house --->city:" + city + " Lat: " + lat + "  Lng: " + lng);
        }
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        mLocationClient = new LocationClient(getContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数

        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true

        option.setNeedNewVersionRgc(true);
        //可选，设置是否需要最新版本的地址信息。默认不需要，即参数为false

        mLocationClient.setLocOption(option);
        mLocationClient.start();
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        if (permissionName instanceof String[]) {
            if (isSamePermission(Permission.FINE_LOCATION, ((String[]) permissionName)[0])) {
                startLocation();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(myListener);
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
