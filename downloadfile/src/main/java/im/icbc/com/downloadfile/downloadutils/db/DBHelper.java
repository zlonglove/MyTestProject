package im.icbc.com.downloadfile.downloadutils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created on 2017/7/20.
 * @author zhanglong
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "down.db";
    private static final int VERSION = 1;
    private final String CREATE_TABLE="create table if not exists downloadinfo(_id integer PRIMARY KEY AUTOINCREMENT, thread_id integer,start_pos long, end_pos long, compelete_size long,url char)";

    public DBHelper(Context context){
        super(context,DBNAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
