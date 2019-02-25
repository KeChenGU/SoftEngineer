package com.android.keche.baidutieba;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.OnClick;

/**
 * @author 黄兆翔
 * @breif “我的”页面，一个用户各种数据入口的界面
 */


public class MyActivity extends AppCompatActivity implements View.OnClickListener{

    private Button gerenxinxi; /**< 进入个人信息的按钮 **/

    private TextView userName;

    private ImageView userIcon;

    private ImageButton myStore;
    private ImageButton myHistory;
    private ImageButton myGroup;
    //private TextView myStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        //创建监听器
        gerenxinxi = (Button) findViewById(R.id.gerenxinxi);
        //gerenxinxi.setOnClickListener(listener);
        gerenxinxi.setOnClickListener(this);
        userName = (TextView) findViewById(R.id.user_name);
        userIcon = (ImageView) findViewById(R.id.user_icon);
        myGroup = (ImageButton) findViewById(R.id.MyGroup);
        myHistory = (ImageButton) findViewById(R.id.MyHistory);
        myStore = (ImageButton) findViewById(R.id.MyStore);
        myGroup.setOnClickListener(this);
        myHistory.setOnClickListener(this);
        myStore.setOnClickListener(this);
        if (MainActivity.user != null) {
            userName.setText(MainActivity.user.getName());
            if (MainActivity.userEx != null) {
                Glide.with(this)
                        .load(MainActivity.DEFAULT_SERVER_URL + MainActivity.userEx.getIconUrl())
                        .into(userIcon);
            }
        }
        Button logOut = (Button) findViewById(R.id.login_out);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.user != null) {
                    MainActivity.user = null;
                    MainActivity.user = null;
                }
                startActivity(new Intent(MyActivity.this, MainActivity.class));
                MyActivity.this.finish();
            }
        });
    }

    /**
     * @breif 切换到个人信息视图的方法
     */
//    Button.OnClickListener listener = new Button.OnClickListener(){
//        public void onClick(View v){
//            Intent intent = new Intent(MyActivity.this, MypageActivity.class);
//            startActivity(intent);
//            MyActivity.this.finish();
//        }
//    };

//    @OnClick({
//            R.id.gerenxinxi,
//            R.id.MyGroup,
//            R.id.MyHistory,
//            R.id.MyStore
//    })
    public void onClick(View view){
        switch (view.getId()){
            case R.id.gerenxinxi:
                startActivity(new Intent(this, MypageActivity.class));
                finish();
                break;
            case R.id.MyGroup:
                startActivity(new Intent(this, GroupActivity.class));
                finish();
                break;
            case R.id.MyHistory:
                startActivity(new Intent(this, HistoryActivity.class));
                finish();
                break;
            case R.id.MyStore:
                startActivity(new Intent(this, StoreActivity.class));
                finish();
                break;
        }
    }
}
