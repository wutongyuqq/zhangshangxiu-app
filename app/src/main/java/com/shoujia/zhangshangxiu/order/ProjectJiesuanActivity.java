package com.shoujia.zhangshangxiu.order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseActivity;
import com.shoujia.zhangshangxiu.http.HttpClient;
import com.shoujia.zhangshangxiu.http.IGetDataListener;
import com.shoujia.zhangshangxiu.order.entity.JsBaseBean;
import com.shoujia.zhangshangxiu.order.entity.JsCompBean;
import com.shoujia.zhangshangxiu.order.entity.JsPartBean;
import com.shoujia.zhangshangxiu.order.entity.JsXmBean;
import com.shoujia.zhangshangxiu.support.InfoSupport;
import com.shoujia.zhangshangxiu.support.NavSupport;
import com.shoujia.zhangshangxiu.util.Constance;
import com.shoujia.zhangshangxiu.util.DateUtil;
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
public class ProjectJiesuanActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "ProjectOrderActivity";


    private SharePreferenceManager sp;
    private TextView start_sy,sing_print,double_print,cancle_jiesuan;
    JsBaseBean mJsBaseBean = new JsBaseBean();
    JsCompBean mJsCompBean;
    List<JsPartBean> mPartBeans = new ArrayList<>();
    List<JsXmBean> mXmBeans = new ArrayList<>();
    double totalXlf = 0;
    double totalZkMoney = 0;
    float totalPartSl = 0;
    double totalPartMoney = 0;
    double totalCb = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pro_jiesuan);
        initView();
        initData();
        loginPrintPre();
    }

    private void initData() {
        getBaseData();
        getPjDataList();
        getXmDataList();
        getCompanyData();
    }
    private void initView() {
        sp = new SharePreferenceManager(this);
        new NavSupport(this, 12);

        new InfoSupport(this);
        start_sy = findViewById(R.id.start_sy);
        sing_print = findViewById(R.id.sing_print);
        double_print = findViewById(R.id.double_print);
        cancle_jiesuan = findViewById(R.id.cancle_jiesuan);
        start_sy.setOnClickListener(this);
        sing_print.setOnClickListener(this);
        double_print.setOnClickListener(this);
        cancle_jiesuan.setOnClickListener(this);
        TextView jsd_id_view = findViewById(R.id.jsd_id);
        TextView factoy_name = findViewById(R.id.factoy_name);
        TextView dytime = findViewById(R.id.dytime);
        jsd_id_view.setText("结算单号："+sp.getString(Constance.JSD_ID));
        factoy_name.setText(sp.getString(Constance.FACTORYNAME));
        dytime.setText("打印时间："+DateUtil.getCurDate());
    }

    @Override
    protected void updateUIThread(int msgInt) {
        super.updateUIThread(msgInt);
        if(msgInt==101){
            updatePartView();
        }else if(msgInt==102){
            updateXmView();
        }else if(msgInt==100){
            if(mJsBaseBean==null){
                return;
            }
            TextView ywg_date_view = findViewById(R.id.ywg_date);
            TextView custome_name = findViewById(R.id.custome_name);
            TextView cp_name = findViewById(R.id.cp_name);
            TextView cx_name = findViewById(R.id.cx_name);
            TextView cjh_name = findViewById(R.id.cjh_name);
            TextView jclc_name = findViewById(R.id.jclc_name);
            TextView gz_name = findViewById(R.id.gz_name);
            TextView memoView = findViewById(R.id.memo);
            ywg_date_view.setText("预完工时间："+Util.getFormatDate(mJsBaseBean.ywg_date));
            custome_name.setText(mJsBaseBean.cz);
            cp_name.setText(mJsBaseBean.cp);
            cjh_name.setText(mJsBaseBean.cjhm);
            cx_name.setText(mJsBaseBean.cx);
            jclc_name.setText(mJsBaseBean.jclc);
            gz_name.setText(mJsBaseBean.car_fault);
            memoView.setText("备注："+mJsBaseBean.memo);
            mJsBaseBean.compName = sp.getString(Constance.FACTORYNAME);
            mJsBaseBean.printDate = DateUtil.getCurDate();
            mJsBaseBean.jsd_id = sp.getString(Constance.JSD_ID);

        }else if(msgInt==103){
            if(mJsCompBean==null){
                return;
            }
            TextView address = findViewById(R.id.address);
            TextView telPhone = findViewById(R.id.telPhone);
            address.setText("地址："+mJsCompBean.address);
            telPhone.setText("电话："+mJsCompBean.telphone);
        }
    }

    private void updateXmView(){

        LinearLayout part_lay = findViewById(R.id.pro_lay);
        part_lay.removeAllViews();
        if(mXmBeans==null||mXmBeans.size()==0){
            return;
        }
        for(JsXmBean bean:mXmBeans){
            View partView = View.inflate(this,R.layout.view_pro_item,null);
            String slStr = bean.xlf;
            float tmpSl = Float.parseFloat(slStr);
            totalXlf+=tmpSl;

            String moneyStr = bean.zk;
            double tmpDob = Double.parseDouble(moneyStr);
            totalZkMoney+=tmpDob;
            TextView xm_name = partView.findViewById(R.id.xm_name);
            TextView xm_money = partView.findViewById(R.id.xm_money);
            TextView xm_yh_money = partView.findViewById(R.id.xm_yh_money);
            xm_money.setText(Util.getDoubleStr(slStr));
            xm_yh_money.setText(Util.getDoubleStr(moneyStr));
            xm_name.setText(bean.xlxm);
            part_lay.addView(partView);
        }
        View totalView = View.inflate(this,R.layout.view_pro_item,null);
        TextView part_num = totalView.findViewById(R.id.xm_money);
        part_num.setText(totalXlf+"");
        TextView money_total = totalView.findViewById(R.id.xm_yh_money);
        TextView xm_name = totalView.findViewById(R.id.xm_name);
        money_total.setText(Util.getDoubleStr(totalZkMoney+""));
        xm_name.setText("小计");
        part_lay.addView(totalView);
    }


    private void updatePartView(){
        if(mPartBeans==null||mPartBeans.size()==0){
            return;
        }

        LinearLayout part_lay = findViewById(R.id.part_lay);
        part_lay.removeAllViews();
        for(JsPartBean bean:mPartBeans){
            View partView = View.inflate(this,R.layout.view_part_item,null);
            String slStr = bean.sl;
            float tmpSl = Float.parseFloat(slStr);
            totalPartSl+=tmpSl;
            String moneyStr = bean.ssj;
            double tmpDob = Double.parseDouble(moneyStr);
            totalPartMoney+=tmpDob;
            totalCb += Double.parseDouble(TextUtils.isEmpty(bean.cb)?"0":bean.cb);
            TextView part_num = partView.findViewById(R.id.part_num);
            TextView money_total = partView.findViewById(R.id.part_money_total);
            TextView part_name = partView.findViewById(R.id.part_name);
            part_num.setText(Util.getDoubleStr(slStr));
            money_total.setText(Util.getDoubleStr(moneyStr));
            part_name.setText(bean.pjmc);
            part_lay.addView(partView);
        }
        View totalView = View.inflate(this,R.layout.view_part_item,null);
        TextView part_num = totalView.findViewById(R.id.part_num);
        part_num.setText(totalPartSl+"");
        TextView money_total = totalView.findViewById(R.id.part_money_total);
        TextView part_name = totalView.findViewById(R.id.part_name);
        part_name.setText("小计");
        money_total.setText(Util.getDoubleStr(totalPartMoney+""));
        part_lay.addView(totalView);
    }

    private void getPjDataList() {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_down_jsdmx_pjclmx");
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
                    List<JsPartBean> projectBeans = JSONArray.parseArray(dataArray.toJSONString(), JsPartBean.class);
                    if(projectBeans!=null&&projectBeans.size()>0) {
                        mPartBeans.addAll(projectBeans);
                        mHandler.sendEmptyMessage(101);
                    }
                } else {
                    if(resMap.get("msg")!=null) {
                        toastMsg = (String) resMap.get("msg");
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }else{
                        toastMsg = "网络连接异常";
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


    private void getXmDataList() {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_down_jsdmx_xlxm");
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
                    List<JsXmBean> projectBeans = JSONArray.parseArray(dataArray.toJSONString(), JsXmBean.class);
                    if(projectBeans!=null&&projectBeans.size()>0) {
                        mXmBeans.addAll(projectBeans);
                        mHandler.sendEmptyMessage(102);
                    }
                } else {
                    if(resMap.get("msg")!=null) {
                        toastMsg = (String) resMap.get("msg");
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }else{
                        toastMsg = "网络连接异常";
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

    public void saveDataForLogin(String machine_code,
                                 String msign) {
        SharedPreferences shared_user_info = getSharedPreferences("user_info",
                MODE_PRIVATE);
        shared_user_info.edit().putString("Data_Source",sp.getString(Constance.FACTORYNAME)).commit();
        shared_user_info.edit().putString("machine_code", machine_code)
                .commit();
        shared_user_info.edit().putString("msign", msign).commit();

    }


    private void loginPrintPre(){
        /**
         * 登录
         */

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_check_service_validity");
        dataMap.put("data_source", sp.getString(Constance.FACTORYNAME));
        dataMap.put("operater_code", sp.getString(Constance.USERNAME));
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Log.d("onSuccess--json", json);
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("true".equals(state)) {
                    String machine_code = (String) resMap.get("machine_code");
                    String msign = (String) resMap.get("machine_key");
                    saveDataForLogin(machine_code,msign);
                } else {
                    if(resMap.get("msg")!=null) {
                        toastMsg = (String) resMap.get("msg");
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }else{
                        toastMsg = "网络连接异常";
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

    private void printData(){
        if(mJsCompBean!=null){
            mJsBaseBean.compName = mJsCompBean.company_name;
            mJsBaseBean.address = mJsCompBean.address;
            mJsBaseBean.telphone = mJsCompBean.telphone;
        }
        mJsBaseBean.totalPartMoney = totalPartMoney;
        mJsBaseBean.totalPartSl = totalPartSl;
        mJsBaseBean.totalZkMoney = totalZkMoney;
        mJsBaseBean.totalXlf = totalXlf;
        Util.print(this,mJsBaseBean,mXmBeans,mPartBeans);
    }

    private void getBaseData() {

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
                    List<JsBaseBean> projectBeans = JSONArray.parseArray(dataArray.toJSONString(), JsBaseBean.class);
                   if(projectBeans!=null&&projectBeans.size()>0) {
                       mJsBaseBean = projectBeans.get(0);
                       mHandler.sendEmptyMessage(100);
                   }
                } else {
                    if(resMap.get("msg")!=null) {
                        toastMsg = (String) resMap.get("msg");
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }else{
                        toastMsg = "网络连接异常";
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



    private void getCompanyData() {

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_get_company_info");
        dataMap.put("company_code", sp.getString(Constance.COMP_CODE));
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
                    List<JsCompBean> projectBeans = JSONArray.parseArray(dataArray.toJSONString(), JsCompBean.class);
                    if(projectBeans!=null&&projectBeans.size()>0) {
                        mJsCompBean = projectBeans.get(0);
                        mHandler.sendEmptyMessage(103);
                    }
                } else {
                    if(resMap.get("msg")!=null) {
                        toastMsg = (String) resMap.get("msg");
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }else{
                        toastMsg = "网络连接异常";
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
            case R.id.start_sy:
                uploadMoney();
               break;
                case R.id.sing_print:
                case R.id.double_print:
                    printData();
                break;
            case R.id.cancle_jiesuan:
                break;
            default:

                break;
        }
    }

    private void uploadMoney() {
        ProjectShouyinActivity.totalZk = totalZkMoney+"";
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_update_repair_main_money");
        dataMap.put("jsd_id", sp.getString(Constance.JSD_ID));
        dataMap.put("zje", Float.parseFloat(totalPartMoney+totalXlf+"")+"");
        dataMap.put("wxfzj", Float.parseFloat(totalXlf+"")+"");
        dataMap.put("clfzj", Float.parseFloat(totalPartMoney+"")+"");
        dataMap.put("totalCb", Float.parseFloat(totalCb+"")+"");
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Log.d("onSuccess--json", json);
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                    startActivity(new Intent(ProjectJiesuanActivity.this,ProjectShouyinActivity.class));

                } else {
                    if(resMap.get("msg")!=null) {
                        toastMsg = (String) resMap.get("msg");
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }else{
                        toastMsg = "网络连接异常";
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
