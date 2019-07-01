package com.shoujia.zhangshangxiu.web;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

import com.shoujia.zhangshangxiu.R;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.Map;

public class SecondActivity extends Activity {
	
	private ImageView codeImage,backBtn;
	Bitmap mBitmap;
	MyHandler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_second);
		backBtn = findViewById(R.id.back_btn);
		codeImage = findViewById(R.id.code_image);
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}
	
	private void init() {
		handler = new MyHandler(this);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				getData();
			}
		}).start();
		
	}
	
	private void getData(){
		
		try{
			String id = getIntent().getStringExtra("id");
			String aSet = getIntent().getStringExtra("aSet");
			String nonce = getIntent().getStringExtra("nonce");
			String usercode = getIntent().getStringExtra("usercode");
			NetTool netTool = new NetTool();
			Map<String, Object> jsonMap = netTool.getWxInfoFromServer(id,aSet,nonce);
			String access_token = (String)jsonMap.get("access_token");
			Map<String, Object> jsonMap2 = netTool.getTicket(access_token,usercode);
			if(jsonMap2.get("ticket")!=null){
				String ticket = (String) jsonMap2.get("ticket");
				mBitmap = netTool.getImageCode(ticket);
				handler.sendEmptyMessage(3);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	/**
	 * 声明一个静态的Handler内部类，并持有外部类的弱引用
	 */
	private class MyHandler extends Handler {
		private final WeakReference<SecondActivity> mActivty;

		private MyHandler(SecondActivity mActivty) {
			this.mActivty = new WeakReference<SecondActivity>(mActivty);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			SecondActivity activity = mActivty.get();
			if (activity != null) {
				switch (msg.what) {
				case 3:// 47.106.108.87
					if(mBitmap!=null){
						codeImage.setImageBitmap(mBitmap);
					}
					break;
					default:
						break;
						
				}
			}
		}
	}
}
