package com.android.keche.baidutieba.fragments;

import com.android.keche.baidutieba.R;
import com.android.keche.baidutieba.beans.ReceiveMessageBean;
import com.android.keche.baidutieba.beans.TalkMessageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谷恪忱
 * @brief 仅提供测试数据的数据类
 */
class DefaultDataProvider {

    /**
     * @brief 生成MessageFragment默认测试数据
     * @return 用于测试的ReceiveMessageBean类信息列表
     */
    public static List<ReceiveMessageBean> createMessageTestDataList() {
        List<ReceiveMessageBean> receiveMessageBeans = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            receiveMessageBeans.add(new ReceiveMessageBean(0, null, "虾米爱吃鱼",
                    "2018-10-08", "教师资格证吧",
                    "请问还能分享一下笔记吗", "我：来我给你。"));
        }
        receiveMessageBeans.add(new ReceiveMessageBean(R.drawable.enter_select, null, "123465",
                "2019-1-2", "fjsfjlaksf", "fifhjsfa", "fsfjsalfjs"));
        return receiveMessageBeans;
    }

    /**
     * @brief 生成MessageFragment默认测试数据2
     * @return 用于测试的ReceiveMessageBean类信息列表
     */
    public static List<ReceiveMessageBean> createMessageTestData2List() {
        List<ReceiveMessageBean> receiveMessageBeans = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            receiveMessageBeans.add(new ReceiveMessageBean(R.drawable.distribute, null, "laogutou",
                    "2017-10-06", "C语言吧",
                    "请问还能分享一下资料吗", "我：C语言程序设计教程"));
        }
        receiveMessageBeans.add(new ReceiveMessageBean(R.drawable.enter_select, null, "123465",
                "2019-1-2", "fjsfjlaksf", "fifhjsfa", "fsfjsalfjs"));
        return receiveMessageBeans;
    }


    public static List<TalkMessageBean> createTalkTestDataList() {
        List<TalkMessageBean> talkMessageBeans = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            talkMessageBeans.add(new TalkMessageBean(R.drawable.m_n_talk_light, null,
                    "来自陌生人的消息", "10-06", "你好，请问还能提供你的安卓学习秘籍吗？？"));
        }
        return talkMessageBeans;
    }

}

