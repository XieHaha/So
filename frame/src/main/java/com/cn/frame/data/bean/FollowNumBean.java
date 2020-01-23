package com.cn.frame.data.bean;

import java.io.Serializable;

/**
 * @创建者 顿顿
 * @创建时间 ${DATA} 21:11
 * @描述
 */
public class FollowNumBean implements Serializable {
    private static final long serialVersionUID = 4837837253324738216L;

    private int collection_num;
    private int attention_num;

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
}
