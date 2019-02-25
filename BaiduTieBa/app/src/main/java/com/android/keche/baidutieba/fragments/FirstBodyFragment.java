package com.android.keche.baidutieba.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.keche.baidutieba.R;
import com.android.keche.baidutieba.serials.FirstPageDistribution;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谷恪忱
 * @brief 首页模块主碎片
 * 内部使用ViewPager切换页
 */
public class FirstBodyFragment extends Fragment implements View.OnClickListener{

    private TextView focus;       /**< 关注 **/

    private TextView focusLine;   /**< 底部隐藏线（关注） **/

    private TextView first;          /**< 首页 **/

    private TextView firstLine;      /**< 底部隐藏线（首页） **/

    private TextView online;        /**< 直播 **/

    private TextView onlineLine;    /**< 底部隐藏线（直播） **/

    private ViewPager tabViewPager; /**< 视图换页者，此处用于实现左右滑动切换 **/

    private PageTabAdapter tabAdapter;  /**< 视图换页者的适配器 **/

    public static final int PAGE_FOCUS = 0;    /**< 关注 **/

    public static final int PAGE_FIRST = 1;    /**< 首页 **/

    public static final int PAGE_ONLINE = 2;   /**< 直播 **/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_body, container, false);
        initUIs(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.f_n_focus:
            case R.id.f_n_focus_bottom_line:
                setRelativeTextSelected(focus, focusLine);
                setRelativeTextUnSelected(first, firstLine);
                setRelativeTextUnSelected(online, onlineLine);
                // fragmentTag = tabAdapter.messageFragment.getTag();
                tabViewPager.setCurrentItem(PAGE_FOCUS, true);
                break;
            case R.id.f_n_first:
            case R.id.f_n_first_bottom_line:
                setRelativeTextUnSelected(focus, focusLine);
                setRelativeTextSelected(first, firstLine);
                setRelativeTextUnSelected(online, onlineLine);
                tabViewPager.setCurrentItem(PAGE_FIRST, true);
                break;
            case R.id.f_n_online:
            case R.id.f_n_online_bottom_line:

                setRelativeTextUnSelected(focus, focusLine);
                setRelativeTextUnSelected(first, firstLine);
                setRelativeTextSelected(online, onlineLine);
                tabViewPager.setCurrentItem(PAGE_ONLINE, true);
                break;
            default:
        }
    }

    /**
     * @brief 给外部活动控制页面切换的方法接口
     * @param pageFd 碎片页描述符
     *               PAGE_FOCUS -- 关注
     *               PAGE_FIRST -- 首页
     *               PAGE_ONLINE -- 视频
     */
    public void setCurrentPage(int pageFd) {
        switch (pageFd) {
            case PAGE_FOCUS:
                tabViewPager.setCurrentItem(PAGE_FOCUS, true);
                break;
            case PAGE_FIRST:
                tabViewPager.setCurrentItem(PAGE_FIRST, true);
                break;
            case PAGE_ONLINE:
                tabViewPager.setCurrentItem(PAGE_ONLINE, true);
                break;
            default:
                break;
        }
    }

//    /**
//     * @brief 往 ViewPager 中的 FirstFragment
//     * 添加 新帖子的接口
//     * @param distribution 发布具体内容参数
//     */
//    public void addDistribution(FirstPageDistribution distribution) {
//
//    }

    /**
     * @brief 初始化UI控件
     * @param view 主视图
     */
    private void initUIs(View view) {
        focus = (TextView) view.findViewById(R.id.f_n_focus);
        focusLine = (TextView) view.findViewById(R.id.f_n_focus_bottom_line);
        first = (TextView) view.findViewById(R.id.f_n_first);
        firstLine = (TextView) view.findViewById(R.id.f_n_first_bottom_line);
        online = (TextView) view.findViewById(R.id.f_n_online);
        onlineLine = (TextView) view.findViewById(R.id.f_n_online_bottom_line);
        tabViewPager = (ViewPager) view.findViewById(R.id.f_n_page_child_view_pager);
        focus.setOnClickListener(this);
        focusLine.setOnClickListener(this);
        first.setOnClickListener(this);
        firstLine.setOnClickListener(this);
        online.setOnClickListener(this);
        onlineLine.setOnClickListener(this);
        tabAdapter = new FirstBodyFragment.PageTabAdapter(getChildFragmentManager()); // !!!!!! 注意此处一定要设置孩子碎片的FragmentManager否则无法正常显示
        tabViewPager.setAdapter(tabAdapter);
        tabViewPager.setCurrentItem(PAGE_FIRST);
        tabViewPager.addOnPageChangeListener(new FirstBodyFragment.TabPageChangeListener());
        setRelativeTextSelected(first, firstLine);
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

    private class PageTabAdapter extends FragmentPagerAdapter {

        private FocusFragment focusFragment;    /**< 关注模块碎片 **/

        private FirstFragment firstFragment;    /**< 首页模块碎片 **/

        private OnlineFragment onlineFragment;  /**< 直播模块碎片 **/

        private List<Fragment> fragmentList = new ArrayList<>();

        private SparseArray<String> tagsArray = new SparseArray<>();

        private Fragment currentFragment;

        PageTabAdapter(FragmentManager fm) {
            super(fm);
            //fragmentList = new ArrayList<>();
            //tagsArray = new SparseArray<>();
            focusFragment = new FocusFragment();
            //tagsArray.put(PAGE_FOCUS, focusFragment.getTag());
            //tagsArray.append(PAGE_FOCUS, focusFragment.getTag());
            fragmentList.add(focusFragment);
            firstFragment = new FirstFragment();
            //tagsArray.put(PAGE_FIRST, firstFragment.getTag());
            //tagsArray.append(PAGE_FIRST, firstFragment.getTag());
            fragmentList.add(firstFragment);
            onlineFragment = new OnlineFragment();
            //tagsArray.put(PAGE_ONLINE, onlineFragment.getTag());
            //tagsArray.append(PAGE_ONLINE, onlineFragment.getTag());
            fragmentList.add(onlineFragment);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            switch (i) {
                case PAGE_FOCUS:
                    fragment = focusFragment;
                    break;
                case PAGE_FIRST:
                    fragment = firstFragment;
                    break;
                case PAGE_ONLINE:
                    fragment = onlineFragment;
                    break;
                default:
                    break;
            }
            currentFragment = fragment;
            return fragment;
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            String tag = fragment.getTag();
            tagsArray.put(position, tag);
            return fragment;//super.instantiateItem(container, position);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            //return super.getItemPosition(object);
            return POSITION_NONE;
        }
    }

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

        @Override
        public void onPageScrollStateChanged(int i) {
            state = i;
            if (state == SCROLL_OVER) {
                switch (tabViewPager.getCurrentItem()) {
                    case PAGE_FOCUS:
                        setRelativeTextSelected(focus, focusLine);
                        setRelativeTextUnSelected(first, firstLine);
                        setRelativeTextUnSelected(online, onlineLine);
                        break;
                    case PAGE_FIRST:
                        setRelativeTextUnSelected(focus, focusLine);
                        setRelativeTextSelected(first, firstLine);
                        setRelativeTextUnSelected(online, onlineLine);
                        break;
                    case PAGE_ONLINE:
                        setRelativeTextUnSelected(focus, focusLine);
                        setRelativeTextUnSelected(first, firstLine);
                        setRelativeTextSelected(online, onlineLine);
                        break;
                    default:
                }
            }
        }
    }
}
