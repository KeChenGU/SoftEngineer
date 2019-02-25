package com.android.keche.baidutieba.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * @author 谷恪忱
 * @brief 自定义网络数据解析类（XMl、JSON）
 * 封装了常用的XMl和JSON解析方法
 */
public final class ParseUtil {

    /**
     * @brief 使用将XMl数据解析为用；隔开的字符串
     * @param xml XML字符串数据
     * @param parseTags 解析标签名
     * @param endTag 终止标名
     * @return 解析后的字符串数据
     */
    public static String parseXMLToNormalStringByPull(String xml, String[] parseTags, String endTag) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();

            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();

            xmlPullParser.setInput(new StringReader(xml));

            int eventType = xmlPullParser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        for (int i = 0; i < parseTags.length; ++i) {
                            if (parseTags[i].equals(nodeName)) {
                                String value = xmlPullParser.nextText();
                                stringBuilder.append(nodeName).append(":")
                                        .append(value).append(";");//.append(";");
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
//                            for (int i = 0; i < parseTags.length; ++i) {
//                                if (parseTags[i].equals(nodeName)) {
//                                    stringBuilder.append(" ");
//                                }
//                            }
                        if (endTag.equals(nodeName)) {
                            stringBuilder.append("\n");
                        } /*else {
                                stringBuilder.append(";");
                            }*/
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return stringBuilder.toString();
    }

    /**
     * @brief 通过JSONObject将JSON字符串解析为用换行符'\n'隔开的通常字符串
     * @param json json字符串
     * @param attributes 键值属性
     * @note JSONObject属于org.json包 使用时需预先知道要解析的键值属性
     * @return 解析后字符串
     */
    public static String parseJSONToNormalStringByJSONObject(String json, String[] attributes) {
//            try {
//                JSONObject jsonObject = new JSONObject(json);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        JSONObject jsonObject = getOrgJSONObject(json);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (String attr: attributes) {
                stringBuilder.append(attr).append(":")
                        .append(jsonObject.getString(attr)).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HttpUtil:", e.getMessage() + " " + e.getCause());
        }
        return stringBuilder.toString();
    }

    /**
     * @brief 通过通过JSONObject将JSON字符串解析为用换行符'\n'隔开的通常字符串
     * @param json json字符串
     * @note JSONObject属于org.json包 是上一个方法（String json, String[] attributes）的重载
     * 无需提前知道键值
     * @return 解析后的换行字符串
     *
     */
    public static String parseJSONToNormalStringByJSONObject(String json) {
        JSONObject jsonObject = getOrgJSONObject(json);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = jsonObject.getString(key);
                stringBuilder.append(key).append(":")
                        .append(value).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HttpUtil", e.getMessage() + " " + e.getCause());
        }
        return stringBuilder.toString();
    }


    /**
     * @brief 使用JSONArray
     * @param json json字符串
     * @note 使用的JSONArray属于org.json包
     * @return 解析后的字符串
     */
    public static String parseJSONToNormalStringByJSONArray(String json) {
        JSONArray jsonArray = getOrgJSONArray(json);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Iterator<String> iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = jsonObject.getString(key);
                    stringBuilder.append(key).append(":")
                            .append(value).append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HttpUtil", e.getMessage() + " " + e.getCause());
        }
        return stringBuilder.toString();
    }

    /**
     * @brief 使用第三方开源库Gson将Json字符串转换为实体信息类
     * @param json json字符串
     * @param type 实体信息类的类型
     * @note 解析的实体类不可以是其他类的子类或者重写了接口
     * @return 解析后的实体信息类
     */
    public static Object parseJSONToObjectByGson(String json, Type type) {
        Object o = new Gson().fromJson(json, type);
        return o;
    }

    /**
     * @brief 使用第三方开源库Gson解析json字符串为信息实体类
     * @param json json字符串
     * @param typeToken 类型签名，Gson解析集合泛型类专用（如List<E>\Map<V, K>等）
     * @note 解析的实体类不可以是其他类的子类或者重写了接口
     * @return 解析后的实体类
     */
    public static Object parseJSONToObjectByGson(String json, TypeToken typeToken) {
        Object o = new Gson().fromJson(json, typeToken.getType());
        return o;
    }


    /**
     * @brief 私有方法，将json字符串转化为有效的org.json.JSONObject类型的对象
     * 仅为简化公有方法冗余代码用
     * @param json json字符串
     * @return JSONObject类型对象
     * @retval 失败时返回null
     */
    private static JSONObject getOrgJSONObject(String json) {
        //JSONObject jsonObject = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            //StringBuilder stringBuilder = new StringBuilder();
            //stringBuilder.append();
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        //return jsonObject;
    }

    /**
     * @brief 私有方法，将json字符串转化为有效的org.json.JSONArray类型的对象
     * 仅为简化公有方法冗余代码用
     * @param json json字符串
     * @return JSONOArray类型对象
     * @retval 失败时返回null
     */
    private static JSONArray getOrgJSONArray(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            return jsonArray;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }





}



