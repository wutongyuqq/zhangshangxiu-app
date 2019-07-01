package com.kernal.plateid.controller;


import android.graphics.Point;
import android.hardware.Camera;
import android.view.SurfaceHolder;


/**
 * @author user
 */
public interface CameraHelper {

    /**
     * 打开相机
     * @param cameraId 前置还是后置摄像头
     * @return 相机实体
     * @throws Exception 抛出
     */
    Camera openCamera(int cameraId) throws Exception;

    /***
     * 设置相机参数
     * @param surfaceHolder 参数
     * @param srcPoint 屏幕宽高
     */
    void setUpCamera(SurfaceHolder surfaceHolder, Point srcPoint);
    /***
     * 设置循环自动对焦
     */
    void autoFocus();
    /***
     * 设置焦距
     * @param progress 焦距值
     */
    void setFocusLength(int progress);
    /***
     * 计算单位焦距（因为seekbar分为100分，所以单位焦距为最大焦距除以100）
     * @return 得出焦距值
     */
    double getFocus();
    /***
     * 控制闪光灯
     */
    void controlFlash();
    /***
     * 释放相机
     */
    void removeCamera();
}
