package com.ISHello.Contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.ISHello.Contentprovider.Employees.Employee;

import java.util.HashMap;

public class ISContentProvider extends ContentProvider {

    private final String TAG = "ISContentProvider";
    private EmployeesDBHelper dbHelper;
    SQLiteDatabase db;

    // 定义一个UriMatcher类
    private static final UriMatcher MATCHER = new UriMatcher(
            UriMatcher.NO_MATCH);
    // 访问表的所有列
    private static final int EMPLOYEE = 1;
    // 访问单独的列
    private static final int EMPLOYEE_ID = 2;

    private static HashMap<String, String> emProjectMap;

    static {
        MATCHER.addURI(Employees.AUTHORITY, "employee", EMPLOYEE);
        MATCHER.addURI(Employees.AUTHORITY, "employee/#", EMPLOYEE_ID);

        emProjectMap = new HashMap<String, String>();
        emProjectMap.put(Employee._ID, Employee._ID);
        emProjectMap.put(Employee.NAME, Employee.NAME);
        emProjectMap.put(Employee.GENDER, Employee.GENDER);
        emProjectMap.put(Employee.AGE, Employee.AGE);
    }

    @Override
    public boolean onCreate() {
        Log.i(TAG, "--->ContProvider---onCreate()");
        dbHelper = new EmployeesDBHelper(this.getContext());
        db = dbHelper.getWritableDatabase();
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.i(TAG, "--->ContProvider---delete()");
        Log.i(TAG, "--->The MATCHER==" + MATCHER.match(uri));
        int count;
        switch (MATCHER.match(uri)) {
            case EMPLOYEE:
                count = db.delete(EmployeesDBHelper.TABEL_NAME, selection,
                        selectionArgs);
                break;
            case EMPLOYEE_ID:
                String noteID = uri.getPathSegments().get(1);
                count = db.delete(EmployeesDBHelper.TABEL_NAME, Employee._ID
                        + "="
                        + noteID
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                        + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("错误的URI" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        Log.i(TAG, "--->ContProvider---getType()");
        switch (MATCHER.match(uri)) {
            case EMPLOYEE:
                return Employee.CONTENT_TYPE;
            case EMPLOYEE_ID:
                return Employee.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.i(TAG, "--->ContProvider---insert()");
        if (db != null) {
            long rowID = db.insert(EmployeesDBHelper.TABEL_NAME, Employee.NAME,
                    values);
            Log.i(TAG, "--->insert rowID==" + rowID);
            if (rowID > 0) {
                Uri empUri = ContentUris.withAppendedId(Employee.CONTENT_URI,
                        rowID);
                getContext().getContentResolver().notifyChange(empUri, null);
                return empUri;
            }
        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.i(TAG, "--->ContProvider---query()");
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (MATCHER.match(uri)) {
            // 查询所有
            case EMPLOYEE:
                qb.setTables(EmployeesDBHelper.TABEL_NAME);
                qb.setProjectionMap(emProjectMap);
                break;
            case EMPLOYEE_ID:
                qb.setTables(EmployeesDBHelper.TABEL_NAME);
                qb.setProjectionMap(emProjectMap);
                qb.appendWhere(Employee._ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Uri错误! " + uri);
        }
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = Employee.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }
        if (db != null) {
            Cursor cursor = qb.query(db, projection, selection, selectionArgs,
                    null, null, orderBy);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.i(TAG, "--->ContProvider---update()");
        int count;
        switch (MATCHER.match(uri)) {
            case EMPLOYEE:
                count = db.update(EmployeesDBHelper.TABEL_NAME, values, selection,
                        selectionArgs);
                break;
            case EMPLOYEE_ID:
                String noteID = uri.getPathSegments().get(1);
                count = db.update(EmployeesDBHelper.TABEL_NAME, values,

                        Employee._ID
                                + "="
                                + noteID
                                + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                                + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("错误的URI" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
