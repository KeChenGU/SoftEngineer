package com.android.keche.baidutieba.beans;

import java.util.List;

/**
 * 评论信息类
 */

public class CommentBean {
    private int code;/**<代码*/
    private String message;/**<全文*/
    private Data data;/**<数据*/
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }
}
