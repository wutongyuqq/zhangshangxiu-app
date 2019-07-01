package com.shoujia.zhangshangxiu.car;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseActivity;
import com.shoujia.zhangshangxiu.car.adapter.CarListAdapter;
import com.shoujia.zhangshangxiu.car.help.CarDataHelper;
import com.shoujia.zhangshangxiu.db.DBManager;
import com.shoujia.zhangshangxiu.entity.CarInfo;
import com.shoujia.zhangshangxiu.entity.FirstIconInfo;
import com.shoujia.zhangshangxiu.entity.ManageInfo;
import com.shoujia.zhangshangxiu.entity.RepairInfo;
import com.shoujia.zhangshangxiu.entity.SecondIconInfo;
import com.shoujia.zhangshangxiu.home.HomeZsxFragment;
import com.shoujia.zhangshangxiu.home.help.HomeDataHelper;
import com.shoujia.zhangshangxiu.support.NavSupport;
import com.shoujia.zhangshangxiu.support.TabSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/23 0023.
 * 首页
 */
public class CarListActivity extends BaseActivity implements View.OnClickListener{
	private final String TAG = "HomeActivity";
  private NavSupport navSupport;
  private List<ManageInfo> mInfoList;
  private CarListAdapter carListAdapter;
	ListView mListview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_car_list);
		initView();
		initData();

	}

	private void initView() {
		mInfoList = new ArrayList<>();
		navSupport = new NavSupport(this,3);
		mListview = findViewById(R.id.listview);
		carListAdapter = new CarListAdapter(this,mInfoList);
		mListview.setAdapter(carListAdapter);
	}

	//初始化数据
	private void initData(){
    	String typeStr = getIntent().getStringExtra("typeStr");
    	getData(typeStr);
	}

	@Override
	protected void updateUIThread(int msgInt) {
		super.updateUIThread(msgInt);
		if(mInfoList!=null&&mInfoList.size()>0){
			if(msgInt==302){
				carListAdapter.setListData(mInfoList);
				carListAdapter.notifyDataSetChanged();

			}
		}
	}

	private void getData(String chooseName){
		CarDataHelper carDataHelper = new CarDataHelper(this);
		carDataHelper.setPreZero();
		carDataHelper.getCardList(chooseName, new CarDataHelper.GetDataListener() {
			@Override
			public void getData(List<ManageInfo> manageInfoList) {
				mInfoList = manageInfoList;
				mHandler.sendEmptyMessage(302);

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

	@Override
	public void onResume() {
		super.onResume();
	}
}
