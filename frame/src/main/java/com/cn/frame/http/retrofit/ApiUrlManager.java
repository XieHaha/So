package com.cn.frame.http.retrofit;

import com.cn.frame.data.BaseListData;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.bean.AboutUsBean;
import com.cn.frame.data.bean.CardInfoBean;
import com.cn.frame.data.bean.CityBean;
import com.cn.frame.data.bean.DataDictBean;
import com.cn.frame.data.bean.FollowNumBean;
import com.cn.frame.data.bean.HelpBean;
import com.cn.frame.data.bean.PaymentBean;
import com.cn.frame.data.bean.ProvinceBean;
import com.cn.frame.data.bean.RolesBean;
import com.cn.frame.data.bean.UserBaseBean;
import com.cn.frame.data.bean.UserInfoBean;
import com.cn.frame.data.bean.VersionBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * @author dundun
 * Path是网址中的参数,例如:trades/{userId}
 * Query是问号后面的参数,例如:trades/{userId}?token={token}
 * QueryMap 相当于多个@Query
 * Field用于Post请求,提交单个数据,然后要加@FormUrlEncoded
 * Body相当于多个@Field,以对象的方式提交
 * @Streaming:用于下载大文件
 * @Header,@Headers、加请求头 ————————————————
 */
public interface ApiUrlManager {

    /**
     * 获取验证码
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<String>> getVerifyCode(@Body Map<String, String> info);

    /**
     * 登录
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<UserBaseBean>> login(@Body Map<String, String> info);

    /**
     * 退出
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<String>> signOut(@Body Map<String, String> info);

    /**
     * 注册
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<UserBaseBean>> register(@Body Map<String, Object> info);

    /**
     * 重置密码
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<UserBaseBean>> resetPwd(@Body Map<String, Object> info);

    /**
     * 版本更新
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("version/current-version")
    Observable<BaseResponse<VersionBean>> getVersion(@Header("token") String token, @Query(
            "device") String info);

    /**
     * 数据字典
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<DataDictBean>> getBasicsInfo(@Body Map<String, String> info);

    /**
     * session 更新
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<UserBaseBean>> renewSign(@Body Map<String, String> info);

    /**
     * 首页数据
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<BaseListData<RolesBean>>> getHomeInfo(@Body Map<String, Object> info);

    /**
     * 关注列表
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<BaseListData<RolesBean>>> collectionList(@Body Map<String, Object> info);

    /**
     * 关注
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<FollowNumBean>> renewCollection(@Body Map<String, Object> info);

    /**
     * 关于我们
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<AboutUsBean>> aboutUs(@Body Map<String, Object> info);

    /**
     * 帮助中心
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<BaseListData<HelpBean>>> helpsList(@Body Map<String, Object> info);

    /**
     * 问题反馈
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<String>> questionFeedback(@Body Map<String, String> info);

    /**
     * 举报
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<String>> report(@Body Map<String, String> info);

    /**
     * 屏蔽列表
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<BaseListData<RolesBean>>> shieldList(@Body Map<String, Object> info);

    /**
     * 卡信息
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<List<CardInfoBean>>> getCardInfo(@Body Map<String, Object> info);

    /**
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<PaymentBean>> upgradeMembership(@Body Map<String, Object> info);

    /**
     * or 信息编辑
     *
     * @param info map参数
     * @return 返回值
     */
    @Multipart
    @POST("api/")
    Observable<BaseResponse<PaymentBean>> auth(@Part List<MultipartBody.Part> info);

    /**
     * 省信息
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<List<ProvinceBean>>> getProvinceData(@Body Map<String, String> info);

    /**
     * 市信息
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<List<CityBean>>> getCityData(@Body Map<String, Object> info);

    /**
     * 用户信息
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<UserInfoBean>> getUserInfo(@Body Map<String, String> info);

    /**
     * 屏蔽
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<String>> shieldUser(@Body Map<String, Object> info);

    /**
     * 图片删除
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<String>> pictureDel(@Body Map<String, Object> info);

    /**
     * 自动消息
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("api/")
    Observable<BaseResponse<String>> sendRobotMsg(@Body Map<String, Object> info);

}
