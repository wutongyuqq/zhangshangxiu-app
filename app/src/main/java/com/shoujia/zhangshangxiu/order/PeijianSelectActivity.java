package com.shoujia.zhangshangxiu.order;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseActivity;
import com.shoujia.zhangshangxiu.db.DBManager;
import com.shoujia.zhangshangxiu.entity.PartsBean;
import com.shoujia.zhangshangxiu.home.help.HomeDataHelper;
import com.shoujia.zhangshangxiu.http.HttpClient;
import com.shoujia.zhangshangxiu.http.IGetDataListener;
import com.shoujia.zhangshangxiu.order.adapter.PeijianSelectOneAdapter;
import com.shoujia.zhangshangxiu.order.adapter.PeijianSelectThreeAdapter;
import com.shoujia.zhangshangxiu.order.adapter.PeijianSelectTwoAdapter;
import com.shoujia.zhangshangxiu.order.entity.TwoBean;
import com.shoujia.zhangshangxiu.support.NavSupport;
import com.shoujia.zhangshangxiu.util.Constance;
import com.shoujia.zhangshangxiu.util.SharePreferenceManager;
import com.shoujia.zhangshangxiu.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/23 0023.
 * 首页
 */
public class PeijianSelectActivity extends BaseActivity implements View.OnClickListener {

