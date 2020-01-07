package com.cn.lv.ui.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.UserBaseBean;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.permission.Permission;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.SweetLog;
import com.cn.frame.utils.ToastUtil;
import com.cn.lv.R;
import com.cn.lv.SweetApplication;
import com.cn.lv.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    private String phone, pwd;
    /**
     * 定位
     */
    private LocationManager manager;
    /**
     * 坐标
     */
    private float lat = 0.0f, lng = 0.0f;

    @Override
    protected boolean isInitStatusBar() {
        return false;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_login;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        permissionHelper.request(new String[]{Permission.FINE_LOCATION});
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        phone = sharePreferenceUtil.getAlwaysString(CommonData.KEY_LOGIN_ACCOUNT);
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    /**
     * 登录
     */
    private void login() {
        RequestUtils.login(this, BaseUtils.signSpan(this, phone, InterfaceName.signIn), pwd,
                String.valueOf(lat), String.valueOf(lng), this);
    }

    private void jump() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    @OnClick({R.id.tv_forgot_pwd, R.id.tv_register, R.id.layout_login_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forgot_pwd:
                startActivity(new Intent(this, RegisterAndModifyPwdActivity.class));
                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterInfoActivity.class));
                break;
            case R.id.layout_login_next:
                phone = etPhone.getText().toString().trim();
                if (!BaseUtils.isMobileNumber(phone)) {
                    ToastUtil.toast(this, R.string.txt_input_phone_hint);
                    return;
                }
                pwd = etPwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtil.toast(this, R.string.txt_input_pwd_hint);
                    return;
                }
                login();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.LOGIN) {
            loginBean = (UserBaseBean) response.getData();
            //存储登录结果
            SweetApplication.getInstance().setLoginBean(loginBean);
            jump();
        }
    }

    /**
     * 开始定位
     */
    @SuppressLint("MissingPermission")
    private void startLocation() {
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Location location = this.manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                lat = (float) location.getLatitude();
                lng = (float) location.getLongitude();
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
                lat = (float) location.getLatitude();
                lng = (float) location.getLongitude();
            }
        }
        SweetLog.i(TAG, "Location  : Lat: " + lat + " Lng: " + lng);
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        if (permissionName instanceof String[]) {
            if (isSamePermission(Permission.FINE_LOCATION, ((String[]) permissionName)[0])) {
                startLocation();
            }
        }
    }
}
