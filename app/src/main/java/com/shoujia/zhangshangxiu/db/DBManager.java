package com.shoujia.zhangshangxiu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.shoujia.zhangshangxiu.entity.CarInfo;
import com.shoujia.zhangshangxiu.entity.FirstIconInfo;
import com.shoujia.zhangshangxiu.entity.ManageInfo;
import com.shoujia.zhangshangxiu.entity.PartsBean;
import com.shoujia.zhangshangxiu.entity.PeijianBean;
import com.shoujia.zhangshangxiu.entity.ProjectBean;
import com.shoujia.zhangshangxiu.entity.RepairInfo;
import com.shoujia.zhangshangxiu.entity.SecondIconInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private static Context mContext;
    private static DBManager instanse = null;
    private static DBHelper dbHelper;

    public DBManager(Context context) {
        mContext = context;
    }

    public static DBManager getInstanse(Context context) {
        mContext = context;

        if (instanse == null) {
            instanse = new DBManager(context);
        }
        dbHelper = new DBHelper(context);
        return instanse;
    }


    public void inertOrUpdateDateBatch(List<String> sqls) {
        if (dbHelper == null) {
            return;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (String sql : sqls) {
                db.execSQL(sql);
            }
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();
        }
    }

    public long getTotalSize() {
        if (dbHelper == null) {
            return 0;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(id) from " + DBHelper.TABLE_NAME, null);
        cursor.moveToFirst();
        long count = cursor.getInt(0);
        cursor.close();
        return count;

    }
    public List<CarInfo> queryListData(String param) {
        return queryListData(param,true);
    }
    //获取汽车信息列表
    public List<CarInfo> queryListData(String param,boolean isLike) {
        List<CarInfo> beanList = new ArrayList<>();
        if (dbHelper == null) {
            return null;
        }
        String limitStr =  "50 OFFSET 0";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String sql = "SELECT  *  FROM " + DBHelper.TABLE_NAME +" LIMIT "+limitStr;
            if(TextUtils.isEmpty(param)){

            }else{
                sql = "SELECT  *  FROM " + DBHelper.TABLE_NAME + " WHERE mc like '%"+param+"%'" +" LIMIT "+limitStr;
            }
            if(!isLike){
                sql = "SELECT  *  FROM " + DBHelper.TABLE_NAME + " WHERE mc = '"+param+"'" +" LIMIT "+limitStr;
            }
            Cursor cursor = db.rawQuery(sql, null, null);//db.query(DBHelper.TABLE_NAME,null,nameStr,typeArr,null,null,"watch_num",limit);
            //String sql = "select * from "+DBHelper.TABLE_NAME+" where videotype";
            //Cursor cursor = db.rawQuery()
           if (cursor!=null) {
                while (cursor.moveToNext()) {
                    CarInfo bean = new CarInfo();
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String cjhm = cursor.getString(cursor.getColumnIndex("cjhm"));
                    String custom5 = cursor.getString(cursor.getColumnIndex("custom5"));
                    String customer_id = cursor.getString(cursor.getColumnIndex("customer_id"));
                    String cx = cursor.getString(cursor.getColumnIndex("cx"));
                    String cz = cursor.getString(cursor.getColumnIndex("cz"));
                    String fdjhm = cursor.getString(cursor.getColumnIndex("fdjhm"));
                    String linkman = cursor.getString(cursor.getColumnIndex("linkman"));
                    String mc = cursor.getString(cursor.getColumnIndex("mc"));
                    String mobile = cursor.getString(cursor.getColumnIndex("mobile"));
                    String ns_date = cursor.getString(cursor.getColumnIndex("ns_date"));
                    String openid = cursor.getString(cursor.getColumnIndex("openid"));
                    String phone = cursor.getString(cursor.getColumnIndex("phone"));
                    String vipnumber = cursor.getString(cursor.getColumnIndex("vipnumber"));
                    String gzms = cursor.getString(cursor.getColumnIndex("gzms"));
                    String gls = cursor.getString(cursor.getColumnIndex("gls"));
                    String memo = cursor.getString(cursor.getColumnIndex("memo"));
                    String keys_no = cursor.getString(cursor.getColumnIndex("keys_no"));

                    bean.setId(id);
                    bean.setCjhm(cjhm);
                    bean.setCustom5(custom5);
                    bean.setCustomer_id(customer_id);
                    bean.setCx(cx);
                    bean.setCz(cz);
                    bean.setFdjhm(fdjhm);
                    bean.setLinkman(linkman);
                    bean.setMc(mc);
                    bean.setMobile(mobile);
                    bean.setNs_date(ns_date);
                    bean.setOpenid(openid);
                    bean.setPhone(phone);
                    bean.setVipnumber(vipnumber);
                    bean.setGzms(gzms);
                    bean.setGls(gls);
                    bean.setMemo(memo);
                    bean.setKeys_no(keys_no);
                    beanList.add(bean);
                }
            }
            cursor.close();
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();

        }
        return beanList;
    }


    //获取汽车信息列表
    public List<CarInfo> queryAllListData() {
        List<CarInfo> beanList = new ArrayList<>();
        if (dbHelper == null) {
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String limitStr =  "50 OFFSET 0";
            String sql = "SELECT  *  FROM " + DBHelper.TABLE_NAME +" LIMIT "+limitStr;

            Cursor cursor = db.rawQuery(sql, null, null);//db.query(DBHelper.TABLE_NAME,null,nameStr,typeArr,null,null,"watch_num",limit);
            //String sql = "select * from "+DBHelper.TABLE_NAME+" where videotype";
            //Cursor cursor = db.rawQuery()
            if (cursor!=null) {
                while (cursor.moveToNext()) {
                    CarInfo bean = new CarInfo();
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String cjhm = cursor.getString(cursor.getColumnIndex("cjhm"));
                    String custom5 = cursor.getString(cursor.getColumnIndex("custom5"));
                    String customer_id = cursor.getString(cursor.getColumnIndex("customer_id"));
                    String cx = cursor.getString(cursor.getColumnIndex("cx"));
                    String cz = cursor.getString(cursor.getColumnIndex("cz"));
                    String fdjhm = cursor.getString(cursor.getColumnIndex("fdjhm"));
                    String linkman = cursor.getString(cursor.getColumnIndex("linkman"));
                    String mc = cursor.getString(cursor.getColumnIndex("mc"));
                    String mobile = cursor.getString(cursor.getColumnIndex("mobile"));
                    String ns_date = cursor.getString(cursor.getColumnIndex("ns_date"));
                    String openid = cursor.getString(cursor.getColumnIndex("openid"));
                    String phone = cursor.getString(cursor.getColumnIndex("phone"));
                    String vipnumber = cursor.getString(cursor.getColumnIndex("vipnumber"));
                    String gzms = cursor.getString(cursor.getColumnIndex("gzms"));
                    String gls = cursor.getString(cursor.getColumnIndex("gls"));
                    String memo = cursor.getString(cursor.getColumnIndex("memo"));
                    String keys_no = cursor.getString(cursor.getColumnIndex("keys_no"));

                    bean.setId(id);
                    bean.setCjhm(cjhm);
                    bean.setCustom5(custom5);
                    bean.setCustomer_id(customer_id);
                    bean.setCx(cx);
                    bean.setCz(cz);
                    bean.setFdjhm(fdjhm);
                    bean.setLinkman(linkman);
                    bean.setMc(mc);
                    bean.setMobile(mobile);
                    bean.setNs_date(ns_date);
                    bean.setOpenid(openid);
                    bean.setPhone(phone);
                    bean.setVipnumber(vipnumber);
                    bean.setGzms(gzms);
                    bean.setGls(gls);
                    bean.setMemo(memo);
                    bean.setKeys_no(keys_no);
                    beanList.add(bean);
                }
            }
            cursor.close();
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();

        }
        return beanList;
    }

    //获取汽车信息列表
    public List<CarInfo> querySearchListData(String param) {
        List<CarInfo> beanList = new ArrayList<>();
        if (dbHelper == null) {
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String sql = "SELECT  *  FROM " + DBHelper.TABLE_NAME;
            if(TextUtils.isEmpty(param)){
            }else{
                sql = "SELECT  *  FROM " + DBHelper.TABLE_NAME + " WHERE mc like '%"+param+"%' or mobile like '%"+param+"%' or vipnumber like '%"+param+"%'";
            }
            Cursor cursor = db.rawQuery(sql, null, null);//db.query(DBHelper.TABLE_NAME,null,nameStr,typeArr,null,null,"watch_num",limit);
            //String sql = "select * from "+DBHelper.TABLE_NAME+" where videotype";
            //Cursor cursor = db.rawQuery()
           if (cursor!=null) {
                while (cursor.moveToNext()) {
                    CarInfo bean = new CarInfo();
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String cjhm = cursor.getString(cursor.getColumnIndex("cjhm"));
                    String custom5 = cursor.getString(cursor.getColumnIndex("custom5"));
                    String customer_id = cursor.getString(cursor.getColumnIndex("customer_id"));
                    String cx = cursor.getString(cursor.getColumnIndex("cx"));
                    String cz = cursor.getString(cursor.getColumnIndex("cz"));
                    String fdjhm = cursor.getString(cursor.getColumnIndex("fdjhm"));
                    String linkman = cursor.getString(cursor.getColumnIndex("linkman"));
                    String mc = cursor.getString(cursor.getColumnIndex("mc"));
                    String mobile = cursor.getString(cursor.getColumnIndex("mobile"));
                    String ns_date = cursor.getString(cursor.getColumnIndex("ns_date"));
                    String openid = cursor.getString(cursor.getColumnIndex("openid"));
                    String phone = cursor.getString(cursor.getColumnIndex("phone"));
                    String vipnumber = cursor.getString(cursor.getColumnIndex("vipnumber"));
                    String gzms = cursor.getString(cursor.getColumnIndex("gzms"));
                    String gls = cursor.getString(cursor.getColumnIndex("gls"));
                    String memo = cursor.getString(cursor.getColumnIndex("memo"));
                    String keys_no = cursor.getString(cursor.getColumnIndex("keys_no"));

                    bean.setId(id);
                    bean.setCjhm(cjhm);
                    bean.setCustom5(custom5);
                    bean.setCustomer_id(customer_id);
                    bean.setCx(cx);
                    bean.setCz(cz);
                    bean.setFdjhm(fdjhm);
                    bean.setLinkman(linkman);
                    bean.setMc(mc);
                    bean.setMobile(mobile);
                    bean.setNs_date(ns_date);
                    bean.setOpenid(openid);
                    bean.setPhone(phone);
                    bean.setVipnumber(vipnumber);
                    bean.setGzms(gzms);
                    bean.setGls(gls);
                    bean.setMemo(memo);
                    bean.setKeys_no(keys_no);
                    beanList.add(bean);
                }
            }
            cursor.close();
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();

        }
        return beanList;
    }
    //插入汽车信息列表
    public void insertListData(List<CarInfo> carInfos) {

        if (dbHelper == null || carInfos == null) {
            return;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i=0;i<carInfos.size();i++) {
                CarInfo car = carInfos.get(i);
                values.put("id",i);
                values.put("cjhm", car.getCjhm());
                values.put("custom5", car.getCustom5());
                values.put("customer_id", car.getCustomer_id());
                values.put("cx", car.getCx());
                values.put("cz", car.getCz());
                values.put("fdjhm", car.getFdjhm());
                values.put("linkman", car.getLinkman());
                values.put("mc", car.getMc());
                values.put("mobile", car.getMobile());
                values.put("ns_date", car.getNs_date());
                values.put("openid", car.getOpenid());
                values.put("phone", car.getPhone());
                values.put("vipnumber", car.getVipnumber());
                values.put("gzms", car.getGzms());
                values.put("gls", car.getGls());
                values.put("memo", car.getMemo());
                values.put("keys_no", car.getKeys_no());
                db.insert(DBHelper.TABLE_NAME, null, values);
            }
            //加上的代码
            db.setTransactionSuccessful();
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
            db.endTransaction();
            //db.close();
        }

    }

    //插入汽车信息
    public void insertOrUpdateCarInfo(CarInfo car) {
        List<CarInfo> beanList = new ArrayList<>();
        if (dbHelper == null || car == null) {
            return;
        }
        List<CarInfo> carInfos = queryListData(car.getMc());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            if(carInfos!=null&&carInfos.size()>0){
                CarInfo oldCar = carInfos.get(0);
                ContentValues values = new ContentValues();
                values.put("id",oldCar.getId());
                values.put("cjhm", car.getCjhm());
                values.put("custom5", car.getCustom5());
                values.put("customer_id", oldCar.getCustomer_id());
                values.put("cx", car.getCx());
                values.put("cz", car.getCz());
                values.put("fdjhm", oldCar.getFdjhm());
                values.put("linkman", car.getLinkman());
                values.put("mc", car.getMc());
                values.put("mobile", car.getMobile());
                values.put("ns_date", car.getNs_date());
                values.put("openid", oldCar.getOpenid());
                values.put("phone", oldCar.getPhone());
                values.put("vipnumber", oldCar.getVipnumber());
                values.put("gzms", car.getGzms());
                values.put("gls", car.getGls());
                values.put("memo", car.getMemo());
                values.put("keys_no", car.getKeys_no());
                //修改条件
                String whereClause = "mc=?";
                //修改添加参数
                String[] whereArgs={car.getMc()};
                db.update(DBHelper.TABLE_NAME, values, whereClause,whereArgs);

            }else{

                ContentValues values = new ContentValues();

                    values.put("id",0);
                    values.put("cjhm", car.getCjhm());
                    values.put("custom5", car.getCustom5());
                    values.put("customer_id", car.getCustomer_id());
                    values.put("cx", car.getCx());
                    values.put("cz", car.getCz());
                    values.put("fdjhm", car.getFdjhm());
                    values.put("linkman", car.getLinkman());
                    values.put("mc", car.getMc());
                    values.put("mobile", car.getMobile());
                    values.put("ns_date", car.getNs_date());
                    values.put("openid", car.getOpenid());
                    values.put("phone", car.getPhone());
                    values.put("vipnumber", car.getVipnumber());
                    values.put("gzms", car.getGzms());
                    values.put("gls", car.getGls());
                    values.put("memo", car.getMemo());
                    values.put("keys_no", car.getKeys_no());
                    db.insert(DBHelper.TABLE_NAME, null, values);

            }

            //加上的代码
            db.setTransactionSuccessful();
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }

    }

    //获取修理工信息列表
    public List<RepairInfo> queryRepairListData() {
        List<RepairInfo> beanList = new ArrayList<>();
        if (dbHelper == null) {
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String sql = "SELECT  *  FROM " + DBHelper.REPAIR_TABLE_NAME;
            Cursor cursor = db.rawQuery(sql, null, null);//db.query(DBHelper.TABLE_NAME,null,nameStr,typeArr,null,null,"watch_num",limit);
            //String sql = "select * from "+DBHelper.TABLE_NAME+" where videotype";
            //Cursor cursor = db.rawQuery()
           if (cursor!=null) {
                while (cursor.moveToNext()) {
                    RepairInfo bean = new RepairInfo();
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String xlz = cursor.getString(cursor.getColumnIndex("xlz"));
                    String xlg = cursor.getString(cursor.getColumnIndex("xlg"));
                    bean.setId(id);
                    bean.setXlz(xlz);
                    bean.setXlg(xlg);
                    beanList.add(bean);
                }
            }
            cursor.close();
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();

        }
        return beanList;
    }



    //获取修理工信息列表
    public List<String> queryRepairListStringData() {
        List<String> beanList = new ArrayList<>();
        if (dbHelper == null) {
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String sql = "SELECT  distinct xlz   FROM " + DBHelper.REPAIR_TABLE_NAME ;
            Cursor cursor = db.rawQuery(sql, null, null);//db.query(DBHelper.TABLE_NAME,null,nameStr,typeArr,null,null,"watch_num",limit);
            //String sql = "select * from "+DBHelper.TABLE_NAME+" where videotype";
            //Cursor cursor = db.rawQuery()
            if (cursor!=null) {
                while (cursor.moveToNext()) {

                    String xlz = cursor.getString(cursor.getColumnIndex("xlz"));
                    beanList.add(xlz);
                }
            }
            cursor.close();
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();

        }
        return beanList;
    }


    //获取修理组信息列表
    public List<RepairInfo> queryRepairZuListData() {
        List<RepairInfo> beanList = new ArrayList<>();
        if (dbHelper == null) {
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String sql = "SELECT  distinct xlz  FROM " + DBHelper.REPAIR_TABLE_NAME;
            Cursor cursor = db.rawQuery(sql, null, null);//db.query(DBHelper.TABLE_NAME,null,nameStr,typeArr,null,null,"watch_num",limit);
            //String sql = "select * from "+DBHelper.TABLE_NAME+" where videotype";
            //Cursor cursor = db.rawQuery()
            if (cursor!=null) {
                while (cursor.moveToNext()) {
                    RepairInfo bean = new RepairInfo();
                    String xlz = cursor.getString(cursor.getColumnIndex("xlz"));
                    bean.setXlz(xlz);
                    beanList.add(bean);
                }
            }
            cursor.close();
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();

        }
        return beanList;
    }




    //获取修理组信息列表
    public List<RepairInfo> queryRepairPersonListData(String xlzStr) {
        List<RepairInfo> beanList = new ArrayList<>();
        if (dbHelper == null) {
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String sql = "SELECT  distinct xlg  FROM " + DBHelper.REPAIR_TABLE_NAME + " where xlz='"+xlzStr+"'";
            if(xlzStr.equals("全部")){
                 sql = "SELECT  distinct xlg  FROM " + DBHelper.REPAIR_TABLE_NAME;
            }
            Cursor cursor = db.rawQuery(sql, null, null);//db.query(DBHelper.TABLE_NAME,null,nameStr,typeArr,null,null,"watch_num",limit);
            //String sql = "select * from "+DBHelper.TABLE_NAME+" where videotype";
            //Cursor cursor = db.rawQuery()
            if (cursor!=null) {
                while (cursor.moveToNext()) {
                    RepairInfo bean = new RepairInfo();
                    String xlg = cursor.getString(cursor.getColumnIndex("xlg"));
                    bean.setXlg(xlg);
                    beanList.add(bean);
                }
            }
            cursor.close();
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();

        }
        return beanList;
    }



    //插入汽车信息列表
    public void insertRepairListData(List<RepairInfo> repairInfos) {
        if (dbHelper == null || repairInfos == null) {
            return;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String deleteSql = "DELETE FROM " + DBHelper.REPAIR_TABLE_NAME;
            db.execSQL(deleteSql);

           // ContentValues values = new ContentValues();
            for (RepairInfo info : repairInfos) {
               // values.put("xlg", info.getXlg());
                //values.put("xlz", info.getXlz());
               // INSERT INTO table_name (列1, 列2,...) VALUES (值1, 值2,....)
                String insertSql = "INSERT INTO " + DBHelper.REPAIR_TABLE_NAME+"(xlg,xlz) VALUES('"+ info.getXlg()+"','"+info.getXlz()+"') ";
                //db.insert(DBHelper.REPAIR_TABLE_NAME, null, values);
                db.execSQL(insertSql);
            }
            //加上的代码
            db.setTransactionSuccessful();
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
            db.endTransaction();
            //db.close();
        }
    }


    //插入汽车信息列表
    public void deleteCarList() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String deleteSql = "DELETE FROM " + DBHelper.TABLE_NAME;
            db.execSQL(deleteSql);
            //加上的代码
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    //获取一级页面图标数据
    public List<FirstIconInfo> queryFirstIconListData() {
        List<FirstIconInfo> beanList = new ArrayList<>();
        if (dbHelper == null) {
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String sql = "SELECT  *  FROM " + DBHelper.FIRST_ICON_TABLE_NAME;
            Cursor cursor = db.rawQuery(sql, null, null);//db.query(DBHelper.TABLE_NAME,null,nameStr,typeArr,null,null,"watch_num",limit);
            //String sql = "select * from "+DBHelper.TABLE_NAME+" where videotype";
            //Cursor cursor = db.rawQuery()
           if (cursor!=null) {
                while (cursor.moveToNext()) {
                    FirstIconInfo info = new FirstIconInfo();
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String imageurl = cursor.getString(cursor.getColumnIndex("imageurl"));
                    String wxgz = cursor.getString(cursor.getColumnIndex("wxgz"));
                    info.setId(id);
                    info.setImageurl(imageurl);
                    info.setWxgz(wxgz);
                    beanList.add(info);
                }
            }
            cursor.close();
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();

        }
        return beanList;
    }


    //插入一级页面数据列表
    public void insertFirstIconListData(List<FirstIconInfo> repairInfos) {
        List<FirstIconInfo> beanList = new ArrayList<>();
        if (dbHelper == null || repairInfos == null) {
            return;
        }


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        db.execSQL("delete from "+DBHelper.FIRST_ICON_TABLE_NAME);
        try {

            ContentValues values = new ContentValues();
            for (FirstIconInfo info : repairInfos) {
                values.put("imageurl", info.getImageurl());
                values.put("wxgz", info.getWxgz());
                db.insert(DBHelper.FIRST_ICON_TABLE_NAME, null, values);
            }
            //加上的代码
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            //db.close();
        }
    }

    public List<PartsBean> getPartsListData(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<PartsBean> partsBeans = new ArrayList<>();
        try{
            String sql = "SELECT  *  FROM " + DBHelper.PARTS_INFO_TABLE_NAME;
            db.beginTransaction();
            Cursor cursor = db.rawQuery(sql, null, null);//db.query(DBHelper.TABLE_NAME,null,nameStr,typeArr,null,null,"watch_num",limit);
            if (cursor!=null) {
                while (cursor.moveToNext()) {
                    PartsBean bean = new PartsBean();
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String pjbm = cursor.getString(cursor.getColumnIndex("pjbm"));
                    String pjmc = cursor.getString(cursor.getColumnIndex("pjmc"));
                    String ck = cursor.getString(cursor.getColumnIndex("ck"));
                    String cd = cursor.getString(cursor.getColumnIndex("cd"));
                    String cx = cursor.getString(cursor.getColumnIndex("cx"));
                    String dw = cursor.getString(cursor.getColumnIndex("dw"));
                    String cangwei = cursor.getString(cursor.getColumnIndex("cangwei"));
                    String bz = cursor.getString(cursor.getColumnIndex("bz"));
                    String type = cursor.getString(cursor.getColumnIndex("type"));
                    String kcl = cursor.getString(cursor.getColumnIndex("kcl"));
                    String xsj = cursor.getString(cursor.getColumnIndex("xsj"));
                    String pjjj = cursor.getString(cursor.getColumnIndex("pjjj"));
                    bean.setPjbm(pjbm);
                    bean.setPjmc(pjmc);
                    bean.setCk(ck);
                    bean.setCd(cd);
                    bean.setCx(cx);
                    bean.setDw(dw);
                    bean.setCangwei(cangwei);
                    bean.setType(type);
                    bean.setBz(bz);
                    bean.setKcl(kcl);
                    bean.setXsj(xsj);
                    bean.setPjjj(pjjj);
                    partsBeans.add(bean);
                }
            }
            //加上的代码
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return partsBeans;
    }




    public List<PartsBean> getPartsListData(String pjmcStr){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<PartsBean> partsBeans = new ArrayList<>();
        try{
            String sql = "SELECT  *  FROM " + DBHelper.PARTS_INFO_TABLE_NAME +" where pjmc like '%"+pjmcStr+"%'";
            db.beginTransaction();
            Cursor cursor = db.rawQuery(sql, null, null);//db.query(DBHelper.TABLE_NAME,null,nameStr,typeArr,null,null,"watch_num",limit);
            if (cursor!=null) {
                while (cursor.moveToNext()) {
                    PartsBean bean = new PartsBean();
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String pjbm = cursor.getString(cursor.getColumnIndex("pjbm"));
                    String pjmc = cursor.getString(cursor.getColumnIndex("pjmc"));
                    String ck = cursor.getString(cursor.getColumnIndex("ck"));
                    String cd = cursor.getString(cursor.getColumnIndex("cd"));
                    String cx = cursor.getString(cursor.getColumnIndex("cx"));
                    String dw = cursor.getString(cursor.getColumnIndex("dw"));
                    String cangwei = cursor.getString(cursor.getColumnIndex("cangwei"));
                    String bz = cursor.getString(cursor.getColumnIndex("bz"));
                    String type = cursor.getString(cursor.getColumnIndex("type"));
                    String kcl = cursor.getString(cursor.getColumnIndex("kcl"));
                    String xsj = cursor.getString(cursor.getColumnIndex("xsj"));
                    String pjjj = cursor.getString(cursor.getColumnIndex("pjjj"));
                    bean.setPjbm(pjbm);
                    bean.setPjmc(pjmc);
                    bean.setCk(ck);
                    bean.setCd(cd);
                    bean.setCx(cx);
                    bean.setDw(dw);
                    bean.setCangwei(cangwei);
                    bean.setType(type);
                    bean.setBz(bz);
                    bean.setKcl(kcl);
                    bean.setXsj(xsj);
                    bean.setPjjj(pjjj);
                    partsBeans.add(bean);
                }
            }
            //加上的代码
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return partsBeans;
    }

    //插入一级页面数据列表
    public void insertPartsListData(List<PartsBean> partsBeans) {
        if (dbHelper == null || partsBeans == null) {
            return;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        db.execSQL("delete from "+DBHelper.PARTS_INFO_TABLE_NAME);
        try {

            ContentValues values = new ContentValues();
            for (PartsBean info : partsBeans) {
                values.put("pjbm", info.getPjbm());
                values.put("pjmc", info.getPjmc());
                values.put("ck", info.getCk());
                values.put("cd", info.getCd());
                values.put("cx", info.getCx());
                values.put("dw", info.getDw());
                values.put("cangwei", info.getCangwei());
                values.put("bz", info.getBz());
                values.put("type", info.getType());
                values.put("kcl", info.getKcl());
                values.put("xsj", info.getXsj());
                values.put("pjjj", info.getPjjj());
                db.insert(DBHelper.PARTS_INFO_TABLE_NAME, null, values);
            }
            //加上的代码
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            //db.close();
        }
    }


    //插入一级页面数据列表
    public void updatePartsData(ProjectBean projectsBean) {
        if (dbHelper == null || projectsBean == null) {
            return;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String  updateSql ="update "+DBHelper.SECOND_ICON_TABLE_NAME+" set xlf='"+projectsBean.getXlf()+"' where mc='"+projectsBean.getXlxm()+"'";
            db.execSQL(updateSql);
            //加上的代码
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    //插入一级页面数据列表
    public void updatePeijianData(PeijianBean peijianBean) {
        if (dbHelper == null || peijianBean == null) {
            return;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String  updateSql ="update "+DBHelper.PARTS_INFO_TABLE_NAME+" set xsj='"+peijianBean.getSsj()+"' where pjmc='"+peijianBean.getPjmc()+"'";
            db.execSQL(updateSql);
            //加上的代码
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    //获取汽车信息列表
    public List<SecondIconInfo> querySecondIconListData() {
        List<SecondIconInfo> beanList = new ArrayList<>();
        if (dbHelper == null) {
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String sql = "SELECT  *  FROM " + DBHelper.SECOND_ICON_TABLE_NAME;
            Cursor cursor = db.rawQuery(sql, null, null);//db.query(DBHelper.TABLE_NAME,null,nameStr,typeArr,null,null,"watch_num",limit);
            //String sql = "select * from "+DBHelper.TABLE_NAME+" where videotype";
            //Cursor cursor = db.rawQuery()
            if (cursor!=null) {
                while (cursor.moveToNext()) {
                    SecondIconInfo bean = new SecondIconInfo();
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String cx = cursor.getString(cursor.getColumnIndex("cx"));
                    String is_quick_project = cursor.getString(cursor.getColumnIndex("is_quick_project"));
                    String lb = cursor.getString(cursor.getColumnIndex("lb"));
                    String mc = cursor.getString(cursor.getColumnIndex("mc"));
                    String pgzgs = cursor.getString(cursor.getColumnIndex("pgzgs"));
                    String pycode = cursor.getString(cursor.getColumnIndex("pycode"));
                    String spj = cursor.getString(cursor.getColumnIndex("spj"));
                    String tybz = cursor.getString(cursor.getColumnIndex("tybz"));
                    String wxgz = cursor.getString(cursor.getColumnIndex("wxgz"));
                    String xlf = cursor.getString(cursor.getColumnIndex("xlf"));

                    bean.setId(id);
                   bean.setCx(cx);
                   bean.setIs_quick_project(is_quick_project);
                   bean.setLb(lb);
                   bean.setMc(mc);
                   bean.setPgzgs(pgzgs);
                   bean.setPycode(pycode);
                   bean.setSpj(spj);
                   bean.setTybz(tybz);
                   bean.setWxgz(wxgz);
                   bean.setXlf(xlf);
                    beanList.add(bean);
                }
            }
            cursor.close();
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();

        }
        return beanList;
    }


    //获取汽车信息列表
    public List<SecondIconInfo> querySecondIconListData(String name) {
        List<SecondIconInfo> beanList = new ArrayList<>();
        if (dbHelper == null) {
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String sql = "SELECT  *  FROM " + DBHelper.SECOND_ICON_TABLE_NAME + " where wxgz='"+name+"'";
            Cursor cursor = db.rawQuery(sql, null, null);//db.query(DBHelper.TABLE_NAME,null,nameStr,typeArr,null,null,"watch_num",limit);
            //String sql = "select * from "+DBHelper.TABLE_NAME+" where videotype";
            //Cursor cursor = db.rawQuery()
            if (cursor!=null) {
                while (cursor.moveToNext()) {
                    SecondIconInfo bean = new SecondIconInfo();
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String cx = cursor.getString(cursor.getColumnIndex("cx"));
                    String is_quick_project = cursor.getString(cursor.getColumnIndex("is_quick_project"));
                    String lb = cursor.getString(cursor.getColumnIndex("lb"));
                    String mc = cursor.getString(cursor.getColumnIndex("mc"));
                    String pgzgs = cursor.getString(cursor.getColumnIndex("pgzgs"));
                    String pycode = cursor.getString(cursor.getColumnIndex("pycode"));
                    String spj = cursor.getString(cursor.getColumnIndex("spj"));
                    String tybz = cursor.getString(cursor.getColumnIndex("tybz"));
                    String wxgz = cursor.getString(cursor.getColumnIndex("wxgz"));
                    String xlf = cursor.getString(cursor.getColumnIndex("xlf"));

                    bean.setId(id);
                    bean.setCx(cx);
                    bean.setIs_quick_project(is_quick_project);
                    bean.setLb(lb);
                    bean.setMc(mc);
                    bean.setPgzgs(pgzgs);
                    bean.setPycode(pycode);
                    bean.setSpj(spj);
                    bean.setTybz(tybz);
                    bean.setWxgz(wxgz);
                    bean.setXlf(xlf);
                    beanList.add(bean);
                }
            }
            cursor.close();
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();

        }
        return beanList;
    }



    //获取汽车信息列表
    public SecondIconInfo querySecondIconData(int id) {
        List<SecondIconInfo> beanList = new ArrayList<>();
        if (dbHelper == null) {
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String sql = "SELECT  *  FROM " + DBHelper.SECOND_ICON_TABLE_NAME + " where id="+id;
            Cursor cursor = db.rawQuery(sql, null, null);//db.query(DBHelper.TABLE_NAME,null,nameStr,typeArr,null,null,"watch_num",limit);
            //String sql = "select * from "+DBHelper.TABLE_NAME+" where videotype";
            //Cursor cursor = db.rawQuery()
            if (cursor!=null) {
                while (cursor.moveToNext()) {
                    SecondIconInfo bean = new SecondIconInfo();
                    int sid = cursor.getInt(cursor.getColumnIndex("id"));
                    String cx = cursor.getString(cursor.getColumnIndex("cx"));
                    String is_quick_project = cursor.getString(cursor.getColumnIndex("is_quick_project"));
                    String lb = cursor.getString(cursor.getColumnIndex("lb"));
                    String mc = cursor.getString(cursor.getColumnIndex("mc"));
                    String pgzgs = cursor.getString(cursor.getColumnIndex("pgzgs"));
                    String pycode = cursor.getString(cursor.getColumnIndex("pycode"));
                    String spj = cursor.getString(cursor.getColumnIndex("spj"));
                    String tybz = cursor.getString(cursor.getColumnIndex("tybz"));
                    String wxgz = cursor.getString(cursor.getColumnIndex("wxgz"));
                    String xlf = cursor.getString(cursor.getColumnIndex("xlf"));

                    bean.setId(sid);
                    bean.setCx(cx);
                    bean.setIs_quick_project(is_quick_project);
                    bean.setLb(lb);
                    bean.setMc(mc);
                    bean.setPgzgs(pgzgs);
                    bean.setPycode(pycode);
                    bean.setSpj(spj);
                    bean.setTybz(tybz);
                    bean.setWxgz(wxgz);
                    bean.setXlf(xlf);
                    beanList.add(bean);
                }
            }
            cursor.close();
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();

        }
        if(beanList!=null&&beanList.size()>0){
            return beanList.get(0);
        }
        return null;
    }

    //插入汽车信息列表
    public void insertSecondIconListData(List<SecondIconInfo> infos) {
        if (dbHelper == null || infos == null) {
            return;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("delete from "+DBHelper.SECOND_ICON_TABLE_NAME);
            ContentValues values = new ContentValues();
            for (SecondIconInfo bean : infos) {
                values.put("cx", bean.getCx());
                values.put("is_quick_project", bean.getIs_quick_project());
                values.put("lb", bean.getLb());
                values.put("mc", bean.getMc());
                values.put("pgzgs", bean.getPgzgs());
                values.put("pycode", bean.getPgzgs());
                values.put("spj", bean.getSpj());
                values.put("tybz", bean.getTybz());
                values.put("wxgz",bean.getWxgz());
                values.put("xlf", bean.getXlf());
                db.insert(DBHelper.SECOND_ICON_TABLE_NAME, null, values);
            }
            //加上的代码
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            //db.close();
        }

    }



    //插入汽车信息列表
    public void insertManagerListData(List<ManageInfo> infos) {
        List<CarInfo> beanList = new ArrayList<>();
        if (dbHelper == null || infos == null) {
            return;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("delete from "+DBHelper.MANAGER_INFO_TABLE_NAME);
            ContentValues values = new ContentValues();
            for (ManageInfo bean : infos) {
                values.put("assign", bean.getAssign());
                values.put("cjhm", bean.getCjhm());
                values.put("cp", bean.getCp());
                values.put("cx", bean.getCx());
                values.put("jc_date", bean.getJc_date());
                values.put("jsd_id", bean.getJsd_id());
                values.put("states", bean.getStates());
                values.put("wxgz", bean.getWxgz());
                values.put("xlg",bean.getXlg());
                values.put("ywg_date", bean.getYwg_date());
                values.put("jcr", bean.getJcr());
                db.insert(DBHelper.MANAGER_INFO_TABLE_NAME, null, values);
            }
            //加上的代码
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }

    }





    //获取汽车信息列表
    public List<ManageInfo> queryManagerListData(String  orderByStr) {
        List<ManageInfo> beanList = new ArrayList<>();
        if (dbHelper == null) {
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String sql = "SELECT  *  FROM " + DBHelper.MANAGER_INFO_TABLE_NAME + "  ORDER BY "+ orderByStr;
            Cursor cursor = db.rawQuery(sql, null, null);//db.query(DBHelper.TABLE_NAME,null,nameStr,typeArr,null,null,"watch_num",limit);
            //String sql = "select * from "+DBHelper.TABLE_NAME+" where videotype";
            //Cursor cursor = db.rawQuery()
            if (cursor!=null) {
                while (cursor.moveToNext()) {
                    ManageInfo bean = new ManageInfo();

                    String assign = cursor.getString(cursor.getColumnIndex("assign"));
                    String cjhm = cursor.getString(cursor.getColumnIndex("cjhm"));
                    String cp = cursor.getString(cursor.getColumnIndex("cp"));
                    String cx = cursor.getString(cursor.getColumnIndex("cx"));
                    String jc_date = cursor.getString(cursor.getColumnIndex("jc_date"));
                    String jsd_id = cursor.getString(cursor.getColumnIndex("jsd_id"));
                    String states = cursor.getString(cursor.getColumnIndex("states"));
                    String wxgz = cursor.getString(cursor.getColumnIndex("wxgz"));
                    String xlg = cursor.getString(cursor.getColumnIndex("xlg"));
                    String ywg_date = cursor.getString(cursor.getColumnIndex("ywg_date"));


                    bean.setCx(cx);
                    bean.setAssign(assign);
                    bean.setCjhm(cjhm);
                    bean.setCp(cp);
                    bean.setJc_date(jc_date);
                    bean.setJsd_id(jsd_id);
                    bean.setStates(states);
                    bean.setXlg(xlg);
                    bean.setWxgz(wxgz);
                    bean.setYwg_date(ywg_date);
                    beanList.add(bean);
                }
            }
            cursor.close();
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();

        }
        if(beanList!=null&&beanList.size()>0){
            return beanList;
        }
        return null;
    }





    //获取汽车信息列表
    public List<ManageInfo> queryManagerListData(String  cp,String wxgz,String assign,String orderStr) {
        List<ManageInfo> beanList = new ArrayList<>();
        if (dbHelper == null) {
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String sql = "SELECT  *  FROM " + DBHelper.MANAGER_INFO_TABLE_NAME +" where 1=1";//+ "  where assign like '%"+ name+"%'";

            if(!TextUtils.isEmpty(assign)){
                sql = sql+" and  (assign like '%"+ assign +"%' or xlg like '%"+assign+"%')";
            }

            if(!TextUtils.isEmpty(wxgz)&&!wxgz.equals("全部")){
                sql = sql+" and  wxgz like '%"+ wxgz +"%'";
            }



            if(!TextUtils.isEmpty(cp)){
                sql = sql+" and  cp like '%"+ cp +"%'";
            }

            if(!TextUtils.isEmpty(orderStr)){
                sql = sql+"  ORDER BY "+ orderStr;
            }

            Cursor cursor = db.rawQuery(sql, null, null);//db.query(DBHelper.TABLE_NAME,null,nameStr,typeArr,null,null,"watch_num",limit);
            //String sql = "select * from "+DBHelper.TABLE_NAME+" where videotype";
            //Cursor cursor = db.rawQuery()
            if (cursor!=null) {
                while (cursor.moveToNext()) {
                    ManageInfo bean = new ManageInfo();

                    String assign1 = cursor.getString(cursor.getColumnIndex("assign"));
                    String cjhm = cursor.getString(cursor.getColumnIndex("cjhm"));
                    String cp1 = cursor.getString(cursor.getColumnIndex("cp"));
                    String cx = cursor.getString(cursor.getColumnIndex("cx"));
                    String jc_date = cursor.getString(cursor.getColumnIndex("jc_date"));
                    String jsd_id = cursor.getString(cursor.getColumnIndex("jsd_id"));
                    String states = cursor.getString(cursor.getColumnIndex("states"));
                    String wxgz1 = cursor.getString(cursor.getColumnIndex("wxgz"));
                    String xlg = cursor.getString(cursor.getColumnIndex("xlg"));
                    String ywg_date = cursor.getString(cursor.getColumnIndex("ywg_date"));
                    String jcr = cursor.getString(cursor.getColumnIndex("jcr"));


                    bean.setCx(cx);
                    bean.setAssign(assign1);
                    bean.setCjhm(cjhm);
                    bean.setCp(cp1);
                    bean.setJc_date(jc_date);
                    bean.setJsd_id(jsd_id);
                    bean.setStates(states);
                    bean.setXlg(xlg);
                    bean.setWxgz(wxgz1);
                    bean.setYwg_date(ywg_date);
                    bean.setJcr(jcr);
                    beanList.add(bean);
                }
            }
            cursor.close();
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();

        }
        if(beanList!=null&&beanList.size()>0){
            return beanList;
        }
        return null;
    }



    //获取汽车信息列表
    public List<ManageInfo> queryManagerList(String  cp,String states,String assign,String orderStr) {
        List<ManageInfo> beanList = new ArrayList<>();
        if (dbHelper == null) {
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String sql = "SELECT  *  FROM " + DBHelper.MANAGER_INFO_TABLE_NAME +" where 1=1";//+ "  where assign like '%"+ name+"%'";

            if(!TextUtils.isEmpty(assign)){
                sql = sql+" and  (assign like '%"+ assign +"%' or xlg like '%"+assign+"%')";
            }

            if(!TextUtils.isEmpty(states)&&!states.equals("全部")){
                sql = sql+" and  states like '%"+ states +"%'";
            }



            if(!TextUtils.isEmpty(cp)){
                sql = sql+" and  cp like '%"+ cp +"%'";
            }

            if(!TextUtils.isEmpty(orderStr)){
                sql = sql+"  ORDER BY "+ orderStr;
            }

            Cursor cursor = db.rawQuery(sql, null, null);//db.query(DBHelper.TABLE_NAME,null,nameStr,typeArr,null,null,"watch_num",limit);
            //String sql = "select * from "+DBHelper.TABLE_NAME+" where videotype";
            //Cursor cursor = db.rawQuery()
            if (cursor!=null) {
                while (cursor.moveToNext()) {
                    ManageInfo bean = new ManageInfo();

                    String assign1 = cursor.getString(cursor.getColumnIndex("assign"));
                    String cjhm = cursor.getString(cursor.getColumnIndex("cjhm"));
                    String cp1 = cursor.getString(cursor.getColumnIndex("cp"));
                    String cx = cursor.getString(cursor.getColumnIndex("cx"));
                    String jc_date = cursor.getString(cursor.getColumnIndex("jc_date"));
                    String jsd_id = cursor.getString(cursor.getColumnIndex("jsd_id"));
                    String states1 = cursor.getString(cursor.getColumnIndex("states"));
                    String wxgz1 = cursor.getString(cursor.getColumnIndex("wxgz"));
                    String xlg = cursor.getString(cursor.getColumnIndex("xlg"));
                    String ywg_date = cursor.getString(cursor.getColumnIndex("ywg_date"));
                    String jcr = cursor.getString(cursor.getColumnIndex("jcr"));


                    bean.setCx(cx);
                    bean.setAssign(assign1);
                    bean.setCjhm(cjhm);
                    bean.setCp(cp1);
                    bean.setJc_date(jc_date);
                    bean.setJsd_id(jsd_id);
                    bean.setStates(states1);
                    bean.setXlg(xlg);
                    bean.setWxgz(wxgz1);
                    bean.setYwg_date(ywg_date);
                    bean.setJcr(jcr);
                    beanList.add(bean);
                }
            }
            cursor.close();
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();

        }
        if(beanList!=null&&beanList.size()>0){
            return beanList;
        }
        return null;
    }



    //获取汽车信息列表
    public List<ManageInfo> queryWxgzListData(String  name) {
        List<ManageInfo> beanList = new ArrayList<>();
        if (dbHelper == null) {
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String sql = "SELECT  *  FROM " + DBHelper.MANAGER_INFO_TABLE_NAME + "  where wxgz = '"+ name+"'";
            if(name.equals("全部")){
                sql = "SELECT  *  FROM " + DBHelper.MANAGER_INFO_TABLE_NAME;
            }
            Cursor cursor = db.rawQuery(sql, null, null);//db.query(DBHelper.TABLE_NAME,null,nameStr,typeArr,null,null,"watch_num",limit);
            //String sql = "select * from "+DBHelper.TABLE_NAME+" where videotype";
            //Cursor cursor = db.rawQuery()
            if (cursor!=null) {
                while (cursor.moveToNext()) {
                    ManageInfo bean = new ManageInfo();

                    String assign = cursor.getString(cursor.getColumnIndex("assign"));
                    String cjhm = cursor.getString(cursor.getColumnIndex("cjhm"));
                    String cp = cursor.getString(cursor.getColumnIndex("cp"));
                    String cx = cursor.getString(cursor.getColumnIndex("cx"));
                    String jc_date = cursor.getString(cursor.getColumnIndex("jc_date"));
                    String jsd_id = cursor.getString(cursor.getColumnIndex("jsd_id"));
                    String states = cursor.getString(cursor.getColumnIndex("states"));
                    String wxgz = cursor.getString(cursor.getColumnIndex("wxgz"));
                    String xlg = cursor.getString(cursor.getColumnIndex("xlg"));
                    String ywg_date = cursor.getString(cursor.getColumnIndex("ywg_date"));


                    bean.setCx(cx);
                    bean.setAssign(assign);
                    bean.setCjhm(cjhm);
                    bean.setCp(cp);
                    bean.setJc_date(jc_date);
                    bean.setJsd_id(jsd_id);
                    bean.setStates(states);
                    bean.setXlg(xlg);
                    bean.setWxgz(wxgz);
                    bean.setYwg_date(ywg_date);
                    beanList.add(bean);
                }
            }
            cursor.close();
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();

        }
        if(beanList!=null&&beanList.size()>0){
            return beanList;
        }
        return null;
    }



    //获取修理组信息列表
    public List<String> queryWxgzListData() {
        List<String> beanList = new ArrayList<>();
        if (dbHelper == null) {
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String sql = "SELECT  distinct wxgz  FROM " + DBHelper.MANAGER_INFO_TABLE_NAME;
            Cursor cursor = db.rawQuery(sql, null, null);//db.query(DBHelper.TABLE_NAME,null,nameStr,typeArr,null,null,"watch_num",limit);
            //String sql = "select * from "+DBHelper.TABLE_NAME+" where videotype";
            //Cursor cursor = db.rawQuery()
            if (cursor!=null) {
                while (cursor.moveToNext()) {
                    String wxgz = cursor.getString(cursor.getColumnIndex("wxgz"));
                    if(!TextUtils.isEmpty(wxgz)) {
                        beanList.add(wxgz);
                    }
                }
            }
            cursor.close();
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();

        }
        return beanList;
    }


    public void close(){
        if(dbHelper!=null) {
            dbHelper.close();
        }
    }

}
