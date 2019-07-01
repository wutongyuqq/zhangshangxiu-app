package com.shoujia.zhangshangxiu.web.util;

import android.content.Context;

/**
 * WaitTool.java
 * 
 * comments.
 * 
 * @author xudq
 * @company SANY Heavy Industry Co, Ltd
 * @creation date 2015-7-27
 * @version $Revision: 3 $
 */
public class WaitTool {

    private static CustomProgressDialog waitingDialog;

    public static void showLongDialog(Context cont, IWaitParent parent) {
        try {
//            msg = msg == null ? "..." : msg;
            if (waitingDialog == null) {
                waitingDialog = CustomProgressDialog.createDialog(cont);
            }
            waitingDialog.setCanceledOnTouchOutside(false);
            waitingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showDialog(Context cont) {
        try {
            if (waitingDialog == null) {
                waitingDialog = CustomProgressDialog.createDialog(cont);
                // waitingDialog.setMessage("正在加载中...");
            }
            waitingDialog.setCanceledOnTouchOutside(false);
            waitingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 

    public static void dismissDialog() {
        try {
            if (waitingDialog != null) {
                // waitingDialog.dismiss();
                waitingDialog.cancelDismiss();
                waitingDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
