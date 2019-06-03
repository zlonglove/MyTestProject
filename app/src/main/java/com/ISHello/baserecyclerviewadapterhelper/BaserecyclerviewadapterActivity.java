package com.ISHello.baserecyclerviewadapterhelper;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.ISHello.base.base.BaseActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.ishelloword.R;

import java.util.ArrayList;

/**
 * @author zhanglong
 */
public class BaserecyclerviewadapterActivity extends BaseActivity {
    /*private static final Class<?>[] ACTIVITY = {AnimationUseActivity.class, ChooseMultipleItemUseTypeActivity.class,
            HeaderAndFooterUseActivity.class, PullToRefreshUseActivity.class, SectionUseActivity.class, EmptyViewUseActivity.class,
            ItemDragAndSwipeUseActivity.class, ItemClickActivity.class, ExpandableUseActivity.class, DataBindingUseActivity.class,
            UpFetchUseActivity.class, SectionMultipleItemUseActivity.class};*/

    private static final String[] TITLE = {"Animation", "MultipleItem", "Header/Footer", "PullToRefresh", "Section",
            "EmptyView", "DragAndSwipe", "ItemClick", "ExpandableItem", "DataBinding", "UpFetchData", "SectionMultipleItem"};

    private static final int[] IMG = {R.drawable.gv_animation, R.drawable.gv_animation, R.drawable.gv_animation, R.drawable.gv_animation,
            R.drawable.gv_animation, R.drawable.gv_animation, R.drawable.gv_animation, R.drawable.gv_animation, R.drawable.gv_animation, R.drawable.gv_animation,
            R.drawable.gv_animation, R.drawable.gv_animation};

    private ArrayList<HomeItem> mDataList;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baserecyclerviewadapter);
        setTitle("Base Recycler View Adapter");
        initView();
        initData();
        initAdapter();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void initAdapter() {
        BaseQuickAdapter homeAdapter = new HomeAdapter(R.layout.home_item_view, mDataList);
        homeAdapter.openLoadAnimation();
        View top = getLayoutInflater().inflate(R.layout.top_view, (ViewGroup) mRecyclerView.getParent(), false);
        homeAdapter.addHeaderView(top);
        homeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                /*Intent intent = new Intent(HomeActivity.this, ACTIVITY[position]);
                startActivity(intent);*/
            }
        });
        mRecyclerView.setAdapter(homeAdapter);
    }

    private void initData() {
        mDataList = new ArrayList<>();
        for (int i = 0; i < TITLE.length; i++) {
            HomeItem item = new HomeItem();
            item.setTitle(TITLE[i]);
            //item.setActivity(ACTIVITY[i]);
            item.setActivity(null);
            item.setImageResource(IMG[i]);
            mDataList.add(item);
        }
    }
}
