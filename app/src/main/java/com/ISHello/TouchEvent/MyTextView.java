package com.ISHello.TouchEvent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by zhanglong on 2017/3/27.
 */

public class MyTextView extends TextView {
    private final String TAG = "MyTextView";

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 分发
     * 返回值为true-事件被当前视图消费，不再继续分发。
     * 返回super.dispatchTouchEvent(event)-表示继续分发改事件，如果当前视图是ViewGroup及其子类，则会调用onInterceptTouchEvent方法
     * 判断是否拦截该事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Log.i(TAG, "--->dispatchTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.i(TAG, "--->dispatchTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "--->dispatchTouchEvent ACTION_UP");
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * MyTextView的onTOuchEvent默认返回true，消耗了touch事件，MyRelativeLayout中的onTouchEvent将不会被调用。
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Log.i(TAG, "--->onTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.i(TAG, "--->onTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "--->onTouchEvent ACTION_UP");
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
