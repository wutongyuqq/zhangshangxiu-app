package com.shoujia.zhangshangxiu.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseActivity;
import com.shoujia.zhangshangxiu.support.InfoSupport;
import com.shoujia.zhangshangxiu.support.NavSupport;
import com.shoujia.zhangshangxiu.util.SharePreferenceManager;

/**
 * Created by Administrator on 2017/2/23 0023.
 * 首页
 */
public class ProjectShouyinActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "ProjectOrderActivity";


    private SharePreferenceManager sp;
  

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pro_shouyin);
        initView();
        initData();
     
    }

    private void initData() {
    }

    private void initView() {
        sp = new SharePreferenceManager(this);
        new NavSupport(this, 1);
        new InfoSupport(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
         
            default:

                break;
        }
    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
     
    }
}
