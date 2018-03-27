package com.ISHello.database;

import com.ISHello.Application.BaseApplication;

public class SQliteManager {
    private static SQliteManager sQlitePool;
    private MySQLiteOpenHelper sqLiteOpenHelper;

    public SQliteManager() {
        initDB();
    }

    private synchronized void initDB() {
        if (sqLiteOpenHelper != null) {
            return;// 多线程情况下 可能已经初始化的db
        }
        String name = String.format(DBCommon.DB_NAME, DBCommon.USERID);
        sqLiteOpenHelper = new MySQLiteOpenHelper(BaseApplication.getInstance(),
                DBCommon.CREATE_TABLE_SQL,
                name);
    }

    /**
     * 单例模式,获取一个实例
     *
     * @return
     */
    public static SQliteManager getInstance() {
        if (sQlitePool == null) {
            synchronized (SQliteManager.class) {
                if (sQlitePool == null) {
                    sQlitePool = new SQliteManager();
                }
            }
        }
        return sQlitePool;
    }

    /**
     * 获取主DB数据库
     *
     * @return
     */
    public MySQLiteOpenHelper getMainDB() {
        if (sqLiteOpenHelper == null) {
            initDB();
        }
        return sqLiteOpenHelper;
    }

    /**
     * 关闭数据库,不许用使用的时候关闭数据库
     */
    public void close() {
        if (sqLiteOpenHelper != null) {
            sqLiteOpenHelper.closeDB();
        }
        sqLiteOpenHelper = null;
    }
}
