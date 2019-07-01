package com.shoujia.zhangshangxiu.entity;

public class FirstIconInfo {
    int id;
    String imageurl;
    String wxgz;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageurl() {
        return imageurl==null?"":imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getWxgz() {
        return wxgz==null?"":wxgz;
    }

    public void setWxgz(String wxgz) {
        this.wxgz = wxgz;
    }
}
