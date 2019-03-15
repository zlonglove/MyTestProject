package com.ISHello.baseModule.fragment;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.ISHello.baseModule.common.MyLazyFragment;
import com.ISHello.baseModule.widget.XCollapsingToolbarLayout;
import com.gyf.barlibrary.ImmersionBar;

import zlonglove.cn.base.R;

/**
 * @author : zl
 * @github :
 * @time : 2018/10/18
 * @desc : 项目炫酷效果示例
 */
public class TestFragmentA extends MyLazyFragment implements XCollapsingToolbarLayout.OnScrimsListener {
    private XCollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;

    private TextView mAddressView;
    private TextView mSearchView;

    public static TestFragmentA newInstance() {
        return new TestFragmentA();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_a;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.tb_test_a_bar;
    }


    @Override
    protected void findViews() {
        mCollapsingToolbarLayout = findViewById(R.id.ctl_test_bar);
        mToolbar = findViewById(R.id.t_test_title);
        mAddressView = findViewById(R.id.tv_test_address);
        mSearchView = findViewById(R.id.tv_test_search);
    }

    @Override
    protected void initView() {
        // 给这个ToolBar设置顶部内边距，才能和TitleBar进行对齐
        ImmersionBar.setTitleBar(getFragmentActivity(), mToolbar);

        //设置渐变监听
        mCollapsingToolbarLayout.setOnScrimsListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public boolean statusBarDarkFont() {
        return mCollapsingToolbarLayout.isScrimsShown();
    }

    /**
     * {@link XCollapsingToolbarLayout.OnScrimsListener}
     */
    @Override
    public void onScrimsStateChange(boolean shown) {
        // CollapsingToolbarLayout 发生了渐变
        if (shown) {
            mAddressView.setTextColor(getResources().getColor(R.color.black));
            mSearchView.setBackgroundResource(R.drawable.bg_home_search_bar_gray);
            getStatusBarConfig().statusBarDarkFont(true).init();
        } else {
            mAddressView.setTextColor(getResources().getColor(R.color.white));
            mSearchView.setBackgroundResource(R.drawable.bg_home_search_bar_transparent);
            getStatusBarConfig().statusBarDarkFont(false).init();
        }
    }
}