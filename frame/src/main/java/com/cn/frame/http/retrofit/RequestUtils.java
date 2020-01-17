package com.cn.frame.http.retrofit;

import android.content.Context;

import com.cn.frame.data.BaseData;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.UserInfoBean;
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
        RetrofitManager.getApiUrlManager(context)
                .getVerifyCode(params)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_VERIFY_CODE,
                        listener));
    }

    public static void login(Context context, String phone, String pwd, String lat, String lng,
                             final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("sign", RsaUtils.encryptData(phone));
        params.put("login_password", pwd);
        params.put("lat", lat);
        params.put("lng", lng);
        RetrofitManager.getApiUrlManager(context)
                .login(params)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, true, false,
                        Tasks.LOGIN, listener));
    }

    public static void signOut(Context context, String sign,
                               final ResponseListener<BaseResponse> l) {
        Map<String, String> params = new HashMap<>(16);
        params.put("sign", RsaUtils.encryptData(sign));
        RetrofitManager.getApiUrlManager(context)
                .signOut(params)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, true, false,
                        Tasks.SIGN_OUT, l));
    }

    public static void register(Context context, String phone, String pwd, int captcha, int sex,
                                int registerType, int beInterestedIn, String lat, String lng,
                                final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("sign", RsaUtils.encryptData(phone));
        params.put("login_password", pwd);
        params.put("sex", sex);
        params.put("register_type", registerType);
        params.put("be_interested_in", beInterestedIn);
        params.put("captcha", captcha);
        params.put("lat", lat);
        params.put("lng", lng);
        RetrofitManager.getApiUrlManager(context)
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
        RetrofitManager.getApiUrlManager(context)
                .resetPwd(params)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, true, false,
                        Tasks.RESET_PWD, listener));
    }

    public static void getVersion(Context context, String token,
                                  final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager(context)
                .getVersion(token, BaseData.ADMIN)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_VERSION, listener));
    }


    public static void getBasicsInfo(Context context, String sign,
                                     final ResponseListener<BaseResponse> l) {
        Map<String, String> params = new HashMap<>(16);
        params.put("sign", RsaUtils.encryptData(sign));
        RetrofitManager.getApiUrlManager(context)
                .getBasicsInfo(params)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, true, false,
                        Tasks.GET_BASICS_INFO, l));
    }

    public static void renewSign(Context context, String sign,
                                 final ResponseListener<BaseResponse> l) {
        Map<String, String> params = new HashMap<>(16);
        params.put("sign", RsaUtils.encryptData(sign));
        RetrofitManager.getApiUrlManager(context)
                .renewSign(params)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, true, false,
                        Tasks.RENEW_SIGN, l));
    }

    public static void getHomeInfo(Context context, String sign, String type, UserInfoBean userInfo,
                                   int page, int pageSize, boolean show,
                                   final ResponseListener<BaseResponse> l) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("sign", sign);
        params.put("requestDisplayType", type);
        params.put("age", userInfo.getAge());
        params.put("height", userInfo.getHeight());
        params.put("somatotype", userInfo.getSomatotype());
        params.put("race", userInfo.getRace());
        params.put("education", userInfo.getEducation());
        params.put("marriage", userInfo.getMarriage());
        params.put("children", userInfo.getChildren());
        params.put("smoke", userInfo.getSmoke());
        params.put("drink", userInfo.getDrink());
        params.put("per_page", pageSize);
        params.put("page", page);
        RetrofitManager.getApiUrlManager(context)
                .getHomeInfo(params)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, show, Tasks.GET_HOME_INFO, l));
    }

    public static void collectionList(Context context, String sign, int followType, int page,
                                      int pageSize, final ResponseListener<BaseResponse> l) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("sign", sign);
        //1、关注我  2、我关注
        params.put("followType", followType);
        params.put("per_page", pageSize);
        params.put("page", page);
        RetrofitManager.getApiUrlManager(context)
                .collectionList(params)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, Tasks.COLLECTION_LIST, l));
    }

    public static void renewCollection(Context context, String sign, int userId, int collection,
                                       final ResponseListener<BaseResponse> l) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("sign", sign);
        params.put("user_id", userId);
        params.put("collection_state", collection);
        RetrofitManager.getApiUrlManager(context)
                .renewCollection(params)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, Tasks.RENEW_COLLECTION, l));
    }

    public static void aboutUs(Context context, String sign,
                               final ResponseListener<BaseResponse> l) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("sign", sign);
        RetrofitManager.getApiUrlManager(context)
                .aboutUs(params)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, Tasks.ABOUT_US, l));
    }

    public static void helpsList(Context context, String sign, int page, int pageSize,
                                 final ResponseListener<BaseResponse> l) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("sign", sign);
        params.put("per_page", pageSize);
        params.put("page", page);
        RetrofitManager.getApiUrlManager(context)
                .helpsList(params)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, Tasks.HELPS_LIST, l));
    }

    public static void questionFeedback(Context context, String sign, String title, String feedback,
                                        final ResponseListener<BaseResponse> l) {
        Map<String, String> params = new HashMap<>(16);
        params.put("sign", sign);
        params.put("title", title);
        params.put("feedback", feedback);
        RetrofitManager.getApiUrlManager(context)
                .questionFeedback(params)
                .compose(RxJavaHelper.observableIO2Main(context))
                .subscribe(new AbstractLoadViewObserver<>(context, Tasks.QUESTION_FEEDBACK, l));
    }
}

