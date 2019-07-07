package com.shoujia.zhangshangxiu.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.shoujia.zhangshangxiu.entity.OrderCarInfo;
import com.shoujia.zhangshangxiu.entity.PeijianBean;
import com.shoujia.zhangshangxiu.entity.ProjectBean;
import com.shoujia.zhangshangxiu.http.HttpClient;
import com.shoujia.zhangshangxiu.http.IGetDataListener;
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
public class ProjectOrderActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "ProjectOrderActivity";
    private NavSupport navSupport;
    private LinearLayout car_history, cancle_reciver, project_select;
    private SharePreferenceManager sp;
    List<ProjectBean> mProjectList = new ArrayList<>();
    List<PeijianBean> mPeiJianList = new ArrayList<>();
    ProjectOrderProAdapter mOrderAdapter;
    PeijianOrderProAdapter mPeijianAdapter;
    ListView listview,listview2;
    TextView tv_pro, tv_pj,temp_pro,peijianku,project_ck,car_home_page,paigong,total_jiesuan,total_jiesuan2;
    LinearLayout pro_btn_lay,pj_btn_lay;
    String gdStatu="";
    private OrderCarInfo mOrderCarInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pro_order);
        initView();

        initData();
        getProjectListData();
        getPjListData();
        getJsdInfo();
    }

    private void initView() {
        sp = new SharePreferenceManager(this);
        navSupport = new NavSupport(this, 1);
        listview = findViewById(R.id.listview);
        listview2 = findViewById(R.id.listview2);
        temp_pro = findViewById(R.id.temp_pro);
        pro_btn_lay = findViewById(R.id.pro_btn_lay);
        pj_btn_lay = findViewById(R.id.pj_btn_lay);
        peijianku = findViewById(R.id.peijianku);
        tv_pro = findViewById(R.id.tv_pro);
        tv_pj = findViewById(R.id.tv_pj);
        project_ck = findViewById(R.id.project_ck);
        car_home_page = findViewById(R.id.car_home_page);
        paigong = findViewById(R.id.paigong);
        total_jiesuan = findViewById(R.id.total_jiesuan);
        total_jiesuan2 = findViewById(R.id.total_jiesuan2);
        tv_pro.setOnClickListener(this);
        tv_pj.setOnClickListener(this);
        temp_pro.setOnClickListener(this);
        peijianku.setOnClickListener(this);
        project_ck.setOnClickListener(this);
        car_home_page.setOnClickListener(this);
        total_jiesuan.setOnClickListener(this);
        total_jiesuan2.setOnClickListener(this);
        paigong.setOnClickListener(this);
        new InfoSupport(this);
        mOrderAdapter = new ProjectOrderProAdapter(this, mProjectList);
        mPeijianAdapter = new PeijianOrderProAdapter(this, mPeiJianList);
        listview.setAdapter(mOrderAdapter);
        listview2.setAdapter(mPeijianAdapter);
        mOrderAdapter.setDeleteClickListener(new ProjectOrderProAdapter.DeleteClickListener() {
            @Override
            public void deleteClick(int position) {
                showDeleteDialog(position);

            }
        });
        mOrderAdapter.setEditClickListener(new ProjectOrderProAdapter.EditClickListener() {
            @Override
            public void editClick(int position) {
                showEditDialog(position);
            }
        });


        mPeijianAdapter.setDeleteClickListener(new PeijianOrderProAdapter.DeleteClickListener() {
            @Override
            public void deleteClick(int position) {
                showDeleteDialog(position);

            }
        });
        mPeijianAdapter.setEditClickListener(new PeijianOrderProAdapter.EditClickListener() {
            @Override
            public void editClick(int position) {
                showEditDialog(position);
            }
        });

        View headView = View.inflate(this, R.layout.project_order_head, null);
        listview.addHeaderView(headView);

        View footView = View.inflate(this, R.layout.project_order_bottom, null);
        listview.addFooterView(footView);
    }

    private void showEditDialog(final int position) {
        OrderTempEditDialog editDialog = new OrderTempEditDialog(this,mProjectList.get(position));
        editDialog.setOnClickListener(new OrderTempEditDialog.OnClickListener() {
            @Override
            public void rightBtnClick(ProjectBean newBean) {
                saveNewPrice(newBean,position);
            }
        });
        editDialog.show();
    }


    private void saveNewPrice(final ProjectBean projectBean,final int position){

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_upload_maintenance_project_library");
        dataMap.put("xlxm", projectBean.getXlxm());
        dataMap.put("wxgz",projectBean.getWxgz());
        dataMap.put("xlf",projectBean.getXlf());
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Log.d("onSuccess--json", json);
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                    mProjectList.set(position,projectBean);
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

    private void showDeleteDialog(final int position) {
        OrderDeleteDialog dialog = new OrderDeleteDialog(this, mProjectList.get(position).getXlxm());
        dialog.setOnClickListener(new OrderDeleteDialog.OnClickListener() {
            @Override
            public void rightBtnClick() {
                deleteProData(position);
            }
        });
        dialog.show();
    }

    private void deleteProData(int position) {

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_delete_maintenance_project_detail");
        dataMap.put("jsd_id", sp.getString(Constance.JSD_ID));
        dataMap.put("xh", mProjectList.get(position).getXh());
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Log.d("onSuccess--json", json);
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                    getProjectListData();
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

    //初始化数据
    private void initData() {


    }

    @Override
    protected void updateUIThread(int msgInt) {
        super.updateUIThread(msgInt);
        if (msgInt == 100) {
            if (mProjectList != null && mProjectList.size() > 0) {
                mOrderAdapter.notifyDataSetChanged();
            }
        }else if(msgInt==101){
            if(mPeiJianList!=null&&mPeiJianList.size()>0){
                mPeijianAdapter.notifyDataSetChanged();
            }
        }
    }

    private void getJsdInfo() {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_down_repair_list_main");
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
                    JSONArray dataArray = (JSONArray) resMap.get("data");
                    List<OrderCarInfo> projectBeans = JSONArray.parseArray(dataArray.toJSONString(), OrderCarInfo.class);
                    if(projectBeans!=null&&projectBeans.size()>0){
                        mOrderCarInfo = projectBeans.get(0);
                    }
                    mHandler.sendEmptyMessage(101);
                } else {

                }
            }

            @Override
            public void onFail() {
                toastMsg = "网络连接异常";
                mHandler.sendEmptyMessage(TOAST_MSG);
            }
        });

    }

    //获取故障描述
    private void getGuzhang() {

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_update_fault_info");
        dataMap.put("customer_id", sp.getString(Constance.CUSTOMER_ID));
        dataMap.put("car_fault", mOrderCarInfo.getCar_fault());
        dataMap.put("days",mOrderCarInfo.getJc_date());
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                    JSONArray dataArray = (JSONArray) resMap.get("data");

                    List<ProjectBean> projectBeans = JSONArray.parseArray(dataArray.toJSONString(), ProjectBean.class);
                    if (projectBeans != null) {
                        mProjectList.addAll(projectBeans);
                    }
                    mHandler.sendEmptyMessage(100);
                } else {

                }
            }

            @Override
            public void onFail() {
                toastMsg = "网络连接异常";
                mHandler.sendEmptyMessage(TOAST_MSG);
            }
        });
    }

    private void getProjectListData() {
        mProjectList.clear();
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_down_jsdmx_xlxm");
        dataMap.put("jsd_id", sp.getString(Constance.JSD_ID));
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                    JSONArray dataArray = (JSONArray) resMap.get("data");

                    List<ProjectBean> projectBeans = JSONArray.parseArray(dataArray.toJSONString(), ProjectBean.class);
                    if (projectBeans != null) {
                        mProjectList.addAll(projectBeans);
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


    private void getPjListData(){
        mPeiJianList.clear();
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_down_jsdmx_pjclmx");
        dataMap.put("jsd_id", sp.getString(Constance.JSD_ID));
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                    JSONArray dataArray = (JSONArray) resMap.get("data");
                    List<PeijianBean> projectBeans = JSONArray.parseArray(dataArray.toJSONString(), PeijianBean.class);
                    if (projectBeans != null) {
                        mPeiJianList.addAll(projectBeans);
                    }
                    mHandler.sendEmptyMessage(101);
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

    private void judgeToStatu(){
            if(gdStatu.equals("派工")) {
            //去派工页面
            }else if(gdStatu.equals("全部完工")){
                Map<String, String> dataMap = new HashMap<>();
                dataMap.put("db", sp.getString(Constance.Data_Source_name));
                dataMap.put("function", "sp_fun_update_repair_list_state");
                dataMap.put("jsd_id", sp.getString(Constance.JSD_ID));
                dataMap.put("states", "审核未结算");
                dataMap.put("xm_state", "已完工");

                HttpClient client = new HttpClient();
                client.post(Util.getUrl(), dataMap, new IGetDataListener() {
                    @Override
                    public void onSuccess(String json) {
                        System.out.println("11111");
                        Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                        String state = (String) resMap.get("state");
                        if ("ok".equals(state)) {

                            mHandler.sendEmptyMessage(102);
                        } else {
                            if (resMap.get("msg") != null) {
                                toastMsg = (String) resMap.get("msg");
                                mHandler.sendEmptyMessage(TOAST_MSG);
                            } else {
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
            }else if(gdStatu.equals("取消完工")){
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
                        System.out.println("11111");
                        Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                        String state = (String) resMap.get("state");
                        if ("ok".equals(state)) {

                            mHandler.sendEmptyMessage(102);
                        } else {
                            if (resMap.get("msg") != null) {
                                toastMsg = (String) resMap.get("msg");
                                mHandler.sendEmptyMessage(TOAST_MSG);
                            } else {
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

    }

//增加临时项目
    private void addTmpServer(final ProjectBean bean){

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_upload_maintenance_project_detail");
        dataMap.put("jsd_id",sp.getString(Constance.JSD_ID));
        dataMap.put("xlxm",bean.getXlxm());
        dataMap.put("xlf",bean.getXlf());
        dataMap.put("zk","0.00");
        dataMap.put("wxgz",bean.getWxgz());
        dataMap.put("pgzje","0");
        dataMap.put("pgzgs","1");
        dataMap.put("xh","0");
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ( "ok".equals(state)) {
                    mProjectList.add(bean);
                    mHandler.sendEmptyMessage(100);
                }else{

                }
            }
            @Override
            public void onFail() {
                toastMsg ="网络连接异常";
                mHandler.sendEmptyMessage(TOAST_MSG);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pj:
                listview.setVisibility(View.GONE);
                listview2.setVisibility(View.VISIBLE);
                tv_pro.setTextColor(Color.parseColor("#666666"));
                tv_pro.setBackgroundColor(Color.parseColor("#A4A3A3"));
                tv_pj.setTextColor(Color.parseColor("#ffffff"));
                tv_pj.setBackgroundColor(Color.parseColor("#ff9db4"));
                pro_btn_lay.setVisibility(View.GONE);
                pj_btn_lay.setVisibility(View.VISIBLE);

                break;
            case R.id.tv_pro:
                listview.setVisibility(View.VISIBLE);
                listview2.setVisibility(View.GONE);
                tv_pro.setTextColor(Color.parseColor("#ffffff"));
                tv_pro.setBackgroundColor(Color.parseColor("#ff9db4"));
                tv_pj.setTextColor(Color.parseColor("#666666"));
                tv_pj.setBackgroundColor(Color.parseColor("#A4A3A3"));
                pro_btn_lay.setVisibility(View.VISIBLE);
                pj_btn_lay.setVisibility(View.GONE);
                break;
            case R.id.temp_pro:
                OrderAddTmpDialog dialog1 = new OrderAddTmpDialog(this);
                dialog1.setOnClickListener(new OrderAddTmpDialog.OnClickListener() {
                    @Override
                    public void rightBtnClick(ProjectBean newBean) {
                        mProjectList.add(newBean);
                        mHandler.sendEmptyMessage(100);
                    }
                });
                dialog1.show();
                break;
            case R.id.peijianku:
                startActivity(new Intent(ProjectOrderActivity.this,PeijianSelectActivity.class));
                break;
                case R.id.project_ck:
                startActivity(new Intent(ProjectOrderActivity.this,ProjectSelectActivity.class));
                finish();
                break;
            case R.id.car_home_page:
                startActivity(new Intent(ProjectOrderActivity.this,ProjectActivity.class));
                finish();
                break;
                case R.id.paigong:
                startActivity(new Intent(ProjectOrderActivity.this,ProjectPaigongActivity.class));
                break;
            case R.id.total_jiesuan:
            case R.id.total_jiesuan2:
                startActivity(new Intent(ProjectOrderActivity.this,ProjectJiesuanActivity.class));
                break;
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
        if(requestCode==2){
            getPjListData();
        }
    }
}
