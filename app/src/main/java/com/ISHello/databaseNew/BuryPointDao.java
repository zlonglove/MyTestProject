package com.ISHello.databaseNew;

import android.database.Cursor;

import com.ISHello.Entity.BuryPointEntity;

import java.util.HashMap;

/**
 * Created by kfzx-zhanglong on 2016/11/2.
 * Company ICBC
 */
public class BuryPointDao extends BaseDAO<BuryPointEntity> {
    //埋点映射表
    private static String TABLE_NAME = "BuryPoint";

    private static final String BURYPOINTID_COLUMN = "buryPointId";
    private static final String AREANO_COLUMN = "areano";
    private static final String AREANONAME_COLUMN = "areanoname";
    private static final String BUTTONNO_COLUMN = "buttonno";
    private static final String BUTTONNAME_COLUMN = "buttonname";

    public BuryPointDao() {
        super();
    }

    public HashMap<String, String> getBuryPointInfo(String buryPointId) {
        HashMap<String, String> result = new HashMap<String, String>();
        open();
        beginTransaction();
        try {
            Cursor c = query(TABLE_NAME, new String[]{AREANO_COLUMN, AREANONAME_COLUMN, BUTTONNO_COLUMN, BUTTONNAME_COLUMN},
                    BURYPOINTID_COLUMN + " = ?", new String[]{buryPointId});
            if (c.moveToNext()) {
                result.put(AREANO_COLUMN, c.getString(c.getColumnIndex(AREANO_COLUMN)));
                result.put(AREANONAME_COLUMN, c.getString(c.getColumnIndex(AREANONAME_COLUMN)));
                result.put(BUTTONNO_COLUMN, c.getString(c.getColumnIndex(BUTTONNO_COLUMN)));
                result.put(BUTTONNAME_COLUMN, c.getString(c.getColumnIndex(BUTTONNAME_COLUMN)));
            }
            c.close();
            commit();
        } catch (Exception e) {
            rollback();
        } finally {
            close();
        }
        return result;
    }

    public BuryPointEntity getBuryPointEntity(String buryPointId) {
        BuryPointEntity buryPointEntity = null;
        open();
        beginTransaction();
        try {
            buryPointEntity = queryObject(BuryPointEntity.class, TABLE_NAME, new String[]{BURYPOINTID_COLUMN, AREANO_COLUMN, AREANONAME_COLUMN, BUTTONNO_COLUMN, BUTTONNAME_COLUMN},
                    BURYPOINTID_COLUMN + " = ?", new String[]{buryPointId});
            commit();
        } catch (Exception e) {
            rollback();
        } finally {
            close();
        }
        return buryPointEntity;
    }
}
