package com.ISHello.TouchEvent;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class MyButton extends AppCompatButton {
    private static final String TAG = MyButton.class.getSimpleName();

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG, "--->【Button】下达任务：" + Util.actionToString(event.getAction()));
        boolean result = super.dispatchTouchEvent(event);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //boolean relust = false;
        boolean relust = super.onTouchEvent(event);
        Log.i(TAG, "--->【Button】完成任务：" + Util.actionToString(event.getAction()) +
                "，【Button】现在只能靠自己了！是否解决：" + Util.canDoTask(relust));
        return relust;
    }
}
