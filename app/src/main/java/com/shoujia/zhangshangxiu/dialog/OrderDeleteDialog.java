package com.shoujia.zhangshangxiu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;

public class OrderDeleteDialog {
    private Context mContext;
    Dialog mDialog;
    private OnClickListener onClickListener;
    String  mTitleStr;
    TextView right_btn,btn_left,name;


    public OrderDeleteDialog(Context context, String titleStr) {
        this.mContext = context;
        mTitleStr = titleStr;
    }
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }


    public void show() {
        //1、使用Dialog、设置style
        mDialog = new Dialog(mContext, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mContext, R.layout.dialog_order_delete, null);
        btn_left = view.findViewById(R.id.btn_left);
        right_btn = view.findViewById(R.id.btn_right);
        name = view.findViewById(R.id.name);
        name.setText(mTitleStr);
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
                    onClickListener.rightBtnClick();
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



    public void setData(GridLayout gridLayout) {

    }

    public interface OnClickListener {
        void rightBtnClick();
    }

}
