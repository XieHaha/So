package com.cn.frame.data;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * @date 20/3/13 14:58
 * @des
 */
public class ChatBean extends LitePalSupport implements Serializable {
    private static final long serialVersionUID = -5353560505210195451L;
    private String chatId;
    private int msgCount;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }
}
