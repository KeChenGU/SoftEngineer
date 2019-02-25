package com.android.keche.baidutieba.beans;

/**
 * @author 马宏涛
 * @brief 用户类的创建、修改和查看 该类用于网络传递 是 UserBean的加强版
 */
public class UserExBean  {

    private int id;                 /**< 用户id **/

    private String name;            /**< 用户名 **/

    private String password;        /**< 密码 **/

    private String iconUrl;      /**< 图标字符串 **/

    private String old;             /**< 吧年龄 **/

    private int fansNum;            /**< 粉丝数 **/

    private int focusNum;           /**< 关注吧数 **/

    /**
     * @breif 用户表的构造方法
     * @param name 用户名
     * @param password 密码
     */
    public UserExBean(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public UserExBean(String name, String password, String iconUrl, int fansNum, int focusNum) {
        this.name = name;
        this.password = password;
        this.iconUrl = iconUrl;
        this.fansNum = fansNum;
        this.focusNum = focusNum;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * @breif 返回用户名
     * @return 用户名
     */
    public String getName() {
        return name;
    }

    /**
     * @breif 设置用户名
     * @param name 用户名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @breif 返回密码
     * @return 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * @breif 设置密码
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getOld() {
        return old;
    }

    public void setOld(String old) {
        this.old = old;
    }

    public int getFansNum() {
        return fansNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }

    public int getFocusNum() {
        return focusNum;
    }

    public void setFocusNum(int focusNum) {
        this.focusNum = focusNum;
    }

    /**
     * @breif 读取用户表
     * @return 用户名密码
     */
    @Override
    public String toString() {
        return "UserExBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", old='" + old + '\'' +
                ", fansNum=" + fansNum +
                ", focusNum=" + focusNum +
                '}';
    }
}