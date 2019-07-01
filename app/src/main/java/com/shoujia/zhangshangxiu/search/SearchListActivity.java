package com.shoujia.zhangshangxiu.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseActivity;
import com.shoujia.zhangshangxiu.db.DBManager;
import com.shoujia.zhangshangxiu.entity.CarInfo;
import com.shoujia.zhangshangxiu.entity.ManageInfo;
import com.shoujia.zhangshangxiu.search.adapter.SearchListAdapter;
import com.shoujia.zhangshangxiu.support.InfoSupport;
import com.shoujia.zhangshangxiu.support.NavSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/23 0023.
 * 首页
 */
public class SearchListActivity extends BaseActivity implements View.OnClickListener{
	private final String TAG = "HomeActivity";
  private NavSupport navSupport;
  private List<CarInfo> mInfoList;
  private SearchListAdapter carListAdapter;
	ListView mListview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_list);
		initView();
		initData();

	}

	private void initView() {
		mInfoList = new ArrayList<>();
		mListview = findViewById(R.id.listview);
		carListAdapter = new SearchListAdapter(this,mInfoList);
		mListview.setAdapter(carListAdapter);
		new NavSupport(this,6);
		mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				CarInfo carInfo = mInfoList.get(i);
				Intent intent = new Intent();
		//把返回数据存入Intent
             intent.putExtra("carInfo", carInfo);
          //设置返回数据
             setResult(RESULT_OK, intent);
             finish();

			}
		});
	}

	//初始化数据
	private void initData(){
    	String typeStr = getIntent().getStringExtra("searchName");
    	getData(typeStr);
	}



	private void getData(String chooseName){
		DBManager db = DBManager.getInstanse(this);
		mInfoList = db.querySearchListData(chooseName);
		carListAdapter.setListData(mInfoList);
		carListAdapter.notifyDataSetChanged();
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
