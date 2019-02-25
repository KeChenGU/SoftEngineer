package com.android.keche.baidutieba;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.keche.baidutieba.helpers.DBOpenHelper;
import com.android.keche.baidutieba.utils.Code;
import com.android.keche.baidutieba.utils.HttpUtil;
import com.android.keche.baidutieba.utils.ImageDealUtil;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author 黄兆翔
 * @breif 实现账号注册
 */
public class RegisterActivity extends AppCompatActivity {

    private String realCode;                            /**< 存储验证码 **/
    private DBOpenHelper mDBOpenHelper;                 /**< 工具类 **/
    private ImageView ibBackLogin;                      /**< 返回登录按钮 **/
    private ImageView mIvRegisteractivityShowcode;    /**< 显示验证码 **/
    private EditText mEtRegisteractivityUsername;     /**< 用户名输入框 **/
    private EditText mEtRegisteractivityPassword1;    /**< 密码输入框 **/
    private EditText mEtRegisteractivityPassword2;    /**< 确认密码输入框 **/
    private EditText mEtRegisteractivityTestcode;     /**< 验证码输入框 **/
    private Button btRegister;                          /**< 注册按钮 **/
    private String tempUser;
    private String tempPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //ButterKnife.bind(this);
        mDBOpenHelper = new DBOpenHelper(this);

        //定义控件
        ibBackLogin = (ImageView) findViewById(R.id.ib_registeractivity_backlogin);
        mIvRegisteractivityShowcode = (ImageView) findViewById(R.id.iv_registeractivity_showCode);
        mEtRegisteractivityUsername = (EditText) findViewById(R.id.et_registeractivity_username);
        mEtRegisteractivityPassword1 = (EditText) findViewById(R.id.et_registeractivity_password1);
        mEtRegisteractivityPassword2 = (EditText) findViewById(R.id.et_registeractivity_password2);
        mEtRegisteractivityTestcode = (EditText) findViewById(R.id.et_registeractivity_testCodes);
        btRegister = (Button) findViewById(R.id.bt_register);

        //设置密码隐藏
        TransformationMethod method =  PasswordTransformationMethod.getInstance();
        mEtRegisteractivityPassword1.setTransformationMethod(method);
        mEtRegisteractivityPassword2.setTransformationMethod(method);

        //将验证码用图片的形式显示出来
        mIvRegisteractivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
    }

    @OnClick({
            R.id.ib_registeractivity_backlogin,
            R.id.iv_registeractivity_showCode,
            R.id.bt_register
    })

    /**
     * @breif 实现跳转账号密码登录页面功能、注册功能和切换随机验证码功能，通过view.getId()响应不同功能
     * 随机验证码生成通过调用Code.java中的函数实现，realCode保存验证码用于检测验证是否正确
     * 获取输入栏的账号密码，确认密码一致并验证码正确后用add（）的方法写入数据库
     */
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_registeractivity_backlogin://返回登录页面
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                RegisterActivity.this.finish();
                break;
            case R.id.iv_registeractivity_showCode://改变随机验证码的生成
                mIvRegisteractivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                break;
            case R.id.bt_register: //注册按钮
                //获取用户输入的用户名、密码、验证码
                String username = mEtRegisteractivityUsername.getText().toString().trim();
                String password1 = mEtRegisteractivityPassword1.getText().toString().trim();
                String password2 = mEtRegisteractivityPassword2.getText().toString().trim();
                String testCode = mEtRegisteractivityTestcode.getText().toString().toLowerCase();
                //注册验证
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password1) && !TextUtils.isEmpty(testCode) && !TextUtils.isEmpty(password2) ) {
                    if (!password1.equals(password2)){
                        Toast.makeText(this,  "两次密码不一致，请修改", Toast.LENGTH_SHORT).show();
                    }else {
                        if (testCode.equals(realCode)) {
                            //将用户名和密码加入到数据库中
                            //mDBOpenHelper.add(username, password1);
                            tempUser = username;
                            tempPass = password1;
                            sendRegisterRequest(username, password1);
//                            Intent intent2 = new Intent(this, LoginActivity.class);
//                            startActivity(intent2);
//                            finish();
//                            Toast.makeText(this, "验证通过，注册成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "验证码错误，注册失败" , Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(this, "未完善信息，注册失败", Toast.LENGTH_SHORT).show();
                }
                mDBOpenHelper.getWritableDatabase();
                break;

        }
    }

    private void sendRegisterRequest(String userName, String password) {
        String iconString = ImageDealUtil.convertIconToString(
                ImageDealUtil.drawable2Bitmap(getResources().getDrawable(R.mipmap.touxiang1))
        );
        HttpUtil.postByOkHttp(MainActivity.DEFAULT_SERVER_URL + "insert_new_user_to_server.jsp",
                new String[]{"name", "password", "phone", "icon"},
                new String[]{userName, password, userName, ""},
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(RegisterActivity.this, "注册异常!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //Log.d("response:", response.body().string());
                        if (response.body().string().contains("该用户已被注册！")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "该账号已被注册！", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                        mDBOpenHelper.add(tempUser, tempPass);
                        Intent intent2 = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent2);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, "验证通过，注册成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                        finish();

                        //Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
