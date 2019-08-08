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
import com.shoujia.zhangshangxiu.entity.CarInfo;
import com.shoujia.zhangshangxiu.entity.ProjectBean;
import com.shoujia.zhangshangxiu.order.entity.OrderBeanInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/2/26 0026.
 */
public class ProjectOrderProAdapter extends BaseAdapter {
    private  List<ProjectBean> listData;
    Context context;
    Handler handler;
    private EditClickListener editClickListener;
    private DeleteClickListener deleteClickListener;
    public ProjectOrderProAdapter(Context context, List<ProjectBean> listData) {
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

    public void setEditClickListener(EditClickListener listener){
        this.editClickListener = listener;
    }

    public void setDeleteClickListener(DeleteClickListener listener){
        this.deleteClickListener = listener;
    }

    public void setListData(List<ProjectBean> carInfos){
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
                    R.layout.order_pro_item, null);
            hodler.tv_num = convertView.findViewById(R.id.tv_num);
            hodler.pro_name = convertView.findViewById(R.id.pro_name);
            hodler.pro_type = convertView.findViewById(R.id.pro_type);
            hodler.tv_xlf = convertView.findViewById(R.id.tv_xlf);
            hodler.tv_zk = convertView.findViewById(R.id.tv_zk);
            hodler.tv_edit = convertView.findViewById(R.id.tv_edit);
            hodler.tv_delete = convertView.findViewById(R.id.tv_delete);

            convertView.setTag(hodler);
        }else{
            hodler = (Hodler) convertView.getTag();
        }
        ProjectBean bean = listData.get(position);
        hodler.tv_num.setText((position+1)+"");
        hodler.pro_name.setText(bean.getXlxm());
        hodler.pro_type.setText(bean.getWxgz());
        hodler.tv_xlf.setText(bean.getXlf());
        hodler.tv_zk.setText(bean.getZk());
        hodler.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editClickListener!=null)
                editClickListener.editClick(position);
            }
        });

        hodler.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deleteClickListener!=null)
                deleteClickListener.deleteClick(position);
            }
        });
        if(OrderBeanInfo.notDelete){
            hodler.tv_delete.setVisibility(View.GONE);
        }

        return convertView;
    }
    public interface EditClickListener{
        void editClick(int position);
    }

    public interface DeleteClickListener{
        void deleteClick(int position);
    }

    class Hodler {
        TextView tv_num;
        TextView pro_name;
        TextView pro_type;
        TextView tv_xlf;
        TextView tv_zk;
        ImageView tv_edit;
        ImageView tv_delete;

    }
}





