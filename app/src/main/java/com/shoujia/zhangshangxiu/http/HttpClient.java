package com.shoujia.zhangshangxiu.http;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shoujia.zhangshangxiu.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HttpClient {
    public static final MediaType JSONTYPE = MediaType.parse("application/json; charset=utf-8");
    public void get(String url,final IGetDataListener listener) {
        if(listener==null){
            return;
        }
        final Request request = new Request.Builder()
                .url(url).method("GET",null)
                .build();

        MyApplication.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                listener.onFail();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null&&response.body()!=null){
                    String bodyStr = response.body().string();
                    if(!TextUtils.isEmpty(bodyStr)) {
                        listener.onSuccess(bodyStr);
                    }else{
                        listener.onFail();
                    }
                }else{
                    listener.onFail();
                }
                System.out.println(response.body().string());
            }
        });
    }
    public void post(String url, Map<String,String> dataMap, final IGetDataListener listener) {
        FormBody.Builder builder = new FormBody.Builder();
        if(listener==null){
            return;
        }

        if(dataMap==null){
            listener.onFail();
        }
        String jsonStr = JSON.toJSONString(dataMap);
        Log.e("post---data--request",jsonStr);
        //2.通过new FormBody()调用build方法,创建一个RequestBody,可以用add添加键值对
        RequestBody  requestBody = RequestBody.create(JSONTYPE, jsonStr);
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder().url(url).post(requestBody).build();
        //4.创建一个call对象,参数就是Request请求对象
        Call call = MyApplication.getOkHttpClient().newCall(request);
        //5.请求加入调度,重写回调方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null&&response.body()!=null){
                    String bodyStr = response.body().string();
                    if(!TextUtils.isEmpty(bodyStr)&&isjson(bodyStr)) {
                        Log.e("post---data--response",bodyStr);
                        listener.onSuccess(bodyStr);
                    }else{
                        listener.onFail();
                    }
                }else{
                    listener.onFail();
                }
            }
        });
    }

    private boolean isjson(String string){
        try {
            JSONObject jsonStr= JSONObject.parseObject(string);
            return  true;
        } catch (Exception e) {
            return false;
        }
    }


    public void downLoadFile(String url, final String filePath, final IGetDataListener listener){
        Request request = new Request.Builder().url(url).get().build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = MyApplication.getOkHttpClient().newCall(request);
        //4.请求加入调度,重写回调方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFail();
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    //拿到字节流
                    InputStream is = response.body().byteStream();
                    int len = 0;
                    //设置下载图片存储路径和名称
                    File file = new File(filePath);
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buf = new byte[128];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    fos.close();
                    is.close();
                    listener.onSuccess("");
                }catch (Exception e){
                    e.printStackTrace();
                    listener.onFail();
                }
            }
        });
    }
}
