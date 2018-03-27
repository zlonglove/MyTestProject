package com.ISHello.ViewPage;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class ScrollableTabView extends HorizontalScrollView implements
        ViewPager.OnPageChangeListener {

    private final LinearLayout mContainer;
    private ViewPager mPager = null;
    private TabAdapter mAdapter = null;
    private final String TAG = "ScrollableTabView";

    public ScrollableTabView(Context context) {
        this(context, null);
    }

    public ScrollableTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollableTabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        this.setHorizontalScrollBarEnabled(false);
        this.setHorizontalFadingEdgeEnabled(false);

        mContainer = new LinearLayout(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        mContainer.setLayoutParams(params);
        mContainer.setOrientation(LinearLayout.HORIZONTAL);

        this.addView(mContainer);

    }

    /**
     * 设置ScrollableTabView的内容适配器
     *
     * @param adapter
     */
    public void setAdapter(TabAdapter adapter) {
        this.mAdapter = adapter;
        if (mPager != null && mAdapter != null) {
            initTabs();
        }
    }

    /**
     * 设置ScrollableTabView相对应的ViewPager适配器
     *
     * @param pager
     */
    public void setViewPager(ViewPager pager) {
        this.mPager = pager;
        mPager.setOnPageChangeListener(this);
        if (mPager != null && mAdapter != null) {
            initTabs();
        }
    }

    private void initTabs() {

        mContainer.removeAllViews();
        //mTabs.clear();

        if (mAdapter == null)
            return;

        for (int i = 0; i < mPager.getAdapter().getCount(); i++) {
            final int index = i;
            View tab = mAdapter.getView(i);
            mContainer.addView(tab);
            tab.setFocusable(true);
            //tab.setTag(i);我的想法
            //mTabs.add(tab);
            tab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "--->ScrollableTabView onClick");
                    Log.i(TAG, "--->ScrollableTabView index==" + index + "mPager.getCurrentItem()==" + mPager.getCurrentItem());
                    if (mPager.getCurrentItem() == index) {
                        selectTab(index);
                    } else {
                        mPager.setCurrentItem(index, true);
                    }
                }
            });
        }

        selectTab(mPager.getCurrentItem());
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.i(TAG, "--->onPageSelected==" + position);
        selectTab(position);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed && mPager != null)
            selectTab(mPager.getCurrentItem());
    }

    private void selectTab(int position) {

        for (int i = 0, pos = 0; i < mContainer.getChildCount(); i++, pos++) {
            View tab = mContainer.getChildAt(i);
            tab.setSelected(pos == position);
        }
        View selectedTab = mContainer.getChildAt(position);

        final int w = selectedTab.getMeasuredWidth();
        final int l = selectedTab.getLeft();

        final int x = l - this.getWidth() / 2 + w / 2;
        smoothScrollTo(x, this.getScrollY());
    }

}
