package com.ISHello.database;

public class DBCommon {
    public static final String USERID = "925811560";
    public static final String DB_NAME = "ZL%sSEC.db";
    public static final int DB_VERSION = 5;
    public static final String DROPTABLE = "drop table if exists ";

    /**
     * 建表集合
     */
    public static final String[] CREATE_TABLE_SQL = {
            Wealth_Bar_Info.TBL_WEALTH_BAR_INFO_OPERATECREATE_SQL,
            Wealth_Circle_List.TBL_WEALTH_CIRCLE_LIST_OPERATECREATE_SQL,
            System_Area_Constant.TBL_SYSTEM_AREA_CREATE_SQL
    };
}
