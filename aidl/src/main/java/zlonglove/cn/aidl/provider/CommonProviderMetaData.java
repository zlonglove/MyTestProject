package zlonglove.cn.aidl.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by zhanglong on 2018/3/28.
 */

public class CommonProviderMetaData {
    public static final String AUTHORITY = "zlonglove.cn.provider.commondb";

    public static final String DATABASE_NAME = "note.db";
    public static final int DATABASE_VERSION = 1;

    /**
     *
     *数据库中表相关的元数据
     */
    public static final class NoteTableMetaData implements BaseColumns {

        public static final String TABLE_NAME = "notes";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/notes");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.common.note";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.common.note";

        public static final String NOTE_TITLE = "title";
        public static final String NOTE_CONTENT = "content";
        public static final String CREATE_DATE = "create_date";

        public static final String DEFAULT_ORDERBY = "create_date DESC";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + NOTE_TITLE + " VARCHAR(50),"
                + NOTE_CONTENT + " TEXT,"
                + CREATE_DATE + " INTEGER"
                + ");" ;
    }
}
