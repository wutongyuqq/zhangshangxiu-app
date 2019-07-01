package com.shoujia.zhangshangxiu.introduce;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseFragment;
import com.shoujia.zhangshangxiu.car.CarListActivity;
import com.shoujia.zhangshangxiu.entity.IntroduceInfo;
import com.shoujia.zhangshangxiu.introduce.help.IntroduceDataHelper;

/*
* 整厂概况
*
* */
public class FactoryIntroduceFragment extends BaseFragment implements OnClickListener{
    private View mView;
    private TextView car_enter_factory,car_in_factory,car_out_factory,car_gj_factory,car_dpg_factory,car_dlg_factory,car_xlz_factory,car_dzj_factory,car_djs_factory,car_dcc_factory;
    private  IntroduceInfo mIntroduceInfo;
    private LinearLayout intro_gjz,intro_dpg,intro_dlg,intro_xlz,intro_dzj,intro_djs,intro_dcc;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = View.inflate(getActivity(), R.layout.fragment_factory_introduce, null);
        initView();
        initData();

        return mView;
    }
    public void updateUIThread(Message msg) {
        int msgInt = msg.what;
        if(msgInt==301){
            if(mIntroduceInfo!=null) {
                car_enter_factory.setText(mIntroduceInfo.getIncoming_today());
                car_in_factory.setText(mIntroduceInfo.getIn_garage());
                car_out_factory.setText(mIntroduceInfo.getOut_today());
                car_gj_factory.setText(mIntroduceInfo.getGujia());
                car_dpg_factory.setText(mIntroduceInfo.getDaipaigong());
                car_dlg_factory.setText(mIntroduceInfo.getDailinggong());
                car_xlz_factory.setText(mIntroduceInfo.getRepairing());
                car_dzj_factory.setText(mIntroduceInfo.getDaishenhe());
                car_djs_factory.setText(mIntroduceInfo.getDaijiesuan());
                car_dcc_factory.setText(mIntroduceInfo.getDaichuchang());
            }
        }

    }

    private View findViewById(int id){
        return mView.findViewById(id);
    }

    private void initView(){
        car_enter_factory = mView.findViewById(R.id.car_enter_factory);
        car_in_factory = mView.findViewById(R.id.car_in_factory);
        car_out_factory = mView.findViewById(R.id.car_out_factory);
        car_gj_factory = mView.findViewById(R.id.car_gj_factory);
        car_dpg_factory = mView.findViewById(R.id.car_dpg_factory);
        car_dlg_factory = mView.findViewById(R.id.car_dlg_factory);
        car_xlz_factory = mView.findViewById(R.id.car_xlz_factory);
        car_dzj_factory = mView.findViewById(R.id.car_dzj_factory);
        car_djs_factory = mView.findViewById(R.id.car_djs_factory);
        car_dcc_factory = mView.findViewById(R.id.car_dcc_factory);
        intro_gjz = mView.findViewById(R.id.intro_gjz);
        intro_dpg = mView.findViewById(R.id.intro_dpg);
        intro_dlg = mView.findViewById(R.id.intro_dlg);
        intro_xlz = mView.findViewById(R.id.intro_xlz);
        intro_dzj = mView.findViewById(R.id.intro_dzj);
        intro_djs = mView.findViewById(R.id.intro_djs);
        intro_dcc = mView.findViewById(R.id.intro_dcc);
        intro_gjz.setOnClickListener(this);
        intro_dpg.setOnClickListener(this);
        intro_dlg.setOnClickListener(this);
        intro_xlz.setOnClickListener(this);
        intro_dzj.setOnClickListener(this);
        intro_djs.setOnClickListener(this);
        intro_dcc.setOnClickListener(this);

    }


    public void initData() {
        IntroduceDataHelper introduceDataHelper = new IntroduceDataHelper(getActivity());
        introduceDataHelper.getData(new IntroduceDataHelper.GetDataListener() {
            @Override
            public void getData(IntroduceInfo info) {
                mIntroduceInfo = info;
                mHandler.sendEmptyMessage(301);
            }
        });

    }

    private void toCarListPage(String typeStr){
        Intent intent = new Intent(getActivity(),CarListActivity.class);
        intent.putExtra("typeStr",typeStr);
        getActivity().startActivity(intent);
    }

	@Override
	public void onClick(View v) {
        switch (v.getId()){
            case R.id.intro_gjz:
                toCarListPage("估价中");
                break;
            case R.id.intro_dpg:
                toCarListPage("待派工");
                break;
            case R.id.intro_dlg:
                toCarListPage("待领工");
                break;
            case R.id.intro_xlz:
                toCarListPage("修理中");
                break;
            case R.id.intro_dzj:
                toCarListPage("待质检");
                break;
            case R.id.intro_djs:
                toCarListPage("待结算");
                break;
            case R.id.intro_dcc:
                toCarListPage("待出厂");
                break;
            default:
                break;
        }

	}

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
