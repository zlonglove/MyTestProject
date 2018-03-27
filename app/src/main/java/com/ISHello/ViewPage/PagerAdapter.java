package com.ISHello.ViewPage;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * @author zhanglong
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private final String TAG = "PagerAdapter";

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment) {
        Log.i(TAG, "--->addFragment " + fragment.getId());
        mFragments.add(fragment);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Log.i(TAG, "--->getItem()" + position);
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        Log.i(TAG, "--->getCount()" + mFragments.size());
        return mFragments.size();
    }

    /**
     * This method update the fragments that extends the {@link RefreshableFragment} class
     */
    /*
    public void refresh() 
    {
        for (int i = 0; i < mFragments.size(); i++) 
        {
            if(mFragments.get(i) instanceof RefreshableFragment) 
            {
                ((RefreshableFragment)mFragments.get(i)).refresh();
            }
        }
    }
    */

}
