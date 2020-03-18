package com.cn.frame.data.bean;

import java.io.Serializable;

public class PicturePathBean implements Serializable {
    private static final long serialVersionUID = 1129321068886771962L;

    private String picture_path;
    private int picture_type;
    private int position;
    private int id;

    public String getPicture_path() {
        return picture_path;
    }

    public void setPicture_path(String picture_path) {
        this.picture_path = picture_path;
    }

    public int getPicture_type() {
        return picture_type;
    }

    public void setPicture_type(int picture_type) {
        this.picture_type = picture_type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
