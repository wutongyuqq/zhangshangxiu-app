package com.shoujia.zhangshangxiu.order.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.entity.PartsBean;
import com.shoujia.zhangshangxiu.entity.PeijianBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/26 0026.
 */
public class PeijianSelectOneAdapter extends BaseAdapter {
    private  List<PartsBean> listData;
    Context context;
    Handler handler;
    private EditClickListener editClickListener;
    private DeleteClickListener deleteClickListener;
    public PeijianSelectOneAdapter(Context context, List<PartsBean> listData) {
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

    public void setListData(List<PartsBean> carInfos){
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
                    R.layout.peijian_select_one_item, null);
            hodler.pj_name = convertView.findViewById(R.id.pj_name);
            hodler.pj_cang = convertView.findViewById(R.id.pj_cang);
            hodler.pj_xsj = convertView.findViewById(R.id.pj_xsj);
            hodler.pj_gg = convertView.findViewById(R.id.pj_gg);
            hodler.pj_kcl = convertView.findViewById(R.id.pj_kcl);
            hodler.pj_xh = convertView.findViewById(R.id.pj_xh);
            hodler.tv_select = convertView.findViewById(R.id.tv_select);
            hodler.select_one = convertView.findViewById(R.id.select_one);
            convertView.setTag(hodler);
        }else{
            hodler = (Hodler) convertView.getTag();
        }
        PartsBean bean = listData.get(position);
        if(bean!=null){
            hodler.pj_name.setText("名称："+bean.getPjmc());
            hodler.pj_cang.setText("仓库："+bean.getCd()+"号仓");
            hodler.pj_xsj.setText("销售价：￥"+bean.getXsj());
            hodler.pj_gg.setText("规格："+bean.getCd());
            hodler.pj_kcl.setText("库存量："+bean.getKcl());
            hodler.pj_xh.setText("配件型号："+bean.getCx());
        }
        final boolean isSelected = listData.get(position).isSelected();
        hodler.tv_select.setImageResource(isSelected?R.drawable.right_now:R.drawable.right_now_no);
        hodler.select_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSelected = listData.get(position).isSelected();
                listData.get(position).setSelected(!isSelected);
                notifyDataSetChanged();
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
        TextView pj_name;
        TextView pj_cang;
        TextView pj_xsj;
        TextView pj_gg;
        TextView pj_kcl;
        TextView pj_xh;
        ImageView tv_select;
        LinearLayout select_one;

    }
}





