package com.kernal.plateid.controller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

import static android.content.Context.SENSOR_SERVICE;

/***
 * @author user
 * 识别方向旋转类（重力感应）
 */
public class ScreenRotation {
    private SensorManager sensorManager;
    /**向左旋转*/
    public boolean rotateLeft = false;
    /**正向旋转*/
    public boolean rotateTop = true;
    /**向右旋转*/
    public boolean rotateRight = false;
    /**倒置旋转*/
    public boolean rotateBottom = false;
    private Handler mHandler;

    /**
     * 根据重力感应  获取屏幕状态
     */
    public ScreenRotation(Context context, Handler mHandler) {
        this.mHandler = mHandler;

        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        Sensor sensor = null;
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        if (sensorManager != null) {
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[SensorManager.DATA_X];
            float y = event.values[SensorManager.DATA_Y];
            int standardValue = 7;
            if (x > standardValue) {
                if (!rotateLeft) {
                    System.out.println("向左旋转");
                    rotateBottom = false;
                    rotateRight = false;
                    rotateTop = false;
                    rotateLeft = true;
                    mHandler.sendEmptyMessage(0);
                }

            } else if (x < -standardValue) {
                if (!rotateRight) {
                    System.out.println("向右旋转");
                    rotateBottom = false;
                    rotateRight = true;
                    rotateTop = false;
                    rotateLeft = false;
                    mHandler.sendEmptyMessage(0);
                }

            } else if (y < -standardValue) {
                if (!rotateBottom) {
                    System.out.println("倒置旋转");
                    rotateBottom = true;
                    rotateRight = false;
                    rotateTop = false;
                    rotateLeft = false;
                    mHandler.sendEmptyMessage(0);
                }
            } else if (y > standardValue) {
                if (!rotateTop) {
                    System.out.println("竖屏状态");
                    rotateBottom = false;
                    rotateRight = false;
                    rotateTop = true;
                    rotateLeft = false;
                    mHandler.sendEmptyMessage(0);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    public void recoverySensorManager() {
        sensorManager.unregisterListener(listener);
    }

}
