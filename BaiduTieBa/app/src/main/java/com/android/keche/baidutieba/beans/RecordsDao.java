package com.android.keche.baidutieba.beans;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.keche.baidutieba.helpers.RecordSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @breif 搜索记录操作类
 * @note
 * 函数如下
 * RecordsDao()
 * addRecords()
 * isHasRecord()
 * getRecordsList()
 * querySimlarRecord()
 * deleteAllRecords()
 * delete()
 */
public class RecordsDao {
    RecordSQLiteOpenHelper recordHelper; /**< 打开帮助类  */

    SQLiteDatabase recordsDb;  /**< SQLite数据库  */

    /**
     *
     * @param context 搜索记录操作的上下文
     * @brief 构造函数
     */
    public RecordsDao(Context context) {
        recordHelper = new RecordSQLiteOpenHelper(context);
    }

    /**
     *
     * @param record 用户输入要搜索的数据
     * @brief 向数据库中添加用户输入的数据
     */
    public void addRecords(String record)
    {
        if (!isHasRecord(record)) {
            recordsDb = recordHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", record);
            //添加
            recordsDb.insert("records", null, values);
            //关闭
            recordsDb.close();
        }
    }

    /**
     *
     * @param record 用户输入要搜索的数据
     * @return true 搜索记录存在 ，false 搜索记录不存在
     * @brief 判断是否存在该搜索记录
     */
    public boolean isHasRecord(String record) {
        boolean isHasRecord = false;
        recordsDb = recordHelper.getReadableDatabase();
        Cursor cursor = recordsDb.query("records", null, null, null, null, null, null);
        while (cursor.moveToNext())
        {
            if (record.equals(cursor.getString(cursor.getColumnIndexOrThrow("name"))))
            {
                isHasRecord = true;
            }
        }        //关闭数据库
              recordsDb.close();
        cursor.close();
        return isHasRecord;
    }
    /**
     *
     * @return 搜索记录的列表
     * @brief 获取全部搜索记录
     *
     */
    public List<String> getRecordsList()
    {
        List<String> recordsList = new ArrayList<>();
        recordsDb = recordHelper.getReadableDatabase();
        Cursor cursor = recordsDb.query("records", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            recordsList.add(name);
        }
        //关闭数据库
        recordsDb.close();
        cursor.close();
        return recordsList;
    }
    /**
     *
     * @param record 用户输入要搜索的数据
     * @return 与该搜索记录相匹配的数据中的列表
     * @brief 进行模糊查询
     */
     public List<String> querySimlarRecord(String record)
     {
         String queryStr = "select * from records where name like '%" + record + "%' order by name ";
         List<String> similarRecords = new ArrayList<>();
         Cursor cursor= recordHelper.getReadableDatabase().rawQuery(queryStr,null);
         while (cursor.moveToNext())
         {
             String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
             similarRecords.add(name);
         }
         cursor.close();
         return similarRecords;
     }
    //清空搜索记录

    /**
     * @brief
     * 清空数据库中所有记录
     */
    public void deleteAllRecords()
    {
        recordsDb = recordHelper.getWritableDatabase();
        recordsDb.execSQL("delete from records");
        recordsDb.close();
    }
    /**
     *
     * @param _id 数据库中的id
     * @return 删除记录的条数
     * @brief
     * 删除指定数据库中的某条记录
     */
    public int delete(int _id)
    {
        SQLiteDatabase db = recordHelper.getWritableDatabase();
        int d = db.delete("records", "_id=?", new String[] { _id + "" });
        db.close();
        return d;
    }
    }
