package com.ISHello.ImageLoader.implement;

import android.widget.ImageView;

/**
 * Created by zhanglong on 2017/4/21.
 */

final class ImageLoadingInfo {

    final String uri;
    final ImageView imageAware;
    final ImageLoadingListener listener;

    public ImageLoadingInfo(String uri, ImageView imageAware, ImageLoadingListener listener) {
        this.uri = uri;
        this.imageAware = imageAware;
        this.listener = listener;
    }
}
