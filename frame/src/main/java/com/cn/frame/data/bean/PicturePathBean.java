package com.cn.frame.data.bean;

import java.io.Serializable;

public class PicturePathBean implements Serializable {
    private static final long serialVersionUID = 1129321068886771962L;

    private String picture_path;

    public String getPicture_path() {
        return picture_path;
    }

    public void setPicture_path(String picture_path) {
        this.picture_path = picture_path;
    }
}
