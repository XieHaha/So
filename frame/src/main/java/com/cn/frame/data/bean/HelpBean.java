package com.cn.frame.data.bean;

import java.io.Serializable;

public class HelpBean implements Serializable {
    private static final long serialVersionUID = -7256043653396363108L;

    private String title;
    private String reply;
    private String create_time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
