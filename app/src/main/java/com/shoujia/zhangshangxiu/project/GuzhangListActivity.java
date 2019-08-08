package com.shoujia.zhangshangxiu.project;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseActivity;
import com.shoujia.zhangshangxiu.dialog.GuzhangEditDialog;
import com.shoujia.zhangshangxiu.dialog.ProjectCancleDialog;
import com.shoujia.zhangshangxiu.dialog.ProjectEditDialog;
import com.shoujia.zhangshangxiu.entity.GuzhangInfo;
import com.shoujia.zhangshangxiu.entity.PartsBean;
import com.shoujia.zhangshangxiu.history.HistoryActivity;
import com.shoujia.zhangshangxiu.http.HttpClient;
import com.shoujia.zhangshangxiu.http.IGetDataListener;
import com.shoujia.zhangshangxiu.order.ProjectOrderActivity;
import com.shoujia.zhangshangxiu.project.adater.GuzhangListAdapter;
import com.shoujia.zhangshangxiu.project.help.ProjectDataHelper;
import com.shoujia.zhangshangxiu.support.InfoSupport;
import com.shoujia.zhangshangxiu.support.NavSupport;
import com.shoujia.zhangshangxiu.util.Constance;
import com.shoujia.zhangshangxiu.util.DateUtil;
import com.shoujia.zhangshangxiu.util.SharePreferenceManager;
import com.shoujia.zhangshangxiu.util.Util;
import com.shoujia.zhangshangxiu.view.CustomDatePicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/23 0023.
 * 首页
 */
public class GuzhangListActivity extends BaseActivity implements View.OnClickListener{
	private final String TAG = "GuzhangListActivity";
	private LinearLayout car_history,cancle_reciver,project_select,order_total_num;
    ListView listview;
    LinearLayout add;
    List<GuzhangInfo> mDataList = new ArrayList<>();
    private SharePreferenceManager sp;
    private GuzhangListAdapter mAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guzhang);
        initView();
		initData();

	}

    private void initView() {
        sp = new SharePreferenceManager(this);
        listview = findViewById(R.id.gz_listview);
        View footView = View.inflate(this,R.layout.view_add_gz,null);
        add = footView.findViewById(R.id.add);

        mAdapter = new GuzhangListAdapter(this,mDataList);
        listview.setAdapter(mAdapter);
        listview.addFooterView(footView);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GuzhangEditDialog dialog = new GuzhangEditDialog(GuzhangListActivity.this);
                dialog.show();
                dialog.setOnClickListener(new GuzhangEditDialog.OnClickListener() {
                    @Override
                    public void rightBtnClick(String numStr) {
                        if(TextUtils.isEmpty(numStr)||TextUtils.isEmpty(numStr.trim())){
                            toastMsg = "您还没填写故障信息，请重新添加故障信息";
                            mHandler.sendEmptyMessage(TOAST_MSG);
                        }else {
                            insertGuzhangData(numStr);
                        }
                    }
                });
            }
        });
    }

    private void insertGuzhangData(final String car_fault){
        String jc_date =  sp.getString(Constance.JIECHEDATE);
        if(jc_date!=null&&jc_date.length()<=10){
            jc_date = jc_date+" 00:00:00";
        }
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_update_fault_info");
        dataMap.put("customer_id", sp.getString(Constance.CUSTOMER_ID));
        dataMap.put("days",jc_date);
        dataMap.put("car_fault", car_fault);

        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {

                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                if(resMap!=null&&resMap.get("state")!=null){
                    String state = (String) resMap.get("state");
                    if (state.equals("ok")) {
                        sp.putString(Constance.GUZHNAGMIAOSHU,car_fault);
                        getCuzhangList();
                        //Toast.makeText(ProjectActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    }else{
                        toastMsg = "服务器异常";
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }
                }

            }

            @Override
            public void onFail() {
                toastMsg = "服务器异常";
                mHandler.sendEmptyMessage(TOAST_MSG);
            }
        });

    }

    @Override
    protected void updateUIThread(int msgInt) {
        super.updateUIThread(msgInt);
        if(msgInt==100) {
            mAdapter.notifyDataSetChanged();
        }
    }

    //获取车辆数据
    public void getCuzhangList(){
        mDataList.clear();

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_get_fault_info");
        dataMap.put("customer_id", sp.getString(Constance.CUSTOMER_ID));
        dataMap.put("days", "1901-01-01 0:00:00");

        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {

                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                if(resMap!=null&&resMap.get("state")!=null){
                    String state = (String) resMap.get("state");
                    if (state.equals("ok")) {

                        JSONArray dataArray = (JSONArray) resMap.get("data");
                        mDataList.addAll(JSONArray.parseArray(dataArray.toJSONString(),GuzhangInfo.class));
                        //mDataList.addAll(dataList);
                        mHandler.sendEmptyMessage(100);
                        //Toast.makeText(ProjectActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    }else{
                        if(resMap.get("msg")!=null) {
                            toastMsg = (String) resMap.get("msg");
                            mHandler.sendEmptyMessage(TOAST_MSG);
                        }
                    }
                }

            }

            @Override
            public void onFail() {

            }
        });
    }



    //初始化数据
	private void initData(){
        getCuzhangList();

	}

}
