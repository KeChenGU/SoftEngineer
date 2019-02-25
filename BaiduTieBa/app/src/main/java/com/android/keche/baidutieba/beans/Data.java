package com.android.keche.baidutieba.beans;

import java.util.List;

/***
 * 评论数据类
 */

public class Data {
        private int total; /***<总评论个数*/
        private List<CommentDetailBean> list;/***<评论消息的列表*/
        public void setTotal(int total) {
            this.total = total;
        }
        public int getTotal() {
            return total;
        }

        public void setList(List<CommentDetailBean> list) {
            this.list = list;
        }
        public List<CommentDetailBean> getList() {
            return list;
        }

}
