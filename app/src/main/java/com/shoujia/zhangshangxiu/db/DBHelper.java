package com.shoujia.zhangshangxiu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // 数据库文件名
    public static final String DB_NAME = "my_database.db";
    // 数据库表名
    public static final String TABLE_NAME = "car_info";
    // 修理工数据库表名
    public static final String REPAIR_TABLE_NAME = "repair_info";
    //一级页面数据
    public static final String FIRST_ICON_TABLE_NAME = "first_icon";

    //二级页面数据
    public static final String SECOND_ICON_TABLE_NAME = "second_icon";

    //配件数据
    public static final String PARTS_INFO_TABLE_NAME = "parts_info";

    //车间管理
    public static final String MANAGER_INFO_TABLE_NAME = "manager_info";

    // 数据库版本号
    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 当数据库文件创建时，执行初始化操作，并且只执行一次
    @Override
    public void onCreate(SQLiteDatabase db) {
        //删除表
        String createSql = "drop table if exists "+ TABLE_NAME;
        String createRepairSql = "drop table if exists "+ REPAIR_TABLE_NAME;
        String createFirstIconSql = "drop table if exists "+ FIRST_ICON_TABLE_NAME;
        String createSecondIconSql = "drop table if exists "+ SECOND_ICON_TABLE_NAME;
        String createPartSql = "drop table if exists "+ PARTS_INFO_TABLE_NAME;
        String managerSql = "drop table if exists "+ MANAGER_INFO_TABLE_NAME;
        db.execSQL(createSql);
        db.execSQL(createRepairSql);
        db.execSQL(createFirstIconSql);
        db.execSQL(createSecondIconSql);
        db.execSQL(createPartSql);
        db.execSQL(managerSql);
        // 建表
        String sql = "create table " +
                TABLE_NAME +
                "(id integer default 0, " +
                 "cjhm varchar, " +
                 "custom5 varchar," +
                "customer_id varchar," +
                 "cx varchar," +
                 "cz varchar," +
                 "fdjhm varchar," +
                 "linkman varchar," +
                 "mc varchar," +
                 "mobile varchar," +
                 "ns_date varchar," +
                 "openid varchar," +
                 "phone varchar," +
                 "gzms varchar," +
                 "gls varchar," +
                 "memo varchar," +
                 "keys_no varchar," +
                "vipnumber varchar"
                + ")";


        // 建修理工表
        String sqlRepair = "create table " +
                REPAIR_TABLE_NAME +
                "(id integer primary key autoincrement, " +
                "xlz varchar, " +
                "xlg varchar"
                + ")";

        // 建一级页面icon表
        String sqlFirstIcon = "create table " +
                FIRST_ICON_TABLE_NAME +
                "(id integer primary key autoincrement, " +
                "imageurl varchar, " +
                "wxgz varchar"
                + ")";

        // 建一级页面icon表
        String sqlSeconndIcon = "create table " +
                SECOND_ICON_TABLE_NAME +
                "(id integer primary key autoincrement, " +
                "cx varchar, " +
                "is_quick_project varchar,"+
                "lb varchar,"+
                "mc varchar,"+
                "pgzgs varchar,"+
                "pycode varchar,"+
                "spj varchar,"+
                "tybz varchar,"+
                "wxgz varchar,"+
                "xlf varchar"
                + ")";

        // 建配件数据表
        String sqlPartsInfo = "create table " +
                PARTS_INFO_TABLE_NAME +
                "(id integer primary key autoincrement, " +
                "pjbm varchar, " +
                "pjmc varchar,"+
                "ck varchar,"+
                "cd varchar,"+
                "cx varchar,"+
                "dw varchar,"+
                "cangwei varchar,"+
                "bz varchar,"+
                "type varchar,"+
                "kcl varchar,"+
                "xsj varchar,"+
                "pjjj varchar"
                + ")";

        // 建配件数据表
        String sqlManagerInfo = "create table " +
                MANAGER_INFO_TABLE_NAME +
                "(id integer primary key autoincrement, " +
                "assign varchar, " +
                "cjhm varchar,"+
                "jsd_id varchar,"+
                "cp varchar,"+
                "cx varchar,"+
                "jc_date varchar,"+
                "states varchar,"+
                "wxgz varchar,"+
                "xlg varchar,"+
                "jcr varchar,"+
                "ywg_date varchar"+
                 ")";


        db.execSQL(sql);
        db.execSQL(sqlRepair);
        db.execSQL(sqlFirstIcon);
        db.execSQL(sqlSeconndIcon);
        db.execSQL(sqlPartsInfo);
        db.execSQL(sqlManagerInfo);
    }

    // 当数据库版本更新执行该方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}