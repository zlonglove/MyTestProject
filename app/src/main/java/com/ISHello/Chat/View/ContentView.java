package com.ISHello.Chat.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.ISHello.utils.LogUtil;

public class ContentView extends RelativeLayout {
    private int range = 0;

    public ContentView(Context context) {
        this(context, null);
    }

    public ContentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setRange(int range) {
        this.range = range;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getRawX() >= range && getLeft() >= range) {
            LogUtil.log("中断");
        } else {
            LogUtil.log("不中断");
        }
        return ev.getRawX() >= range && getLeft() >= range;
    }
}
