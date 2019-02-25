package com.android.keche.baidutieba.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.keche.baidutieba.R;

import java.util.List;

/**
 * @author 夏辉
 * @brief 搜索历史记录适配器
 * @note
 * 函数如下
 * SearchRecordsAdapter()
 * getCount()
 * getItem()
 * getItemId()
 * getView()
 * @class
 * ViewHolder
 */
public class SearchRecordsAdapter extends BaseAdapter {
    private Context context; /**<搜索历史适配器上下文  */
    private List<String> searchRecordsList;/**< 搜索记录列表*/
    private LayoutInflater inflater;/**<动态载入界面*/

    /**
     *
     * @param context 搜索历史记录适配器上下文
     * @param searchRecordsList 搜索记录的列表
     * @brief
     * 构造函数
     */
    public SearchRecordsAdapter(Context context,List<String> searchRecordsList){
        this.context=context;
        this.searchRecordsList=searchRecordsList;
        inflater =LayoutInflater.from(context);
    }

    /**
     *
     * @return 返回数据库中历史记录的条数
     * @brief 如果历史记录条数大于5 ，则返回5
     * 如果历史条数小于或者等于5，则返回数据库中历史记录的条数
     */
    @Override
    public int getCount(){
        return searchRecordsList.size() > 5 ? 5 : searchRecordsList.size();
    }

    /**
     *
     * @param position 记录所在数据库中的位置
     * @return 数据库中的记录
     * @brief
     * 得到数据库中已存在的数据
     */
    @Override
    public Object getItem(int position) {
        return searchRecordsList.size() == 0 ? null : searchRecordsList.get(position);
    }

    /**
     *
     * @param position 记录所在数据库中的位置
     * @return 记录所在数据库中的位置
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     *
     * @param position 该视图在适配器数据中的位置
     * @param convertView 旧视图
     * @param parent 此视图最终会被附加到的父级视图
     * @return  视图
     * @brief
     * 用来获得指定位置要显示的View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(null == convertView){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter,null);

            viewHolder.recordTv = (TextView) convertView.findViewById(R.id.search_content_tv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String content = searchRecordsList.get(position);
        viewHolder.recordTv.setText(content);
        return convertView;
    }

    /**
     * @brief 定义TextView
     */
    private class ViewHolder{
        TextView recordTv;
    }

}
