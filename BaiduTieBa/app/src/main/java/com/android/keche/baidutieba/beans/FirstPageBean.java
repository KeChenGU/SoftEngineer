package com.android.keche.baidutieba.beans;

import java.util.List;

/**
 * @author 马宏涛
 * @brief 用于显示首页帖子的信息封装类
 */
public class FirstPageBean {

    private String date;            /**< 发帖时间 **/

    private String title;           /**< 发帖标题 **/

    private String source;          /**< 来源 **/

    private String content;         /**< 发帖文字内容 **/

    //private String imageString;     /**< 发帖图片的字符串表示形式 **/

    private String userName;        /**< 发帖用户名 **/

    private String userIconURL;  /**< 发帖用户头像 **/

    private int shareNum;           /**< 分享数 **/

    private int commentNum;         /**< 评论数 **/

    private int likeNum;            /**< 点赞数 **/

    private List<String> imageURLs;      /**< 发帖图片字符串列表 **/

    public FirstPageBean() {
    }

    public FirstPageBean(String date, String title, String source,
                         String content, String userName, String userIconURL,
                         int shareNum, int commentNum, int likeNum, List<String> imageURLs) {
        this.date = date;
        this.title = title;
        this.source = source;
        this.content = content;
        this.userName = userName;
        this.userIconURL = userIconURL;
        this.shareNum = shareNum;
        this.commentNum = commentNum;
        this.likeNum = likeNum;
        this.imageURLs = imageURLs;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getShareNum() {
        return shareNum;
    }

    public void setShareNum(int shareNum) {
        this.shareNum = shareNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public List<String> getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(List<String> imageURLs) {
        this.imageURLs = imageURLs;
    }
}
