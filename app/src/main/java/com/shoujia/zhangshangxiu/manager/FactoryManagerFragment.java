package com.shoujia.zhangshangxiu.manager;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseFragment;
import com.shoujia.zhangshangxiu.entity.ManageInfo;
import com.shoujia.zhangshangxiu.manager.adapter.FactoryManagerAdapter;
import com.shoujia.zhangshangxiu.manager.help.ManageDataHelper;

import java.util.ArrayList;
import java.util.List;

/*
* 车间管理
* */
public class FactoryManagerFragment extends BaseFragment implements View.OnClickListener {

    private ListView listview;
    private View mView;
    private List<ManageInfo> manageInfoList;
    private TextView prepare_work,doing_work,prepare_check,done_work;
    FactoryManagerAdapter managerAdapter;



    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = View.inflate(getActivity(), R.layout.fragment_factory_manager, null);
        initView();
        getServerData(0);
        return mView;
    }

    public void updateUIThread(Message msg) {
        int msgInt = msg.what;
        if(msgInt==201){
            if(manageInfoList!=null) {
                managerAdapter.setListData(manageInfoList);
                managerAdapter.notifyDataSetChanged();
            }else{
                managerAdapter.setListData(new ArrayList<ManageInfo>());
                managerAdapter.notifyDataSetChanged();
            }
        }
    }


    private View findViewById(int id){
        return mView.findViewById(id);
    }

    private void initView() {
        listview = mView.findViewById(R.id.listview);
        prepare_work = mView.findViewById(R.id.prepare_work);
        doing_work = mView.findViewById(R.id.doing_work);
        prepare_check = mView.findViewById(R.id.prepare_check);
        done_work = mView.findViewById(R.id.done_work);


        listview = mView.findViewById(R.id.listview);
        listview = mView.findViewById(R.id.listview);
        manageInfoList = new ArrayList<>();
        managerAdapter = new FactoryManagerAdapter(getContext(),manageInfoList);
        listview.setAdapter(managerAdapter);

        prepare_work.setOnClickListener(this);
        doing_work.setOnClickListener(this);
        prepare_check.setOnClickListener(this);
        done_work.setOnClickListener(this);

    }

    private void getServerData(int index){
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
        ManageDataHelper helper = new ManageDataHelper(getActivity());
        helper.setPreZero();
        helper.getListData(index, new ManageDataHelper.GetDataListener() {
            @Override
            public void getData(List<ManageInfo> manageInfos) {
                manageInfoList = manageInfos;
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
            default:
                break;
        }
    }


}
