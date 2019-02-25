package com.android.keche.baidutieba.beans;

/**
 * 回复信息类
 */

public class ReplyDetailBean {
    private String nickName;/**<回复人的昵称*/
    private String userLogo;/**<回复人的图标*/
    private int id;/**<回复人的*/
    private String commentId;/**<评论的id*/
    private String content;/**<评论的内容*/
    private String status;/**<位置*/
    private String createDate;/**<创建日期*/

    public ReplyDetailBean(String nickName, String content) {
        this.nickName = nickName;
        this.content = content;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getNickName() {
        return nickName;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }
    public String getUserLogo() {
        return userLogo;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
    public String getCommentId() {
        return commentId;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public String getCreateDate() {
        return createDate;
    }

}
