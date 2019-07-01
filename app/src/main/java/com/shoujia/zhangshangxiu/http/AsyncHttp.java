package com.shoujia.zhangshangxiu.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alibaba.fastjson.JSON;
import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.MyApplication;
import com.shoujia.zhangshangxiu.util.MyParcel;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;


/**
 * 异步网络通讯
 * 
 * @author zhulin zhulin.zhuy@gmail.com
 * 
 * @date 2014-12-6
 */
public abstract class AsyncHttp extends AsyncTask<Object, Object, Object> {

	private final String TAG = this.getClass().getSimpleName();

	public abstract void onFail(Object reult);

	public abstract void onSuccess(Object map);

	/** 连接状态 */
	private boolean connected = false;
	private String personalInterface;

	private MyParcel parm;

	/** http返回code */
	private int code;
	/** 输出流 */
	private OutputStream os;
	/** 输入流 */
	private InputStream in;
	/** http连接 */
	private HttpURLConnection hc;
	/** 链接地址 */
	private StringBuffer url = new StringBuffer();

	private static String sessionId = "";

	public static boolean synSession = false;

	boolean connectError = false;
	boolean isFree = false;

	public synchronized static void resultSynSession(boolean sysnVal) {
		synSession = sysnVal;
	}

	/**
	 * json 复杂度 根据数据格式采用最佳方法 提高性能
	 */
	private int JsonLevel;

	public AsyncHttp(String personalInterface, MyParcel parm, int JsonLevel) {
		this.personalInterface = personalInterface;
		this.parm = parm;
		this.JsonLevel = JsonLevel;
		this.isFree = false;
	}

	public AsyncHttp(String personalInterface, MyParcel parm, int JsonLevel, Boolean isFree) {
		this.personalInterface = personalInterface;
		this.parm = parm;
		this.JsonLevel = JsonLevel;
		this.isFree = isFree;
	}

	public AsyncHttp(String personalInterface, MyParcel parm, int JsonLevel, String mFreeUrl) {
		this.personalInterface = personalInterface;
		this.parm = parm;
		this.JsonLevel = JsonLevel;
		this.isFree = false;
	}

	protected void getSessionId() {
		String json = JSON.toJSONString(parm.getMap());
		try {
			URL urlpath = new URL(url.toString());
			// 建立连接
			hc = (HttpURLConnection) urlpath.openConnection();
			// 设置连接服务器超时5秒
			hc.setConnectTimeout(5000);

			// 设置读取服务器数据超时5秒
			hc.setReadTimeout(5000);
			// 设置属性
			hc.setDoOutput(true);// 使用 URL 连接进行输出
			hc.setDoInput(true);// 使用 URL 连接进行输入
			hc.setUseCaches(false);// 忽略缓存
			String cookieVal = hc.getHeaderField("Set-Cookie");
			sessionId = new String(cookieVal.substring(0, cookieVal.indexOf(";")));
			synSession = false;
		} catch (Exception e) {

		} finally {
			hc.disconnect();
		}
	}

	public String request() {
		if (AsyncHttp.synSession) {
			getSessionId();
		}

		// 连接失败flag，失败则中断连接，记录连接次数进行重连
		String json = JSON.toJSONString(parm.getMap());

		try {
			URL urlpath = new URL(url.toString());
			// 建立连接
			hc = (HttpURLConnection) urlpath.openConnection();
			// 设置连接服务器超时5秒
			hc.setConnectTimeout(5000);

			// 设置读取服务器数据超时5秒
			hc.setReadTimeout(20000);
			// 设置属性
			hc.setDoOutput(true);// 使用 URL 连接进行输出
			hc.setDoInput(true);// 使用 URL 连接进行输入
			hc.setUseCaches(false);// 忽略缓存
			hc.setRequestProperty("Cookie", sessionId);

			hc.setRequestMethod("POST");// 设置URL请求方法POST
			hc.setInstanceFollowRedirects(true);
			hc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			hc.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			// 建立连接
			hc.connect();
			if (hc == null) {// 连接失败
				connectError = true;
				throw new java.io.IOException();// 主动抛出异常中断后续动作进入本层的catch块，则可起到重连效果
			}
			byte[] tempStr = json.getBytes();
			os = hc.getOutputStream();
			os.write(tempStr);
			os.flush();
			os.close();
			// 获取响应状态
			code = hc.getResponseCode();
			if (HttpURLConnection.HTTP_OK != code) {// 连接失败
				connectError = true;
				// connectErrorTimes ++;
				throw new java.io.IOException();
			} else {
				// 做中国移动资费页面过滤处理
				// String header = hc.getHeaderField("Content-Type");
				// if (null != header && header.toLowerCase().indexOf("wml") !=
				// -1) {
				// request();
				// return "";
				// }
				// 读取输入流
				in = hc.getInputStream();
				if (in == null) {// 连接失败
					connectError = true;
					// connectErrorTimes ++;
					throw new java.io.IOException();
				}
			}
			connected = true;
		} catch (Exception e) {
			// 将toast放入主线程
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new Runnable() {
				public void run() {
					// Toast.makeText(AppApplication.getContext(),
					// "网络连接异常，请检查您的网络连接", Toast.LENGTH_LONG).show();
				}
			});
			disconnect();
		}
		if (!connected)
			return "";
		String responseData = "";
		try {
			StringBuffer sb = new StringBuffer();
			byte[] b = new byte[1024];
			int len = 0;
			while (-1 != (len = in.read(b, 0, 200))) {
				String tmp = new String(b, 0, len, "UTF-8");
				sb.append(tmp);
			}
			responseData = sb.toString();
			// 替换掉引号
			
			sb = null;

			connected = false;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return responseData;
	}

    private void creatURL() {
		// 再组url
		StringBuffer str = new StringBuffer();
		String debugAddress = "";
		if (isFree)
			debugAddress = MyApplication.getContext().getString(R.string.internet_ip_debug);
		else
			debugAddress = MyApplication.getContext().getString(R.string.ip_address_debug);

		if (TextUtils.isEmpty(debugAddress)) {
			String ipAdd = "";
			if (isFree)
				ipAdd = MyApplication.getContext().getString(R.string.internet_ip);
			else
				ipAdd = MyApplication.getContext().getString(R.string.ip_address);

			String areaName = ipAdd.substring(ipAdd.indexOf("www."), ipAdd.indexOf(".com") + 4);
			java.net.InetAddress x;
			String ip = null;
			try {
				x = java.net.InetAddress.getByName(areaName);
				ip = x.getHostAddress();// 得到字符串形式的ip地址
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (TextUtils.isEmpty(ip)) {
				str.append(ipAdd);
			} else {
				ipAdd.replace(areaName, ip);
				str.append(ipAdd);
			}
		} else {
			str.append(debugAddress);
		}

		str.append(personalInterface);
		url.append(str);
	}

	private void disconnect() {
		try {
			if (os != null)
				os.close();
			if (in != null)
				in.close();
			if (hc != null)
				hc.disconnect();
		} catch (Exception e) {
		} finally {
			os = null;
			in = null;
			hc = null;
		}
		connected = false;
	}

	@Override
	protected Object doInBackground(Object... arg0) {
		creatURL();
		return request();
	}

	// 当后台操作结束时，此方法将会被调用，计算结果将做为参数传递到此方法中，直接将结果显示到UI组件上
	@Override
	protected void onPostExecute(Object result) {
		try {
			if (result.toString().equals(""))
				onFail(result);
			else {
				onSuccess(result);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void toUrl(String url) {

	}
}
