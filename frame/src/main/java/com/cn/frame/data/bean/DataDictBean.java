package com.cn.frame.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 数据字典
 */
public class DataDictBean implements Serializable {
    private static final long serialVersionUID = 4069355328557575517L;
    private String certification_fee;
    private ArrayList<String> contactObject;
    private ArrayList<String> constellationInfo;
    private ArrayList<String> education;
    private ArrayList<String> smokeOrDrink;
    private ArrayList<String> marriage ;
    private ArrayList<String> somatotype;
    private ArrayList<String> race;
    private ArrayList<String> beInterestedIn;
    private ArrayList<String> children;
    private ArrayList<String> income;
    private ArrayList<String> lifeStyle;
    private ArrayList<String> occupationInfo;

    public String getCertification_fee() {
        return certification_fee;
    }

    public void setCertification_fee(String certification_fee) {
        this.certification_fee = certification_fee;
    }

    public ArrayList<String> getContactObject() {
        return contactObject;
    }

    public void setContactObject(ArrayList<String> contactObject) {
        this.contactObject = contactObject;
    }

    public ArrayList<String> getConstellationInfo() {
        return constellationInfo;
    }

    public void setConstellationInfo(ArrayList<String> constellationInfo) {
        this.constellationInfo = constellationInfo;
    }

    public ArrayList<String> getEducation() {
        return education;
    }

    public void setEducation(ArrayList<String> education) {
        this.education = education;
    }

    public ArrayList<String> getSmokeOrDrink() {
        return smokeOrDrink;
    }

    public void setSmokeOrDrink(ArrayList<String> smokeOrDrink) {
        this.smokeOrDrink = smokeOrDrink;
    }

    public ArrayList<String> getMarriage() {
        return marriage;
    }

    public void setMarriage(ArrayList<String> marriage) {
        this.marriage = marriage;
    }

    public ArrayList<String> getSomatotype() {
        return somatotype;
    }

    public void setSomatotype(ArrayList<String> somatotype) {
        this.somatotype = somatotype;
    }

    public ArrayList<String> getRace() {
        return race;
    }

    public void setRace(ArrayList<String> race) {
        this.race = race;
    }

    public ArrayList<String> getBeInterestedIn() {
        return beInterestedIn;
    }

    public void setBeInterestedIn(ArrayList<String> beInterestedIn) {
        this.beInterestedIn = beInterestedIn;
    }

    public ArrayList<String> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<String> children) {
        this.children = children;
    }

    public ArrayList<String> getIncome() {
        return income;
    }

    public void setIncome(ArrayList<String> income) {
        this.income = income;
    }

    public ArrayList<String> getLifeStyle() {
        return lifeStyle;
    }

    public void setLifeStyle(ArrayList<String> lifeStyle) {
        this.lifeStyle = lifeStyle;
    }

    public ArrayList<String> getOccupationInfo() {
        return occupationInfo;
    }

    public void setOccupationInfo(ArrayList<String> occupationInfo) {
        this.occupationInfo = occupationInfo;
    }
}
