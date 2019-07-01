package com.shoujia.zhangshangxiu.history.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.entity.ManageInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/2/26 0026.
 */
public class HistoryListAdapter extends BaseAdapter {
    private  List<ManageInfo> listData;
    Context context;
    Handler handler;
    public HistoryListAdapter(Context context, List<ManageInfo> listData) {
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
                    R.layout.activity_history_item, null);
            hodler.car_cur_order = convertView.findViewById(R.id.car_cur_order);
            hodler.car_xllb = convertView.findViewById(R.id.car_xllb);
            hodler.car_jcrq = convertView.findViewById(R.id.car_jcrq);
            hodler.car_jclc = convertView.findViewById(R.id.car_jclc);
            convertView.setTag(hodler);
        }else{
            hodler = (Hodler) convertView.getTag();
        }
        ManageInfo bean = listData.get(position);
        hodler.car_cur_order.setText("本次工单："+bean.getJsd_id());
        hodler.car_xllb.setText("修理类别："+bean.getXllb());
        hodler.car_jcrq.setText("进厂日期："+bean.getJc_date());
        hodler.car_jclc.setText("进厂里程："+bean.getJclc());

        return convertView;
    }

}

class Hodler {
    TextView car_cur_order;
    TextView car_xllb;
    TextView car_jcrq;
    TextView car_jclc;
}

