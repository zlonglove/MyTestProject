package com.ISHello.baserecyclerviewadapterhelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ISHello.base.base.BaseActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.ishelloword.R;
import com.in.zlonglove.commonutil.ToastUtils;

import java.util.ArrayList;
import java.util.List;

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
            R.drawable.gv_animation, R.drawable.gv_animation, R.drawable.gv_animation, R.drawable.gv_animation, R.drawable.gv_animation,
            R.drawable.gv_animation, R.drawable.gv_animation, R.drawable.gv_animation};

    private ArrayList<HomeItem> mDataList;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter homeAdapter;

    private int mNextRequestPage = 1;
    private static final int PAGE_SIZE = 6;

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

    private void initAdapter() {
        homeAdapter = new HomeAdapter(R.layout.home_item_view, mDataList);
        homeAdapter.openLoadAnimation();
        View top = getLayoutInflater().inflate(R.layout.top_view, (ViewGroup) mRecyclerView.getParent(), false);
        homeAdapter.addHeaderView(top);
        homeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                /*Intent intent = new Intent(HomeActivity.this, ACTIVITY[position]);
                startActivity(intent);*/
                ToastUtils.showShortToast(TITLE[position]);
            }
        });
        homeAdapter.setEnableLoadMore(true);
        homeAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Log.i("zhanglong","--->onLoadMoreRequested()"+mNextRequestPage);
                loadMore();
            }
        });
        mRecyclerView.setAdapter(homeAdapter);
    }

    private void loadMore() {
        new Request(mNextRequestPage, new RequestCallBack() {
            @Override
            public void success(List<HomeItem> data) {
                /**
                 */
                boolean isRefresh = mNextRequestPage == 1;
                setData(isRefresh, data);
            }

            @Override
            public void fail(Exception e) {
                homeAdapter.loadMoreFail();
                Toast.makeText(BaserecyclerviewadapterActivity.this, "Simulation network error", Toast.LENGTH_LONG).show();
            }
        }).start();
    }

    private void setData(boolean isRefresh, List data) {
        mNextRequestPage++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            homeAdapter.addData(data);
        } else {
            if (size > 0) {
                homeAdapter.addData(data);
            }
        }
        if (size < PAGE_SIZE) {
            //第一页如果不够一页就不显示没有更多数据布局
            homeAdapter.loadMoreEnd(isRefresh);
            Toast.makeText(this, "no more data", Toast.LENGTH_SHORT).show();
        } else {
            homeAdapter.loadMoreComplete();
        }
    }

    interface RequestCallBack {
        void success(List<HomeItem> data);

        void fail(Exception e);
    }

    class Request extends Thread {
        private static final int PAGE_SIZE = 6;
        private int mPage;
        private RequestCallBack mCallBack;
        private Handler mHandler;

        private boolean mFirstPageNoMore;
        private boolean mFirstError = true;

        public Request(int page, RequestCallBack callBack) {
            mPage = page;
            mCallBack = callBack;
            mHandler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }

            int size = PAGE_SIZE;
            if (mPage == 1) {
                if (mFirstPageNoMore) {
                    size = 1;
                }
                mFirstPageNoMore = !mFirstPageNoMore;
                if (!mFirstError) {
                    mFirstError = true;
                }
            } else if (mPage == 4) {
                size = 1;
            }

            final int dataSize = size;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallBack.success(getSampleData(dataSize));
                }
            });

        }
    }

    public List<HomeItem> getSampleData(int lenth) {
        List<HomeItem> list = new ArrayList<>();
        for (int i = 0; i < lenth; i++) {
            HomeItem item = new HomeItem();
            item.setTitle(TITLE[i] + i);
            //item.setActivity(ACTIVITY[i]);
            item.setActivity(null);
            item.setImageResource(IMG[i]);
            list.add(item);
        }
        return list;
    }
}
