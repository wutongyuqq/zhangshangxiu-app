package com.shoujia.zhangshangxiu.order.entity;

import android.text.TextUtils;

public class ShouyinBean {
    public String Pre_payment = "";
    public String bit_amount = "";
    public String bit_compute = "";
    public String cp = "";
    public String customer_id = "";
    public String zje = "";

    public String getPre_payment() {
        return Pre_payment;
    }

    public void setPre_payment(String pre_payment) {
        Pre_payment = pre_payment;
    }

    public String getBit_amount() {
        return bit_amount;
    }

    public void setBit_amount(String bit_amount) {
        this.bit_amount = bit_amount;
    }

    public String getBit_compute() {
        return bit_compute;
    }

    public void setBit_compute(String bit_compute) {
        this.bit_compute = bit_compute;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getZje() {
        return TextUtils.isEmpty(zje)?"0":zje;
    }

    public void setZje(String zje) {
        this.zje = zje;
    }
}
