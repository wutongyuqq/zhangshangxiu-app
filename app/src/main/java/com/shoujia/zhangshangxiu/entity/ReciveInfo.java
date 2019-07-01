package com.shoujia.zhangshangxiu.entity;

import java.io.Serializable;

public class ReciveInfo implements Serializable {
    String jsd_id;
    String jcr;
    String jc_date;
    String customer_id;
    public String getJsd_id() {
        return jsd_id;
    }

    public void setJsd_id(String jsd_id) {
        this.jsd_id = jsd_id;
    }

    public String getJcr() {
        return jcr==null?"":jcr;
    }

    public void setJcr(String jcr) {
        this.jcr = jcr;
    }

    public String getJc_date() {
        return jc_date==null?"":jc_date;
    }

    public void setJc_date(String jc_date) {
        this.jc_date = jc_date;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }
}
