package com.shoujia.zhangshangxiu.support;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.util.Constance;
import com.shoujia.zhangshangxiu.util.SharePreferenceManager;

public class InfoSupport implements View.OnClickListener{

    private Activity mActivity;
    private TextView tv_title;
    TextView car_cp,person_head;
    SharePreferenceManager sp;
    public InfoSupport(Activity activity){
        this.mActivity = activity;
        initView();
    }
    private void initView(){
        sp = new SharePreferenceManager(mActivity);
        car_cp = (TextView) findViewById(R.id.car_cp);
        person_head = (TextView) findViewById(R.id.person_head);
        car_cp.setText(sp.getString(Constance.CURRENTCP));
        person_head.setText(sp.getString(Constance.CURRENTCZ));
    }

    public void setCz(String cz){
        if(cz==null){
            cz="";
        }
        person_head.setText(cz);
    }






    private View findViewById(int id){
        return mActivity.findViewById(id);
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {

            default:
                break;
        }

    }
}
