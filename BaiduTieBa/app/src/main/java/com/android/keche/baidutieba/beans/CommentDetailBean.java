package com.android.keche.baidutieba.beans;

import java.util.List;

/**
 * 评论详细信息类
 */

public class CommentDetailBean {
    private int id;/**<id*/
    private String nickName;/**<昵称*/
    private String userLogo;/**<使用者的logo*/
    private String content;/**<评论的内容*/
    private String imgId;/**<图片id*/
    private int replyTotal;/**<总记得回复数*/
    private String createDate;/**<评论的创建日期*/
    private List<ReplyDetailBean> replyList;/**<回复列表*/

    public CommentDetailBean(String nickName,  String content, String createDate) {
        this.nickName = nickName;
        this.content = content;
        this.createDate = createDate;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
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

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }
    public String getImgId() {
        return imgId;
    }

    public void setReplyTotal(int replyTotal) {
        this.replyTotal = replyTotal;
    }
    public int getReplyTotal() {
        return replyTotal;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public String getCreateDate() {
        return createDate;
    }

    public void setReplyList(List<ReplyDetailBean> replyList) {
        this.replyList = replyList;
    }
    public List<ReplyDetailBean> getReplyList() {
        return replyList;
    }
}
