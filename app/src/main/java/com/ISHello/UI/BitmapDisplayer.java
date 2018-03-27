package com.ISHello.UI;

import android.graphics.Bitmap;

public interface BitmapDisplayer {
    /**
     * Displays bitmap in <b>NOTE:</b> This method is called on UI thread so
     * it's strongly recommended not to do any heavy work in it.
     *
     * @param bitmap     Source bitmap
     * @param imageAware to display Bitmap
     * @param loadedFrom Source of loaded image
     */
    void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom);

    public static enum LoadedFrom {
        NETWORK, DISC_CACHE, MEMORY_CACHE
    }
}
