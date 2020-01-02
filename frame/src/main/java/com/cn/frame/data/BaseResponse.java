package com.cn.frame.data;

import java.io.Serializable;

/**
 * @date 2015/12/31
 */
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = -4363411262887201686L;
    /**
     * 请求响应码
     */
    int code;
    int count;
    String type;
    /**
     * 请求响应文本
     */
    String msg;
    /**
     * 响应实体类
     */
    T data;

    public int getCode() {
        return code;
    }

    public BaseResponse setCode(int code) {
        this.code = code;
        return this;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public BaseResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    @SuppressWarnings({"unchecked", "UnusedDeclaration"})
    public <T extends Object> T getData() {
        return (T) data;
    }

    public BaseResponse setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "BaseResponse{" + "code=" + code + ", '" + (msg == null ? "msg为空" : "msg=" + msg) + '\'' + ", " +
                (data == null ? "data为空" : "data=" + data.toString()) + '}';
    }
}
