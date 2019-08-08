package com.shoujia.zhangshangxiu.order.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.order.entity.RateBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/26 0026.
 */
public class WxgzListAdapter extends BaseAdapter {
    private  List<String> listData;
    Context context;
    Handler handler;
    public WxgzListAdapter(Context context, List<String> listData) {
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

    public void setListData(List<String> carInfos){
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
                    R.layout.bank_list_item, null);
            hodler.tv_cp_name = convertView
                    .findViewById(R.id.tv_cp_name);

            convertView.setTag(hodler);
        }else{
            hodler = (Hodler) convertView.getTag();
        }
        String bean = listData.get(position);
       
        hodler.tv_cp_name.setText(bean);

        return convertView;
    }
    class Hodler {
        TextView tv_cp_name;

    }
}


