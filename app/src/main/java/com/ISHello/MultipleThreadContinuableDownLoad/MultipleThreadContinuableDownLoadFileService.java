package com.ISHello.MultipleThreadContinuableDownLoad;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MultipleThreadContinuableDownLoadFileService {
    /**
     * 声明数据库管理器
     */
    private MultipleThreadContinuableDownLoadDBOpenHelper openHelper;

    public MultipleThreadContinuableDownLoadFileService(Context context) {
        openHelper = new MultipleThreadContinuableDownLoadDBOpenHelper(context);

    }

    /**
     * 获取特定URL的每条线程已经下载的文件的的长度
     */
    public Map<Integer, Integer> getDate(String path) {

        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select threadid, downlength from " +
                openHelper.getTableName() +
                " where downpath=?", new String[]{path});
        /**
         * 建立一个哈希表用于存放每条线程的已经下载的文件的长度
         */
        Map<Integer, Integer> data = new HashMap<Integer, Integer>();
        while (cursor.moveToNext()) {
            data.put(cursor.getInt(0), cursor.getInt(0));
            /**
             * 把线程ID和该线程已经下载的长度设置进data哈希表中
             */
            data.put(cursor.getInt(cursor.getColumnIndexOrThrow("threadid")), cursor.getInt(
                    cursor.getColumnIndexOrThrow("downlength"))
            );
        }
        cursor.close();
        db.close();
        return data;
    }


    /**
     * 保存每条线程已经下载的文件长度
     *
     * @param path 下载的路径
     * @param map  现在的Id和已经下载的长度的集合
     */
    public void save(String path, Map<Integer, Integer> map) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        //开始事务,因为此处理要插入多批数据
        db.beginTransaction();
        try {
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                /**
                 * 插入特定下载路径特定线程ID已经下载的数据
                 */
                db.execSQL("insert into " + openHelper.getTableName() + "(downpath,threadid," +
                        "downlength) values(?,?,?)", new Object[]{path, entry.getKey(), entry.getValue()});
            }
            /**
             * 设置事务执行的标志位成功
             */
            db.setTransactionSuccessful();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            /**
             * 结束一个事务,如果事务设立了成功标志，则提交事务，否则会滚事务
             */
            db.endTransaction();
        }
        db.close();
    }

    /**
     * 实时更新每条线程已经下载的文件的长度
     *
     * @param path
     * @param threadId
     * @param pos
     */
    public void update(String path, int threadId, int pos) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.execSQL("update " + openHelper.getTableName() + " set downlenth=? where downpath=? and threadid=?",
                new Object[]{pos, path, threadId});
        db.close();
    }

    /**
     * 当文件下载完成后,删除对应的下载记录
     *
     * @param path
     */
    public void delete(String path) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        /**
         * 删除特定下载路径下的所有线程记录
         */
        db.execSQL("delete from " + openHelper.getTableName() + " where downpath=?",
                new Object[]{path});
        db.close();
    }
}


