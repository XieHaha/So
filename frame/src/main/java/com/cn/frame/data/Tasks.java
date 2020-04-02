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
    /**
     * 关注列表
     */
    COLLECTION_LIST,
    /**
     * 关于我们
     */
    ABOUT_US,
    /**
     * 帮助中心
     */
    HELPS_LIST,
    /**
     * 问题反馈
     */
    QUESTION_FEEDBACK,
    /**
     * 举报
     */
    REPORT,
    /**
     * 屏蔽列表
     */
    SHIELD_LIST,
    /**
     * 会员卡信息
     */
    GET_CARD_INFO,
    /**
     * 会员
     */
    UPGRADE_MEMBERSHIP,
    /**
     * 认证
     */
    AUTH,
    /**
     * 信息编辑
     */
    EDIT_USER_INFO,
    /**
     * 省信息
     */
    GET_PROVINCE_INFO,
    /**
     * 市信息
     */
    GET_CITY_INFO,
    /**
     * 用户信息
     */
    GET_USER_INFO,
    /**
     * 屏蔽
     */
    SHIELD_USER,
    /**
     * 图片删除
     */
    PICTURE_DEL,
    /**
     * 机器消息
     */
    SEND_ROBOT,
}

