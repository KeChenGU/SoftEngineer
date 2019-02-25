package com.android.keche.baidutiebar.server.bean;

public class ReceiveMsgBean {

    private String userIconUrl;     /**< 用户图标url **/

    private String userName;        /**< 用户名 **/

    private String receiveDate;     /**< 回复时间 **/

    private String sourceBar;       /**< 回复源（即用户曾经留言过的吧） **/

    private String replyMsg;        /**< 回复者的留言 **/

    private String recentMsg;       /**< 用户在某个吧的留言 **/


    public ReceiveMsgBean() {
    }


    public ReceiveMsgBean(String userIconUrl, String userName, String receiveDate, String sourceBar, String replyMsg, String recentMsg) {
        this.userIconUrl = userIconUrl;
        this.userName = userName;
        this.receiveDate = receiveDate;
        this.sourceBar = sourceBar;
        this.replyMsg = replyMsg;
        this.recentMsg = recentMsg;
    }

    public String getUserIconUrl() {
        return userIconUrl;
    }

    public void setUserIconUrl(String userIconUrl) {
        this.userIconUrl = userIconUrl;
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

    public String getRecentMsg() {
        return recentMsg;
    }

    public void setRecentMsg(String recentMsg) {
        this.recentMsg = recentMsg;
    }
}
