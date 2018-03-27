package com.ISHello.databaseNew;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.ISHello.Entity.AdvEntity;

import java.util.ArrayList;

/**
 * Created by kfzx-zhanglong on 2016/11/2.
 * Company ICBC
 */
public class AdvStorageDao extends BaseDAO<AdvEntity> {
    private static final String TABLE_NAME = "User_AdvertingStorage";
    private static final String ID_COLUMN = "ID";
    private static final String ADV_ID_COLUMN = "ADV_ID";
    private static final String ADV_KIND_COLUMN = "ADV_KIND";
    private static final String ADV_DESCRIPTION_COLUMN = "ADV_DESCRIPTION";
    private static final String ADV_SHOW_COLUMN = "ADV_SHOW";
    private static final String ADV_BEGINDATE_COLUMN = "ADV_BEGINDATE";
    private static final String ADV_ENDDATE_COLUMN = "ADV_ENDDATE";
    private static final String ADV_PICPATH_COLUMN = "ADV_PICPATH";
    private static final String ADV_LOCALPICPATH_COLUMN = "ADV_LOCALPICPATH";
    private static final String ADV_AREACODE_COLUMN = "ADV_AREACODE";
    private static final String ADV_URLTYPE_COLUMN = "ADV_URLTYPE";
    private static final String ADV_URL_COLUMN = "ADV_URL";
    private static final String ALL_COLUMN = "*";

    public AdvStorageDao() {
        super();
    }

    // 根据广告id及广告类型查找
    public AdvEntity findAdvByIdAndKind(String ADV_ID, String ADV_KIND) {
        open();
        try {
            String where = ADV_ID_COLUMN + "=? AND " + ADV_KIND_COLUMN + "=?";
            AdvEntity result = queryObject(AdvEntity.class, TABLE_NAME, new String[]{ALL_COLUMN}, where, new String[]{ADV_ID, ADV_KIND});
            return result;
        } catch (Exception e) {
        } finally {
            close();
        }
        return null;
    }

    // 根据广告id及广告类型删除
    public void deleteAdvByIdAndKind(String ADV_ID, String ADV_KIND) {
        open();
        beginTransaction();
        try {
            String where = ADV_ID_COLUMN + "=? AND " + ADV_KIND_COLUMN + "=?";
            delete(TABLE_NAME, where, new String[]{ADV_ID, ADV_KIND});
            commit();
        } catch (Exception e) {
            rollback();
        } finally {
            close();
        }
    }

    // 查找启动页广告
    public AdvEntity findLoadingAdv() {
        open();
        try {
            String where = ADV_KIND_COLUMN + "=?";
            AdvEntity result = queryObject(AdvEntity.class, TABLE_NAME, new String[]{ALL_COLUMN}, where, new String[]{"0"});
            return result;
        } catch (Exception e) {
        } finally {
            close();
        }
        return null;
    }

