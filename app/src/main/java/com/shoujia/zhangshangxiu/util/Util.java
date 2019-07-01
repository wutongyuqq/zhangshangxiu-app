package com.shoujia.zhangshangxiu.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;


import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.MyApplication;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Util {
	public static String getUrl(){

		return getIp()+"/restful/pro";
	}
	public static String getIp(){
		return MyApplication.getContext().getString(R.string.internet_ip_debug);
	}


	public static int dp2px(Context context,float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}


	/**
	 * 将ip的整数形式转换成ip形式
	 *
	 * @param ipInt
	 * @return
	 */
	public static String int2ip(int ipInt) {
		StringBuilder sb = new StringBuilder();
		sb.append(ipInt & 0xFF).append(".");
		sb.append((ipInt >> 8) & 0xFF).append(".");
		sb.append((ipInt >> 16) & 0xFF).append(".");
		sb.append((ipInt >> 24) & 0xFF);
		return sb.toString();
	}



	//GPRS连接下的ip
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return null;
	}


	public static String getIpAddress(Context context){
		String ip = "";
		ConnectivityManager conMann = (ConnectivityManager)
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileNetworkInfo = conMann.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNetworkInfo = conMann.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


		if (mobileNetworkInfo.isConnected()) {
			ip = getLocalIpAddress();
			System.out.println("local ip"+ip);
		}else if(wifiNetworkInfo.isConnected())
		{
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
			ip = int2ip(ipAddress);
			System.out.println("wifi ip"+ip);
		}
		return ip;
	}
}
