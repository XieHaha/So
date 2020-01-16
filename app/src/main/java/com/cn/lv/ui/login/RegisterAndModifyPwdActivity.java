package com.cn.lv.ui.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.frame.data.BaseData;
import com.cn.frame.data.BaseNetConfig;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.Tasks;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.permission.Permission;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.SweetLog;
import com.cn.frame.utils.ToastUtil;
import com.cn.lv.R;
import com.cn.lv.ui.WebViewActivity;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterAndModifyPwdActivity extends BaseActivity {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_verify)
    EditText etVerify;
    @BindView(R.id.tv_get_verify_code)
    TextView tvGetVerifyCode;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_pwd_again)
    EditText etPwdAgain;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title1)
    TextView tvTitle1;
    @BindView(R.id.tv_title2)
    TextView tvTitle2;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.layout_register_hint)
    LinearLayout layoutRegisterHint;
    private ScheduledExecutorService executorService;
    private String phone, pwd, pwdAgain;
    /**
     * 验证码计时
     */
    private int time = 0;
    /**
     * 是否获取过验证码
     */
    private boolean isSendVerifyCode = false;
    /**
     * true为注册；false为忘记密码
     */
    private boolean mode;
    private int code, sex, who, interest;
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
        return R.layout.act_register_and_modify_pwd;
    }

    private Handler handler = new Handler(message -> {
        if (time <= 0) {
            tvGetVerifyCode.setClickable(true);
            tvGetVerifyCode.setText(R.string.txt_get_verify_code);
        } else {
            tvGetVerifyCode.setClickable(false);
            tvGetVerifyCode.setText(String.format(getString(R.string.txt_login_time), time));
        }
        return true;
    });

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        permissionHelper.request(new String[]{Permission.FINE_LOCATION});
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            mode = getIntent().getBooleanExtra(CommonData.KEY_INTENT_BOOLEAN, false);
            sex = getIntent().getIntExtra(CommonData.KEY_SEX, -1);
            who = getIntent().getIntExtra(CommonData.KEY_WHO, -1);
            interest = getIntent().getIntExtra(CommonData.KEY_INTEREST, -1);
        }
        spannableString(getString(R.string.txt_register_hint));
        if (mode) {
            ivBack.setVisibility(View.GONE);
            tvTitle1.setText(R.string.txt_hello);
            tvTitle2.setVisibility(View.VISIBLE);
            layoutRegisterHint.setVisibility(View.VISIBLE);
        } else {
            ivBack.setVisibility(View.VISIBLE);
            tvTitle1.setText(R.string.txt_find_pwd);
            tvTitle2.setVisibility(View.GONE);
            layoutRegisterHint.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        RequestUtils.getVerifyCode(this, BaseUtils.signSpan(this, phone,
                InterfaceName.SEND_CAPTCHA), this);
    }

    /**
     * 注册
     */
    private void register() {
        RequestUtils.register(this, BaseUtils.signSpan(this, phone, InterfaceName.SIGN_UP), pwd,
                code, sex, who, interest, String.valueOf(lat), String.valueOf(lng), this);
    }

    /**
     * 重置密码
     */
    private void resetPwd() {
        RequestUtils.resetPwd(this, BaseUtils.signSpan(this, phone, InterfaceName.PWD_RESET), pwd,
                code, this);
    }


    @OnClick({R.id.iv_back, R.id.tv_get_verify_code, R.id.layout_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_get_verify_code:
                phone = etPhone.getText().toString().trim();
                if (!BaseUtils.isMobileNumber(phone)) {
                    ToastUtil.toast(this, R.string.txt_input_phone_hint);
                    return;
                }
                getVerifyCode();
                break;
            case R.id.layout_next:
                if (verifyNext()) {
                    if (mode) {
                        register();
                    } else {
                        resetPwd();
                    }
                }

                break;
            default:
                break;
        }
    }

    /**
     * 校验输入数据
     */
    private boolean verifyNext() {
        String verify = etVerify.getText().toString().trim();
        if (TextUtils.isEmpty(verify)) {
            ToastUtil.toast(this, R.string.txt_input_verify_code);
            return false;
        } else {
            code = Integer.valueOf(verify);
        }
        pwd = etPwd.getText().toString().trim();
        pwdAgain = etPwdAgain.getText().toString().trim();
        if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdAgain)) {
            ToastUtil.toast(this, R.string.txt_input_pwd_hint);
            return false;
        }
        if (!TextUtils.equals(pwd, pwdAgain)) {
            ToastUtil.toast(this, R.string.txt_pwd_diff);
            return false;
        }
        return true;
    }

    /**
     * 验证码再次获取倒计时
     */
    private void startVerifyCodeTimer() {
        time = BaseData.BASE_MAX_RESEND_TIME;
        executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("social").daemon(true).build());
        executorService.scheduleAtFixedRate(() -> {
            time--;
            if (time < 0) {
                time = 0;
                executorService.shutdownNow();
            } else {
                handler.sendEmptyMessage(0);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }


    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_VERIFY_CODE:
                isSendVerifyCode = true;
                startVerifyCodeTimer();
                break;
            case REGISTER:
                ToastUtil.toast(this, response.getMsg());
                finish();
                break;
            case RESET_PWD:
                ToastUtil.toast(this, response.getMsg());
                finish();
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
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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

    /**
     * 字符串处理
     */
    private void spannableString(String s) {
        SpannableString style = new SpannableString(s);
        //颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor
                (this,
                        R.color.color_1491fc));
        style.setSpan(colorSpan, 11, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(colorSpan, 18, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //点击
        ClickableSpan clickSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(RegisterAndModifyPwdActivity.this,
                        WebViewActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC, BaseNetConfig.BASE_BASIC_USER_PROTOCOL_URL);
                startActivity(intent);
                clearBackgroundColor(widget);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.clearShadowLayer();
            }
        };
        //点击
        ClickableSpan privacyClickSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(RegisterAndModifyPwdActivity.this,
                        WebViewActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC,
                        BaseNetConfig.BASE_BASIC_PRIVATE_PROTOCOL_URL);
                startActivity(intent);
                clearBackgroundColor(widget);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.clearShadowLayer();
            }
        };
        style.setSpan(clickSpan, 11, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(privacyClickSpan, 18, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //配置给TextView
        tvHint.setMovementMethod(LinkMovementMethod.getInstance());
        tvHint.setText(style);
    }

    private void clearBackgroundColor(View view) {
        if (view instanceof TextView) {
            ((TextView) view).setHighlightColor(ContextCompat.getColor(this,
                    android.R.color.transparent));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}
