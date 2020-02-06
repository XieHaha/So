package com.cn.frame.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class UserDetailBean implements Serializable {
    private static final long serialVersionUID = -2151380113838938807L;
    private int user_id;
    private int somatotype;
    private int race;
    private int education;
    private int marriage;
    private int children;
    private int smoke;
    private int drink;
    private int contact_object;
    private int follow_state;
    private int auth_state;
    private int usage_state;
    private String head_portrait;
    private String nickname;
    private String individuality_signature;
    private String address;
    private ArrayList<PicturePathBean> album;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getSomatotype() {
        return somatotype;
    }

    public void setSomatotype(int somatotype) {
        this.somatotype = somatotype;
    }

    public int getRace() {
        return race;
    }

    public void setRace(int race) {
        this.race = race;
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

    public int getContact_object() {
        return contact_object;
    }

    public void setContact_object(int contact_object) {
        this.contact_object = contact_object;
    }

    public int getFollow_state() {
        return follow_state;
    }

    public void setFollow_state(int follow_state) {
        this.follow_state = follow_state;
    }

    public int getAuth_state() {
        return auth_state;
    }

    public void setAuth_state(int auth_state) {
        this.auth_state = auth_state;
    }

    public int getUsage_state() {
        return usage_state;
    }

    public void setUsage_state(int usage_state) {
        this.usage_state = usage_state;
    }

    public String getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        this.head_portrait = head_portrait;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public ArrayList<PicturePathBean> getAlbum() {
        return album;
    }

    public void setAlbum(ArrayList<PicturePathBean> album) {
        this.album = album;
    }
}
