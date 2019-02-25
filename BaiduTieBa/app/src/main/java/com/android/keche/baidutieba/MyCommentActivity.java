package com.android.keche.baidutieba;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.keche.baidutieba.adapters.CommentExpandAdapter;
import com.android.keche.baidutieba.beans.CommentBean;
import com.android.keche.baidutieba.beans.CommentDetailBean;
import com.android.keche.baidutieba.beans.ReplyDetailBean;
import com.android.keche.baidutieba.serials.FirstPageDistribution;
import com.android.keche.baidutieba.utils.ToastUtils;
import com.android.keche.baidutieba.views.CommentExpandableListView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;


import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *主函数
 */
public class MyCommentActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MyCommentActivity";

    private Toolbar toolbar;

    private TextView bt_comment;

    private CommentExpandableListView expandableListView;

    private CommentExpandAdapter adapter;

    private CommentBean commentBean;

    private List<CommentDetailBean> commentsList;

    private BottomSheetDialog dialog;

    private ImageView toolbarImage;

    private TextView userName;

    private ImageView userIcon;

    private TextView content;

    private TextView time;

    //private ImageView userIconItem;

    private TextView storeNum;

    private TextView likeNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment);
        //initView();
        sendRequestWithOkHttp();
       // initView();


    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        expandableListView = (CommentExpandableListView) findViewById(R.id.detail_page_lv_comment);
        bt_comment = (TextView) findViewById(R.id.detail_page_do_comment);
        toolbarImage = (ImageView) findViewById(R.id.detail_page_image);
        userName = (TextView) findViewById(R.id.detail_page_userName);
        userIcon = (ImageView) findViewById(R.id.detail_page_userLogo);
        time = (TextView) findViewById(R.id.detail_page_time);
        content = (TextView) findViewById(R.id.detail_page_story);
        //storeNum = (TextView) findViewById(R.id.page_store_nums);
        //likeNum = (TextView) findViewById(R.id.page_like_nums);
        bt_comment.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            FirstPageDistribution distribution = (FirstPageDistribution) bundle.getSerializable("Distribution");
            if (distribution == null) {
                return;
            }
            userName.setText(distribution.getUserName());
            Glide.with(this)
                    .load(MainActivity.DEFAULT_SERVER_URL + distribution.getUserIconURL())
                    .into(userIcon);
            time.setText(distribution.getDate());
            content.setText(distribution.getContent());
            if (distribution.getImageURLs() != null && distribution.getImageURLs().size() >0) {
                Glide.with(this).load(MainActivity.DEFAULT_SERVER_URL + distribution.getImageURLs().get(0))
                        .into(toolbarImage);
            }
            //storeNum.setText(distribution.getShareNum());
            //likeNum.setText(distribution.getLikeNum());

        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("详情");
       commentsList = commentBean.getData().getList();
        initExpandableListView(commentsList);
    }

    /**
     * 初始化评论和回复列表
     */
    private void initExpandableListView(final List<CommentDetailBean> commentList){
        if (MainActivity.user == null) {
            ToastUtils.showShort(this, "要先登录才能查看哦");
            return;
        }
        expandableListView.setGroupIndicator(null);
        //默认展开所有回复
        adapter = new CommentExpandAdapter(this, commentList);
        expandableListView.setAdapter(adapter);
        for(int i = 0; i<commentList.size(); i++){
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
                Log.e(TAG, "onGroupClick: 当前的评论id>>>"+commentList.get(groupPosition).getId());
                showReplyDialog(groupPosition);
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(MyCommentActivity.this,"点击了回复",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //toast("展开第"+groupPosition+"个分组");
            }
        });

    }

    /**
     * func:生成测试数据
     * @return 评论数据
     */
//    private List<CommentDetailBean> generateTestData(){
//        Gson gson = new Gson();
//        commentBean = gson.fromJson(testJson, CommentBean.class);
//        List<CommentDetailBean> commentList = commentBean.getData().getList();
//        return commentList;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.detail_page_do_comment){

            showCommentDialog();
        }
    }

    /**
     * func:弹出评论框
     */
    private void showCommentDialog(){
        if (MainActivity.user == null) {
            ToastUtils.showShort(this, "要先登录才能发帖哦");
            return;
        }
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(commentContent)){

                    //commentOnWork(commentContent);
                    dialog.dismiss();
                    CommentDetailBean detailBean = new CommentDetailBean(MainActivity.user.getName(),
                            commentContent,"刚刚");
                    adapter.addTheCommentData(detailBean);
                    Toast.makeText(MyCommentActivity.this,"评论成功",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(MyCommentActivity.this,"评论内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    /**
     * func:弹出回复框
     */
    private void showReplyDialog(final int position){
        if (MainActivity.user == null) {
            ToastUtils.showShort(this, "要先登录才能回复哦");
            return;
        }
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复 " + commentsList.get(position).getNickName() + " 的评论:");
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(replyContent)){

                    dialog.dismiss();
                    ReplyDetailBean detailBean = new ReplyDetailBean(MainActivity.user.getName(),
                            replyContent);
                    adapter.addTheReplyData(detailBean, position);
                    expandableListView.expandGroup(position);
                    Toast.makeText(MyCommentActivity.this,"回复成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MyCommentActivity.this,"回复内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    /***
     * 从服务器中解析数据
     */
    private void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            //指定访问的服务器地址是电脑本机
                            .url(MainActivity.DEFAULT_SERVER_URL + "Comment.json")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData =response.body().string();
                    parseJSONWithJSONObject(responseData) ;
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }).start();

    }
    private void parseJSONWithJSONObject(String jsonData){
        //GSON封装解析
        Gson gson = new Gson();
        Log.d("Json:", jsonData);
        commentBean = gson.fromJson(jsonData, CommentBean.class);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initView();
            }
        });
    }



}
