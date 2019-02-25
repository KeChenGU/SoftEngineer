package com.android.keche.baidutieba.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.keche.baidutieba.MainActivity;
import com.android.keche.baidutieba.R;
import com.android.keche.baidutieba.beans.ReceiveMessageBean;
import com.android.keche.baidutieba.beans.ReceiveMsgBean;
import com.android.keche.baidutieba.utils.HttpUtil;
import com.android.keche.baidutieba.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author 谷恪忱
 * @brief 消息--消息模块
 * 用于展示他人回复消息界面的碎片
 */
public class MessageFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RelativeLayout mineLayout;              /**< ‘我的’栏 **/

    private RelativeLayout likeLayout;              /**< ‘点赞’栏 **/

    private SwipeRefreshLayout refreshLayout;       /**< 刷新框 **/

    private RecyclerView replyRecycleView;          /**< 回复消息列表的视图 **/

    private ReplyMsgAdapter msgAdapter;             /**< 消息列表视图的适配器 **/

    private ReplyNetMsgAdapter msgNetAdapter;

    private List<ReceiveMessageBean> receiveMessageBeans;   /**< 接收信息列表 **/

    private List<ReceiveMsgBean> receiveMsgBeans;

    /**
     * @brief 生成消息模块视图的方法
     * 包含事件监听
     * @param inflater 视图展开器
     * @param container 容器
     * @param savedInstanceState 状态保存实例
     * @return 生成的碎片视图
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_message, container, false);
        initUIs(view);
        return view;
    }

    /**
     * @brief 设置消息模块UI控件的点击监听事件逻辑
     * @param v 碎片视图
     *
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_c_msg_mine_frame:
                Toast.makeText(this.getContext(), "你点击了我的！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.m_c_msg_like_frame:
                Toast.makeText(this.getContext(), "你点击了点赞！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * @brief 设置刷新的具体处理逻辑
     * 内部逻辑详见refreshData（List<ReceiveMessageBean>）私有方法
     */
    @Override
    public void onRefresh() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//              refreshData(DefaultDataProvider.createMessageTestData2List());
