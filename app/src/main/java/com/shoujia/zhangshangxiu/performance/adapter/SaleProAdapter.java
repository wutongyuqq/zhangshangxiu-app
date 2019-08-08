package com.shoujia.zhangshangxiu.performance.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.entity.JiedaiBaseInfo;
import com.shoujia.zhangshangxiu.entity.PeijianBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/26 0026.
 */
public class SaleProAdapter extends BaseAdapter {
    private  List<JiedaiBaseInfo> listData;
    Context context;


    public SaleProAdapter(Context context, List<JiedaiBaseInfo> listData) {
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


    public void setListData(List<JiedaiBaseInfo> carInfos){
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
                    R.layout.sale_pro_item, null);

            hodler.pro_name = convertView.findViewById(R.id.pro_name);
            hodler.pro_type = convertView.findViewById(R.id.pro_type);
            hodler.tv_xlf = convertView.findViewById(R.id.tv_xlf);
            hodler.tv_zk = convertView.findViewById(R.id.tv_zk);
            hodler.tv_hj = convertView.findViewById(R.id.tv_hj);

            convertView.setTag(hodler);
        }else{
            hodler = (Hodler) convertView.getTag();
        }
        JiedaiBaseInfo bean = listData.get(position);
        hodler.pro_name.setText(bean.service_time);
        hodler.pro_type.setText(bean.sale_time);
        hodler.tv_xlf.setText(bean.sale_money);
        hodler.tv_zk.setText(bean.sale_profit);
        hodler.tv_hj.setText(bean.sale_achievement);
        return convertView;
    }

    class Hodler {
        TextView pro_name;
        TextView pro_type;
        TextView tv_xlf;
        TextView tv_zk;
        TextView tv_hj;
    }
}





