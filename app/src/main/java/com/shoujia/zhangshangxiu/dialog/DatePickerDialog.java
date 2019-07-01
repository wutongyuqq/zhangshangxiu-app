package com.shoujia.zhangshangxiu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.util.Util;

import java.util.Calendar;

public class DatePickerDialog {
    private Context mContext;
    Dialog mDialog;
    private ChooseListener chooseListener;
        public DatePickerDialog(Context context, ChooseListener chooseListener){
            this.mContext = context;
            this.chooseListener = chooseListener;
        }

    public void show() {
        Calendar calendar = Calendar.getInstance();
        //1、使用Dialog、设置style
        mDialog = new Dialog(mContext, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mContext, R.layout.dialog_date_picker, null);

        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
         TextView cancle = view.findViewById(R.id.cancle);
         TextView confirm = view.findViewById(R.id.confirm);

        //设置日期简略显示 否则详细显示 包括:星期\周
        datePicker.setCalendarViewShown(false);
        //初始化当前日期
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), null);
        //设置date布局
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //赋值后面闹钟使用
                int  mYear = datePicker.getYear();
                int mMonth = datePicker.getMonth();
                int mDay = datePicker.getDayOfMonth();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        mDialog.setContentView(view);
        Window window = mDialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.show();
    }



    public interface ChooseListener{
            void choose(String proStr);
    }

}
