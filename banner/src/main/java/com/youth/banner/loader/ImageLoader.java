package com.youth.banner.loader;

import android.content.Context;
import android.widget.ImageView;

/**
 * @author
 */
public abstract class ImageLoader implements ImageLoaderInterface<ImageView> {

    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        return imageView;
    }

}
