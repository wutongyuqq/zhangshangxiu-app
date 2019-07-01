package com.shoujia.zhangshangxiu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;

public class ProjectSelectDialog {
    private Context mContext;
    Dialog mDialog;
    private TextView title, content_line_one, content_line_two, btn_left, btn_right;
    private LinearLayout content_bottom_lay, btn_left_lay, btn_right_lay;
    private OnClickListener onClickListener;
    String mLeftBtnStr, mRightBtnStr, mTitleStr;

    public ProjectSelectDialog(Context context, String titleStr, String leftBtnStr, String rightBtnStr) {
        this.mContext = context;
        mLeftBtnStr = leftBtnStr;
        mRightBtnStr = rightBtnStr;
        mTitleStr = titleStr;
    }
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public void show() {
        //1、使用Dialog、设置style
        mDialog = new Dialog(mContext, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mContext, R.layout.dialog_peoject_select, null);
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
        void leftBtnClick();

        void rightBtnClick();
    }

}
