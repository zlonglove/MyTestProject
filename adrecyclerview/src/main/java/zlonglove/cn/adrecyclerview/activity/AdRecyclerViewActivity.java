package zlonglove.cn.adrecyclerview.activity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextUtil.init(getApplicationContext());
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

    }

    @Override
    public void onItemClick(View v, MenuItem item, int position, int segment) {
        if (item.getItemId() == ID_ADD_ITEM) {
            Toast.makeText(this, item.toString(), Toast.LENGTH_SHORT).show();
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
        mFavList.addAll(MenuHelper.parseJSONData());
        MenuItem add = new MenuItem();
        add.setName("添加");
        add.setIcon("add");
        add.setItemId(ID_ADD_ITEM);
        mFavList.add(add);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
