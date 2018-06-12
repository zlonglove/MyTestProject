package com.ISHello.TouchEvent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.ISHello.utils.LogUtil;

/**
 * Created by zhanglong on 2017/3/27.
 */

public class MyRelativeLayout extends RelativeLayout {
    private final String TAG = "MyRelativeLayout";

    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 分发
     * 返回值为true-事件被当前视图消费，不再继续分发。
     * 返回super.dispatchTouchEvent(event)-表示继续分发改事件，如果当前视图是ViewGroup及其子类，则会调用onInterceptTouchEvent方法
     * 判断是否拦截该事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.log(TAG, "--->dispatchTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                //LogUtil.log((TAG, "--->dispatchTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                LogUtil.log(TAG, "--->dispatchTouchEvent ACTION_UP");
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 对事件进行拦截，true-事件不会传递给子View
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.log(TAG, "--->onInterceptTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                // LogUtil.log(TAG, "--->onInterceptTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                LogUtil.log(TAG, "--->onInterceptTouchEvent ACTION_UP");
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
        //return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.log(TAG, "--->onTouchEvent ACTION_DOWN");
                //getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                // LogUtil.log(TAG, "--->onTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                LogUtil.log(TAG, "--->onTouchEvent ACTION_UP");
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
