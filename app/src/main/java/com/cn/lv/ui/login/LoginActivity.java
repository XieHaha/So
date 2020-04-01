package com.cn.lv.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.DataDictBean;
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
import com.cn.lv.ui.main.my.PersonalActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    private static final int REQUEST_CODE_INFO = 100;
    private static final int REQUEST_CODE_REGISTER = 200;
    private static final int REQUEST_CODE_EDIT = 300;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    private String phone, pwd;
    /**
     * 坐标
     */
    private double lat = 0.0f, lng = 0.0f;

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    @Override
    protected boolean isInitStatusBar() {
        return false;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_login;
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
        RequestUtils.login(this, BaseUtils.signSpan(this, phone, InterfaceName.SIGN_IN), pwd,
                String.valueOf(lat), String.valueOf(lng), this);
    }

    /**
     * 获取基础数据集合
     */
    private void getBasicsInfo() {
        RequestUtils.getBasicsInfo(this, BaseUtils.signSpan(this,
                loginBean.getUserInfo().getMobile_number(),
                loginBean.getSession_id(), InterfaceName.GET_BASICS_INFO), this);
    }

    private void jump() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * 信息编辑
     */
    private void jumpEditInfo() {
        Intent intent = new Intent(this, PersonalActivity.class);
        intent.putExtra(CommonData.KEY_INTENT_BOOLEAN, true);
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }


    @OnClick({R.id.tv_forgot_pwd, R.id.tv_register, R.id.layout_login_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forgot_pwd:
                startActivity(new Intent(this, RegisterAndModifyPwdActivity.class));
                break;
            case R.id.tv_register:
                startActivityForResult(new Intent(this, RegisterInfoActivity.class),
                        REQUEST_CODE_INFO);
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
            //存储登录信息
            loginBean = (UserBaseBean) response.getData();
            sharePreferenceUtil.putAlwaysString(CommonData.KEY_LOGIN_ACCOUNT, phone);
            sharePreferenceUtil.putAlwaysString(CommonData.KEY_LOGIN_PWD, pwd);
            sharePreferenceUtil.putAlwaysString(CommonData.KEY_LOGIN_SESSION_ID,
                    loginBean.getSession_id());
            //存储登录结果
            SweetApplication.getInstance().setLoginBean(loginBean);
            getBasicsInfo();
        } else if (task == Tasks.GET_BASICS_INFO) {
            DataDictBean bean = (DataDictBean) response.getData();
            SweetApplication.getInstance().setDataDictBean(bean);
            if (TextUtils.isEmpty(loginBean.getUserInfo().getHead_portrait())
                    || TextUtils.isEmpty(loginBean.getUserInfo().getNickname())
                    || TextUtils.isEmpty(loginBean.getUserInfo().getIndividuality_signature())) {
                jumpEditInfo();
            } else {
                jump();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_INFO:
                Intent intent = new Intent(this, RegisterAndModifyPwdActivity.class);
                intent.putExtra(CommonData.KEY_INTENT_BOOLEAN, true);
                intent.putExtra(CommonData.KEY_SEX, data.getIntExtra(CommonData.KEY_SEX, -1));
                intent.putExtra(CommonData.KEY_WHO, data.getIntExtra(CommonData.KEY_WHO, -1));
                intent.putExtra(CommonData.KEY_INTEREST, data.getIntExtra(CommonData.KEY_INTEREST
                        , -1));
                startActivityForResult(intent, REQUEST_CODE_REGISTER);
                break;
            case REQUEST_CODE_REGISTER:
                jumpEditInfo();
                break;
            case REQUEST_CODE_EDIT:
                jump();
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
            SweetApplication.getInstance().setCity(city);
            SweetLog.i(TAG, "login --->city:" + city + " Lat: " + lat + "  Lng: " + lng);
        }
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
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
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(myListener);
        }
    }
}
