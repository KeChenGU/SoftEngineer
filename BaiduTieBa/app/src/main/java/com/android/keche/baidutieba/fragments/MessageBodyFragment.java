package com.android.keche.baidutieba.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.ArrayMap;
//import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.keche.baidutieba.R;
import com.android.keche.baidutieba.beans.ReceiveMessageBean;
import com.android.keche.baidutieba.beans.TalkMessageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谷恪忱
 * @brief 主界面消息栏界面碎片
 * 包含了主界面消息模块的所有处理事件和逻辑
 *
 */
public class MessageBodyFragment extends Fragment implements View.OnClickListener {

    private TextView message;       /**< 消息 **/

    private TextView messageLine;   /**< 底部隐藏线（消息） **/

    private TextView talk;          /**< 聊天 **/

    private TextView talkLine;      /**< 底部隐藏线（聊天） **/

    private TextView notify;        /**< 通知 **/

    private TextView notifyLine;    /**< 底部隐藏线（通知） **/

    private ViewPager tabViewPager; /**< 视图换页者，此处用于实现左右滑动切换 **/

    private PageTabAdapter tabAdapter;  /**< 视图换页者的适配器 **/




    private static final int PAGE_MESSAGE = 0;

    private static final int PAGE_TALK = 1;

    private static final int PAGE_NOTIFY = 2;


    /**
     * @brief 加载layout资源并加入活动容器，不与主界面关联
     * @param inflater 视图展开器 在本方法中不使用
     * @param container 视图容器
     * @param savedInstanceState 保存实例状态（数据）的对象
     * @return 返回View类型（视图）对象 由动态获取的视图展开器展开对应layout资源生成
     * @note 会产生InflateException
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_body, container, false);
        initUIs(view);
        return view;
    }

    /**
     * @brief 设置点击切换碎片的逻辑
     * @param v 消息主视图碎片
     */
    @Override
    public void onClick(View v) {
//        RelativeLayout.LayoutParams messageLineParam = (RelativeLayout.LayoutParams) messageLine.getLayoutParams();
//        RelativeLayout.LayoutParams talkLineParam = (RelativeLayout.LayoutParams) talkLine.getLayoutParams();
//        RelativeLayout.LayoutParams notifyLineParam = (RelativeLayout.LayoutParams) notifyLine.getLayoutParams();
        // String fragmentTag = "";
        switch (v.getId()) {
            case R.id.m_n_message:
            case R.id.m_n_message_bottom_line:
//                message.setTextColor(Color.BLACK);
//                //messageLine.setHeight(2);
//                messageLineParam.height = 2;
//                messageLine.setLayoutParams(messageLineParam);
//                messageLine.setBackgroundColor(Color.BLACK);
//                talk.setTextColor(Color.parseColor("#A8A8A8"));
//                talkLineParam.height = 0;
//                talkLine.setLayoutParams(talkLineParam);
//                talkLine.setBackgroundColor(Color.parseColor("#A8A8A8"));
//                notify.setTextColor(Color.parseColor("#A8A8A8"));
//                notifyLineParam.height = 0;
//                notifyLine.setLayoutParams(notifyLineParam);
//                notifyLine.setBackgroundColor(Color.parseColor("#A8A8A8"));
                setRelativeTextSelected(message, messageLine);
                setRelativeTextUnSelected(talk, talkLine);
                setRelativeTextUnSelected(notify, notifyLine);
                // fragmentTag = tabAdapter.messageFragment.getTag();
                tabViewPager.setCurrentItem(PAGE_MESSAGE, true);
                break;
            case R.id.m_n_talk:
            case R.id.m_n_talk_bottom_line:
//                message.setTextColor(Color.parseColor("#A8A8A8"));
//                messageLineParam.height = 0;
//                messageLine.setLayoutParams(messageLineParam);
//                messageLine.setTextColor(Color.parseColor("#A8A8A8"));
//                talk.setTextColor(Color.BLACK);
//                talkLineParam.height = 2;
//                talkLine.setLayoutParams(talkLineParam);
//                talkLine.setBackgroundColor(Color.BLACK);
//                notify.setTextColor(Color.parseColor("#A8A8A8"));
//                notifyLineParam.height = 0;
//                notifyLine.setLayoutParams(notifyLineParam);
//                notifyLine.setBackgroundColor(Color.parseColor("#A8A8A8"));
                setRelativeTextUnSelected(message, messageLine);
                setRelativeTextSelected(talk, talkLine);
                setRelativeTextUnSelected(notify, notifyLine);
                tabViewPager.setCurrentItem(PAGE_TALK, true);
                break;
            case R.id.m_n_notify:
            case R.id.m_n_notify_bottom_line:
//                message.setTextColor(Color.parseColor("#A8A8A8"));
//                messageLineParam.height = 0;
//                messageLine.setLayoutParams(messageLineParam);
//                messageLine.setTextColor(Color.parseColor("#A8A8A8"));
//                talk.setTextColor(Color.parseColor("#A8A8A8"));
//                talkLineParam.height = 0;
//                talkLine.setLayoutParams(talkLineParam);
//                talkLine.setBackgroundColor(Color.parseColor("#A8A8A8"));
//                notify.setTextColor(Color.BLACK);
//                notifyLineParam.height = 2;
//                notifyLine.setLayoutParams(notifyLineParam);
//                notifyLine.setBackgroundColor(Color.BLACK);
                setRelativeTextUnSelected(message, messageLine);
                setRelativeTextUnSelected(talk, talkLine);
                setRelativeTextSelected(notify, notifyLine);
                tabViewPager.setCurrentItem(PAGE_NOTIFY, true);
                break;
            default:
        }
    }


