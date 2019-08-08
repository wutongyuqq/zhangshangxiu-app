package com.shoujia.zhangshangxiu.entity;

import android.text.TextUtils;

/**
 * Created by Administrator on 2017/2/27 0027.
 */
public class ProjectBean {
	private String ssje;
	private String wxgz;
	private String xh;
	private String xlf;
	private String xlxm;
	private String zk;
	private String cb;

	public String getSsje() {
		return TextUtils.isEmpty(ssje)?"0":ssje;
	}

	public void setSsje(String ssje) {
		this.ssje = ssje;
	}

	public String getWxgz() {
		return wxgz;
	}

	public void setWxgz(String wxgz) {
		this.wxgz = wxgz;
	}

	public String getXh() {
		return TextUtils.isEmpty(xh)?"0":xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getXlf() {
		return TextUtils.isEmpty(xlf)?"0":xlf;
	}

	public void setXlf(String xlf) {
		this.xlf = xlf;
	}

	public String getXlxm() {
		return xlxm;
	}

	public void setXlxm(String xlxm) {
		this.xlxm = xlxm;
	}

	public String getZk() {
		return TextUtils.isEmpty(zk)?"0":zk;
	}

	public void setZk(String zk) {
		this.zk = zk;
	}

	public String getCb() {
		return cb;
	}

	public void setCb(String cb) {
		this.cb = cb;
	}
}
