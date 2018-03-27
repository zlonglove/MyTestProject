package com.ISHello.databaseNew;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kfzx-zhanglong on 2016/11/2.
 * Company ICBC
 */
public class BaseDAO<T> {
    public BaseDAO() {
    }

    public synchronized SQLiteDatabase getDataBase() {
        return DBHelper.getDataBase();
    }

    /**
     * 增加、删除、修改表时，调用此方法
     *
     * @param sql DDL语句
     */
    public synchronized void execute(String sql) {
        getDataBase().execSQL(sql);
    }

    /**
     * 删除表中的记录
     *
     * @param table       表名
     * @param whereClause 删除条件 如：( id>? and time>?)
     * @param whereArgs   条件里的参数 用来替换"?" 第1个参数，代表第1个问号；第2个参数，代表第2个问号；依此类推......
     * @return 返回删除的条数
     */
    public synchronized int delete(String table, String whereClause, String[] whereArgs) {
        return getDataBase().delete(table, whereClause, whereArgs);
    }

    /**
     * 插入数据
     *
     * @param table  表名
     * @param values ContentValues对象
     * @return 返回当前行ID值，如果失败返回-1
     */
    public synchronized long insert(String table, ContentValues values) {
        return insert(table, null, values);
    }

    /**
     * 插入数据
     *
     * @param table          表名
     * @param values         ContentValues对象
     * @param nullColumnHack 空列
     * @return 返回当前行ID值，如果失败返回-1
     */
    public synchronized long insert(String table, String nullColumnHack, ContentValues values) throws SQLException {
        return getDataBase().insertOrThrow(table, nullColumnHack, values);
    }