    /**
     * @brief 初始化各UI控件
     * @param view 消息主活动View
     */
    private void initUIs(View view) {
        message = (TextView) view.findViewById(R.id.m_n_message);
        messageLine = (TextView) view.findViewById(R.id.m_n_message_bottom_line);
        talk = (TextView) view.findViewById(R.id.m_n_talk);
        talkLine = (TextView) view.findViewById(R.id.m_n_talk_bottom_line);
        notify = (TextView) view.findViewById(R.id.m_n_notify);
        notifyLine = (TextView) view.findViewById(R.id.m_n_notify_bottom_line);
        tabViewPager = (ViewPager) view.findViewById(R.id.m_n_msg_child_view_pager);
        message.setOnClickListener(this);
        messageLine.setOnClickListener(this);
        talk.setOnClickListener(this);
        talkLine.setOnClickListener(this);
        notify.setOnClickListener(this);
        notifyLine.setOnClickListener(this);
        tabAdapter = new PageTabAdapter(getChildFragmentManager()); // !!!!!! 注意此处一定要设置孩子碎片的FragmentManager否则无法正常显示
        tabViewPager.setAdapter(tabAdapter);
        tabViewPager.setCurrentItem(PAGE_MESSAGE);
        tabViewPager.addOnPageChangeListener(new TabPageChangeListener());
        setRelativeTextSelected(message, messageLine);
    }

