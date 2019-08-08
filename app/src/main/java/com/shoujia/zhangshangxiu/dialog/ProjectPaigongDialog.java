package com.shoujia.zhangshangxiu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.db.DBManager;
import com.shoujia.zhangshangxiu.entity.RepairInfo;
import com.shoujia.zhangshangxiu.order.adapter.DialogPaigongAdapter;
import com.shoujia.zhangshangxiu.order.adapter.DialogPaigongPersonAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProjectPaigongDialog {
    private Context mContext;
    Dialog mDialog;
    private OnClickListener onClickListener;

    TextView right_btn;
    TextView title;


    public ProjectPaigongDialog(Context context) {
        this.mContext = context;
    }
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }
    public void setTitle(String titleStr){
        if(title!=null&&!TextUtils.isEmpty(titleStr)){
            title.setText(titleStr);
        }
    }


    public void show() {
        //1、使用Dialog、设置style
        mDialog = new Dialog(mContext, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mContext, R.layout.dialog_peoject_paigong, null);
         ListView listview1 = view.findViewById(R.id.listview1);
         ListView listview2 = view.findViewById(R.id.listview2);
        title = view.findViewById(R.id.title);

        final DBManager dbManager = DBManager.getInstanse(mContext);
        RepairInfo sinfo = new RepairInfo();
        sinfo.setXlz("全部");
        final List<RepairInfo> repairInfos = new ArrayList<>();
        repairInfos.add(sinfo);
        repairInfos.addAll(dbManager.queryRepairZuListData());
        final List<RepairInfo> repairInfos2 = new ArrayList<>();
        final DialogPaigongAdapter adapter1 = new DialogPaigongAdapter(mContext,repairInfos);
        final DialogPaigongPersonAdapter adapter2 = new DialogPaigongPersonAdapter(mContext,repairInfos2);
        listview1.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();
        listview2.setAdapter(adapter2);
        if(repairInfos!=null&&repairInfos.size()>0){
            RepairInfo info = repairInfos.get(0);
            info.setSelected(true);
            List<RepairInfo> infos = dbManager.queryRepairPersonListData(info.getXlz());
            if(infos!=null&&infos.size()>0){
                repairInfos2.addAll(dbManager.queryRepairPersonListData(info.getXlz()));

            }
        }
        adapter2.notifyDataSetChanged();

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RepairInfo info3 = repairInfos.get(i);
                if(info3.isSelected()){

                }else{
                    for(RepairInfo rInfo:repairInfos){
                        rInfo.setSelected(false);
                    }
                    info3.setSelected(true);
                }
                adapter1.notifyDataSetChanged();
                List<RepairInfo> infos2 = dbManager.queryRepairPersonListData(info3.getXlz());
                repairInfos2.clear();
                if(infos2!=null&&infos2.size()>0){
                    repairInfos2.addAll(dbManager.queryRepairPersonListData(info3.getXlz()));
                }
                adapter2.notifyDataSetChanged();
            }
        });
        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RepairInfo info4= repairInfos2.get(i);
                if(info4.isSelected()){
                    info4.setSelected(false);
                }else{
                    info4.setSelected(true);
                }
                adapter2.notifyDataSetChanged();
            }
        });

        right_btn = view.findViewById(R.id.btn_right);
         TextView btn_left = view.findViewById(R.id.btn_left);
        mDialog.setContentView(view);
        Window window = mDialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.CENTER);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.show();
        right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    onClickListener.rightBtnClick(repairInfos2);
                    dismiss();
            }
        });
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    dismiss();
            }
        });

    }


    public void dismiss(){
        if(mDialog!=null) {
            mDialog.dismiss();
        }
    }


    public interface OnClickListener {
        void rightBtnClick(List<RepairInfo> repairInfos);
    }

}
