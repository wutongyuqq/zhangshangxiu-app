package com.shoujia.zhangshangxiu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.util.Constance;
import com.shoujia.zhangshangxiu.util.SharePreferenceManager;

public class LoginOutDialog {
    private Context mContext;
    Dialog mDialog;
    private TextView content_line_one, btn_left, btn_right;
    private OnClickListener onClickListener;

    public LoginOutDialog(Context context) {
        this.mContext = context;
    }
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public void show() {
        //1、使用Dialog、设置style
        mDialog = new Dialog(mContext, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mContext, R.layout.dialog_logout_tip, null);
        btn_left = view.findViewById(R.id.btn_left);
        btn_right = view.findViewById(R.id.btn_right);
        content_line_one = view.findViewById(R.id.content_line_one);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClickListener!=null){
                    onClickListener.rightBtnClick();
                }
            }
        });
        mDialog.setContentView(view);
        Window window = mDialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.CENTER);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.show();
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
