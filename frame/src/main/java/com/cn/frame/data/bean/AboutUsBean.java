package com.cn.frame.data.bean;

import java.io.Serializable;

public class AboutUsBean implements Serializable {
    private static final long serialVersionUID = 1963841827689067007L;

    private String client_app_name;
    private String client_app_version;
    private String client_customer_service;
    private String client_customer_qq;
    private String client_customer_wechat;
    private String client_nickname;
    private String privacy_policy;
    private String user_agreement;

    public String getClient_app_name() {
        return client_app_name;
    }

    public void setClient_app_name(String client_app_name) {
        this.client_app_name = client_app_name;
    }

    public String getClient_app_version() {
        return client_app_version;
    }

    public void setClient_app_version(String client_app_version) {
        this.client_app_version = client_app_version;
    }

    public String getClient_customer_service() {
        return client_customer_service;
    }

    public void setClient_customer_service(String client_customer_service) {
        this.client_customer_service = client_customer_service;
    }

    public String getClient_nickname() {
        return client_nickname;
    }

    public void setClient_nickname(String client_nickname) {
        this.client_nickname = client_nickname;
    }

    public String getPrivacy_policy() {
        return privacy_policy;
    }

    public void setPrivacy_policy(String privacy_policy) {
        this.privacy_policy = privacy_policy;
    }

    public String getUser_agreement() {
        return user_agreement;
    }

    public void setUser_agreement(String user_agreement) {
        this.user_agreement = user_agreement;
    }

    public String getClient_customer_qq() {
        return client_customer_qq;
    }

    public void setClient_customer_qq(String client_customer_qq) {
        this.client_customer_qq = client_customer_qq;
    }

    public String getClient_customer_wechat() {
        return client_customer_wechat;
    }

    public void setClient_customer_wechat(String client_customer_wechat) {
        this.client_customer_wechat = client_customer_wechat;
    }
}
