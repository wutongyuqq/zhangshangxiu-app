package com.shoujia.zhangshangxiu.manager.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseActivity;
import com.shoujia.zhangshangxiu.dialog.OrderAddTmpDialog;
import com.shoujia.zhangshangxiu.dialog.OrderDeleteDialog;
import com.shoujia.zhangshangxiu.dialog.OrderTempEditDialog;
import com.shoujia.zhangshangxiu.dialog.ProjectPaigongDialog;
import com.shoujia.zhangshangxiu.entity.ManageInfo;
import com.shoujia.zhangshangxiu.entity.OrderCarInfo;
import com.shoujia.zhangshangxiu.entity.PaigongInfo;
import com.shoujia.zhangshangxiu.entity.PeijianBean;
import com.shoujia.zhangshangxiu.entity.ProjectBean;
import com.shoujia.zhangshangxiu.entity.RepairInfo;
import com.shoujia.zhangshangxiu.http.HttpClient;
import com.shoujia.zhangshangxiu.http.IGetDataListener;
import com.shoujia.zhangshangxiu.manager.adapter.ManagerLinggongAdapter;
import com.shoujia.zhangshangxiu.order.PeijianSelectActivity;
import com.shoujia.zhangshangxiu.order.ProjectJiesuanActivity;
import com.shoujia.zhangshangxiu.order.ProjectOrderActivity;
import com.shoujia.zhangshangxiu.order.ProjectPaigongActivity;
import com.shoujia.zhangshangxiu.order.adapter.PeijianOrderProAdapter;
import com.shoujia.zhangshangxiu.order.adapter.ProjectOrderProAdapter;
import com.shoujia.zhangshangxiu.project.ProjectActivity;
import com.shoujia.zhangshangxiu.project.ProjectSelectActivity;
import com.shoujia.zhangshangxiu.support.InfoSupport;
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
public class MangerLinggongActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "ProjectOrderActivity";

    ManagerLinggongAdapter  mAdapter;
    private SharePreferenceManager sp;
    private ListView listview;
    private List<ManageInfo> manageInfos;
    private LinearLayout select_all;
    private TextView tv_linggong,tv_hr,tv_jr,tv_tg,tv_sgwb,tv_qxjy,tv_go_tender,tv_jytg,tv_fg;
    private String mStates;
    private int mCurIndex;
    LinearLayout first_page,second_page,third_page,fourth_page;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_manager_linggong);
        initView();
        getLinggongData();
    }

    @Override
    protected void updateUIThread(int msgInt) {
        super.updateUIThread(msgInt);
        if(msgInt==100){
            /*List<ManageInfo> infos = new ArrayList<>();
            for(ManageInfo info:manageInfos){
                if(info.getStates().equals(mStates)){
                    infos.add(info);
                }
            }*/

            //manageInfos.addAll(infos);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        manageInfos = new ArrayList<>();
        sp = new SharePreferenceManager(this);
        new NavSupport(this, 17);
        listview = findViewById(R.id.listview);
        select_all = findViewById(R.id.select_all);
        tv_linggong = findViewById(R.id.tv_linggong);

        first_page = findViewById(R.id.first_page);
        third_page = findViewById(R.id.third_page);
        second_page = findViewById(R.id.second_page);
        fourth_page = findViewById(R.id.fourth_page);
        tv_hr = findViewById(R.id.tv_hr);//tv_jr,tv_tg,tv_sgwb
        tv_jr = findViewById(R.id.tv_jr);
        tv_tg = findViewById(R.id.tv_tg);
        tv_sgwb = findViewById(R.id.tv_sgwb);
        tv_qxjy = findViewById(R.id.tv_qxjy);
        tv_jytg = findViewById(R.id.tv_jytg);
        tv_go_tender = findViewById(R.id.tv_go_tender);
        tv_fg = findViewById(R.id.tv_fg);
        mAdapter = new ManagerLinggongAdapter(this,manageInfos);
        listview.setAdapter(mAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                boolean isChecked = manageInfos.get(i).isChecked();
                manageInfos.get(i).setChecked(!isChecked);
                mAdapter.notifyDataSetChanged();
            }
        });
        select_all.setOnClickListener(this);
        tv_linggong.setOnClickListener(this);
        tv_hr.setOnClickListener(this);
        tv_jr.setOnClickListener(this);
        tv_tg.setOnClickListener(this);
        tv_sgwb.setOnClickListener(this);
        tv_qxjy.setOnClickListener(this);
        tv_go_tender.setOnClickListener(this);
        tv_jytg.setOnClickListener(this);
        tv_fg.setOnClickListener(this);
        mStates = getIntent().getStringExtra("state");
        mCurIndex = getIntent().getIntExtra("curIndex",0);
        setViewVisible();
    }

    private void setViewVisible(){
        View[] views = {first_page,second_page,third_page,fourth_page};
        for(int i=0;i<4;i++){
            if(i==mCurIndex) {
                views[i].setVisibility(View.VISIBLE);
            }else{
                views[i].setVisibility(View.GONE);
            }
        }
    }

    private void getLinggongData(){
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_down_repair_project_schedule");
        dataMap.put("jsd_id", sp.getString(Constance.JSD_ID));
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Log.d("onSuccess--json", json);
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                    manageInfos.clear();
                    JSONArray dataArray = (JSONArray) resMap.get("data");
                    List<ManageInfo> projectBeans = JSONArray.parseArray(dataArray.toJSONString(), ManageInfo.class);
                    manageInfos.addAll(projectBeans);
                    mHandler.sendEmptyMessage(100);
                } else {
                    if(resMap.get("msg")!=null) {
                        toastMsg = (String) resMap.get("msg");
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }else{
                        toastMsg = "网络异常，删除失败";
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

    private void lingGong(){


        String  xhStr="";
        for(ManageInfo info:manageInfos){
           if(info.isChecked()){
               xhStr+=info.getXh()+",";
           }
        }
        if(TextUtils.isEmpty(xhStr)){
            toastMsg = "您还未选择";
            mHandler.sendEmptyMessage(TOAST_MSG);
            return;
        }
        if(xhStr.endsWith(",")){
            xhStr = xhStr.substring(0,xhStr.length()-1);
        }

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_update_jsdmx_xlxm_xlg");
        dataMap.put("jsd_id", sp.getString(Constance.JSD_ID));
        dataMap.put("xh_list", xhStr);
        dataMap.put("assign", sp.getString(Constance.CHINESE_NAME));
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Log.d("onSuccess--json", json);
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                    toastMsg = "领工成功";
                    mHandler.sendEmptyMessage(TOAST_MSG);
                    manageInfos.clear();
                    getLinggongData();
                } else {
                    if(resMap.get("msg")!=null) {
                        toastMsg = (String) resMap.get("msg");
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }else{
                        toastMsg = "网络异常，删除失败";
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_all:
                selectAll();
                break;
            case R.id.tv_linggong:
                lingGong();
                break;
            case R.id.tv_hr:
                huanRen();
                break;
                case R.id.tv_jr:
                jiaRen();
                break;     
                case R.id.tv_tg:
                tuiGong();
                break;   
                case R.id.tv_sgwb:
                finishWork();
                break;
                case R.id.tv_qxjy:
                cancleJianYan();
                break;
                case R.id.tv_go_tender:
                toOrder();
                break;  
                case R.id.tv_jytg:
                crossJianYan();
                break;
                case R.id.tv_fg:
                fanGong();
                break;
            default:

                break;
        }
    }

    private void fanGong() {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_update_repair_list_state");
        dataMap.put("jsd_id", sp.getString(Constance.JSD_ID));
        dataMap.put("states", "修理中");
        dataMap.put("xm_state", "修理中");
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Log.d("onSuccess--json", json);
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                    toastMsg = "已返工";
                    mHandler.sendEmptyMessage(TOAST_MSG);
                    manageInfos.clear();
                    getLinggongData();
                } else {
                    if(resMap.get("msg")!=null) {
                        toastMsg = (String) resMap.get("msg");
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }else{
                        toastMsg = "网络异常，返工失败";
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

    private void crossJianYan() {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_update_repair_list_state");
        dataMap.put("jsd_id", sp.getString(Constance.JSD_ID));
        dataMap.put("states", "");
        dataMap.put("xm_state", "已完工");
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Log.d("onSuccess--json", json);
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                    toastMsg = "检验通过";
                    mHandler.sendEmptyMessage(TOAST_MSG);
                    manageInfos.clear();
                    getLinggongData();
                } else {
                    if(resMap.get("msg")!=null) {
                        toastMsg = (String) resMap.get("msg");
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }else{
                        toastMsg = "网络异常，检验失败";
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

    private void toOrder() {
        startActivity(new Intent(this,ProjectOrderActivity.class));
    }

    private void cancleJianYan() {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_update_repair_list_state");
        dataMap.put("jsd_id", sp.getString(Constance.JSD_ID));
        dataMap.put("states", "修理中");
        dataMap.put("xm_state", "待质检");
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Log.d("onSuccess--json", json);
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                    toastMsg = "取消检验成功";
                    mHandler.sendEmptyMessage(TOAST_MSG);
                    manageInfos.clear();
                    getLinggongData();
                } else {
                    if(resMap.get("msg")!=null) {
                        toastMsg = (String) resMap.get("msg");
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }else{
                        toastMsg = "网络异常，取消检验失败";
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

    private void finishWork() {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_update_repair_list_state");
        dataMap.put("jsd_id", sp.getString(Constance.JSD_ID));
        dataMap.put("states", "已完工");
        dataMap.put("xm_state", "已完工");
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Log.d("onSuccess--json", json);
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                    toastMsg = "领工成功";
                    mHandler.sendEmptyMessage(TOAST_MSG);
                    manageInfos.clear();
                    getLinggongData();
                } else {
                    if(resMap.get("msg")!=null) {
                        toastMsg = (String) resMap.get("msg");
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }else{
                        toastMsg = "网络异常，删除失败";
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

    private void tuiGong() {
        String  xhStr="";
        for(ManageInfo info:manageInfos){
            if(info.isChecked()){
                xhStr+=info.getXh()+",";
            }
        }
        if(TextUtils.isEmpty(xhStr)){
            toastMsg = "您还未选择";
            mHandler.sendEmptyMessage(TOAST_MSG);
            return;
        }
        if(xhStr.endsWith(",")){
            xhStr = xhStr.substring(0,xhStr.length()-1);
        }

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_update_jsdmx_xlxm_xlg");
        dataMap.put("jsd_id", sp.getString(Constance.JSD_ID));
        dataMap.put("xh_list", xhStr);
        dataMap.put("assign", "");
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Log.d("onSuccess--json", json);
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                    toastMsg = "退工成功";
                    mHandler.sendEmptyMessage(TOAST_MSG);
                    manageInfos.clear();
                    getLinggongData();
                } else {
                    if(resMap.get("msg")!=null) {
                        toastMsg = (String) resMap.get("msg");
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }else{
                        toastMsg = "网络异常，删除失败";
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
    String  xhStr="";
    private void huanRen() {
        xhStr="";
        for(ManageInfo info:manageInfos){
            if(info.isChecked()){
                xhStr+=info.getXh()+",";
            }
        }
        if(TextUtils.isEmpty(xhStr)){
            toastMsg = "您还未选择";
            mHandler.sendEmptyMessage(TOAST_MSG);
            return;
        }

        ProjectPaigongDialog dialog = new ProjectPaigongDialog(this);
        dialog.setOnClickListener(new ProjectPaigongDialog.OnClickListener() {
            @Override
            public void rightBtnClick(List<RepairInfo> repairInfos) {
                String choosePersonStr = "";
                if(repairInfos!=null&&repairInfos.size()>0){
                    for(RepairInfo repairInfo:repairInfos){
                        if(repairInfo.isSelected()){
                            choosePersonStr+=repairInfo.getXlg()+",";
                        }
                    }
                    if(choosePersonStr.endsWith(",")){
                        choosePersonStr = choosePersonStr.substring(0,choosePersonStr.length()-1);
                    }

                    if(xhStr.endsWith(",")){
                        xhStr = xhStr.substring(0,choosePersonStr.length()-1);
                    }

                    realReplaceMan(xhStr,choosePersonStr);
                }
            }
        });

        dialog.show();
        dialog.setTitle("换人");
    }



    private void realReplaceMan(String xhStr,String choosePerson){
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_update_jsdmx_xlxm_xlg");
        dataMap.put("jsd_id", sp.getString(Constance.JSD_ID));
        dataMap.put("xh_list", xhStr);
        dataMap.put("assign", choosePerson);
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Log.d("onSuccess--json", json);
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                    toastMsg = "换人成功";
                    mHandler.sendEmptyMessage(TOAST_MSG);
                    manageInfos.clear();
                    getLinggongData();
                } else {
                    if(resMap.get("msg")!=null) {
                        toastMsg = (String) resMap.get("msg");
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }else{
                        toastMsg = "网络异常，换人失败";
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
    private void jiaRen() {
        xhStr="";
        for(ManageInfo info:manageInfos){
            if(info.isChecked()){
                xhStr+=info.getXh()+",";
            }
        }
        if(TextUtils.isEmpty(xhStr)){
            toastMsg = "您还未选择";
            mHandler.sendEmptyMessage(TOAST_MSG);
            return;
        }

        ProjectPaigongDialog dialog = new ProjectPaigongDialog(this);
        dialog.setOnClickListener(new ProjectPaigongDialog.OnClickListener() {
            @Override
            public void rightBtnClick(List<RepairInfo> repairInfos) {
                String choosePersonStr = "";
                if(repairInfos!=null&&repairInfos.size()>0){
                    for(RepairInfo repairInfo:repairInfos){
                        if(repairInfo.isSelected()){
                            choosePersonStr+=repairInfo.getXlg()+",";
                        }
                    }
                    if(choosePersonStr.endsWith(",")){
                        choosePersonStr = choosePersonStr.substring(0,choosePersonStr.length()-1);
                    }

                    if(xhStr.endsWith(",")){
                        xhStr = xhStr.substring(0,xhStr.length()-1);
                    }

                    realAddMan(xhStr,choosePersonStr);
                }
            }
        });

        dialog.show();
        dialog.setTitle("加人");

    }


    private void realAddMan(String xhStr,String choosePerson){
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_update_jsdmx_xlxm_xlg");
        dataMap.put("jsd_id", sp.getString(Constance.JSD_ID));
        dataMap.put("xh_list", xhStr);
        dataMap.put("assign", choosePerson);
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Log.d("onSuccess--json", json);
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                    toastMsg = "加人成功";
                    mHandler.sendEmptyMessage(TOAST_MSG);
                    manageInfos.clear();
                    getLinggongData();
                } else {
                    if(resMap.get("msg")!=null) {
                        toastMsg = (String) resMap.get("msg");
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }else{
                        toastMsg = "网络异常，加人失败";
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

    private void selectAll() {
       String tag = (String) select_all.getTag();
       for(ManageInfo info:manageInfos){
           if(tag.equals("0")){
               select_all.setTag("1");
               info.setChecked(true);
           }else{
               select_all.setTag("0");
               info.setChecked(false);
           }
       }
       mAdapter.notifyDataSetChanged();
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
