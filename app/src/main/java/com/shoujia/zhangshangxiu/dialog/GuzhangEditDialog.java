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
import com.shoujia.zhangshangxiu.util.DateUtil;

public class GuzhangEditDialog {
    private Context mContext;
    Dialog mDialog;
    private OnClickListener onClickListener;

    private EditText edit_num;
    TextView right_btn,tv_date;


    public GuzhangEditDialog(Context context) {
        this.mContext = context;

    }
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }


    public void show() {
        //1、使用Dialog、设置style
        mDialog = new Dialog(mContext, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mContext, R.layout.dialog_guzhang_edit, null);

        edit_num = view.findViewById(R.id.edit_num);
        right_btn = view.findViewById(R.id.btn_right);
        tv_date = view.findViewById(R.id.tv_date);
        tv_date.setText(DateUtil.getCurrentDate());
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
                if(edit_num.getText()!=null) {
                    String editNumStr = edit_num.getText().toString().trim();
                    onClickListener.rightBtnClick(editNumStr);
                    dismiss();
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
        void rightBtnClick(String numStr);
    }

}
