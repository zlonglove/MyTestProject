/**
 *
 */
package com.ISHello.database;

public class Wealth_Circle_List {
    /**
     * 表名：wealth_circle_list
     */
    public static final String TABLE_WEALTH_CIRCLE_LIST_OPERATE = "wealth_circle_list";
    public static final String PK_WEALTH_CIRCLE_LIST_OPERATE = "pk_wealth_circle_list";

    /**
     * 贴吧ID:bar_id
     */
    public static final String BAR_ID = "bar_id";
    /**
     * 贴吧名称:bar_name
     */
    public static final String BAR_NAME = "bar_name";
    /**
     * 贴吧图片名称:bar_icon
     */
    public static final String BAR_ICON = "bar_icon";

    /**
     * 是否有动态贴标识:dynamic_flag
     */
    public static final String DYNAMIC_FLAG = "dynamic_flag";

    /**
     * 财富圈吧图标请求地址:filesserver_url
     */
    public static final String FILESSERVER_URL = "filesserver_url";
    /**
     * 帖子缩略图请求地址:FilesServer_Bar_Url
     */
    public static final String FILESSERVER_BAR_URL = "filesserver_bar_url";

    /**
     * 建表语句
     */
    public static final String TBL_WEALTH_CIRCLE_LIST_OPERATECREATE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_WEALTH_CIRCLE_LIST_OPERATE
            + " ( "
            + BAR_ID
            + " TEXT, "
            + BAR_NAME
            + " TEXT, "
            + BAR_ICON
            + " TEXT, "
            + DYNAMIC_FLAG + " TEXT, " + FILESSERVER_URL + " TEXT, " + FILESSERVER_BAR_URL + " TEXT, " + " CONSTRAINT "
            + PK_WEALTH_CIRCLE_LIST_OPERATE + " PRIMARY KEY ( " + BAR_ID + " )" + ");";
}
