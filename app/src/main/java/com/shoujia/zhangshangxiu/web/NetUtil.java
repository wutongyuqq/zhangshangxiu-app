package com.shoujia.zhangshangxiu.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by yufs on 2017/8/16.
 */

public class NetUtil {
    public static final int DOWNLOAD_FAIL=0;
    public static final int DOWNLOAD_PROGRESS=1;
    public static final int DOWNLOAD_SUCCESS=2;
    private static NetUtil downloadUtil;
    private final OkHttpClient okHttpClient;
    public static NetUtil getInstance() {
        if (downloadUtil == null) {
            downloadUtil = new NetUtil();
        }
        return downloadUtil;
    }

    private NetUtil() {
        okHttpClient = new OkHttpClient();
    }
    
    
    
    public String getUpDateInfo(String filePath) {
        String path = filePath;
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
           // 创建一个url对象
           URL url = new URL(path);
           // 通過url对象，创建一个HttpURLConnection对象（连接）
           HttpURLConnection urlConnection = (HttpURLConnection) url
                 .openConnection();
           // 通过HttpURLConnection对象，得到InputStream
           reader = new BufferedReader(new InputStreamReader(
                 urlConnection.getInputStream()));
           // 使用io流读取文件
           while ((line = reader.readLine()) != null) {
              sb.append(line);
           }
        } catch (Exception e) {
           e.printStackTrace();
        } finally {
           try {
              if (reader != null) {
                 reader.close();
              }
           } catch (Exception e) {
              e.printStackTrace();
           }
        }
        String info = sb.toString();
    
        return "";
     }

  




    /**
     *
     */
    public void download2(final String url, final String saveDir, final OnDownloadListener listener){
        this.listener=listener;
        Request request=new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onDownloadFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is=null;
                byte[] buf=new byte[2048];
                int len=0;
                FileOutputStream fos=null;
                //储存下载文件的目录
                String savePath=isExistDir(saveDir);
                try{
                    is=response.body().byteStream();
                    long total=response.body().contentLength();
                    File file=new File(savePath,getNameFromUrl(url));
                    fos=new FileOutputStream(file);
                    long sum=0;
                    while((len = is.read(buf))!=-1){
                        fos.write(buf,0,len);
                        sum+=len;
                        int progress=(int)(sum*1.0f/total*100);
                        //下载中
                        listener.onDownloading(progress);

                    }
                    fos.flush();
                    //下载完成
                    listener.onDownloadSuccess(file.getAbsolutePath());
                }catch (Exception e){
                	listener.onDownloadFailed();
                }finally{
                    try{
                        if(is!=null)
                            is.close();
                    }catch (IOException e){

                    }
                    try {
                        if(fos!=null){
                            fos.close();
                        }
                    }catch (IOException e){

                    }
                }
            }
        });
    }

    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/")+1);
    }


    private String isExistDir(String saveDir) throws IOException {
        File downloadFile=new File(saveDir);
        if(!downloadFile.mkdirs()){
            downloadFile.createNewFile();
        }
        String savePath=downloadFile.getAbsolutePath();
        return savePath;
    }





    OnDownloadListener listener;
    public interface OnDownloadListener{
        /**
         * 下载成功
         */
        void onDownloadSuccess(String path);
        /**
         * 下载进度
         * @param progress
         */
        void onDownloading(int progress);
        /**
         * 下载失败
         */
        void onDownloadFailed();
    }
    
    
}
