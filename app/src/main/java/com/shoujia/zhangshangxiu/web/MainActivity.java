package com.shoujia.zhangshangxiu.web;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.web.util.WaitTool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends Activity {
	WebView webView;
	private static Boolean isQuit = false;
	private long mExitTime = 0;
	private List<String> list = new ArrayList();
	public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType
			.parse("text/x-markdown; charset=utf-8");
	private static String BaseURL;
	private static boolean isInit = false;
	private Context context;
	ProgressDialog pBar;


	int progressInt = 0;
	String apkPath = "";
	String paramsJSON, xmListJson, pjDataListJson;
	private String SDPath = "/mnt/sdcard/zsx/";
	private String[] logins = new String[2];
	Timer timer = new Timer();
	SharedPreferences shared_user_info;
	String saveDir = "";
	Handler myHandler;

	/**
	 * 声明一个静态的Handler内部类，并持有外部类的弱引用
	 */
	private class MyHandler extends Handler {
		private final WeakReference<MainActivity> mActivty;

		private MyHandler(MainActivity mActivty) {
			this.mActivty = new WeakReference<MainActivity>(mActivty);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			MainActivity activity = mActivty.get();
			if (activity != null) {
				switch (msg.what) {
				case 3:// 47.106.108.87
					/*findViewById(R.id.image_tv).setVisibility(View.GONE);
					findViewById(R.id.web).setVisibility(View.VISIBLE);*/
					//webView.loadUrl("http://47.106.108.87:3000/src/index.html#/home");// 10.8.28.153
					break;
				case 2:
					Toast.makeText(context, "服务异常", Toast.LENGTH_LONG).show();
					break;
				case 4:
					down();
					break;
				case 5:
					pBar.setProgress(progressInt);
					break;
				case 6:
					Toast.makeText(context, "文件下载失败", Toast.LENGTH_LONG).show();
					break;
				case 9:
					WaitTool.dismissDialog();
					break;
				case 11:
					WaitTool.showDialog(context);
					break;
				case 2001:
					MyHandleBean bean = (MyHandleBean) msg.obj;
					Intent intent = new Intent(MainActivity.this,SecondActivity.class);
					intent.putExtra("id", bean.id);
					intent.putExtra("aSet", bean.aSet);
					intent.putExtra("nonce", bean.nonce);
					intent.putExtra("usercode", bean.usercode);
					startActivity(intent);
					break;
				}

			}
		}
	}

	@JavascriptInterface
	public void saveData(String Data_Source, String machine_code, String msign) {

		shared_user_info.edit().putString("Data_Source", Data_Source).commit();
		shared_user_info.edit().putString("machine_code", machine_code)
				.commit();
		shared_user_info.edit().putString("msign", msign).commit();
		printData(paramsJSON, xmListJson, pjDataListJson);

	}

	@JavascriptInterface
	public void saveDataForLogin(String Data_Source, String machine_code,
                                 String msign) {

		shared_user_info.edit().putString("Data_Source", Data_Source).commit();
		shared_user_info.edit().putString("machine_code", machine_code)
				.commit();
		shared_user_info.edit().putString("msign", msign).commit();

	}

	@JavascriptInterface
	public void print(final String paramsJSON, final String xmListJson,
                      final String pjDataListJson) {
		printData(paramsJSON, xmListJson, pjDataListJson);
	}

	private void printData(String paramsJSON, String xmListJson,
                           String pjDataListJson) {

		Map<String, Object> paramMap = JsonUtil.jsToMap(paramsJSON);
		List<Map<String, Object>> xmList = JsonUtil.jsToList(xmListJson);
		List<Map<String, Object>> pjDataList = JsonUtil
				.jsToList(pjDataListJson);
		if (paramMap == null) {
			Toast.makeText(context, "数据异常，请重新操作！", Toast.LENGTH_LONG).show();
			return;
		}
		String jsd_id = paramMap.get("jsd_id") != null ? (String) paramMap
				.get("jsd_id") : "";
		String ticheTime = paramMap.get("ticheTime") != null ? (String) paramMap
				.get("ticheTime") : "";
		String company_name = paramMap.get("company_name") != null ? (String) paramMap
				.get("company_name") : "";
		String cjhm = paramMap.get("cjhm") != null ? (String) paramMap
				.get("cjhm") : "";
		String cz = paramMap.get("cz") != null ? (String) paramMap.get("cz")
				: "";
		String cp = paramMap.get("cp") != null ? (String) paramMap.get("cp")
				: "";
		String cx = paramMap.get("cx") != null ? (String) paramMap.get("cx")
				: "";
		String jclc = paramMap.get("jclc") != null ? (String) paramMap
				.get("jclc") : "";
		String car_fault = paramMap.get("car_fault") != null ? (String) paramMap
				.get("car_fault") : "";
		String totalsl = paramMap.get("totalsl") != null ? (String) paramMap
				.get("totalsl") : "";
		String totalMoney = paramMap.get("totalMoney") != null ? (String) paramMap
				.get("totalMoney") : "";
		String yszje = paramMap.get("yszje") != null ? (String) paramMap
				.get("yszje") : "";
		String address = paramMap.get("address") != null ? (String) paramMap
				.get("address") : "";
		String telphone = paramMap.get("telphone") != null ? (String) paramMap
				.get("telphone") : "";
		String jc_date = paramMap.get("jc_date") != null ? (String) paramMap
				.get("jc_date") : "";
		String memo = paramMap.get("memo") != null ? (String) paramMap
				.get("memo") : "";
		String dyTime = paramMap.get("dyTime") != null ? (String) paramMap
				.get("dyTime") : "";
		String totalXlf = paramMap.get("totalXlf") != null ? (String) paramMap
				.get("totalXlf") : "";
		String totalZk = paramMap.get("totalZk") != null ? (String) paramMap
				.get("totalZk") : "";
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
				Map<String, Object> temp = xmList.get(i);
				String wxgz = temp.get("wxgz") != null ? temp.get("wxgz")
						.toString() : "";
				String xlf = temp.get("xlf") != null ? temp.get("xlf")
						.toString() : "";
				String zk = temp.get("zk") != null ? temp.get("zk").toString()
						: "";
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
				Map<String, Object> temp = pjDataList.get(i);
				String pjmc = temp.get("pjmc") != null ? temp.get("pjmc")
						.toString() : "";
				String sl = temp.get("sl") != null ? temp.get("sl").toString()
						: "";
				String ssj = temp.get("ssj") != null ? temp.get("ssj")
						.toString() : "";
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_main);
		context = this;
		//webView = (WebView) findViewById(R.id.web);
		webView.addJavascriptInterface(this, "printdata");
		shared_user_info = context.getSharedPreferences("user_info",
				MODE_PRIVATE);
		myHandler = new MyHandler(this);
		init();

		try {
			getData();
		} catch (Exception e) {
			Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					Thread.sleep(3000);
					myHandler.sendEmptyMessage(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	public void getData() throws RuntimeException {

	}

	private class MyWebViewClient extends WebViewClient {

		// 步骤3. 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示

		@Override
		public void onPageFinished(WebView view, String url) {
			myHandler.sendEmptyMessage(11);
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(300);
						myHandler.sendEmptyMessage(9);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}).start();
			super.onPageFinished(view, url);

		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			WaitTool.showDialog(context);
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
			// super.onReceivedSslError(view, handler, error);
			handler.proceed();
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			WaitTool.showDialog(context);
			if (url.contains("login") && !isInit) {
				logins[0] = "login";
				isInit = true;
			}
			if (url.contains("home")) {
				if ("login".equals(logins[0]) && isInit) {
					webView.clearHistory(); // 清除
					logins = new String[2];
				}
			}
			view.loadUrl(url);

			return true;
		}

	}

	private void init() {
		// 声明WebSettings子类
		WebSettings webSettings = webView.getSettings();

		// 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
		webSettings.setJavaScriptEnabled(true);

		// 设置自适应屏幕，两者合用
		webSettings.setUseWideViewPort(true); // 将图片调整到适合webview的大小
		webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

		// 缩放操作
		webSettings.setSupportZoom(false); // 支持缩放，默认为true。是下面那个的前提。
		webSettings.setBuiltInZoomControls(false); // 设置内置的缩放控件。若为false，则该WebView不可缩放
		webSettings.setDisplayZoomControls(true); // 隐藏原生的缩放控件

		// 其他细节操作
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); // 关闭webview中缓存
		webSettings.setAllowFileAccess(true); // 设置可以访问文件
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过JS打开新窗口
		webSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片
		webSettings.setDefaultTextEncodingName("utf-8");// 设置编码格式

		// 优先使用缓存:
		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		// 缓存模式如下：
		// LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
		// LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
		// LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
		// LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。

		if (NetStatusUtil.isNetworkAvailable(getApplicationContext())) {
			webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);// 根据cache-control决定是否从网络上取数据。
		} else {
			webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 没网，则从本地获取，即离线加载
		}

		webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
		webSettings.setDatabaseEnabled(true); // 开启 database storage API 功能
		webSettings.setAppCacheEnabled(true);// 开启 Application Caches 功能

		String cacheDirPath = getFilesDir().getAbsolutePath() + "/cache";
		webSettings.setAppCachePath(cacheDirPath); // 设置 Application Caches 缓存目录
		webView.setWebViewClient(new MyWebViewClient());

	}

	// 点击返回上一页面而不是退出浏览器
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (webView.getUrl().contains("login")) {
			System.exit(0);// 否则退出程序
			return true;
		}
		if (!webView.getUrl().contains("home")) {
			if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
				webView.goBack();
				return true;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {//
				// 如果两次按键时间间隔大于2000毫秒，则不退出
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();// 更新mExitTime
			} else {
				System.exit(0);// 否则退出程序
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		if (webView != null) {
			webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
			webView.clearHistory();
			((ViewGroup) webView.getParent()).removeView(webView);
			webView.destroy();
			webView = null;
		}
		super.onDestroy();
	}

	@JavascriptInterface
	public void getWxInfoFromServer(String id, String aSet, String nonce,
                                    String usercode) {
		MyHandleBean bean = new MyHandleBean(id, aSet, nonce, usercode);
		Message msg = new Message();
		msg.obj=bean;
		msg.what=2001;
		myHandler.sendMessage(msg);
	}

	@JavascriptInterface
	public void checkVesion() {
		NetTool netTool = new NetTool();
		Map<String, Object> jsonMap = netTool.getVesionInfoFromServer();
		if (jsonMap != null) {
			String stateStr = jsonMap.get("state") != null ? (String) jsonMap
					.get("state") : "";
			String updateStr = jsonMap.get("online_update") != null ? (String) jsonMap
					.get("online_update") : "";
			String download_url = jsonMap.get("download_url") != null ? (String) jsonMap
					.get("download_url") : "";
			if (stateStr.equals("ok") && updateStr.equals("YES")
					&& !download_url.equals("")) {
				String versionServer = jsonMap.get("version") != null ? (String) jsonMap
						.get("version") : "";
				if (isNeedUpdate(versionServer)) {
					String update_log = jsonMap.get("update_log") != null ? (String) jsonMap
							.get("update_log") : "";
					showUpdateDialog(context, update_log, download_url);
				} else {
					Toast.makeText(context, "已是最新版本，无需更新", Toast.LENGTH_LONG)
							.show();
				}
			} else {
				if (stateStr.equals("ok")) {
					String msg = jsonMap.get("msg") != null ? (String) jsonMap
							.get("msg") : "";
					Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(context, "服务异常", Toast.LENGTH_LONG).show();
				}

			}
		} else {
			Toast.makeText(context, "已是最新版本，无需更新", Toast.LENGTH_LONG).show();
		}
	}

	public String getAppVersionName(Context context) {
		String versionName = "";
		int versionCode = 0;
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			versionCode = pi.versionCode;

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (versionName == null || versionName.length() <= 0) {
			versionName = "";
		}

		return versionName;
	}

	// 弹出升级框
	private void showUpdateDialog(Context context, String tipMsg,
                                  final String url) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		apkPath = url;
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("更新版本");
		builder.setMessage(tipMsg);
		builder.setCancelable(false);

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					downFile(apkPath);
				} else {
					Toast.makeText(MainActivity.this, "SD卡不可用，请插入SD卡",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}

		});
		builder.create().show();
	}

	private boolean isNeedUpdate(String version) {
		String oldVersion = getVersion();
		if (oldVersion.contains(".") && version.contains(".")) {
			oldVersion = oldVersion.replaceAll("\\.", "");
			version = version.replaceAll("\\.", "");
			long oldVersionLong = Long.parseLong(oldVersion);
			long versionLong = Long.parseLong(version);
            return versionLong > oldVersionLong;
		} else {
			return false;
		}

	}

	// 获取当前版本的版本号
	private String getVersion() {
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);

			Log.d("TAK", "packageInfo.versionName" + packageInfo.versionName);
			return packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return "";

		}
	}

	void downFile(final String url) {
		Log.d("TSK", "url" + apkPath);
		pBar = new ProgressDialog(MainActivity.this); // 进度条，在下载的时候实时更新进度，提高用户友好度
		pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pBar.setTitle("正在下载");
		pBar.setMessage("请稍候...");
		pBar.setProgress(0);
		pBar.show();
		final String videoUrl = apkPath;
		saveDir = Environment.getExternalStorageDirectory() + "/abc";
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				download(videoUrl);
			}
		}).start();

	}

	void down() {
		myHandler.post(new Runnable() {
			public void run() {
				pBar.cancel();
				update();
			}
		});
	}

	void update() {
		/*
		 * Intent intent = new Intent(Intent.ACTION_VIEW);
		 * intent.setDataAndType(Uri.fromFile(new File(downLoadPath)),
		 * "application/vnd.android.package-archive"); startActivity(intent);
		 */

		String fileName = saveDir + "/zhangshangxiu.apk";
		File apkfile = new File(fileName);
		if (apkfile.exists()) {
			/*
			 * Intent intent = new Intent(Intent.ACTION_VIEW);
			 * intent.setDataAndType(Uri.fromFile(new File(fileName)),
			 * "application/vnd.android.package-archive");
			 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 * context.startActivity(intent);
			 * android.os.Process.killProcess(android.os.Process.myPid());
			 */
			Intent intent = new Intent(Intent.ACTION_VIEW);
			if (Build.VERSION.SDK_INT >= 24) { // 适配安卓7.0
				intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				Uri apkFileUri = FileProvider.getUriForFile(
						context.getApplicationContext(),
						context.getPackageName() + ".fileprovider", apkfile);
				intent.setDataAndType(apkFileUri,
						"application/vnd.android.package-archive");
			} else {
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setDataAndType(
						Uri.parse("file://" + apkfile.toString()),
						"application/vnd.android.package-archive");// File.toString()会返回路径信息
			}
			startActivity(intent);
		}

	}

	public void downloadFile1(final String url) {
		try {
			// 下载路径，如果路径无效了，可换成你的下载路径

			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath();

			final long startTime = System.currentTimeMillis();
			Log.i("DOWNLOAD", "startTime=" + startTime);
			// 下载函数
			String filename = url.substring(url.lastIndexOf("/") + 1);
			// 获取文件名
			URL myURL = new URL(url);
			URLConnection conn = myURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			int fileSize = conn.getContentLength();// 根据响应获取文件大小
			if (fileSize <= 0)
				throw new RuntimeException("无法获知文件大小 ");
			if (is == null)
				throw new RuntimeException("stream is null");
			File file1 = new File(path);
			if (!file1.exists()) {
				file1.mkdirs();
			}
			// 把数据存入路径+文件名
			FileOutputStream fos = new FileOutputStream(path + "/" + filename);
			byte buf[] = new byte[1024];

			/*
			 * do{ //循环读取 int numread = is.read(buf); if (numread == -1) {
			 * break; } fos.write(buf, 0, numread); downLoadFileSize += numread;
			 * //更新进度条 } while (true);
			 */

			long sum = 0;

			long total = 0;
			int current = 0;
			boolean isUploading = false;
			int len = 0;
			while ((len = is.read(buf)) != -1) {
				fos.write(buf, 0, len);
				sum += len;
				int progress = (int) (sum * 1.0f / total * 100);
				Message msg = new Message();
				// 下载中
				progressInt = progress;
				myHandler.sendEmptyMessage(5);

			}

			Log.i("DOWNLOAD", "download success");
			Log.i("DOWNLOAD", "totalTime="
					+ (System.currentTimeMillis() - startTime));

			is.close();
		} catch (Exception ex) {
			Log.e("DOWNLOAD", "error: " + ex.getMessage(), ex);
			myHandler.sendEmptyMessage(6);
		}
	}

	public void download(final String url) {
		File file = new File(SDPath + "zhangshangxiu.apk");
		if (file.exists()) {
			file.delete();
		}

		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url(url).build();
			client.newCall(request).enqueue(new Callback() {

				@Override
				public void onResponse(Call arg0, Response response) {
					long total = 0;
					int current = 0;
					boolean isUploading = false;
					int len = 0;
					InputStream is = null;
					byte[] buf = new byte[2048];
					FileOutputStream fos = null;

					File fileDir = new File(saveDir);

					try {
						if (!fileDir.exists() || !fileDir.isDirectory()) {
							fileDir.mkdirs();
						}
						is = response.body().byteStream();
						total = response.body().contentLength();

						File file = new File(saveDir, "zhangshangxiu.apk");
						fos = new FileOutputStream(file);
						long sum = 0;
						while ((len = is.read(buf)) != -1) {
							fos.write(buf, 0, len);
							sum += len;
							int progress = (int) (sum * 1.0f / total * 100);
							Message msg = new Message();
							// 下载中
							progressInt = progress;
							myHandler.sendEmptyMessage(5);

						}
						fos.flush();
						Message msg = new Message();

						fos.flush();
						// 下载完成
						myHandler.sendEmptyMessage(4);
					} catch (Exception e) {
						e.printStackTrace();
						myHandler.sendEmptyMessage(6);
					}
				}

				@Override
				public void onFailure(Call arg0, IOException arg1) {
					myHandler.sendEmptyMessage(6);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			myHandler.sendEmptyMessage(6);
		}
	}

	private String isExistDir(String saveDir) throws IOException {
		File downloadFile = new File(saveDir);
		if (!downloadFile.mkdirs()) {
			downloadFile.createNewFile();
		}
		String savePath = downloadFile.getAbsolutePath();
		return savePath;
	}

	private String getNameFromUrl(String url) {
		return url.substring(url.lastIndexOf("/") + 1);
	}
	
	private class MyHandleBean{
		public String id;
		public String aSet;
		public String nonce;
		public String usercode;
		public MyHandleBean(String id, String aSet, String nonce,
                            String usercode) {
			super();
			this.id = id;
			this.aSet = aSet;
			this.nonce = nonce;
			this.usercode = usercode;
		}
		
	}

}
