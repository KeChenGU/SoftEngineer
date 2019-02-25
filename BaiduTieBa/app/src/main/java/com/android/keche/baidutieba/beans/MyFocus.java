package com.android.keche.baidutieba.beans;

import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MyFocus {
    private List<String> myFocuText;

    public MyFocus() {
    }

    public MyFocus(List<String> myFocuText) {
        this.myFocuText = myFocuText;
    }

    public List<String> getMyFocuText() {
        return myFocuText;
    }

    public void setMyFocuText(List<String> myFocuText) {
        this.myFocuText = myFocuText;
    }
}
