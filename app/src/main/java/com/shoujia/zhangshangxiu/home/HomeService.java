package com.shoujia.zhangshangxiu.home;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.shoujia.zhangshangxiu.db.DBManager;
import com.shoujia.zhangshangxiu.entity.CarInfo;
import com.shoujia.zhangshangxiu.entity.FirstIconInfo;
import com.shoujia.zhangshangxiu.entity.SecondIconInfo;
import com.shoujia.zhangshangxiu.home.help.HomeDataHelper;
import com.shoujia.zhangshangxiu.http.IGetDataListener;

import java.util.List;

public class HomeService extends Service {
    private List<CarInfo> carInfoList;
    HomeDataHelper mHomeDataHelper;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         getData();
        getPersonRepairList();
        return super.onStartCommand(intent, flags, startId);
    }


    //初始化数据
    private void getData(){

            // showDialog(getContext());
        DBManager db = DBManager.getInstanse(this);
        carInfoList = db.queryAllListData();
        if(carInfoList==null||carInfoList.size()==0) {
            getHomeHelper().getCardList(new HomeDataHelper.InsertDataListener() {
                @Override
                public void onSuccess() {
                    // dismissDialog();
                    // getPersonRepairList();
                    DBManager.getInstanse(HomeService.this).close();
                }

                @Override
                public void onFail() {
                    // dismissDialog();
                    // getPersonRepairList();
                }
            });
        }

            // mHandler.sendEmptyMessageDelayed(101,10000);

    }

    private HomeDataHelper getHomeHelper(){
        if(mHomeDataHelper==null){
            mHomeDataHelper = new HomeDataHelper(this);
        }
        return mHomeDataHelper;
    }


    private void getPersonRepairList(){

        getHomeHelper().getPersonRepairList(new HomeDataHelper.InsertDataListener() {
            @Override
            public void onSuccess() {
                getFirstIconList();
            }

            @Override
            public void onFail() {
                getFirstIconList();
            }
        });
    }



    private void getFirstIconList(){
        DBManager db = DBManager.getInstanse(this);
        List<FirstIconInfo> firstIconInfos = db.queryFirstIconListData();
        if(firstIconInfos==null||firstIconInfos.size()==0){

            getHomeHelper().getFirstIconList(new HomeDataHelper.InsertDataListener() {
                @Override
                public void onSuccess() {
                    getSecondInconList();
                }

                @Override
                public void onFail() {
                    getSecondInconList();
                }
            });
        }

    }



    private void getSecondInconList(){
        DBManager db = DBManager.getInstanse(this);
        List<SecondIconInfo> secondIconInfos = db.querySecondIconListData();
        if(secondIconInfos==null||secondIconInfos.size()==0){

            getHomeHelper().getSecondIconHomeList(new IGetDataListener() {
                @Override
                public void onSuccess(String json) {
                    //dismissDialog();
                    DBManager.getInstanse(HomeService.this).close();
                }

                @Override
                public void onFail() {
                    //dismissDialog();
                    DBManager.getInstanse(HomeService.this).close();
                }
            });

        }

    }

}