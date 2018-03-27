package com.youth.banner.loader;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

/**
 * @param <T>
 * @author
 */
public interface ImageLoaderInterface<T extends View> extends Serializable {
    /**
     * ImageView 显示
     *
     * @param context
     * @param path
     * @param imageView
     */
    void displayImage(Context context, Object path, T imageView);

    /**
     * 创建ImageView
     *
     * @param context
     * @return
     */
    T createImageView(Context context);
}
