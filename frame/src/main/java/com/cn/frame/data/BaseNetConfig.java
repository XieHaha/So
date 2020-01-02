package com.cn.frame.data;

/**
 * @date 2016/11/29
 */
public interface BaseNetConfig {
    /**
     * 请求成功编码
     */
    int REQUEST_SUCCESS = 200;
    /**
     * 其他错误
     */
    int REQUEST_OTHER_ERROR = 4000;
    /**
     * token错误或者失效
     */
    int REQUEST_TOKEN_ERROR = 4001;
    /**
     * 账号禁用
     */
    int REQUEST_ACCOUNT_ERROR = 4005;
    /**
     * 服务器
     */
    int REQUEST_SERVER_ERROR = 4010;
    /**
     * 设置默认超时时间
     */
    int DEFAULT_TIME = 30;
}
