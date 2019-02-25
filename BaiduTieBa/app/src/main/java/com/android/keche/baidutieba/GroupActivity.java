package com.android.keche.baidutieba;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.keche.baidutieba.beans.GroupBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GroupActivity extends AppCompatActivity {

    private List<GroupBean> GroupBeans;

    private static final int CREATE_LIST = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case CREATE_LIST:
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.group_list);
                    GroupAdapter adapter = new GroupAdapter(GroupBeans);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(GroupActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    break;
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.group_list);
        //sendOkHttpRequest();
//        GroupAdapter adapter = new GroupAdapter(GroupBeans);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
    }

    private void sendOkHttpRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://192.168.43.201/AndroidBaiduBar/GroupServlet").build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithGSON(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        List<GroupBean> beanList = gson.fromJson(jsonData, new TypeToken<List<GroupBean>>(){}.getType());
        GroupBeans = beanList;
    }

    private class GroupViewHolder extends RecyclerView.ViewHolder{
        private TextView titleText;

        private TextView sourceText;

        private TextView timeText;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            timeText = (TextView) itemView.findViewById(R.id.history_time);
            sourceText = (TextView) itemView.findViewById(R.id.history_source);
            titleText = (TextView) itemView.findViewById(R.id.history_title);
        }
    }

    private class GroupAdapter extends RecyclerView.Adapter<GroupViewHolder> {

        private List<GroupBean> GroupBeans;

        public GroupAdapter(List<GroupBean> GroupBeans) {
            this.GroupBeans = GroupBeans;
        }

        @NonNull
        @Override
        public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item, viewGroup, false);
            GroupViewHolder holder = new GroupViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull GroupViewHolder GroupViewHolder, int i) {
            GroupBean bean = GroupBeans.get(i);
            GroupViewHolder.titleText.setText(bean.getTitle());
            GroupViewHolder.sourceText.setText(bean.getSource());
            GroupViewHolder.timeText.setText(bean.getTime());
        }

        @Override
        public int getItemCount() {
            return GroupBeans == null ? 0 : GroupBeans.size();
        }
    }
}
