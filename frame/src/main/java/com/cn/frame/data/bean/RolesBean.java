package com.cn.frame.data.bean;

import java.io.Serializable;

/**
 *
 */
public class RolesBean implements Serializable {
    private static final long serialVersionUID = 1214351172233327897L;
    private int sex;
    private int age;
    private int occupation;
    private int online_state;
    private int usage_state;
    private int auth_state;
    private int collection_state;
    private String user_id;
    private String rong_cloud_user_id;
    private int attribute;
    private String nickname;
    private String location;
    private String head_portrait;
    private String individuality_signature;
    private String address;
    private String last_login_time;

    public String getRong_cloud_user_id() {
        return rong_cloud_user_id;
    }

    public void setRong_cloud_user_id(String rong_cloud_user_id) {
        this.rong_cloud_user_id = rong_cloud_user_id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getOccupation() {
        return occupation;
    }

    public void setOccupation(int occupation) {
        this.occupation = occupation;
    }

    public int getOnline_state() {
        return online_state;
    }

    public void setOnline_state(int online_state) {
        this.online_state = online_state;
    }

    public int getUsage_state() {
        return usage_state;
    }

    public void setUsage_state(int usage_state) {
        this.usage_state = usage_state;
    }

    public int getAuth_state() {
        return auth_state;
    }

    public void setAuth_state(int auth_state) {
        this.auth_state = auth_state;
    }

    public int getCollection_state() {
        return collection_state;
    }

    public void setCollection_state(int collection_state) {
        this.collection_state = collection_state;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        this.head_portrait = head_portrait;
    }

    public String getIndividuality_signature() {
        return individuality_signature;
    }

    public void setIndividuality_signature(String individuality_signature) {
        this.individuality_signature = individuality_signature;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(String last_login_time) {
        this.last_login_time = last_login_time;
    }

    public int getAttribute() {
        return attribute;
    }

    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
