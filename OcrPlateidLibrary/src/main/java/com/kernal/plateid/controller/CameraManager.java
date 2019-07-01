package com.kernal.plateid.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.util.List;

/****
 * @author user
 * @date 201812-20
 * 相机参数设置方法管理类
 */
public class CameraManager implements CameraHelper {

    private static final int AUTO_FOCUS_MESSAGE = 10;
    private static final int AUTO_FOCUS_DELAY_TIME = 2000;
    public Camera camera = null;
    private Camera.Parameters parameters;
    private CommonTools commonTools;
    public Point prePoint;
    private Activity activity;
    static final String PHONE_MOLD = "Nexus 5X";

    @SuppressLint("HandlerLeak")
    private Handler autoFocusHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == AUTO_FOCUS_MESSAGE) {
                autoFocus();
            }
        }
    };

    /***
     * 初始化启用循环对焦
     * 4000ms对焦一次
     */
    public CameraManager(Activity activity) {
        commonTools = new CommonTools();
        this.activity = activity;
        autoFocusHandler.sendEmptyMessageDelayed(
                AUTO_FOCUS_MESSAGE, AUTO_FOCUS_DELAY_TIME);
    }

    /***
     * 打开相机
     * @param cameraId 前置还是后置
     * @return 相机实体
     */
    @Override
    public Camera openCamera(int cameraId) {
        if (camera == null) {
            try {
                camera = Camera.open(cameraId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return camera;
    }

    /***
     * 设置初始化相机参数
     * @param surfaceHolder 相机参数
     * @param srcPoint 设备的屏幕分辨率，用来获取合适的相机预览分辨率
     */
    @Override
    public void setUpCamera(SurfaceHolder surfaceHolder, Point srcPoint) {
        try {
            camera.setPreviewDisplay(surfaceHolder);
            parameters = camera.getParameters();

            //筛选合适的预览分辨率
            System.out.println("srcWidth:" + srcPoint.x + "-" + srcPoint.y);
            prePoint = commonTools.getCameraPreviewSize(parameters);
            System.out.println("preWidth:" + prePoint.x + "-" + prePoint.y);
            //设置预览分辨率
            parameters.setPreviewSize(prePoint.x, prePoint.y);
            //设置对焦模式
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            if (PHONE_MOLD.equals(Build.MODEL)) {
                camera.setDisplayOrientation(270);
            } else {
                camera.setDisplayOrientation(90);
            }
            parameters.setPictureFormat(PixelFormat.JPEG);
            camera.setParameters(parameters);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自动对焦
     */
    @Override
    public void autoFocus() {
        if (camera != null) {
            try {
                camera.autoFocus(null);
                autoFocusHandler.sendEmptyMessageDelayed(
                        AUTO_FOCUS_MESSAGE, AUTO_FOCUS_DELAY_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * 设置焦距
     */
    @Override
    public void setFocusLength(int progress) {
        if (this.camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            if (parameters.isZoomSupported()) {
                parameters.setZoom(progress);
                camera.setParameters(parameters);
            } else {
                Toast.makeText(activity, "不支持调焦", Toast.LENGTH_LONG).show();
            }
        }
    }

    /***
     * 计算单位焦距（因为seekbar分为100分，所以单位焦距为最大焦距除以100）
     */
    @Override
    public double getFocus() {
        if (this.camera != null) {
            if (camera.getParameters().isZoomSupported()) {
                return (double) camera.getParameters().getMaxZoom() / 100;
            } else {
                Toast.makeText(activity, "不支持调焦", Toast.LENGTH_LONG).show();
            }
        }
        return 0;
    }

    /***
     * 控制闪光灯
     */
    @Override
    public void controlFlash() {
        if (camera != null) {
            String flashMode = parameters.getFlashMode();
            if (flashMode == null) {
                Toast.makeText(activity, "不支持闪光灯", Toast.LENGTH_LONG).show();
            } else {
                if (flashMode.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                } else {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    parameters.setExposureCompensation(-1);
                }
            }
            try {
                camera.setParameters(parameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * 关闭相机
     */
    @Override
    public void removeCamera() {
        if (camera != null) {
            try {
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
