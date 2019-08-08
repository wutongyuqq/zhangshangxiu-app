package com.shoujia.zhangshangxiu.entity;

import android.text.TextUtils;

import java.io.Serializable;

public class GuzhangInfo implements Serializable {

    String fault_info;
    String days;

    public String getFault_info() {
        return fault_info==null?"":fault_info;
    }

    public void setFault_info(String fault_info) {
        this.fault_info = fault_info;
    }

    public String getDays() {
        if(TextUtils.isEmpty(days)){
            return "";
        }else if(days.length()>10){
            return days.substring(0,10);
        }
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}
