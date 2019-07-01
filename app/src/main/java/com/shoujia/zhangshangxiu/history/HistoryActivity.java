package com.shoujia.zhangshangxiu.history;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseActivity;
import com.shoujia.zhangshangxiu.entity.ManageInfo;
import com.shoujia.zhangshangxiu.history.adapter.HistoryListAdapter;
import com.shoujia.zhangshangxiu.history.help.HistoryDataHelper;
import com.shoujia.zhangshangxiu.support.InfoSupport;
import com.shoujia.zhangshangxiu.support.NavSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/23 0023.
 * 首页
 */
public class HistoryActivity extends BaseActivity implements View.OnClickListener{
	private final String TAG = "HomeActivity";
  private NavSupport navSupport;
  private List<ManageInfo> mInfoList;
  private HistoryListAdapter carListAdapter;
	ListView mListview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_history_list);
		initView();
		initData();

	}

	private void initView() {
		mInfoList = new ArrayList<>();
		navSupport = new NavSupport(this,3);
		mListview = findViewById(R.id.listview);
		carListAdapter = new HistoryListAdapter(this,mInfoList);
		mListview.setAdapter(carListAdapter);
		new InfoSupport(this);
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
		HistoryDataHelper carDataHelper = new HistoryDataHelper(this);
		carDataHelper.setPreZero();
		carDataHelper.getCardList(chooseName, new HistoryDataHelper.GetDataListener() {
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
