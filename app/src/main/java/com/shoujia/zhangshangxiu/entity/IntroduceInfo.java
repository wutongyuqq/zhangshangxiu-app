package com.shoujia.zhangshangxiu.entity;

public class IntroduceInfo {
	private String daichuchang;
	private String daijiesuan;
	private String dailinggong;
	private String daipaigong;
	private String daishenhe;
	private String gujia;
	private String in_garage;
	private String incoming_today;
	private String out_today;
	private String repairing;

	public String getDaichuchang() {
		return daichuchang==null?"0":daichuchang;
	}

	public void setDaichuchang(String daichuchang) {
		this.daichuchang = daichuchang;
	}

	public String getDaijiesuan() {
		return daijiesuan==null?"0":daijiesuan;
	}

	public void setDaijiesuan(String daijiesuan) {
		this.daijiesuan = daijiesuan;
	}

	public String getDailinggong() {
		return dailinggong==null?"0":dailinggong;
	}

	public void setDailinggong(String dailinggong) {
		this.dailinggong = dailinggong;
	}

	public String getDaipaigong() {
		return daipaigong==null?"0":daipaigong;
	}

	public void setDaipaigong(String daipaigong) {
		this.daipaigong = daipaigong;
	}

	public String getDaishenhe() {
		return daishenhe==null?"0":daishenhe;
	}

	public void setDaishenhe(String daishenhe) {
		this.daishenhe = daishenhe;
	}

	public String getGujia() {
		return gujia==null?"0":gujia;
	}

	public void setGujia(String gujia) {
		this.gujia = gujia;
	}

	public String getIn_garage() {
		return in_garage==null?"0":in_garage;
	}

	public void setIn_garage(String in_garage) {
		this.in_garage = in_garage;
	}

	public String getIncoming_today() {
		return incoming_today==null?"0":incoming_today;
	}

	public void setIncoming_today(String incoming_today) {
		this.incoming_today = incoming_today;
	}

	public String getOut_today() {
		return out_today==null?"0":out_today;
	}

	public void setOut_today(String out_today) {
		this.out_today = out_today;
	}

	public String getRepairing() {
		return repairing==null?"0":repairing;
	}

	public void setRepairing(String repairing) {
		this.repairing = repairing;
	}
}
