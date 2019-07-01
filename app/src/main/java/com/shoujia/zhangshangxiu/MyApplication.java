package com.shoujia.zhangshangxiu;

import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;


public class MyApplication extends MultiDexApplication {
    private static MyApplication instance;
    private static final String JCHAT_CONFIGS = "JChat_configs";
    public static final String TARGET_ID = "targetId";
    public static final String ATUSER = "atuser";
    public static final String TARGET_APP_KEY = "targetAppKey";

    public static final String DRAFT = "draft";
    public static final String GROUP_ID = "groupId";
    public static final String POSITION = "position";
    public static final String MsgIDs = "msgIDs";
    public static final String NAME = "name";
    public static final String ATALL = "atall";

    public static final String CONV_TITLE = "conv_title";

    private static OkHttpClient okHttp;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        // 缓存图片的配置，一般通用的配置
        instance = this;
        //initImageLoader(getApplicationContext());
        //极光推送
        okHttp = new OkHttpClient();
        getDefaultSSLSocketFactory();
        /*JMessageClient.setDebugMode(true);
        JMessageClient.init(this);
        JMessageClient.registerEventReceiver(new GlobalEventListener(getApplicationContext()));

        SharePreferenceManager.init(getApplicationContext(), JCHAT_CONFIGS);
        //设置Notification的模式
        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_WITH_SOUND | JMessageClient.FLAG_NOTIFY_WITH_LED | JMessageClient.FLAG_NOTIFY_WITH_VIBRATE);
        //注册Notification点击的接收器
        new NotificationClickEventReceiver(getApplicationContext());
        */
    }

    public static OkHttpClient getOkHttpClient() {
        if(okHttp==null){
            return new OkHttpClient();
        }else{
            return okHttp;
        }
    }
    public static Context getContext() {
        return instance;
    }


    // 信任所有证书，不建议这么操作
    private static synchronized SSLSocketFactory getDefaultSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {

                        }

                        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
                        }

                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            }, null);
            return sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new AssertionError();
        }
    }
}