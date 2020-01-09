package com.cn.frame.data;

public interface BaseData {
    /**
     * 基础type 0
     */
    int BASE_ZERO = 0;
    /**
     * 基础type 1
     */
    int BASE_ONE = 1;
    /**
     * 基础type 2
     */
    int BASE_TWO = 2;
    /**
     * 基础type3
     */
    int BASE_THREE = 3;
    /**
     * 男
     */
    int BASE_MALE = 1;
    /**
     * 女
     */
    int BASE_FEMALE = 2;
    /**
     * 男女皆可
     */
    int BASE_NEUTRAL = 3;
    /**
     * 分页数据每页数量
     */
    int PAGE_SIZE = 10;

    /**
     * 身份证号最大长度
     */
    int BASE_ID_CARD_LENGTH = 18;
    /**
     * 手机号默认长度
     */
    int BASE_PHONE_DEFAULT_LENGTH = 11;
    /**
     * 验证码默认长度
     */
    int BASE_VERIFY_CODE_DEFAULT_LENGTH = 6;
    /**
     * 多选图片最大数量
     */
    int BASE_IMAGE_SIZE_MAX = 10;
    /**
     * 验证码二次获取默认时间
     */
    int BASE_MAX_RESEND_TIME = 60;
    /**
     * session时间
     */
    int BASE_MAX_SESSION_TIME = 7200;
    /**
     * 适配华为  裁剪
     */
    String BASE_HONOR_NAME = "HONOR";
    /**
     * 适配华为  裁剪
     */
    String BASE_HUAWEI_NAME = "HUAWEI";
    /**
     * 登出广播
     */
    String BASE_SIGN_OUT_ACTION = "zyc.doctor.logout.action";
    /**
     * token失效
     */
    String BASE_TOKEN_ERROR_ACTION = "zyc.doctor.token.action";
    /**
     * 账号禁用
     */
    String BASE_ACCOUNT_ERROR_ACTION = "zyc.doctor.account.action";
    /**
     * 接口固定参数
     */
    String ADMIN = "ANDROID";
    /**
     * 聊天
     */
    String BASE_CHAT_CHANNEL = "d_base_chat_channel";
}
