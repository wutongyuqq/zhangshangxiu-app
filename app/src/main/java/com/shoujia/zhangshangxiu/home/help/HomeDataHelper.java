package com.shoujia.zhangshangxiu.home.help;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.shoujia.zhangshangxiu.base.BaseHelper;
import com.shoujia.zhangshangxiu.db.DBManager;
import com.shoujia.zhangshangxiu.entity.CarInfo;
import com.shoujia.zhangshangxiu.entity.CareInfo;
import com.shoujia.zhangshangxiu.entity.FirstIconInfo;
import com.shoujia.zhangshangxiu.entity.PartsBean;
import com.shoujia.zhangshangxiu.entity.RepairInfo;
import com.shoujia.zhangshangxiu.entity.SecondIconInfo;
import com.shoujia.zhangshangxiu.http.HttpClient;
import com.shoujia.zhangshangxiu.http.IGetDataListener;
import com.shoujia.zhangshangxiu.login.PhoneLoginActivity;
import com.shoujia.zhangshangxiu.project.ProjectActivity;
import com.shoujia.zhangshangxiu.project.ProjectSelectActivity;
import com.shoujia.zhangshangxiu.util.Constance;
import com.shoujia.zhangshangxiu.util.DateUtil;
import com.shoujia.zhangshangxiu.util.SharePreferenceManager;
import com.shoujia.zhangshangxiu.util.Util;
import com.shoujia.zhangshangxiu.web.SecondActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeDataHelper extends BaseHelper {

    private Context mActivity;
    private String  previous_xh1 = "0";
    private String  previous_xh2 = "0";
    private SharePreferenceManager sp;

    private List<SecondIconInfo> secondIconInfos;


    public HomeDataHelper(Activity activity){
        super(activity);
        this.mActivity = activity;
        sp = new SharePreferenceManager(mActivity);

        secondIconInfos = new ArrayList<>();
    }

    public HomeDataHelper(Context context){
        super(context);
        this.mActivity = context;
        sp = new SharePreferenceManager(mActivity);
        secondIconInfos = new ArrayList<>();
    }

    //获取车辆数据
    public void getCardList(final InsertDataListener listener){
        if(previous_xh1.equals("end")){
            /*DBManager db = DBManager.getInstanse(getActivity());
            db.insertListData(carInfoList);*/
            listener.onSuccess();
            return;
        }
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_down_plate_number");
        dataMap.put("company_code", sp.getString(Constance.COMP_CODE));
        dataMap.put("previous_xh", previous_xh1);
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                previous_xh1 = (String) resMap.get("Previous_xh");
                if ( state!=null&&"ok".equals(state)) {
                    JSONArray dataArray = (JSONArray) resMap.get("data");
                    List<CarInfo> dataList = JSONArray.parseArray(dataArray.toJSONString(),CarInfo.class);
                    if(dataList!=null&&dataList.size()>0){
                        DBManager.getInstanse(getActivity()).insertListData(dataList);
                        //carInfoList.addAll(dataList);
                    }

                }else{
                    previous_xh1="end";
                }
                getCardList(listener);
            }

            //{"state":"ok","data":[{"mc":"鄂J15805","cz":"方江山","mobile":"15997375334","phone":"","vipnumber":""
            // ,"customer_id":"A20101483","linkman":"","custom5":"","cx":"福瑞迪","cjhm":"LJDGAA223A0111979","fdjhm":"AW132300","ns_date":"","openid":""},{"mc":"鄂AAM830","cz":"A","mobile":"","phone":"","vipnumber":"","customer_id":"A20101484","linkman":"","custom5":"","cx":"桑","cjhm":"","fdjhm":"","ns_date":"","openid":""},{"mc":"苏A8A188","cz":"章","mobile":"18762086808","phone":"","vipnumber":"","customer_id":"A20101485","linkman":"","custom5":"","cx":"99新秀","cjhm":"","fdjhm":"","ns_date":"","openid":""},{"mc":"浙B9010F","cz":"A","mobile":"","phone":"","vipnumber":"","customer_id":"A20101486","linkman":"","custom5":"","cx":"","cjhm":"","fdjhm":"","ns_date":"","openid":""},{"mc":"鄂JA7259","cz":"洪","mobile":"8819027","phone":"","vipnumber":"","customer_id":"A20101487","linkman":"","custom5":"","cx":"S460","cjhm":"LS4AAB3R8AA514571","fdjhm":"A7BB019176","ns_date":"","openid":""},{"mc":"鄂JA8187","cz":"A","mobile":"13871983939","phone":"","vipnumber":"","customer_id":"A20101490","linkman":"","custom5":"","cx":"长安1026","cjhm":"","fdjhm":"","ns_date":"","openid":""},{"mc":"鄂JB333警","cz":"邹","mobile":"15171326877","phone":"","vipnumber":"","customer_id":"A20101488","linkman":"","custom5":"","cx":"狮跑","cjhm":"","fdjhm":"","ns_date":"","openid":""},{"mc":"鄂JA7938","cz":"孙军","mobile":"13636029205","phone":"","vipnumber":"","customer_id":"A20101489","linkman":"","custom5":"","cx":"丰田","cjhm":"","fdjhm":"","ns_date":"","openid":""},{"mc":"鄂JHA188","cz":"朱","mobile":"13469927504","phone":"","vipnumber":"","customer_id":"A20101491","linkman":"","custom5":"","cx":"海马","cjhm":"","fdjhm":"","ns_date":"","openid":""},{"mc":"鲁HED697","cz":"王","mobile":"13409691088","phone":"","vipnumber":"","customer_id":"A20101492","linkman":"","custom5":"","cx":"福克斯","cjhm":"","fdjhm":"","ns_date":"","openid":""},{"mc":"鄂J06156","cz":"张","mobile":"13972737817","phone":"","vipnumber":"","customer_id":"A20101493","linkman":"","custom5":"","cx":"风行","cjhm":"","fdjhm":"","ns_date":"","openid":""},{"mc":"鄂J08097","cz":"陈","mobile":"18971717011","phone":"","vipnumber":"","customer_id":"A20101494","linkman":"","custom5":"","cx":"别克","cjhm":"","fdjhm":"","ns_date":"","openid":""},{"mc":"鄂JA6180","cz":"A","mobile":"13871950861","phone":"","vipnumber":"","customer_id":"A20101495","linkman":"","custom5":"","cx":"本田","cjhm":"","fdjhm":"","ns_date":"","openid":""},{"mc":"鄂J售前无牌","cz":"A","mobile":"","phone":"","vipnumber":"","customer_id":"A20101496","linkman":"","custom5":"","cx":"F3","cjhm":"","fdjhm":"","ns_date":"","openid":""},{"mc":"鄂J0A289","cz":"A","mobile":"","phone":"","vipnumber":"","customer_id":"A20101497","linkman":"","custom5":"","cx":"广本","cjhm":"","fdjhm":"","ns_date":"","openid":""},{"mc":"鄂J00610","cz":"A","mobile":"","phone":"","vipnumber":"","customer_id":"A20101498","linkman":"","custom5":"","cx":"领驭","cjhm":"","fdjhm":"","ns_date":"","openid":""},{"mc":"湘E2LN68","cz":"李","mobile":"13871996962","phone":"13871996962","vipnumber":"","customer_id":"A20101499","linkman":"","custom5":"","cx":"长安S460","cjhm":"","fdjhm":"","ns_date":"","openid":""},{"mc":"鄂J16167","cz":"A","mobile":"","phone":"","vipnumber":"","customer_id":"A20101500","linkman":"","custom5":"","cx":"F3","cjhm":"","fdjhm":"","ns_date":"","openid":""},{"mc":"鄂J16017","cz":"余","mobile":"13995945315","phone":"","vipnumber":"","customer_id":"A20101501","linkman":"","custom5":"","cx":"奇瑞","cjhm":"","fdjhm":"","ns_date":"","openid":""},{"mc":"鄂AU0A22","cz":"周松","mobile":"13971197980","phone":"","vipnumber":"","customer_id":"A20101502","linkman":"","custom5":"","cx":"620","cjhm":"LLV2A3A22A0215047","fdjhm":"Y100200140","ns_date":"","openid":""},
            // {"mc":"鄂HAK536","cz":"A","mobile":"","phone":"","vipnumber":"","customer_id":"A20101503","linkman":"","custom5":"",
            // "cx":"大众","cjhm":"","fdjhm":"","ns_date":"","openid":""},{"mc"

            @Override
            public void onFail() {
                listener.onFail();
            }
        });
    }

    //获取员工信息数据
    public void getPersonRepairList(final InsertDataListener listener){
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_down_repairman");
        dataMap.put("company_code", sp.getString(Constance.COMP_CODE));
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ( state!=null&&"ok".equals(state)) {
                    JSONArray dataArray = (JSONArray) resMap.get("data");
                    List<RepairInfo> dataList = JSONArray.parseArray(dataArray.toJSONString(),RepairInfo.class);
                    DBManager.getInstanse(getActivity()).insertRepairListData(dataList);
                    if(listener!=null) {
                        listener.onSuccess();
                    }
                }else{
                    listener.onFail();
                }
            }
            @Override
            public void onFail() {
                if(listener!=null) {
                    listener.onFail();
                }
            }
        });
    }


    //获取员工信息数据
    public void getPersonRepairList(final UpdateDataListener listener){
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_down_repairman");
        dataMap.put("company_code", sp.getString(Constance.COMP_CODE));
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");

                if ( state!=null&&"ok".equals(state)) {
                    JSONArray dataArray = (JSONArray) resMap.get("data");
                    List<RepairInfo> dataList = JSONArray.parseArray(dataArray.toJSONString(),RepairInfo.class);
                    DBManager dbManager = DBManager.getInstanse(mActivity);
                    dbManager.insertRepairListData(dataList);
                    listener.onSuccess();
                } else {
                   // ionicToast.show("更新项目库数据失败："+data.msg, "middle",false, 2000);
                    toastMsg = resMap.get("msg")!=null?"更新项目库数据失败："+(String) resMap.get("msg"):"更新项目库数据失败：网络异常";
                    mHandler.sendEmptyMessage(TOAST_MSG);
                }
            }
            @Override
            public void onFail() {
                toastMsg = "网络异常";
                mHandler.sendEmptyMessage(TOAST_MSG);

            }
        });
    }



    //获取第一页数据
    public void getFirstIconList(final InsertDataListener listener){
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_down_maintenance_category");
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");

                if (state!=null&& "ok".equals(state)) {
                    JSONArray dataArray = (JSONArray) resMap.get("data");
                    List<FirstIconInfo> dataList = JSONArray.parseArray(dataArray.toJSONString(),FirstIconInfo.class);
                    DBManager.getInstanse(getActivity()).insertFirstIconListData(dataList);
                    listener.onSuccess();
                }else{
                    listener.onFail();
                }
            }
            @Override
            public void onFail() {
                listener.onFail();
            }
        });
    }
