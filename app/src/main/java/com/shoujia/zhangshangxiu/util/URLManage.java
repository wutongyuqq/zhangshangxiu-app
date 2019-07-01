package com.shoujia.zhangshangxiu.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class URLManage {
	public final static String HOST = "https://www.baidu.com/";
	private static AsyncHttpClient client = new AsyncHttpClient(); 

	static {
		client.setTimeout(11000); 
	}

	public static void showInfos(String string, JsonHttpResponseHandler res) {
		String urlString = HOST + "s";
		RequestParams params = new RequestParams();
		params.put("wd", string);
		get(urlString, params, res);
	}

	private static void get(String urlString, RequestParams params, JsonHttpResponseHandler res) {
		 System.out.println((urlString + "?" + params.toString()));
		client.get(urlString, params, res);
	}
}
