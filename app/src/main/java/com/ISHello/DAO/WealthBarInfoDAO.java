package com.ISHello.DAO;

import android.content.ContentValues;
import android.database.Cursor;

import com.ISHello.Module.BarInfoModel;
import com.ISHello.database.Wealth_Bar_Info;

import java.util.ArrayList;

public class WealthBarInfoDAO extends BaseDAO {

    private static WealthBarInfoDAO wealthBarInfoDAO;

    public WealthBarInfoDAO() {
        super();
    }

    public static WealthBarInfoDAO getInstance() {
        if (wealthBarInfoDAO == null) {
            wealthBarInfoDAO = new WealthBarInfoDAO();
        }
        return wealthBarInfoDAO;
    }

    /**
     * 方法名称：初始化吧列表 输入项说明： 返回项说明：
     *
     * @return
     */
    public BarInfoModel initWealthBarInfo(String bar_id, String fans_count, String topic_count, String public_group_count, String top_article_ids,
                                          String top_article_titles, String is_attention, String files_server_url, String is_comment) {
        BarInfoModel wealthBarInfo = new BarInfoModel();
        wealthBarInfo.setBarId(bar_id);
        wealthBarInfo.setFansCount(fans_count);
        wealthBarInfo.setTopicCount(topic_count);
        wealthBarInfo.setGroupCount(public_group_count);
        wealthBarInfo.setArticleId(top_article_ids);
        wealthBarInfo.setArticleTitle(top_article_titles);
        wealthBarInfo.setIsAttention(is_attention);
        wealthBarInfo.setFilesServerUrl(files_server_url);
        wealthBarInfo.setIsComment(is_comment);

        return wealthBarInfo;
    }

