package com.ISHello.ImageLoader.implement;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by zhanglong on 2017/4/21.
 */

public class DisplayBitmapTask implements Runnable {
    private final Bitmap bitmap;
    private final String imageUri;
    private final ImageView imageAware;

    private final ImageLoadingListener listener;
    private final ImageLoaderEngine engine;

    public DisplayBitmapTask(Bitmap bitmap, ImageLoadingInfo imageLoadingInfo, ImageLoaderEngine engine) {
        this.bitmap = bitmap;
        imageUri = imageLoadingInfo.uri;
        imageAware = imageLoadingInfo.imageAware;
        listener = imageLoadingInfo.listener;
        this.engine = engine;
    }

    @Override
    public void run() {
        if (isCollected()) {
            listener.onLoadingCancelled(imageUri, imageAware);
        } else {
            imageAware.setImageBitmap(bitmap);
        }
    }

    public boolean isCollected() {
        // return imageAware.get() == null;
        return false;
    }
}
