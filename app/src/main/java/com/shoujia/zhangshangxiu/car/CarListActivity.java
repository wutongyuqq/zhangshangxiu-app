package com.shoujia.zhangshangxiu.car;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.shoujia.zhangshangxiu.order.ProjectOrderActivity;
import com.shoujia.zhangshangxiu.order.adapter.WxgzListAdapter;
import com.shoujia.zhangshangxiu.support.NavSupport;
import com.shoujia.zhangshangxiu.support.TabSupport;
import com.shoujia.zhangshangxiu.util.Constance;
import com.shoujia.zhangshangxiu.util.SharePreferenceManager;
import com.shoujia.zhangshangxiu.util.Util;

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
	SharePreferenceManager sp;
	TextView select_name;
	EditText cp_name;
	ImageView cp_search;
	RelativeLayout select_down;
	TextView jc_date_view;
	String cp;
	String orderStr;
	String assign;
	String states;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_car_list);
		sp = new SharePreferenceManager(this);
		initView();
		initData();

	}

	private void initView() {
		mInfoList = new ArrayList<>();
		navSupport = new NavSupport(this,3);
		mListview = findViewById(R.id.listview);
		select_name = findViewById(R.id.select_name);
		select_down = findViewById(R.id.select_down);
		jc_date_view = findViewById(R.id.jc_date_view);
		cp_name = findViewById(R.id.cp_name);
		cp_search = findViewById(R.id.cp_search);
		carListAdapter = new CarListAdapter(this,mInfoList);
		mListview.setAdapter(carListAdapter);
		select_name.setText(getIntent().getStringExtra("typeStr"));
		mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				ManageInfo info = mInfoList.get(i);
				sp.putString(Constance.JSD_ID,info.getJsd_id());
				sp.putString(Constance.CHEJIAHAO,info.getCjhm());
				sp.putString(Constance.CURRENTCP,info.getCp());
				sp.putString(Constance.CHEXING,info.getCx());
				sp.putString(Constance.JIECHEDATE,info.getJc_date());
				sp.putString(Constance.YUWANGONG,info.getYwg_date());
				startActivity(new Intent(CarListActivity.this,ProjectOrderActivity.class));
			}
		});
		select_down.setOnClickListener(this);
		jc_date_view.setOnClickListener(this);
		cp_search.setOnClickListener(this);
		cp_name.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				if(editable!=null) {
					cp = editable.toString();
				}
			}
		});
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
				//carListAdapter.setListData(mInfoList);
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
				mInfoList.clear();
				if(manageInfoList!=null&&manageInfoList.size()>0) {
					DBManager dbManager = DBManager.getInstanse(CarListActivity.this);
					dbManager.insertManagerListData(manageInfoList);
					mInfoList.addAll(manageInfoList);
				}
				mHandler.sendEmptyMessage(302);

			}
		});
	}


	public void setTittle(String title){
		navSupport.setTittle(title);
	}
    @Override
    public void onClick(View v) {
    	switch (v.getId()){
			case R.id.select_down:
				selectDown();
				break;
				case R.id.jc_date_view:
					String tag = (String) jc_date_view.getTag();
					if(tag.equals("0")) {
						DBManager dbManager = DBManager.getInstanse(CarListActivity.this);
						mInfoList.clear();
						orderStr = " jc_date desc";
						List<ManageInfo> manageInfos = dbManager.queryManagerList(cp, states, assign, orderStr);
						if(manageInfos!=null) {
							mInfoList.addAll(manageInfos);
						}
						carListAdapter.notifyDataSetChanged();
						jc_date_view.setText("按时间降序排列");
						jc_date_view.setTag("1");
					}else{
						DBManager dbManager = DBManager.getInstanse(CarListActivity.this);
						mInfoList.clear();
						orderStr = " jc_date asc";
						List<ManageInfo> manageInfos = dbManager.queryManagerList(cp, states, assign, orderStr);
						if(manageInfos!=null) {
							mInfoList.addAll(manageInfos);
						}
						carListAdapter.notifyDataSetChanged();
						jc_date_view.setText("按时间升序排列");
						jc_date_view.setTag("0");
					}
				break;
			case R.id.cp_search:
				searchCp();
				break;
			default:
				break;
		}
    }

	private void searchCp() {
    	if(cp_name.getText()!=null) {
			cp = cp_name.getText().toString().trim();
		}
		DBManager dbManager = DBManager.getInstanse(CarListActivity.this);
		mInfoList.clear();
		List<ManageInfo> manageInfos = dbManager.queryManagerList(cp, states, assign, orderStr);
		if(manageInfos!=null) {
			mInfoList.addAll(manageInfos);
		}
		carListAdapter.notifyDataSetChanged();

	}

	private void selectDown() {
    	List<String> strList = new ArrayList<>();
    	strList.add("估价中");
    	strList.add("待派工");
    	strList.add("待领工");
    	strList.add("修理中");
    	strList.add("待质检");
    	strList.add("待结算");
    	strList.add("待出厂");
		initPopWindow(strList);
	}



	private void initPopWindow(final List<String> strList){


		// 用于PopupWindow的View
		View contentView=LayoutInflater.from(this).inflate(R.layout.popwindow_bank_rate, null, false);
		ListView mListView = contentView.findViewById(R.id.listview);
		// 创建PopupWindow对象，其中：
		// 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
		// 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
		final PopupWindow mPopupWindow=new PopupWindow(contentView, Util.dp2px(this,100),
				LinearLayout.LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.showAsDropDown(select_down);
		mPopupWindow.setTouchable(true); // 设置屏幕点击事件


		WxgzListAdapter homeCarInfoAdapter = new WxgzListAdapter(this,strList);//新建并配置ArrayAapeter
		mListView.setAdapter(homeCarInfoAdapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
				mPopupWindow.dismiss();
				states = strList.get(position);
				select_name.setText(states);
				mInfoList.clear();
				carListAdapter.notifyDataSetChanged();
				getData(states);
			}
		});

		// 设置PopupWindow的背景
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		// 设置PopupWindow是否能响应外部点击事件
		mPopupWindow.setOutsideTouchable(true);
		// 设置PopupWindow是否能响应点击事件
		mPopupWindow.setTouchable(true);
		// 显示PopupWindow，其中：
		// 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移

		// 或者也可以调用此方法显示PopupWindow，其中：
		// 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
		// 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
		// window.showAtLocation(parent, gravity, x, y);
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
