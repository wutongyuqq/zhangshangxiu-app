package com.shoujia.zhangshangxiu.util;

import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author zhulin  zhulin.zhuy@gmail.com
 * 
 * @date 2014-12-6
 */
public class MyParcel implements Parcelable{

	protected HashMap<String, Object> map = null;

	/**
	 * 
	 */
	public MyParcel() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeMap(map);
	}
	
	@SuppressWarnings("unchecked")
	 public static final Parcelable.Creator<MyParcel> CREATOR = new Parcelable.Creator<MyParcel>() {
		@Override
		public MyParcel createFromParcel(Parcel source) {
			MyParcel result = new MyParcel();
			result.map = source.readHashMap(HashMap.class.getClassLoader());
			return result;
		}

		@Override
		public MyParcel[] newArray(int size) {
			return null;
		} 
		 
	 };

	public void setMap(HashMap<String, Object> map) {
		this.map = map;
	}

	public HashMap<String, Object> getMap() {
		return map;
	}

}
