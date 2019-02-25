package com.android.keche.baidutieba.serials;

import java.io.Serializable;
import java.util.List;

/**
 * @author 谷恪忱
 * @brief 与FirstPageBean中的数据基本一样 （多了Bitmap类型）
 * Serializable接口使该类可被intent在活动间传递
 * 这里另外设置是为了避免Serializable接口对使用
 * Gson解析时造成的死循环超时
 * @note 不可使用Gson解析
 */
public class FirstPageDistribution implements Serializable {

    private String date;            /**< 发帖时间 **/

    private String title;           /**< 发帖标题 **/

    private String source;          /**< 来源 **/

    private String content;         /**< 发帖文字内容 **/

    //private String imageString;     /**< 发帖图片的字符串表示形式 **/

    private String userName;        /**< 发帖用户名 **/

    // private Bitmap userIcon;        /**< 发帖用户头像 **/

    private String userIconURL;  /**< 发帖用户头像字符串 **/

    private int shareNum;           /**< 分享数 **/

    private int commentNum;         /**< 评论数 **/

    private int likeNum;            /**< 点赞数 **/

    //private List<Bitmap> bitmaps;   /**< 发帖图片列表 **/

    private List<String> imageURLs;      /**< 发帖图片字符串列表 **/

    public FirstPageDistribution() {
    }

    public FirstPageDistribution(String date, String title, String source,
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

//    public Bitmap getUserIcon() {
//        return userIcon;
//    }
//
//    public void setUserIcon(Bitmap userIcon) {
//        this.userIcon = userIcon;
//    }

    public String getUserIconURL() {
        return userIconURL;
    }

    public void setUserIconURL(String userIconString) {
        this.userIconURL = userIconString;
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

//    public List<Bitmap> getBitmaps() {
//        return bitmaps;
//    }
//
//    public void setBitmaps(List<Bitmap> bitmaps) {
//        this.bitmaps = bitmaps;
//    }

    public List<String> getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(List<String> imageURLs) {
        this.imageURLs = imageURLs;
    }

}
