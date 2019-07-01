package com.shoujia.zhangshangxiu.msgcenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseActivity;
import com.shoujia.zhangshangxiu.dialog.ProjectCancleDialog;
import com.shoujia.zhangshangxiu.entity.OrderBean;
import com.shoujia.zhangshangxiu.history.HistoryActivity;
import com.shoujia.zhangshangxiu.msgcenter.adapter.QueryOrderAdapter;
import com.shoujia.zhangshangxiu.msgcenter.help.QueryOrderDataHelper;
import com.shoujia.zhangshangxiu.project.ProjectSelectActivity;
import com.shoujia.zhangshangxiu.project.help.ProjectDataHelper;
import com.shoujia.zhangshangxiu.support.InfoSupport;
import com.shoujia.zhangshangxiu.support.NavSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/23 0023.
 * 首页
 */
public class QueryOrderActivity extends BaseActivity implements View.OnClickListener{
	private final String TAG = "ProjectOrderActivity";
  private NavSupport navSupport;
	List<OrderBean> mOrderBeans;
	QueryOrderAdapter mAdapter;
	private ListView listview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_query_order);
		listview = findViewById(R.id.listview);
		navSupport = new NavSupport(this,7);
		mOrderBeans = new ArrayList<>();
		 mAdapter = new QueryOrderAdapter(this,mOrderBeans);
		listview.setAdapter(mAdapter);
		initData();
		QueryOrderDataHelper helper = new QueryOrderDataHelper(this);
		helper.getListData(new QueryOrderDataHelper.GetDataListener() {
			@Override
			public void getData(List<OrderBean> orderBeans) {
				mOrderBeans = orderBeans;
				mHandler.sendEmptyMessage(2);
			}
		});
	}

	@Override
	protected void updateUIThread(int msgInt){
		if(msgInt==2){
			mAdapter.setListData(mOrderBeans);
			mAdapter.notifyDataSetChanged();
		}
	}

	//初始化数据
	private void initData(){


	}


    @Override
    public void onClick(View v) {
    	switch (v.getId()){

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
