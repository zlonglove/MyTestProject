package com.ISHello.RecyclerView.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

import com.ISHello.RecyclerView.Bean.TallyDetailBean;
import com.ISHello.RecyclerView.stickyheader.StickyHeaderGridLayoutManager;
import com.ISHello.RecyclerView.utils.TestDataUtil;
import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

import java.util.List;

public class StickHeaderActivity extends BaseActivity {
    private RecyclerView rvList;
    private static final int SPAN_SIZE = 1;
    private StickyHeaderGridLayoutManager mLayoutManager;
    private TallyDetailAdapter adapter;
    private List<TallyDetailBean.DaylistBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_header);
        findViews();
        init();
    }

    private void findViews() {
        rvList = findViewById(R.id.rv_list);
    }

    private void init() {
        mLayoutManager = new StickyHeaderGridLayoutManager(SPAN_SIZE);
        mLayoutManager.setHeaderBottomOverlapMargin(5);
        rvList.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean animateRemove(RecyclerView.ViewHolder holder) {
                dispatchRemoveFinished(holder);
                return false;
            }
        });
        rvList.setLayoutManager(mLayoutManager);
        adapter = new TallyDetailAdapter(this, list);
        rvList.setAdapter(adapter);
        setTestData();
    }

    private void setTestData() {
        TallyDetailBean data = TestDataUtil.getTallyDetailBean();
        list = data.getDaylist();
        adapter.setmDatas(list);
        adapter.notifyAllSectionsDataSetChanged();//需调用此方法刷新
    }
}
