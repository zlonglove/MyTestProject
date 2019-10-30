package com.ISHello.TouchEvent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

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
        Log.i(TAG, "--->【Group】下达任务：" + Util.actionToString(ev.getAction()) +
                "，找个人帮我完成，任务往下分发。");
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
        boolean relust = false;
        Log.i(TAG, "--->【Group】是否拦截任务：" + Util.actionToString(ev.getAction()) + "，拦下来？" + relust);
        return relust;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean relust = super.onTouchEvent(event);
        Log.i(TAG, "--->【Group】完成任务：" + Util.actionToString(event.getAction()) +
                "，【员工】太差劲了，以后不再找你干活了，我自来搞定！是否解决：" + Util.canDoTask(relust));
        return relust;
    }
}
