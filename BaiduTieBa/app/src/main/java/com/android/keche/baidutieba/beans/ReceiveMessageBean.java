package com.android.keche.baidutieba.beans;

import android.graphics.Bitmap;

/**
 * @author 谷恪忱
 * @brief 他人回复本机用户的消息类
 *
 */
public class ReceiveMessageBean {

    private int userIconId;         /**< 回复者图标资源ID **/

    private Bitmap userIcon;        /**< 回复者图标 **/

    private String userName;        /**< 回复者名 **/

    private String receiveDate;     /**< 回复时间 **/

    private String sourceBar;       /**< 回复源（即用户曾经留言过的吧） **/

    private String replyMsg;        /**< 回复者的留言 **/

    private String mineRecentMsg;   /**< 用户在某个吧的留言 **/


    public ReceiveMessageBean() {
    }


    public ReceiveMessageBean(int userIconId, Bitmap userIcon, String userName,
                              String receiveDate, String sourceBar, String replyMsg, String mineRecentMsg) {
        this.userIconId = userIconId;
        this.userIcon = userIcon;
        this.userName = userName;
        this.receiveDate = receiveDate;
        this.replyMsg = replyMsg;
        this.sourceBar = sourceBar;
        this.mineRecentMsg = mineRecentMsg;
    }

    public int getUserIconId() {
        return userIconId;
    }

    public void setUserIconId(int userIconId) {
        this.userIconId = userIconId;
    }

    public Bitmap getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(Bitmap userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getSourceBar() {
        return sourceBar;
    }

    public void setSourceBar(String sourceBar) {
        this.sourceBar = sourceBar;
    }

    public String getReplyMsg() {
        return replyMsg;
    }

    public void setReplyMsg(String replyMsg) {
        this.replyMsg = replyMsg;
    }

    public String getMineRecentMsg() {
        return mineRecentMsg;
    }

    public void setMineRecentMsg(String mineRecentMsg) {
        this.mineRecentMsg = mineRecentMsg;
    }
}
