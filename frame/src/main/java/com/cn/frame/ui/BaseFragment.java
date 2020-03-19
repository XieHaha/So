package com.cn.frame.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.cn.frame.api.notify.INotifyChangeListenerServer;
import com.cn.frame.data.BaseData;
import com.cn.frame.data.BaseNetConfig;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.DataDictBean;
import com.cn.frame.data.bean.UserBaseBean;
import com.cn.frame.data.bean.UserInfoBean;
import com.cn.frame.http.listener.ResponseListener;
import com.cn.frame.permission.OnPermissionCallback;
import com.cn.frame.permission.PermissionHelper;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.RsaUtils;
import com.cn.frame.utils.SharePreferenceUtil;
import com.cn.frame.utils.ToastUtil;
import com.cn.frame.widgets.dialog.LoadingDialog;
import com.google.gson.Gson;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author dundun
 */
public abstract class BaseFragment extends Fragment
        implements UiInterface, BaseData, OnPermissionCallback, ResponseListener<BaseResponse>,
        View.OnClickListener {
    public static final String TAG = "SWEET";
    /**
     * load view
     */
    private LoadingDialog loadingView;
    /**
     * 注解
     */
    protected Unbinder unbinder;
    /**
     * 动态权限
     */
    public PermissionHelper permissionHelper;
    /**
     * 轻量级存储
     */
    protected SharePreferenceUtil sharePreferenceUtil;
    /**
     * 监听器
     */
    public INotifyChangeListenerServer iNotifyChangeListenerServer;
    /**
     * 登录数据
     */
    protected UserBaseBean loginBean;
    protected UserInfoBean userInfo;
    protected DataDictBean dataDictBean;
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
     * 懒加载 刷新
     */
    public boolean isPrepared;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        beforeCreateView(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState) {
        View view;
        int layoutID = getLayoutID();
        if (layoutID != 0) {
            view = inflater.inflate(getLayoutID(), null);
        } else {
            view = getLayoutView();
        }
        unbinder = ButterKnife.bind(this, view);
        permissionHelper = PermissionHelper.getInstance(getActivity());
        sharePreferenceUtil = new SharePreferenceUtil(getContext());
        dataDictBean = getDataDictBean();
        getLoginBean();
        isPrepared = true;
        init(view, savedInstanceState);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoginBean();
    }

    private void initLoadingView() {
        loadingView = new LoadingDialog(getContext());
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
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
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
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
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
     * 初始化login数据
     */
    public UserBaseBean getLoginBean() {
        String userStr = (String) SharePreferenceUtil.getObject(getActivity(),
                CommonData.KEY_LOGIN_BEAN, "");
        if (!TextUtils.isEmpty(userStr)) {
            loginBean = new Gson().fromJson(userStr, UserBaseBean.class);
            if (loginBean != null) {
                userInfo = loginBean.getUserInfo();
            }
        }
        return loginBean;
    }

    public DataDictBean getDataDictBean() {
        String data = (String) SharePreferenceUtil.getObject(getActivity(),
                CommonData.KEY_DATA_DICT_BEAN,
                "");
        if (!TextUtils.isEmpty(data)) {
            dataDictBean = new Gson().fromJson(data, DataDictBean.class);
        }
        return dataDictBean;
    }

    /**
     * 获取sign
     */
    public String signSession(String interfaceName) {
        return RsaUtils.encryptData(BaseUtils.signSpan(getContext(), userInfo.getMobile_number(),
                loginBean.getSession_id(), interfaceName));
    }

    /**
     * 获取状态栏高度,在页面还没有显示出来之前
     */
    public static int getStateBarHeight(Activity a) {
        if (a == null) {
            return 0;
        }
        int result = 0;
        int resourceId = a.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = a.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 方法回调顺序
     * 1.initView
     * 3.initData
     * 4.initListener
     */
    private void init(@NonNull View view, @NonNull Bundle savedInstanceState) {
        initView(view, savedInstanceState);
        initView(savedInstanceState);
        initData(savedInstanceState);
        initListener();
        view.post(this::fillNetWorkData);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().removeActivity(getActivity());
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputFromWindow(Context context, EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 打开软键盘
     */
    public void showSoftInputFromWindow(Context context, EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 裁剪图片
     *
     * @param originUri  裁剪前
     * @param cutFileUri 裁剪后
     */
    public Intent getCutImageIntent(Uri originUri, Uri cutFileUri) {
        //系统裁剪
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        // 在Android N中，为了安全起见，您必须获得“写入或读取Uri文件”的权限。如果您希望系统照片裁剪您的“uri文件”，那么您 必须允许系统照片。
        intent.setDataAndType(originUri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        if (Build.BRAND.toUpperCase().contains(BaseData.BASE_HONOR_NAME) ||
                Build.BRAND.toUpperCase().contains(BaseData.BASE_HUAWEI_NAME)) {
            //华为特殊处理 不然会显示圆
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);
        } else {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cutFileUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        return intent;
    }

    /**
     * 退出登录
     */
    public void exit() {
        Intent intent = new Intent(BaseData.BASE_SIGN_OUT_ACTION);
        intent.setPackage(getActivity().getPackageName());
        getActivity().sendBroadcast(intent);
    }

    /**
     * token失效
     */
    public void token(String errorHint) {
        Intent intent = new Intent(BaseData.BASE_TOKEN_ERROR_ACTION);
        intent.putExtra(CommonData.KEY_PUBLIC_STRING, errorHint);
        intent.setPackage(getActivity().getPackageName());
        getActivity().sendBroadcast(intent);
    }

    /**
     * 账号禁用
     */
    private void accountError() {
        Intent intent = new Intent(BaseData.BASE_ACCOUNT_ERROR_ACTION);
        intent.setPackage(getActivity().getPackageName());
        getActivity().sendBroadcast(intent);
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
    public void beforeCreateView(@NonNull Bundle savedInstanceState) {
    }

    public void initView(View view, @NonNull Bundle savedInstanceState) {
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
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
    public void onResponseSuccess(Tasks task, BaseResponse response) {
    }

    @Override
    public void onResponseError(Tasks task, Exception e) {
        ToastUtil.toast(getActivity(), e.getMessage());
    }

    @Override
    public void onResponseCode(Tasks task, BaseResponse response) {
        if (response.getCode() == BaseNetConfig.REQUEST_TOKEN_ERROR) {
            token(response.getMsg());
        } else if (response.getCode() == BaseNetConfig.REQUEST_OTHER_ERROR ||
                response.getCode() == BaseNetConfig.REQUEST_SERVER_ERROR) {
            ToastUtil.toast(getContext(), response.getMsg());
        } else if (response.getCode() == BaseNetConfig.REQUEST_ACCOUNT_ERROR) {
            accountError();
        }
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
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        permissionHelper.requestAfterExplanation(permissionName);
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
    }

    public boolean isSamePermission(String o, String n) {
        if (TextUtils.isEmpty(o) || TextUtils.isEmpty(n)) {
            return false;
        }
        return o.equals(n);
    }

}
