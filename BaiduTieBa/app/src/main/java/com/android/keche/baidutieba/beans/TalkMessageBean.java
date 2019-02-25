package com.android.keche.baidutieba.beans;


import android.graphics.Bitmap;

/**
 * @author 谷恪忱
 * @brief 消息--聊天模块实体信息类
 * 用于存储展示在RecyclerView中的子项UI控件具体值信息
 */
public class TalkMessageBean {

    private int talkIconId;     /**< 聊天图标资源ID **/

    private Bitmap talkIcon;    /**< 聊天图标 **/

    private String talkName;    /**< 聊天名称 **/

    private String talkDate;    /**< 聊天日期 **/

    private String talkMessage; /**< 最新聊天信息 **/

    public TalkMessageBean() {
    }

    public TalkMessageBean(int talkIconId, Bitmap talkIcon, String talkName, String talkDate, String talkMessage) {
        this.talkIconId = talkIconId;
        this.talkIcon = talkIcon;
        this.talkName = talkName;
        this.talkDate = talkDate;
        this.talkMessage = talkMessage;
    }

    public int getTalkIconId() {
        return talkIconId;
    }

    public void setTalkIconId(int talkIconId) {
        this.talkIconId = talkIconId;
    }

    public Bitmap getTalkIcon() {
        return talkIcon;
    }

    public void setTalkIcon(Bitmap talkIcon) {
        this.talkIcon = talkIcon;
    }

    public String getTalkName() {
        return talkName;
    }

    public void setTalkName(String talkName) {
        this.talkName = talkName;
    }

    public String getTalkDate() {
        return talkDate;
    }

    public void setTalkDate(String talkDate) {
        this.talkDate = talkDate;
    }

    public String getTalkMessage() {
        return talkMessage;
    }

    public void setTalkMessage(String talkMessage) {
        this.talkMessage = talkMessage;
    }
}
