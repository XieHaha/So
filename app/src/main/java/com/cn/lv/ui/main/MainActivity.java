package com.cn.lv.ui.main;

import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.cn.frame.api.ApiManager;
import com.cn.frame.data.bean.VersionBean;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.widgets.AbstractOnPageChangeListener;
import com.cn.lv.R;
import com.cn.lv.ui.adapter.ViewPagerAdapter;
import com.cn.lv.ui.dialog.UpdateDialog;
import com.cn.lv.ui.main.fragment.FollowFragment;
import com.cn.lv.ui.main.fragment.HouseFragment;
import com.cn.lv.ui.main.fragment.MessageFragment;
import com.cn.lv.ui.main.fragment.MyFragment;
import com.cn.lv.version.ConstantsVersionMode;
import com.cn.lv.version.presenter.VersionPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity
        implements VersionPresenter.VersionViewListener, UpdateDialog.OnEnterClickListener {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.act_main_tab1)
    LinearLayout actMainTab1;
    @BindView(R.id.act_main_tab2)
    LinearLayout actMainTab2;
    @BindView(R.id.act_main_tab3)
    LinearLayout actMainTab3;
    @BindView(R.id.act_main_tab4)
    LinearLayout actMainTab4;
    /**
     * 首页碎片
     */
    private HouseFragment houseFragment;
    /**
     * 版本检测
     */
    private VersionPresenter mVersionPresenter;
    /**
     * 版本弹窗
     */
    private UpdateDialog updateDialog;
    private NotificationManager mNotificationManager;
    private Bitmap largeIcon = null;
    /**
     * 是否检查版本更新
     */
    private boolean hideVersionUpdate;
    private int pendingCount = 1;

    @Override
    public void beforeCreateView(@NonNull Bundle savedInstanceState) {
        super.beforeCreateView(savedInstanceState);
        //必须在super 之前调用,不然无效。因为那时候fragment已经被恢复了。
        if (savedInstanceState != null) {
            // FRAGMENTS_TAG
            savedInstanceState.remove("android:support:fragments");
            savedInstanceState.remove("android:fragments");
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_main;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
        initFragment();
        //环信登录
        loginEaseChat();
        //检查更新
        if (!hideVersionUpdate) {
            mVersionPresenter = new VersionPresenter(this, "");
            mVersionPresenter.setVersionViewListener(this);
            mVersionPresenter.init();
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new AbstractOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                selectTab(position);
            }
        });
    }


    /**
     * 登录环信聊天
     */
    private void loginEaseChat() {
        if (loginBean != null) {
        }
    }

    @OnClick({R.id.act_main_tab1, R.id.act_main_tab2, R.id.act_main_tab3, R.id.act_main_tab4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.act_main_tab1:
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.act_main_tab2:
                viewPager.setCurrentItem(1, false);
                break;
            case R.id.act_main_tab3:
                viewPager.setCurrentItem(2, false);
                break;
            case R.id.act_main_tab4:
                viewPager.setCurrentItem(3, false);
                break;
            default:
                break;
        }
    }

    /**
     * 碎片初始化
     */
    private void initFragment() {
        houseFragment = new HouseFragment();
        //关注碎片
        FollowFragment followFragment = new FollowFragment();
        //消息碎片
        MessageFragment messageFragment = new MessageFragment();
        //我的碎片
        MyFragment myFragment = new MyFragment();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(houseFragment);
        fragmentList.add(followFragment);
        fragmentList.add(messageFragment);
        fragmentList.add(myFragment);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        selectTab(0);
    }

    /**
     * 选中tab
     */
    private void selectTab(int index) {
        switch (index) {
            case BASE_ZERO:
                actMainTab1.setSelected(true);
                actMainTab2.setSelected(false);
                actMainTab3.setSelected(false);
                actMainTab4.setSelected(false);
                break;
            case BASE_ONE:
                actMainTab1.setSelected(false);
                actMainTab2.setSelected(true);
                actMainTab3.setSelected(false);
                actMainTab4.setSelected(false);
                break;
            case BASE_TWO:
                actMainTab1.setSelected(false);
                actMainTab2.setSelected(false);
                actMainTab3.setSelected(true);
                actMainTab4.setSelected(false);
                break;
            case BASE_THREE:
                actMainTab1.setSelected(false);
                actMainTab2.setSelected(false);
                actMainTab3.setSelected(false);
                actMainTab4.setSelected(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (houseFragment != null) {
            houseFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        if (houseFragment != null) {
            houseFragment.onPermissionNeedExplanation(permissionName);
        }
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        if (houseFragment != null) {
            houseFragment.onNoPermissionNeeded(permissionName);
        }
    }

    /*********************版本更新回调*************************/
    @Override
    public void updateVersion(VersionBean version, int mode, boolean isDownNewApk) {
        if (mode <= ConstantsVersionMode.UPDATE_OTHER) {
            return;
        }
        updateDialog = new UpdateDialog(this);
        updateDialog.setCancelable(false);
        updateDialog.setUpdateMode(mode)
                .setIsDownNewAPK(isDownNewApk)
                .setTitle(version.getTitle())
                .setData(version.getNotes());
        updateDialog.setOnEnterClickListener(this);
        updateDialog.show();
    }

    @Override
    public void updateLoading(long total, long current) {
        if (updateDialog != null && updateDialog.isShowing()) {
            updateDialog.setProgressValue(total, current);
        }
    }

    /**
     * 当无可用网络时回调
     */
    @Override
    public void updateNetWorkError() {
    }

    @Override
    public void onEnter(boolean isMustUpdate) {
        mVersionPresenter.getNewAPK(isMustUpdate);
    }

    /**
     * 返回键 后台运行,如果前一个activity未finish  会导致无法返回到后台
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
