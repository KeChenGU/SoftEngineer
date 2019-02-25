package com.android.keche.baidutieba;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class SearchPageExActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page_ex);
        WebView detailView = (WebView) findViewById(R.id.see_web_detail);
        Intent intent = getIntent();
        if (intent == null) {
            this.finish();
            return;
        }
        String keyWord = intent.getStringExtra("barName");
        String quoteWord = intent.getStringExtra("content");
        if (keyWord == null || quoteWord == null) {
            this.finish();
            return;
        }
        //http://tieba.baidu.com/f/search/res?ie=utf-8&kw=xxx&qw=yyy
        detailView.loadUrl("http://tieba.baidu.com/f/search/res?ie=utf-8&kw=" + keyWord + "&qw=" + quoteWord);
    }
}
