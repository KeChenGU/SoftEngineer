package com.android.keche.baidutieba.beans;

import android.graphics.Bitmap;

/**
 * @brief 最近逛的吧信息类
 */
public class RecentBar {

    //private Bitmap barIcon; /**< 图标 */

    private String iconUrl;/**<图标以字符串方式显示*/

    private String barName;  /**< 图标名称 */

    public RecentBar() {
    }

    public RecentBar(String iconUrl, String barName) {
        this.iconUrl = iconUrl;
        this.barName = barName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getBarName() {
        return barName;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }
}
