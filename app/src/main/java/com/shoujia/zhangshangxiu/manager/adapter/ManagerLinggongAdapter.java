package com.shoujia.zhangshangxiu.manager.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.entity.ManageInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/2/26 0026.
 */
public class ManagerLinggongAdapter extends BaseAdapter {
    private  List<ManageInfo> listData;
    Context context;
    Handler handler;
    public ManagerLinggongAdapter(Context context, List<ManageInfo> listData) {
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
                    R.layout.manager_linggong_item, null);
            hodler.repair_assign = convertView.findViewById(R.id.repair_assign);
            hodler.repair_type = convertView.findViewById(R.id.repair_type);
            hodler.repair_name = convertView.findViewById(R.id.repair_name);
            hodler.iv_select = convertView.findViewById(R.id.iv_select);
            convertView.setTag(hodler);
        }else{
            hodler = (Hodler) convertView.getTag();
        }
       ManageInfo info = listData.get(position);
        hodler.repair_name.setText(info.getXlxm());
        hodler.repair_type.setText(info.getWxgz());
        hodler.repair_assign.setText("指派给："+info.getAssign());
        if(info.isChecked()){
            hodler.iv_select.setImageResource(R.drawable.right_now);
        }else{
            hodler.iv_select.setImageResource(R.drawable.right_now_no);
        }

        return convertView;
    }

    class Hodler {
        TextView repair_assign;
        TextView repair_type;
        TextView repair_name;
        ImageView iv_select;
    }
}


