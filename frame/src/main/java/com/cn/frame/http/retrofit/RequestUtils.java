package com.cn.frame.http.retrofit;

import android.content.Context;

import com.cn.frame.data.BaseData;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.Tasks;
import com.cn.frame.http.listener.ResponseListener;

import java.util.HashMap;
import java.util.Map;

/**
 * 提交参数方式
 *
 * @author dundun
 */
public class RequestUtils {

    public static void getVerifyCode(Context context, String phone, String merchant,
                                     final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("phone", phone);
        params.put("merchant", merchant);
        RetrofitManager.getApiUrlManager()
                .getVerifyCode(params)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_VERIFY_CODE,
                        listener));
    }

    public static void login(Context context, String phone, String pwd,
                             final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("sign", phone);
        params.put("login_password", pwd);
        RetrofitManager.getApiUrlManager()
                .login(params)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, true, false,
                        Tasks.LOGIN_AND_REGISTER,
                        listener));
    }

    public static void getVersion(Context context, String token,
                                  final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                .getVersion(token, BaseData.ADMIN)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_VERSION, listener));
    }

}

