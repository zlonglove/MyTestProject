package com.ISHello.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanglong on 2017/5/5.
 */

public class CaptureUtils {
    private final String TAG = CaptureUtils.class.getSimpleName();
    private static CaptureUtils captureUtils;

    private CaptureUtils() {

    }

    /**
     * Returns singleton class instance
     */
    public static CaptureUtils getInstance() {
        if (captureUtils == null) {
            synchronized (CaptureUtils.class) {
                if (captureUtils == null) {
                    captureUtils = new CaptureUtils();
                }
            }
        }
        return captureUtils;
    }

    /**
     * 获取当前Window的DrawingCache
     *
     * @param activity
     * @return
     */
    public Bitmap shotActivity(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 获取当前View的DrawingCache
     *
     * @param view
     * @return
     */
    public Bitmap shotViewBitmap(View view) {
        if (null == view) {
            return null;
        }
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        if (Build.VERSION.SDK_INT >= 11) {
            view.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(view.getHeight(), View.MeasureSpec.EXACTLY));
            view.layout((int) view.getX(), (int) view.getY(), (int) view.getX() + view.getMeasuredWidth(), (int) view.getY() + view.getMeasuredHeight());
        } else {
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 获取当前ScrollView的DrawingCache
     *
     * @param scrollView
     * @return
     */
    public Bitmap shotScrollView(ScrollView scrollView) {
        if (scrollView == null) {
            return null;
        }
        int h = 0;
        Bitmap bitmap;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
        }
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h, Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    /**
     * 获取当前ListView的DrawingCache
     *
     * @param listView
     * @return
     */
    public Bitmap shotListView(ListView listView) {
        if (null == listView) {
            return null;
        }
        ListAdapter adapter = listView.getAdapter();
        int itemsCount = adapter.getCount();
        int allItemsHeight = 0;
        List<Bitmap> bmps = new ArrayList<>();
        for (int i = 0; i < itemsCount; i++) {
            View childView = adapter.getView(i, null, listView);
            childView.measure(View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
            childView.setDrawingCacheEnabled(true);
            childView.buildDrawingCache();
            bmps.add(childView.getDrawingCache());
            allItemsHeight += childView.getMeasuredHeight();
        }
        Bitmap bigBitmap = Bitmap.createBitmap(listView.getMeasuredWidth(), allItemsHeight, Bitmap.Config.RGB_565);
        Canvas bigCanvas = new Canvas(bigBitmap);

        Paint paint = new Paint();
        int iHeight = 0;
        for (int i = 0; i < bmps.size(); i++) {
            Bitmap bmp = bmps.get(i);
            bigCanvas.drawBitmap(bmp, 0, iHeight, paint);
            iHeight += bmp.getHeight();
            bmp.recycle();
            bmp = null;
        }
        return bigBitmap;
    }

}
