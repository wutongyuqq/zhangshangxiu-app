package com.shoujia.zhangshangxiu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.db.DBManager;
import com.shoujia.zhangshangxiu.entity.ProjectBean;

public class OrderTempEditDialog {
    private Context mContext;
    Dialog mDialog;
    private OnClickListener onClickListener;
    private EditText edit_num;
    TextView right_btn,btn_left;
    ProjectBean mProjectBean;



    public OrderTempEditDialog(Context context,ProjectBean projectBean) {
        this.mContext = context;
        this.mProjectBean = projectBean;
    }
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }


    public void show() {
        //1、使用Dialog、设置style
        mDialog = new Dialog(mContext, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mContext, R.layout.dialog_order_edit, null);
        final EditText edit_name = view.findViewById(R.id.edit_pro_name);
        final EditText edit_wx_cb = view.findViewById(R.id.edit_wx_cb);
        final EditText edit_pro_jg = view.findViewById(R.id.edit_pro_jg);
        final EditText edit_pro_lb = view.findViewById(R.id.edit_pro_lb);
        final LinearLayout save_new_price = view.findViewById(R.id.save_new_price);
        final ImageView save_new_price_img = view.findViewById(R.id.save_new_price_img);
        right_btn = view.findViewById(R.id.btn_right);
        btn_left = view.findViewById(R.id.btn_left);
        edit_name.setText(mProjectBean.getXlxm());
        edit_wx_cb.setText(mProjectBean.getWxgz());
        edit_pro_jg.setText(mProjectBean.getXlf());
        edit_pro_lb.setText(mProjectBean.getZk());
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

                    if(edit_name.getText()!=null){
                        mProjectBean.setXlxm(edit_name.getText().toString());
                    }

                    if(edit_wx_cb.getText()!=null){
                        mProjectBean.setWxgz(edit_wx_cb.getText().toString());
                    }

                    if(edit_pro_jg.getText()!=null){
                        mProjectBean.setXlf(edit_pro_jg.getText().toString());
                    }

                    if(edit_pro_lb.getText()!=null){
                        mProjectBean.setZk(edit_pro_lb.getText().toString());
                    }
                    if(save_new_price.getTag().equals("1")){
                        DBManager dbManager = DBManager.getInstanse(mContext);
                        dbManager.updatePartsData(mProjectBean);

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
        save_new_price.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(view.getTag()!=null&&view.getTag().equals("0")){
                    view.setTag("1");
                    save_new_price_img.setImageResource(R.drawable.right_now);
                }else{
                    view.setTag("0");
                    save_new_price_img.setImageResource(R.drawable.right_now_no);
                }
            }
        });

    }


    public void dismiss(){
        if(mDialog!=null) {
            mDialog.dismiss();
        }
    }




    public interface OnClickListener {
        void rightBtnClick(ProjectBean newBean);
    }

}
