package com.shoujia.zhangshangxiu.msgcenter.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.entity.ManageInfo;
import com.shoujia.zhangshangxiu.entity.OrderBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/26 0026.
 */
public class QueryOrderAdapter extends BaseAdapter {
    private  List<OrderBean> listData;
    Context context;
    Handler handler;
    public QueryOrderAdapter(Context context, List<OrderBean> listData) {
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

    public void setListData(List<OrderBean> carInfos){
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
                    R.layout.activity_query_order_item, null);
            hodler.car_cur_order = convertView.findViewById(R.id.car_cur_order);
            hodler.cur_date = convertView.findViewById(R.id.cur_date);
            hodler.cur_cp = convertView.findViewById(R.id.cur_cp);
            hodler.car_gz = convertView.findViewById(R.id.car_gz);
            hodler.car_money = convertView.findViewById(R.id.car_money);
            convertView.setTag(hodler);
        }else{
            hodler = (Hodler) convertView.getTag();
        }
        OrderBean bean = listData.get(position);
        hodler.car_cur_order.setText(bean.getJsd_id());
        hodler.cur_cp.setText(bean.getCp());
        hodler.cur_date.setText(bean.getJc_date());
        hodler.car_gz.setText(bean.getWxgz_collect());
        hodler.car_money.setText(bean.getZje());



        return convertView;
    }

}

class Hodler {
    TextView car_cur_order;
    TextView cur_date;
    TextView cur_cp;
    TextView car_gz;
    TextView car_money;
}

