package com.shoujia.zhangshangxiu.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.shoujia.zhangshangxiu.dialog.WaitProgressDialog;

import java.lang.ref.WeakReference;

public class BaseActivity extends Activity implements OnClickListener{

    protected final int TOAST_MSG = 1;
    protected final int RESULT_JSON = 2;
    protected String toastMsg = "";
    protected String resJson = "";
    protected  MyHandler mHandler =null;
    private static WaitProgressDialog waitingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new MyHandler(this);
    }


    @Override
    public void onClick(View view) {

    }

    protected void updateUIThread(int msgInt){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler = null;
    }

    protected class MyHandler extends Handler{
        WeakReference<BaseActivity> mWeakReference;
        public MyHandler(BaseActivity activity)
        {
            mWeakReference=new WeakReference<BaseActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg)
        {
            final BaseActivity activity=mWeakReference.get();
            if(activity!=null){
                if(msg.what==TOAST_MSG){
                    Toast.makeText(BaseActivity.this,toastMsg,Toast.LENGTH_LONG).show();
                }else{
                    updateUIThread(msg.what);
                }
            }
        }
    }



    public static void showDialog(Context cont) {
        try {
            if (waitingDialog == null) {
                waitingDialog = WaitProgressDialog.createDialog(cont);
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
                waitingDialog.dismiss();
                waitingDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
