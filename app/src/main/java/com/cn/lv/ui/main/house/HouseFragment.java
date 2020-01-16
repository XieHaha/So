package com.cn.lv.ui.main.house;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.frame.permission.Permission;
import com.cn.frame.ui.BaseFragment;
import com.cn.frame.utils.ScreenUtils;
import com.cn.frame.utils.SweetLog;
import com.cn.frame.widgets.AbstractOnPageChangeListener;
import com.cn.lv.R;
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
    private LocationManager manager;
    /**
     * 坐标
     */
    private float latitude = 0.0f, longitude = 0.0f;

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

    /**
     * 开始定位
     */
    @SuppressLint("MissingPermission")
    private void startLocation() {
        manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Location location = this.manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = (float) location.getLatitude();
                longitude = (float) location.getLongitude();
            }
        } else {
            LocationListener locationListener = new LocationListener() {
                // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                // Provider被enable时触发此函数，比如GPS被打开
                @Override
                public void onProviderEnabled(String provider) {

                }

                // Provider被disable时触发此函数，比如GPS被关闭
                @Override
                public void onProviderDisabled(String provider) {

                }

                //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        SweetLog.i(TAG, "Location changed : Lat: "
                                + location.getLatitude() + " Lng: "
                                + location.getLongitude());
                    }
                }
            };
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0,
                    locationListener);
            Location location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = (float) location.getLatitude();
                longitude = (float) location.getLongitude();
            }
        }
        SweetLog.i(TAG, "Location  : Lat: " + latitude + " Lng: " + longitude);
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        if (permissionName instanceof String[]) {
            if (isSamePermission(Permission.FINE_LOCATION, ((String[]) permissionName)[0])) {
                startLocation();
            }
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
