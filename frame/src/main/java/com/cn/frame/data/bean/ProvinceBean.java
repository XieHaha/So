package com.cn.frame.data.bean;

import java.io.Serializable;

public class ProvinceBean implements Serializable {
    private static final long serialVersionUID = 8883751651822890660L;

    private int ordinal;
    private int id;
    private String name;

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
