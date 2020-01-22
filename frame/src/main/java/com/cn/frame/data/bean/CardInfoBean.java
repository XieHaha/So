package com.cn.frame.data.bean;

import java.io.Serializable;

public class CardInfoBean implements Serializable {
    private static final long serialVersionUID = -594252763970719572L;

    private int card_id;
    private long card_duration;
    private int flag;
    private float card_money;

    private String card_name;
    private String card_duration2;
    private String create_time;


    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public long getCard_duration() {
        return card_duration;
    }

    public void setCard_duration(long card_duration) {
        this.card_duration = card_duration;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public float getCard_money() {
        return card_money;
    }

    public void setCard_money(float card_money) {
        this.card_money = card_money;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getCard_duration2() {
        return card_duration2;
    }

    public void setCard_duration2(String card_duration2) {
        this.card_duration2 = card_duration2;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
