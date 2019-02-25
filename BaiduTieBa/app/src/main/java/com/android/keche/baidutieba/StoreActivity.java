package com.android.keche.baidutieba;

import android.content.Intent;
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

import com.android.keche.baidutieba.beans.StoreBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StoreActivity extends AppCompatActivity {

    private List<StoreBean> StoreBeans = new ArrayList<>();

    //private StoreHandler handler = new StoreHandler();

    private StoreAdapter adapter;

    private static final int CREATE_LIST = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case CREATE_LIST:
                    StoreBeans.clear();
                    StoreBeans.addAll((List<StoreBean>)msg.obj);
                    adapter.notifyDataSetChanged();
                    break;
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.Store_list);
        adapter = new StoreAdapter(StoreBeans);
        LinearLayoutManager layoutManager = new LinearLayoutManager(StoreActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        sendOkHttpRequest();
//        StoreAdapter adapter = new StoreAdapter(StoreBeans);
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
                    Request request = new Request.Builder().url("http://192.168.43.201/AndroidBaiduBar/StoreServlet" +
                            "?user_id=" + MainActivity.user.getId()).build();
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
        List<StoreBean> beanList = gson.fromJson(jsonData, new TypeToken<List<StoreBean>>(){}.getType());
        //StoreBeans = beanList;
        Message msg = new Message();
        msg.obj = beanList;
        msg.what = CREATE_LIST;
        handler.sendMessage(msg);
    }

    private class StoreViewHolder extends RecyclerView.ViewHolder{
        private TextView titleText;

        private TextView sourceText;

        private TextView timeText;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            timeText = (TextView) itemView.findViewById(R.id.history_time);
            sourceText = (TextView) itemView.findViewById(R.id.history_source);
            titleText = (TextView) itemView.findViewById(R.id.history_title);
        }
    }

    private class StoreAdapter extends RecyclerView.Adapter<StoreViewHolder> {

        private List<StoreBean> StoreBeans;

        public StoreAdapter(List<StoreBean> StoreBeans) {
            this.StoreBeans = StoreBeans;
        }

        @NonNull
        @Override
        public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item, viewGroup, false);
            StoreViewHolder holder = new StoreViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final StoreViewHolder StoreViewHolder, int i) {
            final StoreBean bean = StoreBeans.get(i);
            StoreViewHolder.titleText.setText(bean.getTitle());
            StoreViewHolder.sourceText.setText(bean.getSource());
            StoreViewHolder.timeText.setText(bean.getTime());
            StoreViewHolder.titleText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StoreActivity.this, SearchPageExActivity.class);
                    intent.putExtra("content", bean.getTitle());
                    intent.putExtra("barName", bean.getSource());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return StoreBeans == null ? 0 : StoreBeans.size();
        }
    }

//    private class StoreHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            //super.handleMessage(msg);
//            switch (msg.what) {
//
//            }
//        }
//    }
}
