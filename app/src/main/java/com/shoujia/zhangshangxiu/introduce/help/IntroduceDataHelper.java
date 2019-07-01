package com.shoujia.zhangshangxiu.introduce.help;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.shoujia.zhangshangxiu.base.BaseHelper;
import com.shoujia.zhangshangxiu.entity.IntroduceInfo;
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

public class IntroduceDataHelper extends BaseHelper {

    private Activity mActivity;

    private SharePreferenceManager sp;




    public IntroduceDataHelper(Activity activity){
        super(activity);
        this.mActivity = activity;
        sp = new SharePreferenceManager(mActivity);

    }

    //获取车辆数据
    public void getData(final GetDataListener listener){

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_get_general_situation");
        dataMap.put("company_code", sp.getString(Constance.COMP_CODE));

        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                String state = (String) resMap.get("state");
                if ( "ok".equals(state)) {
                    JSONArray dataArray = (JSONArray) resMap.get("data");
                    List<IntroduceInfo> dataList = JSONArray.parseArray(dataArray.toJSONString(),IntroduceInfo.class);
                    if(dataList!=null&&dataList.size()>0){
                        listener.getData(dataList.get(0));
                    }
                }else{

                }
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
        void getData(IntroduceInfo info);
    }
}
