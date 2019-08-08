package com.shoujia.zhangshangxiu.home.adapter;

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
public class HomeCarInfoAdapter extends BaseAdapter {
    private  List<CarInfo> listData;
    Context context;
    Handler handler;
    public HomeCarInfoAdapter(Context context, List<CarInfo> listData) {
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
                    R.layout.first_fragment_item, null);
            hodler.tv_cp_name = convertView
                    .findViewById(R.id.tv_cp_name);

            convertView.setTag(hodler);
        }else{
            hodler = (Hodler) convertView.getTag();
        }
        CarInfo bean = listData.get(position);
       
        hodler.tv_cp_name.setText(bean.getMc()+"");
        
        return convertView;
    }
    class Hodler {
        TextView tv_cp_name;

    }
}


