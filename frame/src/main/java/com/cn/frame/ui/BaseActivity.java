package com.cn.frame.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.frame.R;
import com.cn.frame.api.notify.INotifyChangeListenerServer;
import com.cn.frame.data.BaseData;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.DataDictBean;
import com.cn.frame.data.bean.UserBaseBean;
import com.cn.frame.data.bean.UserInfoBean;
import com.cn.frame.http.listener.ResponseListener;
import com.cn.frame.permission.OnPermissionCallback;
import com.cn.frame.permission.Permission;
import com.cn.frame.permission.PermissionHelper;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.RsaUtils;
import com.cn.frame.utils.SharePreferenceUtil;
import com.cn.frame.utils.StatusBarUtil;
import com.cn.frame.utils.SweetLog;
import com.cn.frame.utils.ToastUtil;
import com.cn.frame.widgets.dialog.HintDialog;
import com.cn.frame.widgets.dialog.LoadingDialog;
import com.google.gson.Gson;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;

/**
 * activity基类
 *
 * @author DUNDUN
 */
public abstract class BaseActivity extends RxAppCompatActivity
        implements UiInterface, BaseData, ResponseListener<BaseResponse>, View.OnClickListener,
        OnPermissionCallback {
    public static final String TAG = "SWEET";
    /**
     * load view
     */
    private LoadingDialog loadingView;
    /**
     * 登录数据
     */
    protected UserBaseBean loginBean;
    protected UserInfoBean userInfo;
    protected DataDictBean dataDictBean;
    /**
     * 轻量级存储
     */
    protected SharePreferenceUtil sharePreferenceUtil;
    /**
     * 监听器
     */
    public INotifyChangeListenerServer iNotifyChangeListenerServer;
    /**
     * 权限管理类
     */
    protected PermissionHelper permissionHelper;
    private boolean isRequest = true;
    private boolean isRequestLocation = true;
    private boolean isRequestPhone = true;
    private boolean isRequestCamera = true;
    private boolean isRequestRecord = true;
    /**
     * 选择图片
     */
    public static final int RC_PICK_IMG = 0x0001;
    /**
     * 拍照
     */
    public static final int RC_PICK_CAMERA = RC_PICK_IMG + 1;
    /**
     * 图片  裁剪
     */
    public static final int RC_CROP_IMG = RC_PICK_CAMERA + 1;
    /**
     * 返回按钮对象
     */
    protected ImageView backBtn;
    /**
     * 标题
     */
    private TextView tvTitle;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        beforeCreateView(savedInstanceState);
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        int layoutID = getLayoutID();
        if (layoutID != 0) {
            setContentView(layoutID);
        } else {
            setContentView(getLayoutView());
        }
        ButterKnife.bind(this);
        loginBean = getLoginBean();
        if (loginBean != null) {
            userInfo = loginBean.getUserInfo();
        }
        dataDictBean = getDataDictBean();
        sharePreferenceUtil = new SharePreferenceUtil(this);
        //权限管理类
        permissionHelper = PermissionHelper.getInstance(this);
        init(savedInstanceState);
    }

    /**
     * 方法回调顺序
     * 1.initView
     * 3.initData
     * 4.initListener
     */
    private void init(@NonNull Bundle savedInstanceState) {
        initBaseViews();
        if (isInitBackBtn()) {
            initBackBtn();
        }
        initStatusBar(isInitStatusBar());
        initView(savedInstanceState);
        initData(savedInstanceState);
        initListener();
        runOnUiThread(this::fillNetWorkData);
    }

    private void initBaseViews() {
        try {
            backBtn = findViewById(R.id.public_title_bar_back);
            tvTitle = findViewById(R.id.public_title_bar_title);
        } catch (Exception e) {
            SweetLog.e(getClass().getSimpleName(), e.getMessage());
        }
    }

    /**
     * 初始化back按钮事件，及title名称赋值
     */
    private void initBackBtn() {
        try {
            backBtn.setVisibility(View.VISIBLE);
            backBtn.setOnClickListener(v -> {
                hideSoftInputFromWindow();
                finish();
            });
            tvTitle.setSelected(true);
            tvTitle.setText(getTitle().toString());
        } catch (Exception e) {
            SweetLog.e(getClass().getSimpleName(), e.getMessage());
        }
    }

    /**
     * 状态栏处理
     */
    private void initStatusBar(boolean white) {
        StatusBarUtil.statuBarLightMode(this, white);
    }

    /**
     * 是否初始化返回按钮
     *
     * @return 如果不想baseactivity自动设置监听返回按钮的话就传回null，
     * 系统则不会自动设置监听,但是会初始化控件
     */
    protected boolean isInitBackBtn() {
        return false;
    }

    protected boolean isInitStatusBar() {
        return true;
    }

    private void initLoadingView() {
        loadingView = new LoadingDialog(this);
    }

    /**
     * 显示进度条
     */
    public void showLoadingView() {
        showLoadingView(true);
    }

    /**
     * 显示进度条
     */
    public void showLoadingView(final boolean cancel) {
        runOnUiThread(() -> {
            if (loadingView == null) {
                initLoadingView();
            }
            loadingView.setCancelable(cancel);
            loadingView.setCanceledOnTouchOutside(cancel);
            if (!loadingView.isShowing()) {
                loadingView.show();
            }
        });
    }

    /**
     * 关闭进度条
     */
    public void closeLoadingView() {
        runOnUiThread(() -> {
            if (loadingView == null) {
                return;
            }
            if (!loadingView.isShowing()) {
                return;
            }
            loadingView.setCancelable(true);
            loadingView.setCanceledOnTouchOutside(true);
            loadingView.dismiss();
        });
    }

    /**
     * 获取sign
     */
    public String signSession(String interfaceName) {
        return RsaUtils.encryptData(BaseUtils.signSpan(this, userInfo.getMobile_number(),
                loginBean.getSession_id(), interfaceName));
    }

    /**
     * 初始化login数据
     */
    public UserBaseBean getLoginBean() {
        String userStr = (String) SharePreferenceUtil.getObject(this, CommonData.KEY_LOGIN_BEAN,
                "");
        if (!TextUtils.isEmpty(userStr)) {
            loginBean = new Gson().fromJson(userStr, UserBaseBean.class);
        }
        return loginBean;
    }

    public DataDictBean getDataDictBean() {
        String data = (String) SharePreferenceUtil.getObject(this, CommonData.KEY_DATA_DICT_BEAN,
                "");
        if (!TextUtils.isEmpty(data)) {
            dataDictBean = new Gson().fromJson(data, DataDictBean.class);
        }
        return dataDictBean;
    }

    /**
     * 获取状态栏高度,在页面还没有显示出来之前
     */
    public static int getStateBarHeight(Activity a) {
        int result = 0;
        int resourceId = a.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = a.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 修改状态栏为全透明
     */
    @TargetApi(19)
    @Deprecated
    public void transparencyBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputFromWindow() {
        Activity activity = AppManager.getInstance().getCurrentActivity();
        if (activity == null || activity.getCurrentFocus() == null ||
                activity.getCurrentFocus().getWindowToken() == null) {
            return;
        }
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * 打开软键盘
     */
    public void showSoftInputFromWindow(View editText) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    /**
     * 退出登录
     */
    public void exit() {
        Intent intent = new Intent(BaseData.BASE_SIGN_OUT_ACTION);
        intent.setPackage(getPackageName());
        sendBroadcast(intent);
    }

    /**
     * token失效
     */
    public void token(String errorHint) {
        Intent intent = new Intent(BaseData.BASE_TOKEN_ERROR_ACTION);
        intent.putExtra(CommonData.KEY_PUBLIC_STRING, errorHint);
        intent.setPackage(getPackageName());
        sendBroadcast(intent);
    }

    /**
     * 账号禁用
     */
    public void accountError() {
        Intent intent = new Intent(BaseData.BASE_ACCOUNT_ERROR_ACTION);
        intent.setPackage(getPackageName());
        sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().removeActivity(this);
    }

    @Override
    public void onClick(View v) {
    }

    /**
     * 默认不适用此方法，在子类里可以重构他
     */
    @Override
    public View getLayoutView() {
        return null;
    }
    //=====================setContentView 前回调

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        permissionHelper.request(new String[]{Permission.FINE_LOCATION, Permission.STORAGE_WRITE});
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
    }

    @Override
    public void initListener() {
    }

    @Override
    public void fillNetWorkData() {
    }

    @Override
    public void beforeCreateView(@NonNull Bundle savedInstanceState) {
    }

    /**
     * ==============================网络回调
     */
    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
    }

    @Override
    public void onResponseCode(Tasks task, BaseResponse response) {
        ToastUtil.toast(this, response.getMsg());
    }

    @Override
    public void onResponseError(Tasks task, Exception e) {
        ToastUtil.toast(this, e.getMessage());
    }

    @Override
    public void onResponseStart(Tasks task) {
    }

    @Override
    public void onResponseEnd(Tasks task) {
    }

    @Override
    public void onResponseCancel(Tasks task) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        for (String per : permissions) {
            if (Permission.STORAGE_WRITE.equals(per)) {
                permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
            }
            if (Permission.FINE_LOCATION.equals(per)) {
                permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
            }
            if (Permission.READ_PHONE_STATE.equals(per)) {
                permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
            }
            if (Permission.CAMERA.equals(per)) {
                permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
            }
            if (Permission.RECORD_AUDIO.equals(per)) {
                permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
            }
        }
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        onNoPermissionNeeded(permissionName);
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {
        for (String permission : permissionName) {
            if (Permission.STORAGE_WRITE.equals(permission)) {
                ToastUtil.toast(getApplicationContext(), R.string.dialog_no_storage_permission_tip);
                break;
            }
            if (Permission.FINE_LOCATION.equals(permission)) {
                ToastUtil.toast(getApplicationContext(),
                        R.string.dialog_no_location_permission_tip);
                break;
            }
            if (Permission.READ_PHONE_STATE.equals(permission)) {
                ToastUtil.toast(getApplicationContext(), R.string.dialog_no_read_phone_state_tip);
                break;
            }
            if (Permission.CAMERA.equals(permission)) {
                ToastUtil.toast(getApplicationContext(), R.string.dialog_no_camera_permission_tip);
                break;
            }
            if (Permission.RECORD_AUDIO.equals(permission)) {
                ToastUtil.toast(getApplicationContext(), R.string.dialog_no_audio_permission_tip);
                break;
            }
        }
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
        onNoPermissionNeeded(permissionsName);
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        if (isRequest) {
            isRequest = false;
            permissionHelper.requestAfterExplanation(Permission.STORAGE_WRITE);
        }
        if (isRequestLocation) {
            isRequestLocation = false;
            permissionHelper.requestAfterExplanation(Permission.FINE_LOCATION);
        }
        if (isRequestPhone) {
            isRequestPhone = false;
            permissionHelper.requestAfterExplanation(Permission.READ_PHONE_STATE);
        }
        if (isRequestCamera) {
            isRequestCamera = false;
            permissionHelper.requestAfterExplanation(Permission.CAMERA);
        }
        if (isRequestRecord) {
            isRequestRecord = false;
            permissionHelper.requestAfterExplanation(Permission.RECORD_AUDIO);
        }
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
        HintDialog dialog = new HintDialog(this);
        dialog.setEnterSelect(true);
        dialog.setOnEnterClickListener(() -> PermissionHelper.toPermissionSetting(getBaseContext()));
        switch (permissionName) {
            case Permission.STORAGE_WRITE:
                dialog.setContentString(getString(R.string.dialog_no_storage_permission_tip));
                break;
            case Permission.FINE_LOCATION:
                dialog.setContentString(getString(R.string.dialog_no_location_permission_tip));
                break;
            case Permission.READ_PHONE_STATE:
                dialog.setContentString(getString(R.string.dialog_no_read_phone_state_tip));
                break;
            case Permission.CAMERA:
                dialog.setContentString(getString(R.string.dialog_no_camera_permission_tip));
                break;
            case Permission.RECORD_AUDIO:
                dialog.setContentString(getString(R.string.dialog_no_audio_permission_tip));
                break;
            default:
                break;
        }
        dialog.show();
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        isRequest = true;
        isRequestLocation = true;
        isRequestPhone = true;
        isRequestCamera = true;
        isRequestRecord = true;
    }

    public boolean isSamePermission(String o, String n) {
        if (TextUtils.isEmpty(o) || TextUtils.isEmpty(n)) {
            return false;
        }
        if (o.equals(n)) {
            return true;
        }
        return false;
    }
}
