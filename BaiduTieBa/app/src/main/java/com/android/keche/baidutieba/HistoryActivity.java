package com.android.keche.baidutieba;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.keche.baidutieba.beans.HistoryBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HistoryActivity extends AppCompatActivity {

    private List<HistoryBean> historyBeans = new ArrayList<>();

    private HistoryAdapter adapter;// = new HistoryAdapter(historyBeans);

    private static final int CREATE_LIST = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case CREATE_LIST:
                    historyBeans.clear();
                    historyBeans.addAll((List<HistoryBean>)msg.obj);
                    adapter.notifyDataSetChanged();
                    break;
                default:
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.history_list);
        adapter = new HistoryAdapter(historyBeans);
        LinearLayoutManager layoutManager = new LinearLayoutManager(HistoryActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        //RecyclerView recyclerView = (RecyclerView) findViewById(R.id.history_list);
        sendOkHttpRequest();
//        HistoryAdapter adapter = new HistoryAdapter(historyBeans);
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
                    Request request = new Request.Builder().url("http://192.168.43.201/AndroidBaiduBar/HistoryServlet"
                    +"?user_id=" + MainActivity.user.getId()).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithGSON(responseData);
                    Log.e("parseHistory", responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
//                String reponsedata = "";
//
//                parseJSONWithGSON(reponsedata);
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        List<HistoryBean> beanList = gson.fromJson(jsonData, new TypeToken<List<HistoryBean>>(){}.getType());
//        historyBeans = beanList;
        Message msg = new Message();
        msg.obj = beanList;
        msg.what = CREATE_LIST;
        handler.sendMessage(msg);
    }

    private class HistoryViewHolder extends RecyclerView.ViewHolder{
        private TextView titleText;

        private TextView sourceText;

        private TextView timeText;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            timeText = (TextView) itemView.findViewById(R.id.history_time);
            sourceText = (TextView) itemView.findViewById(R.id.history_source);
            titleText = (TextView) itemView.findViewById(R.id.history_title);
        }
    }

    private class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

        private List<HistoryBean> historyBeans;

        HistoryAdapter(List<HistoryBean> historyBeans) {
            this.historyBeans = historyBeans;
        }

        @NonNull
        @Override
        public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item, viewGroup, false);
            HistoryViewHolder holder = new HistoryViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull HistoryViewHolder historyViewHolder, int i) {
            final HistoryBean bean = historyBeans.get(i);
            historyViewHolder.titleText.setText(bean.getTitle());
            historyViewHolder.sourceText.setText(bean.getSource());
            historyViewHolder.timeText.setText(bean.getTime());
            historyViewHolder.titleText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HistoryActivity.this, SearchPageExActivity.class);
                    intent.putExtra("content", bean.getTitle());
                    intent.putExtra("barName", bean.getSource());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return historyBeans == null ? 0 : historyBeans.size();
        }
    }
}
