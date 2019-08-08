package com.shoujia.zhangshangxiu.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseActivity;
import com.shoujia.zhangshangxiu.home.HomeActivity;
import com.shoujia.zhangshangxiu.http.HttpClient;
import com.shoujia.zhangshangxiu.http.IGetDataListener;
import com.shoujia.zhangshangxiu.util.Constance;
import com.shoujia.zhangshangxiu.util.DateUtil;
import com.shoujia.zhangshangxiu.util.SharePreferenceManager;
import com.shoujia.zhangshangxiu.util.Util;

import java.util.HashMap;
import java.util.Map;

public class PhoneLoginActivity extends BaseActivity implements OnClickListener {

    private Button login_btn;
    private LinearLayout top_btn;
    private EditText factory_name, password, username,server_ip;
    SharePreferenceManager sp;
    int clickNum = 0;
    String  url = "http://121.43.148.193:5555/restful/pro";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_main);
        login_btn = findViewById(R.id.login_btn);
        top_btn = findViewById(R.id.top_btn);
        factory_name = findViewById(R.id.factory_name);
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        server_ip = findViewById(R.id.server_ip);

        login_btn.setOnClickListener(this);
        top_btn.setOnClickListener(this);
        sp = new SharePreferenceManager(this);
        username.setText(sp.getString(Constance.USERNAME));
        password.setText(sp.getString(Constance.PASSWORD));
        factory_name.setText(sp.getString(Constance.FACTORYNAME));
        if(sp.getBoolean(Constance.HASLOGIN)){
            startActivity(new Intent(PhoneLoginActivity.this, HomeActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        clickNum=0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                if (factory_name.getText() == null || TextUtils.isEmpty(factory_name.getText().toString().trim())) {
                    Toast.makeText(this, "修理厂名称/分店名称未填写", Toast.LENGTH_LONG).show();
                    return;
                }
                if (username.getText() == null || TextUtils.isEmpty(username.getText().toString().trim())) {
                    Toast.makeText(this, "修理厂名称/分店名称未填写", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.getText() == null || TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_LONG).show();
                    return;
                }
                showDialog(this);
                String factoryName = factory_name.getText().toString().trim();
                String userName = username.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                checkLoginDate(factoryName, userName, pwd);

                break;
            case R.id.top_btn:
                clickNum++;
                if(clickNum>10){
                    server_ip.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void updateUIThread(int msgInt) {
        super.updateUIThread(msgInt);
        if(msgInt==200){
            server_ip.setText(sp.getString(Constance.server_ip_port));
        }
    }

    private void checkLoginDate(final String factoryName, final String userName, final String pwd) {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", "sjsoft_SQL");
        dataMap.put("function", "sp_fun_check_service_validity");
        dataMap.put("data_source", factoryName);
        dataMap.put("operater_code", userName);
        HttpClient client = new HttpClient();
        client.post(url, dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                Map<String, Object> resMap = JSONObject.parseObject(json);
                if (resMap.get("state") != null && "true".equals(resMap.get("state"))) {
                    String endDateStr = (String) resMap.get("service_end_date");
                    if (!TextUtils.isEmpty(endDateStr)) {
                        long endDateLong = DateUtil.stringToLong(endDateStr, "yyyy-MM-dd HH:mm:ss");
                        long curDateLong = System.currentTimeMillis();
                        if (endDateLong > curDateLong) {
                            sp.putString(Constance.Data_Source_name,(String) resMap.get("Data_Source_name"));
                            if(server_ip.getVisibility()==View.VISIBLE && server_ip.getText()!=null&&!TextUtils.isEmpty( server_ip.getText().toString().trim())){
                                sp.putString(Constance.server_ip_port,server_ip.getText().toString().trim());
                                mHandler.sendEmptyMessage(200);
                            }else {
                                sp.putString(Constance.server_ip_port, (String) resMap.get("server_ip_port"));
                            }
                            login(factoryName,userName, pwd);
                        } else {
                            dismissDialog();
                            toastMsg = "服务有效期限已经过了，请联系首佳软件进行续费。 过期时间：" + ((endDateStr.length()) > 10 ? endDateStr.substring(0, 10) : endDateStr);
                            mHandler.sendEmptyMessage(TOAST_MSG);
                        }
                    } else {
                        login(factoryName,userName, pwd);
                    }
                }
            }

            @Override
            public void onFail() {
                dismissDialog();
            }
        });
    }

    private void login(final String factoryName,final String userName, final String password) {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("db", sp.getString(Constance.Data_Source_name));
        dataMap.put("function", "sp_fun_check_user_state_login");
        dataMap.put("operater_code", userName);
        dataMap.put("operater_ip", "121.43.148.193");
        dataMap.put("password", password);
        HttpClient client = new HttpClient();
        client.post(Util.getUrl(), dataMap, new IGetDataListener() {
            @Override
            public void onSuccess(String json) {
                dismissDialog();
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(json);
                if (resMap.get("state") != null && "true".equals(resMap.get("state"))) {
                    String state = (String) resMap.get("state");
                    String comp_code = (String) resMap.get("comp_code");
                    String chinese_name = (String) resMap.get("chinese_name");
                    if (state.equals("true")) {
                       sp.putString(Constance.PASSWORD,password);
                       sp.putString(Constance.USERNAME,userName);
                       sp.putString(Constance.FACTORYNAME,factoryName);
                       sp.putString(Constance.COMP_CODE,comp_code);
                       sp.putString(Constance.CHINESE_NAME,chinese_name);
                       sp.putBoolean(Constance.HASLOGIN,true);
                        startActivity(new Intent(PhoneLoginActivity.this, HomeActivity.class));
                    } else {

                        Toast.makeText(PhoneLoginActivity.this, "服务异常", Toast.LENGTH_LONG).show();
                    }
                } else {

                    toastMsg = "服务异常" + (resMap.get("msg") != null ? (":" + resMap.get("msg")) : "");
                    mHandler.sendEmptyMessage(TOAST_MSG);
                }
            }

            @Override
            public void onFail() {
                dismissDialog();
                toastMsg = "服务异常";
                mHandler.sendEmptyMessage(TOAST_MSG);
            }
        });
    }

}
