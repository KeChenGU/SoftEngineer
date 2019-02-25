package com.android.keche.baidutieba;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.keche.baidutieba.beans.UserBean;
import com.android.keche.baidutieba.helpers.DBOpenHelper;

import java.time.chrono.MinguoChronology;
import java.util.ArrayList;

import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;



/**
 * @author 黄兆翔
 * @breif 实现短信验证码登录
 */
public class PhoneLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private DBOpenHelper mDBOpenHelper;             /**< 工具类 **/
    private Button btGoLogin;                       /**< 跳转默认登录页面按钮 **/
    private ImageView ivBackLogin;                  /**< 跳转账号密码登录页面按钮 **/
    private EditText etUsername;                    /**< 电话号码输入栏 **/
    private EditText etPhoneCode;                   /**< 短信验证码输入栏 **/
    private Button btGetPhoneCode;                  /**< 获取短信验证码按钮 **/
    private Button btLogin;                          /**< 登录按钮 **/
    private String username;                         /**< 保存电话号码字符串 **/
    private String phonecode;                        /**< 保存短信验证码字符串 **/
    EventHandler eventHandler;                        /**< 发送短信事件处理器 **/
    private int time=60;                              /**< 获取短信验证码倒计时 **/
    private boolean flag = true;                     /**< 出错的标志 **/

    /**
     * @breif 初始化和定义各个数据，注册短信事件
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        mDBOpenHelper = new DBOpenHelper(this);

        //定义控件
        btGoLogin = (Button) findViewById(R.id.bt_phoneloginactivity_gologin);
        ivBackLogin = (ImageView) findViewById(R.id.ib_phoneloginacivity_backlogin);
        etUsername = (EditText) findViewById(R.id.et_phoneloginactivity_username);
        etPhoneCode = (EditText) findViewById(R.id.et_phoneloginactivity_phonecode);
        btGetPhoneCode = (Button) findViewById(R.id.bt_phoneloginactivity_getphonecode);
        btLogin = (Button) findViewById(R.id.bt_phoneloginactivity_login);

        eventHandler = new EventHandler(){
            public void afterEvent(int event, int result, Object data){
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    /**
     *@breif 完成短信验证后，销毁回调监听接口
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    /**
     * @breif 使用Handler来分发Message对象到主线程中处理事件
     * 完成校对验证码工作
     */
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
//            if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
//                if(result == SMSSDK.RESULT_COMPLETE){
//                    boolean smart = (Boolean)data;
//                    if(smart) {
//                        Toast.makeText(getApplicationContext(),"该手机号已经注册过，请重新输入",
//                                Toast.LENGTH_LONG).show();
//                        etUsername.requestFocus();
//                        return;
//                    }
//                }
//            }
            if(result==SMSSDK.RESULT_COMPLETE)
            {
//                String name = etUsername.getText().toString().trim();
//                ArrayList<UserBean> datas = mDBOpenHelper.getAllData();
//                boolean match = false;
//                for(int i=0;i<datas.size();i++) {
//                    UserBean user = datas.get(i);
//                    if (name.equals(user.getName())){
//                        match = true;
//                        break;
//                    }else{
//                        match = false;
//                    }
//                }
//
//                if(match){
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "验证码输入正确",
                                Toast.LENGTH_LONG).show();
                        if (MainActivity.user == null) {
                            MainActivity.user = new UserBean("", "");
                        }
                        String name = etUsername.getText().toString().trim();
                        MainActivity.user.setName(name);
                        Intent intent = new Intent(getApplicationContext(), MyActivity.class);
                        startActivity(intent);
                        finish();//销毁此Activity
                    }
//                }else{
//                    Toast.makeText(getApplicationContext(), "没有该账号，请重新输入",
//                            Toast.LENGTH_LONG).show();
//                    etUsername.requestFocus();
//                }

            }
            else
            {
                if(flag)
                {
                    btGetPhoneCode.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"验证码获取失败请重新获取", Toast.LENGTH_LONG).show();
                    etUsername.requestFocus();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"验证码输入错误", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    @OnClick({
            R.id.bt_phoneloginactivity_gologin,
            R.id.ib_registeractivity_backlogin,
            R.id.bt_phoneloginactivity_getphonecode,
            R.id.bt_phoneloginactivity_login
    })

    /**
     * @breif 实现跳转账号密码登录页面功能和短信验证码登录功能，通过view.getId()响应不同功能
     * 获取验证码按钮相响应judPhone()方法，校对电话格式
     * 登录按钮响应judCord()方法，校对短信验证码
     */
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_phoneloginactivity_gologin:
            case R.id.ib_phoneloginacivity_backlogin:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.bt_phoneloginactivity_getphonecode:
                if(judPhone())//去掉左右空格获取字符串
                {
                    SMSSDK.getVerificationCode("86",username);
                    etPhoneCode.requestFocus();
                }
                break;
            case R.id.bt_phoneloginactivity_login:
                if(judCord())
                    SMSSDK.submitVerificationCode("86",username,phonecode);
                flag=false;
                break;
            default:
                break;
        }
    }

    /**
     * @breif 完成电话号码空值、位数不对、号码不对、无账号、正确五种情况的判断
     * @return 电话号码是否正确
     */
    private boolean judPhone()
    {
        String name = etUsername.getText().toString().trim();
        ArrayList<UserBean> datas = mDBOpenHelper.getAllData();
        boolean match = false;
        for(int i=0;i<datas.size();i++) {
            UserBean userBean = datas.get(i);
            if (name.equals(userBean.getName())){
                match = true;
                break;
            }else{
                match = false;
                }
        }
        if(TextUtils.isEmpty(etUsername.getText().toString().trim()))
        {
            Toast.makeText(this,"请输入您的电话号码",Toast.LENGTH_LONG).show();
            etUsername.requestFocus();
            return false;
        }
        else if(etUsername.getText().toString().trim().length()!=11)
        {
            Toast.makeText(this,"您的电话号码位数不正确",Toast.LENGTH_LONG).show();
            etUsername.requestFocus();
            return false;
        }
        else if(!match)
        {
            Toast.makeText(this, "没有该账号，请重新输入",
                    Toast.LENGTH_LONG).show();
            etUsername.requestFocus();
            return false;
        }
        else
        {
            username=etUsername.getText().toString().trim();
            String num="[1][358]\\d{9}";
            if(username.matches(num))
                return true;
            else
            {
                Toast.makeText(this,"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    /**
     * @breif 完成验证码空值、位数不对和格式正确三种情况的判断
     * @return 验证码格式是否正确
     */
    private boolean judCord()
    {
        judPhone();
        if(TextUtils.isEmpty(etPhoneCode.getText().toString().trim()))
        {
            Toast.makeText(this,"请输入您的验证码",Toast.LENGTH_LONG).show();
            etPhoneCode.requestFocus();
            return false;
        }
        else if(etPhoneCode.getText().toString().trim().length()!=4)
        {
            Toast.makeText(this,"您的验证码位数不正确",Toast.LENGTH_LONG).show();
            etPhoneCode.requestFocus();

            return false;
        }
        else
        {
            phonecode=etPhoneCode.getText().toString().trim();
            return true;
        }

    }
}
