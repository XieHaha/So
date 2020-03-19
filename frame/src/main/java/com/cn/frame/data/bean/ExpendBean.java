package com.cn.frame.data.bean;

import java.io.Serializable;

/**
 * @date 20/3/19 13:19
 * @des
 */
public class ExpendBean implements Serializable {
    private static final long serialVersionUID = -3531720737336927996L;

    private String pay_info;

    public String getPay_info() {
        return pay_info;
    }

    public void setPay_info(String pay_info) {
        this.pay_info = pay_info;
    }
}
