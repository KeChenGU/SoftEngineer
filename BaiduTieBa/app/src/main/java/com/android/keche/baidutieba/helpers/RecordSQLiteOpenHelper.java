package com.android.keche.baidutieba.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author 夏辉
 * @breif 搜索记录帮助类
 * @note
 * 函数如下
 * RecordSQLiteOpenHelper()
 * onCreate()
 *
 *
 */

public class RecordSQLiteOpenHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "temp.db";  /**< 数据库名称 */
    private final static int DB_VERSION = 1; /**< 数据库版本 */

    /**
     *
     * @param context 记录搜索历史的上下文
     * @brief  构造数据库函数
     *
     */
    public  RecordSQLiteOpenHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    /**
     *
     * @param db 数据库对象
     * @brief 创建历史搜索记录表
     *
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlStr="CREATE TABLE IF NOT EXISTS records (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);";
        db.execSQL(sqlStr);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
