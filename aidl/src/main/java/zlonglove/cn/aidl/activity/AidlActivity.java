package zlonglove.cn.aidl.activity;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import zlonglove.cn.aidl.R;
import zlonglove.cn.aidl.aidlInterface.IMyAidlInterface;
import zlonglove.cn.aidl.provider.CommonProviderMetaData;
import zlonglove.cn.aidl.service.AidlService;

public class AidlActivity extends AppCompatActivity {
    private final String TAG = AidlActivity.class.getSimpleName();
    private Toolbar toolbar;
    private IMyAidlInterface iMyAidlInterface;

    private Button aidlInsert;
    private Button aidlModify;
    private Button aidlDelete;
    private Button aidlQuery;

    public static final String AUTHORITY = "zlonglove.cn.provider.commondb";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/notes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_aidl);
        findViews();
        initView();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        aidlInsert = (Button) findViewById(R.id.aidl_insert);
        aidlModify = (Button) findViewById(R.id.aidl_modify);
        aidlDelete = (Button) findViewById(R.id.aidl_delete);
        aidlQuery = (Button) findViewById(R.id.aidl_query);
    }

    private void initView() {
        //设置导航图标要在setSupportActionBar方法之后
        setSupportActionBar(toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        aidlInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });
        aidlModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        aidlDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        aidlQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query();
            }
        });

        bindService(new Intent(getApplicationContext(), AidlService.class), new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i(TAG, "--->onServiceConnected()");
                iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
                try {
                    Log.i(TAG, "--->" + iMyAidlInterface.getName());
                    Log.i(TAG, "--->" + iMyAidlInterface.getUser().toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i(TAG, "--->onServiceDisconnected()");
            }
        }, BIND_AUTO_CREATE);
    }

    private void insert() {
        ContentValues values = new ContentValues();
        values.put("title", "hello");
        values.put("content", "my name is alex zhou");
        long time = System.currentTimeMillis();
        values.put("create_date", time);
        Uri uri = this.getContentResolver().insert(CONTENT_URI, values);
        Log.i(TAG, "--->insert()" + uri.toString());
        /**
         * content://zlonglove.cn.provider.commondb/notes/1
         */
    }

    private void query() {
        Cursor cursor = this.getContentResolver().query(CONTENT_URI, null, null, null, null);
        Log.i(TAG, "--->count=" + cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(cursor.getColumnIndex(CommonProviderMetaData.NoteTableMetaData._ID));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            long createDate = cursor.getLong(cursor.getColumnIndex("create_date"));
            Log.i(TAG, "--->id: " + id);
            Log.i(TAG, "--->title: " + title);
            Log.i(TAG, "--->content: " + content);
            Log.i(TAG, "--->date: " + createDate);

            cursor.moveToNext();
        }
        cursor.close();
    }

    private void update() {
        ContentValues values = new ContentValues();
        values.put("content", "my name is alex zhou !!!!!!!!!!!!!!!!!!!!!!!!!!");
        int count = this.getContentResolver().update(CONTENT_URI, values, "_id=1", null);
        Log.e(TAG, "--->count=" + count);
    }

    private void delete() {
        int count = this.getContentResolver().delete(CONTENT_URI, "_id=1", null);
        Log.e(TAG, "--->count=" + count);
    }


}
