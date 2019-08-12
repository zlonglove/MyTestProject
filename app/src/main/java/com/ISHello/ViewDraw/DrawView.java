package com.ISHello.ViewDraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by zhanglong on 2017/3/28.
 */

public class DrawView extends View {
    private final String TAG = "DrawView";
    private Paint textPaint = new Paint();          // 创建画笔
    private String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private int width = 0;
    private float totalWidth = 0;

    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        textPaint.setColor(Color.BLACK);        // 设置颜色
        textPaint.setStyle(Paint.Style.FILL);   // 设置样式
        textPaint.setTextSize(50);              // 设置字体大小
        totalWidth = textPaint.measureText(str);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, "--->onMeasure " + widthMeasureSpec + "/" + heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.i(TAG, "--->onLayout" + left + "/" + top + "/" + right + "/" + bottom);
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "--->onDraw");
        super.onDraw(canvas);
        textPaint.setColor(Color.BLACK);        // 设置颜色
        // 参数分别为 (字符串 开始截取位置 结束截取位置 基线x 基线y 画笔)
        canvas.drawText(str, 0, str.length(), 0, 50, textPaint);

        textPaint.setColor(Color.RED);        // 设置颜色
        canvas.clipRect(0, 0, width, 50);
        canvas.drawText(str, 0, str.length(), 0, 50, textPaint);
        width += 2;

        invalidate();
    }
}
