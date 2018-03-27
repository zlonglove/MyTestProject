package com.ISHello.Banner2;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.transformer.DefaultTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author
 */
public class BannerActivity2 extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OnBannerListener {
    static final int REFRESH_COMPLETE = 0X1112;
    SuperSwipeRefreshLayout mSwipeLayout;
    Banner banner;

    private Handler mHandler = new Handler() {
        /**
         * hanler消息处理
         * @param msg
         */
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    String[] urls = getResources().getStringArray(R.array.url4);
                    List list = Arrays.asList(urls);
                    List arrayList = new ArrayList(list);
                    banner.update(arrayList);
                    mSwipeLayout.setRefreshing(false);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner2);
        mSwipeLayout = (SuperSwipeRefreshLayout) findViewById(R.id.swipe);
        banner = (Banner) findViewById(R.id.banner2);
        mSwipeLayout.setOnRefreshListener(this);

        String[] urls = getResources().getStringArray(R.array.url);
        List list = Arrays.asList(urls);
        List<?> images = new ArrayList(list);
        //简单使用
        banner.setImages(images)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(this)
                .setViewPagerIsScroll(true)
                .setBannerAnimation(DefaultTransformer.class)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
    }

    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(getApplicationContext(), "你点击了：" + position, Toast.LENGTH_SHORT).show();
    }


    /**
     * 如果你需要考虑更好的体验，可以这么操作
     */
    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }


    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
    }


}
