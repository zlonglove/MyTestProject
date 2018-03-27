package com.ISHello.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.ISHello.utils.MD5;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private final String TAG = "MySQLiteOpenHelper";
    private SQLiteDatabase mdb;
    private String mCreateTableSql[];
    private String mDBName;

    public MySQLiteOpenHelper(Context context, String[] createTableSql, String dbName) {
        this(context, createTableSql, dbName, DBCommon.DB_VERSION);
    }

    /**
     * @param context
     * @param createTableSql---创建表语句集合
     * @param dbName---数据库名称
     * @param dbVersion---数据库版本号
     */
    public MySQLiteOpenHelper(Context context, String[] createTableSql, String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion);
        /**
         *sqlcipher.so
         */
        SQLiteDatabase.loadLibs(context.getApplicationContext());
        mDBName = dbName;
        mCreateTableSql = createTableSql;
    }

    /**
     * 数据库第一创建的时候调用
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        mdb = db;
        initTable();
    }

    /**
     * 初始化表
     */
    private void initTable() {
        if (mCreateTableSql != null) {
            for (int i = 0; i < mCreateTableSql.length; ++i) {
                try {
                    mdb.execSQL(mCreateTableSql[i]);
                } catch (Exception e) {
                    Log.e(TAG, "--->create table[" + mCreateTableSql[i] + "]" + e.getMessage());
                }
            }
        }
    }

    /**
     * 获取对数据库进行操作的引用
     *
     * @return
     */
    public SQLiteDatabase getWritableDatabase() {
        if (mDBName.contains("SEC")) {
            return super.getWritableDatabase(MD5.encode(mDBName));// 加密数据库
        } else {
            return super.getWritableDatabase("");// 没有加密的
        }
    }

    /**
     * 只获取对数据库读操作的引用
     *
     * @return
     */
    public synchronized SQLiteDatabase getReadableDatabase() {
        if (mDBName.contains("SEC")) {
            return super.getReadableDatabase(MD5.encode(mDBName));
        } else {
            return super.getReadableDatabase("");
        }
    }

    /**
     * 创建表
     *
     * @param createTableSql--建表语句
     */
    public void createTable(String createTableSql) {
        getWritableDatabase().execSQL(createTableSql);
    }

    /**
     * 删除指定表名的一张表
     *
     * @param tableName--表名
     */
    public void deleteTable(String tableName) {
        String sql = DBCommon.DROPTABLE + tableName;
        try {
            getWritableDatabase().execSQL(sql);
        } catch (Exception e) {
            Log.e(TAG, "--->When Delete Table got a Exception. sql: " + sql);
        }
    }

    /**
     * 关闭数据库
     */
    public void closeDB() {
        if (mdb != null) {
            mdb.close();
        }
        this.close();
    }

    /**
     * 数据库升级
     *
     * @param db
     * @param oldVersion--旧的版本号
     * @param newVersion--新的版本号
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mdb = db;
        if (newVersion == oldVersion) {
            return;
        }
        if (oldVersion < 5) {
            updateDBVersion2(mdb);
        }
        initTable();
    }

    public void updateDBVersion2(SQLiteDatabase mdb) {
        ArrayList<String> sqlList = new ArrayList<String>();
        Cursor cursor = null;
        try {
            cursor = mdb.query(String.valueOf("sqlite_master"), null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("type")).equals("table")) {
                    String tableName = cursor.getString(cursor.getColumnIndex(String.valueOf("name")));
                    if (tableName.startsWith("wealth_bar_info")) {
                        String str1 = "ALTER TABLE " + tableName + " ADD COLUMN " + "mobile_no_str" + " TEXT;";
                        sqlList.add(str1);
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        if (sqlList != null) {
            for (int i = 0; i < sqlList.size(); i++) {
                mdb.execSQL(sqlList.get(i));
            }
        }
    }
}
