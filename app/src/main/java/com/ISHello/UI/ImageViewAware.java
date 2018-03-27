package com.ISHello.UI;

import java.lang.reflect.Field;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class ImageViewAware extends ViewAware {
    private static final String TAG = "ImageViewAware";

    /**
     * Constructor. <br />
     * References {@link #ImageViewAware(android.widget.ImageView, boolean)
     * ImageViewAware(imageView, true)}.
     *
     * @param imageView {@link android.widget.ImageView ImageView} to work with
     */
    public ImageViewAware(ImageView imageView) {
        super(imageView);
    }

    /**
     * Constructor
     *
     * @param imageView           {@link android.widget.ImageView ImageView} to work with
     * @param checkActualViewSize <b>true</b> - then {@link #getWidth()} and
     *                            {@link #getHeight()} will check actual size of ImageView. It
     *                            can cause known issues like. But it helps to save memory because memory cache
     *                            keeps bitmaps of actual (less in general) size.
     *                            <p/>
     *                            <b>false</b> - then {@link #getWidth()} and
     *                            {@link #getHeight()} will <b>NOT</b> consider actual size of
     *                            ImageView, just layout parameters. <br />
     *                            If you set 'false' it's recommended 'android:layout_width' and
     *                            'android:layout_height' (or 'android:maxWidth' and
     *                            'android:maxHeight') are set with concrete values. It helps to
     *                            save memory.
     *                            <p/>
     */
    public ImageViewAware(ImageView imageView, boolean checkActualViewSize) {
        super(imageView, checkActualViewSize);
    }

    /**
     * {@inheritDoc} <br />
     * 3) Get <b>maxWidth</b>.
     */
    @Override
    public int getWidth() {
        int width = super.getWidth();
        if (width <= 0) {
            ImageView imageView = (ImageView) viewRef.get();
            if (imageView != null) {
                width = getImageViewFieldValue(imageView, "mMaxWidth"); // Check
                // maxWidth
                // parameter
            }
        }
        return width;
    }

    /**
     * {@inheritDoc} <br />
     * 3) Get <b>maxHeight</b>
     */
    @Override
    public int getHeight() {
        int height = super.getHeight();
        if (height <= 0) {
            ImageView imageView = (ImageView) viewRef.get();
            if (imageView != null) {
                height = getImageViewFieldValue(imageView, "mMaxHeight"); // Check
                // maxHeight
                // parameter
            }
        }
        return height;
    }

    @Override
    public ViewScaleType getScaleType() {
        ImageView imageView = (ImageView) viewRef.get();
        if (imageView != null) {
            return ViewScaleType.fromImageView(imageView);
        }
        return super.getScaleType();
    }

    @Override
    public ImageView getWrappedView() {
        return (ImageView) super.getWrappedView();
    }

    @Override
    protected void setImageDrawableInto(Drawable drawable, View view) {
        ((ImageView) view).setImageDrawable(drawable);
        if (drawable instanceof AnimationDrawable) {
            ((AnimationDrawable) drawable).start();
        }
    }

    @Override
    protected void setImageBitmapInto(Bitmap bitmap, View view) {
        ((ImageView) view).setImageBitmap(bitmap);
    }

    private static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = (Integer) field.get(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return value;
    }
}
