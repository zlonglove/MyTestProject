package com.ISHello.MultipleThreadContinuableDownLoad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MultipleThreadContinuableDownLoadDBOpenHelper extends SQLiteOpenHelper {

    private final static String DEFAULT_DBNAME = "etic.db";
    private final static int DEFAULT_VERSION = 1;
    private final static String TABEL_NAME = "download";


    public MultipleThreadContinuableDownLoadDBOpenHelper(Context context,
                                                         String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public MultipleThreadContinuableDownLoadDBOpenHelper(Context context) {
        this(context, DEFAULT_DBNAME, null, DEFAULT_VERSION);
    }


    /*
     * 获取表的名字
     */
    public String getTableName() {
        return TABEL_NAME;
    }

    /**
     * 创建数据库
     * id
     * downpath--下载路径
     * threadid--线程的ID号
     * downlength-下载的长度
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS "
                + TABEL_NAME + " ("
                + "id integer primary autoincrement,"
                + "downpath varchar(100),"
                + "threadid INTEGER,"
                + "downlength INTEGER"
                + ")";
        db.execSQL(sql);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABEL_NAME;
        db.execSQL(sql);
        onCreate(db);
    }


}
