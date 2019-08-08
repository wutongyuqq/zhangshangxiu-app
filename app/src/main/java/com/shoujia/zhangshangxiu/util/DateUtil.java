package com.shoujia.zhangshangxiu.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType){
        try {
            Date date = stringToDate(strTime, formatType); // String类型转成date类型
            if (date == null) {
                return 0;
            } else {
                long currentTime = dateToLong(date); // date类型转成long类型
                return currentTime;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formatType);
            Date date = null;
            date = formatter.parse(strTime);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
       return df.format(new Date());// new Date()为获取当前系统时间
    }


    public static String getCurrentDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }
}
