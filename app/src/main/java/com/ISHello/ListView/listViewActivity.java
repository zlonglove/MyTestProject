package com.ISHello.ListView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ISHello.utils.CaptureUtils;
import com.example.ishelloword.R;

import java.util.ArrayList;
import java.util.HashMap;

public class listViewActivity extends Activity {
    private static final String TAG = "listViewActivity";
    private ListView list;
    private ImageView cacheList;
    private ImageView cacheWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listviewactivity);
        list = (ListView) findViewById(R.id.ListView01);
        cacheList = (ImageView) findViewById(R.id.cacheList);
        cacheWindow = (ImageView) findViewById(R.id.cacheWindow);

        //生成动态数组，加入数据
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", R.drawable.ic_launcher);//图像资源的ID
            map.put("ItemTitle", "Level " + i);
            map.put("ItemText", "Finished in 1 Min 54 Secs, 70 Moves! ");
            listItem.add(map);
        }

        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,//数据源
                //ListItem的XML实现
                R.layout.listviewlayout,
                //动态数组与ImageItem对应的子项
                new String[]{"ItemImage", "ItemTitle", "ItemText"},
                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[]{R.id.ItemImage, R.id.ItemTitle, R.id.ItemText}
        );

        //添加并且显示
        list.setAdapter(listItemAdapter);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Log.i(TAG, "点击第" + position + "个项目");
            }

        });

        list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("长按菜单-ContextMenu");
                menu.add(0, 0, 0, "弹出长按菜单0");
                menu.add(0, 1, 0, "弹出长按菜单1");
            }

        });

        cacheList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cacheList.setImageBitmap(CaptureUtils.getInstance().shotListView(list));
            }
        });

        cacheWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cacheWindow.setImageBitmap(CaptureUtils.getInstance().shotActivity(listViewActivity.this));
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.i(TAG, "点击了长按菜单里面的第" + item.getItemId() + "个项目");
        // TODO Auto-generated method stub
        return super.onContextItemSelected(item);
    }

    /**
     * 更新listView里的内容
     */
    public void updateListView() {
        if (list != null) {
            SimpleAdapter sAdapter = (SimpleAdapter) list.getAdapter();
            sAdapter.notifyDataSetChanged();
        }
    }


}
