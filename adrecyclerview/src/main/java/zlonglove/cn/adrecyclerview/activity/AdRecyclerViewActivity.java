package zlonglove.cn.adrecyclerview.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zlonglove.cn.adrecyclerview.R;
import zlonglove.cn.adrecyclerview.adapter.MenuRecyclerGridAdapter;
import zlonglove.cn.adrecyclerview.base.OnRecyclerItemClickListener;
import zlonglove.cn.adrecyclerview.entity.MenuItem;
import zlonglove.cn.adrecyclerview.helper.MenuHelper;
import zlonglove.cn.adrecyclerview.tools.ContextUtil;

public class AdRecyclerViewActivity extends AppCompatActivity implements OnRecyclerItemClickListener<MenuItem> {

    private RecyclerView mRecyclerView;

    private List<MenuItem> mFavList;

    private MenuRecyclerGridAdapter mAdapter;
    static final int ID_ADD_ITEM = -1;//自定义添加条目的

    private RecyclerUpdateReceiver mRecyclerUpdateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextUtil.init(getApplicationContext());
        if(!MenuHelper.hasEverInit()){
            MenuHelper.init();
        }
        setTitle("主页");
        setContentView(R.layout.activity_ad_recycler_view);
        initView();
        initEvents();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_display);
    }

    private void initEvents() {
        initData();
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new MenuRecyclerGridAdapter(mFavList);
        mAdapter.setOnRecyclerItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        //注册刷新数据的广播
        mRecyclerUpdateReceiver = new RecyclerUpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(1000);
        filter.addAction("refreshMainListData");
        registerReceiver(mRecyclerUpdateReceiver, filter);

    }

    @Override
    public void onItemClick(View v, MenuItem item, int position, int segment) {
        if (item.getItemId() == ID_ADD_ITEM) {
            Intent i = new Intent(this, AdRecyclerEditActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this, item.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 初始化数据列表
     */
    private void initData() {
        if (mFavList != null) {
            mFavList.clear();
        } else {
            mFavList = new ArrayList<>();
        }
        mFavList.addAll(MenuHelper.getPreferFavoriteList());
        MenuItem add = new MenuItem();
        add.setName("添加");
        add.setIcon("add");
        add.setItemId(ID_ADD_ITEM);
        mFavList.add(add);
    }


    /**
     * 用于执行刷新数据的广播接收器
     */
    private class RecyclerUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            initData();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        //注销刷新数据的广播
        if (mRecyclerUpdateReceiver != null) {
            unregisterReceiver(mRecyclerUpdateReceiver);
        }
        super.onDestroy();
    }
}
