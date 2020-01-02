package com.cn.frame.data;

/**
 * 此类为任务队列编号，根据任务队列编号确定是否取消的任务
 *
 */
public enum Tasks {
    /**
     * 获取验证码
     */
    GET_VERIFY_CODE,
    /**
     * 登录 注册
     */
    LOGIN_AND_REGISTER,
    /**
     * 版本更新
     */
    GET_VERSION,
}

