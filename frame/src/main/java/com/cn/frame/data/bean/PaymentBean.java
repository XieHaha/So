package com.cn.frame.data.bean;

import java.io.Serializable;

public class PaymentBean implements Serializable {
    private static final long serialVersionUID = 7421649647227771762L;

    private String payment_address;

    public String getPayment_address() {
        return payment_address;
    }

    public void setPayment_address(String payment_address) {
        this.payment_address = payment_address;
    }
}
