package com.shoujia.zhangshangxiu.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseActivity;
import com.shoujia.zhangshangxiu.db.DBManager;
import com.shoujia.zhangshangxiu.entity.FirstIconInfo;
import com.shoujia.zhangshangxiu.order.ProjectOrderActivity;
import com.shoujia.zhangshangxiu.support.InfoSupport;
import com.shoujia.zhangshangxiu.support.NavSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/2/23 0023.
 * 首页
 */
public class ProjectSelectActivity extends BaseActivity implements View.OnClickListener{
	private final String TAG = "ProjectSelectActivity";
  private NavSupport navSupport;
  private TextView confirm_order;
  private GridLayout gridLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pro_select);
		navSupport = new NavSupport(this,4);
		confirm_order = findViewById(R.id.confirm_order);
		confirm_order.setOnClickListener(this);
		new InfoSupport(this);
		initView();
		initData();

	}

	private void initView() {
		gridLayout = findViewById(R.id.gridlayout1);
		DBManager db = DBManager.getInstanse(this);
		List<FirstIconInfo> firstIconInfos = db.queryFirstIconListData();

		if(firstIconInfos!=null&&firstIconInfos.size()>0) {
			gridLayout.setRowCount(firstIconInfos.size());
			gridLayout.setColumnCount(3);
			for (int i = 0; i < firstIconInfos.size(); i++) {

				View subView = View.inflate(this, R.layout.view_grid_item, null);
				TextView name = subView.findViewById(R.id.name);
				name.setText(firstIconInfos.get(i).getWxgz());
				gridLayout.addView(subView);

			}
		}
	}

	//初始化数据
	private void initData(){


	}


    @Override
    public void onClick(View v) {
    	switch (v.getId()){
			case R.id.confirm_order:
				startActivity(new Intent(this,ProjectOrderActivity.class));
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
