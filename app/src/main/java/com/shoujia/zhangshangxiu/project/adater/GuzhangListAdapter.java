package com.shoujia.zhangshangxiu.project.adater;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.entity.CarInfo;
import com.shoujia.zhangshangxiu.entity.GuzhangInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/2/26 0026.
 */
public class GuzhangListAdapter extends BaseAdapter {
    private  List<GuzhangInfo> listData;
    Context context;
    public GuzhangListAdapter(Context context, List<GuzhangInfo> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
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
                    R.layout.activity_guzhang_item, null);
            hodler.jc_date = convertView.findViewById(R.id.jc_date);
            hodler.gzms = convertView.findViewById(R.id.gzms);
            convertView.setTag(hodler);
        }else{
            hodler = (Hodler) convertView.getTag();
        }
        GuzhangInfo bean = listData.get(position);
        hodler.jc_date.setText("进厂时间："+bean.getDays());
        hodler.gzms.setText("故障描述："+bean.getFault_info());
        return convertView;
    }
    class Hodler {
        TextView jc_date;
        TextView gzms;
    }
}



