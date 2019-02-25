package com.android.keche.baidutieba.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.keche.baidutieba.R;
import com.android.keche.baidutieba.beans.TalkMessageBean;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author 谷恪忱
 * @brief 消息--聊天模块碎片
 * 可和好友、陌生人进行聊天 （#未实现）
 * 查看吧务等
 */
public class TalkFragment extends Fragment implements View.OnClickListener{



    private RecyclerView showRecycList;         /**< 聊天列表 **/

    private TalkViewAdapter talkAdapter;        /**< 聊天列表适配器 **/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_talk, container, false);
        showRecycList = (RecyclerView) view.findViewById(R.id.m_c_talk_show_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        talkAdapter = new TalkViewAdapter(DefaultDataProvider.createTalkTestDataList());
        showRecycList.setLayoutManager(layoutManager);
        showRecycList.setAdapter(talkAdapter);
        //添加分割线
        showRecycList.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_c_talk_show_list_item:
                Toast.makeText(this.getContext(), "你点击了聊天", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
    }

    class TalkViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout showListItemLayout;    /**< 列表项整体布局 **/

        private CircleImageView talkIcon;           /**< 聊天图标 **/

        private TextView talkName;                  /**< 聊天名称 **/

        private TextView talkDate;                  /**< 聊天日期 **/

        private TextView talkMsg;                   /**< 最新聊天信息 **/

        TalkViewHolder(@NonNull View itemView) {
            super(itemView);
            talkIcon = (CircleImageView) itemView.findViewById(R.id.m_c_talk_show_list_icon);
            talkName = (TextView) itemView.findViewById(R.id.m_c_talk_show_list_name);
            talkDate = (TextView) itemView.findViewById(R.id.m_c_talk_show_list_time);
            talkMsg = (TextView) itemView.findViewById(R.id.m_c_talk_show_list_msg);
            showListItemLayout = (LinearLayout) itemView.findViewById(R.id.m_c_talk_show_list_item);
            showListItemLayout.setOnClickListener(TalkFragment.this);
        }

    }

    class TalkViewAdapter extends RecyclerView.Adapter<TalkViewHolder> {

        private List<TalkMessageBean> talkMessageBeans;

        TalkViewAdapter(List<TalkMessageBean> talkMessageBeans) {
            this.talkMessageBeans = talkMessageBeans;
        }

        @NonNull
        @Override
        public TalkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.fragment_child_talk_list_item, viewGroup, false);
            return new TalkViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TalkViewHolder talkViewHolder, int i) {
            TalkMessageBean talkMessageBean = talkMessageBeans.get(i);
            if (talkMessageBean.getTalkIconId() != 0) {
                talkViewHolder.talkIcon.setImageResource(talkMessageBean.getTalkIconId());
            } else if (talkMessageBean.getTalkIcon() != null) {
                talkViewHolder.talkIcon.setImageBitmap(talkMessageBean.getTalkIcon());
            } else {
                talkViewHolder.talkIcon.setImageResource(R.drawable.m_n_talk_dark);
            }
            talkViewHolder.talkName.setText(talkMessageBean.getTalkName());
            talkViewHolder.talkDate.setText(talkMessageBean.getTalkDate());
            talkViewHolder.talkMsg.setText(talkMessageBean.getTalkMessage());
        }

        @Override
        public int getItemCount() {
            return talkMessageBeans == null ? 0 : talkMessageBeans.size();
        }
    }

}




