package com.shoujia.zhangshangxiu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.db.DBManager;
import com.shoujia.zhangshangxiu.entity.ManageInfo;
import com.shoujia.zhangshangxiu.entity.ProjectBean;
import com.shoujia.zhangshangxiu.order.adapter.WxgzListAdapter;
import com.shoujia.zhangshangxiu.util.Util;

import java.util.ArrayList;
import java.util.List;

public class OrderAddTmpDialog {
    private Context mContext;
    Dialog mDialog;
    private OnClickListener onClickListener;
    private EditText edit_num;
    TextView right_btn,btn_left;
    TextView edit_pro_lb;



    public OrderAddTmpDialog(Context context) {
        this.mContext = context;
    }
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }


    public void show() {
        final ProjectBean mProjectBean = new ProjectBean();
        //1、使用Dialog、设置style
        mDialog = new Dialog(mContext, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mContext, R.layout.dialog_order_tmp, null);
        final EditText edit_name = view.findViewById(R.id.edit_pro_name);
        final EditText edit_wx_cb = view.findViewById(R.id.edit_wx_cb);
        final EditText edit_pro_jg = view.findViewById(R.id.edit_pro_jg);
        edit_pro_lb = view.findViewById(R.id.edit_pro_lb);
        right_btn = view.findViewById(R.id.btn_right);
        btn_left = view.findViewById(R.id.btn_left);
        edit_name.setText(mProjectBean.getXlxm());
        edit_wx_cb.setText(mProjectBean.getCb());
        edit_pro_jg.setText(mProjectBean.getXlf());
        edit_pro_lb.setText(mProjectBean.getWxgz());
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
                    if(edit_name.getText()!=null) {
                        mProjectBean.setXlxm(edit_name.getText().toString());
                    }

                    if(edit_wx_cb.getText()!=null){
                        mProjectBean.setCb(edit_wx_cb.getText().toString());
                    }

                    if(edit_pro_jg.getText()!=null){
                        mProjectBean.setXlf(edit_pro_jg.getText().toString());
                    }

                    if(edit_pro_lb.getText()!=null){
                        mProjectBean.setWxgz(edit_pro_lb.getText().toString());
                    }
                    onClickListener.rightBtnClick(mProjectBean);
                    dismiss();

            }
        });
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        edit_pro_lb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPopWindow();
            }
        });
    }



    private void initPopWindow(){


        // 用于PopupWindow的View
        View contentView=LayoutInflater.from(mContext).inflate(R.layout.popwindow_bank_rate, null, false);
        ListView mListView = contentView.findViewById(R.id.listview);
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        final PopupWindow mPopupWindow=new PopupWindow(contentView, Util.dp2px(mContext,220),
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.showAsDropDown(edit_pro_lb);
        mPopupWindow.setTouchable(true); // 设置屏幕点击事件
        final DBManager dbManager = DBManager.getInstanse(mContext);
        final List<String> wxgzList = new ArrayList<>();
        wxgzList.add("");
        wxgzList.addAll(dbManager.queryRepairListStringData());
        WxgzListAdapter homeCarInfoAdapter = new WxgzListAdapter(mContext,wxgzList);//新建并配置ArrayAapeter
        mListView.setAdapter(homeCarInfoAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mPopupWindow.dismiss();
                String info = wxgzList.get(position);
                edit_pro_lb.setText(info);

            }
        });

        // 设置PopupWindow的背景
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        mPopupWindow.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        mPopupWindow.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移

        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        // window.showAtLocation(parent, gravity, x, y);
    }


    public void dismiss(){
        if(mDialog!=null) {
            mDialog.dismiss();
        }
    }


    public void setData(GridLayout gridLayout) {

    }

    public interface OnClickListener {
        void rightBtnClick(ProjectBean newBean);
    }

}
