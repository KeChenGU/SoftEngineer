package com.android.keche.baidutieba;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.keche.baidutieba.beans.FirstPageBean;
import com.android.keche.baidutieba.beans.UserBean;
import com.android.keche.baidutieba.beans.UserExBean;
import com.android.keche.baidutieba.fragments.FirstBodyFragment;
import com.android.keche.baidutieba.fragments.MessageBodyFragment;
import com.android.keche.baidutieba.serials.FirstPageDistribution;
import com.android.keche.baidutieba.utils.HttpUtil;
import com.android.keche.baidutieba.utils.HttpUtils;
import com.android.keche.baidutieba.utils.ToastUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author 谷恪忱
 * @brief 主活动框架
 * 主界面碎片承载框架
 *
 * 重写方法：
 * onCreate() 方法 继承自AppCompatActivity
 * onClick() 方法 重写了View.OnClickListener接口
 * onSaveInstance() 和 onRestoreInstance()方法 用于
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView firstPage;        /**< 首页 **/

    private ImageView enterBar;         /**< 进吧 **/

    private ImageView distribute;       /**< 发帖 **/

    private ImageView messageNotify;    /**< 消息 **/

    private ImageView myDetails;        /**< 我的 **/

    //private FrameLayout frameLayout;    /**< 碎片框架 **/

    private FragmentManager fragmentManager;    /**< 碎片管理者 **/

    private FirstBodyFragment firstBodyFragment;        /**< 首页碎片类 **/

    private MessageBodyFragment messageBodyFragment;    /**< 消息碎片类 **/

    private BottomSheetDialog distributeSheet;  /**< 底部发帖对话框 **/

    private View distributeView;        /**< 发贴对话框主视图 **/

    //private MainHandler mainHandler = new MainHandler();    /**< 主活动处理器（处理多线程）**/

    private FirstPageDistribution distribution;

    //private LinearLayout showListLayout;

    private static List<FirstPageDistribution> distributionList = new ArrayList<>();    /**< 发帖列表 **/

    public static UserBean user;        /**< 用于存储用户登录信息的Session **/

    public static UserExBean userEx;    /**< 用户网络传输的加强UserBean **/

    public static final String DEFAULT_SERVER_URL = "http://192.168.43.201/AndroidBaiduBar/";

    private static final int DISTRIBUTE_TEXT_MODE = 0;

    private static final int DISTRIBUTE_PICTURE_MODE = 1;

    private static final int DISTRIBUTE_CAMERA_MODE = 2;

    private static final int DISTRIBUTE_ONLINE_MODE = 3;

    private static final int ENTER_BAR = 4;

    private static final int MY_DETAIL = 5;

    private static final int LOGIN = 6;

    /**
     * @brief 主活动构建函数 处理视图初始化逻辑
     * @param savedInstanceState 用于保存某些用户数据（用户活动间切换）
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUIs();
        //listenDistribution();
//        if (savedInstanceState != null) {
//            distributionList = (List<FirstPageDistribution>)
//                    savedInstanceState.getSerializable("DistributionList");
//            user = (UserBean) savedInstanceState.getSerializable("User");
//        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Toast.makeText(this, "你好！！！！！！！！", Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        if (intent != null && !DistributeActivity.DISTRIBUTED_FLAG) {
            distribution = (FirstPageDistribution) intent.getSerializableExtra("Distribution");
            if (distribution != null) {
                firstBodyFragment.setCurrentPage(FirstBodyFragment.PAGE_FIRST);
                if (distributionList.contains(distribution)) {
                    return;
                }
                distributionList.add(distribution);
                sendDistributionData(changeSerialToBean(distribution));
                //firstBodyFragment.addDistribution(distribution);
                Toast.makeText(this, "发帖成功！", Toast.LENGTH_SHORT).show();
                //this.finish();
            }
            //intent.putExtra("Distribution", (Serializable) null);
            DistributeActivity.DISTRIBUTED_FLAG = true;
        }
    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
////        if (savedInstanceState != null) {
////            distributionList = (List<FirstPageDistribution>)
////                    savedInstanceState.getSerializable("DistributionList");
////            user = (UserBean) savedInstanceState.getSerializable("User");
////        }
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putSerializable("DistributionList", (Serializable) distributionList);
//        outState.putSerializable("User", user);
//    }

    /**
     * @brief 设置底部导航栏的点击监听事件
     * 主要用于主活动中主框架的碎片切换
     * @param v 主活动的视图
     * @note 注意R中的drawable资源id取决于drawable文件下的图片名称
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first_page_pic:
                firstPage.setImageResource(R.drawable.first_select);
                enterBar.setImageResource(R.drawable.enter_uselect);
                messageNotify.setImageResource(R.drawable.message_uselect);
                myDetails.setImageResource(R.drawable.my_uselect);
                fragmentManager.beginTransaction().replace(R.id.content_layout, firstBodyFragment)
                        /*.addToBackStack(null)*/.commit();
                //fragmentManager.beginTransaction().addToBackStack(null);
                break;
            case R.id.enter_bar_pic:
                firstPage.setImageResource(R.drawable.first_uselect);
                enterBar.setImageResource(R.drawable.enter_select);
                messageNotify.setImageResource(R.drawable.message_uselect);
                myDetails.setImageResource(R.drawable.my_uselect);
                if (user == null) {
                    Toast.makeText(this, "请先登录！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent barIntent = new Intent(this, MyBarNetActivity.class);
                startActivityForResult(barIntent, ENTER_BAR);
                barIntent.removeExtra("Distribution");
                break;
            case R.id.distribute:
                if (user == null) {
                    Toast.makeText(this, "请先登录！", Toast.LENGTH_SHORT).show();
                    return;
                }
                showDistributeSheet();
                break;
            case R.id.message_notify_pic:
                firstPage.setImageResource(R.drawable.first_uselect);
                enterBar.setImageResource(R.drawable.enter_uselect);
                messageNotify.setImageResource(R.drawable.message_select);
                myDetails.setImageResource(R.drawable.my_uselect);
                if (user == null) {
                    Toast.makeText(this, "请先登录！", Toast.LENGTH_SHORT).show();
                    return;
                }
                fragmentManager.beginTransaction().replace(R.id.content_layout, messageBodyFragment)
                        /*.addToBackStack(null)*/.commit();//.commit();
                //fragmentManager.beginTransaction().addToBackStack(null).commit();
                break;
            case R.id.my_details_pic:
                firstPage.setImageResource(R.drawable.first_uselect);
                enterBar.setImageResource(R.drawable.enter_uselect);
                messageNotify.setImageResource(R.drawable.message_uselect);
                myDetails.setImageResource(R.drawable.my_select);
                if (user == null) {
                    Intent loginIntent = new Intent(this, LoginActivity.class);
                    startActivityForResult(loginIntent, LOGIN);
                    break;
                }
                Intent myIntent = new Intent(this, MyActivity.class);
                startActivityForResult(myIntent, MY_DETAIL);
                myIntent.removeExtra("Distribution");
                break;
            case R.id.bottom_sheet_text:
                if (distributeView == null) {
                    return;
                }
                //Toast.makeText(distributeView.getContext(), "你点击了文字！", Toast.LENGTH_SHORT).show();
                Intent distributeIntentTextMode = new Intent(this, DistributeActivity.class);
                startActivityForResult(distributeIntentTextMode, DISTRIBUTE_TEXT_MODE);
                distributeSheet.dismiss();
                break;
            case R.id.bottom_sheet_picture:
                if (distributeView == null) {
                    return;
                }
                //Toast.makeText(distributeView.getContext(), "你点击了拍摄！", Toast.LENGTH_SHORT).show();
                Intent distributeIntentImageMode = new Intent(this, DistributeActivity.class);
                distributeIntentImageMode.putExtra("camera", true);
                startActivityForResult(distributeIntentImageMode, DISTRIBUTE_CAMERA_MODE);
                distributeSheet.dismiss();
                break;
            case R.id.bottom_sheet_album:
                if (distributeView == null) {
                    return;
                }
                Toast.makeText(distributeView.getContext(), "你点击了相册！", Toast.LENGTH_SHORT).show();
                distributeSheet.dismiss();
                break;
            case R.id.bottom_sheet_online:
                if (distributeView == null) {
                    return;
                }
                Toast.makeText(distributeView.getContext(), "你点击了直播！", Toast.LENGTH_SHORT).show();
                distributeSheet.dismiss();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ENTER_BAR:
                break;
            case MY_DETAIL:
                break;
        }
    }

    /**
     * @brief 将可序列化对象转换为专门用于Gson解析的bean
     * @param distribution 可序列化对象
     * @return 一个可被解析为json的bean(FirstPageBean类型)
     */
    public static FirstPageBean changeSerialToBean(FirstPageDistribution distribution) {
        FirstPageBean pageBean = new FirstPageBean();
        pageBean.setTitle(distribution.getTitle());
        pageBean.setUserName(distribution.getUserName());
        pageBean.setUserIconURL(distribution.getUserIconURL());
        pageBean.setSource(distribution.getSource());
        pageBean.setDate(distribution.getDate());
        pageBean.setContent(distribution.getContent());
        pageBean.setShareNum(distribution.getShareNum());
        pageBean.setCommentNum(distribution.getCommentNum());
        pageBean.setLikeNum(distribution.getLikeNum());
        pageBean.setImageURLs(distribution.getImageURLs());
        return  pageBean;
    }

    /**
     * @brief 将bean转换为可序列化对象
     * @param pageBean 首页bean
     * @return 一个可序列化对象
     */
    public static FirstPageDistribution changeBeanToSerial(FirstPageBean pageBean) {
        FirstPageDistribution distribution = new FirstPageDistribution();
        distribution.setTitle(pageBean.getTitle());
        distribution.setUserName(pageBean.getUserName());
        distribution.setUserIconURL(pageBean.getUserIconURL());
        distribution.setSource(pageBean.getSource());
        distribution.setDate(pageBean.getDate());
        distribution.setContent(pageBean.getContent());
        distribution.setShareNum(pageBean.getShareNum());
        distribution.setCommentNum(pageBean.getCommentNum());
        distribution.setLikeNum(pageBean.getLikeNum());
        distribution.setImageURLs(pageBean.getImageURLs());
        return  distribution;
    }

    /**
     * @brief 用于传递帖子消息给内部碎片的方法
     * @param distribution 发布
     */
    public void setDistribution(FirstPageDistribution distribution) {
        this.distribution = distribution;
    }

    /**
     * @brief 获取单个添加到帖子信息
     * @return 一个可序列化的帖子信息对象
     */
    public FirstPageDistribution getDistribution() {
        return distribution;
    }

    /**
     * @brief 获取帖子信息列表
     * @return 一个包含帖子信息的列表
     */
    public List<FirstPageDistribution> getDistributionList() {
        return distributionList;
    }

    /**
     * @brief 设置在首页展示的帖子列表
     * @param distributionList 帖子列表
     */
    public void setDistributionList(List<FirstPageDistribution> distributionList) {
        this.distributionList = distributionList;
    }

    /**
     * @brief 初始化 主界面UI控件
     *
     */
    private void initUIs() {
        // getSupportActionBar().hide();
        firstPage = (ImageView) findViewById(R.id.first_page_pic);
        enterBar = (ImageView) findViewById(R.id.enter_bar_pic);
        distribute = (ImageView) findViewById(R.id.distribute);
        messageNotify = (ImageView) findViewById(R.id.message_notify_pic);
        myDetails = (ImageView) findViewById(R.id.my_details_pic);
        firstBodyFragment = new FirstBodyFragment();
        messageBodyFragment = new MessageBodyFragment();
        //frameLayout = (FrameLayout) findViewById(R.id.content_layout);
        firstPage.setOnClickListener(this);
        enterBar.setOnClickListener(this);
        distribute.setOnClickListener(this);
        messageNotify.setOnClickListener(this);
        myDetails.setOnClickListener(this);
        firstPage.setImageResource(R.drawable.first_select);
        enterBar.setImageResource(R.drawable.enter_uselect);
        messageNotify.setImageResource(R.drawable.message_uselect);
        myDetails.setImageResource(R.drawable.my_uselect);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_layout, firstBodyFragment)
                /*.addToBackStack(null)*/.commit();//.commit();
    }

    /**
     * @brief 显示底部发帖框
     */
    private void showDistributeSheet() {
        if (distributeSheet == null) {
            distributeSheet = new BottomSheetDialog(this);
            distributeView = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_sheet, null);
            distributeSheet.setContentView(distributeView);
            regDistributeSheetUIs(distributeView);
        }
        distributeSheet.show();
        // distributeSheet.dismiss();
    }

    /**
     * @brief 配置底部点击发帖框的UI控件
     * @param view 底部发帖框主视图
     */
    private void regDistributeSheetUIs(View view) {
        ImageView textIcon = (ImageView) view.findViewById(R.id.bottom_sheet_text);
        ImageView pictureIcon = (ImageView) view.findViewById(R.id.bottom_sheet_picture);
        ImageView albumIcon = (ImageView) view.findViewById(R.id.bottom_sheet_album);
        ImageView onlineIcon = (ImageView) view.findViewById(R.id.bottom_sheet_online);
        textIcon.setOnClickListener(this);
        pictureIcon.setOnClickListener(this);
        albumIcon.setOnClickListener(this);
        onlineIcon.setOnClickListener(this);
    }


    /**
     * @brief 将刚生成的帖子数据上传至服务器
     * 包括文字、图片内容
     */
    private void sendDistributionData(final FirstPageBean pageBean) {
    //        HttpUtil.postByOkHttp(MainActivity.DEFAULT_SERVER_URL + "",
//                new String[]{"user_name", "user_icon", "dist_date", "dist_title",
//                        "dist_content", "share_num", "comment_num", "like_num",
//                        "source", ""},
//                new String[]{},
//                new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//
//                    }
//                });
        if (pageBean == null) {
            return;
        }
        String sheetJson = new Gson().toJson(pageBean);
        HttpUtil.postByOkHttp(MainActivity.DEFAULT_SERVER_URL + "InsertSheetServlet",
                new String[]{"sheet_json"},
                new String[]{sheetJson},
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("postSheetJsonError", e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("postSuccess", response.body().string());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShort(MainActivity.this, "上传成功！");
                            }
                        });
                    }
                });
        if (pageBean.getImageURLs() == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("localPath", pageBean.getImageURLs().get(0));
                File image = new File(pageBean.getImageURLs().get(0));
                String result = HttpUtils.uploadFile(image, MainActivity.DEFAULT_SERVER_URL + "SheetImageServlet");
                Log.d("uploadResult", result);
            }
        }).start();
    }



//    private void listenDistribution() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    Intent intent = MainActivity.this.getIntent();
//                    if (intent != null) {
//                        distribution =
//                                (FirstPageDistribution) intent.getSerializableExtra("Distribution");
//                        if (distribution != null) {
//                            Message msg = new Message();
//                            msg.what = MainHandler.MSG_DISTRIBUTION;
//                            msg.obj = distribution;
//                            mainHandler.sendMessage(msg);
//                        }
//                    }
//                }
//            }
//        }).start();
//    }
//    private static class MainHandler extends Handler {
//
//        private static final int MSG_DISTRIBUTION = 0x00;
//
//        @Override
//        public void handleMessage(Message msg) {
//            //super.handleMessage(msg); // 空方法
//            switch (msg.what) {
//                case MSG_DISTRIBUTION:
//                    //Toast.makeText(, distribution.getContent(), Toast.LENGTH_SHORT).show();
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
}

//class DistributeSheet extends BottomSheetDialog {
//
//
//    public DistributeSheet(@NonNull Context context) {
//        super(context);
//    }
//
//    public DistributeSheet(@NonNull Context context, int theme) {
//        super(context, theme);
//    }
//
//    protected DistributeSheet(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
//        super(context, cancelable, cancelListener);
//    }
//}
