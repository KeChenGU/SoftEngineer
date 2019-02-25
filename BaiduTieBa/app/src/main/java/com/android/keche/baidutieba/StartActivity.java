package com.android.keche.baidutieba;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (MainActivity.user != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
        try {
            Thread.sleep(1000); //线程休眠1s，使出现白屏时的效果更加明显
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //展示2s后前往主页
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

}
