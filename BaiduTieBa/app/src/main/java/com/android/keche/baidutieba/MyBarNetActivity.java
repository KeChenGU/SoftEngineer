package com.android.keche.baidutieba;

import android.annotation.TargetApi;
import android.app.Activity;
import okhttp3.Request;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.keche.baidutieba.beans.MyFocus;
import com.android.keche.baidutieba.beans.RecentBar;
import com.android.keche.baidutieba.beans.RecordsDao;
import com.android.keche.baidutieba.helpers.SearchRecordsAdapter;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import okhttp3.OkHttpClient;
import okhttp3.Response;



/**
 * @brief 主函数
 * @note
 * 继承Activity,实现接口OnClickListener
 * 函数如下
 * onCreate()
 * initView()
 * initView2()
 * initData()
 * bindAdapter()
 * initListener()
 */
public class MyBarNetActivity extends Activity implements View.OnClickListener{
    private EditText searchContentEt;/**<搜索内容编辑文本*/
    private SearchRecordsAdapter recordsAdapter;/**<搜索记录适配器*/
    private View recordsHistoryView;/**<历史记录视图*/
    private ListView recordsListLv;/**<记录列表*/
    private TextView clearAllRecordsTv;/**<清除所有历史记录*/
    private LinearLayout searchRecordsLl;/**<搜索记录列表布局*/
    private List<String> searchRecordsList;/**<搜索记录列表*/
    private List<String> tempList;/**<临时列表*/
    private RecordsDao recordsDao;/**<数据库*/
    private TextView tv_history;/**<搜索历史文本框*/
    private View.OnClickListener btnOnClick; /**<Button视图*/
   private  Vector<String> testName=new Vector<String>();
    private LinearLayout contentView; /**<我关注的吧的视图*/
    private List<TextView> textViews = new ArrayList<>();
    private Map<TextView, ImageView> recentMap = new HashMap<>();
    //private ArrayMap<TextView, ImageView>
    private Bitmap temp;
    private ImageView Image1;
    private ImageView Image2;
    private ImageView Image3;
    private ImageView Image4;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    public static String str;
    public static String btnStr;

