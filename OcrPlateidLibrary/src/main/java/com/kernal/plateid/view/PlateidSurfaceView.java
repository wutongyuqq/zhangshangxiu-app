package com.kernal.plateid.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.kernal.plateid.CoreSetup;
import com.kernal.plateid.controller.CameraManager;
import com.kernal.plateid.controller.PreviewCallback;

/***
 * @author user
 * 自定义SurfaceView
 */
@SuppressLint("ViewConstructor")
public class PlateidSurfaceView extends SurfaceView implements SurfaceHolder.Callback {


    private static final int CAMERA_ID = 0;
    private Activity activity;
    private CameraManager mCameraManager;
    private Point srcPoint;
    private PreviewCallback callback;
    private boolean isSetCamera;
    private CoreSetup coreSetup;


    public PlateidSurfaceView(Activity activity, final Point srcPoint, CameraManager cameraManager, CoreSetup coreSetup) {
        super(activity);
        this.activity = activity;
        this.srcPoint = srcPoint;
        this.coreSetup = coreSetup;
        SurfaceHolder surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mCameraManager = cameraManager;
        mCameraManager.openCamera(CAMERA_ID);
        isSetCamera = true;

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (isSetCamera) {
            isSetCamera = false;
            mCameraManager.setUpCamera(surfaceHolder, srcPoint);
            if (mCameraManager.prePoint != null) {
                callback = new PreviewCallback(activity, mCameraManager.prePoint, coreSetup);
                mCameraManager.camera.setPreviewCallback(callback);
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        mCameraManager.autoFocus();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCameraManager.removeCamera();
    }


    /**
     * 界面旋转，识别区域也要旋转
     *
     * @param left 左
     * @param right 右
     * @param top 上
     * @param bottom 下
     */
    public void screenRotationChange(boolean left, boolean right, boolean top, boolean bottom) {
        if (callback != null) {
            callback.screenRotationChange(left, right, top, bottom);
        }
    }

    /***
     * 点击拍照按钮
     */
    public void isTakePicOnclick(boolean isTakePicOnclick) {
        if (callback != null) {
            callback.isTakePicOnclick(isTakePicOnclick);
        }
    }

    public void controlFlash() {
        mCameraManager.controlFlash();
    }


    /****
     * 计算设置surfaceView宽高
     * @param widthMeasureSpec 宽
     * @param heightMeasureSpec 高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        short desiredWidth;
        short desiredHeight;
        desiredWidth = (short) coreSetup.preHeight;
        desiredHeight = (short) coreSetup.preWidth;
        float radio = (float) desiredWidth / (float) desiredHeight;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int layoutWidth;
        if (widthMode == MeasureSpec.EXACTLY) {
            layoutWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            layoutWidth = Math.min(desiredWidth, widthSize);
        } else {
            layoutWidth = desiredWidth;
        }

        int layoutHeight;
        if (heightMode == MeasureSpec.EXACTLY) {
            layoutHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            layoutHeight = Math.min(desiredHeight, heightSize);
        } else {
            layoutHeight = desiredHeight;
        }

        float layoutRadio = (float) layoutWidth / (float) layoutHeight;
        if (layoutRadio > radio) {
            layoutHeight = (int) ((float) layoutWidth / radio);
        } else {
            layoutWidth = (int) ((float) layoutHeight * radio);
        }

        this.setMeasuredDimension(layoutWidth, layoutHeight);
    }

}
