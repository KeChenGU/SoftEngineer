package com.android.keche.baidutiebar.server.bean;

public class RecentBarBean {
	
	private String iconUrl;
	
	private String barName;
	
	public RecentBarBean() {
		
	}
	
	public RecentBarBean(String iconUrl, String barName) {
		super();
		this.iconUrl = iconUrl;
		this.barName = barName;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getBarName() {
		return barName;
	}

	public void setBarName(String barName) {
		this.barName = barName;
	}
	
	
}