    /**
     * 修改数据
     *
     * @param table       表名
     * @param values      ContentValues对象 表示要修改的列，如： name="steven" 即 values.put("name","steven");
     * @param whereClause 修改条件 如：( id=?)
     * @param whereArgs   条件里的参数 用来替换"?" 第1个参数，代表第1个问号；第2个参数，代表第2个问号；依此类推......
     * @return 返回修改的条数
     */
    public synchronized int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return getDataBase().update(table, values, whereClause, whereArgs);
    }

    /**
     * @param table  表名
     * @param values ContentValues键值对
     * @return
     */
    public synchronized long replace(String table, ContentValues values) {
        return replace(table, null, values);
    }

    /**
     * @param table          表名
     * @param initialValues  ContentValues对象
     * @param nullColumnHack 空列
     * @return 返回当前行ID值，如果失败返回-1
     */
    public synchronized long replace(String table, String nullColumnHack, ContentValues initialValues) {
        return getDataBase().replace(table, nullColumnHack, initialValues);
    }

    /**
     * 查询数据
     *
     * @param table         表名
     * @param columns       要查询的列名
     * @param selection     查询条件 如：( id=?)
     * @param selectionArgs 条件里的参数，用来替换"?"
     * @return 返回Cursor
     */
    public synchronized Cursor query(String table, String[] columns, String selection, String[] selectionArgs) {
        return getDataBase().query(table, columns, selection, selectionArgs, null, null, null);
    }

    /**
     * 查询数据
     *
     * @param table         表名
     * @param columns       要查询的列名
     * @param selection     查询条件 如：( id=?)
     * @param selectionArgs 条件里的参数，用来替换"?"
     * @param orderBy       排序 如：id desc
     * @return 返回Cursor
     */
    public synchronized Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String orderBy) {
        return getDataBase().query(table, columns, selection, selectionArgs, null, null, orderBy);
    }

    /**
     * 查询数据
     *
     * @param distinct      每行是唯一 true:表示唯一 false:表示不唯一
     * @param table         表名
     * @param columns       要查询的列名
     * @param selection     查询条件 如：( id=?)
     * @param selectionArgs 条件里的参数，用来替换"?"
     * @param orderBy       排序 如：id desc
     * @param limit         查询的条数 如：10
     * @return 返回Cursor
     */
    public synchronized Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String orderBy, String limit) {
        return getDataBase().query(distinct, table, columns, selection, selectionArgs, null, null, orderBy, limit);
    }

    /**
     * 按指定SQL语句查询
     *
     * @param sql
     * @param selectionArgs
     * @return
     */
    public synchronized Cursor query(String sql, String[] selectionArgs) {
        return getDataBase().rawQuery(sql, selectionArgs);
    }

    /**
     * 查询某个字段
     *
     * @param classz        字节码 如：String.class
     * @param table         表名
     * @param columns       要查询的列名
     * @param selection     查询条件 如：( id=?)
     * @param selectionArgs 条件里的参数，用来替换"?"
     * @return 返回Object
     */
    public synchronized Object queryField(Class<?> classz, String table, String[] columns, String selection, String[] selectionArgs) {
        Object o = null;
        // 查询单条记录
        Cursor c = getDataBase().query(table, columns, selection, selectionArgs, null, null, null, "1");
        if (c.moveToFirst()) {
            try {
                if (classz == Integer.TYPE) { // int
                    o = c.getInt(0);
                } else if (classz == String.class) { // String
                    o = c.getString(0);
                } else if (classz == Long.TYPE) { // long
                    o = c.getLong(0);
                } else if (classz == Float.TYPE) { // float
                    o = c.getFloat(0);
                } else if (classz == Double.TYPE) { // double
                    o = c.getDouble(0);
                } else if (classz == Short.TYPE) { // short
                    o = c.getShort(0);
                }
            } catch (Exception e) {
                Log.e("queryField", e.toString());
            }
        }
        c.close();
        return o;
    }

    /**
     * 查询数据 单个对象
     *
     * @param classz        字节码 如：String.class
     * @param table         表名
     * @param columns       要查询的列名
     * @param selection     查询条件 如：( id=?)
     * @param selectionArgs 条件里的参数，用来替换"?"
     * @return 返回Object
     */
    @SuppressWarnings("unchecked")
    public synchronized T queryObject(Class<?> classz, String table, String[] columns, String selection, String[] selectionArgs) {
        // 查询单条记录
        Cursor c = getDataBase().query(table, columns, selection, selectionArgs, null, null, null, "1");
        T t = null;
        if (c.moveToFirst()) {
            try {
                // 生成新的实例
                t = (T) classz.newInstance();
                // 把列的值，转换成对象里属性的值
                columnToField(t, c);
            } catch (Exception e) {
                Log.e("newInstance error", "生成新实例时出错 ：" + e.toString());
            }
        }
        c.close();
        return t;
    }

    /**
     * 查询数据 带分页功能
     *
     * @param classz        字节码 如：String.class
     * @param table         表名
     * @param columns       要查询的列名
     * @param selection     查询条件 如：( id=?)
     * @param selectionArgs 条件里的参数，用来替换"?"
     * @param orderBy       排序 如：id desc
     * @param pageNo        页码 不分页时，为null
     * @param pageSize      每页的个数 不分页时，为null
     * @return 返回List
     */
    @SuppressWarnings("unchecked")
    public synchronized List<T> queryList(Class<?> classz, String table, String[] columns, String selection, String[] selectionArgs, String orderBy, Integer pageNo, Integer pageSize) {
        // 分页
        if (!(pageNo == null || pageSize == null)) {
            // 分页的起始位置
            int begin = (pageNo - 1) * pageSize;
            orderBy = orderBy + " limit " + begin + ", " + pageSize;
        }
        // 查询数据
        Cursor c = getDataBase().query(table, columns, selection, selectionArgs, null, null, orderBy);
        List<T> list = new ArrayList<T>();
        T t = null;
        while (c.moveToNext()) {
            try {
                // 生成新的实例
                t = (T) classz.newInstance();
            } catch (Exception e) {
                Log.e("newInstance error", "生成新实例时出错 ：" + e.toString());
            }
            // 把列的值，转换成对象里属性的值
            columnToField(t, c);
            list.add(t);
        }
        c.close();

        return list;
    }

    /**
     * 把列的值，转换成对象里属性的值
     */
    public synchronized void columnToField(T t, Cursor c) {
        // 获取T里的所有属性
        Field[] f = t.getClass().getDeclaredFields();
        for (int i = 0; i < f.length; i++) {

            int columnIndex = c.getColumnIndex(f[i].getName());
            // 如果为-1，表示不存在此列
            if (columnIndex == -1) {
                continue;
            }

            Class<?> classz = f[i].getType();
            // 设置成可访问，否则不能set值
            f[i].setAccessible(true);

            try {
                if (classz == Integer.TYPE) { // int
                    f[i].set(t, c.getInt(columnIndex));
                } else if (classz == String.class) { // String
                    f[i].set(t, c.getString(columnIndex));
                } else if (classz == Long.TYPE) { // long
                    f[i].set(t, c.getLong(columnIndex));
                } else if (classz == byte[].class) { // byte
                    f[i].set(t, c.getBlob(columnIndex));
                } else if (classz == Float.TYPE) { // float
                    f[i].set(t, c.getFloat(columnIndex));
                } else if (classz == Double.TYPE) { // double
                    f[i].set(t, c.getDouble(columnIndex));
                } else if (classz == Short.TYPE) { // short
                    f[i].set(t, c.getShort(columnIndex));
                }
            } catch (Exception e) {
                Log.e("column to field error", "字段转换成对象时出错 ：" + e.toString());
            }
        }
    }

    /**
     * 开始事务
     */
    protected synchronized void beginTransaction() {
        getDataBase().beginTransaction();
    }

    /**
     * 提交事务及结束事务
     */
    protected synchronized void commit() {
        getDataBase().setTransactionSuccessful();
        getDataBase().endTransaction();
    }

    /**
     * 回滚事务
     */
    protected synchronized void rollback() {
        getDataBase().endTransaction();
    }

    /**
     * 关闭连接
     */
    public synchronized void close() {
        // 不做处理，db为单例无需关闭
    }

    /**
     * 打开连接
     */
    public synchronized void open() {
        // 不做处理，db为单例无需再次打开
    }
}
