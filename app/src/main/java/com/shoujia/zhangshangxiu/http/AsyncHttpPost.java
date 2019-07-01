package com.shoujia.zhangshangxiu.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.MyApplication;
import com.shoujia.zhangshangxiu.util.Encryption;
import com.shoujia.zhangshangxiu.util.JsonUtils;
import com.shoujia.zhangshangxiu.util.MyParcel;
import com.shoujia.zhangshangxiu.util.ZipUtils;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;


/**
 * 异步网络通讯
 * 
 * @author zhulin zhulin.zhuy@gmail.com
 * 
 * @date 2014-12-6
 */
public abstract class AsyncHttpPost extends AsyncTask<Object, Object, Object> {
	private final String TAG = this.getClass().getSimpleName();

	public abstract void onFail(Object reult);

	public abstract void onSuccess(Object reult);

	/** 连接状态 */
	private boolean connected = false;
	private String personalInterface;

	private MyParcel parm;

	/** http返回code */
	private int code;
	/** 输出流 */
	private OutputStream os;
	/** 输入流 */
	private InputStream is;
	/** http连接 */
	private HttpURLConnection hc;
	/** 链接地址 */
	private StringBuffer url = new StringBuffer();
	
	private static String sessionId="";
	
	public static boolean synSession =false;

	boolean connectError = false;
	
	public synchronized static void resultSynSession(boolean sysnVal){
		synSession = sysnVal;
	}
	
	/**
	 * json 复杂度 根据数据格式采用最佳方法 提高性能
	 */
	private int JsonLevel;

	public AsyncHttpPost(String personalInterface, MyParcel parm, int JsonLevel) {
		this.personalInterface = personalInterface;
		this.parm = parm;
		this.JsonLevel = JsonLevel;
	}
	
	protected void getSessionId(){
		String json = JsonUtils.mapTojson(parm.getMap());
		byte[] j = ZipUtils.compress(json);
		json = Encryption.Encrypt(j);
		try{
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
			sessionId =new String(cookieVal.substring(0,cookieVal.indexOf(";")));
			synSession=false;
		}catch(Exception e){
			
		}finally{
			hc.disconnect();
		}
	}
	
	public String request() {
		if(AsyncHttpPost.synSession){
			getSessionId();
		}
		
		// 连接失败flag，失败则中断连接，记录连接次数进行重连
		String json = JsonUtils.mapTojson(parm.getMap());
		byte[] j = ZipUtils.compress(json);
		json = Encryption.Encrypt(j);
		
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
			hc.setRequestProperty("Cookie",sessionId);
			
			hc.setRequestMethod("POST");// 设置URL请求方法POST
			hc.setInstanceFollowRedirects(true);
			hc.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
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
				is = hc.getInputStream();
				if (is == null) {// 连接失败
					connectError = true;
					// connectErrorTimes ++;
					throw new java.io.IOException();
				}
			}
			connected = true;
		} catch (Exception e) {
			disconnect();
		}
		if (!connected)
			return "";
		String responseData="";
		try {
			StringBuffer sb = new StringBuffer();
			byte[] b = new byte[1024];
			int len = 0;
			while (-1 != (len = is.read(b, 0, 200))) {
				String tmp = new String(b, 0, len, "UTF-8");
				sb.append(tmp);
			}
			 responseData = sb.toString();
			// 替换掉引号
			responseData = responseData.replaceAll("\"", "");
			byte[] jsonByte = Encryption.Decrypt(responseData);//返回为空
			responseData = ZipUtils.unCompress(jsonByte);
			sb = null;

			connected = false;
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			disconnect();
		}
		return responseData;
	}


    public String request2(){
		
		return "";		
	}
	
	private void creatURL() {
		// 再组url
		StringBuffer str = new StringBuffer();
		String debugAddress = MyApplication.getContext().getString(R.string.ip_address_debug);
		if(TextUtils.isEmpty(debugAddress)){
			String ipAdd = MyApplication.getContext().getString(R.string.ip_address);
			String areaName = ipAdd.substring(ipAdd.indexOf("www."), ipAdd.indexOf(".com")+4);
			java.net.InetAddress x;
			 String ip = null ;
	        try {
	            x = java.net.InetAddress.getByName(areaName);
	            ip = x.getHostAddress();// 得到字符串形式的ip地址
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			str.append("http://");
			str.append(ip);
			str.append(":8084/");
			debugAddress = str.toString();
		}else{
			str.append(debugAddress);
		}
		//////////////////////////////////
		
		/*str.append(AppApplication.getContext().getString(
				R.string.ip_address_test));
		*/
		
		str.append(personalInterface);
		url.append(str);
	}

	private void disconnect() {
		try {
			if (os != null)
				os.close();
			if (is != null)
				is.close();
			if (hc != null)
				hc.disconnect();
		} catch (Exception e) {
		} finally {
			os = null;
			is = null;
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
				//表示单层json
				if (JsonLevel == 0)
					result = JsonUtils.jsonToMap(result.toString());
				//表示多层json
				else if (JsonLevel == 1)
					result = JsonUtils.jsonToMap4(result.toString());
				//表示json 数组
				else if(JsonLevel == 3) {
					result = JsonUtils.jsonToMap(result.toString());
					System.out.println(result);
				}
				onSuccess(result);
			}

		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}

	}
}
