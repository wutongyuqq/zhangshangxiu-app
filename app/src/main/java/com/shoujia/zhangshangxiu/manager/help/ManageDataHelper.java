package com.shoujia.zhangshangxiu.manager.help;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.shoujia.zhangshangxiu.base.BaseHelper;
import com.shoujia.zhangshangxiu.entity.ManageInfo;
import com.shoujia.zhangshangxiu.http.HttpClient;
import com.shoujia.zhangshangxiu.http.IGetDataListener;
import com.shoujia.zhangshangxiu.util.Constance;
import com.shoujia.zhangshangxiu.util.SharePreferenceManager;
import com.shoujia.zhangshangxiu.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageDataHelper extends BaseHelper {

    private Activity mActivity;

    private SharePreferenceManager sp;
    private String pre_row_number = "0";
    private  List<ManageInfo> manageInfos;



    public ManageDataHelper(Activity activity){
        super(activity);
        this.mActivity = activity;
        sp = new SharePreferenceManager(mActivity);
        manageInfos = new ArrayList<>();

    }

    public void setPreZero(){
        pre_row_number = "0";
    }
    //获取车辆数据
    public void getListData(final int index, final GetDataListener listener){
        String[] strArr = {"待领工","修理中","待质检","已完工"};
        if(pre_row_number.equals("0")){
            if(manageInfos!=null) {
                manageInfos.clear();
            }
        }
       if(pre_row_number.equals("end")){
           //manageInfos;
           listener.getData(manageInfos);
            return;
        }
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_down_repair_project_state");
        dataMap.put("company_code", sp.getString(Constance.COMP_CODE));
        dataMap.put("pre_row_number", pre_row_number);
        if(index>3){
            dataMap.put("states", strArr[3]);
        }else if(index<0){
            dataMap.put("states", strArr[0]);
        }else{
            dataMap.put("states", strArr[index]);
        }
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");

                if ( "ok".equals(state)) {
                    pre_row_number = (String) resMap.get("pre_row_number");
                    JSONArray dataArray = (JSONArray) resMap.get("data");
                    List<ManageInfo> dataList = JSONArray.parseArray(dataArray.toJSONString(),ManageInfo.class);
                    manageInfos.addAll(dataList);
                }else{
                    pre_row_number = "end";
                }
                getListData(index,listener);
            }

            @Override
            public void onFail() {

            }
        });
    }

    private Activity getActivity(){
        return mActivity;
    }

    public interface GetDataListener{
        void getData(List<ManageInfo> manageInfos);
    }
}
