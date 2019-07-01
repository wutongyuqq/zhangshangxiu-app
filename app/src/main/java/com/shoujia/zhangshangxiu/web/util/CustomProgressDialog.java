/*
 * @(#)CustomProgressDialog.java
 * 
 * Copyright(c)2001-2015 SANY Heavy Industry Co.,Ltd
 * All right reserved.
 * 
 * 这个软件是属于三一重工股份有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一重工股份有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information 
 * of SANY Heavy Industry Co, Ltd. You shall not disclose such 
 * Confidential Information and shall use it only in accordance 
 * with the terms of the license agreement you entered into with 
 * SANY Heavy Industry Co, Ltd.
 */
package com.shoujia.zhangshangxiu.web.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import com.shoujia.zhangshangxiu.R;


/**
 * CustomProgressDialog.java
 * 
 * comments.
 * 
 * @author xudq
 * @company SANY Heavy Industry Co, Ltd
 * @creation date 2015-7-27
 * @version $Revision: 3 $
 */
public class CustomProgressDialog extends Dialog {
    private Context context = null;

    private static CustomProgressDialog customProgressDialog = null;

    private Boolean cancelflag = true;

    private IWaitParent parent;

    public CustomProgressDialog(Context context) {
        super(context);
        this.context = context;
    }
    public CustomProgressDialog(Context context, int theme, Boolean cancelflag) {
        super(context, theme);
        this.context = context;
        this.cancelflag = cancelflag;
    }
 
    @Override
    public void dismiss() {
        if (parent != null) {
            parent.waitDialogCanced();
        }
        if (cancelflag) {
            super.dismiss();
        }
    }

    public void cancelDismiss() {
        cancelflag = true;
        dismiss();
    }

    public static CustomProgressDialog createDialog(Context context) {
        customProgressDialog = new CustomProgressDialog(context, R.style.Theme_AppCompat_Dialog, null);
        customProgressDialog.setContentView(R.layout.dialog_wait);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

        return customProgressDialog;
    }

   

    public void setCanceHandler(IWaitParent parent) {
        this.parent = parent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        WaitTool.dismissDialog();
        parent.activityFinish();
    }

   

}
