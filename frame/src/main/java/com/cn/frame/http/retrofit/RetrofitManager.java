package com.cn.frame.http.retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.cn.frame.data.BaseNetConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit封装
 *
 * @author dundun
 */
public class RetrofitManager {
    private static String baseUrl;
    private static RetrofitServer retrofitServer;
    private static volatile ApiUrlManager apiUrlManager;

    public synchronized static RetrofitServer getInstance() {
        if (retrofitServer == null) {
            retrofitServer = new RetrofitServer();
        }
        return retrofitServer;
    }

    public static ApiUrlManager getApiUrlManager() {
        if (apiUrlManager == null) {
            synchronized (ApiUrlManager.class) {
                apiUrlManager = getInstance().initApiUrlManager();
            }
        }
        return apiUrlManager;
    }

    public static class RetrofitServer {
        private static OkHttpClient okHttpClient;
        private static Retrofit retrofit;
        private static LogInterceptor interceptor;

        /**
         * 更新url
         *
         * @param url
         */
        public void updateBaseUrl(String url) {
            if (interceptor != null) {
                interceptor.setNewBaseUrl(url);
            }
        }

        /**
         * 初始化
         */
        public void init(String url) {
            baseUrl = url;
            initOkHttp(baseUrl);
            initRetrofit(baseUrl);
            initApiUrlManager();
        }

        /**
         * 初始化okhttp
         */
        private void initOkHttp(String baseUrl) {
            okHttpClient = new OkHttpClient().newBuilder()
                                             //设置读取超时时间
                                             .readTimeout(BaseNetConfig.DEFAULT_TIME, TimeUnit.SECONDS)
                                             //设置请求超时时间
                                             .connectTimeout(BaseNetConfig.DEFAULT_TIME, TimeUnit.SECONDS)
                                             //设置写入超时时间
                                             .writeTimeout(BaseNetConfig.DEFAULT_TIME, TimeUnit.SECONDS)
                                             //添加打印拦截器
                                             .addInterceptor(interceptor = new LogInterceptor(baseUrl))
                                             //设置出现错误进行重新连接。
                                             .retryOnConnectionFailure(true).build();
        }

        /**
         * 初始化Retrofit
         */
        private void initRetrofit(String baseUrl) {
            retrofit = new Retrofit.Builder().client(okHttpClient)
                                             .baseUrl(baseUrl)
                                             .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                             .addConverterFactory(GsonConverterFactory.create())
                                             .build();
        }

        private ApiUrlManager initApiUrlManager() {
            if (retrofit == null) {
                init(baseUrl);
            }
            return retrofit.create(ApiUrlManager.class);
        }
    }
}

