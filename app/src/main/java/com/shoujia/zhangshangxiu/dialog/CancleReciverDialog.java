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

public class CancleReciverDialog {
    private Context mContext;
    Dialog mDialog;
    private TextView title, content_line_one, content_line_two, btn_left, btn_right;
    private LinearLayout content_bottom_lay, btn_left_lay, btn_right_lay;
    private OnClickListener onClickListener;
    String mLeftBtnStr, mRightBtnStr, mTitleStr;

    public CancleReciverDialog(Context context, String titleStr, String leftBtnStr, String rightBtnStr) {
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
        View view = View.inflate(mContext, R.layout.dialog_common_tip, null);
        title = view.findViewById(R.id.title);
        content_line_one = view.findViewById(R.id.content_line_one);
        content_line_two = view.findViewById(R.id.content_line_two);
        btn_left = view.findViewById(R.id.btn_left);
        btn_right = view.findViewById(R.id.btn_right);
        content_bottom_lay = view.findViewById(R.id.content_bottom_lay);
        btn_left_lay = view.findViewById(R.id.btn_left_lay);
        btn_right_lay = view.findViewById(R.id.btn_right_lay);
        if(!TextUtils.isEmpty(mTitleStr)){
            title.setVisibility(View.VISIBLE);
        }

        if(!TextUtils.isEmpty(mLeftBtnStr)){
            btn_left_lay.setVisibility(View.VISIBLE);
            content_bottom_lay.setVisibility(View.VISIBLE);
            btn_left.setText(mLeftBtnStr);
            btn_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onClickListener!=null) {
                        onClickListener.leftBtnClick();
                    }
                }
            });
        }

        if(!TextUtils.isEmpty(mRightBtnStr)){
            btn_right_lay.setVisibility(View.VISIBLE);
            content_bottom_lay.setVisibility(View.VISIBLE);
            btn_right.setText(mRightBtnStr);
            btn_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onClickListener!=null) {
                        onClickListener.rightBtnClick();
                    }
                }
            });
        }

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

    public void setContent(String content1,String content2){
        if(!TextUtils.isEmpty(content1)){
            content_line_one.setVisibility(View.VISIBLE);
            content_line_one.setText(content1);
        }

        if(!TextUtils.isEmpty(content2)){
            content_line_two.setVisibility(View.VISIBLE);
            content_line_two.setText(content2);
        }

    }

    public void setData(GridLayout gridLayout) {

    }

    public interface OnClickListener {
        void leftBtnClick();

        void rightBtnClick();
    }

}
