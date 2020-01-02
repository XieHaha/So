package com.cn.frame.http.retrofit;

import android.content.Context;

import com.cn.frame.data.BaseData;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.Tasks;
import com.cn.frame.http.listener.ResponseListener;
import com.cn.frame.utils.RsaUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 提交参数方式
 *
 * @author dundun
 */
public class RequestUtils {

    public static void getVerifyCode(Context context, String phone,
                                     final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("sign", RsaUtils.encryptData(phone));
        RetrofitManager.getApiUrlManager()
                .getVerifyCode(params)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_VERIFY_CODE,
                        listener));
    }

    public static void login(Context context, String phone, String pwd,
                             final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("sign", RsaUtils.encryptData(phone));
        params.put("login_password", pwd);
        RetrofitManager.getApiUrlManager()
                .login(params)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, true, false,
                        Tasks.LOGIN, listener));
    }

    public static void register(Context context, String phone, String pwd, int captcha, int sex,
                                int registerType, int beInterestedIn,
                                final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("sign", RsaUtils.encryptData(phone));
        params.put("login_password", pwd);
        params.put("sex", sex);
        params.put("register_type", registerType);
        params.put("be_interested_in", beInterestedIn);
        params.put("captcha", captcha);
        RetrofitManager.getApiUrlManager()
                .register(params)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, true, false,
                        Tasks.REGISTER, listener));
    }

    public static void resetPwd(Context context, String phone, String pwd, int captcha,
                                final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("sign", RsaUtils.encryptData(phone));
        params.put("new_pwd", pwd);
        params.put("captcha", captcha);
        RetrofitManager.getApiUrlManager()
                .resetPwd(params)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, true, false,
                        Tasks.RESET_PWD, listener));
    }

    public static void getVersion(Context context, String token,
                                  final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                .getVersion(token, BaseData.ADMIN)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_VERSION, listener));
    }

}

