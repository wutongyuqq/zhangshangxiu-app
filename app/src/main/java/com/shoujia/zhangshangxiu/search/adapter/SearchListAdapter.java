package com.shoujia.zhangshangxiu.search.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.entity.CarInfo;


import java.util.List;

/**
 * Created by Administrator on 2017/2/26 0026.
 */
public class SearchListAdapter extends BaseAdapter {
    private  List<CarInfo> listData;
    Context context;
    Handler handler;
    public SearchListAdapter(Context context, List<CarInfo> listData) {
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

    public void setListData(List<CarInfo> carInfos){
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
                    R.layout.activity_search_item, null);
            hodler.car_cur_order = convertView.findViewById(R.id.car_cur_order);
            hodler.car_xllb = convertView.findViewById(R.id.car_xllb);
            hodler.car_jcrq = convertView.findViewById(R.id.car_jcrq);
            hodler.car_jclc = convertView.findViewById(R.id.car_jclc);
            convertView.setTag(hodler);
        }else{
            hodler = (Hodler) convertView.getTag();
        }
        CarInfo bean = listData.get(position);
        hodler.car_cur_order.setText("车牌："+bean.getMc());
        hodler.car_xllb.setText("车主："+bean.getCz());
        hodler.car_jcrq.setText("手机号："+bean.getMobile());
        hodler.car_jclc.setText("车型："+bean.getCx());

        return convertView;
    }

}

class Hodler {
    TextView car_cur_order;
    TextView car_xllb;
    TextView car_jcrq;
    TextView car_jclc;
}

