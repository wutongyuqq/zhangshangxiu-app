package com.shoujia.zhangshangxiu.entity;

import android.text.TextUtils;

import java.io.Serializable;

public class CarInfo implements Serializable {
    int id;
    String cjhm;
    String custom5;
    String customer_id;
    String cx;
    String cz;
    String fdjhm;
    String linkman;
    String mc = "";
    String mobile;
    String ns_date;
    String openid;
    String phone;
    String vipnumber;
    String gzms;//故障描述
    String gls;//公里数
    String memo;//备注
    String keys_no;//钥匙编号
    String jsd_id;//结算单编号
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCjhm() {
        return getString(cjhm);
    }

    public void setCjhm(String cjhm) {
        this.cjhm = cjhm;
    }

    public String getCustom5() {
        return getString(custom5);
    }

    public void setCustom5(String custom5) {
        this.custom5 = custom5;
    }

    public String getCustomer_id() {
        return getString(customer_id);
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCx() {
        return getString(cx);
    }

    public void setCx(String cx) {
        this.cx = cx;
    }

    public String getCz() {
        return getString(cz);
    }

    public void setCz(String cz) {
        this.cz = cz;
    }

    public String getFdjhm() {
        return getString(fdjhm);
    }

    public void setFdjhm(String fdjhm) {
        this.fdjhm = fdjhm;
    }

    public String getLinkman() {
        return getString(linkman);
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getMc() {
        return getString(mc);
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getMobile() {
        return getString(mobile);
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNs_date() {
        return getString(ns_date);
    }

    public void setNs_date(String ns_date) {
        this.ns_date = ns_date;
    }

    public String getOpenid() {
        return getString(openid);
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPhone() {
        return getString(phone);
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVipnumber() {
        return getString(vipnumber);
    }

    public void setVipnumber(String vipnumber) {
        this.vipnumber = vipnumber;
    }

    private String getString(String value){

            return TextUtils.isEmpty(value)?"":value;

    }

    public String getGzms() {
        return gzms;
    }

    public void setGzms(String gzms) {
        this.gzms = gzms;
    }

    public String getGls() {
        return gls;
    }

    public void setGls(String gls) {
        this.gls = gls;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getKeys_no() {
        return keys_no;
    }

    public void setKeys_no(String keys_no) {
        this.keys_no = keys_no;
    }

    public String getJsd_id() {
        return jsd_id;
    }

    public void setJsd_id(String jsd_id) {
        this.jsd_id = jsd_id;
    }

    @Override
    public String toString() {
        return "CarInfo{" +
                "id=" + id +
                ", cjhm='" + cjhm + '\'' +
                ", custom5='" + custom5 + '\'' +
                ", customer_id='" + customer_id + '\'' +
                ", cx='" + cx + '\'' +
                ", cz='" + cz + '\'' +
                ", fdjhm='" + fdjhm + '\'' +
                ", linkman='" + linkman + '\'' +
                ", mc='" + mc + '\'' +
                ", mobile='" + mobile + '\'' +
                ", ns_date='" + ns_date + '\'' +
                ", openid='" + openid + '\'' +
                ", phone='" + phone + '\'' +
                ", vipnumber='" + vipnumber + '\'' +
                ", gzms='" + gzms + '\'' +
                ", gls='" + gls + '\'' +
                ", memo='" + memo + '\'' +
                ", keys_no='" + keys_no + '\'' +
                ", jsd_id='" + jsd_id + '\'' +
                '}';
    }
}