    /**
     * @brief 设置标题颜色为黑色，显示底部隐藏线
     * 用于简化处理OnClick()方法逻辑
     * @param textView 被选中的标题
     * @param textViewLine 标题对应底部隐藏线
     */
    private void setRelativeTextSelected(TextView textView, TextView textViewLine) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) textViewLine.getLayoutParams();
        layoutParams.height = 2;
        textView.setTextColor(Color.BLACK);
        //textViewLine.setBackgroundColor(Color.BLACK);
        textViewLine.setLayoutParams(layoutParams);
    }

    /**
     * @brief 设置标题颜色为原来默认颜色，隐藏底部隐藏线
     * 用于简化处理OnClick()方法逻辑
     * @param textView 未被选中的标题
     * @param textViewLine 标题对应底部隐藏线
     */
    private void setRelativeTextUnSelected(TextView textView, TextView textViewLine) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) textViewLine.getLayoutParams();
        layoutParams.height = 0;
        textView.setTextColor(Color.parseColor("#A8A8A8"));
        //textViewLine.setBackgroundColor(Color.parseColor("#A8A8A8"));
        textViewLine.setLayoutParams(layoutParams);
    }


    /**
     * @brief 自定义内部类， 用于处理左右横向滑动切换碎片的适配器
     * @note 必须要继承重写的父类方法
     *      @function 构造器
     *      @function getItem(int i) 获取第i个元素
     *      @function getCount() 获取总页数（即碎片数量）
     */
    private class PageTabAdapter extends FragmentPagerAdapter {

        private ArrayMap<String, Fragment> fragmentArrayMap;    /**< 数组映射，优化HashMap的内存存储效率 **/

        private MessageFragment messageFragment;    /**< 消息模块碎片 **/

        private TalkFragment talkFragment;          /**< 聊天模块碎片 **/

        private NotifyFragment notifyFragment;      /**< 通知模块碎片 **/

        private List<Fragment> fragmentList;

        PageTabAdapter(FragmentManager fm) {
            super(fm);
            fragmentArrayMap = new ArrayMap<>();
            fragmentList = new ArrayList<>();
            messageFragment = new MessageFragment();
            fragmentArrayMap.put(messageFragment.getTag(), messageFragment);
            fragmentList.add(messageFragment);
            talkFragment = new TalkFragment();
            fragmentArrayMap.put(talkFragment.getTag(), talkFragment);
            fragmentList.add(talkFragment);
            notifyFragment = new NotifyFragment();
            fragmentArrayMap.put(notifyFragment.getTag(), notifyFragment);
            fragmentList.add(notifyFragment);
        }

        @Override
        public Fragment getItem(int i) {
            //return fragmentArrayMap.valueAt(i);
            Fragment fragment = null;
            switch (i) {
                case PAGE_MESSAGE:
                    fragment = messageFragment;
                    break;
                case PAGE_TALK:
                    fragment = talkFragment;
                    break;
                case PAGE_NOTIFY:
                    fragment = notifyFragment;
                    break;
                default:
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            //return fragmentArrayMap.size();
            return fragmentList.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.destroyItem(container, position, object);
        }

    }

    /**
     * @brief 自定义接口类，重写了ViewPager的OnPageChangeListener，用于处理换页对UI控件状态改变的逻辑
     * @note 只能用于ViewPager 对象 的 addOnPageChangeListener()方法
     */
    private class TabPageChangeListener implements ViewPager.OnPageChangeListener {

        private final int NO_ACTION = 0;

        private final int SCROLL_ON = 1;

        private final int SCROLL_OVER = 2;

        int state;

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

        }

        /**
         * @brief 对滑动状态监听的接口
         * @param i 表示滑动状态码
         *          i = 0 -- 无改变
         *          i = 1 -- 正在滑动
         *          i = 2 -- 滑动结束
         */
        @Override
        public void onPageScrollStateChanged(int i) {
            state = i;
            if (state == SCROLL_OVER) {
               switch (tabViewPager.getCurrentItem()) {
                   case PAGE_MESSAGE:
                       setRelativeTextSelected(message, messageLine);
                       setRelativeTextUnSelected(talk, talkLine);
                       setRelativeTextUnSelected(notify, notifyLine);
                       break;
                   case PAGE_TALK:
                       setRelativeTextUnSelected(message, messageLine);
                       setRelativeTextSelected(talk, talkLine);
                       setRelativeTextUnSelected(notify, notifyLine);
                       break;
                   case PAGE_NOTIFY:
                       setRelativeTextUnSelected(message, messageLine);
                       setRelativeTextUnSelected(talk, talkLine);
                       setRelativeTextSelected(notify, notifyLine);
                       break;
                   default:
               }
            }
        }
    }

}


