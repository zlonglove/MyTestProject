/**
 *
 */
package com.ISHello.database;

public class Wealth_Bar_Info {

    /**
     * 表名：wealth_bar_info
     */
    public static final String TABLE_WEALTH_BAR_INFO_OPERATE = "wealth_bar_info";
    public static final String PK_WEALTH_BAR_INFO_OPERATE = "pk_wealth_bar_info";
    /**
     * 贴吧ID:bar_id
     */
    public static final String BAR_ID = "bar_id";
    /**
     * 粉丝人数:fans_count
     */
    public static final String FANS_COUNT = "fans_count";
    /**
     * 话题数:topic_count
     */
    public static final String TOPIC_COUNT = "topic_count";
    /**
     * 公开群数量:public_group_count
     */
    public static final String PUBLIC_GROUP_COUNT = "public_group_count";
    /**
     * 置顶帖子id:top_article_ids
     */
    public static final String TOP_ARTCLE_IDS = "top_article_ids";
    /**
     * 置顶帖子标题:top_article_titles
     */
    public static final String TOP_ARTCLE_TITLES = "top_article_titles";
    /**
     * 我是否关注:is_attention
     */
    public static final String IS_ATTENTION = "is_attention";

    /**
     * 文件前缀
     */
    public static final String FILES_SERVER_URL = "files_server_url";

    /**
     * 是否可以评论
     */
    public static final String IS_COMMENT = "is_comment";

    /**
     * 建表语句
     */
    public static final String TBL_WEALTH_BAR_INFO_OPERATECREATE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_WEALTH_BAR_INFO_OPERATE
            + " ( "
            + BAR_ID + " TEXT, "
            + FILES_SERVER_URL + " TEXT, "
            + FANS_COUNT + " TEXT, "
            + TOPIC_COUNT + " TEXT, "
            + IS_COMMENT + " TEXT, "
            + PUBLIC_GROUP_COUNT + " TEXT, "
            + TOP_ARTCLE_IDS + " TEXT, "
            + TOP_ARTCLE_TITLES + " TEXT, "
            + IS_ATTENTION + " TEXT, "
            + " CONSTRAINT " + PK_WEALTH_BAR_INFO_OPERATE
            + " PRIMARY KEY ( " + BAR_ID + " , " + TOP_ARTCLE_IDS + " )"
            + ");";

}
