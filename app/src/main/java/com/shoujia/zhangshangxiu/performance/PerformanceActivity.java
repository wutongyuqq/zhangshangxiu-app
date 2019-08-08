package com.shoujia.zhangshangxiu.performance;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseActivity;
import com.shoujia.zhangshangxiu.db.DBManager;
import com.shoujia.zhangshangxiu.dialog.ProjectCancleDialog;
import com.shoujia.zhangshangxiu.entity.JiedaiBaseInfo;
import com.shoujia.zhangshangxiu.entity.JixiaoBaseInfo;
import com.shoujia.zhangshangxiu.entity.RepairInfo;
import com.shoujia.zhangshangxiu.entity.ShigongBaseInfo;
import com.shoujia.zhangshangxiu.history.HistoryActivity;
import com.shoujia.zhangshangxiu.http.HttpClient;
import com.shoujia.zhangshangxiu.http.IGetDataListener;
import com.shoujia.zhangshangxiu.performance.adapter.SaleProAdapter;
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
public class PerformanceActivity extends BaseActivity implements View.OnClickListener{
	private final String TAG = "PerformanceActivity";
	private SharePreferenceManager sp;
	TextView select_date_start,select_date_end,tv_base_info,tv_more_info;
	ImageView query_btn;
	LinearLayout xiaoshou_page,shigong_page,pro_list,shigong_list;
	JixiaoBaseInfo mJixiaoBaseInfo;
	JiedaiBaseInfo mJidaiBaseInfo;
	List<JiedaiBaseInfo> mJidaiBaseInfoList;
	List<JiedaiBaseInfo> mJidaiBaseInfoList2;
	ShigongBaseInfo mShigongBaseInfo;
	ListView pro_listview,shigong_listview;
	SaleProAdapter mProAdapter;
	SaleProAdapter mProAdapter2;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_performance);
		new NavSupport(this,8);
		sp = new SharePreferenceManager(this);
		initView();
		initData();

	}

	private void initView() {
		select_date_start = findViewById(R.id.select_date_start);
		select_date_end = findViewById(R.id.select_date_end);
		query_btn = findViewById(R.id.query_btn);
		tv_base_info = findViewById(R.id.tv_base_info);
		tv_more_info = findViewById(R.id.tv_more_info);
		xiaoshou_page = findViewById(R.id.xiaoshou_page);
		shigong_page = findViewById(R.id.shigong_page);
		pro_list = findViewById(R.id.pro_list);
		shigong_list = findViewById(R.id.shigong_list);
		pro_listview  = findViewById(R.id.pro_listview);
		shigong_listview = findViewById(R.id.shigong_listview);

		select_date_start.setOnClickListener(this);
		select_date_end.setOnClickListener(this);
		query_btn.setOnClickListener(this);
		tv_base_info.setOnClickListener(this);
		tv_more_info.setOnClickListener(this);

	}

	//初始化数据
	private void initData(){
		mJidaiBaseInfoList = new ArrayList<>();
        mJidaiBaseInfoList2 = new ArrayList<>();
		mProAdapter = new SaleProAdapter(this,mJidaiBaseInfoList);
		mProAdapter2 = new SaleProAdapter(this,mJidaiBaseInfoList2);
		pro_listview.setAdapter(mProAdapter);
		shigong_listview.setAdapter(mProAdapter2);
		getData();
	}

	@Override
	protected void updateUIThread(int msgInt) {
		super.updateUIThread(msgInt);
		if(msgInt==101){
			TextView shigong_level = findViewById(R.id.shigong_level);
			TextView sale_level = findViewById(R.id.sale_level);
			shigong_level.setText(mJixiaoBaseInfo.repair_achievement);
			sale_level.setText(mJixiaoBaseInfo.sale_achievement);
		}else if(msgInt==102){

			TextView jdcc = findViewById(R.id.jdcc);
			jdcc.setText(mJidaiBaseInfo.service_time);
			TextView xsxms = findViewById(R.id.xsxms);
			xsxms.setText(mJidaiBaseInfo.sale_time);
			TextView xszje = findViewById(R.id.xszje);
			xszje.setText(mJidaiBaseInfo.sale_money);

			TextView sslr = findViewById(R.id.sslr);
			sslr.setText(mJidaiBaseInfo.sale_profit);
			TextView ssjx = findViewById(R.id.ssjx);
			ssjx.setText(mJidaiBaseInfo.sale_achievement);


		}else if(msgInt==103){

			TextView sgcc = findViewById(R.id.sgcc);
			sgcc.setText(mShigongBaseInfo.service_time);

			TextView sgxms = findViewById(R.id.sgxms);
			sgxms.setText(mShigongBaseInfo.repair_time);

			TextView xmze = findViewById(R.id.xmze);
			xmze.setText(mShigongBaseInfo.repair_money);

			TextView xmlr = findViewById(R.id.xmlr);
			xmlr.setText(mShigongBaseInfo.repair_profit);

			TextView zgs = findViewById(R.id.zgs);
			zgs.setText(mShigongBaseInfo.hours);

			TextView sgjx = findViewById(R.id.sgjx);
			sgjx.setText(mShigongBaseInfo.repair_achievement);


		}else if(msgInt==104){
			mProAdapter.notifyDataSetChanged();
		}else if(msgInt==105){
			mProAdapter2.notifyDataSetChanged();
		}
	}

	private void getJieDaiData(){
		String endDate =select_date_end.getText().toString() + " 23:59:59";

		String startDate = select_date_start.getText().toString()+ " 00:00:00";

		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("db", sp.getString(Constance.Data_Source_name));
		dataMap.put("function", "sp_fun_query_achievement_sale");
		dataMap.put("company_code", sp.getString(Constance.COMP_CODE));
		dataMap.put("employee", sp.getString(Constance.USERNAME));
		dataMap.put("dates", startDate);
		dataMap.put("datee", endDate);
		HttpClient client = new HttpClient();
		client.post(Util.getUrl(), dataMap, new IGetDataListener() {
			@Override
			public void onSuccess(String json) {
				System.out.println("11111");
				Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
				String state = (String) resMap.get("state");

				if ( "ok".equals(state)) {
					JSONArray dataArray = (JSONArray) resMap.get("data");
					List<JiedaiBaseInfo> dataList = JSONArray.parseArray(dataArray.toJSONString(),JiedaiBaseInfo.class);
					if(dataList!=null&&dataList.size()>0){
						mJidaiBaseInfo = dataList.get(0);
						mHandler.sendEmptyMessage(102);
					}

				}
			}
			@Override
			public void onFail() {

			}
		});
	}

	private void getCardListData(){
		String endDate =select_date_end.getText().toString() + " 23:59:59";

		String startDate = select_date_start.getText().toString()+ " 00:00:00";

		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("db", sp.getString(Constance.Data_Source_name));
		dataMap.put("function", "sp_fun_query_achievement");
		dataMap.put("company_code", sp.getString(Constance.COMP_CODE));
		dataMap.put("employee", sp.getString(Constance.USERNAME));
		dataMap.put("dates", startDate);
		dataMap.put("datee", endDate);
		HttpClient client = new HttpClient();
		client.post(Util.getUrl(), dataMap, new IGetDataListener() {
			@Override
			public void onSuccess(String json) {
				System.out.println("11111");
				Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
				String state = (String) resMap.get("state");

				if ( "ok".equals(state)) {
					JSONArray dataArray = (JSONArray) resMap.get("data");
					List<JixiaoBaseInfo> dataList = JSONArray.parseArray(dataArray.toJSONString(),JixiaoBaseInfo.class);
					if(dataList!=null&&dataList.size()>0){
						mJixiaoBaseInfo = dataList.get(0);
						mHandler.sendEmptyMessage(101);
					}
				}
			}
			@Override
			public void onFail() {

			}
		});

	}

	private void getShigongData(){
		String endDate =select_date_end.getText().toString() + " 23:59:59";
		String startDate = select_date_start.getText().toString()+ " 00:00:00";

		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("db", sp.getString(Constance.Data_Source_name));
		dataMap.put("function", "sp_fun_query_achievement_repair");
		dataMap.put("company_code", sp.getString(Constance.COMP_CODE));
		dataMap.put("employee", sp.getString(Constance.USERNAME));
		dataMap.put("dates", startDate);
		dataMap.put("datee", endDate);
		HttpClient client = new HttpClient();
		client.post(Util.getUrl(), dataMap, new IGetDataListener() {
			@Override
			public void onSuccess(String json) {
				System.out.println("11111");
				Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
				String state = (String) resMap.get("state");

				if ( "ok".equals(state)) {
					JSONArray dataArray = (JSONArray) resMap.get("data");
					List<ShigongBaseInfo> dataList = JSONArray.parseArray(dataArray.toJSONString(),ShigongBaseInfo.class);
					if(dataList!=null&&dataList.size()>0){
						mShigongBaseInfo = dataList.get(0);
						mHandler.sendEmptyMessage(103);
					}
				}
			}
			@Override
			public void onFail() {

			}
		});

	}

	private void getYejiFenzuData(){
		String endDate =select_date_end.getText().toString() + " 23:59:59";
		String startDate = select_date_start.getText().toString()+ " 00:00:00";


		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("db", sp.getString(Constance.Data_Source_name));
		dataMap.put("function", "sp_fun_query_achievement_collect");
		dataMap.put("company_code", sp.getString(Constance.COMP_CODE));
		dataMap.put("employee", sp.getString(Constance.USERNAME));
		dataMap.put("dates", startDate);
		dataMap.put("datee", endDate);
		HttpClient client = new HttpClient();
		client.post(Util.getUrl(), dataMap, new IGetDataListener() {
			@Override
			public void onSuccess(String json) {
				System.out.println("11111");
				Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
				String state = (String) resMap.get("state");

				if ( "ok".equals(state)) {
					mJidaiBaseInfoList.clear();
					JSONArray dataArray = (JSONArray) resMap.get("data");
				 	List<JiedaiBaseInfo> dataList = JSONArray.parseArray(dataArray.toJSONString(),JiedaiBaseInfo.class);
				 	if(dataList!=null&&dataList.size()>0) {
						mJidaiBaseInfoList.addAll(dataList);
					}
					mHandler.sendEmptyMessage(104);
				}

			}
			@Override
			public void onFail() {

			}
		});
	}
	private void getShigongFenzuData(){
		String endDate =select_date_end.getText().toString() + " 23:59:59";
		String startDate = select_date_start.getText().toString()+ " 00:00:00";

		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("db", sp.getString(Constance.Data_Source_name));
		dataMap.put("function", "sp_fun_query_achievement_collect_repair");
		dataMap.put("company_code", sp.getString(Constance.COMP_CODE));
		dataMap.put("employee", sp.getString(Constance.USERNAME));
		dataMap.put("dates", startDate);
		dataMap.put("datee", endDate);
		HttpClient client = new HttpClient();
		client.post(Util.getUrl(), dataMap, new IGetDataListener() {
			@Override
			public void onSuccess(String json) {
				System.out.println("11111");
				Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
				String state = (String) resMap.get("state");

				if ( "ok".equals(state)) {
					mJidaiBaseInfoList2.clear();

					JSONArray dataArray = (JSONArray) resMap.get("data");
					List<JiedaiBaseInfo> dataList = JSONArray.parseArray(dataArray.toJSONString(),JiedaiBaseInfo.class);
					if(dataList!=null&&dataList.size()>0){
						mJidaiBaseInfoList2.addAll(dataList);
					}
					mHandler.sendEmptyMessage(105);

				}
			}
			@Override
			public void onFail() {

			}
		});
	}
	private void getData(){
		getCardListData();
		getJieDaiData();
		getShigongData();
		getYejiFenzuData();
		getShigongFenzuData();
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
                case R.id.tv_base_info:
					tv_base_info.setTextColor(Color.parseColor("#ffffff"));
					tv_more_info.setTextColor(Color.parseColor("#333333"));
					tv_base_info.setBackgroundColor(Color.parseColor("#ff9db4"));
					tv_more_info.setBackgroundColor(Color.parseColor("#a4a3a3"));
					pro_list.setVisibility(View.VISIBLE);
					shigong_list.setVisibility(View.GONE);
					pro_listview.setVisibility(View.VISIBLE);
					shigong_listview.setVisibility(View.GONE);
                    xiaoshou_page.setVisibility(View.VISIBLE);
					shigong_page.setVisibility(View.GONE);
                break;
                case R.id.tv_more_info:
					tv_base_info.setTextColor(Color.parseColor("#333333"));
					tv_more_info.setTextColor(Color.parseColor("#ffffff"));
					tv_base_info.setBackgroundColor(Color.parseColor("#a4a3a3"));
					tv_more_info.setBackgroundColor(Color.parseColor("#ff9db4"));
					pro_list.setVisibility(View.GONE);
					shigong_list.setVisibility(View.VISIBLE);
					pro_listview.setVisibility(View.GONE);
					shigong_listview.setVisibility(View.VISIBLE);
                    shigong_page.setVisibility(View.VISIBLE);
                    xiaoshou_page.setVisibility(View.GONE);

                break;
            default:

                break;
		}
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
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
}
