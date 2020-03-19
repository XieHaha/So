package com.cn.lv.ui.main;

import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.frame.api.ApiManager;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.DataDictBean;
import com.cn.frame.data.bean.UserBaseBean;
import com.cn.frame.data.bean.VersionBean;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.SweetLog;
import com.cn.frame.widgets.AbstractOnPageChangeListener;
import com.cn.lv.R;
import com.cn.lv.SweetApplication;
import com.cn.lv.ui.adapter.ViewPagerAdapter;
import com.cn.lv.ui.dialog.UpdateDialog;
import com.cn.lv.ui.main.attention.FollowFragment;
import com.cn.lv.ui.main.fragment.MessageFragment;
import com.cn.lv.ui.main.house.HouseFragment;
import com.cn.lv.ui.main.my.MyFragment;
import com.cn.lv.utils.BadgeUtils;
import com.cn.lv.version.ConstantsVersionMode;
import com.cn.lv.version.presenter.VersionPresenter;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;


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
    @BindView(R.id.layout_receiving_transfer_num)
    RelativeLayout layout;
    @BindView(R.id.tv_receiving_transfer_num)
    TextView tvMessageNum;
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
    protected boolean isInitStatusBar() {
        return false;
    }

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
    protected void onResume() {
        super.onResume();
        updateMessage();
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
        initFragment();
        //融云
        loginChat();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //检查更新
        if (!hideVersionUpdate) {
            mVersionPresenter = new VersionPresenter(this, "");
            mVersionPresenter.setVersionViewListener(this);
            //            mVersionPresenter.init();
        }
        updateSession();
        updateDataDict();
    }

    @Override
    public void initListener() {
        super.initListener();
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new AbstractOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                selectTab(position);
            }
        });
    }

    /**
     * 获取基础数据集合
     */
    private void getBasicsInfo() {
        RequestUtils.getBasicsInfo(this, BaseUtils.signSpan(this, userInfo.getMobile_number(),
                loginBean.getSession_id(), InterfaceName.GET_BASICS_INFO), this);
    }

    /**
     * 更新
     */
    private void renewSign() {
        RequestUtils.renewSign(this, BaseUtils.signSpan(this, userInfo.getMobile_number(),
                loginBean.getSession_id(), InterfaceName.RENEW_SIGN), this);
    }

    /**
     * 未读消息
     */
    private void updateMessage() {
        RongIM.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                if (integer > 0) {
                    layout.setVisibility(View.VISIBLE);
                    tvMessageNum.setText(integer > 99 ? "99+" : String.valueOf(integer));
                } else {
                    layout.setVisibility(View.INVISIBLE);
                }

                if (BadgeUtils.setCount(integer, MainActivity.this)) {
                    SweetLog.i(TAG, "设置成功");
                } else {
                    SweetLog.i(TAG, "设置失败");
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                layout.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * 基础数据字典
     */
    private void updateDataDict() {
        if (dataDictBean == null) {
            getBasicsInfo();
        }
    }

    /**
     * 计算过期时间
     */
    private void updateSession() {
        if (loginBean != null) {
            //提前30分钟更新 -1800
            long time = loginBean.getDue_time() - System.currentTimeMillis() / 1000 - 1800;
            if (time <= 0) {
                handler.sendEmptyMessage(0);
            } else {
                initScheduledThread(time);
            }
        }
    }


    /**
     * 登录融云聊天
     */
    private void loginChat() {
        if (userInfo != null) {
            SweetLog.i(TAG, "connect...");
            RongIM.connect(userInfo.getRong_cloud_token(), new RongIMClient.ConnectCallbackEx() {
                @Override
                public void OnDatabaseOpened(RongIMClient.DatabaseOpenStatus code) {

                }

                @Override
                public void onTokenIncorrect() {
                    SweetLog.i(TAG, "connect token error");
                }

                @Override
                public void onSuccess(String s) {
                    SweetLog.i(TAG, "connect  success：" + s);
                    updateMessage();
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {
                    SweetLog.i(TAG, "connect  error:" + e.getMessage());
                }
            });
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
        ConversationListFragment mConversationListFragment = new ConversationListFragment();
        messageFragment.setConversationListFragment(mConversationListFragment);
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
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_BASICS_INFO:
                DataDictBean bean = (DataDictBean) response.getData();
                SweetApplication.getInstance().setDataDictBean(bean);
                break;
            case RENEW_SIGN:
                UserBaseBean userBaseBean = (UserBaseBean) response.getData();
                loginBean.setDue_time(userBaseBean.getDue_time());
                loginBean.setSession_id(userBaseBean.getSession_id());
                SweetApplication.getInstance().setLoginBean(loginBean);
                updateSession();
                break;
            default:
                break;
        }
    }

    private ScheduledExecutorService executorService;
    private Handler handler = new Handler(message -> {
        //刷新
        renewSign();
        return true;
    });


    private void initScheduledThread(long time) {
        executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("sweet").daemon(true).build());
        executorService.scheduleAtFixedRate(() -> {
            handler.sendEmptyMessage(0);
            executorService.shutdownNow();
        }, time, time, TimeUnit.SECONDS);
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
