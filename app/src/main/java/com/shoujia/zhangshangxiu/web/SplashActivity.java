package com.shoujia.zhangshangxiu.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.login.PhoneLoginActivity;

/**
 * Created by Administrator on 2018/6/10 0010.
 */

public class SplashActivity extends Activity {
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_main);
        context = this;
        //initData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                    Intent intent = new Intent(context,PhoneLoginActivity.class);
                    startActivity(intent);
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }



}
