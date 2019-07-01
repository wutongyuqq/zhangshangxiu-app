package com.shoujia.zhangshangxiu.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseActivity;
import com.shoujia.zhangshangxiu.dialog.ProjectCancleDialog;
import com.shoujia.zhangshangxiu.history.HistoryActivity;
import com.shoujia.zhangshangxiu.project.ProjectSelectActivity;
import com.shoujia.zhangshangxiu.project.help.ProjectDataHelper;
import com.shoujia.zhangshangxiu.support.InfoSupport;
import com.shoujia.zhangshangxiu.support.NavSupport;

/**
 * Created by Administrator on 2017/2/23 0023.
 * 首页
 */
public class ProjectOrderActivity extends BaseActivity implements View.OnClickListener{
	private final String TAG = "ProjectOrderActivity";
  private NavSupport navSupport;
  private LinearLayout car_history,cancle_reciver,project_select;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pro_order);
		navSupport = new NavSupport(this,1);
		new InfoSupport(this);
		initData();

	}

	//初始化数据
	private void initData(){


	}


    @Override
    public void onClick(View v) {
    	switch (v.getId()){
			case R.id.car_history:
				Intent intent = new Intent(this,HistoryActivity.class);
				startActivity(intent);
				break;
				case R.id.project_select:
				Intent intent2 = new Intent(this,ProjectSelectActivity.class);
				startActivity(intent2);
				break;
            case R.id.cancle_reciver:
                ProjectCancleDialog dialog = new ProjectCancleDialog(this);
                dialog.setOnClickListener(new ProjectCancleDialog.OnClickListener() {
                    @Override
                    public void rightBtnClick() {
                        ProjectDataHelper helper = new ProjectDataHelper(ProjectOrderActivity.this);
                        helper.cancleReciver();
                    }
                });
                dialog.show();
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
