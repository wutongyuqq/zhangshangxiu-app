package com.shoujia.zhangshangxiu.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceManager {
    static SharedPreferences sp;
    private final String SP_NAME="SP_NAME";
    public SharePreferenceManager(Context context){
        init(context,SP_NAME);
    }
    public static void init(Context context, String name) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    private static final String KEY_CACHED_USERNAME = "jchat_cached_username";

    public static String getCachedUsername() {
        if (null != sp) {
            return sp.getString(KEY_CACHED_USERNAME, null);
        }
        return null;
    }


    public  void putBoolean(String key,boolean value) {
        if (null != sp) {
            sp.edit().putBoolean(key, value).apply();
        }
    }

    public  boolean getBoolean(String key) {
        if (null != sp) {
            return sp.getBoolean(key,false);
        }
        return false;
    }

    public  void putString(String key,String value) {
        if(value == null){
            sp.edit().putString(key, "").apply();
        }
        if (null != sp) {
            sp.edit().putString(key, value).apply();
        }
    }

    public  String getString(String key) {
        if (null != sp) {
           return sp.getString(key, "");
        }
        return "";
    }
    public  void putInt(String key,int value) {
        if (null != sp) {
            sp.edit().putInt(key, value).apply();
        }
    }

    public  int getInt(String key) {
        if (null != sp) {
           return sp.getInt(key,0);
        }
        return 0;
    }
}