    private SharePreferenceManager sp;
    private ListView rl_pj_one_list,rl_pj_two_list,rl_pj_three_list;
    private RelativeLayout rl_pj_one,rl_pj_two,rl_pj_three;
    List<PartsBean> mPartsBeans = new ArrayList<>();
    List<TwoBean> mTwoBeans = new ArrayList<>();
    List<TwoBean> mThreeBeans = new ArrayList<>();
    PeijianSelectOneAdapter oneAdapter;
    PeijianSelectTwoAdapter twoAdapter;
    PeijianSelectThreeAdapter threeAdapter;
    TextView xinzen_peijian,add_three,tv_zdpj,tv_pj,tv_pro,make_sure_one,make_sure_two,make_sure_three;
    private int postNum = 0;
    private int totalPostNum = 0;
    int currentSelectInt = 0;
    ImageView query_btn;
    EditText headInput;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_peijian_select);
        initView();
        initData();

    }

    private void initView() {
        sp = new SharePreferenceManager(this);
        rl_pj_one_list = findViewById(R.id.rl_pj_one_list);
        View headView = View.inflate(this,R.layout.view_select_head,null);
         headInput = headView.findViewById(R.id.content);
         query_btn = headView.findViewById(R.id.query_btn);
        rl_pj_one_list.addHeaderView(headView);
        new NavSupport(this,15);
        query_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPartsBeans.clear();
                if (headInput.getText() != null && !TextUtils.isEmpty(headInput.getText().toString().trim())) {

                    String contentStr = headInput.getText().toString().trim();
                    DBManager dbManager = DBManager.getInstanse(PeijianSelectActivity.this);
                    List<PartsBean> beans = dbManager.getPartsListData(contentStr);
                    if (beans != null && beans.size() > 0) {
                        mPartsBeans.addAll(beans);
                        mHandler.sendEmptyMessage(101);
                    }
                }else{
                    DBManager dbManager = DBManager.getInstanse(PeijianSelectActivity.this);
                    List<PartsBean> beans = dbManager.getPartsListData();
                    if (beans != null && beans.size() > 0) {
                        mPartsBeans.addAll(beans);
                        mHandler.sendEmptyMessage(101);
                    }
                }
            }
        });
        rl_pj_two_list = findViewById(R.id.rl_pj_two_list);
        rl_pj_three_list = findViewById(R.id.rl_pj_three_list);
        rl_pj_one = findViewById(R.id.rl_pj_one);
        rl_pj_two = findViewById(R.id.rl_pj_two);
        rl_pj_three = findViewById(R.id.rl_pj_three);
        xinzen_peijian = findViewById(R.id.xinzen_peijian);
        tv_zdpj = findViewById(R.id.tv_zdpj);
        tv_pj = findViewById(R.id.tv_pj);
        tv_pro = findViewById(R.id.tv_pro);
        add_three = findViewById(R.id.add_three);
        make_sure_one = findViewById(R.id.make_sure_one);
        make_sure_two = findViewById(R.id.make_sure_two);
        make_sure_three = findViewById(R.id.make_sure_three);
        oneAdapter = new PeijianSelectOneAdapter(this,mPartsBeans);
        mTwoBeans.add(new TwoBean());
        twoAdapter = new PeijianSelectTwoAdapter(this,mTwoBeans);
        mThreeBeans.add(new TwoBean());
        threeAdapter = new PeijianSelectThreeAdapter(this,mThreeBeans);
        rl_pj_one_list.setAdapter(oneAdapter);
        rl_pj_two_list.setAdapter(twoAdapter);
        rl_pj_three_list.setAdapter(threeAdapter);
        xinzen_peijian.setOnClickListener(this);
        add_three.setOnClickListener(this);
        tv_pro.setOnClickListener(this);
        tv_pj.setOnClickListener(this);
        tv_zdpj.setOnClickListener(this);
        make_sure_one.setOnClickListener(this);
        make_sure_two.setOnClickListener(this);
        make_sure_three.setOnClickListener(this);

    }



    private void getPeijianDataList(){
        HomeDataHelper helper = new HomeDataHelper(this);
        helper.getPartsList(new HomeDataHelper.UpdateDataListener() {
            @Override
            public void onSuccess() {
                DBManager dbManager = DBManager.getInstanse(PeijianSelectActivity.this);
                List<PartsBean> beans = dbManager.getPartsListData();
                if(beans!=null&&beans.size()>0) {
                    mPartsBeans.addAll(beans);
                    mHandler.sendEmptyMessage(101);
                }
            }
        });
    }



    //初始化数据
    private void initData() {
        DBManager dbManager = DBManager.getInstanse(this);
        mPartsBeans.addAll(dbManager.getPartsListData());
        if(mPartsBeans==null||mPartsBeans.size()==0){
            getPeijianDataList();
        }else{
            mHandler.sendEmptyMessage(101);
        }

    }

    @Override
    protected void updateUIThread(int msgInt) {
        super.updateUIThread(msgInt);
        if(msgInt==101){
            oneAdapter.notifyDataSetChanged();
        }
    }

    private void choosePosition(int position){
        currentSelectInt = position;
        TextView[] textViews = {tv_pro,tv_pj,tv_zdpj};
        RelativeLayout[] relativeLayouts = {rl_pj_one,rl_pj_two,rl_pj_three};
        for(int i=0;i<3;i++){
            if(i==position){
                relativeLayouts[i].setVisibility(View.VISIBLE);
                textViews[i].setTextColor(Color.parseColor("#ffffff"));
                textViews[i].setBackgroundColor(Color.parseColor("#ff9db4"));
            }else{
                relativeLayouts[i].setVisibility(View.GONE);
                textViews[i].setTextColor(Color.parseColor("#666666"));
                textViews[i].setBackgroundColor(Color.parseColor("#A4A3A3"));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pj:
                choosePosition(1);
                break;
            case R.id.tv_pro:
                choosePosition(0);
                break;
            case R.id.tv_zdpj:
                choosePosition(2);
                break;
                case R.id.xinzen_peijian:
                    mTwoBeans.add(new TwoBean());
                    twoAdapter.notifyDataSetChanged();
                break;
            case R.id.add_three:
                mThreeBeans.add(new TwoBean());
                threeAdapter.notifyDataSetChanged();
                break;
            case R.id.make_sure_one:
            case R.id.make_sure_two:
            case R.id.make_sure_three:
                makeSureOne();
                break;
            default:

                break;
        }
    }

    private void makeSureOne() {
        postNum = 0;
        totalPostNum = 0;
        if(currentSelectInt ==0 ) {
            totalPostNum = 0;
            for (PartsBean bean : mPartsBeans) {
                if (bean.isSelected()) {
                    totalPostNum++;
                    makeSureData(bean);
                }
            }
        }else if(currentSelectInt ==1 ){
            totalPostNum = 0;
            for (TwoBean bean : mTwoBeans) {
                if (bean.name!=null&&bean.jinjia!=null&&bean.xiaojia!=null&&bean.shuliang!=null) {
                    totalPostNum++;
                    PartsBean bean2 = new PartsBean();
                    bean2.setPjmc(bean.name);
                    bean2.setXsj(bean.xiaojia);
                    bean2.setPjjj(bean.jinjia);
                    bean2.setSl(bean.shuliang);
                    makeSureTmpData(bean2);
                }
            }
        }else if(currentSelectInt ==2 ){
            totalPostNum = 0;
            for (TwoBean bean : mThreeBeans) {
                if (bean.name!=null&&bean.shuliang!=null) {
                    totalPostNum++;
                    PartsBean bean2 = new PartsBean();
                    bean2.setPjmc(bean.name);
                    bean2.setSl(bean.shuliang);
                    makeSureTmpData(bean2);
                }
            }
        }

    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    private void makeSureData(PartsBean bean){
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_upload_parts_project_detail");
        dataMap.put("jsd_id", sp.getString(Constance.JSD_ID));
        dataMap.put("pjbm", bean.getPjbm());
        dataMap.put("pjmc", bean.getPjmc());
        dataMap.put("ck", bean.getCk());
        dataMap.put("cd", bean.getCd());
        dataMap.put("cx", bean.getCx());
        dataMap.put("dw", bean.getDw());
        dataMap.put("property","维修");
        dataMap.put("zt","");
        dataMap.put("ssj",bean.getXsj());
        dataMap.put("cb",bean.getPjjj());
        dataMap.put("sl",bean.getSl());
        dataMap.put("xh","0");
        dataMap.put("operater_code", sp.getString(Constance.USERNAME));
        dataMap.put("comp_code", sp.getString(Constance.COMP_CODE));


        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                    postNum ++;
                    if(postNum==totalPostNum) {
                        //mHandler.sendEmptyMessage(103);
                        setResult(2);
                        finish();
                    }
                } else {
                    if (resMap.get("msg") != null) {
                        toastMsg = (String) resMap.get("msg");
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    } else {
                        toastMsg = "网络异常";
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }
                }
            }

            @Override
            public void onFail() {
                toastMsg = "网络连接异常";
                mHandler.sendEmptyMessage(TOAST_MSG);
            }
        });
    }

    private void makeSureTmpData(PartsBean bean){
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_upload_parts_project_detail");
        dataMap.put("jsd_id", sp.getString(Constance.JSD_ID));
        dataMap.put("pjbm", bean.getPjbm());
        dataMap.put("pjmc", bean.getPjmc());
        dataMap.put("ck", bean.getCk());
        dataMap.put("cd", bean.getCd());
        dataMap.put("cx", bean.getCx());
        dataMap.put("dw", bean.getDw());
        dataMap.put("property","维修");
        dataMap.put("zt","急件销售");
        dataMap.put("ssj",bean.getXsj());
        dataMap.put("cb",bean.getPjjj());
        dataMap.put("sl",bean.getSl());
        dataMap.put("xh","0");
        dataMap.put("operater_code", sp.getString(Constance.USERNAME));
        dataMap.put("comp_code", sp.getString(Constance.COMP_CODE));


        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                    postNum ++;
                    if(postNum==totalPostNum) {
                        //mHandler.sendEmptyMessage(103);
                        setResult(2);
                        finish();
                    }
                } else {
                    if (resMap.get("msg") != null) {
                        toastMsg = (String) resMap.get("msg");
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    } else {
                        toastMsg = "网络异常";
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }
                }
            }

            @Override
            public void onFail() {
                toastMsg = "网络连接异常";
                mHandler.sendEmptyMessage(TOAST_MSG);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}
