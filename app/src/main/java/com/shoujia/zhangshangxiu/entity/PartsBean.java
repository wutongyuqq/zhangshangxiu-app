package com.shoujia.zhangshangxiu.entity;

import android.text.TextUtils;

public class PartsBean {
	private int id;
	private String pjbm;
	private String pjmc;
	private String ck;
	private String cd;
	private String cx;
	private String dw;
	private String cangwei;
	private String bz;
	private String type;
	private String pjjj;
	private String kcl;
	private String xsj;
	private String sl;
	private boolean isSelected;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPjbm() {
		return pjbm == null?"":pjbm;
	}

	public void setPjbm(String pjbm) {
		this.pjbm = pjbm;
	}

	public String getPjmc() {
		return pjmc == null?"":pjmc;
	}

	public void setPjmc(String pjmc) {
		this.pjmc = pjmc;
	}

	public String getCk() {
		return ck == null?"":ck;
	}

	public void setCk(String ck) {
		this.ck = ck;
	}

	public String getCd() {
		return cd == null?"":cd;
	}

	public void setCd(String cd) {
		this.cd = cd;
	}

	public String getCx() {
		return cx == null?"":cx;
	}

	public void setCx(String cx) {
		this.cx = cx;
	}

	public String getDw() {
		return dw== null?"":dw;
	}

	public void setDw(String dw) {
		this.dw = dw;
	}

	public String getCangwei() {
		return cangwei== null?"":cangwei;
	}

	public void setCangwei(String cangwei) {
		this.cangwei = cangwei;
	}

	public String getBz() {
		return bz== null?"":bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getType() {
		return type== null?"":type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPjjj() {
		return TextUtils.isEmpty(pjjj)?"0":pjjj;
	}

	public void setPjjj(String pjjj) {
		this.pjjj = pjjj;
	}

	public String getKcl() {
		if(kcl!=null){
			kcl = kcl.replace(".0000","");
		}
		return kcl== null?"":kcl;
	}

	public void setKcl(String kcl) {
		this.kcl = kcl;
	}

	public String getXsj() {
		if(xsj!=null){
			xsj = xsj.replace(".0000","");
			if(xsj.endsWith("00")&&xsj.contains(".")){
				xsj = xsj.substring(0,xsj.lastIndexOf("00"));
			}
		}
		return TextUtils.isEmpty(xsj)?"0":xsj;
	}

	public void setXsj(String xsj) {
		this.xsj = xsj;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public String getSl() {
		return TextUtils.isEmpty(sl)?"1":sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}
}
