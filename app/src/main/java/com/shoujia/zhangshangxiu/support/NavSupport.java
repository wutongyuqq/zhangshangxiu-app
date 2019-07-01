package com.shoujia.zhangshangxiu.support;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;

public class NavSupport  implements View.OnClickListener{

    private ImageView right_btn,left_btn;
    private Activity mActivity;
    private TextView tv_title;
    private int mFromType;
    public NavSupport(Activity activity, int fromType){
        this.mActivity = activity;
        this.mFromType = fromType;
        initView();
    }
    private void initView(){
        right_btn = (ImageView) findViewById(R.id.right_btn);
        left_btn = (ImageView) findViewById(R.id.left_btn);
        tv_title = (TextView) findViewById(R.id.title);
        right_btn.setOnClickListener(this);
        left_btn.setOnClickListener(this);
        if(mFromType==1){
            setTittle("车辆接待");
        }else if(mFromType==2){
            setTittle("业务导航");
        }else if(mFromType==3){
            setTittle("在厂车辆");
            setLeftBtnVisible(true);
        }else if(mFromType==4){
            setTittle("项目选择");
            setLeftBtnVisible(true);
        }else if(mFromType==6){
            setTittle("搜索车辆");
            setLeftBtnVisible(true);
        }else if(mFromType==7){
            setTittle("已完成工单");
            setLeftBtnVisible(true);
        }
    }

    public void setLeftBtnVisible(boolean isVisible){
        left_btn.setVisibility(isVisible?View.VISIBLE:View.GONE);
    }



    public void setTittle(String title){
        tv_title.setText(title);
    }

    private View findViewById(int id){
        return mActivity.findViewById(id);
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {


            case R.id.left_btn:
                mActivity.finish();
                break;
            default:
                break;
        }

    }
}
