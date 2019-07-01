package com.shoujia.zhangshangxiu.web;

import android.app.Application;
import android.content.Context;

import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import okhttp3.OkHttpClient;


public class MyApplication extends Application {
	private static MyApplication instance;
	private static OkHttpClient okHttp;
	 @Override
	    public void onCreate() {
	        // TODO Auto-generated method stub
	        super.onCreate();

	        instance = this;
	        okHttp = new OkHttpClient();
	        getDefaultSSLSocketFactory();
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