    /**
     * 方法简介：插入一条吧信息到sqlite数据库
     * <p>
     * 输入项说明：BarInfoModel
     * <p>
     * 返回项说明：无
     */
    public void insertWealthBarInfo(BarInfoModel wealthBarInfo) {
        ContentValues values = new ContentValues();
        values.put(Wealth_Bar_Info.BAR_ID, wealthBarInfo.getBarId());
        values.put(Wealth_Bar_Info.FANS_COUNT, wealthBarInfo.getFansCount());
        values.put(Wealth_Bar_Info.TOPIC_COUNT, wealthBarInfo.getTopicCount());
        values.put(Wealth_Bar_Info.PUBLIC_GROUP_COUNT, wealthBarInfo.getGroupCount());
        values.put(Wealth_Bar_Info.TOP_ARTCLE_IDS, wealthBarInfo.getArticleId());
        values.put(Wealth_Bar_Info.TOP_ARTCLE_TITLES, wealthBarInfo.getArticleTitle());
        values.put(Wealth_Bar_Info.IS_ATTENTION, wealthBarInfo.getIsAttention());
        values.put(Wealth_Bar_Info.FILES_SERVER_URL, wealthBarInfo.getFilesServerUrl());
        values.put(Wealth_Bar_Info.IS_COMMENT, wealthBarInfo.getIsComment());

        try {
            super.insert(Wealth_Bar_Info.TABLE_WEALTH_BAR_INFO_OPERATE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 方法简介：更新一条吧信息到sqlite数据库
     * <p>
     * 输入项说明：BarInfoModel
     * <p>
     * 返回项说明：无
     */
    public void updateWealthBarInfo(BarInfoModel wealthBarInfo) {
        ContentValues values = new ContentValues();
        String tableName = Wealth_Bar_Info.TABLE_WEALTH_BAR_INFO_OPERATE;

        values.put(Wealth_Bar_Info.BAR_ID, wealthBarInfo.getBarId());
        values.put(Wealth_Bar_Info.FANS_COUNT, wealthBarInfo.getFansCount());
        values.put(Wealth_Bar_Info.TOPIC_COUNT, wealthBarInfo.getTopicCount());
        values.put(Wealth_Bar_Info.PUBLIC_GROUP_COUNT, wealthBarInfo.getGroupCount());
        values.put(Wealth_Bar_Info.TOP_ARTCLE_IDS, wealthBarInfo.getArticleId());
        values.put(Wealth_Bar_Info.TOP_ARTCLE_TITLES, wealthBarInfo.getArticleTitle());
        values.put(Wealth_Bar_Info.IS_ATTENTION, wealthBarInfo.getIsAttention());
        values.put(Wealth_Bar_Info.FILES_SERVER_URL, wealthBarInfo.getFilesServerUrl());
        values.put(Wealth_Bar_Info.IS_COMMENT, wealthBarInfo.getIsComment());

        super.replace(tableName, null, values);
    }

    /**
     * 方法简介：按结构初始化吧信息
     * <p>
     * 输入项说明：Cursor
     * <p>
     * 返回项说明：无
     */
    public BarInfoModel initWealthBar(Cursor cursor) {
        BarInfoModel barInfoModel = new BarInfoModel();

        barInfoModel.setBarId(cursor.getString(cursor.getColumnIndex(Wealth_Bar_Info.BAR_ID)));
        barInfoModel.setFansCount(cursor.getString(cursor.getColumnIndex(Wealth_Bar_Info.FANS_COUNT)));
        barInfoModel.setTopicCount(cursor.getString(cursor.getColumnIndex(Wealth_Bar_Info.TOPIC_COUNT)));
        barInfoModel.setGroupCount(cursor.getString(cursor.getColumnIndex(Wealth_Bar_Info.PUBLIC_GROUP_COUNT)));
        barInfoModel.setArticleId(cursor.getString(cursor.getColumnIndex(Wealth_Bar_Info.TOP_ARTCLE_IDS)));
        barInfoModel.setArticleTitle(cursor.getString(cursor.getColumnIndex(Wealth_Bar_Info.TOP_ARTCLE_TITLES)));
        barInfoModel.setIsAttention(cursor.getString(cursor.getColumnIndex(Wealth_Bar_Info.IS_ATTENTION)));
        barInfoModel.setFilesServerUrl(cursor.getString(cursor.getColumnIndex(Wealth_Bar_Info.FILES_SERVER_URL)));
        barInfoModel.setIsComment(cursor.getString(cursor.getColumnIndex(Wealth_Bar_Info.IS_COMMENT)));

        return barInfoModel;
    }

    /**
     * 方法简介：根据吧id查询吧信息
     * <p>
     * 输入项说明：String bar_id 吧ID
     * <p>
     * 返回项说明：ArrayList<BarInfoModel>
     */
    public ArrayList<BarInfoModel> queryWealthBarInfo(String bar_id) {
        ArrayList<BarInfoModel> barList = new ArrayList<BarInfoModel>();
        try {
            String table = Wealth_Bar_Info.TABLE_WEALTH_BAR_INFO_OPERATE;
            String selection = Wealth_Bar_Info.BAR_ID + "=?";
            Cursor cursor = super.query(table, null, selection, new String[]{String.valueOf(bar_id)}, null, null, null);
            try {
                while (cursor != null && cursor.moveToNext()) {
                    barList.add(initWealthBar(cursor));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return barList;
    }

    /**
     * 方法简介：根据吧id删除数据
     * <p>
     * 输入项说明：String bar_id 吧ID
     * <p>
     * 返回项说明：无
     */
    public void deteleTableForWealthBarInfoById(String bar_id) {
        String tableName = Wealth_Bar_Info.TABLE_WEALTH_BAR_INFO_OPERATE;
        String selection = Wealth_Bar_Info.BAR_ID + "=?";
        try {
            super.delete(tableName, selection, new String[]{String.valueOf(bar_id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 方法简介： 删除本表
     * <p>
     * 输入项说明：无
     * <p>
     * 返回项说明:无
     */
    public void deteleTableForWealthBarInfo() {
        String tableName = Wealth_Bar_Info.TABLE_WEALTH_BAR_INFO_OPERATE;
        try {
            super.delete(tableName, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 方法简介：查询表中的列是不是存在
     * <p>
     * 输入项说明：tableName-表名称,columnName-列名称
     * <p>
     * 返回项说明:无
     */
    public boolean checkColumnExists(String tableName, String columnName) {
        return super.checkColumnExists(tableName, columnName);
    }
}