//            }
//        }).start();
        replyRecycleView.setAdapter(msgNetAdapter);
        sendGetMessageRequest();
    }

    /**
     * @brief 注册初始化消息模块控件
     * 简化onCreateView(...) 方法内部代码量
     * @param view 碎片主视图
     */
    private void initUIs(View view) {
        mineLayout = (RelativeLayout) view.findViewById(R.id.m_c_msg_mine_frame);
        likeLayout = (RelativeLayout) view.findViewById(R.id.m_c_msg_like_frame);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.m_c_msg_swipe_refresh);
        replyRecycleView = (RecyclerView) view.findViewById(R.id.m_c_msg_list);
        mineLayout.setOnClickListener(this);
        likeLayout.setOnClickListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        receiveMessageBeans = DefaultDataProvider.createMessageTestDataList();
        receiveMsgBeans = new ArrayList<>();
        replyRecycleView.setLayoutManager(layoutManager);
        msgAdapter = new ReplyMsgAdapter(receiveMessageBeans);
        msgNetAdapter = new ReplyNetMsgAdapter(receiveMsgBeans);
        replyRecycleView.setAdapter(msgAdapter);
    }

    /**
     * @brief 内部类， 视图承载器继承自RecyclerView的ViewHolder
     * 用于注册装载RecyclerView表子项item需要的UI控件
     */
    class ReplyViewHolder extends RecyclerView.ViewHolder {

        private ImageView userIcon;

        private TextView userName;

        private TextView replyDate;

        private TextView replyText;

        private TextView recentText;

        private TextView sourceBar;

        ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            userIcon = (ImageView) itemView.findViewById(R.id.m_c_msg_list_item_user_icon);
            userName = (TextView) itemView.findViewById(R.id.m_c_msg_list_item_user_name);
            replyDate = (TextView) itemView.findViewById(R.id.m_c_msg_list_item_date);
            replyText = (TextView) itemView.findViewById(R.id.m_c_msg_list_item_reply);
            recentText = (TextView) itemView.findViewById(R.id.m_c_msg_list_item_recent);
            sourceBar = (TextView) itemView.findViewById(R.id.m_c_msg_list_item_bar);
            replyText.setOnClickListener(MessageFragment.this);
            recentText.setOnClickListener(MessageFragment.this);
        }

    }

    /**
     * @brief 内部类， 使用ReplyViewHolder的适配器
     * 用于设置各item项中UI组件的具体内容（图片、文字）
     * @function onCreateViewHolder(...)
     * @function onBindViewHolder(...)
     * @function getItemCount()
     * 上述三个函数为继承RecyclerView.Adapter必须所重写的方法
     */
    class ReplyMsgAdapter extends RecyclerView.Adapter<ReplyViewHolder> {

        private List<ReceiveMessageBean> replyList;  /**< 回复消息类列表 **/

        ReplyMsgAdapter(List<ReceiveMessageBean> replyList) {
            this.replyList = replyList;
        }

        /**
         * @brief 生成视图承载器
         * @param viewGroup 视图团
         * @param i 第i个视图承载器
         * @return 初始化好的视图承载器
         * @note 需要预定义好对应的资源XML文件
         */
        @NonNull
        @Override
        public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.fragment_child_message_list_item, viewGroup, false);
            return new ReplyViewHolder(view);
        }

        /**
         * @brief 绑定，即设置ViewHolder中UI控件的具体内容
         * @param replyViewHolder 回复消息的视图承载器
         * @param i 回复消息列表的第i项
         */
        @Override
        public void onBindViewHolder(@NonNull ReplyViewHolder replyViewHolder, int i) {
            ReceiveMessageBean replyMsg = replyList.get(i);
            if (replyMsg.getUserIconId() != 0) {
                replyViewHolder.userIcon.setImageResource(replyMsg.getUserIconId());
            } else if (replyMsg.getUserIcon() != null){
                replyViewHolder.userIcon.setImageBitmap(replyMsg.getUserIcon());
            } else {
                replyViewHolder.userIcon.setImageResource(R.drawable.my_uselect);
            }
            replyViewHolder.userName.setText(replyMsg.getUserName());
            replyViewHolder.replyDate.setText(replyMsg.getReceiveDate());
            replyViewHolder.recentText.setText(replyMsg.getMineRecentMsg());
            replyViewHolder.replyText.setText(replyMsg.getReplyMsg());
            replyViewHolder.sourceBar.setText(replyMsg.getSourceBar());
        }

        @Override
        public int getItemCount() {
            return replyList == null ? 0 : replyList.size();
        }
    }


    class ReplyNetMsgAdapter extends  RecyclerView.Adapter<ReplyViewHolder> {

        private List<ReceiveMsgBean> receiveMsgBeans;

        ReplyNetMsgAdapter(List<ReceiveMsgBean> receiveMsgBeans) {
            this.receiveMsgBeans = receiveMsgBeans;
        }

        @NonNull
        @Override
        public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.fragment_child_message_list_item, viewGroup, false);
            return new ReplyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ReplyViewHolder replyViewHolder, int i) {
            ReceiveMsgBean msgBean = receiveMsgBeans.get(i);
            Glide.with(MessageFragment.this.getContext())
                    .load(MainActivity.DEFAULT_SERVER_URL + msgBean.getUserIconUrl())
                    .into(replyViewHolder.userIcon);
            replyViewHolder.userName.setText(msgBean.getUserName());
            replyViewHolder.recentText.setText(msgBean.getRecentMsg());
            replyViewHolder.replyText.setText(msgBean.getReplyMsg());
            replyViewHolder.sourceBar.setText(msgBean.getSourceBar());
            replyViewHolder.replyDate.setText(msgBean.getReceiveDate());
        }

        @Override
        public int getItemCount() {
            return receiveMsgBeans == null ? 0 : receiveMsgBeans.size();
        }
    }
    /**
     * @brief 刷新数据私有方法
     * @param receiveMessageBeanList 用于更新的数据列表
     */
    private void refreshData(final List<ReceiveMessageBean> receiveMessageBeanList) {
        try {
            Thread.sleep(1600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                receiveMessageBeans.clear();
                if (receiveMessageBeanList != null) {
                    receiveMessageBeans.addAll(receiveMessageBeanList);
                    msgAdapter.notifyDataSetChanged();
                }
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void refreshNetData(final List<ReceiveMsgBean> receiveMsgBeanList) {
        try {
            Thread.sleep(1600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                receiveMessageBeans.clear();
                if (receiveMsgBeanList != null) {
                    receiveMsgBeans.addAll(receiveMsgBeanList);
                    msgNetAdapter.notifyDataSetChanged();
                }
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void sendGetMessageRequest() {
        HttpUtil.getByOkHttp(MainActivity.DEFAULT_SERVER_URL
                + "MessageServlet?id=" + MainActivity.userEx.getId(),
                new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("gainMessageError", e.getMessage());
                MessageFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShort(MessageFragment.this.getActivity(), "刷新信息失败!");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (responseData != null && !responseData.equals("")) {
                    Log.e("parseMsg", responseData);
                    List<ReceiveMsgBean> messageBeans = new Gson().fromJson(responseData,
                            new TypeToken<List<ReceiveMsgBean>>(){}.getType());
                    refreshNetData(messageBeans);
                } else {
                    MessageFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (refreshLayout.isRefreshing()) {
                                refreshLayout.setRefreshing(false);
                            }
                            ToastUtils.showShort(MessageFragment.this.getContext(), "当前无消息！");
                        }
                    });
                }
            }
        });
    }


}
