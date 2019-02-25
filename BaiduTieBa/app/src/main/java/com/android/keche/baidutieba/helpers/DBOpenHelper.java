package com.android.keche.baidutieba.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.keche.baidutieba.beans.UserBean;

import java.util.ArrayList;

/**
 * @author 黄兆翔
 * @breif 建立数据库中的用户表，完成增删改查
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;          /**< 数据库 **/

    /**
     * @breif 类的构造函数
     * @param context 路径
     */
    public DBOpenHelper(Context context){
        super(context,"db_test",null,1);
        db = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS user(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }

    /**
     * @breif 增
     * @param name 用户名
     * @param password 密码
     */
    public void add(String name,String password){
        db.execSQL("INSERT INTO user (name,password) VALUES(?,?)",new Object[]{name,password});
    }

    /**
     *@breif 删
     * @param name 用户名
     * @param password 密码
     */
    public void delete(String name,String password){
        db.execSQL("DELETE FROM user WHERE name = AND password ="+name+password);
    }

    /**
     *@breif 改密码
     * @param password 密码
     */
    public void updata(String password){
        db.execSQL("UPDATE user SET password = ?",new Object[]{password});
    }

    /**
     * @breif 查询表的全部内容
     * @return 查询所得结果
     */
    public ArrayList<UserBean> getAllData(){

        ArrayList<UserBean> list = new ArrayList<UserBean>();
        Cursor cursor = db.query("user",null,null,null,null,null,"name DESC");
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            list.add(new UserBean(name,password));
        }
        return list;
    }
}
