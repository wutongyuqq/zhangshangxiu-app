package com.shoujia.zhangshangxiu.entity;

import android.text.TextUtils;

import java.io.Serializable;

public class PaigongInfo implements Serializable {
    boolean checked;
    String xlxm;
    String assign;
    String xlg;
    String pgje;
    String states;
    String xh;
    boolean showPgje;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getXlxm() {
        return xlxm==null?"":xlxm;
    }

    public void setXlxm(String xlxm) {
        this.xlxm = xlxm;
    }

    public String getAssign() {
        return assign==null?"":assign;
    }

    public void setAssign(String assign) {
        this.assign = assign;
    }

    public String getXlg() {
        return xlg==null?"":xlg;
    }

    public void setXlg(String xlg) {
        this.xlg = xlg;
    }

    public String getPgje() {
        return pgje==null?"":pgje;
    }

    public void setPgje(String pgje) {
        this.pgje = pgje;
    }

    public String getStates() {
        return states==null?"":states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public boolean isShowPgje() {
        return showPgje;
    }

    public void setShowPgje(boolean showPgje) {
        this.showPgje = showPgje;
    }

    public String getXh() {
        return TextUtils.isEmpty(xh)?"0":xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }
}
