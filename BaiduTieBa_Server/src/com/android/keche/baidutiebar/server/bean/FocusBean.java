package com.android.keche.baidutiebar.server.bean;

import java.util.ArrayList;
import java.util.List;

public class FocusBean {
	
	private List<String> myFocuText;
	
	public FocusBean() {
		myFocuText = new ArrayList<>();
	}

	public FocusBean(List<String> myFocuText) {
		super();
		this.myFocuText = myFocuText;
	}

	public List<String> getMyFocuText() {
		return myFocuText;
	}

	public void setMyFocuText(List<String> myFocuText) {
		this.myFocuText = myFocuText;
	}
	
	
	
}
