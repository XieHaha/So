package com.cn.frame.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class UserBaseBean implements Serializable {
    private static final long serialVersionUID = 271432271629996237L;
    private long due_time;
    private String session_id;
    private UserInfoBean userInfo;
    private ArrayList<VipDetailsBean> vipDetails;


    public long getDue_time() {
        return due_time;
    }

    public void setDue_time(long due_time) {
        this.due_time = due_time;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public ArrayList<VipDetailsBean> getVipDetails() {
        return vipDetails;
    }

    public void setVipDetails(ArrayList<VipDetailsBean> vipDetails) {
        this.vipDetails = vipDetails;
    }
}
