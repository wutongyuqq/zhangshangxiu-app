package com.shoujia.zhangshangxiu.order.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.order.entity.TwoBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/26 0026.
 */
public class PeijianSelectThreeAdapter extends BaseAdapter {
    private  List<TwoBean> listData;
    Context context;
    Handler handler;
    private EditClickListener editClickListener;
    private DeleteClickListener deleteClickListener;
    public PeijianSelectThreeAdapter(Context context, List<TwoBean> listData) {
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

    public void setListData(List<TwoBean> carInfos){
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
                    R.layout.peijian_select_three_item, null);
            hodler.name = convertView.findViewById(R.id.name);
            hodler.shuliang = convertView.findViewById(R.id.shuliang);
            convertView.setTag(hodler);
        }else{
            hodler = (Hodler) convertView.getTag();
        }
        hodler.name.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                listData.get(position).name = editable.toString();
            }
        });

        hodler.shuliang.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                listData.get(position).shuliang = editable.toString();
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
        EditText name;
        EditText shuliang;
    }
}





