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
import com.shoujia.zhangshangxiu.entity.PeijianBean;
import com.shoujia.zhangshangxiu.entity.ProjectBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/26 0026.
 */
public class PeijianOrderProAdapter extends BaseAdapter {
    private  List<PeijianBean> listData;
    Context context;
    Handler handler;
    private EditClickListener editClickListener;
    private DeleteClickListener deleteClickListener;
    public PeijianOrderProAdapter(Context context, List<PeijianBean> listData) {
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

    public void setListData(List<PeijianBean> carInfos){
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
            //hodler.tv_hj = convertView.findViewById(R.id.tv_hj);

            convertView.setTag(hodler);
        }else{
            hodler = (Hodler) convertView.getTag();
        }
        PeijianBean bean = listData.get(position);
        hodler.tv_num.setText((position+1)+"");
        hodler.pro_name.setText(bean.getPjmc());
        hodler.pro_type.setText(bean.getPjbm());
        hodler.tv_xlf.setText(bean.getSl());
        hodler.tv_zk.setText(bean.getSsj());
        float totalMoney = Float.parseFloat(bean.getSl()) * Float.parseFloat(bean.getSsj());
        //hodler.tv_hj.setText( (float)(Math.round(totalMoney*100))/100+"");
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
       // TextView tv_hj;
        ImageView tv_edit;
        ImageView tv_delete;

    }
}





