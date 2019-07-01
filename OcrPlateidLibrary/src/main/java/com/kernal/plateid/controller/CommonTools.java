package com.kernal.plateid.controller;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.kernal.plateid.CoreSetup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;


/***
 * @author user
 * 有用的方法管理类
 */
public class CommonTools {

    public CommonTools() {
    }

    /***
     * 获取合适的预览分辨率
     * @param parameters 参数
     * @return 预览分辨率
     */
    Point getCameraPreviewSize(Camera.Parameters parameters) {
        //获取所有支持的预览分辨率组
        List<Camera.Size> rawSupportedSizes = parameters.getSupportedPreviewSizes();
        Camera.Size supportedPreviewSize;
        CoreSetup coreSetup = new CoreSetup();
        if(rawSupportedSizes == null) {
            supportedPreviewSize = parameters.getPreviewSize();
            return new Point(supportedPreviewSize.height, supportedPreviewSize.width);
        } else {
            if(rawSupportedSizes.size()==1){
                return new Point(rawSupportedSizes.get(0).width, rawSupportedSizes.get(0).height);
            }else{
                Iterator var6 = rawSupportedSizes.iterator();
                do {
                    if(!var6.hasNext()) {
                        return this.getCloselyPreSize( parameters, coreSetup.preWidth,coreSetup.preHeight);
                    }

                    supportedPreviewSize = (Camera.Size)var6.next();
//                    System.out.println("预览分辨率全部："+supportedPreviewSize.width+"   "+supportedPreviewSize.height);
                } while(supportedPreviewSize.width != coreSetup.preWidth || supportedPreviewSize.height != coreSetup.preHeight);
            }
            return new Point(coreSetup.preWidth, coreSetup.preHeight);
        }
    }

    /**
     *  若未找到设置的预览分辨率   则寻找最接近预览分辨率（优先寻找同比例   例如1920*1080与1280*720 同比例）
     * @param parameters 相机参数
     * @param width 宽
     * @param height 高
     * @return 预览分辨率
     */
    private Point getCloselyPreSize(Camera.Parameters parameters, int width, int height) {
        int reqtmpwidth = width;
        int reqTmpHeight = height;
        int realWidth = 0, realHeight = 0;
        float reqRatio = (float) reqtmpwidth / (float) reqTmpHeight;
        float deltaRatioMin = 3.4028235E38F;
        List preSizeList = parameters.getSupportedPreviewSizes();
        Camera.Size retSize = null;
        Iterator var13 = preSizeList.iterator();
        Camera.Size defaultSize;
        while (var13.hasNext()) {
            defaultSize = (Camera.Size) var13.next();
            float realRatio = (float) defaultSize.width / (float) defaultSize.height;
            if (reqRatio == realRatio) {
                if(defaultSize.width<=1920){
                    if (realWidth <= defaultSize.width) {
                        realWidth = defaultSize.width;
                        realHeight = defaultSize.height;
//                        LogUtil.E(TAG, "筛选参数：" + realWidth + "   " + realHeight);
                    }
                }

            }

        }
        if (realWidth == 0 || realHeight == 0) {
            while (var13.hasNext()) {
                defaultSize = (Camera.Size) var13.next();
                float curRatio = (float) defaultSize.width / (float) defaultSize.height;
                float deltaRatio = Math.abs(reqRatio - curRatio);
                if (deltaRatio < deltaRatioMin) {
                    deltaRatioMin = deltaRatio;
                    retSize =defaultSize;
                    realWidth = defaultSize.width;
                    realHeight = defaultSize.height;
                }
            }
            if (retSize == null) {
                defaultSize = parameters.getPreviewSize();
                retSize = defaultSize;
//                LogUtil.D(TAG, "没找到合适的尺寸，使用默认尺寸: " + defaultSize);
            }
        }
        return new Point(realWidth, realHeight);
    }

