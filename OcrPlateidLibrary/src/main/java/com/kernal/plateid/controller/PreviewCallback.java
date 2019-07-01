package com.kernal.plateid.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.widget.Toast;

import com.kernal.plateid.CoreSetup;
import com.kernal.plateid.PlateCfgParameter;
import com.kernal.plateid.PlateRecognitionParameter;
import com.kernal.plateid.RecogService;
import com.kernal.plateid.activity.PlateidCameraActivity;

/***
 * @author user
 * 识别获取结果接口调用类
 */

public class PreviewCallback implements Camera.PreviewCallback {
    private RecogService.MyBinder recogBinder;
    private int iInitPlateIDSDK = -2;
    /**isGetResult判断是否获取到结果*/
    private boolean isGetResult = false;
    private Activity activity;
    private int preWidth, preHeight;
    private CoreSetup coreSetup;
    private PlateRecognitionParameter prp = new PlateRecognitionParameter();
    /**rotateLeft向左横屏*/
    private boolean rotateLeft;
    /**rotateTop正向竖屏*/
    private boolean rotateTop;
    /**rotateRight向右横屏*/
    private boolean rotateRight;
    /**rotateBottom倒置竖屏*/
    private boolean rotateBottom;
    /**isTakePicOnclick判断是否点击了拍照按钮*/
    private boolean isTakePicOnclick = false;
    private int nRet = -2;
    private Runnable recogRunnable;


