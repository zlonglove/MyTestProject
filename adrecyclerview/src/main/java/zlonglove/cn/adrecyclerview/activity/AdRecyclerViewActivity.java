package zlonglove.cn.adrecyclerview.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
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

// mAdapter.notifyDataSetChanged()
/*DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBack(oldDatas, newDatas), true);
  diffResult.dispatchUpdatesTo(mAdapter);
  DiffUtil最终是调用Adapter的下面几个方法来进行局部刷新：
  mAdapter.notifyItemRangeInserted(position, count);
 mAdapter.notifyItemRangeRemoved(position, count);
 mAdapter.notifyItemMoved(fromPosition, toPosition);
 mAdapter.notifyItemRangeChanged(position, count, payload);
  */
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
        if (!MenuHelper.hasEverInit()) {
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

        //LineItemDecoration lineItemDecoration = new LineItemDecoration(this);
        //mRecyclerView.addItemDecoration(lineItemDecoration);

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
            Toast.makeText(this, item.toString(), Toast.LENGTH_SHORT).show();
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
            /*initData();
            mAdapter.notifyDataSetChanged();*/
            refreshData();
        }
    }

    private void refreshData() {
        List<MenuItem> oldData = mAdapter.getRecyclerItems();
        List<MenuItem> newData = MenuHelper.getPreferFavoriteList();
        MenuItem add = new MenuItem();
        add.setName("添加");
        add.setIcon("add");
        add.setItemId(ID_ADD_ITEM);
        newData.add(add);
        DiffUtil.DiffResult DiffCallBack = DiffUtil.calculateDiff(new DiffCallBack(oldData, newData), true);
        mAdapter.setData(newData);
        DiffCallBack.dispatchUpdatesTo(mAdapter);
    }

    public class DiffCallBack extends DiffUtil.Callback {
        //Thing 是adapter 的数据类，要换成自己的adapter 数据类
        private List<MenuItem> oldData;
        private List<MenuItem> newData;

        public DiffCallBack(List<MenuItem> oldData, List<MenuItem> newData) {
            this.oldData = oldData;
            this.newData = newData;
        }

        /**
         * 旧数据的size
         */
        @Override
        public int getOldListSize() {
            return oldData.size();
        }

        /**
         * 新数据的size
         */
        @Override
        public int getNewListSize() {
            return newData.size();
        }

        /**
         * 这个方法自由定制 ，
         * 在对比数据的时候会被调用
         * 返回 true 被判断为同一个item
         */
        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            MenuItem currentItem = oldData.get(oldItemPosition);
            MenuItem nextItem = newData.get(newItemPosition);
            return currentItem.getName().equals(nextItem.getName());
        }

        /**
         * 在上面的方法返回true 时，
         * 这个方法才会被diff 调用
         * 返回true 就证明内容相同
         */
        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            MenuItem currentItem = oldData.get(oldItemPosition);
            MenuItem nextItem = newData.get(newItemPosition);
            return currentItem.getName().equals(nextItem.getName());
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
