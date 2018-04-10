package zlonglove.cn.aidl.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

import zlonglove.cn.aidl.util.util;

/**
 * Created by zhanglong on 2018/3/28.
 */

public class CommonProvider extends ContentProvider {
    private final String TAG = CommonProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher;
    private static final int COLLECTION_INDICATOR = 1;
    private static final int SINGLE_INDICATOR = 2;
    private static HashMap<String, String> sNotesProjectionMap;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(CommonProviderMetaData.AUTHORITY, "notes", COLLECTION_INDICATOR);
        sUriMatcher.addURI(CommonProviderMetaData.AUTHORITY, "notes/#", SINGLE_INDICATOR);

        sNotesProjectionMap = new HashMap<>();
        sNotesProjectionMap.put(CommonProviderMetaData.NoteTableMetaData._ID, CommonProviderMetaData.NoteTableMetaData._ID);
        sNotesProjectionMap.put(CommonProviderMetaData.NoteTableMetaData.NOTE_CONTENT, CommonProviderMetaData.NoteTableMetaData.NOTE_CONTENT);
        sNotesProjectionMap.put(CommonProviderMetaData.NoteTableMetaData.NOTE_TITLE, CommonProviderMetaData.NoteTableMetaData.NOTE_TITLE);
        sNotesProjectionMap.put(CommonProviderMetaData.NoteTableMetaData.CREATE_DATE, CommonProviderMetaData.NoteTableMetaData.CREATE_DATE);
    }

    private DatabaseHelper mDbHelper;

    @Override
    public boolean onCreate() {
        Log.i(TAG, "--->onCreate()");
        mDbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.i(TAG, "--->query() Uri==" + uri.toString());
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case COLLECTION_INDICATOR:
                // 设置查询的表
                queryBuilder.setTables(CommonProviderMetaData.NoteTableMetaData.TABLE_NAME);
                // 设置投影映射
                queryBuilder.setProjectionMap(sNotesProjectionMap);
                break;

            case SINGLE_INDICATOR:
                queryBuilder.setTables(CommonProviderMetaData.NoteTableMetaData.TABLE_NAME);
                queryBuilder.setProjectionMap(sNotesProjectionMap);
                queryBuilder.appendWhere(CommonProviderMetaData.NoteTableMetaData._ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknow URI: " + uri);
        }

        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = CommonProviderMetaData.NoteTableMetaData.DEFAULT_ORDERBY;
        } else {
            orderBy = sortOrder;
        }
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, orderBy);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.i(TAG, "--->getType() Uri==" + uri.toString());
        switch (sUriMatcher.match(uri)) {
            case COLLECTION_INDICATOR:
                return CommonProviderMetaData.NoteTableMetaData.CONTENT_TYPE;

            case SINGLE_INDICATOR:
                return CommonProviderMetaData.NoteTableMetaData.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unknow URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.i(TAG, "--->insert() processName==" + util.getProcessName(getContext()));
        if (sUriMatcher.match(uri) != COLLECTION_INDICATOR) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long rowID = db.insert(CommonProviderMetaData.NoteTableMetaData.TABLE_NAME, null, values);

        if (rowID > 0) {
            Uri retUri = ContentUris.withAppendedId(CommonProviderMetaData.NoteTableMetaData.CONTENT_URI, rowID);
            return retUri;
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.i(TAG, "--->delete() uri==" + uri.toString());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count = -1;
        /*switch (sUriMatcher.match(uri)) {
            case COLLECTION_INDICATOR:
                count = db.delete(CommonProviderMetaData.NoteTableMetaData.TABLE_NAME, selection, selectionArgs);
                break;

            case SINGLE_INDICATOR:
                String rowID = uri.getPathSegments().get(1);
                count = db.delete(CommonProviderMetaData.NoteTableMetaData.TABLE_NAME, CommonProviderMetaData.NoteTableMetaData._ID + "=" + rowID, null);
                break;

            default:
                throw new IllegalArgumentException("Unknow URI :" + uri);

        }*/
        count = db.delete(CommonProviderMetaData.NoteTableMetaData.TABLE_NAME, selection, selectionArgs);
        // 更新数据时，通知其他ContentObserver
        this.getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count = -1;
        /*switch (sUriMatcher.match(uri)) {
            case COLLECTION_INDICATOR:
                count = db.update(CommonProviderMetaData.NoteTableMetaData.TABLE_NAME, values, null, null);
                break;

            case SINGLE_INDICATOR:
                String rowID = uri.getPathSegments().get(1);
                count = db.update(CommonProviderMetaData.NoteTableMetaData.TABLE_NAME, values, CommonProviderMetaData.NoteTableMetaData._ID + "=" + rowID, null);
                break;

            default:
                throw new IllegalArgumentException("Unknow URI : " + uri);

        }*/
        count = db.update(CommonProviderMetaData.NoteTableMetaData.TABLE_NAME, values, selection, selectionArgs);
        this.getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    /**
     * aidl进程建表
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, CommonProviderMetaData.DATABASE_NAME, null, CommonProviderMetaData.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.e("DatabaseHelper", "create table: " + CommonProviderMetaData.NoteTableMetaData.SQL_CREATE_TABLE);
            db.execSQL(CommonProviderMetaData.NoteTableMetaData.SQL_CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CommonProviderMetaData.NoteTableMetaData.TABLE_NAME);
            onCreate(db);
        }

    }

}
