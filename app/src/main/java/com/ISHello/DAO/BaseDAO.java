package com.ISHello.DAO;

import android.content.ContentValues;
import android.database.Cursor;

import com.ISHello.database.MySQLiteOpenHelper;
import com.ISHello.database.SQliteManager;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class BaseDAO {
    protected MySQLiteOpenHelper imDB;
    protected SQLiteDatabase writeDatabase;

    public BaseDAO() {
        initDatabase();
    }

    protected void createTable(String sql) {
        if (imDB != null) {
            imDB.createTable(sql);
        }
    }

    protected void deleteTable(String tableName) {
        if (imDB != null) {
            imDB.deleteTable(tableName);
        }
    }

    private void initDatabase() {
        synchronized (BaseDAO.class) {
            imDB = SQliteManager.getInstance().getMainDB();
            if (imDB != null) {
                writeDatabase = imDB.getWritableDatabase();
            }
        }
    }

    protected Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        if (!isTableExists(table)) {
            return null;
        }
        if (writeDatabase != null)
            return writeDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        return null;
    }

    protected Cursor rawQuery(String sql, String[] selectionArgs) {
        if (writeDatabase != null)
            return writeDatabase.rawQuery(sql, selectionArgs);
        return null;
    }

    /**
     * 执行一条sql语句
     *
     * @param sql --sql语句
     */
    protected void execSQL(String sql) {
        if (writeDatabase != null)
            writeDatabase.execSQL(sql);
    }

    protected long replace(String table, String nullColumnHack, ContentValues initialValues) {
        if (!isTableExists(table)) {
            return 0;
        }
        if (writeDatabase != null)
            return writeDatabase.replace(table, nullColumnHack, initialValues);
        return 0;
    }

    protected long insert(String table, String nullColumnHack, ContentValues values) {
        if (writeDatabase != null)
            return writeDatabase.insert(table, nullColumnHack, values);
        return 0;
    }

    protected long update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        if (!isTableExists(table)) {
            return 0;
        }
        if (writeDatabase != null)
            return writeDatabase.update(table, values, whereClause, whereArgs);
        return 0;
    }

    protected long delete(String table, String whereClause, String[] whereArgs) {
        if (!isTableExists(table)) {
            return 0;
        }
        if (writeDatabase != null)
            return writeDatabase.delete(table, whereClause, whereArgs);
        return 0;
    }

    /**
     * 方法2：检查表中某列是否存在
     *
     * @param tableName  表名
     * @param columnName 列名
     * @return
     */
    public boolean checkColumnExists(String tableName, String columnName) {
        boolean result = false;
        Cursor cursor = null;
        // initDatabase();
        try {
            cursor = writeDatabase.rawQuery("select * from sqlite_master where name = ? and sql like ?", new String[]{tableName,
                    "%" + columnName + "%"});
            result = null != cursor && cursor.moveToFirst();
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
            return result;
        }
    }

    /**
     * 查询当前数据库里的所有表的集合
     *
     * @return 表名称的集合
     */
    public List<String> queryAllTableName() {
        List<String> list = new ArrayList<String>();
        Cursor cursor = rawQuery("select name from sqlite_master where type='table' order by name", null);
        while (cursor.moveToNext()) {
            String tableName = cursor.getString(cursor.getColumnIndex(String.valueOf("name")));
            list.add(tableName);
        }
        return list;
    }

    /**
     * 查询一个表是不是存在
     *
     * @param tableName --表的名称
     * @return true-存在,false--不存在
     */
    public boolean isTableExists(String tableName) {
        if (writeDatabase == null) {
            initDatabase();
            if (writeDatabase == null) {
                return false;
            }
        }
        Cursor cursor = null;
        boolean flag = false;
        try {
            cursor = writeDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    flag = true;
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return flag;
    }

    public boolean isTableExists2(String tableName) {
        if (writeDatabase == null || !writeDatabase.isOpen()) {
            writeDatabase = imDB.getReadableDatabase();
        }

        if (!writeDatabase.isReadOnly()) {
            writeDatabase.close();
            writeDatabase = imDB.getReadableDatabase();
        }

        Cursor cursor = writeDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    /**
     * 查询一列最大值
     *
     * @param tableName  --表名
     * @param columnName --列名
     * @return
     */
    public int queryMaxValue(String tableName, String columnName) {
        int value = 0;
        String sql = "select max(" + columnName + ") as maxValue from " + tableName;
        Cursor cursor = null;
        try {
            if (!isTableExists(tableName)) {
                return value;
            }
            cursor = writeDatabase.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                value = cursor.getInt(cursor.getColumnIndex("maxValue"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return value;
    }
    //select * from dbname where columnName=(select max(columnName) from dbname) 
}