    public PreviewCallback(Activity activity, Point prePoint, CoreSetup coreSetup) {
        this.activity = activity;
        this.preWidth = prePoint.x;
        this.preHeight = prePoint.y;
        this.coreSetup = coreSetup;
        rotateLeft = false;
        rotateTop = true;
        rotateRight = false;
        rotateBottom = false;
        coreSetup();
    }
    private void coreSetup() {
        if (coreSetup.accurateRecog && !coreSetup.takePicMode) {
            //精准识别---[精准识别模式参数(0:快速、导入、拍照识别模式-----2:精准识别模式)]
            RecogService.recogModel = 2;
        } else {
            //快速识别、拍照识别---[快速、导入、拍照识别模式参数(0:快速、导入、拍照识别模式-----2:精准识别模式)]
            RecogService.recogModel = 0;
        }
        Intent recogIntent = new Intent(activity,
                RecogService.class);
        activity.bindService(recogIntent, recogConn, Service.BIND_AUTO_CREATE);

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

    public void screenRotationChange(boolean left, boolean right, boolean top, boolean bottom) {
        //向左旋转横屏
        rotateLeft = left;
        //正向旋转竖屏
        rotateTop = top;
        //向右旋转横屏
        rotateRight = right;
        //倒置旋转
        rotateBottom = bottom;
    }

    public void isTakePicOnclick(boolean isTakePicOnclick) {
        this.isTakePicOnclick = isTakePicOnclick;
    }

    /***
     * 初始化设置识别车牌核心
     *
     */
    private void setPlateid() {
        iInitPlateIDSDK = recogBinder.getInitPlateIDSDK();
        if (iInitPlateIDSDK != 0) {
            Toast.makeText(activity, "错误码：" + String.valueOf(iInitPlateIDSDK), Toast.LENGTH_LONG).show();
        } else {
            // 视频流NV21格式参数
            int imageformat = 6;
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

            //图像宽
            prp.width = preWidth;
            //图像高
            prp.height = preHeight;
            // 项目授权开发码
            prp.devCode = coreSetup.Devcode;
            //0:不缩放;1:缩放一倍;2:缩放两倍  一般用于较大分辨率，起码是1920*1080以上
            prp.plateIDCfg.scale = 1;
        }
    }



    @Override
    public void onPreviewFrame(final byte[] bytes, final Camera camera) {
        recogRunnable = new Runnable() {
            @Override
            public void run() {
                if (recogBinder != null && iInitPlateIDSDK == 0 && !isGetResult) {
                    startOcr(bytes,camera);
                }
            }
        };
        ThreadManager.getInstance().execute(recogRunnable);
    }

    private void startOcr(byte[] mdata, Camera camera) {
        prp.picByte = mdata;
        if (rotateLeft) {
            // 通知识别核心,识别前图像应先旋转的角度
            prp.plateIDCfg.bRotate = 0;
            if (Build.MODEL.equals(CameraManager.PHONE_MOLD)) {
                prp.plateIDCfg.bRotate = 2;
            }
            setHorizontalRegion();
            rotateLeft = false;
        } else if (rotateTop) {
            prp.plateIDCfg.bRotate = 1;
            if (Build.MODEL.equals(CameraManager.PHONE_MOLD)) {
                prp.plateIDCfg.bRotate = 3;
            }
            setLinearRegion();
            rotateTop = false;
        } else if (rotateRight) {
            prp.plateIDCfg.bRotate = 2;
            if (Build.MODEL.equals(CameraManager.PHONE_MOLD)) {
                prp.plateIDCfg.bRotate = 0;
            }
            setHorizontalRegion();
            rotateRight = false;
        } else if (rotateBottom) {
            prp.plateIDCfg.bRotate = 3;
            if (Build.MODEL.equals(CameraManager.PHONE_MOLD)) {
                prp.plateIDCfg.bRotate = 1;
            }
            setLinearRegion();
            rotateBottom = false;
        }
        String[] recogResult;
        if (!coreSetup.takePicMode) {
            //视频流自动识别
            recogResult = recogBinder.doRecogDetail(prp);
            nRet = recogBinder.getnRet();
            if (recogResult[0] != null && !"".equals(recogResult) && !" ".equals(recogResult)) {
                getRecogResult(recogResult, nRet, mdata);
            } else if (nRet != 0) {
                getRecogResult(recogResult, nRet, mdata);
            }
        } else if (coreSetup.takePicMode && isTakePicOnclick) {
            //拍照识别
            ((PlateidCameraActivity)activity).startAnim();
            recogResult = recogBinder.doRecogDetail(prp);
            nRet = recogBinder.getnRet();
            camera.stopPreview();
            getRecogResult(recogResult, nRet,mdata);
        }
    }

    private void getRecogResult(String[] recogRuslte, int nRet,byte[] mdata) {
        if (nRet != 0) {
            mHandler.sendEmptyMessage(1);
        } else {
            //授权通过，并有识别结果
            isGetResult = true;
            Vibrator mVibrator = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
            //震动提醒
            if (mVibrator != null) {
                mVibrator.vibrate(100);
            }
            ThreadManager.getInstance().remove(recogRunnable);
            String savePicturePath = "";
            if (coreSetup.savePicturePath != null && !"".equals(coreSetup.savePicturePath)) {
                CommonTools commonTools = new CommonTools();
                boolean isHaveRecogResult = false;
                if (recogRuslte[0] != null && !"".equals(recogRuslte[0])) {
                    // 检测到车牌时执行下列代码
                    mdata = recogBinder.getRecogData();
                    isHaveRecogResult = true;
                }
                //保存图片
                savePicturePath = commonTools.savePictures(coreSetup.savePicturePath, coreSetup.onlySaveOnePicture, mdata, preWidth, preHeight, prp.plateIDCfg.bRotate,isHaveRecogResult);

            }
            //释放服务
            releaseService();
            //回传结果
            ((PlateidCameraActivity)activity).getResultFinish(activity, recogRuslte, prp.plateIDCfg.bRotate, savePicturePath);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(activity, "错误码:" + String.valueOf(nRet) + "，请查阅开发手册寻找解决方案", Toast.LENGTH_LONG).show();
        }
    };

    /**
     * 设置横屏时的真实识别区域
     * preWidth：预览分辨率的宽
     * preHeight：预览分辨率的高setHorizontalRegion
     */
    private void setHorizontalRegion() {
        prp.plateIDCfg.left = preWidth / 4;
        prp.plateIDCfg.top = preHeight / 4;
        prp.plateIDCfg.right = preWidth / 4 + preWidth / 2;
        prp.plateIDCfg.bottom = preHeight - preHeight / 4;
//		System.out.println("横屏时   左  ："+prp.plateIDCfg.left+"   右  ："+prp.plateIDCfg.right+"     高："+prp.plateIDCfg.top+"    底："+prp.plateIDCfg.bottom);
    }

    /**
     * 设置竖屏时的真实识别区域
     * preWidth：预览分辨率的宽
     * preHeight：预览分辨率的高
     */
    private void setLinearRegion() {
        prp.plateIDCfg.left = preHeight / 24;
        prp.plateIDCfg.top = preWidth / 4;
        prp.plateIDCfg.right = preHeight / 24 + preHeight * 11 / 12;
        prp.plateIDCfg.bottom = preWidth / 4 + preWidth / 3;
//		 System.out.println("竖屏时      左  ："+prp.plateIDCfg.left+"   右  ："+prp.plateIDCfg.right+"     高："+prp.plateIDCfg.top+"    底："+prp.plateIDCfg.bottom);
    }


    /***
     * 释放服务
     * 连续识别不需要重复初始化、释放
     */
    private void releaseService() {
        if (recogBinder != null) {
            activity.unbindService(recogConn);
        }
    }

}
