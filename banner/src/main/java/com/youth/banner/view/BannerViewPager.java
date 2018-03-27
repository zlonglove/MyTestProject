package com.youth.banner.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @author
 */
public class BannerViewPager extends ViewPager {
    private final String TAG = BannerViewPager.class.getSimpleName();
    private boolean scrollable = true;

    public BannerViewPager(Context context) {
        super(context);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.scrollable) {
            if (getCurrentItem() == 0 && getChildCount() == 0) {
                return false;
            }
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.scrollable) {
            if (getCurrentItem() == 0 && getChildCount() == 0) {
                Log.i(TAG, "--->onInterceptTouchEvent getCurrentItem==0 return false");
                return false;
            }
            Log.i(TAG, "--->onInterceptTouchEvent scrollable true,super.onInterceptTouchEvent(ev)");
            return super.onInterceptTouchEvent(ev);
        } else {
            Log.i(TAG, "--->onInterceptTouchEvent scrollable false,return false");
            return false;
        }
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }
}