//获取第一页数据
    public void getFirstIconList(final UpdateDataListener listener){
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_down_maintenance_category");
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");

                if ( state!=null&&"ok".equals(state)) {
                    JSONArray dataArray = (JSONArray) resMap.get("data");
                    List<FirstIconInfo> dataList = JSONArray.parseArray(dataArray.toJSONString(),FirstIconInfo.class);
                    DBManager.getInstanse(getActivity()).insertFirstIconListData(dataList);

                    listener.onSuccess();
                }else{
                    toastMsg = resMap.get("msg")!=null?(String) resMap.get("msg"):"网络异常";
                    mHandler.sendEmptyMessage(TOAST_MSG);
                }
            }
            @Override
            public void onFail() {
                toastMsg = "网络异常";
                mHandler.sendEmptyMessage(TOAST_MSG);

            }
        });
    }



    //获取二级页面数据
    public void getSecondIconList(){
        if(previous_xh2.equals("end")){
            DBManager.getInstanse(getActivity()).close();
            return;
        }
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_down_maintenance_project");
        dataMap.put("previous_xh", previous_xh2);
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");

                if ( state!=null&&"ok".equals(state)) {
                    previous_xh2 = (String) resMap.get("Previous_xh");
                    JSONArray dataArray = (JSONArray) resMap.get("data");
                    List<SecondIconInfo> dataList = JSONArray.parseArray(dataArray.toJSONString(),SecondIconInfo.class);
                    DBManager.getInstanse(getActivity()).insertSecondIconListData(dataList);
                }else{
                    previous_xh2 = "end";
                }
                getSecondIconList();
            }

            @Override
            public void onFail() {
                DBManager.getInstanse(getActivity()).close();
            }
        });
    }


    //获取二级页面数据
    public void getSecondIconHomeList(final IGetDataListener listener){

        if(previous_xh2.equals("end")){

           // db.insertSecondIconListData(secondIconInfos);
            listener.onSuccess("");
            return;
        }
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_down_maintenance_project");
        dataMap.put("previous_xh", previous_xh2);
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ( state!=null&&"ok".equals(state)) {
                    previous_xh2 = (String) resMap.get("Previous_xh");
                    JSONArray dataArray = (JSONArray) resMap.get("data");
                    List<SecondIconInfo> dataList = JSONArray.parseArray(dataArray.toJSONString(),SecondIconInfo.class);
                    DBManager.getInstanse(getActivity()).insertSecondIconListData(dataList);
                }else{
                    previous_xh2 = "end";
                }
                getSecondIconHomeList(listener);
            }

            @Override
            public void onFail() {
                listener.onFail();
            }
        });
    }

    //获取二级页面数据
    public void getSecondIconList(final UpdateDataListener listener){
        if(previous_xh2.equals("end")){
         //   DBManager db = DBManager.getInstanse(getActivity());
          //  db.insertSecondIconListData(secondIconInfos);
            listener.onSuccess();
            return;
        }

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_down_maintenance_project");
        dataMap.put("previous_xh", previous_xh2);
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ( state!=null&&"ok".equals(state)) {
                    previous_xh2 = (String) resMap.get("Previous_xh");
                    JSONArray dataArray = (JSONArray) resMap.get("data");
                    List<SecondIconInfo> dataList = JSONArray.parseArray(dataArray.toJSONString(),SecondIconInfo.class);
                    //secondIconInfos.addAll(dataList);
                    //DBManager db = DBManager.getInstanse(getActivity());
                    DBManager.getInstanse(getActivity()).insertSecondIconListData(dataList);
                }else{
                    previous_xh2 = "end";
                }
                getSecondIconList(listener);
            }

            @Override
            public void onFail() {

            }
        });
    }


    public void uploadOldCar(final CarInfo carInfo,final GetDataListener listener){
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_update_customer_info");
        dataMap.put("plate_number", carInfo.getMc());
        dataMap.put("cz", carInfo.getCz());
        dataMap.put("mobile", carInfo.getMobile());
        dataMap.put("phone", carInfo.getPhone());
        dataMap.put("linkman", carInfo.getLinkman());
        dataMap.put("custom5", carInfo.getCustom5());
        dataMap.put("cx", carInfo.getCx());
        dataMap.put("cjhm", carInfo.getCjhm());
        dataMap.put("fdjhm", carInfo.getFdjhm());
        dataMap.put("customer_id", carInfo.getCustomer_id());

        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");

                if ( "ok".equals(state)) {
                    DBManager dbManager = DBManager.getInstanse(mActivity);
                    dbManager.insertOrUpdateCarInfo(carInfo);
                    sp.putString(Constance.CURRENTCP,carInfo.getMc());
                    sp.putString(Constance.CURRENTCZ,carInfo.getCz());
                    checkHasNotComplete(carInfo,listener);
                }else{
                    toastMsg = resMap.get("msg")!=null?(String) resMap.get("msg"):"网络异常";
                    mHandler.sendEmptyMessage(TOAST_MSG);
                }
            }
            @Override
            public void onFail() {
                toastMsg ="网络异常";
                mHandler.sendEmptyMessage(TOAST_MSG);
            }
        });
    }

    public void uploadNewCar(final CarInfo carInfo,final GetDataListener listener) {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_upload_customer_info");
        dataMap.put("plate_number", carInfo.getMc());
        dataMap.put("cz", carInfo.getCz());
        dataMap.put("mobile", carInfo.getMobile());
        dataMap.put("phone", carInfo.getPhone());
        dataMap.put("linkman", carInfo.getLinkman());
        dataMap.put("custom5", carInfo.getCustom5());
        dataMap.put("cx", carInfo.getCx());
        dataMap.put("cjhm", carInfo.getCjhm());
        dataMap.put("fdjhm", carInfo.getFdjhm());
        dataMap.put("oprater_code", sp.getString(Constance.USERNAME));
        dataMap.put("company_code", sp.getString(Constance.COMP_CODE));
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");

                if ( state!=null&&"ok".equals(state)) {
                    String customer_id = (String) resMap.get("customer_id");
                    carInfo.setCustomer_id(customer_id);
                    DBManager dbManager = DBManager.getInstanse(mActivity);
                    dbManager.insertOrUpdateCarInfo(carInfo);
                    sp.putString(Constance.CURRENTCP,carInfo.getMc());
                    sp.putString(Constance.CURRENTCZ,carInfo.getCz());
                    checkHasNotComplete(carInfo,listener);
                }else{
                    toastMsg = (String) resMap.get("msg");
                    mHandler.sendEmptyMessage(TOAST_MSG);
                }
            }
            @Override
            public void onFail() {
                toastMsg ="网络连接异常";
                mHandler.sendEmptyMessage(TOAST_MSG);
            }
        });
    }

    private void checkHasNotComplete(final CarInfo carInfo,final GetDataListener listener){
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_check_repair_list_cp");
        dataMap.put("customer_id", carInfo.getCustomer_id());

        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");

                if ( "ok".equals(state)) {
                    String jsd_id = (String) resMap.get("jsd_id");
                    String jcr =  (String) resMap.get("jcr");
                    String jc_date =  (String) resMap.get("jc_date");
                    carInfo.setJsd_id(jsd_id);
                    DBManager dbManager = DBManager.getInstanse(mActivity);
                    dbManager.insertOrUpdateCarInfo(carInfo);
                    //车辆已进厂未完工
                    listener.getData(GetDataListener.TYPE_UN_IN,jsd_id,jcr,jc_date,carInfo.getCustomer_id());

                }else{
                    //车辆未进厂,
                    insertCarInfo(carInfo);
                }
            }
            @Override
            public void onFail() {
                toastMsg ="网络连接异常";
                mHandler.sendEmptyMessage(TOAST_MSG);
            }
        });

    }

    //4  5-8：生成接车单。
    private void  insertCarInfo(final CarInfo carInfo){
        String ywg_date = DateUtil.getCurDate();
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_upload_repair_list_main");
        dataMap.put("company_code", sp.getString(Constance.COMP_CODE));
        dataMap.put("plate_number", carInfo.getMc());
        dataMap.put("cz", carInfo.getCz());
        dataMap.put("mobile", carInfo.getMobile());
        dataMap.put("phone", carInfo.getPhone());
        dataMap.put("linkman", carInfo.getLinkman());
        dataMap.put("custom5", carInfo.getCustom5());
        dataMap.put("cx", carInfo.getCx());
        dataMap.put("cjhm", carInfo.getCjhm());
        dataMap.put("fdjhm", carInfo.getFdjhm());
        dataMap.put("ns_date", ywg_date);
        dataMap.put("xllb", "");
        dataMap.put("jclc", carInfo.getGls());
        dataMap.put("ywg_date", ywg_date);
        dataMap.put("keys_no", carInfo.getKeys_no());
        dataMap.put("memo", carInfo.getMemo());
        dataMap.put("customer_id", carInfo.getCustomer_id());
        dataMap.put("oprater_code", sp.getString(Constance.USERNAME));
        dataMap.put("jsd_id", "");
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");

                if ( state!=null&&"ok".equals(state)) {
                    String jsd_id = (String) resMap.get("jsd_id");
                    carInfo.setJsd_id(jsd_id);
                    sp.putString(Constance.JSD_ID,jsd_id);
                    sp.putString(Constance.CUSTOMER_ID,carInfo.getCustomer_id());

                    if(!TextUtils.isEmpty(carInfo.getGzms())){
                        //上传故障描述
                        uploadGuzhang(carInfo);
                    }else{
                        //跳转至下一页
                        mActivity.startActivity(new Intent(mActivity,ProjectActivity.class));
                    }
                }else{
                    toastMsg = (String) resMap.get("msg");
                    mHandler.sendEmptyMessage(TOAST_MSG);
                }
            }
            @Override
            public void onFail() {
                toastMsg ="网络连接异常";
                mHandler.sendEmptyMessage(TOAST_MSG);
            }
        });

        }

        private void  uploadGuzhang(CarInfo carInfo) {//故障上传
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("db", sp.getString(Constance.Data_Source_name));
            dataMap.put("function", "sp_fun_update_fault_info");
            dataMap.put("customer_id", carInfo.getCustomer_id());
            dataMap.put("car_fault", carInfo.getGzms());
            dataMap.put("days", DateUtil.getCurDate());

            HttpClient client = new HttpClient();
            client.post(Util.getUrl(), dataMap, new IGetDataListener() {
                @Override
                public void onSuccess(String json) {
                    System.out.println("11111");
                    Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                    String state = (String) resMap.get("state");

                    if ( state!=null&&"ok".equals(state)) {

                            //跳转至下一页
                        mActivity.startActivity(new Intent(mActivity,ProjectActivity.class));
                    }else{
                        toastMsg = (String) resMap.get("msg");
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }
                }
                @Override
                public void onFail() {
                    toastMsg ="网络连接异常";
                    mHandler.sendEmptyMessage(TOAST_MSG);
                }
            });
    }

    public void getGuzhuInfo(){

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_get_wxgzh_account");
        dataMap.put("company_code", sp.getString(Constance.COMP_CODE));

        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");

                if ( "ok".equals(state)) {
                    JSONArray dataArray = (JSONArray) resMap.get("data");
                    List<CareInfo> dataList = JSONArray.parseArray(dataArray.toJSONString(),CareInfo.class);

                    if(dataList!=null&&dataList.size()>0){
                        CareInfo dataItem = dataList.get(0);
                        String strAppid = dataItem.getAppid();
                        String strAppSecret = dataItem.getAppSecret();
                        Intent intent = new Intent(mActivity,SecondActivity.class);
                        intent.putExtra("id", strAppid);
                        intent.putExtra("aSet",strAppSecret);
                        intent.putExtra("nonce", "access_token");
                        intent.putExtra("usercode", sp.getString(Constance.COMP_CODE));
                        mActivity.startActivity(intent);
                    }
                }else{
                    toastMsg = (String) resMap.get("msg");
                    mHandler.sendEmptyMessage(TOAST_MSG);
                }
            }
            @Override
            public void onFail() {
                toastMsg ="网络连接异常";
                mHandler.sendEmptyMessage(TOAST_MSG);
            }
        });
    }

    //获取第一页数据
    public void getPartsList(final UpdateDataListener listener){
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_down_stock");
        dataMap.put("comp_code", sp.getString(Constance.COMP_CODE));
        dataMap.put("pjbm", "");
        dataMap.put("cd", "");
        dataMap.put("ck", "");

        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");

                if ( "ok".equals(state)) {
                    JSONArray dataArray = (JSONArray) resMap.get("data");
                    List<PartsBean> dataList = JSONArray.parseArray(dataArray.toJSONString(),PartsBean.class);
                    DBManager dbManager = DBManager.getInstanse(mActivity);
                    dbManager.insertPartsListData(dataList);
                    listener.onSuccess();
                }else{
                    toastMsg = resMap.get("msg")!=null?"更新配件数据失败"+(String) resMap.get("msg"):"更新配件数据失败：网络异常";
                    mHandler.sendEmptyMessage(TOAST_MSG);
                }
            }
            @Override
            public void onFail() {
                toastMsg = "网络异常";
                mHandler.sendEmptyMessage(TOAST_MSG);

            }
        });
    }


    //获取第一页数据
    public void loginOut(final InsertDataListener dataListener){
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_user_logout");
        dataMap.put("operater_code", sp.getString(Constance.USERNAME));
        dataMap.put("operater_ip", Util.getIpAddress(mActivity));


        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ( "true".equals(state)) {
                    sp.putBoolean(Constance.HASLOGIN,false);

                }else{
                    toastMsg = resMap.get("msg")!=null?"退出失败："+(String) resMap.get("msg"):"退出失败：网络异常";
                    mHandler.sendEmptyMessage(TOAST_MSG);
                }
                dataListener.onSuccess();
            }
            @Override
            public void onFail() {
                toastMsg = "网络异常";
                mHandler.sendEmptyMessage(TOAST_MSG);
                dataListener.onFail();
            }
        });
    }



    private Context getActivity(){
        return mActivity;
    }

    public interface GetDataListener{
        public static final int TYPE_UN_IN = 1;//车辆已进厂未完工
        void getData(int resType,String jsd_id,String jcr,String jc_date,String customer_id);
    }


    public interface UpdateDataListener{
        void onSuccess();
    }

    public interface InsertDataListener{
        void onSuccess();
        void onFail();
    }


}
