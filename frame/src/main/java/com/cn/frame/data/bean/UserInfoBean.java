package com.cn.frame.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 */
public class UserInfoBean implements Serializable {
    private static final long serialVersionUID = -4807152809361865645L;
    private String user_id;
    private String rong_cloud_user_id;
    private int sex;
    private int register_type;
    private int age;
    private int height;
    private int weight;
    private int occupation;
    private int constellation;
    private int education;
    private int marriage;
    private int children;
    private int smoke;
    private int drink;
    private int somatotype;
    private int contact_object;
    private int be_interested_in;
    private int follow_state;
    private int race;
    private int net_assets;
    private int annual_income;
    private int visitor_number;
    private int auth_state;
    private int online_state;
    private int is_auth;
    private int collection_num;
    private int attention_num;
    private int life_style;
    /**
     * 1 普通  2  运营
     */
    private int attribute;
    private String qq;
    private String mobile_number;
    private String nickname;
    private String date_of_birth;
    private String head_portrait;
    private String area_code;
    private String individuality_signature;
    private String making_friends_goals;
    private String wechat;
    private String email;
    private String create_time;
    private String rong_cloud_token;
    private String address;
    private String location;
    private int usage_state;
    private int shield_state;

    private ArrayList<PicturePathBean> album;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

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

    public int getRegister_type() {
        return register_type;
    }

    public void setRegister_type(int register_type) {
        this.register_type = register_type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getOccupation() {
        return occupation;
    }

    public void setOccupation(int occupation) {
        this.occupation = occupation;
    }

    public int getConstellation() {
        return constellation;
    }

    public void setConstellation(int constellation) {
        this.constellation = constellation;
    }

    public int getEducation() {
        return education;
    }

    public void setEducation(int education) {
        this.education = education;
    }

    public int getMarriage() {
        return marriage;
    }

    public void setMarriage(int marriage) {
        this.marriage = marriage;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public int getSmoke() {
        return smoke;
    }

    public void setSmoke(int smoke) {
        this.smoke = smoke;
    }

    public int getDrink() {
        return drink;
    }

    public void setDrink(int drink) {
        this.drink = drink;
    }

    public int getSomatotype() {
        return somatotype;
    }

    public void setSomatotype(int somatotype) {
        this.somatotype = somatotype;
    }

    public int getContact_object() {
        return contact_object;
    }

    public void setContact_object(int contact_object) {
        this.contact_object = contact_object;
    }

    public int getBe_interested_in() {
        return be_interested_in;
    }

    public void setBe_interested_in(int be_interested_in) {
        this.be_interested_in = be_interested_in;
    }

    public int getRace() {
        return race;
    }

    public void setRace(int race) {
        this.race = race;
    }

    public int getNet_assets() {
        return net_assets;
    }

    public void setNet_assets(int net_assets) {
        this.net_assets = net_assets;
    }

    public int getAnnual_income() {
        return annual_income;
    }

    public void setAnnual_income(int annual_income) {
        this.annual_income = annual_income;
    }

    public int getVisitor_number() {
        return visitor_number;
    }

    public void setVisitor_number(int visitor_number) {
        this.visitor_number = visitor_number;
    }

    public int getAuth_state() {
        return auth_state;
    }

    public void setAuth_state(int auth_state) {
        this.auth_state = auth_state;
    }

    public int getOnline_state() {
        return online_state;
    }

    public void setOnline_state(int online_state) {
        this.online_state = online_state;
    }

    public int getCollection_num() {
        return collection_num;
    }

    public void setCollection_num(int collection_num) {
        this.collection_num = collection_num;
    }

    public int getAttention_num() {
        return attention_num;
    }

    public void setAttention_num(int attention_num) {
        this.attention_num = attention_num;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        this.head_portrait = head_portrait;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getIndividuality_signature() {
        return individuality_signature;
    }

    public void setIndividuality_signature(String individuality_signature) {
        this.individuality_signature = individuality_signature;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }


    public String getRong_cloud_token() {
        return rong_cloud_token;
    }

    public void setRong_cloud_token(String rong_cloud_token) {
        this.rong_cloud_token = rong_cloud_token;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIs_auth() {
        return is_auth;
    }

    public void setIs_auth(int is_auth) {
        this.is_auth = is_auth;
    }

    public int getLife_style() {
        return life_style;
    }

    public void setLife_style(int life_style) {
        this.life_style = life_style;
    }

    public String getMaking_friends_goals() {
        return making_friends_goals;
    }

    public void setMaking_friends_goals(String making_friends_goals) {
        this.making_friends_goals = making_friends_goals;
    }

    public int getUsage_state() {
        return usage_state;
    }

    public void setUsage_state(int usage_state) {
        this.usage_state = usage_state;
    }

    public int getFollow_state() {
        return follow_state;
    }

    public void setFollow_state(int follow_state) {
        this.follow_state = follow_state;
    }

    public ArrayList<PicturePathBean> getAlbum() {
        return album;
    }

    public void setAlbum(ArrayList<PicturePathBean> album) {
        this.album = album;
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

    public int getShield_state() {
        return shield_state;
    }

    public void setShield_state(int shield_state) {
        this.shield_state = shield_state;
    }
}
