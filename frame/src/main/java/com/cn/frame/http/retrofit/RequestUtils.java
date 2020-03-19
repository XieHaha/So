package com.cn.frame.http.retrofit;

import android.content.Context;

import com.cn.frame.data.BaseData;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.UserInfoBean;
import com.cn.frame.http.listener.ResponseListener;
import com.cn.frame.utils.RsaUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 提交参数方式
 *
 * @author dundun
 */
public class RequestUtils {

    public static void getVerifyCode(Context c, String phone,
                                     final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("sign", RsaUtils.encryptData(phone));
        RetrofitManager.getApiUrlManager(c)
                .getVerifyCode(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, true, Tasks.GET_VERIFY_CODE,
                        listener));
    }

    public static void login(Context c, String phone, String pwd, String lat, String lng,
                             final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("sign", RsaUtils.encryptData(phone));
        params.put("login_password", pwd);
        params.put("lat", lat);
        params.put("lng", lng);
        RetrofitManager.getApiUrlManager(c)
                .login(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, true, false,
                        Tasks.LOGIN, listener));
    }

    public static void signOut(Context c, String sign,
                               final ResponseListener<BaseResponse> l) {
        Map<String, String> params = new HashMap<>(16);
        params.put("sign", RsaUtils.encryptData(sign));
        RetrofitManager.getApiUrlManager(c)
                .signOut(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, true, false,
                        Tasks.SIGN_OUT, l));
    }

    public static void register(Context c, String phone, String pwd, int captcha, int sex,
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
        RetrofitManager.getApiUrlManager(c)
                .register(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, true, false,
                        Tasks.REGISTER, listener));
    }

    public static void resetPwd(Context c, String phone, String pwd, int captcha,
                                final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("sign", RsaUtils.encryptData(phone));
        params.put("new_pwd", pwd);
        params.put("captcha", captcha);
        RetrofitManager.getApiUrlManager(c)
                .resetPwd(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, true, false,
                        Tasks.RESET_PWD, listener));
    }

    public static void getVersion(Context c, String token,
                                  final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager(c)
                .getVersion(token, BaseData.ADMIN)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, Tasks.GET_VERSION, listener));
    }


    public static void getBasicsInfo(Context c, String sign,
                                     final ResponseListener<BaseResponse> l) {
        Map<String, String> params = new HashMap<>(16);
        params.put("sign", RsaUtils.encryptData(sign));
        RetrofitManager.getApiUrlManager(c)
                .getBasicsInfo(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, true, false,
                        Tasks.GET_BASICS_INFO, l));
    }

    public static void renewSign(Context c, String sign, final ResponseListener<BaseResponse> l) {
        Map<String, String> params = new HashMap<>(16);
        params.put("sign", RsaUtils.encryptData(sign));
        RetrofitManager.getApiUrlManager(c)
                .renewSign(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, true, false,
                        Tasks.RENEW_SIGN, l));
    }

    public static void getHomeInfo(Context c, String sign, String type, UserInfoBean userInfo,
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
        RetrofitManager.getApiUrlManager(c)
                .getHomeInfo(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, show, Tasks.GET_HOME_INFO, l));
    }

    public static void collectionList(Context c, String sign, int followType, int page,
                                      int pageSize, final ResponseListener<BaseResponse> l) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("sign", sign);
        //1、关注我  2、我关注
        params.put("followType", followType);
        params.put("per_page", pageSize);
        params.put("page", page);
        RetrofitManager.getApiUrlManager(c)
                .collectionList(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, Tasks.COLLECTION_LIST, l));
    }

    public static void renewCollection(Context c, String sign, int userId, int collection,
                                       final ResponseListener<BaseResponse> l) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("sign", sign);
        params.put("user_id", userId);
        params.put("collection_state", collection);
        RetrofitManager.getApiUrlManager(c)
                .renewCollection(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, Tasks.RENEW_COLLECTION, l));
    }

    public static void aboutUs(Context c, String sign,
                               final ResponseListener<BaseResponse> l) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("sign", sign);
        RetrofitManager.getApiUrlManager(c)
                .aboutUs(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, Tasks.ABOUT_US, l));
    }

    public static void helpsList(Context c, String sign, int page, int pageSize,
                                 final ResponseListener<BaseResponse> l) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("sign", sign);
        params.put("per_page", pageSize);
        params.put("page", page);
        RetrofitManager.getApiUrlManager(c)
                .helpsList(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, Tasks.HELPS_LIST, l));
    }

    public static void questionFeedback(Context c, String sign, String title, String feedback,
                                        final ResponseListener<BaseResponse> l) {
        Map<String, String> params = new HashMap<>(16);
        params.put("sign", sign);
        params.put("title", title);
        params.put("feedback", feedback);
        RetrofitManager.getApiUrlManager(c)
                .questionFeedback(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, Tasks.QUESTION_FEEDBACK, l));
    }

    public static void report(Context c, String sign, String report_reason, String report_content,
                              final ResponseListener<BaseResponse> l) {
        Map<String, String> params = new HashMap<>(16);
        params.put("sign", sign);
        params.put("report_reason", report_reason);
        params.put("report_content", report_content);
        RetrofitManager.getApiUrlManager(c)
                .report(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, Tasks.REPORT, l));
    }

    public static void shieldList(Context c, String sign, int page, int pageSize,
                                  final ResponseListener<BaseResponse> l) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("sign", sign);
        params.put("per_page", pageSize);
        params.put("page", page);
        RetrofitManager.getApiUrlManager(c)
                .shieldList(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, Tasks.SHIELD_LIST, l));
    }

    public static void getCardInfo(Context c, String sign, final ResponseListener<BaseResponse> l) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("sign", sign);
        RetrofitManager.getApiUrlManager(c)
                .getCardInfo(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, Tasks.GET_CARD_INFO, l));
    }

    public static void upgradeMembership(Context c, String sign, int card_id, float card_money,
                                         long card_duration,
                                         final ResponseListener<BaseResponse> l) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("sign", sign);
        params.put("card_id", card_id);
        params.put("card_money", card_money);
        params.put("card_duration", card_duration);
        RetrofitManager.getApiUrlManager(c)
                .upgradeMembership(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, Tasks.UPGRADE_MEMBERSHIP, l));
    }

    public static void edit(Context c, String sign, int type, String name, File headerFile, int age,
                            int height, int weight, int bodyType, int race, int education,
                            int marriage, int child, int smoke, int drink, int job,
                            int be_interested_in, int income, int money, int life, int who,
                            String areaCode, String purpose, String introduction,
                            ArrayList<File> publicFiles, ArrayList<File> privateFiles,
                            final ResponseListener<BaseResponse> l) {

        RequestBody header = RequestBody.create(MediaType.parse("multipart/form-data"),
                headerFile);
        MultipartBody.Part headerBody = null;
        try {
            headerBody = MultipartBody.Part.createFormData("head_portrait",
                    URLEncoder.encode(headerFile.getName(), "UTF-8"), header);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        List<MultipartBody.Part> photos = new ArrayList<>();
        if (publicFiles.size() > 0) {
            for (int i = 0, publicFilesSize = publicFiles.size(); i < publicFilesSize; i++) {
                File file = publicFiles.get(i);
                RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"),
                        file);
                MultipartBody.Part body = null;
                try {
                    body = MultipartBody.Part.createFormData("publicImgs_" + (i + 1),
                            URLEncoder.encode(file.getName(), "UTF-8"), reqFile);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                photos.add(body);
            }
        }
        if (privateFiles.size() > 0) {
            for (int i = 0, privateFilesSize = privateFiles.size(); i < privateFilesSize; i++) {
                File file = privateFiles.get(i);
                RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"),
                        file);
                MultipartBody.Part body = null;
                try {
                    body = MultipartBody.Part.createFormData("privateImgs_" + (i + 1),
                            URLEncoder.encode(file.getName(), "UTF-8"), reqFile);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                photos.add(body);
            }
        }
        photos.add(MultipartBody.Part.createFormData("sign", sign));
        photos.add(MultipartBody.Part.createFormData("nickname", name));
        photos.add(MultipartBody.Part.createFormData("age", String.valueOf(age)));
        photos.add(MultipartBody.Part.createFormData("height", String.valueOf(height)));
        photos.add(MultipartBody.Part.createFormData("weight", String.valueOf(weight)));
        photos.add(MultipartBody.Part.createFormData("somatotype", String.valueOf(bodyType)));
        photos.add(MultipartBody.Part.createFormData("race", String.valueOf(race)));
        photos.add(MultipartBody.Part.createFormData("education", String.valueOf(education)));
        photos.add(MultipartBody.Part.createFormData("marriage", String.valueOf(marriage)));
        photos.add(MultipartBody.Part.createFormData("children", String.valueOf(child)));
        photos.add(MultipartBody.Part.createFormData("smoke", String.valueOf(smoke)));
        photos.add(MultipartBody.Part.createFormData("occupation", String.valueOf(job)));
        photos.add(MultipartBody.Part.createFormData("drink", String.valueOf(drink)));
        photos.add(MultipartBody.Part.createFormData("area_code", areaCode));
        photos.add(MultipartBody.Part.createFormData("be_interested_in",
                String.valueOf(be_interested_in)));
        photos.add(MultipartBody.Part.createFormData("annual_income", String.valueOf(income)));
        photos.add(MultipartBody.Part.createFormData("net_assets", String.valueOf(money)));
        photos.add(MultipartBody.Part.createFormData("life_style", String.valueOf(life)));
        photos.add(MultipartBody.Part.createFormData("contact_object", String.valueOf(who)));
        photos.add(MultipartBody.Part.createFormData("making_friends_goals", purpose));
        photos.add(MultipartBody.Part.createFormData("individuality_signature", introduction));
        photos.add(headerBody);

        Tasks tasks;
        if (type == 1) {
            tasks = Tasks.AUTH;
        } else {
            tasks = Tasks.EDIT_USER_INFO;
        }
        RetrofitManager.getApiUrlManager(c)
                .auth(photos)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, true, false, tasks, l));
    }

    public static void getProvinceData(Context c, String sign,
                                       final ResponseListener<BaseResponse> l) {
        Map<String, String> params = new HashMap<>(16);
        params.put("sign", sign);
        RetrofitManager.getApiUrlManager(c)
                .getProvinceData(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, Tasks.GET_PROVINCE_INFO, l));
    }

    public static void getCityData(Context c, String sign, int parentId,
                                   final ResponseListener<BaseResponse> l) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("sign", sign);
        params.put("parentId", parentId);
        RetrofitManager.getApiUrlManager(c)
                .getCityData(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, Tasks.GET_CITY_INFO, l));
    }

    public static void getUserInfo(Context c, String sign, int user_id,
                                   final ResponseListener<BaseResponse> l) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("sign", sign);
        params.put("user_id", user_id);
        RetrofitManager.getApiUrlManager(c)
                .getUserInfo(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, Tasks.GET_USER_INFO, l));
    }

    public static void shieldUser(Context c, String sign, int user_id, int state,
                                  final ResponseListener<BaseResponse> l) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("sign", sign);
        params.put("user_id", user_id);
        params.put("shield_state", state);
        RetrofitManager.getApiUrlManager(c)
                .shieldUser(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, Tasks.SHIELD_USER, l));
    }

    public static void pictureDel(Context c, String sign, int imageId,
                                  final ResponseListener<BaseResponse> l) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("sign", sign);
        params.put("id", imageId);
        RetrofitManager.getApiUrlManager(c)
                .pictureDel(params)
                .compose(RxJavaHelper.observableIO2Main(c))
                .subscribe(new AbstractLoadViewObserver<>(c, Tasks.PICTURE_DEL, l));
    }
}

