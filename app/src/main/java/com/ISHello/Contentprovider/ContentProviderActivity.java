package com.ISHello.Contentprovider;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ISHello.Contentprovider.Employees.Employee;
import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentProviderActivity extends BaseActivity {
    private final String TAG = "ContentProviderActivity";
    private ListView contentPeople;
    private List<employeeInfo> persons;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> data;
    private final int UpdateListView = 1;
    private MyHandler myHandler;
    private int itemNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contentprovideractivity);
        contentPeople = (ListView) findViewById(R.id.contentPeople);
        persons = new ArrayList<employeeInfo>();
        data = new ArrayList<Map<String, Object>>();
        myHandler = new MyHandler();

        simpleAdapter = new SimpleAdapter(ContentProviderActivity.this, data,
                R.layout.list_item, new String[]{"id", "name", "age",
                "gender"}, new int[]{R.id.tvId, R.id.tvname,
                R.id.tvage, R.id.tvgender});
        contentPeople.setAdapter(simpleAdapter);

        setListViewItemSelected();

        UpViewThread thread = new UpViewThread();
        thread.start();
    }

    public void setListViewItemSelected() {
        if (contentPeople != null) {
            contentPeople.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    Log.i(TAG, "--->The selected number==" + arg2);
                    employeeInfo employee = new employeeInfo();
                    employee.setName("update");
                    employee.setAge(33);
                    // employee.setGender("女");
                    int _id = persons.get(arg2).get_id();
                    updateContent(_id, employee);
                }
            });
        }

        contentPeople
                .setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v,
                                                    ContextMenuInfo menuInfo) {
                        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                        itemNum = info.position;
                        new AlertDialog.Builder(ContentProviderActivity.this)
                                .setTitle("提示")
                                .setMessage("确定要删除这条数据?")
                                .setNeutralButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dlg,
                                                    int sumthin) {
                                                employeeInfo person = persons
                                                        .get(itemNum);
                                                deleteContent(person.get_id());
                                            }
                                        })
                                .setNegativeButton("取消", new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        return;

                                    }
                                })

                                .show();
                    }
                });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.i(TAG, "点击了长按菜单里面的第" + item.getItemId() + "个项目");
        switch (item.getItemId()) {
            case 1:
                break;
            case 0:

                break;

            default:
                break;
        }

        return super.onContextItemSelected(item);
    }

    /**
     * 更新数据
     *
     * @param view
     */
    public void updateContent(View view) {
        employeeInfo employee = new employeeInfo();
        employee.setName("update");
        employee.setAge(33);
        employee.setGender("女");
        updateContent(1, employee);
    }

    public void updateContent(int _id, employeeInfo employee) {
        Uri uri = ContentUris.withAppendedId(Employee.CONTENT_URI, _id);
        ContentValues contentValues = new ContentValues();
        if (!"".equals(employee.getName())) {
            contentValues.put(Employee.NAME, employee.getName());
        }
        if (!"".equals(employee.getGender())) {
            contentValues.put(Employee.GENDER, employee.getGender());
        }
        if (employee.getAge() != 0) {
            contentValues.put(Employee.AGE, employee.getAge());
        }
        getContentResolver().update(uri, contentValues, null, null);
        UpViewThread thread = new UpViewThread();
        thread.start();
    }

    /**
     * 删除指定行的数据
     *
     * @param id
     */
    public void deleteContent(int id) {
        Uri uri = ContentUris.withAppendedId(Employee.CONTENT_URI, id);
        int num = getContentResolver().delete(uri, null, null);
        Log.i(TAG, "--->The delete num is=" + num);
        UpViewThread thread = new UpViewThread();
        thread.start();
    }

    /**
     * 清空数据
     *
     * @param view
     */
    public void deleteAllData(View view) {
        int num = getContentResolver().delete(Employee.CONTENT_URI, null, null);
        Log.i(TAG, "--->The delete num is=" + num);
        UpViewThread thread = new UpViewThread();
        thread.start();
    }

    /**
     * 添加一条固定的数据项
     *
     * @param view
     */
    public void addContent(View view) {
        Uri uri = Employee.CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.put(Employee.NAME, "zhanglong");
        contentValues.put(Employee.GENDER, "男");
        contentValues.put(Employee.AGE, 25);
        Uri result = getContentResolver().insert(uri, contentValues);
        if (ContentUris.parseId(result) >= 0) {
            Log.i(TAG, "--->The Insert number is==" + result.toString());
            showToast("添加成功", Toast.LENGTH_SHORT);
            // 添加成功后再启动线程查询
            UpViewThread thread = new UpViewThread();
            thread.start();
        }

    }

    /**
     * 查找所有数据
     *
     * @param view
     */
    public void queryContent(View view) {
        UpViewThread thread = new UpViewThread();
        thread.start();
    }

    public synchronized void query() {
        String[] PROJECTION = new String[]{Employee._ID, Employee.NAME,
                Employee.GENDER, Employee.AGE};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(Employee.CONTENT_URI, PROJECTION, null,
                null, Employee.DEFAULT_SORT_ORDER);
        while (cursor.moveToNext()) {
            employeeInfo person = new employeeInfo();
            person.set_id(cursor.getInt(cursor.getColumnIndex(Employee._ID)));
            person.setName(cursor.getString(cursor
                    .getColumnIndex(Employee.NAME)));
            person.setGender(cursor.getString(cursor
                    .getColumnIndex(Employee.GENDER)));
            person.setAge(cursor.getInt(cursor.getColumnIndex(Employee.AGE)));
            persons.add(person);
        }
        cursor.close();
        Map<String, Object> map = null;
        for (int i = 0; i < persons.size(); i++) {
            map = new HashMap<String, Object>();
            map.put("id", persons.get(i).get_id());
            map.put("name", persons.get(i).getName());
            map.put("age", persons.get(i).getAge());
            map.put("gender", persons.get(i).getGender());
            data.add(map);
        }
        Message msg = myHandler.obtainMessage();
        msg.what = UpdateListView;
        myHandler.sendMessage(msg);
    }

    /**
     * 更新listView里的数据
     */
    public void updateListViewData() {
        if (contentPeople != null) {
            SimpleAdapter sAdapter = (SimpleAdapter) contentPeople.getAdapter();
            sAdapter.notifyDataSetChanged();
        }
    }

    class UpViewThread extends Thread {
        public UpViewThread() {
            persons.clear();
            data.clear();
        }

        @Override
        public void run() {
            query();
        }

    }

    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UpdateListView:
                    updateListViewData();
                    break;

                default:
                    break;
            }
        }

    }

}
