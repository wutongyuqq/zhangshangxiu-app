package com.shoujia.zhangshangxiu.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;

import com.shoujia.zhangshangxiu.R;


/**
 * 等待进度条
 * @author huqiang
 *
 */
public class WaitProgressDialog extends Dialog {
    private Context context = null;
    private static WaitProgressDialog wProgressDialog = null;

    public WaitProgressDialog(Context context){
        super(context);
        this.context = context;
    }

    public WaitProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public static WaitProgressDialog createDialog(Context context){
        wProgressDialog = new WaitProgressDialog(context,R.style.DialogTheme);
        wProgressDialog.setContentView(R.layout.progress_dialog);
        wProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        wProgressDialog.setCanceledOnTouchOutside(false);
        wProgressDialog.setOnKeyListener(keylistener);
        return wProgressDialog;
    }

    static DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    } ;
}
