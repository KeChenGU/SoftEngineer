package com.android.keche.baidutieba;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.keche.baidutieba.fragments.MessageFragment;
import com.android.keche.baidutieba.fragments.TalkFragment;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        getSupportFragmentManager().beginTransaction().replace(R.id.test_layout, new TalkFragment()).commit();
    }
}
