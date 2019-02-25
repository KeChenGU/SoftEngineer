package com.android.keche.baidutieba;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.keche.baidutieba.beans.UserBean;
import com.android.keche.baidutieba.beans.UserExBean;
import com.android.keche.baidutieba.helpers.DBOpenHelper;
import com.android.keche.baidutieba.utils.HttpUtil;
import com.android.keche.baidutieba.utils.ImageDealUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author 黄兆翔
 * @breif 账号登录
 */
public class LoginActivity extends AppCompatActivity {

    private DBOpenHelper mDBOpenHelper;         /**< 工具类 **/
    private EditText etUsername;                /**< 用户名输入栏 **/
    private EditText etPassword;                /**< 密码输入栏 **/
    private Button btGoRegister;                /**< 跳转注册页面按钮 **/
    private Button btLogin;                     /**< 登录按钮 **/
    private Button btGoPhoneLogin;             /**< 跳转手机登录页面按钮 **/


    private String tempName;

    private String tempPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //ButterKnife.bind(this);
        mDBOpenHelper = new DBOpenHelper(this);

        //定义控件
        etPassword = (EditText)findViewById(R.id.et_password);
        etUsername = (EditText) findViewById(R.id.et_username);
        btGoRegister = (Button) findViewById(R.id.bt_goregister);
        btLogin = (Button) findViewById(R.id.bt_loginactivity_login);
        btGoPhoneLogin = (Button) findViewById(R.id.bt_loginactivity_gophonelogin);

        //设置密码隐藏
        TransformationMethod method =  PasswordTransformationMethod.getInstance();
        etPassword.setTransformationMethod(method);
    }

    @OnClick({
            R.id.bt_goregister,
            R.id.bt_loginactivity_login,
            R.id.bt_loginactivity_gophonelogin
    })

    /**
     * @breif 实现跳转注册、电话登录页面功能和登录功能，通过view.getId()响应不同功能
     * 登录功能的实现：获取用户名密码，查找数据库中对应的数据，对应即跳转到'我的'页面
     */
    public void onClick(View view) {
        switch (view.getId()) {
            //case R.id.iv_loginactivity_back:
            //TODO 返回箭头功能
            //    finish();//销毁此Activity
            //    break;
            case R.id.bt_goregister:
                //TODO 注册界面跳转
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            //case R.id.tv_loginactivity_forget:   //忘记密码
            //TODO 忘记密码操作界面跳转
            //    startActivity(new Intent(this, FindPasswordActivity.class));
            //    break;
            case R.id.bt_loginactivity_gophonelogin:    //短信验证码登录
             //TODO 短信验证码登录界面跳转
                startActivity(new Intent(this, PhoneLoginActivity.class));
                finish();
                break;
            case R.id.bt_loginactivity_login:
                String name = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                    ArrayList<UserBean> data = mDBOpenHelper.getAllData();
                    boolean match = false;
                    for(int i=0;i<data.size();i++) {
                        UserBean userBean = data.get(i);
                        if (name.equals(userBean.getName()) && password.equals(userBean.getPassword())){
                            match = true;
                            break;
                        }else{
                            match = false;
                        }
                    }
                    if (match) {
//                        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(this, MyActivity.class);
//
//                        startActivity(intent);
                        tempName = name;
                        tempPass = password;
                        sendLoginRequest(name, password);
                        finish();//销毁此Activity
                    }else {
                        Toast.makeText(this, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请输入你的用户名或密码", Toast.LENGTH_SHORT).show();
                }
                break;
            //case R.id.tv_loginactivity_else:
            //TODO 第三方登录，时间有限，未实现
            //    break;
        }
    }

    private void sendLoginRequest(String userName, String password) {
        HttpUtil.postByOkHttp(MainActivity.DEFAULT_SERVER_URL + "login_check.jsp",
                new String[]{"name", "password"},
                new String[]{userName, password},
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "登录异常！请检查网络！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String reps = response.body().string();
                        if (reps.contains("用户不存在！")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "请先注册！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Log.d("json", reps);
                            MainActivity.userEx = new Gson().fromJson(reps, UserExBean.class);
                            Log.d("JsonParse:", MainActivity.userEx + "");
                            if (MainActivity.userEx.getIconUrl() == null
                                    ||MainActivity.userEx.getIconUrl().equals("")) {
//                                    MainActivity.userEx.setIconUrl(
//                                            ImageDealUtil.convertIconToString(
//                                                    ImageDealUtil.drawable2Bitmap(
//                                                            getResources().getDrawable(R.drawable.my_select)
//                                                    )
//                                            )
//                                    );
                                MainActivity.userEx.setIconUrl("userIcon/face_happy.png");
                            }
                            if (MainActivity.user == null) {
                                MainActivity.user = new UserBean(tempName, tempPass);
                                MainActivity.user.setId(MainActivity.userEx.getId());
                                //MainActivity.userEx = new UserExBean(tempName, tempPass);

                            } else {
                                MainActivity.user.setId(MainActivity.userEx.getId());
                                MainActivity.user.setName(MainActivity.userEx.getName());
                                MainActivity.user.setPassword(MainActivity.userEx.getPassword());
                            }
                        }
                    }
                });
    }
}
