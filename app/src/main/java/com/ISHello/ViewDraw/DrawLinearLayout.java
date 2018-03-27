package com.ISHello.ViewDraw;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Created by zhanglong on 2017/3/28.
 */

public class DrawLinearLayout extends LinearLayout {
    private final String TAG = "DrawRelativeLayout";

    public DrawLinearLayout(Context context) {
        super(context);
    }

    public DrawLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, "--->onMeasure " + widthMeasureSpec + "/" + heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i(TAG, "--->onLayout" + l + "/" + t + "/" + r + "/" + b);
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "--->onDraw");
        super.onDraw(canvas);
    }
}