    /**
     *
     * @param savedInstanceState 是保存Activity的状态的
     *@brief 消息响应函数，表示一个窗口正在生成
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bar_net);
        initView();
        initData();
        bindAdapter();
        initListener();
    }

    /**
     * @brief 初始化视图
     * @note
     * addView 添加搜索view
     */
    private Handler uiHandler =new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    initView2();
                    break;
                case 2:
                    break;

            }
        }
    };
    private void initView() {
        initRecordsView();
        searchRecordsLl = (LinearLayout) findViewById(R.id.search_content_show_ll);
        searchContentEt = (EditText) findViewById(R.id.input_search_content_et);
        tv_history = (TextView) findViewById(R.id.tv_history);
        searchRecordsLl.addView(recordsHistoryView);
        contentView = (LinearLayout) findViewById(R.id.contentView);
        tv1=(TextView)findViewById(R.id.tv1);
        Image1=(ImageView)findViewById(R.id.Image1) ;
        textViews.add(tv1);
        recentMap.put(tv1, Image1);
        tv2=(TextView)findViewById(R.id.tv2);
        Image2=(ImageView)findViewById(R.id.Image2) ;
        textViews.add(tv2);
        recentMap.put(tv2, Image2);
        tv3=(TextView)findViewById(R.id.tv3);
        Image3=(ImageView)findViewById(R.id.Image3) ;
        textViews.add(tv3);
        recentMap.put(tv3, Image3);
        tv4=(TextView)findViewById(R.id.tv4);
        Image4=(ImageView)findViewById(R.id.Image4) ;
        textViews.add(tv4);
        recentMap.put(tv4, Image4);


        sendRequestWithOkHttp();
        sendRequestWithOkHttp2();

//        Glide.with(this).load("http://192.168.43.201/AndroidBaiduBar/upload/crop_photo.jpg").into(Image1);
//        Glide.with(this).load("http://192.168.43.201/AndroidBaiduBar/upload/crop_photo.jpg").into(Image2);
//        Glide.with(this).load("http://192.168.43.201/AndroidBaiduBar/upload/crop_photo.jpg").into(Image3);
//        Glide.with(this).load("http://192.168.43.201/AndroidBaiduBar/upload/crop_photo.jpg").into(Image4);
    }

    /***
     * @brief初始化我关注的吧界面按钮
     */
    private void initView2()
    {

        LinearLayout parentLL = (LinearLayout) contentView;  // contentView的布局

        int size =testName.size();
       // Log.e("test:","size="+size);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); // 每行的水平LinearLayout
        layoutParams.setMargins(10, 3, 10, 3);


        ArrayList<Button> childBtns = new ArrayList<Button>();
        int totoalBtns = 0;
        for(int i = 0; i < size; i++){
            String item = testName.get(i);
           // String item = testName2[i];
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int length= item.length();

            itemParams.weight=3;
            totoalBtns+=3;
            itemParams.width = 0;
            itemParams.setMargins(5, 5, 5, 5);
            Button childBtn = (Button) LayoutInflater.from(this).inflate(R.layout.button, null);
            childBtn.setText(item);
            Log.e("test:",item);

            childBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b=(Button)v;
                    str = b.getText().toString();
                    Intent intent =new Intent(MyBarNetActivity.this,SearchPageActivity.class);
                    startActivity(intent);
                }
            });
            //childBtn.setOnClickListener(btnOnClick);
            childBtn.setTag(item);

            childBtn.setLayoutParams(itemParams);
            childBtns.add(childBtn);

            if(totoalBtns >= 5){
                LinearLayout  horizLL = new LinearLayout(this);
                horizLL.setOrientation(LinearLayout.HORIZONTAL);
                horizLL.setLayoutParams(layoutParams);

                for(Button addBtn:childBtns){
                    horizLL.addView(addBtn);
                }
                parentLL.addView(horizLL);
                childBtns.clear();
                totoalBtns = 0;
            }
        }
        //最后一行添加一下
        if(!childBtns.isEmpty()){
            LinearLayout horizLL = new LinearLayout(this);
            horizLL.setOrientation(LinearLayout.HORIZONTAL);
            horizLL.setLayoutParams(layoutParams);

            for(Button addBtn:childBtns){
                horizLL.addView(addBtn);
            }
            parentLL.addView(horizLL);
            childBtns.clear();
            totoalBtns = 0;
        }
    }

    /**
     * @brief  初始化搜索历史记录View
     */
      private void initRecordsView()
      {
          recordsHistoryView = LayoutInflater.from(this).inflate(R.layout.search, null);
          //显示历史记录lv
            recordsListLv = (ListView) recordsHistoryView.findViewById(R.id.search_records_lv);
            //清除搜索历史记录
            clearAllRecordsTv = (TextView) recordsHistoryView.findViewById(R.id.clear_all_records_tv);



      }

    /**
     * @brief 初始化数据
     * @note
     * checkRecordsSize()
     * 第一次进入判断数据库中是否有历史记录，没有则不显示
     */
    private void initData()
    {
        recordsDao = new RecordsDao(this);
        searchRecordsList = new ArrayList<>();
        tempList = new ArrayList<>();
        tempList.addAll(recordsDao.getRecordsList());
        reversedList();
        checkRecordsSize();
    }

    /**
     * 捆绑适配器
     */
    private void bindAdapter()
    {
        recordsAdapter = new SearchRecordsAdapter(this, searchRecordsList);
        recordsListLv.setAdapter(recordsAdapter);
    }

    /**
     * @brief 当没有匹配的搜索数据的时候不显示历史记录栏
     * 如果有匹配数据的时候显示历史记录栏
     */
    private void checkRecordsSize()
    {
        if(searchRecordsList.size() == 0)
        {
            searchRecordsLl.setVisibility(View.GONE);
        }
        else {
                searchRecordsLl.setVisibility(View.VISIBLE);
            }
    }
    /**
     * @brief 颠倒list顺序，用户输入的信息会从上依次往下显示
     */
     private void reversedList()
     {
         searchRecordsList.clear();
         for(int i = tempList.size() - 1 ; i >= 0 ; i --)
         {
             searchRecordsList.add(tempList.get(i));
         }
     }

    /**
     *
     * @param v 传递的视图控件
     * @brief
     * 点击清除历史控件，清空所有历史数据
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
                   case R.id.clear_all_records_tv:
                       tempList.clear();
                       reversedList();
                       recordsDao.deleteAllRecords();
                       recordsAdapter.notifyDataSetChanged();
                       searchRecordsLl.setVisibility(View.GONE);
                       searchContentEt.setHint("请输入你要搜索的内容");
                       break;
                    case R.id.Image1:
                        str=tv1.getText().toString();
                        Intent intent =new Intent(MyBarNetActivity.this,SearchPageActivity.class);
                        startActivity(intent);
                         break;
                    case R.id.Image2:
                        str=tv2.getText().toString();
                        Intent intent2 =new Intent(MyBarNetActivity.this,SearchPageActivity.class);
                        startActivity(intent2);
                        break;
                case R.id.Image3:
                    str=tv3.getText().toString();
                    Intent intent3 =new Intent(MyBarNetActivity.this,SearchPageActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.Image4:
                    str=tv4.getText().toString();
                    Intent intent4 =new Intent(MyBarNetActivity.this,SearchPageActivity.class);
                    startActivity(intent4);
                    break;
        }


    }

    /**
     * @brief
     * clearAllRecordsTv.setOnClickListener
     * 清除历史按钮设置监听器
     * setOnEditorActionListener
     * 搜索内容编辑文本设置监听器
     * 编辑完之后点击软键盘上的回车键才会触发
     * 如果该搜索记录为空，屏幕显示吐丝"搜索内容不能为空"
     *  如果该搜索记录不为空，往数据库中添加该搜索记录，屏幕显示吐丝"成功添加该搜索记录"
     *  searchContentEt.addTextChangedListener
     *  根据输入的信息去模糊搜索
     *  setOnItemClickListener
     *  历史点击事件,将获取到的字符串传到搜索结果界面,点击后搜索对应条目内容
     */
    private void initListener()
    {
        Image1.setOnClickListener(this);
        Image2.setOnClickListener(this);
        Image3.setOnClickListener(this);
        Image4.setOnClickListener(this);

        clearAllRecordsTv.setOnClickListener(this);



        searchContentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().trim().length() == 0) {
                    tv_history.setText("搜索历史");
                } else {
                    tv_history.setText("搜索结果");
                }
                String tempName = searchContentEt.getText().toString();
                tempList.clear();
                tempList.addAll(recordsDao.querySimlarRecord(tempName));
                reversedList();
                checkRecordsSize();
                recordsAdapter.notifyDataSetChanged();
            }
        });
        recordsListLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    //         searchContentEt.setText(searchRecordsList.get(position));
                Toast.makeText(MyBarNetActivity.this,searchRecordsList.get(position)+"",Toast.LENGTH_SHORT).show();
                searchContentEt.setSelection(searchContentEt.length());
                str=searchRecordsList.get(position);
                Intent intent =new Intent(MyBarNetActivity.this,SearchPageActivity.class);
                startActivity(intent);
            }
        });
    }
    private void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            //指定访问的服务器地址是电脑本机
                            .url("http://192.168.43.201/AndroidBaiduBar/FocusBarServlet?user_id="
                                    + MainActivity.user.getId())
                            //.url("http://10.0.2.2/get_MyFocusdata.json")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData =response.body().string();
                    parseJSONWithJSONObject(responseData);
                    Message msg=new Message();
                    msg.what=1;
                    msg.obj=testName;
                    uiHandler.sendMessage(msg);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void parseJSONWithJSONObject(String jsonData){
        //GSON封装解析
        Gson gson =new Gson();

        Log.e("test4:","jsonData is : " +jsonData);
        MyFocus myFocusList =gson.fromJson(jsonData, MyFocus.class);

        for(int i=0;i<myFocusList.getMyFocuText().size();i++)
        {
            testName.add(myFocusList.getMyFocuText().get(i));
        }
    }
    private void sendRequestWithOkHttp2(){
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            //指定访问的服务器地址是电脑本机
                            .url("http://192.168.43.201/AndroidBaiduBar/RecentBarServlet?user_id="
                                    + MainActivity.user.getId())
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData =response.body().string();

                    parseJSONWithJSONObject2(responseData) ;
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void parseJSONWithJSONObject2(String jsonData) {
            Log.d("test:",jsonData);
                Gson gson = new Gson();
                final List<RecentBar> recentBarList = gson.fromJson(jsonData, new TypeToken<List<RecentBar>>() {
                }.getType());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                for (RecentBar bar : recentBarList) {
                    TextView textView = textViews.get(i++);
                    Glide.with(MyBarNetActivity.this)
                            .load(MainActivity.DEFAULT_SERVER_URL + bar.getIconUrl())
                            .error(R.drawable.beijing)
                            .into(recentMap.get(textView));
                    textView.setText(bar.getBarName());
                }

            }
        });



    }

}


