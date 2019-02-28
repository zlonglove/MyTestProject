package com.ISHello.Provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * onCreate()运行在main线程中,不能再里面做耗时的操作
 * 其他update.insert.delete.query.delete都运行在一个Binder线程池中
 */
public class BookProvider extends ContentProvider {
    private final String TAG = "BookProvider";
    private static final String AUTHORITY = "cn.zlonglove.com.Book.provider";
    private static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/Book");
    private static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");

    private static final int BOOK_URI_CODE = 0;
    private static final int USER_URI_CODE = 1;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, "Book", BOOK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
    }

    private Context mContext;
    private SQLiteDatabase mdb;

    public BookProvider() {
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        Log.i(TAG, "--->onCreate() current thread:" + Thread.currentThread().getName());
        mContext = getContext();
        mdb = new BookOpenHelper(mContext).getWritableDatabase();
        return false;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        Log.i(TAG, "--->getType()" + Thread.currentThread().getName());
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        Log.i(TAG, "--->delete()" + Thread.currentThread().getName());
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupport URI:" + uri);
        }
        int count = mdb.delete(tableName, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        Log.i(TAG, "--->insert()" + Thread.currentThread().getName());
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupport URI:" + uri);
        }
        mdb.insert(tableName, null, values);
        mContext.getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        Log.i(TAG, "--->query()" + Thread.currentThread().getName());
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupport URI:" + uri);
        }
        return mdb.query(tableName, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        Log.i(TAG, "--->update()" + Thread.currentThread().getName());
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupport URI:" + uri);
        }
        int row = mdb.update(tableName, values, selection, selectionArgs);
        if (row > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (sUriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = BookOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = BookOpenHelper.USER_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }
}
