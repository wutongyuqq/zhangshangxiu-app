package com.shoujia.zhangshangxiu.base;


import android.content.Context;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shoujia.zhangshangxiu.dialog.WaitProgressDialog;
import com.shoujia.zhangshangxiu.web.util.CustomProgressDialog;

import java.lang.ref.WeakReference;

//原有Fragment有状态丢失
//现有BaseFragment解决这个BUG
public abstract class BaseFragment extends Fragment{

	protected View view;
	protected final int TOAST_MSG = 1;
	protected final int TOAST_WITH_MSG = 10;
	protected final int RESULT_JSON = 2;
	protected String toastMsg = "";
	protected String resJson = "";
	protected MyHandler mHandler =null;
	private static WaitProgressDialog waitingDialog;

	// 回调函数:条件 当Fragment移除时
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (view != null) {
			// 将view从它的布局里面使用代码移除
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)// 已经被放到布局
			{
				parent.removeView(view);
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mHandler = new MyHandler(this);
		if (view == null) {
			view = createView(inflater, container, savedInstanceState);
		}
		return view;
	}

	public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
	public void updateUIThread(Message msg){

	}
	protected class MyHandler extends Handler {
		WeakReference<BaseFragment> mWeakReference;
		public MyHandler(BaseFragment activity)
		{
			mWeakReference=new WeakReference<BaseFragment>(activity);
		}
		@Override
		public void handleMessage(Message msg)
		{
			final BaseFragment activity=mWeakReference.get();
			if(activity!=null){
				if(msg.what==TOAST_MSG){
					Toast.makeText(getActivity(),toastMsg,Toast.LENGTH_LONG).show();
				}else{
					updateUIThread(msg);
				}
			}
		}
	}


	public static void showDialog(Context cont) {
		try {
			if (waitingDialog == null) {
				waitingDialog = WaitProgressDialog.createDialog(cont);
				// waitingDialog.setMessage("正在加载中...");
			}
			waitingDialog.setCanceledOnTouchOutside(false);
			waitingDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public static void dismissDialog() {
		try {
			if (waitingDialog != null) {
				// waitingDialog.dismiss();
				waitingDialog.dismiss();
				waitingDialog = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
