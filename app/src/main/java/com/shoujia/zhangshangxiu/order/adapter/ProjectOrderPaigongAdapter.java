package com.shoujia.zhangshangxiu.order.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.entity.PaigongInfo;
import com.shoujia.zhangshangxiu.entity.ProjectBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/26 0026.
 */
public class ProjectOrderPaigongAdapter extends BaseAdapter {
    private  List<PaigongInfo> listData;
    Context context;
    public ProjectOrderPaigongAdapter(Context context, List<PaigongInfo> listData) {
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


    public void setListData(List<PaigongInfo> carInfos){
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
                    R.layout.order_pro_paigong_item, null);

            hodler.pro_name = convertView.findViewById(R.id.pro_name);
            hodler.pro_type = convertView.findViewById(R.id.pro_type);
            hodler.tv_xlf = convertView.findViewById(R.id.tv_xlf);
            hodler.tv_zk = convertView.findViewById(R.id.tv_zk);
            hodler.tv_pg_zt = convertView.findViewById(R.id.tv_pg_zt);
            hodler.tv_select = convertView.findViewById(R.id.tv_select);


            convertView.setTag(hodler);
        }else{
            hodler = (Hodler) convertView.getTag();
        }
        PaigongInfo bean = listData.get(position);
        hodler.pro_name.setText("项目名称："+bean.getXlxm());
        hodler.pro_type.setText("指派给："+bean.getAssign());
        hodler.tv_xlf.setText("维修人员："+bean.getXlg());
        hodler.tv_zk.setText("派工金额："+bean.getPgje());
        hodler.tv_pg_zt.setText("派工状态："+bean.getStates());
        if(bean.isChecked()){
            hodler.tv_select.setImageResource(R.drawable.right_now);
        }else{
            hodler.tv_select.setImageResource(R.drawable.right_now_no);
        }

        return convertView;
    }


    class Hodler {
        ImageView tv_select;
        TextView pro_name;
        TextView pro_type;
        TextView tv_xlf;
        TextView tv_zk;
        TextView tv_pg_zt;

    }
}





