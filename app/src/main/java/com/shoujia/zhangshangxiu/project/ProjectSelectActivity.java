package com.shoujia.zhangshangxiu.project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseActivity;
import com.shoujia.zhangshangxiu.db.DBManager;
import com.shoujia.zhangshangxiu.entity.CarInfo;
import com.shoujia.zhangshangxiu.entity.FirstIconInfo;
import com.shoujia.zhangshangxiu.entity.SecondIconInfo;
import com.shoujia.zhangshangxiu.home.help.HomeDataHelper;
import com.shoujia.zhangshangxiu.http.HttpClient;
import com.shoujia.zhangshangxiu.http.IGetDataListener;
import com.shoujia.zhangshangxiu.order.ProjectOrderActivity;
import com.shoujia.zhangshangxiu.support.InfoSupport;
import com.shoujia.zhangshangxiu.support.NavSupport;
import com.shoujia.zhangshangxiu.util.Constance;
import com.shoujia.zhangshangxiu.util.SharePreferenceManager;
import com.shoujia.zhangshangxiu.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/23 0023.
 * 首页
 */
public class ProjectSelectActivity extends BaseActivity implements View.OnClickListener{
	private final String TAG = "ProjectSelectActivity";
  private NavSupport navSupport;
  private TextView confirm_order,tv_kj,tv_cg,tv_by;
	SecondIconInfo mSecondIconInfo;
  private GridLayout gridLayout1,gridLayout2;
	private SharePreferenceManager sp;
	InfoSupport mInFoupport;
	List<SecondIconInfo> secondIconInfos = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pro_select);
		navSupport = new NavSupport(this,4);
		confirm_order = findViewById(R.id.confirm_order);
		tv_kj = findViewById(R.id.tv_kj);
		tv_cg = findViewById(R.id.tv_cg);
		tv_by = findViewById(R.id.tv_by);
		confirm_order.setOnClickListener(this);
		tv_kj.setOnClickListener(this);
		tv_cg.setOnClickListener(this);
		tv_by.setOnClickListener(this);
		gridLayout1 = findViewById(R.id.gridlayout1);
		gridLayout2 = findViewById(R.id.gridlayout2);
		mInFoupport = new InfoSupport(this);
		sp = new SharePreferenceManager(this);
		initView();
		initData();

	}

	@Override
	protected void updateUIThread(int msgInt) {
		super.updateUIThread(msgInt);
		if(msgInt==101){
			gridLayout2.removeAllViews();
			gridLayout1.setVisibility(View.GONE);
			gridLayout2.setVisibility(View.VISIBLE);
			if(secondIconInfos!=null&&secondIconInfos.size()>0) {
				gridLayout2.setRowCount(100);
				gridLayout2.setColumnCount(3);
				final List<View> subViewArr = new ArrayList<>();
				for (int i = 0; i < secondIconInfos.size(); i++) {

					final View subView = View.inflate(this, R.layout.view_grid_item, null);
					TextView name = subView.findViewById(R.id.name);
					ImageView imageView = subView.findViewById(R.id.tv_fold_img);
					if(i==0){
						imageView.setImageResource(R.drawable.fold_back_img);
						name.setText("返回");
						subView.setTag(-1);
						gridLayout2.addView(subView);
					}else {
						subView.setBackground(getResources().getDrawable(R.drawable.pro_select));
						if(secondIconInfos!=null&&secondIconInfos.get(i)!=null) {
							name.setText(secondIconInfos.get(i).getMc());
							imageView.setImageResource(R.drawable.file_img);
							subView.setTag(secondIconInfos.get(i).getId());
							gridLayout2.addView(subView);
						}
						subViewArr.add(subView);
					}

					subView.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							int tag = (int) view.getTag();
							if(tag!=-1) {
								DBManager dbManager = DBManager.getInstanse(ProjectSelectActivity.this);
								mSecondIconInfo = dbManager.querySecondIconData(tag);
								for(View view1:subViewArr){
									view1.setBackgroundColor(Color.parseColor("#00000000"));
								}
								view.setBackgroundColor(Color.parseColor("#cccccc"));
							}else{
								//view.requestFocus();
								gridLayout1.setVisibility(View.VISIBLE);
								gridLayout2.setVisibility(View.GONE);
								mSecondIconInfo = null;

							}
						}
					});
				}
			}
		}
	}

	private void upLoadServer(){
		if(mSecondIconInfo==null){
			toastMsg ="您还未选择项目";
			mHandler.sendEmptyMessage(TOAST_MSG);
			return;
		}
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("db", sp.getString(Constance.Data_Source_name));
		dataMap.put("function", "sp_fun_upload_maintenance_project_detail");
		dataMap.put("jsd_id",sp.getString(Constance.JSD_ID));
		dataMap.put("xlxm",mSecondIconInfo.getMc());
		dataMap.put("xlf",mSecondIconInfo.getXlf());
		dataMap.put("zk","0.00");
		dataMap.put("wxgz",mSecondIconInfo.getWxgz());
		dataMap.put("pgzje",mSecondIconInfo.getSpj());
		dataMap.put("pgzgs",mSecondIconInfo.getPgzgs());
		dataMap.put("xh","0");


		HttpClient client = new HttpClient();
		client.post(Util.getUrl(), dataMap, new IGetDataListener() {
			@Override
			public void onSuccess(String json) {
				System.out.println("11111");
				Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
				String state = (String) resMap.get("state");

				if ( "ok".equals(state)) {
					Intent intent2 = new Intent(ProjectSelectActivity.this,ProjectOrderActivity.class);
					startActivity(intent2);


				}else{

				}
			}
			@Override
			public void onFail() {
				toastMsg ="网络连接异常";
				mHandler.sendEmptyMessage(TOAST_MSG);
			}
		});
	}
	private void initView() {
		gridLayout1.setVisibility(View.VISIBLE);
		gridLayout2.setVisibility(View.GONE);
		DBManager db = DBManager.getInstanse(this);
		final List<FirstIconInfo> firstIconInfos = db.queryFirstIconListData();
		if(firstIconInfos!=null&&firstIconInfos.size()>0) {
			gridLayout1.setRowCount(firstIconInfos.size());
			gridLayout1.setColumnCount(3);
			for (int i = 0; i < firstIconInfos.size(); i++) {
				View subView = View.inflate(this, R.layout.view_grid_item, null);
				TextView name = subView.findViewById(R.id.name);
				name.setText(firstIconInfos.get(i).getWxgz());
				gridLayout1.addView(subView);
				subView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						TextView view1 = view.findViewById(R.id.name);
						if(view1!=null&& view1.getText()!=null){
							String name = view1.getText().toString();
							DBManager dbManager = DBManager.getInstanse(ProjectSelectActivity.this);
							secondIconInfos.clear();
							secondIconInfos.add(null);
							secondIconInfos.addAll(dbManager.querySecondIconListData(name));
							mHandler.sendEmptyMessage(101);

						}
					}
				});

			}
		}
	}

	//初始化数据
	private void initData(){

		DBManager db = DBManager.getInstanse(this);
		List<FirstIconInfo> firstIconInfos = db.queryFirstIconListData();
		if(firstIconInfos==null||firstIconInfos.size()==0){
			HomeDataHelper homeDataHelper = new HomeDataHelper(this);
			homeDataHelper.getFirstIconList(new HomeDataHelper.InsertDataListener() {
				@Override
				public void onSuccess() {
					DBManager.getInstanse(ProjectSelectActivity.this).close();
					getSecondInconList();
				}

				@Override
				public void onFail() {
					DBManager.getInstanse(ProjectSelectActivity.this).close();
					getSecondInconList();
				}
			});
		}else{
			mHandler.sendEmptyMessage(4);
		}
	}



	private void getSecondInconList(){
		DBManager db = DBManager.getInstanse(this);
		List<SecondIconInfo> secondIconInfos = db.querySecondIconListData();
		if(secondIconInfos==null||secondIconInfos.size()==0){

			HomeDataHelper homeDataHelper = new HomeDataHelper(this);
			homeDataHelper.getSecondIconList();

		}else{
			mHandler.sendEmptyMessage(5);
		}

	}

	private void setTextColor(int index){
    	TextView[] textViews= {tv_cg,tv_kj,tv_by};
    	for(int i=0;i<3;i++){
    		if(i==index){
				textViews[i].setTextColor(Color.parseColor("#ffffff"));
				textViews[i].setBackgroundColor(Color.parseColor("#ff9db4"));
			}else{
				textViews[i].setTextColor(Color.parseColor("#a4a3a3"));
				textViews[i].setBackgroundColor(Color.parseColor("#eeeeee"));
			}
		}

	}

    @Override
    public void onClick(View v) {
    	switch (v.getId()){
			case R.id.confirm_order:
				upLoadServer();
				//startActivity(new Intent(this,ProjectOrderActivity.class));
				break;
				case R.id.tv_cg:
					setTextColor(0);
				break;
			case R.id.tv_kj:
				setTextColor(1);
				break;
			case R.id.tv_by:
				setTextColor(2);
				break;
            default:

                break;
		}
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
