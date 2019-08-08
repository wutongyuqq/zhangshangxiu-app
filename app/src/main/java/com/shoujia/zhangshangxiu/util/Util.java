package com.shoujia.zhangshangxiu.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;


import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.MyApplication;
import com.shoujia.zhangshangxiu.order.entity.JsBaseBean;
import com.shoujia.zhangshangxiu.order.entity.JsPartBean;
import com.shoujia.zhangshangxiu.order.entity.JsXmBean;
import com.shoujia.zhangshangxiu.web.JsonUtil;
import com.shoujia.zhangshangxiu.web.MainActivity;
import com.shoujia.zhangshangxiu.web.NetTool;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class Util {
	public static String getUrl(){

		return getIp()+"/restful/pro";
	}
	public static String getIp(){
		SharePreferenceManager sp = new SharePreferenceManager(MyApplication.getContext());
		String ipAddress = sp.getString(Constance.server_ip_port);
		if(TextUtils.isEmpty(ipAddress)) {
			return MyApplication.getContext().getString(R.string.internet_ip_debug);
		}else{
			return "http://"+ipAddress;
		}
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

	/*
	* 保留两位小数
	* */
	public static String getDoubleStr(String doubleStr){
		String formatStr = "0";
		try {
			double doubleNum = Double.parseDouble(doubleStr);
			DecimalFormat df = new DecimalFormat("0.00");
			formatStr = df.format(doubleNum);
		}catch(Exception e){

		}
		return formatStr;
	}

	//将2019-01-01 01:01:01截取为2019-01-01
	public static String getFormatDate(String oldStr){
		if(TextUtils.isEmpty(oldStr)){
			return "";
		}
		if(oldStr.length()<=10){
			return oldStr;
		}
		return oldStr.substring(0,10);
	}

	public static void print(Context context, final JsBaseBean baseBean, List<JsXmBean> xmList ,
							 List<JsPartBean> pjDataList) {

		if (baseBean == null) {
			Toast.makeText(context, "数据异常，请重新操作！", Toast.LENGTH_LONG).show();
			return;
		}
		String jsd_id = baseBean.jsd_id;
		String ticheTime = baseBean.jc_date;
		String company_name = baseBean.compName;
		String cjhm = baseBean.cjhm;
		String cz = baseBean.cz;
		String cp = baseBean.cp;
		String cx = baseBean.cx;
		String jclc = baseBean.jclc;
		String car_fault = baseBean.car_fault;
		String totalsl = baseBean.totalPartSl+"";
		String totalMoney = baseBean.totalPartMoney+"";
		String yszje =  baseBean.totalPartMoney+baseBean.totalXlf+"";
		String address = baseBean.address;
		String telphone = baseBean.telphone;
		String jc_date = baseBean.jc_date;
		String memo = baseBean.memo;
		String dyTime = baseBean.printDate;
		String totalXlf = baseBean.totalXlf+"";
		String totalZk = baseBean.totalZkMoney+"";
		String content = "<FH><FB><center>首佳软件</center></FB></FH>";
		content += " <FH2>";
		content += "<center> 结算单号： " + jsd_id + "  预完工时间：" + ticheTime
				+ "</center> \r";
		content += " 修理厂名称: " + company_name + "\r";
		content += " 客户名称: " + cz + "\r";
		content += " 车牌: " + cp + "\r";
		content += " 车架号: " + cjhm + "\r";
		content += " 车型: " + cx + "\r";
		content += " 进厂里程: " + jclc + "\r";
		content += " 故障描述: " + memo + "\r";

		content += " </FH2>\r";

		content += "<FH>";
		content += "--------------------------------------------\r";
		if (xmList != null && xmList.size() > 0) {
			content += "项目名称          修理费         优惠\r";
			for (int i = 0; i < xmList.size(); i++) {
				JsXmBean temp = xmList.get(i);
				String wxgz = temp.wxgz;
				String xlf = temp.xlf;
				String zk = temp.zk;
				content += (wxgz.length() < 14 ? ((wxgz + "              ")
						.substring(0, 10)) : wxgz)
						+ " "
						+ (wxgz.length() < 10 ? ((wxgz + "              ")
						.substring(0, 10)) : wxgz)
						+ " "
						+ (zk.length() < 10 ? ((zk + "").substring(0, 10)) : zk)
						+ "\r";
			}
			content += "小计              "
					+ (totalXlf.length() < 10 ? ((totalXlf + "              ")
					.substring(0, 10)) : totalXlf)
					+ " "
					+ (totalZk.length() < 10 ? ((totalZk + "").substring(0, 10))
					: totalZk) + "</FH>\r";
			content += "--------------------------------------------\r";
		}

		if (pjDataList != null && pjDataList.size() > 0) {
			content += "<FH>配件名称          数量        单价        金额\r";
			for (int i = 0; i < pjDataList.size(); i++) {
				JsPartBean temp = pjDataList.get(i);
				String pjmc = temp.pjmc;
				String sl = temp.sl;
				String ssj = temp.ssj;
				String totalStr = "";
				if (!sl.equals("") && !ssj.equals("")) {
					float numTotal = Float.parseFloat(sl)
							* Float.parseFloat(ssj);
					float bFloat = (float) (Math.round(numTotal * 100)) / 100;
					totalStr = bFloat + "";
				}

				content += (pjmc + "          ").substring(0, 14)
						+ (sl.length() < 12 ? ((sl + "          ").substring(0,
						12)) : sl)
						+ " "
						+ (ssj.length() < 12 ? ((ssj + "          ").substring(
						0, 12)) : ssj) + " " + totalStr + "\r";
			}
			content += "小计           "
					+ (totalsl.length() < 12 ? ((totalsl + "              ")
					.substring(0, 12)) : totalsl)
					+ "           "
					+ (totalMoney.length() < 12 ? ((totalMoney + "").substring(
					0, 12)) : totalMoney) + "\r";
			content += "</FH>\r";
			content += "--------------------------------------------\r";
		}
		content += "<FH>  应收总计：￥" + yszje + "\r";
		content += "--------------------------------------------\r";

		content += "  地址：" + address + "\r";
		content += "  电话：" + telphone + "\r";
		content += "--------------------------------------------\r";
		content += "  客户签字：\r";
		content += "  接待签字：\r";
		content += "  日期：" + jc_date + "\r";
		content += "  备注：" + memo + "\r";
		content += "  打印时间：" + dyTime + "\r";
		content += "--------------------------------------------\r";
		NetTool printMsg = new NetTool(context);
		printMsg.print(content);
	}

}
