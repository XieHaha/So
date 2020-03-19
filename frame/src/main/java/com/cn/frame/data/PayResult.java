package com.cn.frame.data;

/**
 * @date 20/3/19 13:24
 * @des
 */
public interface PayResult {
    /**
     * 订单支付成功
     */
    String ORDER_SUCCESS = "0000";
    /**
     * 订单支付失败
     */
    String ORDER_FAILED = "0001";
    /**
     * 订单支付中
     */
    String ORDER_PAYING = "0002";
    /**
     * 用户取消
     */
    String ORDER_CANCEL = "0013";
    /**
     * 参数错误
     */
    String ORDER_PARAM_ERROR = "0020";
    /**
     * 网络出错
     */
    String ORDER_NETWORK_ERROR = "0031";
    /**
     * 其他错误
     */
    String ORDER_OTHER_ERROR = "0032";
}
