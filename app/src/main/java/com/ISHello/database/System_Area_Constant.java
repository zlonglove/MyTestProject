package com.ISHello.database;

/**
 * 这里描述程序简介，由第一位创建人编写：
 * 系统—参数表
 * <p>
 * 这里描述历史变更清单，按照变更日期—变更人—变更事项顺序编写。
 * 1、创建基本程序……
 *
 */
public class System_Area_Constant {

    /**
     * 表名：system_area
     */
    public static final String TABLE_SYSTEM_AREA = "system_area";

    public static final String SYSTEM_AREA_ID = "area_id";
    public static final String SYSTEM_PROVINCE_NAME = "province_name";
    public static final String SYSTEM_CITY_NAME = "city_name";
    public static final String SYSTEM_AREA_LEVEL = "area_level";
    public static final String SYSTEM_VERSION_NO = "version_no";
    /**
     * 建表语句
     */

    public static final String TBL_SYSTEM_AREA_CREATE_SQL = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SYSTEM_AREA + " (" + SYSTEM_AREA_ID + " INTEGER PRIMARY KEY,"
            + SYSTEM_PROVINCE_NAME + " TEXT,"
            + SYSTEM_CITY_NAME + " TEXT,"
            + SYSTEM_AREA_LEVEL + " INTEGER,"
            + SYSTEM_VERSION_NO + " INTEGER"
            + ");";

}