    /***
     * 获取真实的屏幕分辨率
     * @param context 上下文
     * @return 屏幕分辨率
     */
    @SuppressLint("ObsoleteSdkInt")
    public Point getScreenSize(Context context) {
        Point screenSize = new Point();
        WindowManager wm = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE));
        Display display = null;
        if (wm != null) {
            display = wm.getDefaultDisplay();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealSize(screenSize);
                screenSize.x = screenSize.x;
                screenSize.y = screenSize.y;
            } else {
                display.getSize(screenSize);
                screenSize.x = screenSize.x;
                screenSize.y = screenSize.y;
            }
        } else {
            screenSize.x = display.getWidth();
            screenSize.y = display.getHeight();
        }
        return screenSize;

    }

    /**
     * @param mDecorView {tags} 设定文件
     * @return ${return_type} 返回类型
     * @throws @Title: 沉寂模式
     * @Description: 隐藏虚拟按键
     */
    @TargetApi(19)
    public void hiddenVirtualButtons(View mDecorView) {
        if (Build.VERSION.SDK_INT >= 19) {
            mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    /***
     * 保存图片
     * @param picturesPath 图片路径
     * @param onlySaveOnePicture 是否只保存一张
     * @param roteByte 图像数据源
     * @param preW 图像宽
     * @param preH 图像高
     * @param screenDirection 当前拍摄图片方向
     * @param isHaveRecogResult 是否有识别结果
     * @return 保存图片的路径
     */
    public String savePictures(String picturesPath,Boolean onlySaveOnePicture,byte[] roteByte,int preW,int preH,int screenDirection,boolean isHaveRecogResult) {
        YuvImage yuvimage;
        String strCaptureFilePath = "";
        if(!isHaveRecogResult){
            if(screenDirection == 1){
                roteByte = rotateYUV420Degree90(roteByte,preW,preH);
            }else if(screenDirection == 2){
                roteByte = rotateYUV420Degree180(roteByte,preW,preH);
            }else if(screenDirection == 3){
                roteByte = rotateYUVDegree270(roteByte,preW,preH);
            }
        }
        if(onlySaveOnePicture){
            strCaptureFilePath = picturesPath;
        }else{
            strCaptureFilePath = picturesPath.substring(0,picturesPath.lastIndexOf(".")) + String.valueOf(System.currentTimeMillis())+".jpg";
        }
        File dir = new File(strCaptureFilePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(strCaptureFilePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            if (screenDirection == 1 || screenDirection == 3) {
                yuvimage = new YuvImage(roteByte,
                        ImageFormat.NV21,  preH, preW,
                        null);
                yuvimage.compressToJpeg(new Rect(0, 0,
                        preH, preW), 80, fileOutputStream);
            } else {
                yuvimage = new YuvImage(roteByte,
                        ImageFormat.NV21, preW, preH,
                        null);
                yuvimage.compressToJpeg(new Rect(0, 0,
                        preW, preH), 80, fileOutputStream);
            }
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strCaptureFilePath;
    }

    /**
     * YUV数据旋转90度
     * @param data
     * @param imageWidth
     * @param imageHeight
     * @return
     */
    private byte[] rotateYUV420Degree90(byte[] data, int imageWidth, int imageHeight)
    {
        byte [] yuv = new byte[imageWidth*imageHeight*3/2];
        // Rotate the Y luma
        int i = 0;
        for(int x = 0;x < imageWidth;x++)
        {
            for(int y = imageHeight-1;y >= 0;y--)
            {
                yuv[i] = data[y*imageWidth+x];
                i++;
            }
        }
        // Rotate the U and V color components
        i = imageWidth*imageHeight*3/2-1;
        for(int x = imageWidth-1;x > 0;x=x-2)
        {
            for(int y = 0;y < imageHeight/2;y++)
            {
                yuv[i] = data[(imageWidth*imageHeight)+(y*imageWidth)+x];
                i--;
                yuv[i] = data[(imageWidth*imageHeight)+(y*imageWidth)+(x-1)];
                i--;
            }
        }
        return yuv;
    }

    private byte[] rotateYUV420Degree180(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        int i = 0;
        int count = 0;
        for (i = imageWidth * imageHeight - 1; i >= 0; i--) {
            yuv[count] = data[i];
            count++;
        }
        i = imageWidth * imageHeight * 3 / 2 - 1;
        for (i = imageWidth * imageHeight * 3 / 2 - 1; i >= imageWidth
                * imageHeight; i -= 2) {
            yuv[count++] = data[i - 1];
            yuv[count++] = data[i];
        }
        return yuv;
    }

    private byte[] rotateYUVDegree270(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate the Y luma
        int i = 0;
        for (int x = imageWidth - 1; x >= 0; x--) {
            for (int y = 0; y < imageHeight; y++) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }
        }// Rotate the U and V color components
        i = imageWidth * imageHeight;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                i++;
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i++;
            }
        }
        return yuv;
    }

    /***
     * 获取相册图片的路径
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(19)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


    /***
     * 缩放动画
     */
    public void myViewScaleAnimation(View myView) {
        ScaleAnimation animation = new ScaleAnimation(1f, 1.1f, 1f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setDuration(300);
        animation.setFillAfter(false);
        animation.setRepeatCount(0);
        myView.startAnimation(animation);
    }
}
