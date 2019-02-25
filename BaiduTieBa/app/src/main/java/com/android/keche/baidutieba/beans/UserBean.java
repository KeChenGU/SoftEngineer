package com.android.keche.baidutieba.beans;

import java.io.Serializable;

/**
 * @author 黄兆翔
 * @breif 用户类的创建、修改和查看
 */
public class UserBean implements Serializable {

    private int id;                /**< 用户id **/

    private String name;           /**< 用户名 **/

    private String password;       /**< 密码 **/

    /**
     * @breif 用户表的构造方法
     * @param name 用户名
     * @param password 密码
     */
    public UserBean(String name, String password) {
        this.name = name;
        this.password = password;
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

    /**
     * @breif 读取用户表
     * @return 用户名密码
     */
    @Override
    public String toString() {
        return "UserBean{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}