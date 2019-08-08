package com.shoujia.zhangshangxiu.order;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseActivity;
import com.shoujia.zhangshangxiu.entity.MoneyBean;
import com.shoujia.zhangshangxiu.http.HttpClient;
import com.shoujia.zhangshangxiu.http.IGetDataListener;
import com.shoujia.zhangshangxiu.order.adapter.BankListAdapter;
import com.shoujia.zhangshangxiu.order.entity.RateBean;
import com.shoujia.zhangshangxiu.order.entity.ShouyinBean;
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
public class ProjectShouyinActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "ProjectOrderActivity";

    public static String totalZk;
    private SharePreferenceManager sp;
    List<RateBean> mRateBeans;
    String vipcard_money="0";
    TextView bank_name,bank_rate,query_last_money;
    TextView weifenpeiNum;
    EditText xianjinNum,shuakaNum,zhuanzhangNum,vip_card,kcVipMoney,guazhangNum,weixinNum,zfbNum,yskDkNum,kcYcb;
    TextView  xianjinNumBtn, shuakaNumBtn,zhuanzhangNumBtn,guazhangNumBtn,weixinNumBtn,zfbNumBtn;
    String vipcard_no="";
    ShouyinBean mShouyinBean;
    TextView zfbView;
    TextView wxView;
    TextView shouyin;
    TextView zongyingshou;
    private String mShuakaStr="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pro_shouyin);
        initView();
        initData();
     
    }

    private void initData() {
        getRateInfo();
        getRealShouyinData();
    }

    private void initView() {
        sp = new SharePreferenceManager(this);
        bank_name = findViewById(R.id.bank_name);
        bank_rate = findViewById(R.id.bank_rate);
        query_last_money = findViewById(R.id.query_last_money);

        vip_card=findViewById(R.id.vip_card);
        kcVipMoney=findViewById(R.id.kcVipMoney);

        xianjinNum=findViewById(R.id.xianjinNum);
        shuakaNum=findViewById(R.id.shuakaNum);
        zhuanzhangNum=findViewById(R.id.zhuanzhangNum);
        guazhangNum=findViewById(R.id.guazhangNum);
        weixinNum=findViewById(R.id.weixinNum);
        zfbNum=findViewById(R.id.zfbNum);

        yskDkNum=findViewById(R.id.yskDkNum);
        kcYcb=findViewById(R.id.kcYcb);
        xianjinNumBtn=findViewById(R.id.xianjinNumBtn);
        shuakaNumBtn=findViewById(R.id.shuakaNumBtn);
        zhuanzhangNumBtn=findViewById(R.id.zhuanzhangNumBtn);
        guazhangNumBtn=findViewById(R.id.guazhangNumBtn);
        weixinNumBtn=findViewById(R.id.weixinNumBtn);
        zfbNumBtn=findViewById(R.id.zfbNumBtn);
        zfbView = findViewById(R.id.zfb_rate_view);
        wxView = findViewById(R.id.wx_rate_view);
        shouyin = findViewById(R.id.shouyin);
        zongyingshou = findViewById(R.id.zongyingshou);

        shouyin.setOnClickListener(this);
        xianjinNumBtn.setOnClickListener(this);
        shuakaNumBtn.setOnClickListener(this);
        zhuanzhangNumBtn.setOnClickListener(this);
        guazhangNumBtn.setOnClickListener(this);
        weixinNumBtn.setOnClickListener(this);
        zfbNumBtn.setOnClickListener(this);

        xianjinNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    getWfpMoney();
                }
            }
        });
        shuakaNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    getWfpMoney();
                }
            }
        });
        zhuanzhangNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    getWfpMoney();
                }
            }
        });
        guazhangNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    getWfpMoney();
                }
            }
        });
        weixinNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    getWfpMoney();
                }
            }
        });
        zfbNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    getWfpMoney();
                }
            }
        });


         weifenpeiNum = findViewById(R.id.weifenpeiNum);
        new NavSupport(this, 14);
        new InfoSupport(this);
        bank_name.setOnClickListener(this);
        query_last_money.setOnClickListener(this);

    }

    @Override
    protected void updateUIThread(int msgInt) {
        super.updateUIThread(msgInt);
        if(msgInt==100){
            updateView();
        }else if(msgInt==101){
           TextView last_money = findViewById(R.id.last_money);
            LinearLayout last_money_lay = findViewById(R.id.last_money_lay);
            last_money_lay.setVisibility(View.VISIBLE);
           last_money.setText(vipcard_money);
        }else if(msgInt == 109){
            TextView pre_payment = findViewById(R.id.pre_payment);
            pre_payment.setText("预收款余额："+mShouyinBean.Pre_payment);
            weifenpeiNum.setText("未分配："+mShouyinBean.Pre_payment);
            zongyingshou.setText("应收总计："+mShouyinBean.zje);
        }
    }

    private void updateView() {
        if(mRateBeans!=null&&mRateBeans.size()>0){
            for(int i=0;i<mRateBeans.size();i++){
                RateBean bean=mRateBeans.get(i);
                if(i==0){
                    bank_name.setText(bean.setup1);
                    bank_rate.setText(bean.setup2);
                    mShuakaStr = bean.setup1;
                }else if(bean.name.contains("支付宝")){

                    zfbView.setText(bean.setup2);
                }else if(bean.name.contains("微信")){

                    wxView.setText(bean.setup2);
                }
            }
        }
    }

    private void getRateInfo(){

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_down_poundage");
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
                    mRateBeans = JSONArray.parseArray(dataArray.toJSONString(), RateBean.class);
                    mHandler.sendEmptyMessage(100);
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

    private void showAllMoney(){

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_get_vipcard_money");
        dataMap.put("vipcard_no", vipcard_no);
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Log.d("onSuccess--json", json);
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                    vipcard_money = (String) resMap.get("vipcard_money");
                    mHandler.sendEmptyMessage(101);
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


    private void getRealShouyinData() {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_get_settle_accounts_info");
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
                    List<ShouyinBean> projectBeans = JSONArray.parseArray(dataArray.toJSONString(), ShouyinBean.class);
                    if(projectBeans!=null&&projectBeans.size()>0){ ;
                        mShouyinBean = projectBeans.get(0);
                    }
                    mHandler.sendEmptyMessage(109);
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

    private void receiveMoney(){


        float zfbFl = 0;
        if(zfbView.getText()!=null && !TextUtils.isEmpty(zfbView.getText().toString())){
            zfbFl = Float.parseFloat(zfbView.getText().toString());
        }

        float wxFl = 0;
        if(wxView.getText()!=null && !TextUtils.isEmpty(wxView.getText().toString())){
            wxFl = Float.parseFloat(wxView.getText().toString());
        }
        float yhkFl = 0;
        if(bank_rate.getText()!=null && !TextUtils.isEmpty(bank_rate.getText().toString())){
            yhkFl = Float.parseFloat(bank_rate.getText().toString());
        }


        float xianjinNumFloat = 0;
        float shuakaNumFloat = 0;
        float zhuanzhangNumFloat = 0;
        float guazhangNumFloat = 0;
        float weixinNumFloat = 0;
        float zfbNumFloat = 0;
        float kcVipMoneyFloat = 0;




        if(xianjinNum.getText()!=null&&!TextUtils.isEmpty(xianjinNum.getText().toString())){
            xianjinNumFloat = Float.parseFloat(xianjinNum.getText().toString());
        }
        if(shuakaNum.getText()!=null&&!TextUtils.isEmpty(shuakaNum.getText().toString())){
            shuakaNumFloat = Float.parseFloat(shuakaNum.getText().toString());
        }

        if(zhuanzhangNum.getText()!=null&&!TextUtils.isEmpty(zhuanzhangNum.getText().toString())){
            zhuanzhangNumFloat = Float.parseFloat(zhuanzhangNum.getText().toString());
        }
        if(guazhangNum.getText()!=null&&!TextUtils.isEmpty(guazhangNum.getText().toString())){
            guazhangNumFloat = Float.parseFloat(guazhangNum.getText().toString());
        }
        if(weixinNum.getText()!=null&&!TextUtils.isEmpty(weixinNum.getText().toString())){
            weixinNumFloat = Float.parseFloat(weixinNum.getText().toString());
        }

        if(zfbNum.getText()!=null&&!TextUtils.isEmpty(zfbNum.getText().toString())){
            zfbNumFloat = Float.parseFloat(zfbNum.getText().toString());
        }

        if(kcVipMoney.getText()!=null&&!TextUtils.isEmpty(kcVipMoney.getText().toString())){
            kcVipMoneyFloat = Float.parseFloat(kcVipMoney.getText().toString());
        }

        float yhje = 0;

        if(TextUtils.isEmpty(totalZk)){
            yhje = Float.parseFloat(totalZk);
        }

        float yskDkNumFloat = 0;


        if(yskDkNum.getText()!=null&&!TextUtils.isEmpty(yskDkNum.getText().toString())){
            yskDkNumFloat = Float.parseFloat(yskDkNum.getText().toString());
        }

        float kcYcbFloat = 0;


        if(kcYcb.getText()!=null&&!TextUtils.isEmpty(kcYcb.getText().toString())){
            kcYcbFloat = Float.parseFloat(kcYcb.getText().toString());
        }


        //付款方式数组
        String[] moneyDescArr = {"消费卡","现金","刷卡","转账","挂账","微信","支付宝","预收款","养车币"};
        //付款金额数组
        float[] moneyArr ={kcVipMoneyFloat,xianjinNumFloat,shuakaNumFloat,zhuanzhangNumFloat,guazhangNumFloat,weixinNumFloat,zfbNumFloat,yskDkNumFloat,kcYcbFloat};
        //付款金额对应手续费
        float[] moneySxf = {0,0,shuakaNumFloat*yhkFl,0,0,weixinNumFloat*wxFl,zfbNumFloat*zfbFl,0,0};
        //结算数组初始化
        List<MoneyBean> newMoneyArr = new ArrayList();

        float moneyTotal = 0;
        float sxfTotal = 0;
        for(int i=0;i<moneyArr.length;i++){
            float moneyNum = moneyArr[i];
            if(moneyNum>0) {//如果金额大于0，则填充到结算数组中
                MoneyBean moneyBean = new MoneyBean();
                moneyBean.money = moneyNum;
                moneyBean.sxf = moneySxf[i];
                moneyBean.moneyDesc = moneyDescArr[i];
                if(moneyDescArr[i].equals("刷卡")){//如果付款方式是刷卡，则更换为具体的银行名称
                    moneyBean.moneyDesc = TextUtils.isEmpty(mShuakaStr)?"":mShuakaStr;
                }

                moneyTotal+=moneyArr[i];//计算各项付款方式金额的总和
                sxfTotal+=moneySxf[i];//计算各项手续费总和
                newMoneyArr.add(moneyBean);//将对象push到结算数组中
            }
        }
        float ysje = Float.parseFloat(mShouyinBean.getZje());
        //超过三种方式，提示重新选择
        if(newMoneyArr.size()>3){
            Toast.makeText(this,"结算方式超过3种，请重新选择",  Toast.LENGTH_SHORT).show();
            return;
        }else if(newMoneyArr.size()==0){//如果没有填写金额，提示重新选择

            Toast.makeText(this,"您还未填写付款金额",  Toast.LENGTH_SHORT).show();
            return;
        }
        if(moneyTotal<(ysje-yhje)){//如果金额不正确，提示重新选择
            Toast.makeText(this,"您填写的金额不正确",  Toast.LENGTH_SHORT).show();
            return;
        }
        String skfs="";
        float ssje=0;
        float sxf=0;
        String skfs1 = "";
        float skje1 = 0;
        float sxf1 = 0;
        String skfs2 = "";
        float skje2 = 0;
        float sxf2 = 0;
        if(newMoneyArr!=null && newMoneyArr.size()>0){
            //如果是三种付款方式，则按先后顺序赋值
            if(newMoneyArr.size()>2){
                skfs = newMoneyArr.get(0).moneyDesc;
                ysje = newMoneyArr.get(0).money;
                ssje = newMoneyArr.get(0).money - yhje - newMoneyArr.get(0).sxf;
                sxf = newMoneyArr.get(0).sxf;

                skfs1= newMoneyArr.get(1).moneyDesc;
                skje1 =  newMoneyArr.get(1).money;
                sxf1= newMoneyArr.get(1).sxf;

                skfs2= newMoneyArr.get(2).moneyDesc;
                skje2 = newMoneyArr.get(2).money;
                sxf2=newMoneyArr.get(2).sxf;

            }else if(newMoneyArr.size()==2){ //如果是两种付款方式，则按先后顺序赋值，第三个赋值为0
                skfs = newMoneyArr.get(0).moneyDesc;
                ysje = newMoneyArr.get(0).money;
                ssje = newMoneyArr.get(0).money - yhje - newMoneyArr.get(0).sxf;
                sxf = newMoneyArr.get(0).sxf;

                skfs1= newMoneyArr.get(1).moneyDesc;
                skje1 =  newMoneyArr.get(1).money;
                sxf1= newMoneyArr.get(1).sxf;

                skfs2= "";
                skje2 =  0;
                sxf2=0;
            }else if(newMoneyArr.size()==1){ //如果是两种付款方式，则按先后顺序赋值，第三个赋值为0
                skfs =  newMoneyArr.get(0).moneyDesc;
                ysje =  newMoneyArr.get(0).money;
                ssje =  newMoneyArr.get(0).money - yhje -  newMoneyArr.get(0).sxf;
                sxf =  newMoneyArr.get(0).sxf;
                skfs1= "";
                skje1 =  0;
                sxf1=0;
                skfs2="";
                skje2 =  0;
                sxf2=0;
            }else if(newMoneyArr.size()==0){
                Toast.makeText(this,"请未填写付款方式或付款金额", Toast.LENGTH_SHORT).show();
                return;
            }
        }



        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_upload_receivables_data");
        dataMap.put("company_code", sp.getString(Constance.COMP_CODE));
        dataMap.put("customer_id", sp.getString(Constance.CUSTOMER_ID));
        dataMap.put("jsd_id", sp.getString(Constance.JSD_ID));
        dataMap.put("czy", sp.getString(Constance.USERNAME));
        dataMap.put("ysje", mShouyinBean.zje);
        dataMap.put("yhje",totalZk);
        dataMap.put("sxf", Util.getDoubleStr(sxf+""));
        dataMap.put("ssje",  Util.getDoubleStr(ssje+""));
        dataMap.put("skfs", skfs+"");
        dataMap.put("bit_compute", mShouyinBean.bit_compute);
        dataMap.put("bit_use", mShouyinBean.bit_amount);
        dataMap.put("skfs1", skfs1);
        dataMap.put("skje1", skje1+"");
        dataMap.put("sxf1",  Util.getDoubleStr(sxf1+""));
        dataMap.put("skfs2", skfs2);
        dataMap.put("skje2", skje2+"");
        dataMap.put("sxf2",  Util.getDoubleStr(sxf2+""));
        dataMap.put("pre_payment", mShouyinBean.Pre_payment);
        dataMap.put("vipcard_no", vipcard_no);
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Log.d("onSuccess--json", json);
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ("ok".equals(state)) {
                    toastMsg = "提交成功";
                    mHandler.sendEmptyMessage(TOAST_MSG);
                    startActivity(new Intent(ProjectShouyinActivity.this,ProjectOrderActivity.class));
                    finish();
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

    private void xianjinPay(){
        setMoneyPay(xianjinNum);
    }

    private void shuakaPay(){
        setMoneyPay(shuakaNum);
    }

    private void zhuanzhangPay(){
        setMoneyPay(zhuanzhangNum);
    }

    private void guazhangPay(){
        setMoneyPay(guazhangNum);
    }

    private void weixinPay(){
        setMoneyPay(weixinNum);
    }
    private void zfbPay(){
        setMoneyPay(zfbNum);
    }


    private void setMoneyPay(EditText editText){

        float xianjinNumFloat = 0;
        float shuakaNumFloat = 0;
        float zhuanzhangNumFloat = 0;
        float guazhangNumFloat = 0;
        float weixinNumFloat = 0;
        float zfbNumFloat = 0;
        if(xianjinNum.getText()!=null&&!TextUtils.isEmpty(xianjinNum.getText().toString())){
            xianjinNumFloat = Float.parseFloat(xianjinNum.getText().toString());
        }
        if(shuakaNum.getText()!=null&&!TextUtils.isEmpty(shuakaNum.getText().toString())){
            shuakaNumFloat = Float.parseFloat(shuakaNum.getText().toString());
        }

        if(zhuanzhangNum.getText()!=null&&!TextUtils.isEmpty(zhuanzhangNum.getText().toString())){
            zhuanzhangNumFloat = Float.parseFloat(zhuanzhangNum.getText().toString());
        }
        if(guazhangNum.getText()!=null&&!TextUtils.isEmpty(guazhangNum.getText().toString())){
            guazhangNumFloat = Float.parseFloat(guazhangNum.getText().toString());
        }
        if(weixinNum.getText()!=null&&!TextUtils.isEmpty(weixinNum.getText().toString())){
            weixinNumFloat = Float.parseFloat(weixinNum.getText().toString());
        }

        if(zfbNum.getText()!=null&&!TextUtils.isEmpty(zfbNum.getText().toString())){
            zfbNumFloat = Float.parseFloat(zfbNum.getText().toString());
        }

        float otherFloat = xianjinNumFloat + shuakaNumFloat + zhuanzhangNumFloat + guazhangNumFloat + weixinNumFloat + zfbNumFloat;
        float ysjeFloat = Float.parseFloat(mShouyinBean.getZje());
        float shengYuTotal = ysjeFloat - otherFloat;
        if(shengYuTotal<0){
            shengYuTotal = 0;
        }
        editText.setText(shengYuTotal+"");
        weifenpeiNum.setText("未分配：0");
    }

    private void getWfpMoney() {

        float xianjinNumFloat = 0;
        float shuakaNumFloat = 0;
        float zhuanzhangNumFloat = 0;
        float guazhangNumFloat = 0;
        float weixinNumFloat = 0;
        float zfbNumFloat = 0;
        if(xianjinNum.getText()!=null&&!TextUtils.isEmpty(xianjinNum.getText().toString())){
            xianjinNumFloat = Float.parseFloat(xianjinNum.getText().toString());
        }
        if(shuakaNum.getText()!=null&&!TextUtils.isEmpty(shuakaNum.getText().toString())){
            shuakaNumFloat = Float.parseFloat(shuakaNum.getText().toString());
        }

        if(zhuanzhangNum.getText()!=null&&!TextUtils.isEmpty(zhuanzhangNum.getText().toString())){
            zhuanzhangNumFloat = Float.parseFloat(zhuanzhangNum.getText().toString());
        }
        if(guazhangNum.getText()!=null&&!TextUtils.isEmpty(guazhangNum.getText().toString())){
            guazhangNumFloat = Float.parseFloat(guazhangNum.getText().toString());
        }
        if(weixinNum.getText()!=null&&!TextUtils.isEmpty(weixinNum.getText().toString())){
            weixinNumFloat = Float.parseFloat(weixinNum.getText().toString());
        }

        if(zfbNum.getText()!=null&&!TextUtils.isEmpty(zfbNum.getText().toString())){
            zfbNumFloat = Float.parseFloat(zfbNum.getText().toString());
        }

        float otherFloat = xianjinNumFloat + shuakaNumFloat + zhuanzhangNumFloat + guazhangNumFloat + weixinNumFloat + zfbNumFloat;
        float ysjeFloat = Float.parseFloat(mShouyinBean.getZje());
        float shengYuTotal = ysjeFloat - otherFloat;
        if(shengYuTotal<0){
            shengYuTotal = 0;
        }
        weifenpeiNum.setText("未分配："+shengYuTotal);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bank_name:
                showBankList();
                break;
            case R.id.query_last_money:
                EditText vip_card = findViewById(R.id.vip_card);
                if(vip_card.getText()!=null&&TextUtils.isEmpty(vip_card.getText().toString().trim())){
                    vipcard_no = vip_card.getText().toString().trim();
                    showAllMoney();
                }else{
                    toastMsg="您还没有输入卡号";
                    mHandler.sendEmptyMessage(TOAST_MSG);
                }
                break;
            case R.id.xianjinNumBtn:
                xianjinPay();
                break;
            case R.id.shuakaNumBtn:
                shuakaPay();
                break;
            case R.id.zhuanzhangNumBtn:
                zhuanzhangPay();
                break;
            case R.id.guazhangNumBtn:
                guazhangPay();
                break;
            case R.id.weixinNumBtn:
                weixinPay();
                break;
            case R.id.zfbNumBtn:
                zfbPay();
                break;
                case R.id.shouyin:
                receiveMoney();
                break;
            default:

                break;
        }
    }

    private void showBankList() {


            // 用于PopupWindow的View
            View contentView=LayoutInflater.from(this).inflate(R.layout.popwindow_bank_rate, null, false);
           ListView mListView = contentView.findViewById(R.id.listview);
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        final PopupWindow  mPopupWindow=new PopupWindow(contentView, Util.dp2px(this,200), Util.dp2px(this,200), false);
        mPopupWindow.showAsDropDown(bank_name);
        BankListAdapter homeCarInfoAdapter = new BankListAdapter(this,mRateBeans);//新建并配置ArrayAapeter
            mListView.setAdapter(homeCarInfoAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    mPopupWindow.dismiss();
                    RateBean info = mRateBeans.get(position);
                    bank_name.setText(info.setup1);
                    bank_rate.setText(info.setup2);
                }
            });

             // 设置PopupWindow的背景
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            // 设置PopupWindow是否能响应外部点击事件
            mPopupWindow.setOutsideTouchable(true);
            // 设置PopupWindow是否能响应点击事件
            mPopupWindow.setTouchable(true);
            // 显示PopupWindow，其中：
            // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移

            // 或者也可以调用此方法显示PopupWindow，其中：
            // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
            // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
            // window.showAtLocation(parent, gravity, x, y);

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
