package com.ISHello.databaseNew;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.ISHello.Application.BaseApplication;
import com.ISHello.Constants.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by kfzx-zhanglong on 2016/11/2.
 * Company ICBC
 */
public class DBHelper extends SQLiteOpenHelper {
    private final String TAG = "DBHelper";
    private static DBHelper dbHelper;

    /**
     * @param context
     * @param databaseName
     * @param factory
     * @param version
     */
    private DBHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, databaseName, factory, version);
    }

    /**
     * 单例模式,对外部使用
     *
     * @return
     */
    public static synchronized SQLiteDatabase getDataBase() {
        if (dbHelper == null) {
            dbHelper = new DBHelper(BaseApplication.getInstance(), Constants.DB_NAME, null, Constants.DB_VERISON);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                dbHelper.getWritableDatabase().enableWriteAheadLogging();
            }
        }
        return dbHelper.getWritableDatabase();
    }

    public static DBHelper getDBHelper() {
        if (dbHelper == null) {
            dbHelper = new DBHelper(BaseApplication.getInstance(), Constants.DB_NAME, null, Constants.DB_VERISON);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                dbHelper.getWritableDatabase().enableWriteAheadLogging();
            }
        }
        return dbHelper;
    }

    /**
     * 关闭数据库
     * 一般程序退出的时候关闭
     */
    public synchronized void closeDataBase() {
        if (dbHelper != null) {
            dbHelper.getWritableDatabase().close();
            dbHelper = null;
        }
    }

    /**
     * 数据库第一次创建时调用
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "--->onCreate excute");
        executeSchema(db, Constants.DB_INIT_SQL);
    }

    /**
     * 数据库升级时调用
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "--->oldVersion==" + oldVersion + "/newVersion==" + newVersion);
        // 数据库不升级
        if (newVersion <= oldVersion) {
            return;
        }
        int changeCnt = newVersion - oldVersion;
        for (int i = 0; i < changeCnt; i++) {
            // 依次执行updatei_i+1文件 由1更新到2 [1-2]，2更新到3 [2-3]
            String schemaName = "update" + (oldVersion + i) + "_" + (oldVersion + i + 1) + ".sql";
            Log.i(TAG, "--->" + schemaName);
            executeSchema(db, schemaName);
        }
    }

    /**
     * 读取数据库文件（.sql），并执行sql语句
     */
    private void executeSchema(SQLiteDatabase db, String schemaName) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(BaseApplication.getInstance().getAssets().open(Constants.DB_PATH + "/" + schemaName)));
            String line;
            String buffer = "";
            while ((line = in.readLine()) != null) {
                buffer += line;
                if (line.trim().endsWith(";")) {
                    db.execSQL(buffer.replace(";", ""));
                    buffer = "";
                }
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }
    }
}
