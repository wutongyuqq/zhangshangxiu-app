package com.shoujia.zhangshangxiu.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.shoujia.zhangshangxiu.entity.OrderCarInfo;
import com.shoujia.zhangshangxiu.entity.PaigongInfo;
import com.shoujia.zhangshangxiu.entity.PartsBean;
import com.shoujia.zhangshangxiu.entity.PeijianBean;
import com.shoujia.zhangshangxiu.entity.ProjectBean;
import com.shoujia.zhangshangxiu.entity.RepairInfo;
import com.shoujia.zhangshangxiu.http.HttpClient;
import com.shoujia.zhangshangxiu.http.IGetDataListener;
import com.shoujia.zhangshangxiu.order.adapter.PeijianOrderProAdapter;
import com.shoujia.zhangshangxiu.order.adapter.ProjectOrderPaigongAdapter;
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
public class ProjectPaigongActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "ProjectPaigongActivity";
    private SharePreferenceManager sp;
    private ListView listview;
    private ProjectOrderPaigongAdapter mAdapter;
    List<PaigongInfo> mPaigongList;
    private String choosePersonStr="";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pro_paigong);
        initView();
        initData();

    }

    private void initView() {
        sp = new SharePreferenceManager(this);
        new NavSupport(this, 13);
        new InfoSupport(this);

        listview = findViewById(R.id.listview);
        TextView tv_pg = findViewById(R.id.tv_pg);
        mPaigongList = new ArrayList<>();
        mAdapter = new ProjectOrderPaigongAdapter(this,mPaigongList);
        listview.setAdapter(mAdapter);
        tv_pg.setOnClickListener(this);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PaigongInfo info = mPaigongList.get(i);
                if(info.isChecked()) {
                    info.setChecked(false);
                    mPaigongList.set(i,info);
                }else{
                    info.setChecked(true);
                    mPaigongList.set(i,info);
                    //mPaigongList.get(i).setChecked(false);
                }
                mHandler.sendEmptyMessage(101);
            }
        });
    }

    private void initData(){
        mPaigongList.clear();
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_down_jsdmx_xlxm_assign");
        dataMap.put("jsd_id",sp.getString(Constance.JSD_ID));

        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Log.d("onSuccess--json", json);
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                        JSONArray dataArray = (JSONArray) resMap.get("data");
                        List<PaigongInfo> projectBeans = JSONArray.parseArray(dataArray.toJSONString(), PaigongInfo.class);
                        if(projectBeans!=null&&projectBeans.size()>0){
                            mPaigongList.addAll(projectBeans);
                        mHandler.sendEmptyMessage(101);
                    } else {

                    }
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

    @Override
    protected void updateUIThread(int msgInt) {
        super.updateUIThread(msgInt);
        if(msgInt==101){
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pg:
                ProjectPaigongDialog dialog = new ProjectPaigongDialog(this);
                dialog.setOnClickListener(new ProjectPaigongDialog.OnClickListener() {
                    @Override
                    public void rightBtnClick(List<RepairInfo> repairInfos) {
                        choosePersonStr = "";
                        if(repairInfos!=null&&repairInfos.size()>0){
                            for(RepairInfo repairInfo:repairInfos){
                                if(repairInfo.isSelected()){
                                    choosePersonStr+=repairInfo.getXlg()+",";
                                }
                            }
                            if(choosePersonStr.endsWith(",")){
                                choosePersonStr = choosePersonStr.substring(0,choosePersonStr.length()-1);
                            }
                            if(mPaigongList!=null&&mPaigongList.size()>0){
                                for(PaigongInfo info:mPaigongList){
                                    if(info!=null&&info.isChecked()) {
                                        toPGDataToServer(info);
                                    }
                                }
                            }

                        }
                    }
                });

                dialog.show();
                break;
            default:

                break;
        }
    }

    private void toPGDataToServer(PaigongInfo info){
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_update_jsdmx_xlxm_assign");
        dataMap.put("jsd_id",sp.getString(Constance.JSD_ID));
        dataMap.put("xh",info.getXh());
        dataMap.put("assign",choosePersonStr);

        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Log.d("onSuccess--json", json);
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                    initData();
                } else {
                    if(resMap.get("msg")!=null) {
                        toastMsg = (String) resMap.get("msg");
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }else{
                        toastMsg = "网络异常，派工失败";
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
