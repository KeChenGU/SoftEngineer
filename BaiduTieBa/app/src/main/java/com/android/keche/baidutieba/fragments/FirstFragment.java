package com.android.keche.baidutieba.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.keche.baidutieba.MainActivity;
import com.android.keche.baidutieba.MyCommentActivity;
import com.android.keche.baidutieba.R;
import com.android.keche.baidutieba.beans.FirstPageBean;
import com.android.keche.baidutieba.serials.FirstPageDistribution;
import com.android.keche.baidutieba.utils.HttpUtil;
import com.android.keche.baidutieba.utils.ImageDealUtil;
import com.android.keche.baidutieba.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mob.tools.utils.LocationHelper;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author 谷恪忱
 * @brief 首页展示发帖列表碎片
 */
public class FirstFragment extends Fragment implements View.OnClickListener{

    private int num = 0;

    private LinearLayout showListLayout;

    private SwipeRefreshLayout refreshLayout;

    private static final String gainSheetURL = MainActivity.DEFAULT_SERVER_URL + "GainRandSheetServlet";

//    private static final View.OnClickListener likeListener = new View.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            HttpUtil.getByHttpURL(MainActivity.DEFAULT_SERVER_URL, new HttpUtil.HttpCallBackListener() {
//                @Override
//                public void onFinish(String response) {
//
//                }
//
//                @Override
//                public void onError(Exception e) {
//
//                }
//            });
//        }
//    };

    private final SwipeRefreshLayout.OnRefreshListener
            autoGainListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
//            HttpUtil.getByOkHttp(MainActivity.DEFAULT_SERVER_URL + "GainRandSheetServlet", new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    Log.e("gainSheetError", e.getMessage());
//                    e.printStackTrace();
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//
//                }
//            });
            refreshListItemByNet();
        }
    };
    //private LinearLayout.LayoutParams defaultItemParams;

    // private List<LinearLayout> distributionList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_first, container, false);
        initUIs(view, inflater);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        //Toast.makeText(this.getContext(), "hsdifjsifjsklfjs!!!!!!", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this.getContext(), getParentFragment().getActivity() + "", Toast.LENGTH_SHORT).show();
        //Log.d("Activity:", getParentFragment().getActivity() + "");
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            Toast.makeText(this.getContext(), "Heloooooooooooooooooo!!!!!", Toast.LENGTH_SHORT).show();
//        }
        //FirstPageDistribution distribution = getMainDistribution();//((MainActivity)getParentFragment().getActivity()).getDistribution();
        //distributionList = getDistributionList(); //((MainActivity)getParentFragment().getActivity()).getDistributionList() ;
        List<FirstPageDistribution> distributionList = getDistributionList();
        if (distributionList != null) {
            for (FirstPageDistribution distribution : distributionList) {
                addListItemSelf(distribution);
            }
            //Toast.makeText(this.getContext(), showListLayout + "!!!!!", Toast.LENGTH_SHORT).show();
            //setMainDistributionList(distributionList);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            default:
                break;
        }
    }


    //    public FirstPageDistribution getMainDistribution() {
//        return ((MainActivity)getParentFragment().getActivity()).getDistribution();
//    }

