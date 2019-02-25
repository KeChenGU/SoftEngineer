package com.android.keche.baidutieba.beans;

public class HistoryBean {

    private String title;

    private String source;

    private String time;

    public HistoryBean() {
    }

    public HistoryBean(String title, String source, String time) {
        this.title = title;
        this.source = source;
        this.time = time;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}