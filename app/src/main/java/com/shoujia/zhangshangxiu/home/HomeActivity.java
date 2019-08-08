package com.shoujia.zhangshangxiu.home;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.db.DBManager;
import com.shoujia.zhangshangxiu.dialog.DatePickerDialog;
import com.shoujia.zhangshangxiu.entity.CarInfo;
import com.shoujia.zhangshangxiu.entity.FirstIconInfo;
import com.shoujia.zhangshangxiu.entity.RepairInfo;
import com.shoujia.zhangshangxiu.entity.SecondIconInfo;
import com.shoujia.zhangshangxiu.home.help.HomeDataHelper;
import com.shoujia.zhangshangxiu.support.NavSupport;
import com.shoujia.zhangshangxiu.support.TabSupport;
import com.shoujia.zhangshangxiu.view.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/2/23 0023.
 * 首页
 */
public class HomeActivity extends FragmentActivity implements View.OnClickListener{
	private final String TAG = "HomeActivity";
  private NavSupport navSupport;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);

		new TabSupport(this);
		navSupport = new NavSupport(this,1);
		//initData();
		final RelativeLayout root_view = findViewById(R.id.root_view);
		final RelativeLayout rl_bottom = findViewById(R.id.rl_bottom);
		final LinearLayout ll_top_title = findViewById(R.id.ll_top_title);
		LinearLayout fragment_tab = findViewById(R.id.fragment_tab);
		getSupportFragmentManager()    //
				.beginTransaction()
				.add(R.id.fragment_tab,new HomeZsxFragment())   // 此处的R.id.fragment_container是要盛放fragment的父容器
				.commit();

		root_view.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener(){
					@Override
					public void onGlobalLayout()
					{

						int heightDiff = root_view.getRootView().getHeight() - root_view.getHeight();


						if (heightDiff > 100)
						{ // 说明键盘是弹出状态
							Log.v(TAG, "键盘弹出状态");
							rl_bottom.setVisibility(View.GONE);
							ll_top_title.setVisibility(View.GONE);
						} else{
							Log.v(TAG, "键盘收起状态");
							rl_bottom.postDelayed(new Runnable() {
								@Override
								public void run() {
									rl_bottom.setVisibility(View.VISIBLE);
									ll_top_title.setVisibility(View.VISIBLE);
								}
							},100);

						}
					}
				});

	}



	public void setTittle(String title){
		navSupport.setTittle(title);
	}
    @Override
    public void onClick(View v) {
    }



	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	long mLastPress = 0;

	@Override
	public void onBackPressed() {
    	long curTimes = System.currentTimeMillis();
    	if(curTimes-mLastPress>2000){
			mLastPress = System.currentTimeMillis();
			Toast.makeText(this,"再按一次退出应用",Toast.LENGTH_SHORT).show();
		}else {
			super.onBackPressed();
		}

	}

	@Override
	public void onResume() {
		super.onResume();



	}
}
