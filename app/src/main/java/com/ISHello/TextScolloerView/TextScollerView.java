package com.ISHello.TextScolloerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.example.ishelloword.R;

public class TextScollerView extends SurfaceView implements Callback, Runnable {

    private final String TAG = "TextScollerView";
    private SurfaceHolder holder;
    private Thread thread;
    private Paint paint;
    private Canvas canvas;
    private int screenW, screenH;
    private boolean flag = false;
    private Rect bgRect;
    Surface surface;
    private final String text = "张龙的广告张龙的广告张龙的广告张龙的广告";

    private float lineWidth = 20;
    private float textLength = 0f;//文本长度
    private float temp_view_plus_text_length = 0.0f;//用于计算的临时变量
    private float temp_view_plus_two_text_length = 0.0f;//用于计算的临时变量
    private float step = 0f;//文字的横坐标
    private float y = 0f;//文字的纵坐标
    private float fontSize;

    public TextScollerView(Context context) {
        super(context);
    }


    public TextScollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
        fontSize = context.getResources().getDimension(R.dimen.lyricSize);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(255, 207, 60, 11));
        paint.setTextSize(fontSize);
        paint.setStyle(Style.STROKE);
        paint.setTextAlign(Paint.Align.LEFT);
        //this.setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSLUCENT);
        //holder.setType(SurfaceHolder.SURFACE_TYPE_HARDWARE);
        //this.setLayerType(View.L, null);

        textLength = paint.measureText(text);
        Log.i(TAG, "--->TextScollerView GouZao");
    }

    public void DrawText() {
        canvas = holder.lockCanvas(bgRect);
        synchronized (holder) {
            ClearText(canvas);
            if (canvas != null) {
                paint.setStrokeWidth(lineWidth);
                canvas.drawRect(bgRect, paint);
                paint.setStrokeWidth(0f);
                canvas.drawText(text, temp_view_plus_text_length - step, y, paint);
            }
        }
        if (holder == null || canvas == null) {
            return;
        }
        holder.unlockCanvasAndPost(canvas);
        step += 6.0;
        if (step > temp_view_plus_two_text_length) {
            step = textLength;
        }
    }

    public void startDraw() {
        if (flag) {
            return;
        }
        flag = true;
        thread = new Thread(this);
        thread.start();
        surface = holder.getSurface();
    }


    @SuppressWarnings("deprecation")
    public void stopDraw() {
        flag = false;
        if (thread != null && thread.isAlive()) {
            thread.stop();
        }
    }

    public void ClearText(Canvas canvas) {
        if (canvas != null) {
            canvas.drawColor(Color.argb(150, 0, 0, 0), Mode.CLEAR);
        }
    }

    @Override
    public void run() {
        while (flag) {
            long start = System.currentTimeMillis();
            DrawText();
            long end = System.currentTimeMillis();
            try {
                //Log.i(TAG, "--->Draw Text Frame is=="+(end - start)+"Ms");
                if (end - start < 1)
                    Thread.sleep(5 - (end - start));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("DrawText", "--->surfaceChanged width==" + width + " height==" + height);
        screenH = height;
        screenW = width;
        y = fontSize + ((screenH - fontSize - lineWidth) / 2);
        step = textLength;
        temp_view_plus_text_length = screenW + textLength;
        temp_view_plus_two_text_length = screenW + textLength * 2;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("DrawText", "--->surfaceCreated");
        bgRect = new Rect(0, 0, screenW, screenH);
        startDraw();
        //holder.setType(SurfaceHolder.SURFACE_TYPE_HARDWARE);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;
        try {
            if (thread != null) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
