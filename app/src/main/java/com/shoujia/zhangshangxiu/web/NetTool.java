package com.shoujia.zhangshangxiu.web;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.shoujia.zhangshangxiu.web.util.LAVApi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class NetTool {
	private Context context;
	SharedPreferences shared_user_info;
	final String client_id="1056180385";//应用id
	/*private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				Toast.makeText(context, "打印成功", Toast.LENGTH_LONG).show();
			break;
			}
		};
	};*/
	public NetTool(Context context){
		this.context = context;
		shared_user_info = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
	}
	
	public NetTool(){
	}
	public  void print(final String content){
		String access_token = getTokenFromLocal(content);
		if(access_token.equals("")){
			getToken(content);
		}else{
			getPrint(access_token,content);
		}
		
	}
	
	private String getTokenFromZsx(String access_token){
		if(access_token!=null&&!access_token.equals("")){
			return access_token;
		}
		HttpClientService service = HttpClientService.getInstance();
		Map<String,Object> postMap = new HashMap<String,Object>();
		postMap.put("db", "sjsoft_SQL");
		postMap.put("function", "sp_fun_machine_access_token");
		postMap.put("data_source", shared_user_info.getString("Data_Source",""));//"首佳软件SQL");
		postMap.put("machine_code", shared_user_info.getString("machine_code",""));//"4004564459");
		postMap.put("access_token", access_token);
		String json = JsonUtil.mapTojson(postMap);
		String resJson = service.getDataFromZsx("http://121.43.148.193:5555/restful/pro", json);
		Map<String,Object> resMap = JsonUtil.jsToMap(resJson);
		
		String newTokenStr = resMap.get("machine_access_token")!=null? (String)resMap.get("machine_access_token"):"";
		
		if(newTokenStr.equals("")){
			
			String grant_type="client_credentials";
			long timestamp = System.currentTimeMillis()/1000;
			String sign=MD5.MD5Encode(client_id+timestamp+access_token).toLowerCase();//用户id
			String scope="all";//用户id
			String id=getUUID();
			String tokenStr = LAVApi.getToken(client_id, grant_type, sign, scope, timestamp+"", id);
			Map<String,Object> tkMap = JsonUtil.jsToMap(tokenStr);
			Map<String, Object> bodyMap = (Map<String, Object>) tkMap.get("body");
			final String tk_access_token = bodyMap!=null&&bodyMap.get("access_token")!=null?(String)bodyMap.get("access_token"):"";
			if(!tk_access_token.equals("")){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						sendTokenToServer(tk_access_token);
						
					}
				});
				
			}
			return tk_access_token;
		}else{
			return newTokenStr;
		}
	}

	private String getTokenFromZsx(String access_token,String machine_code,String data_source){
		if(access_token!=null&&!access_token.equals("")){
			return access_token;
		}
		HttpClientService service = HttpClientService.getInstance();
		Map<String,Object> postMap = new HashMap<String,Object>();
		postMap.put("db", "sjsoft_SQL");
		postMap.put("function", "sp_fun_machine_access_token");
		postMap.put("data_source", data_source);//"首佳软件SQL");
		postMap.put("machine_code", machine_code);//"4004564459");
		postMap.put("access_token", access_token);
		String json = JsonUtil.mapTojson(postMap);
		String resJson = service.getDataFromZsx("http://121.43.148.193:5555/restful/pro", json);
		Map<String,Object> resMap = JsonUtil.jsToMap(resJson);

		String newTokenStr = resMap.get("machine_access_token")!=null? (String)resMap.get("machine_access_token"):"";

		if(newTokenStr.equals("")){

			String grant_type="client_credentials";
			long timestamp = System.currentTimeMillis()/1000;
			String sign=MD5.MD5Encode(client_id+timestamp+access_token).toLowerCase();//用户id
			String scope="all";//用户id
			String id=getUUID();
			String tokenStr = LAVApi.getToken(client_id, grant_type, sign, scope, timestamp+"", id);
			Map<String,Object> tkMap = JsonUtil.jsToMap(tokenStr);
			Map<String, Object> bodyMap = (Map<String, Object>) tkMap.get("body");
			final String tk_access_token = bodyMap!=null&&bodyMap.get("access_token")!=null?(String)bodyMap.get("access_token"):"";
			if(!tk_access_token.equals("")){
				new Thread(new Runnable() {

					@Override
					public void run() {
						sendTokenToServer(tk_access_token);

					}
				});

			}
			return tk_access_token;
		}else{
			return newTokenStr;
		}
	}

	//发送token到首佳软件服务器
	private void sendTokenToServer(String access_token){
		HttpClientService service = HttpClientService.getInstance();
		Map<String,Object> postMap = new HashMap<String,Object>();
		postMap.put("db", "mycon1");
		postMap.put("function", "sp_fun_machine_access_token");
		/*postMap.put("Data_Source", "首佳软件SQL");
		postMap.put("machine_code", "4004564459");*/
		
		postMap.put("Data_Source", shared_user_info.getString("Data_Source",""));//"首佳软件SQL");
		postMap.put("machine_code", shared_user_info.getString("machine_code",""));//"4004564459");
		postMap.put("access_token", access_token);
		String json = JsonUtil.mapTojson(postMap);
		String resJson = service.getDataFromZsx("http://121.43.148.193:5555/restful/pro", json);
		System.out.println(resJson);
	}
	//从本地获取token
	public String getTokenFromLocal(final String content){
		SharedPreferences shared_user_info = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
		String access_token = shared_user_info.getString("access_token","");
		if(access_token.equals("")){
			access_token = getTokenFromZsx(access_token);
			return access_token;
		}else{
			return access_token;
		}
	}
	public  void getToken(final String content){
		String tokenUrl = "https://open-api.10ss.net/oauth/oauth";
		String access_token="a90188b91d2b34fb00c0b3c6473160f6";//用户id
		String grant_type="client_credentials";//
		String scope="all";//用户id
		long timestamp = System.currentTimeMillis()/1000;
		String id=getUUID();
		String sign=MD5.MD5Encode(client_id+timestamp+access_token).toLowerCase();//用户id
		Map<String,String> params=new HashMap<String,String>();
		params.put("client_id", client_id);
		params.put("grant_type", grant_type);
		params.put("scope", scope);
		params.put("timestamp", timestamp+"");
		params.put("id", id);
		params.put("sign", sign);
		params.put("refresh_token", "");
		HttpClientService service = HttpClientService.getInstance();
		service.getServerData(tokenUrl, params, new RfcDataListener() {

			@Override
			public void onSuccess(String strJson) {
				// TODO Auto-generated method stub
				Map<String,Object> resMap = JsonUtil.jsToMap(strJson);
				if(resMap!=null && resMap.get("body")!=null) {
					Map<String, Object> bodyMap = (Map<String, Object>) resMap.get("body");
					String access_token = bodyMap.get("access_token")!=null?(String)bodyMap.get("access_token"):"";
					String refresh_token = bodyMap.get("refresh_token")!=null?(String)bodyMap.get("refresh_token"):"";
					String machine_code = bodyMap.get("machine_code")!=null?(String)bodyMap.get("machine_code"):"";
					shared_user_info.edit().putString("access_token",access_token).commit();
					shared_user_info.edit().putString("refresh_token",refresh_token).commit();
					getPrint(access_token,content);
					sendTokenToServer(access_token);
				}
			}
			@Override
			public void onFail(String msg) {
				// TODO Auto-generated method stub
				System.out.print(msg);
			}
		});

	}

	public  void getPrint(final String access_token, final String content){
		String url = "https://open-api.10ss.net/printer/addprinter";
		final String userKey="a90188b91d2b34fb00c0b3c6473160f6";//用户id
		final String machine_code=shared_user_info.getString("machine_code","");//"4004564459";//打印机终端号
		final String msign=shared_user_info.getString("msign","");//"66jubixni6j4";//打印机秘钥
		final String origin_id = System.currentTimeMillis()+"";
		final long timestamp = System.currentTimeMillis()/1000;
		final String id=getUUID();
		final String sign=MD5.MD5Encode(client_id+timestamp+userKey);//用户id
		try{
			Map<String,String> params=new HashMap<String,String>();
			params.put("client_id", client_id);
			params.put("access_token", access_token);
			params.put("machine_code", machine_code);
			params.put("msign", msign);
			params.put("timestamp", timestamp+"");
			params.put("id", id);
			params.put("sign", sign.toLowerCase());

			HttpClientService service = HttpClientService.getInstance();
			service.getServerData(url, params, new RfcDataListener() {

				@Override
				public void onSuccess(String msg) {
					
					System.out.println(msg);
					//sendContent(getContent(),access_token);
					String resStr = LAVApi.print(client_id,access_token,machine_code,content,origin_id,sign,id,timestamp+"");
					Map<String,Object> latMap=JsonUtil.jsToMap(resStr);
					if(latMap!=null && latMap.get("error")!=null){
						String errorStr = (String) latMap.get("error");
						if(errorStr.equals("0")){
							//handler.sendEmptyMessage(0);
						}
					}
				}

				@Override
				public void onFail(String msg) {
					// TODO Auto-generated method stub

				}
			});

		}catch(Exception e){
			e.printStackTrace();

		}


	}
	public Map<String,Object> getVesionInfoFromServer(){
	
		HttpClientService service = HttpClientService.getInstance();
		Map<String,Object> postMap = new HashMap<String,Object>();
		postMap.put("db", "sjsoft_SQL");
		postMap.put("function", "sp_fun_check_update");
		postMap.put("os", "android");
		//postMap.put("update_date", getCurTime());//"4004564459");
		
		String json = JsonUtil.mapTojson(postMap);
		String resJson = service.getDataFromZsx("http://121.43.148.193:5555/restful/pro", json);
		Map<String,Object> resMap = JsonUtil.jsToMap(resJson);
		return resMap;
	}
	
	
	public Map<String,Object> getWxInfoFromServer(String id, String aSet, String nonce){
		HttpClientService service = HttpClientService.getInstance();
		if(id==null){
			id="";
		}
		if(aSet==null){
			aSet="";
		}
		if(nonce==null){
			nonce="";
		}
		String resJson = service.getDataFromWx("http://wxgzh.whsjsoft.com/wx/api/push?id="+id+"&aSet="+aSet+"&nonce="+nonce);
		Map<String,Object> resMap = JsonUtil.jsToMap(resJson);
		return resMap;
	}
	
	
	public Map<String,Object> getTicket(String token, String usercode){
		HttpClientService service = HttpClientService.getInstance();
		Map<String,Object> postMap = new HashMap<String,Object>();
		postMap.put("expire_seconds", 4800);
		postMap.put("action_name", "QR_STR_SCENE");
		
		postMap.put("action_info", "{'scene': {'scene_str': '"+usercode+"'}}");
		String json = "{\"expire_seconds\": 4800, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": "+usercode+"}}}";
		String resJson = service.getDataFromZsx("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+token, json);
		Map<String,Object> resMap = JsonUtil.jsToMap(resJson);
		return resMap;
	}
	
	public Bitmap getImageCode(String ticket){
		HttpClientService service = HttpClientService.getInstance();
		String newticket = "";
		try {
			newticket = URLEncoder.encode(ticket, "utf-8");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap bitmap = HttpClientService.getQRcode("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+newticket);
		return bitmap;
	}
	
	

	
	private String getUUID(){
		String uuid = UUID.randomUUID().toString().toUpperCase();
		return uuid;
	}
	
	private String getCurTime(){
		//获取当前时间到毫秒值
        Date d = new Date();
        System.out.println("当前时间为:" + d);
        //创建日期格式化对象(把日期转成字符串)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(d);
        return str;
	}
}
