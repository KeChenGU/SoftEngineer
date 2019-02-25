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
import com.android.keche.baidutieba.beans.MyFocuses;
import com.android.keche.baidutieba.beans.RecentBar;
import com.android.keche.baidutieba.beans.RecordsDao;
import com.android.keche.baidutieba.helpers.SearchRecordsAdapter;
import com.android.keche.baidutieba.utils.ImageUtil;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
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
public class MyBarActivity extends Activity implements View.OnClickListener{
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

    private Bitmap temp;
    private ImageView Image1;
    private ImageView Image2;
    private ImageView Image3;
    private ImageView Image4;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;

    /**
     *
     * @param savedInstanceState 是保存Activity的状态的
     *@brief 消息响应函数，表示一个窗口正在生成
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bar);
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

//                    Image1.setBackground(ImageUtil.bitmap2Drawable(temp));
//                    tv1.setText("清华大学");

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
        Image1=(ImageView)findViewById(R.id.qingHua);
        tv2=(TextView)findViewById(R.id.tv2);
        Image2=(ImageView)findViewById(R.id.beiDa);
        tv3=(TextView)findViewById(R.id.tv3);
        Image3=(ImageView)findViewById(R.id.zafu);
        tv4=(TextView)findViewById(R.id.tv4);
        Image4=(ImageView)findViewById(R.id.zjyc);
        sendRequestWithOkHttp();
        //sendRequestWithOkHttp2();
        Log.d("test", "123456");
        Glide.with(this).load("http://192.168.43.201/AndroidBaiduBar/upload/crop_photo.jpg").into(Image1);
        Log.d("test1", "123456789");

    }

    /***
     * @brief 初始化我关注的吧界面按钮
     */
    private void initView2()
    {

        LinearLayout parentLL = (LinearLayout) contentView;  // contentView的布局

        int size =testName.size();
        Log.e("test:","size="+size);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); // 每行的水平LinearLayout
        layoutParams.setMargins(10, 3, 10, 3);


        ArrayList<Button> childBtns = new ArrayList<Button>();
        int totoalBtns = 0;
        for(int i = 0; i < size; i++){
            String item = testName.get(i);
           // String item = testName2[i];
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int length= item.length();
//            if(length < 4){  // 根据字数来判断按钮的空间长度, 少于4个当一个按钮
//                itemParams.weight = 1;
//                totoalBtns++;
//            }else if(length < 8){ // <8个两个按钮空间
//                itemParams.weight = 2;
//                totoalBtns+=2;
//            }else{
//                itemParams.weight = 3;
//                totoalBtns+=3;
//            }
            itemParams.weight=3;
            totoalBtns+=3;
            itemParams.width = 0;
            itemParams.setMargins(5, 5, 5, 5);
            Button childBtn = (Button) LayoutInflater.from(this).inflate(R.layout.button, null);
            childBtn.setText(item);
            Log.e("test:",item);
            //childBtn.setOnClickListener(btnOnClick);
            childBtn.setTag(item);
            childBtn.setLayoutParams(itemParams);
            childBtns.add(childBtn);
            childBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyBarActivity.this, MyCommentActivity.class);
                    startActivity(intent);
                }
            });

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
        clearAllRecordsTv.setOnClickListener(this);
        searchContentEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER){
                    if(searchContentEt.getText().toString().length()>0){
                        String record =searchContentEt.getText().toString();
                        //将搜索记录保存至数据库中
                        recordsDao.addRecords(record);
                        Toast.makeText(MyBarActivity.this, "成功添加该搜索记录", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MyBarActivity.this,"搜索内容不能为空",Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
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
                Toast.makeText(MyBarActivity.this,searchRecordsList.get(position)+"",Toast.LENGTH_SHORT).show();
                searchContentEt.setSelection(searchContentEt.length());
            }
        });
    }
    private void sendRequestWithOkHttp(){
       // initView2();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            //指定访问的服务器地址是电脑本机
                            .url(MainActivity.DEFAULT_SERVER_URL + "get_focus_data_test.jsp") //"http://10.0.2.2/get_MyFocusdata.json"
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
        //List<MyFocuses> myFocusesList =gson.fromJson(jsonData, new TypeToken<List<MyFocuses>>(){}.getType());
        MyFocus myFocus = gson.fromJson(jsonData, MyFocus.class);
//        for(String myFocus:myFocusList.getMyFocuText())
//        {
//            Log.d("test:","MyFocusText is "+ myFocus);
//        }
//        Log.e("test2:","size:"+myFocusList.getMyFocuText().size());
//        for(int i=0;i<myFocusesList.size();i++)
//        {
//            testName.add(myFocusesList.get(i).getMyFocuText());
//        }
//        for(int i=0;i<myFocusesList.size();i++)
//        {
//            testName.add(myFocusesList.get(i).getMyFocuText());
//        }
        for (int i = 0; i < myFocus.getMyFocuText().size(); ++i) {
            testName.add(myFocus.getMyFocuText().get(i));
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
                            .url(MainActivity.DEFAULT_SERVER_URL + "get_data_test.jsp")  //"http://10.0.2.2/test.json"
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData =response.body().string();
                    parseJSONWithJSONObject2(responseData) ;
                    Message msg=new Message();
                    msg.what=2;
                    uiHandler.sendMessage(msg);
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void parseJSONWithJSONObject2(String jsonData) {
        //GSON封装解析
        Gson gson = new Gson();
        Log.e("Test::", jsonData);
        List<RecentBar> recentBarList = gson.fromJson(jsonData, new TypeToken<List<RecentBar>>() {
        }.getType());
        // for(RecentBar recentBar:recentBarList) {
//           Log.d("test2:", "barIconString is " + recentBar.getBarIconString());
//           Log.d("test2:", "barName is " + recentBar.getBarName());
         //   temp = ImageUtil.convertStringToIcon(recentBarList.get(0).getBarIconString());
           // ImageView testImage = findViewById(R.id.test_image);
            String imageString = ImageUtil.convertIconToString(ImageUtil.drawable2Bitmap(getDrawable(R.drawable.qinghua)));
            temp = ImageUtil.convertStringToIcon(imageString);
            Image1.setBackground(ImageUtil.bitmap2Drawable(temp));
            tv1.setText("清华大学");
            imageString = ImageUtil.convertIconToString(ImageUtil.drawable2Bitmap(getDrawable(R.drawable.beijing)));
            temp = ImageUtil.convertStringToIcon(imageString);
            Image2.setBackground(ImageUtil.bitmap2Drawable(temp));
            tv2.setText("北京大学");
            imageString = ImageUtil.convertIconToString(ImageUtil.drawable2Bitmap(getDrawable(R.drawable.zafu)));
            temp = ImageUtil.convertStringToIcon(imageString);
            Image3.setBackground(ImageUtil.bitmap2Drawable(temp));
            tv3.setText("浙江农林大学");
            imageString = ImageUtil.convertIconToString(ImageUtil.drawable2Bitmap(getDrawable(R.drawable.zjyc)));
            temp = ImageUtil.convertStringToIcon(imageString);
            Image4.setBackground(ImageUtil.bitmap2Drawable(temp));
            tv4.setText("浙江农林大学暨阳学院");
    //}

    }
}


