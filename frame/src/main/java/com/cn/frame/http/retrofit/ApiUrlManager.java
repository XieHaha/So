package com.cn.frame.http.retrofit;

import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.bean.UserBaseBean;
import com.cn.frame.data.bean.VersionBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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


}
