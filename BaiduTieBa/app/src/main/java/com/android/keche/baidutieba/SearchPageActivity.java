package com.android.keche.baidutieba;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;



/**
 * @brief 跳转需要输入的指定页面
 */

public class SearchPageActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_web);
        WebView webView = (WebView) findViewById(R.id.search_web);
        webView.loadUrl("http://tieba.baidu.com/f?kw=" +  MyBarNetActivity.str);
    }
}
