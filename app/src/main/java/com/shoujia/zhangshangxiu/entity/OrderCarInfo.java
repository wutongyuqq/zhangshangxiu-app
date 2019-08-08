package com.shoujia.zhangshangxiu.entity;

import android.text.TextUtils;

import java.io.Serializable;

public class OrderCarInfo implements Serializable {
    String cp;
    String cz;
    String customer_id;
    String cx;
    String cjhm;
    String jclc;
    String memo;
    String xllb = "";
    String djzt;
    String zje;
    String wxfzj;
    String clfzj;
    String clcb;
    String jc_date;//故障描述
    String ywg_date;//公里数
    String car_fault;//备注
    String custom5;//钥匙编号

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getCz() {
        return cz;
    }

    public void setCz(String cz) {
        this.cz = cz;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCx() {
        return cx;
    }

    public void setCx(String cx) {
        this.cx = cx;
    }

    public String getCjhm() {
        return cjhm;
    }

    public void setCjhm(String cjhm) {
        this.cjhm = cjhm;
    }

    public String getJclc() {
        return jclc;
    }

    public void setJclc(String jclc) {
        this.jclc = jclc;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getXllb() {
        return xllb;
    }

    public void setXllb(String xllb) {
        this.xllb = xllb;
    }

    public String getDjzt() {
        return djzt;
    }

    public void setDjzt(String djzt) {
        this.djzt = djzt;
    }

    public String getZje() {
        return zje==null?"0":zje;
    }

    public void setZje(String zje) {
        this.zje = zje;
    }

    public String getWxfzj() {
        return TextUtils.isEmpty(wxfzj)?"0":wxfzj;
    }

    public void setWxfzj(String wxfzj) {
        this.wxfzj = wxfzj;
    }

    public String getClfzj() {
        return TextUtils.isEmpty(clfzj)?"0":clfzj;
    }

    public void setClfzj(String clfzj) {
        this.clfzj = clfzj;
    }

    public String getClcb() {
        return clcb;
    }

    public void setClcb(String clcb) {
        this.clcb = clcb;
    }

    public String getJc_date() {
        return jc_date==null?"":jc_date;
    }

    public void setJc_date(String jc_date) {
        this.jc_date = jc_date;
    }

    public String getYwg_date() {
        return ywg_date;
    }

    public void setYwg_date(String ywg_date) {
        this.ywg_date = ywg_date;
    }

    public String getCar_fault() {
        return car_fault==null?"":car_fault;
    }

    public void setCar_fault(String car_fault) {
        this.car_fault = car_fault;
    }

    public String getCustom5() {
        return custom5==null?"":custom5;
    }

    public void setCustom5(String custom5) {
        this.custom5 = custom5;
    }

    @Override
    public String toString() {
        return "OrderCarInfo{" +
                "cp='" + cp + '\'' +
                ", cz='" + cz + '\'' +
                ", customer_id='" + customer_id + '\'' +
                ", cx='" + cx + '\'' +
                ", cjhm='" + cjhm + '\'' +
                ", jclc='" + jclc + '\'' +
                ", memo='" + memo + '\'' +
                ", xllb='" + xllb + '\'' +
                ", djzt='" + djzt + '\'' +
                ", zje='" + zje + '\'' +
                ", wxfzj='" + wxfzj + '\'' +
                ", clfzj='" + clfzj + '\'' +
                ", clcb='" + clcb + '\'' +
                ", jc_date='" + jc_date + '\'' +
                ", ywg_date='" + ywg_date + '\'' +
                ", car_fault='" + car_fault + '\'' +
                ", custom5='" + custom5 + '\'' +
                '}';
    }


}
