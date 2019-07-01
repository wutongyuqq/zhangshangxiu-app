package com.kernal.plateid.controller;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.IBinder;
import android.widget.Toast;

import com.kernal.plateid.PlateCfgParameter;
import com.kernal.plateid.PlateRecognitionParameter;
import com.kernal.plateid.RecogService;
import com.kernal.plateid.CoreSetup;

import java.io.File;

/***
 * @author user
 * 导入图像识别类
 */
public class ImportPicRecog {

    private Context context;
    private CoreSetup coreSetup;
    private int iInitPlateIDSDK;
    private RecogService.MyBinder recogBinder;
    private String[] recogResult = new String[14];
    private PlateRecognitionParameter prp = new PlateRecognitionParameter();
    private Point picPoint = new Point();
    private int nRet;

    public ImportPicRecog(Context context) {
        nRet = -2;
        iInitPlateIDSDK = -1;
        if(recogBinder == null){
            coreSetup = new CoreSetup();
            this.context = context;
            //快速、导入、拍照识别模式参数(0:快速、导入、拍照识别模式-----2:精准识别模式)
            RecogService.recogModel = 0;
            //启动识别服务
            Intent recogIntent = new Intent(context,
                    RecogService.class);
            context.bindService(recogIntent, recogConn,
                    Service.BIND_AUTO_CREATE);
        }
    }

    /**
     * 识别获取结果
     * @param recogPicPath 传入识别图像的路径
     * @return 识别结果
     */
    public String[] recogPicResults(String recogPicPath) {
        if(recogBinder != null){
            obtainPicSize(recogPicPath);
            // 图像高度
            prp.height = picPoint.y;
            // 图像宽度
            prp.width = picPoint.x;
            // 图像文件
            prp.pic = recogPicPath;
            recogResult = recogBinder.doRecogDetail(prp);
            nRet = recogBinder.getnRet();
            if (nRet != 0) {
                recogResult[0] = "错误码:"+String.valueOf(nRet)+"，请查阅开发手册寻找解决方案";
            }
            releaseService();
        }
        return recogResult;

    }

    /***
     * 释放核心
     * 连续识别不需要重复初始化、释放
     */
    private void releaseService(){
        if(recogBinder != null){
            recogBinder.UninitPlateIDSDK();
            context.unbindService(recogConn);
        }
    }

    private ServiceConnection recogConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            recogConn = null;
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            recogBinder = (RecogService.MyBinder) service;
            setPlateid();
        }
    };

    /***
     * 初始化设置识别车牌核心
     *
     */
    private void setPlateid() {
        iInitPlateIDSDK = recogBinder.getInitPlateIDSDK();
        if (iInitPlateIDSDK != 0) {
            Toast.makeText(context, "错误码：" + iInitPlateIDSDK, Toast.LENGTH_LONG).show();
        } else {
            // 导入图片方式参数
            int imageformat = 0;
            PlateCfgParameter cfgparameter = new PlateCfgParameter();
            //识别阈值(取值范围0-9, 0:最宽松的阈值, 9:最严格的阈值, 5:默认阈值)
            cfgparameter.nOCR_Th = coreSetup.nOCR_Th;
            // 定位阈值(取值范围0-9, 0:最宽松的阈值9, :最严格的阈值, 5:默认阈值)
            cfgparameter.nPlateLocate_Th = coreSetup.nPlateLocate_Th;
            // 省份顺序,例:cfgparameter.szProvince = "京津沪";最好设置三个以内，最多五个。
            cfgparameter.szProvince = coreSetup.szProvince;

            // 是否开启个性化车牌:0开启；1关闭
            cfgparameter.individual = coreSetup.individual;
            // 双层黄色车牌是否开启:开启；3关闭
            cfgparameter.tworowyellow = coreSetup.tworowyellow;
            // 单层武警车牌是否开启:4开启；5关闭
            cfgparameter.armpolice = coreSetup.armpolice;
            // 双层军队车牌是否开启:6开启；7关闭
            cfgparameter.tworowarmy = coreSetup.tworowarmy;
            // 农用车车牌是否开启:8开启；9关闭
            cfgparameter.tractor = coreSetup.tractor;
            // 使馆车牌是否开启:12开启；13关闭
            cfgparameter.embassy = coreSetup.embassy;
            // 双层武警车牌是否开启:16开启；17关闭
            cfgparameter.armpolice2 = coreSetup.armpolice2;
            //厂内车牌是否开启     18:开启  19关闭
//            cfgparameter.Infactory = coreSetup.Infactory;
            //民航车牌是否开启  20开启 21 关闭
//            cfgparameter.civilAviation = coreSetup.civilAviation;
            //领事馆车牌开启   22开启   23关闭
            cfgparameter.consulate = coreSetup.consulate;
            //新能源车牌开启  24开启  25关闭
            cfgparameter.newEnergy = coreSetup.newEnergy;

            recogBinder.setRecogArgu(cfgparameter, imageformat);
            // 图像高度
            prp.devCode = coreSetup.Devcode;
        }
    }

    /***
     * 动态获取图像的宽高
     * @param recogPicPath 识别图像路径
     */
    private void obtainPicSize(String recogPicPath) {
        File file = new File(recogPicPath);
        BitmapFactory.Options options = null;
        if (file.exists()) {
            try {
                options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(recogPicPath, options);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //目前最大支持到4096*4096的图片
            if (options != null) {
                int picMaxWH = 4096;
                if (options.outWidth <= picMaxWH && options.outHeight <= picMaxWH) {
                    picPoint.set(options.outWidth,options.outHeight);
                } else {
                    Toast.makeText(context, "读取文件错误，图片超出识别限制4096*4096",
                            Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(context, "读取文件不存在",
                    Toast.LENGTH_LONG).show();
        }
    }
}
