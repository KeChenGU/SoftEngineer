package com.android.keche.baidutieba;

import android.Manifest;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import butterknife.OnClick;

/**
 * @author 黄兆翔
 * @breif 个人信息展示
 */
public class MypageActivity extends AppCompatActivity {

    private ImageButton ibBackMain;         /**< 返回‘我的’页面按钮 **/

    private TextView userName;

    private TextView old;

    private TextView fansNum;

    private TextView focusNum;

    private ImageView userIconImage;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        ibBackMain = (ImageButton) findViewById(R.id.ib_backmain);
        userName = (TextView) findViewById(R.id.detail_user_name);
        old = (TextView) findViewById(R.id.bar_old);
        fansNum = (TextView) findViewById(R.id.fans_num);
        focusNum = (TextView) findViewById(R.id.focus_num);
        userIconImage = (ImageView) findViewById(R.id.user_icon_image);
        if (MainActivity.user != null) {
            userName.setText(MainActivity.user.getName());
            if (MainActivity.userEx != null) {
                old.setText("吧龄：" + MainActivity.userEx.getOld());
                fansNum.setText(String.valueOf(MainActivity.userEx.getFansNum()));
                focusNum.setText(String.valueOf(MainActivity.userEx.getFocusNum()));
                Glide.with(this)
                        .load(MainActivity.DEFAULT_SERVER_URL + MainActivity.userEx.getIconUrl()).into(userIconImage);
            }
        }
    }

    @OnClick({
            R.id.ib_backmain
    })

    /**
     * @breif 完成返回到‘我的’页面
     */
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ib_backmain:
                Intent intent = new Intent(MypageActivity.this,MyActivity.class);
                startActivity(intent);
                MypageActivity.this.finish();
                break;
        }
    }
}
