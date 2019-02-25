package com.android.keche.baidutieba.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
//import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author 谷恪忱
 * @brief 自定义网络工具类
 * 封装了常见网络请求方法
 *
 */

public final class HttpUtil {

    /**
     * @brief 自定义网络请求结果回调接口
     * onFinish() 表示请求成功
     * onError() 表示请求失败
     */
    public interface HttpCallBackListener {

        void onFinish(String response);

        void onError(Exception e);

    }


    /**
     * @brief 通过GET方式请求 数据 使用okhttp3的CallBack接口处理请求结果
     * @param url 请求网络地址
     * @param callback okhttp3类下的回调接口
     */
    public static void getByOkHttp(final String url,
                                   final okhttp3.Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    okHttpClient.newCall(request).enqueue(callback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * @brief 发送POST请求（带Form表单） 使用okhttp3的CallBack接口处理请求结果
     * @param url 请求网络地址
     * @param column 生成表单的字段
     * @param value 生成表单的字段值（键值）
     * @param callback okhttp3类下的回调接口
     * @note String[] column 和 String[] value 必须长度相等
     * 长度不等 或两者为null、长度为0的情况都将不做处理
     */
    public static void postByOkHttp(final String url,
                                    final String[] column,
                                    final String[] value,
                                    final okhttp3.Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    FormBody.Builder builder = new FormBody.Builder();
                    if (column == null || value == null) {
                        return;
                    }
                    if (column.length != value.length) {
                        return;
                    }
                    for (int i = 0; i < column.length; ++i) {
                        builder.add(column[i], value[i]);
                    }
                    RequestBody requestBody = builder.build();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();
                    okHttpClient.newCall(request).enqueue(callback);
                } catch (Exception e) {
                    Log.e("postError", e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * @brief 通过GET方式请求数据 使用自定义回调接口HttpCallBackListener处理返回结果
     * 请求通过java原生HttpURLConnection类
     * @param url 请求网络地址
     * @param httpCallBackListener 自定义网络请求结果回调接口
     */
    public static void getByHttpURL(final String url,
                                    final HttpCallBackListener httpCallBackListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                BufferedReader bufferedReader = null;
                try {

                    URL _url = new URL(url);
                    httpURLConnection = (HttpURLConnection) _url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(8000);
                    httpURLConnection.setReadTimeout(8000);

                    InputStream inputStream = httpURLConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    final StringBuilder stringBuilder = new StringBuilder();
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    if (httpCallBackListener != null) {
                        httpCallBackListener.onFinish(stringBuilder.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (httpCallBackListener != null) {
                        httpCallBackListener.onError(e);
                    }
                } finally {
                    if( bufferedReader != null ){
                        try {
                            bufferedReader.close();
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                    if( httpURLConnection != null ){
                        httpURLConnection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void postByHttpURL(final String url,
                                     final String[] column,
                                     final String[] value,
                                     final HttpCallBackListener httpCallBackListener) {

    }




}
