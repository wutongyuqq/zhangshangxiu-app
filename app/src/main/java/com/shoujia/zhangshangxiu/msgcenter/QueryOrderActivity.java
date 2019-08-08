package com.shoujia.zhangshangxiu.msgcenter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseActivity;
import com.shoujia.zhangshangxiu.db.DBManager;
import com.shoujia.zhangshangxiu.dialog.DatePickerDialog;
import com.shoujia.zhangshangxiu.dialog.ProjectCancleDialog;
import com.shoujia.zhangshangxiu.entity.ManageInfo;
import com.shoujia.zhangshangxiu.entity.OrderBean;
import com.shoujia.zhangshangxiu.history.HistoryActivity;
import com.shoujia.zhangshangxiu.msgcenter.adapter.QueryOrderAdapter;
import com.shoujia.zhangshangxiu.msgcenter.help.QueryOrderDataHelper;
import com.shoujia.zhangshangxiu.order.ProjectOrderActivity;
import com.shoujia.zhangshangxiu.order.adapter.WxgzListAdapter;
import com.shoujia.zhangshangxiu.project.ProjectActivity;
import com.shoujia.zhangshangxiu.project.ProjectSelectActivity;
import com.shoujia.zhangshangxiu.project.help.ProjectDataHelper;
import com.shoujia.zhangshangxiu.support.InfoSupport;
import com.shoujia.zhangshangxiu.support.NavSupport;
import com.shoujia.zhangshangxiu.util.Constance;
import com.shoujia.zhangshangxiu.util.DateUtil;
import com.shoujia.zhangshangxiu.util.SharePreferenceManager;
import com.shoujia.zhangshangxiu.util.Util;
import com.shoujia.zhangshangxiu.view.CustomDatePicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/23 0023.
 * 首页
 */
public class QueryOrderActivity extends BaseActivity implements View.OnClickListener{
	private final String TAG = "ProjectOrderActivity";
  private NavSupport navSupport;
	List<OrderBean> mOrderBeans;
	List<OrderBean> mTotalBeans;
	QueryOrderAdapter mAdapter;
	private ListView listview;
	TextView select_date_start,select_date_end,select_gz;
	ImageView query_btn;
	EditText content;
	private int mQueryType;
	private SharePreferenceManager sp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_query_order);
		listview = findViewById(R.id.listview);
		select_date_start = findViewById(R.id.select_date_start);
		select_date_end = findViewById(R.id.select_date_end);
		query_btn = findViewById(R.id.query_btn);
		select_gz = findViewById(R.id.select_gz);
		content = findViewById(R.id.content);
		navSupport = new NavSupport(this,7);
		mOrderBeans = new ArrayList<>();
		mTotalBeans = new ArrayList<>();
		 mAdapter = new QueryOrderAdapter(this,mOrderBeans);
		listview.setAdapter(mAdapter);
		new NavSupport(this,18);
		sp = new SharePreferenceManager(this);
		initData();

		select_date_start.setOnClickListener(this);
		select_date_end.setOnClickListener(this);
		query_btn.setOnClickListener(this);
		select_gz.setOnClickListener(this);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				OrderBean bean = mOrderBeans.get(i);
				sp.putString(Constance.JSD_ID,bean.getJsd_id());
				sp.putString(Constance.CURRENTCP,bean.getCp());
				startActivity(new Intent(QueryOrderActivity.this,ProjectActivity.class));
				finish();
			}
		});
	}

	@Override
	protected void updateUIThread(int msgInt){
		if(msgInt==2){
			String gzStr = select_gz.getText().toString();
			if(mQueryType != 0){
					mOrderBeans.clear();
					for(OrderBean bean : mTotalBeans){
						if((bean.getWxgz_collect().contains(gzStr)||gzStr.equals("全部"))&&
								(content.getText()==null||TextUtils.isEmpty(content.getText().toString().trim())||
										bean.getCp().contains(content.getText().toString().trim()))){
							mOrderBeans.add(bean);
						}
					}
				mAdapter.notifyDataSetChanged();
			}else{

				mAdapter.notifyDataSetChanged();
			}
		}
	}

	//初始化数据
	private void initData(){
		mQueryType = 0;
		String endDate = DateUtil.getCurrentDate();

		String startDate = endDate.substring(0,endDate.length()-2)+"01";
		select_date_end.setText(endDate);
		select_date_start.setText(startDate);
		getListData();
	}


	private void getListData(){
		String startDate = select_date_start.getText().toString();
		String endDate = select_date_end.getText().toString();
		String startDateStr = startDate+" 00:00:00";
		String endDateStr = endDate+" 23:59:59";
		QueryOrderDataHelper helper = new QueryOrderDataHelper(this);
		helper.getListData(startDateStr,endDateStr,new QueryOrderDataHelper.GetDataListener() {
			@Override
			public void getData(List<OrderBean> orderBeans) {
				mTotalBeans = orderBeans;
				mOrderBeans.clear();
				mOrderBeans.addAll(orderBeans);
				mHandler.sendEmptyMessage(2);
			}
		});
	}


	private void selectDate(final TextView textView){
		CustomDatePicker customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
			@Override
			public void handle(String time) { // 回调接口，获得选中的时间
				Log.d("yyyyy", time);
				if(!TextUtils.isEmpty(time)&&time.length()>=10){
					String pickTime = time.substring(0,10);
					textView.setText(pickTime);
				}
			}
		},"2007-01-01 00:00","2025-12-31 00:00");
		customDatePicker.show();
	}


    @Override
    public void onClick(View v) {
    	switch (v.getId()){
			case R.id.select_date_start:
				selectDate(select_date_start);
				break;
			case R.id.select_date_end:
				selectDate(select_date_end);
				break;
			case R.id.query_btn:
				mQueryType = 1;
				getListData();
				break;
				case R.id.select_gz:
				initPopWindow();
				break;
            default:

                break;
		}
    }



	private void initPopWindow(){


		// 用于PopupWindow的View
		View contentView=LayoutInflater.from(this).inflate(R.layout.popwindow_bank_rate, null, false);
		ListView mListView = contentView.findViewById(R.id.listview);
		// 创建PopupWindow对象，其中：
		// 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
		// 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
		final PopupWindow mPopupWindow=new PopupWindow(contentView, Util.dp2px(this,120),
				LinearLayout.LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.showAsDropDown(select_gz);
		mPopupWindow.setTouchable(true); // 设置屏幕点击事件
		final DBManager dbManager = DBManager.getInstanse(this);
		final List<String> wxgzList = new ArrayList<>();

		wxgzList.add("全部");
		wxgzList.addAll(dbManager.queryWxgzListData());

		WxgzListAdapter homeCarInfoAdapter = new WxgzListAdapter(this,wxgzList);//新建并配置ArrayAapeter
		mListView.setAdapter(homeCarInfoAdapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
				mPopupWindow.dismiss();
				String info = wxgzList.get(position);
				select_gz.setText(info);
				mQueryType = 2;
				getListData();
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
