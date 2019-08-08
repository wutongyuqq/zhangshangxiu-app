package com.shoujia.zhangshangxiu.base;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class BaseHelper {

    private Context mActivity;
    protected final int TOAST_MSG = 1;
    protected String toastMsg = "";
    protected MyHandler mHandler =null;
    public BaseHelper(Activity activity){
        this.mActivity = activity;
        mHandler = new MyHandler(this);
    }

    public BaseHelper(Context context){
        this.mActivity = context;
        mHandler = new MyHandler(this);
    }

    public void updateUIThread(int msgInt){

    }
    protected class MyHandler extends Handler {
        WeakReference<BaseHelper> mWeakReference;
        public MyHandler(BaseHelper activity)
        {
            mWeakReference=new WeakReference<BaseHelper>(activity);
        }
        @Override
        public void handleMessage(Message msg)
        {
            final BaseHelper activity=mWeakReference.get();
            if(activity!=null){
                if(msg.what==TOAST_MSG){
                    Toast.makeText(mActivity,toastMsg,Toast.LENGTH_LONG).show();
                }else{
                    updateUIThread(msg.what);
                }
            }
        }
    }

}
