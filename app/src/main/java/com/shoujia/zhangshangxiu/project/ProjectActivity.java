package com.shoujia.zhangshangxiu.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseActivity;
import com.shoujia.zhangshangxiu.db.DBManager;
import com.shoujia.zhangshangxiu.dialog.ProjectCancleDialog;
import com.shoujia.zhangshangxiu.dialog.ProjectEditDialog;
import com.shoujia.zhangshangxiu.entity.CarInfo;
import com.shoujia.zhangshangxiu.history.HistoryActivity;
import com.shoujia.zhangshangxiu.http.HttpClient;
import com.shoujia.zhangshangxiu.http.IGetDataListener;
import com.shoujia.zhangshangxiu.project.help.ProjectDataHelper;
import com.shoujia.zhangshangxiu.support.InfoSupport;
import com.shoujia.zhangshangxiu.support.NavSupport;
import com.shoujia.zhangshangxiu.support.TabSupport;
import com.shoujia.zhangshangxiu.util.Constance;
import com.shoujia.zhangshangxiu.util.SharePreferenceManager;
import com.shoujia.zhangshangxiu.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/23 0023.
 * 首页
 */
public class ProjectActivity extends BaseActivity implements View.OnClickListener{
	private final String TAG = "HomeActivity";
  private NavSupport navSupport;
  private LinearLayout car_history,cancle_reciver,project_select;
  private RelativeLayout rl_gls,rl_cjh,rl_cx,rl_ywg,rl_gzms,rl_jsr,rl_bz;
  private TextView tv_gls,tv_cjh,tv_cx,tv_ywg,tv_gzms,tv_jsr,tv_bz;
    private SharePreferenceManager sp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_project);
		car_history = findViewById(R.id.car_history);
        cancle_reciver = findViewById(R.id.cancle_reciver);
        project_select = findViewById(R.id.project_select);
        rl_gls = findViewById(R.id.rl_gls);
        rl_cjh = findViewById(R.id.rl_cjh);
        rl_cx = findViewById(R.id.rl_cx);
        rl_ywg = findViewById(R.id.rl_ywg);
        rl_gzms = findViewById(R.id.rl_gzms);
        rl_jsr = findViewById(R.id.rl_jsr);
        rl_bz = findViewById(R.id.rl_bz);
        tv_gls = findViewById(R.id.tv_gls);
        tv_cjh = findViewById(R.id.tv_cjh);
        tv_cx = findViewById(R.id.tv_cx);
        tv_ywg = findViewById(R.id.tv_ywg);
        tv_gzms = findViewById(R.id.tv_gzms);
        tv_jsr = findViewById(R.id.tv_jsr);
        tv_bz = findViewById(R.id.tv_bz);
        rl_gls.setOnClickListener(this);
        rl_cjh.setOnClickListener(this);
        rl_cx.setOnClickListener(this);
        rl_ywg.setOnClickListener(this);
        rl_gzms.setOnClickListener(this);
        rl_jsr.setOnClickListener(this);
        rl_bz.setOnClickListener(this);
        sp = new SharePreferenceManager(this);
		navSupport = new NavSupport(this,1);
		car_history.setOnClickListener(this);
        cancle_reciver.setOnClickListener(this);
        project_select.setOnClickListener(this);
        new InfoSupport(this);
		initData();

	}

	//初始化数据
	private void initData(){


	}

    private void showTipDialog(final String keyName,String titleName,final TextView tv){
        ProjectEditDialog dialog = new ProjectEditDialog(this,titleName);
        dialog.show();
        dialog.setOnClickListener(new ProjectEditDialog.OnClickListener() {
            @Override
            public void rightBtnClick(String numStr) {
                tv.setText(numStr);
                getCardList(keyName,numStr);
            }
        });
    }

    //获取车辆数据
    public void getCardList(String keyName,String keyValue){

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_upload_repair_list_main_other");
        dataMap.put("company_code", sp.getString(Constance.COMP_CODE));
        dataMap.put("column_name", keyName);
        dataMap.put("data", keyValue);
        dataMap.put("jsd_id", sp.getString(Constance.JSD_ID));
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                System.out.println("11111");
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                if(resMap!=null&&resMap.get("state")!=null){
                    String state = (String) resMap.get("state");
                    if (state.equals("ok")) {
                        toastMsg = "修改成功";
                        mHandler.sendEmptyMessage(TOAST_MSG);
                        //Toast.makeText(ProjectActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    }else{
                        toastMsg = "修改失败";
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }
                }

            }

            @Override
            public void onFail() {

            }
        });
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
                        ProjectDataHelper helper = new ProjectDataHelper(ProjectActivity.this);
                        helper.cancleReciver();
                    }
                });
                dialog.show();
                break;
            case R.id.rl_gls:
                showTipDialog("jclc","公里数",tv_gls);
                break;
            case R.id.rl_cjh:
                showTipDialog("cjhm","车架号",tv_cjh);
                break;
            case R.id.rl_cx:
                showTipDialog("cx","车型",tv_cx);
                break;
            case R.id.rl_ywg:
                showTipDialog("ywg_date","已完工日期",tv_ywg);
                break;
            case R.id.rl_gzms:
                showTipDialog("jclc","故障描述",tv_gzms);
                break;
            case R.id.rl_jsr:
                showTipDialog("custom5","接收人",tv_jsr);
                break;
            case R.id.rl_bz:
                showTipDialog("memo","备注",tv_bz);
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
