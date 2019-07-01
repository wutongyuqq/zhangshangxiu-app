package com.shoujia.zhangshangxiu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.TextView;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.util.Util;

public class ChooseProvinceDialog {
    private Context mContext;
    Dialog mDialog;
    private ChooseListener chooseListener;
        public ChooseProvinceDialog(Context context,ChooseListener chooseListener){
            this.mContext = context;
            this.chooseListener = chooseListener;
        }

    public void show() {
        //1、使用Dialog、设置style
        mDialog = new Dialog(mContext, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mContext, R.layout.dialog_meet_ta, null);
        GridLayout grid_layout = view.findViewById(R.id.grid_layout);

        setData(grid_layout);



        mDialog.setContentView(view);

        Window window = mDialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.show();

       /* dialog.findViewById(R.id.tv_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
*/
    }

    public void setData(GridLayout gridLayout) {
        gridLayout.setRowCount(5);
        gridLayout.setColumnCount(7);
        String[] proArr = {"闽","京","津","冀","晋","蒙","辽","吉","黑",
                "沪","苏","浙","皖","赣","鲁","豫","鄂",
                "湘","粤","桂","琼","川","贵","云","渝","藏",
                "陕","甘","青","宁","新","港","澳","台"};
        for (int i = 0; i < proArr.length; i++) {
                final TextView textView = new TextView(mContext);
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                textView.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                int paddingInt = Util.dp2px(mContext, 10);
                //  TO.dp2px() 是个自定义方法可以用TypedValue.applyDimension()代替
                textView.setPadding(paddingInt,paddingInt, paddingInt, paddingInt);
                textView.setGravity( Gravity.CENTER);
                textView.setText(proArr[i]);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            //左边的靠左，右边的靠右，中间的居中，默认居中
            params.setGravity(Gravity.CENTER);
            int marginInt = Util.dp2px(mContext, 3);
            params.setMargins(marginInt,marginInt,marginInt,marginInt);
            params.width = Util.dp2px(mContext, 40);
            params.height = Util.dp2px(mContext, 40);
            gridLayout.addView(textView, params);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                    if(textView.getText()!=null) {
                        chooseListener.choose(textView.getText().toString());
                    }else{
                        chooseListener.choose(null);
                    }
                }
            });
                //gridLayout.addView(textView);
        }
    }

    public interface ChooseListener{
            void choose(String proStr);
    }

}
