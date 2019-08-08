package com.shoujia.zhangshangxiu.manager.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.entity.CarInfo;
import com.shoujia.zhangshangxiu.entity.ManageInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/2/26 0026.
 */
public class FactoryManagerAdapter extends BaseAdapter {
    private  List<ManageInfo> listData;
    Context context;
    Handler handler;
    public FactoryManagerAdapter(Context context, List<ManageInfo> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        if (listData.size() == 0) {
            return 0;
        }
        return listData.size();
    }

    public void setListData(List<ManageInfo> carInfos){
        if(carInfos==null){
            return;
        }
        if(listData!=null){
            listData.clear();
        }
        listData.addAll(carInfos);
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Hodler hodler;
        if (convertView == null) {
            hodler = new Hodler();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.factory_manager_item, null);
            hodler.tv_cp_name = convertView.findViewById(R.id.tv_cp_name);
            hodler.tv_jsd_id = convertView.findViewById(R.id.tv_jsd_id);
            hodler.car_num = convertView.findViewById(R.id.car_num);
            hodler.project_statu = convertView.findViewById(R.id.project_statu);
            hodler.car_type = convertView.findViewById(R.id.car_type);
            hodler.wxgz_type = convertView.findViewById(R.id.wxgz_type);
            hodler.lgry_type = convertView.findViewById(R.id.lgry_type);
            hodler.zpry_type = convertView.findViewById(R.id.zpry_type);
            hodler.enter_date = convertView.findViewById(R.id.enter_date);
            convertView.setTag(hodler);
        }else{
            hodler = (Hodler) convertView.getTag();
        }
       ManageInfo info = listData.get(position);
        hodler.tv_cp_name.setText("车牌："+info.getCp());
        hodler.tv_jsd_id.setText("结算单："+info.getJsd_id());
        hodler.car_num.setText("车架号码："+info.getCjhm());
        hodler.project_statu.setText("项目状态："+info.getStates());
        hodler.car_type.setText("车型："+info.getCx());
        hodler.wxgz_type.setText("维修工种："+info.getWxgz());
        hodler.lgry_type.setText("领工人员："+info.getAssign());
        hodler.zpry_type.setText("指派人员："+info.getXlg());
        String jc_date = info.getJc_date();
        if(info.getJc_date().length()>10){
            jc_date = info.getJc_date().substring(0,10);
        }
        hodler.enter_date.setText("进厂日期："+jc_date);
        
        return convertView;
    }

    class Hodler {
        TextView tv_cp_name;
        TextView tv_jsd_id;
        TextView car_num;
        TextView project_statu;
        TextView car_type;
        TextView wxgz_type;
        TextView lgry_type;
        TextView zpry_type;
        TextView enter_date;
    }
}


