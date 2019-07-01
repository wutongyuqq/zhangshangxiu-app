package com.shoujia.zhangshangxiu.support;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.home.HomeActivity;
import com.shoujia.zhangshangxiu.home.HomeZsxFragment;
import com.shoujia.zhangshangxiu.manager.FactoryManagerFragment;
import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.introduce.FactoryIntroduceFragment;
import com.shoujia.zhangshangxiu.msgcenter.MsgCenterFragment;
import com.shoujia.zhangshangxiu.setting.SettingFragment;

import java.util.ArrayList;
import java.util.List;

public class TabSupport implements View.OnClickListener{

    private Activity mActivity;
    private int mFromType=1;//1、首页 2、附近  3、消息  4、个人
    private RelativeLayout rl_bttom_01,rl_bottom_02,rl_bottom_03, rl_bottom_04,rl_bottom_05;
    private ImageView iv_one,iv_two,iv_three,iv_four,iv_five;
    private TextView iv_one_txt,iv_two_txt,iv_three_txt,iv_four_txt,iv_five_txt;
    private List<Fragment> fragmentList;
    public TabSupport(Activity activity){
        this.mActivity = activity;
        initView();
    }

    public void setFromType(int fromType){
        this.mFromType = fromType;
    }

    private void initView() {
        fragmentList = new ArrayList<>(5);
        fragmentList.add(new HomeZsxFragment());
        fragmentList.add(new FactoryManagerFragment());
        fragmentList.add(new FactoryIntroduceFragment());
        fragmentList.add(new MsgCenterFragment());
        fragmentList.add(new SettingFragment());
        rl_bttom_01 = (RelativeLayout) findViewById(R.id.rl_bttom_01);
        rl_bottom_02 = (RelativeLayout) findViewById(R.id.rl_bottom_02);
        rl_bottom_03 = (RelativeLayout) findViewById(R.id.rl_bottom_03);
        rl_bottom_04 = (RelativeLayout) findViewById(R.id.rl_bottom_04);
        rl_bottom_05 = (RelativeLayout) findViewById(R.id.rl_bottom_05);
        iv_one = (ImageView) findViewById(R.id.iv_one);
        iv_two = (ImageView) findViewById(R.id.iv_two);
        iv_three = (ImageView) findViewById(R.id.iv_three);
        iv_four = (ImageView) findViewById(R.id.iv_four);
        iv_five = (ImageView) findViewById(R.id.iv_five);
        iv_one_txt = (TextView) findViewById(R.id.tv_one_txt);
        iv_two_txt = (TextView) findViewById(R.id.tv_two_txt);
        iv_three_txt = (TextView) findViewById(R.id.tv_three_txt);
        iv_four_txt = (TextView) findViewById(R.id.tv_four_txt);
        iv_five_txt = (TextView) findViewById(R.id.tv_five_txt);
        rl_bttom_01.setOnClickListener(this);
        rl_bottom_02.setOnClickListener(this);
        rl_bottom_03.setOnClickListener(this);
        rl_bottom_04.setOnClickListener(this);
        rl_bottom_05.setOnClickListener(this);

    }

    public View findViewById(int id){
        return mActivity.findViewById(id);
    }

    private void setTabImage(int fromType){
        mFromType = fromType;
        iv_one.setImageResource(mFromType==1?R.drawable.recever_hand_blue:R.drawable.recever_hand_gray);
        iv_two.setImageResource(mFromType==2?R.drawable.factory_blue:R.drawable.factory_gray);
        iv_three.setImageResource(mFromType==3?R.drawable.condition_blue:R.drawable.condition_gray);
        iv_four.setImageResource(mFromType==4?R.drawable.msg_blue:R.drawable.msg_gray);
        iv_five.setImageResource(mFromType==5?R.drawable.setting_blue:R.drawable.setting_gray);
        iv_one_txt.setTextColor(mFromType==1?Color.parseColor("#60b9e7"):Color.parseColor("#898989"));
        iv_two_txt.setTextColor(mFromType==2?Color.parseColor("#60b9e7"):Color.parseColor("#898989"));
        iv_three_txt.setTextColor(mFromType==3?Color.parseColor("#60b9e7"):Color.parseColor("#898989"));
        iv_four_txt.setTextColor(mFromType==4?Color.parseColor("#60b9e7"):Color.parseColor("#898989"));
        iv_five_txt.setTextColor(mFromType==5?Color.parseColor("#60b9e7"):Color.parseColor("#898989"));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_bttom_01:
                setTabImage(1);
                ((HomeActivity)mActivity).setTittle("车辆接待");
                ((HomeActivity)mActivity).getSupportFragmentManager()    //
                        .beginTransaction()
                        .replace(R.id.fragment_tab,fragmentList.get(0))   // 此处的R.id.fragment_container是要盛放fragment的父容器
                        .commit();
                break;
            /*
             * 消息列表
             * */
            case R.id.rl_bottom_02:
                setTabImage(2);
                ((HomeActivity)mActivity).setTittle("车间管理");
                ((HomeActivity)mActivity).getSupportFragmentManager()    //
                        .beginTransaction()
                        .replace(R.id.fragment_tab,fragmentList.get(1))   // 此处的R.id.fragment_container是要盛放fragment的父容器
                        .commit();
                break;
            case R.id.rl_bottom_03:
                setTabImage(3);
                ((HomeActivity)mActivity).setTittle("整厂概况");
                ((HomeActivity)mActivity).getSupportFragmentManager()    //
                        .beginTransaction()
                        .replace(R.id.fragment_tab,fragmentList.get(2))   // 此处的R.id.fragment_container是要盛放fragment的父容器
                        .commit();
                break;

            case R.id.rl_bottom_04:
                setTabImage(4);
                ((HomeActivity)mActivity).setTittle("信息中心");
                ((HomeActivity)mActivity).getSupportFragmentManager()    //
                        .beginTransaction()
                        .replace(R.id.fragment_tab,fragmentList.get(3))   // 此处的R.id.fragment_container是要盛放fragment的父容器
                        .commit();
                break;
            case R.id.rl_bottom_05:
                setTabImage(5);
                ((HomeActivity)mActivity).setTittle("设置");
                ((HomeActivity)mActivity).getSupportFragmentManager()    //
                        .beginTransaction()
                        .replace(R.id.fragment_tab,fragmentList.get(4))   // 此处的R.id.fragment_container是要盛放fragment的父容器
                        .commit();
                break;
        }

    }
}
