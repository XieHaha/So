package com.cn.frame.data;

/**
 * 此类为任务队列编号，根据任务队列编号确定是否取消的任务
 */
public enum Tasks {
    /**
     * 获取验证码
     */
    GET_VERIFY_CODE,
    /**
     * 登录
     */
    LOGIN,
    /**
     * 退出
     */
    SIGN_OUT,
    /**
     * 注册
     */
    REGISTER,
    /**
     * 重置密码
     */
    RESET_PWD,
    /**
     * 版本更新
     */
    GET_VERSION,
    /**
     * 获取基础数据集合
     */
    GET_BASICS_INFO,
    /**
     * session更新
     */
    RENEW_SIGN,
    /**
     * 首页数据
     */
    GET_HOME_INFO,
    /**
     * 关注更新
     */
    RENEW_COLLECTION,
}

