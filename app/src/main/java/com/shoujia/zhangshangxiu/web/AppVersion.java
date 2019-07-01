package com.shoujia.zhangshangxiu.web;

public class AppVersion {
	private String host;
	private int version;
	private boolean isKeep;
	public AppVersion(String host, int version, boolean isKeep) {
		this.host = host;
		this.version = version;
		this.isKeep = isKeep;
	}
	
	public AppVersion() {
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public boolean isKeep() {
		return isKeep;
	}

	public void setKeep(boolean isKeep) {
		this.isKeep = isKeep;
	}
	
	
}
