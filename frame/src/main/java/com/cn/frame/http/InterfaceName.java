package com.cn.frame.http;

/**
 * 接口名
 */
public interface InterfaceName {
    /**
     * 登录
     */
    String SIGN_IN = "signIn";
    /**
     * 退出
     */
    String SIGN_OUT = "signOut";
    /**
     * 注册
     */
    String SIGN_UP = "signUp";
    /**
     * 发送验证码
     */
    String SEND_CAPTCHA = "sendCaptcha";
    /**
     * 密码重置
     */
    String PWD_RESET = "pwdReset";
    /**
     * 基础数据集合
     */
    String GET_BASICS_INFO = "getBasicsInfo";
    /**
     * 基础数据集合
     */
    String RENEW_SIGN = "renewSign";
    /**
     * 首页数据
     */
    String HOME_INFO = "homeInfo";
    /**
     * 关注列表
     */
    String COLLECTION_LIST = "collectionList";
    /**
     * 关注
     */
    String RENEW_COLLECTION = "renewCollection";
    /**
     * 关于我们
     */
    String ABOUT_US = "aboutUs";
    /**
     * 帮助中心
     */
    String HELPS_LIST = "helpsList";
    /**
     * 问题反馈
     */
    String QUESTION_FEEDBACK = "questionFeedback";
    /**
     * 屏蔽列表
     */
    String SHIELD_LIST = "shieldList";
    /**
     * 卡信息
     */
    String GET_CARD_INFO = "getCardInfo";
    /**
     * 会员
     */
    String UPGRADE_MEMBERSHIP = "upgradeMembership";
    /**
     * 认证
     */
    String AUTH = "auth";
    /**
     * 认证
     */
    String EDIT_USER_INFO = "editUserInfo";
    /**
     * 省信息
     */
    String GET_PROVINCE_INFO = "getProvinceInfo";
    /**
     * 市信息
     */
    String GET_CITY_INFO = "getCityInfo";
    /**
     * 用户信息
     */
    String GET_USER_INFO = "getUserInfo";
    /**
     * 用户屏蔽
     */
    String SHIELD_USER = "shieldUser";

}
