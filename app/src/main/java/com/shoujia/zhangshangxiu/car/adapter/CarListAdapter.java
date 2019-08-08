package com.shoujia.zhangshangxiu.car.adapter;

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
public class CarListAdapter extends BaseAdapter {
    private  List<ManageInfo> listData;
    Context context;
    Handler handler;
    public CarListAdapter(Context context, List<ManageInfo> listData) {
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
                    R.layout.car_list_item, null);
            hodler.tv_cp_name = convertView.findViewById(R.id.tv_cp_name);
            hodler.tv_status = convertView.findViewById(R.id.tv_status);
            hodler.car_cjh = convertView.findViewById(R.id.car_cjh);
            hodler.car_jcsj = convertView.findViewById(R.id.car_jcsj);
            hodler.car_jdry = convertView.findViewById(R.id.car_jdry);
            hodler.car_cx = convertView.findViewById(R.id.car_cx);
            convertView.setTag(hodler);
        }else{
            hodler = (Hodler) convertView.getTag();
        }
        ManageInfo bean = listData.get(position);
        hodler.tv_cp_name.setText("车牌："+bean.getCp());
        hodler.tv_status.setText("状态："+bean.getStates());
        hodler.car_cjh.setText("车架号："+bean.getCjhm());
        hodler.car_jcsj.setText("进厂时间："+bean.getJc_date());
        hodler.car_jdry.setText("接待人员："+bean.getJcr());
        hodler.car_cx.setText("车型："+bean.getCx());
        return convertView;
    }

}

class Hodler {
    TextView tv_cp_name;
    TextView tv_status;
    TextView car_cjh;
    TextView car_jcsj;
    TextView car_jdry;
    TextView car_cx;
}