    // 查找主菜单广告
    public ArrayList<AdvEntity> findMainMenuAdv(String kind, String areaCode) {
        open();
        try {
            String SQL = "";
            SQL += "SELECT * FROM (SELECT * FROM " + TABLE_NAME + " WHERE " + ADV_AREACODE_COLUMN + " = '0000' AND " + ADV_KIND_COLUMN + " = '" + kind + "' AND " + ADV_SHOW_COLUMN + " = '1' ORDER BY " + ADV_BEGINDATE_COLUMN + " DESC LIMIT 3)";
            SQL += " UNION ALL ";
            SQL += "SELECT * FROM (SELECT * FROM " + TABLE_NAME + " WHERE " + ADV_AREACODE_COLUMN + " = '" + areaCode + "' AND " + ADV_KIND_COLUMN + " = '" + kind + "' AND " + ADV_SHOW_COLUMN + " = '1' ORDER BY " + ADV_BEGINDATE_COLUMN + " DESC LIMIT 2)";
            Cursor c = query(SQL, null);
            ArrayList<AdvEntity> list = new ArrayList<AdvEntity>();
            AdvEntity t = null;
            while (c.moveToNext()) {
                try {
                    // 生成新的实例
                    t = AdvEntity.class.newInstance();
                } catch (Exception e) {
                    Log.e("newInstance error", "生成新实例时出错 ：" + e.toString());
                }
                // 把列的值，转换成对象里属性的值
                columnToField(t, c);
                list.add(t);
            }
            c.close();
            return list;
        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.toString());
        } finally {
            close();
        }
        return null;
    }

    // 查找主菜单常用功能广告
    public ArrayList<AdvEntity> findMainAdv() {
        open();
        try {
            String SQL = "";
            SQL += "SELECT * FROM (SELECT * FROM " + TABLE_NAME + " WHERE " + ADV_KIND_COLUMN + " = '1' ORDER BY " + ADV_SHOW_COLUMN + " ASC)";
            Cursor c = query(SQL, null);
            ArrayList<AdvEntity> list = new ArrayList<AdvEntity>();
            AdvEntity t = null;
            while (c.moveToNext()) {
                try {
                    // 生成新的实例
                    t = AdvEntity.class.newInstance();
                } catch (Exception e) {
                    Log.e("newInstance error", "生成新实例时出错 ：" + e.toString());
                }
                // 把列的值，转换成对象里属性的值
                columnToField(t, c);
                list.add(t);
            }
            c.close();
            return list;
        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.toString());
        } finally {
            close();
        }
        return null;
    }

    // 更新及插入启动页广告数据
    public void insertOrUpdateLoadingAdv(AdvEntity advEntity) {
        ContentValues values = new ContentValues();
        values.put(ADV_ID_COLUMN, advEntity.getADV_ID());
        values.put(ADV_KIND_COLUMN, advEntity.getADV_KIND());
        values.put(ADV_DESCRIPTION_COLUMN, advEntity.getADV_DESCRIPTION());
        values.put(ADV_SHOW_COLUMN, advEntity.getADV_SHOW());
        values.put(ADV_BEGINDATE_COLUMN, advEntity.getADV_BEGINDATE());
        values.put(ADV_ENDDATE_COLUMN, advEntity.getADV_ENDDATE());
        values.put(ADV_PICPATH_COLUMN, advEntity.getADV_PICPATH());
        values.put(ADV_LOCALPICPATH_COLUMN, advEntity.getADV_LOCALPICPATH());
        values.put(ADV_AREACODE_COLUMN, advEntity.getADV_AREACODE());
        values.put(ADV_URLTYPE_COLUMN, advEntity.getADV_URLTYPE());
        values.put(ADV_URL_COLUMN, advEntity.getADV_URL());
        open();
        beginTransaction();
        try {
            // 先删除所有启动页广告，保证只有一条
            delete(TABLE_NAME, ADV_KIND_COLUMN + "=?", new String[]{"0"});
            insert(TABLE_NAME, values);
            commit();
        } catch (Exception e) {
            rollback();
        } finally {
            close();
        }
    }

    // 删除启动页广告
    public void deleteLoadingAdv() {
        open();
        beginTransaction();
        try {
            delete(TABLE_NAME, ADV_KIND_COLUMN + "=?", new String[]{"0"});
            commit();
        } catch (Exception e) {
            rollback();
        } finally {
            close();
        }
    }

    // 更新及插入主菜单广告数据
    public void insertOrUpdateMainMenuAdv(AdvEntity advEntity) {
        ContentValues values = new ContentValues();
        values.put(ADV_ID_COLUMN, advEntity.getADV_ID());
        values.put(ADV_KIND_COLUMN, advEntity.getADV_KIND());
        values.put(ADV_DESCRIPTION_COLUMN, advEntity.getADV_DESCRIPTION());
        values.put(ADV_SHOW_COLUMN, advEntity.getADV_SHOW());
        values.put(ADV_BEGINDATE_COLUMN, advEntity.getADV_BEGINDATE());
        values.put(ADV_ENDDATE_COLUMN, advEntity.getADV_ENDDATE());
        values.put(ADV_PICPATH_COLUMN, advEntity.getADV_PICPATH());
        values.put(ADV_LOCALPICPATH_COLUMN, advEntity.getADV_LOCALPICPATH());
        values.put(ADV_AREACODE_COLUMN, advEntity.getADV_AREACODE());
        values.put(ADV_URLTYPE_COLUMN, advEntity.getADV_URLTYPE());
        values.put(ADV_URL_COLUMN, advEntity.getADV_URL());
        open();
        beginTransaction();
        try {
            // 先删除所有启动页广告，保证只有一条
            //delete(TABLE_NAME, ADV_ID_COLUMN + "=?", new String[]{advEntity.getADV_ID()});
            replace(TABLE_NAME, values);
            commit();
        } catch (Exception e) {
            rollback();
        } finally {
            close();
        }
    }
}
