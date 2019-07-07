package com.shoujia.zhangshangxiu.entity;

public class RepairInfo {
    int id;
    String xlz;
    String xlg;
    boolean isSelected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getXlz() {
        return xlz;
    }

    public void setXlz(String xlz) {
        this.xlz = xlz;
    }

    public String getXlg() {
        return xlg;
    }

    public void setXlg(String xlg) {
        this.xlg = xlg;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
