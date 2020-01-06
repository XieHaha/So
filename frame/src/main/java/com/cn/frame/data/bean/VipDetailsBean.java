package com.cn.frame.data.bean;

import java.io.Serializable;

/**
 *
 */
public class VipDetailsBean implements Serializable {
    private static final long serialVersionUID = -3730682248830718305L;

    private String vip_id;
    private String start_time_format;
    private String end_time_format;
    private String usage_state;
    private String surplusTimeLeng;

    public String getVip_id() {
        return vip_id;
    }

    public void setVip_id(String vip_id) {
        this.vip_id = vip_id;
    }

    public String getStart_time_format() {
        return start_time_format;
    }

    public void setStart_time_format(String start_time_format) {
        this.start_time_format = start_time_format;
    }

    public String getEnd_time_format() {
        return end_time_format;
    }

    public void setEnd_time_format(String end_time_format) {
        this.end_time_format = end_time_format;
    }

    public String getUsage_state() {
        return usage_state;
    }

    public void setUsage_state(String usage_state) {
        this.usage_state = usage_state;
    }

    public String getSurplusTimeLeng() {
        return surplusTimeLeng;
    }

    public void setSurplusTimeLeng(String surplusTimeLeng) {
        this.surplusTimeLeng = surplusTimeLeng;
    }
}
