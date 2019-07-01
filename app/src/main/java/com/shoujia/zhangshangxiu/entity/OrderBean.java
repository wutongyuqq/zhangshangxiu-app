package com.shoujia.zhangshangxiu.entity;

/**
 * Created by Administrator on 2017/2/27 0027.
 */
public class OrderBean {
	private String cp;
	private String jc_date;
	private String jsd_id;
	private String wxgz_collect;
	private String zje;

	public String getCp() {
		return cp==null?"":cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getJc_date() {
		return jc_date==null?"":jc_date;
	}

	public void setJc_date(String jc_date) {
		this.jc_date = jc_date;
	}

	public String getJsd_id() {
		return jsd_id==null?"":jsd_id;
	}

	public void setJsd_id(String jsd_id) {
		this.jsd_id = jsd_id;
	}

	public String getWxgz_collect() {
		return wxgz_collect==null?"":wxgz_collect;
	}

	public void setWxgz_collect(String wxgz_collect) {
		this.wxgz_collect = wxgz_collect;
	}

	public String getZje() {
		return zje==null?"":zje;
	}

	public void setZje(String zje) {
		this.zje = zje;
	}
}
