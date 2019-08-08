package com.shoujia.zhangshangxiu.manager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseFragment;
import com.shoujia.zhangshangxiu.db.DBManager;
import com.shoujia.zhangshangxiu.entity.CarInfo;
import com.shoujia.zhangshangxiu.entity.ManageInfo;
import com.shoujia.zhangshangxiu.home.adapter.HomeCarInfoAdapter;
import com.shoujia.zhangshangxiu.manager.activity.MangerLinggongActivity;
import com.shoujia.zhangshangxiu.manager.adapter.FactoryManagerAdapter;
import com.shoujia.zhangshangxiu.manager.help.ManageDataHelper;
import com.shoujia.zhangshangxiu.order.adapter.BankListAdapter;
import com.shoujia.zhangshangxiu.order.adapter.WxgzListAdapter;
import com.shoujia.zhangshangxiu.order.entity.RateBean;
import com.shoujia.zhangshangxiu.util.Constance;
import com.shoujia.zhangshangxiu.util.SharePreferenceManager;
import com.shoujia.zhangshangxiu.util.Util;

import java.util.ArrayList;
import java.util.List;

/*
* 车间管理
* */
public class FactoryManagerFragment extends BaseFragment implements View.OnClickListener {

    private ListView listview;
    private View mView;
    private List<ManageInfo> manageInfoList;
    private TextView prepare_work,doing_work,prepare_check,done_work,jinchangshijian,jiaocheshijian,my_renwu,xuanzegongzhong;
    private EditText search_cp;
    ImageView search_btn;
    FactoryManagerAdapter managerAdapter;
    private int mCurIndex;
    SharePreferenceManager sp;
    String wxgz;
    String cp;
    String assign;
    String orderStr;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = View.inflate(getActivity(), R.layout.fragment_factory_manager, null);
        sp = new SharePreferenceManager(getContext());
        initView();
        getServerData(0);
        return mView;
    }

    public void updateUIThread(Message msg) {
        int msgInt = msg.what;
        if(msgInt==201){
            if(manageInfoList!=null) {
                //managerAdapter.setListData(manageInfoList);
                managerAdapter.notifyDataSetChanged();
            }else{
                //managerAdapter.setListData(new ArrayList<ManageInfo>());
                managerAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initView() {
        listview = mView.findViewById(R.id.listview);
        prepare_work = mView.findViewById(R.id.prepare_work);
        doing_work = mView.findViewById(R.id.doing_work);
        prepare_check = mView.findViewById(R.id.prepare_check);
        done_work = mView.findViewById(R.id.done_work);
        jinchangshijian = mView.findViewById(R.id.jinchangshijian);
        //,,
        jiaocheshijian = mView.findViewById(R.id.jiaocheshijian);
        my_renwu = mView.findViewById(R.id.my_renwu);
        xuanzegongzhong = mView.findViewById(R.id.xuanzegongzhong);


        listview = mView.findViewById(R.id.listview);
        search_btn = mView.findViewById(R.id.search_btn);
        search_cp = mView.findViewById(R.id.search_cp);
        manageInfoList = new ArrayList<>();
        managerAdapter = new FactoryManagerAdapter(getContext(),manageInfoList);
        listview.setAdapter(managerAdapter);

        prepare_work.setOnClickListener(this);
        search_btn.setOnClickListener(this);
        doing_work.setOnClickListener(this);
        prepare_check.setOnClickListener(this);
        done_work.setOnClickListener(this);
        jinchangshijian.setOnClickListener(this);
        jiaocheshijian.setOnClickListener(this);
        my_renwu.setOnClickListener(this);
        xuanzegongzhong.setOnClickListener(this);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ManageInfo info = manageInfoList.get(i);
                sp.putString(Constance.JSD_ID,info.getJsd_id());
                sp.putString(Constance.CHEJIAHAO,info.getCjhm());
                sp.putString(Constance.CURRENTCP,info.getCp());
                sp.putString(Constance.CHEXING,info.getCx());
                sp.putString(Constance.JIECHEDATE,info.getJc_date());
                sp.putString(Constance.YUWANGONG,info.getYwg_date());
                Intent intent = new Intent(getActivity(),MangerLinggongActivity.class);
                intent.putExtra("state",manageInfoList.get(i).getStates());
                intent.putExtra("curIndex",mCurIndex);
                startActivity(intent);
            }
        });
        search_cp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable!=null) {
                    cp = editable.toString();
                }
            }
        });

    }

    private void getServerData(int index){
        mCurIndex = index;
        TextView[] textViews = {prepare_work,doing_work,prepare_check,done_work};
        for(int i=0;i<textViews.length;i++){
            if(i==index){
                textViews[i].setBackgroundColor(Color.parseColor("#ff9db4"));
                textViews[i].setTextColor(Color.parseColor("#ffffff"));
            }else{
                textViews[i].setBackgroundColor(Color.parseColor("#a4a3a3"));
                textViews[i].setTextColor(Color.parseColor("#333333"));
            }
        }
        getListData(index);
    }

    private void getListData(int index){
        assign="";
        wxgz="";
        orderStr="";
        cp="";
        xuanzegongzhong.setText("全部");
        search_cp.setText("");
        my_renwu.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.right_now_no), null, null, null);
        my_renwu.setTag("0");

        ManageDataHelper helper = new ManageDataHelper(getActivity());
        helper.setPreZero();
        helper.getListData(index, new ManageDataHelper.GetDataListener() {
            @Override
            public void getData(List<ManageInfo> manageInfos) {
                manageInfoList.clear();
                if(manageInfos!=null) {
                    manageInfoList.addAll(manageInfos);
                }
                DBManager dbManager = DBManager.getInstanse(getActivity());
                dbManager.insertManagerListData(manageInfos);
                mHandler.sendEmptyMessage(201);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prepare_work:
                getServerData(0);
                break;
            case R.id.doing_work:
                getServerData(1);
                break;
            case R.id.prepare_check:
                getServerData(2);
                break;
            case R.id.done_work:
                getServerData(3);
                break;
                case R.id.jinchangshijian:
                //getServerData(3);
                    //,,,
                    String tag = (String) jinchangshijian.getTag();
                    if(tag.equals("0")) {
                        DBManager dbManager = DBManager.getInstanse(getActivity());
                        manageInfoList.clear();
                        orderStr = " jc_date desc";
                        List<ManageInfo> manageInfos = dbManager.queryManagerListData(cp, wxgz, assign, orderStr);
                        if(manageInfos!=null) {
                            manageInfoList.addAll(manageInfos);
                        }
                        managerAdapter.notifyDataSetChanged();
                        jinchangshijian.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.sjx_up), null, null, null);

                        jinchangshijian.setTag("1");
                    }else{
                        DBManager dbManager = DBManager.getInstanse(getActivity());
                        manageInfoList.clear();
                        orderStr = " jc_date asc";

                        List<ManageInfo> manageInfos = dbManager.queryManagerListData(cp, wxgz, assign, orderStr);
                        if(manageInfos!=null) {
                            manageInfoList.addAll(manageInfos);
                        }
                        managerAdapter.notifyDataSetChanged();
                        jinchangshijian.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.sjx_down), null, null, null);
                        jinchangshijian.setTag("0");
                    }
                break;
            case R.id.jiaocheshijian:
                String tag1 = (String) jiaocheshijian.getTag();
                if(tag1.equals("0")) {
                    DBManager dbManager = DBManager.getInstanse(getActivity());
                    manageInfoList.clear();
                    orderStr = " ywg_date desc";
                    List<ManageInfo> manageInfos = dbManager.queryManagerListData(cp, wxgz, assign, orderStr);
                    if(manageInfos!=null) {
                        manageInfoList.addAll(manageInfos);
                    }
                  //  managerAdapter.setListData(manageInfoList);
                    managerAdapter.notifyDataSetChanged();
                    jiaocheshijian.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.sjx_up), null, null, null);

                    jiaocheshijian.setTag("1");
                }else{
                    DBManager dbManager = DBManager.getInstanse(getActivity());
                    manageInfoList.clear();
                    orderStr = " ywg_date asc";
                    List<ManageInfo> manageInfos = dbManager.queryManagerListData(cp, wxgz, assign, orderStr);
                    if(manageInfos!=null) {
                        manageInfoList.addAll(manageInfos);
                    }
                    //managerAdapter.setListData(manageInfoList);
                    managerAdapter.notifyDataSetChanged();
                    jiaocheshijian.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.sjx_down), null, null, null);
                    jiaocheshijian.setTag("0");
                }
                    break;
                    case R.id.my_renwu:
                        if(my_renwu.getTag().equals("0")) {
                            my_renwu.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.right_now), null, null, null);
                            my_renwu.setTag("1");
                            DBManager dbManager = DBManager.getInstanse(getActivity());
                            manageInfoList.clear();
                            assign = sp.getString(Constance.CHINESE_NAME);
                            List<ManageInfo> manageInfos = dbManager.queryManagerListData(cp, wxgz, assign, orderStr);
                            if (manageInfos != null && manageInfos.size() > 0){
                                manageInfoList.addAll(manageInfos);
                            }
                            managerAdapter.notifyDataSetChanged();
                        }else{
                            assign = "";
                            DBManager dbManager = DBManager.getInstanse(getActivity());
                            manageInfoList.clear();
                            List<ManageInfo> manageInfos = dbManager.queryManagerListData(cp, wxgz, assign, orderStr);
                            if (manageInfos != null && manageInfos.size() > 0){
                                manageInfoList.addAll(manageInfos);
                            }
                            managerAdapter.notifyDataSetChanged();
                            my_renwu.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.right_now_no), null, null, null);
                            my_renwu.setTag("0");
                        }
                    break;
                    case R.id.xuanzegongzhong:
                        initPopWindow();
                    break;
            case R.id.search_btn:
                if(search_cp.getText()!=null&&!TextUtils.isEmpty(search_cp.getText().toString().trim())) {
                    cp = search_cp.getText().toString().trim();
                    DBManager dbManager = DBManager.getInstanse(getActivity());
                    manageInfoList.clear();
                    List<ManageInfo> manageInfos = dbManager.queryManagerListData(cp, wxgz, assign, orderStr);
                    if (manageInfos != null && manageInfos.size() > 0){
                    manageInfoList.addAll(manageInfos);
                }
                    managerAdapter.notifyDataSetChanged();
                }else{
                    toastMsg = "请输入车牌";
                    mHandler.sendEmptyMessage(TOAST_MSG);
                }
                break;
            default:
                break;
        }
    }





    private void initPopWindow(){


        // 用于PopupWindow的View
        View contentView=LayoutInflater.from(getActivity()).inflate(R.layout.popwindow_bank_rate, null, false);
        ListView mListView = contentView.findViewById(R.id.listview);
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        final PopupWindow  mPopupWindow=new PopupWindow(contentView, Util.dp2px(getActivity(),120),
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.showAsDropDown(xuanzegongzhong);
        mPopupWindow.setTouchable(true); // 设置屏幕点击事件
        final DBManager dbManager = DBManager.getInstanse(getActivity());
        final List<String> wxgzList = new ArrayList<>();
        wxgzList.add("全部");
        wxgzList.addAll(dbManager.queryWxgzListData());
        WxgzListAdapter homeCarInfoAdapter = new WxgzListAdapter(getActivity(),wxgzList);//新建并配置ArrayAapeter
        mListView.setAdapter(homeCarInfoAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mPopupWindow.dismiss();
                String info = wxgzList.get(position);
                xuanzegongzhong.setText(info);
                manageInfoList.clear();

                List<ManageInfo> manageInfos = dbManager.queryManagerListData(cp, info, assign, orderStr);
                if(manageInfos!=null) {
                    manageInfoList.addAll(manageInfos);
                }
                //managerAdapter.setListData(manageInfoList);
                managerAdapter.notifyDataSetChanged();
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

}
