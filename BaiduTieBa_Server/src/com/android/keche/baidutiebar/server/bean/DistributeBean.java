package com.android.keche.baidutiebar.server.bean;

public class DistributeBean {

    private int publicFlag;         /**< 是否公开 **/

    private String date;            /**< 发帖时间 **/

    private String title;           /**< 发帖标题 **/

    private String content;         /**< 发帖文字内容 **/

    private String imageURL;     /**< 发帖图片的字符串表示形式 **/

    private String userName;        /**< 发帖用户名 **/

    private String userIconURL;  /**< 发帖用户头像 **/

    public DistributeBean() {
    }

    public DistributeBean(int publicFlag, String date, String title, String content, String imageURL, String userName, String userIconURL) {
        this.publicFlag = publicFlag;
        this.date = date;
        this.title = title;
        this.content = content;
        this.imageURL = imageURL;
        this.userName = userName;
        this.userIconURL = userIconURL;
    }

    public int getPublicFlag() {
        return publicFlag;
    }

    public void setPublicFlag(int publicFlag) {
        this.publicFlag = publicFlag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIconURL() {
        return userIconURL;
    }

    public void setUserIconURL(String userIconURL) {
        this.userIconURL = userIconURL;
    }
}

