package com.ISHello.ViewPage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.example.ishelloword.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class ViewPagerActivity extends FragmentActivity {

    private final String TABS_ENABLED = "tabs_enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slidingscreen);
        initPager();
    }

    public void initPager() {
        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager());

        //Get tab visibility preferences
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> defaults = new HashSet<String>(Arrays.asList(
                getResources().getStringArray(R.array.tab_titles)
        ));
        Set<String> tabs_set = sp.getStringSet(TABS_ENABLED, defaults);
        //if its empty fill reset it to full defaults
        //stops app from crashing when no tabs are shown
        //TODO:rewrite activity to not crash when no tabs are chosen to show
        if (tabs_set.size() == 0) {
            tabs_set = defaults;
        }

        //Only show tabs that were set in preferences

        // Recently added tracks
        if (tabs_set.contains(getResources().getString(R.string.tv_direct))) {
            mPagerAdapter.addFragment(new FirstFragment());
        }
        // Artists
        if (tabs_set.contains(getResources().getString(R.string.tv_interactive))) {
            mPagerAdapter.addFragment(new SecondFragment());
        }
        // Albums
        if (tabs_set.contains(getResources().getString(R.string.tv_live))) {
            mPagerAdapter.addFragment(new ThirdFragment());
        }
        // // Tracks
        if (tabs_set.contains(getResources().getString(R.string.movie_world))) {
            mPagerAdapter.addFragment(new FourthFragment());
        }
        // // Playlists
        if (tabs_set.contains(getResources().getString(R.string.video))) {
            mPagerAdapter.addFragment(new FiveFragment());
        }
        // // Genres
        if (tabs_set.contains(getResources().getString(R.string.app_world))) {
            mPagerAdapter.addFragment(new SixFragment());
        }

        // Initiate ViewPager
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setPageMargin(getResources().getInteger(R.integer.viewpager_margin_width));
        mViewPager.setPageMarginDrawable(R.drawable.viewpager_margin);
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);

        // Tabs
        initScrollableTabs(mViewPager);
    }

    /**
     * Initiate the tabs
     */
    public void initScrollableTabs(ViewPager mViewPager) {
        ScrollableTabView mScrollingTabs = (ScrollableTabView) findViewById(R.id.scrollingTabs);
        ScrollingTabsAdapter mScrollingTabsAdapter = new ScrollingTabsAdapter(this);
        mScrollingTabs.setAdapter(mScrollingTabsAdapter);
        mScrollingTabs.setViewPager(mViewPager);
    }

}