//    public void setMainDistributionList(List<FirstPageDistribution> distributionList) {
//        ((MainActivity)getParentFragment().getActivity()).setDistributionList(distributionList);
//    }

    /**
     * @brief 获取从主活动传递来的帖子数据列表
     * @return 与主活动相同的列表
     */
    public List<FirstPageDistribution> getDistributionList() {
        return ((MainActivity)getParentFragment().getActivity()).getDistributionList();
    }

    /**
     * @brief 往首页贴展示内容添加贴子
     * @param distribution
     */
    private void addListItemSelf(final FirstPageDistribution distribution) {
        LinearLayout layoutView = (LinearLayout) getLayoutInflater().inflate(
                R.layout.fragment_child_first_list_item, null);
        ImageView userIcon = (ImageView) layoutView.findViewById(R.id.f_c_first_list_item_user_icon);
        userIcon.setImageBitmap(ImageDealUtil.convertStringToIcon(distribution.getUserIconURL()));
        TextView userName = (TextView)layoutView.findViewById(R.id.f_c_first_list_item_user_name);
        userName.setText(distribution.getUserName());
        TextView dateText = (TextView) layoutView.findViewById(R.id.f_c_first_list_item_date);
        dateText.setText(distribution.getDate());
        TextView sourceBar = (TextView) layoutView.findViewById(R.id.f_c_first_list_item_source_bar);
        sourceBar.setText(distribution.getSource());
        TextView contentText = (TextView) layoutView.findViewById(R.id.f_c_first_list_item_content);
        contentText.setText(distribution.getContent());
        contentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyCommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Distribution", distribution);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        TextView shareNum = (TextView) layoutView.findViewById(R.id.f_c_first_list_item_share);
        shareNum.setText(String.valueOf(distribution.getShareNum()));
        TextView commentNum = (TextView) layoutView.findViewById(R.id.f_c_first_list_item_comment);
        commentNum.setText(String.valueOf(distribution.getCommentNum()));
        TextView likeNum = (TextView) layoutView.findViewById(R.id.like_number);
        likeNum.setText(String.valueOf(distribution.getLikeNum()));
        ImageView likeIcon = (ImageView) layoutView.findViewById(R.id.like_icon);
        //likeIcon.setOnClickListener();
        //likeNum.setOnClickListener(likeListener);
        LikeNumListener likeNumListener = new LikeNumListener(likeNum);
        likeNumListener.setLikeIcon(likeIcon);
        likeNum.setOnClickListener(likeNumListener);
        LinearLayout mediaLayout = (LinearLayout) layoutView.findViewById(R.id.f_c_first_list_item_media);
        for (String url: distribution.getImageURLs()) {
            ImageView picView = new ImageView(mediaLayout.getContext());
            picView.setLayoutParams(
                    new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
            );
            Glide.with(mediaLayout.getContext()).load(url).into(picView);
            //ImageView picView = imageDemo;
            mediaLayout.addView(picView);
        }
        //setMainDistributionList();
        //showListLayout.removeAllViews();
//        layoutView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MyCommentActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("Distribution", distribution);
//                startActivity(intent);
//            }
//        });

        showListLayout.addView(layoutView);
        Log.d("Times:", num++ + "");
    }

    /**
     * @brief 用户动态刷新帖子的方法
     */
    private void refreshListItemByNet() {
        HttpUtil.getByHttpURL(gainSheetURL, new HttpUtil.HttpCallBackListener() {
            @Override
            public void onFinish(final String response) {
                if (response != null && !response.equals("")) {
                    refreshDistributions(response);
                }
            }

            @Override
            public void onError(Exception e) {
                FragmentActivity fragmentActivity = FirstFragment.this.getParentFragment().getActivity();
                if (fragmentActivity == null) {
                    Log.e("error!", "重新刷新！");
                    return;
                }
                FirstFragment.this.getParentFragment().getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShort(FirstFragment.this.getContext(), "刷新失败！");
                    }
                });
                Log.e("refreshError", e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * @brief 初始化UI界面
     * @param view 主视图
     * @param inflater 视图展开器
     */
    private void initUIs(View view, LayoutInflater inflater) {
        showListLayout = (LinearLayout) view.findViewById(R.id.f_c_first_list);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.f_c_first_list_refresh);
        refreshLayout.setOnRefreshListener(autoGainListener);
        refreshListItemByNet();
        //LinearLayout distItem = (LinearLayout) inflater.inflate(R.layout.fragment_child_first_list_item, null);
        // defaultItemParams = (LinearLayout.LayoutParams) distItem.getLayoutParams();
        //showListLayout.addView(distItem);
        //distributionList.add(distItem);
//        List<FirstPageDistribution> distributionList = getDistributionList();
//        if (distributionList != null) {
//            for (FirstPageDistribution distribution : distributionList) {
//                addListItemSelf(distribution);
//            }
//            //Toast.makeText(this.getContext(), showListLayout + "!!!!!", Toast.LENGTH_SHORT).show();
//            setMainDistributionList(distributionList);
//        }

    }

    /**
     * @brief 刷新显示帖子数据
     * @note 必须在非主线程使用
     */
    private void refreshDistributions(final String response) {
        try {
            Thread.sleep(1600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("refreshJson", response);
        FirstFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showListLayout.removeAllViews();
                List<FirstPageBean> pageBeans = new Gson().fromJson(response,
                        new TypeToken<List<FirstPageBean>>(){}.getType());
                for (final FirstPageBean pageBean : pageBeans) {
                    LinearLayout layoutView = (LinearLayout) getLayoutInflater().inflate(
                            R.layout.fragment_child_first_list_item, null);
                    ImageView userIcon = (ImageView) layoutView.findViewById(R.id.f_c_first_list_item_user_icon);
                    //userIcon.setImageBitmap();
                    Glide.with(showListLayout.getContext())
                            .load(MainActivity.DEFAULT_SERVER_URL + pageBean.getUserIconURL()).into(userIcon);
                    TextView userName = (TextView)layoutView.findViewById(R.id.f_c_first_list_item_user_name);
                    userName.setText(pageBean.getUserName());
                    TextView dateText = (TextView) layoutView.findViewById(R.id.f_c_first_list_item_date);
                    dateText.setText(pageBean.getDate());
                    TextView sourceBar = (TextView) layoutView.findViewById(R.id.f_c_first_list_item_source_bar);
                    sourceBar.setText(pageBean.getSource());
                    TextView contentText = (TextView) layoutView.findViewById(R.id.f_c_first_list_item_content);
                    contentText.setText(pageBean.getContent());
                    contentText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), MyCommentActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("Distribution",
                                    MainActivity.changeBeanToSerial(pageBean));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    TextView shareNum = (TextView) layoutView.findViewById(R.id.f_c_first_list_item_share);
                    shareNum.setText(String.valueOf(pageBean.getShareNum()));
                    TextView commentNum = (TextView) layoutView.findViewById(R.id.f_c_first_list_item_comment);
                    commentNum.setText(String.valueOf(pageBean.getCommentNum()));
                    TextView likeNum = (TextView) layoutView.findViewById(R.id.like_number);
                    likeNum.setText(String.valueOf(pageBean.getLikeNum()));
                    //likeNum.setOnClickListener(likeListener);
                    ImageView likeIcon = (ImageView) layoutView.findViewById(R.id.like_icon);
                    LikeNumListener likeNumListener = new LikeNumListener(likeNum);
                    likeNumListener.setLikeIcon(likeIcon);
                    likeNum.setOnClickListener(likeNumListener);
                    LinearLayout mediaLayout = (LinearLayout) layoutView.findViewById(R.id.f_c_first_list_item_media);
                    for (String url: pageBean.getImageURLs()) {
                        ImageView picView = new ImageView(mediaLayout.getContext());
                        picView.setLayoutParams(
                                new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
                        );
                        url = MainActivity.DEFAULT_SERVER_URL + url;
                        Log.d("loadURL", url);
                        Glide.with(showListLayout.getContext()).load(url).into(picView);
                        Log.d("loadURL", url);
                        //ImageView picView = imageDemo;
                        mediaLayout.addView(picView);
                    }
                    //setMainDistributionList();
                    //showListLayout.removeAllViews();
//                    layoutView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
////                            Intent intent = new Intent(getActivity(), MyCommentActivity.class);
////                            Bundle bundle = new Bundle();
////                            bundle.putSerializable("Distribution_Net", (Serializable) pageBean);
////                            startActivity(intent);
//                        }
//                    });
                    showListLayout.addView(layoutView);
                    Log.e("Times:", num++ + "");
                }
                refreshLayout.setRefreshing(false);
            }
        });

    }


    private class LikeNumListener implements View.OnClickListener {

        private boolean isAdded = false;

        private int num = 0;

        private TextView likeNum;

        private ImageView likeIcon;

        LikeNumListener(TextView likeNum) {
            this.likeNum = likeNum;
            this.likeIcon = new ImageView(FirstFragment.this.getContext());
        }

        @Override
        public void onClick(View v) {
            try {
                num = Integer.parseInt(likeNum.getText().toString());
                num = isAdded ? num - 1 : num + 1;
                likeNum.setText(String.valueOf(num));
                if (isAdded) {
                    isAdded = false;
                    likeIcon.setImageResource(R.drawable.icon_comment_like);
                    //likeIcon.setImageResource(R.drawable.like);
                } else {
                    isAdded = true;
                    likeIcon.setImageResource(R.drawable.like);
                    //likeIcon.setImageResource(R.drawable.icon_comment_like);
                }
            } catch (Exception e) {
                Log.e("addLikeNumError", e.getMessage());
                e.printStackTrace();
                likeNum.setText(0);
                num = 0;
            }
        }

        public void setLikeIcon(ImageView likeIcon) {
            this.likeIcon = likeIcon;
        }

    }


}
