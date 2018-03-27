package com.ISHello.ImageLoader.implement;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhanglong on 2017/4/21.
 */

public class LoadAndDisplayImageTask implements Runnable {
    private final ImageLoaderEngine engine;
    private final ImageLoadingInfo imageLoadingInfo;
    private final Handler handler;

    private final ImageLoaderConfiguration configuration;
    private final ImageDownloader downloader;

    public LoadAndDisplayImageTask(ImageLoaderEngine engine, ImageLoadingInfo imageLoadingInfo, Handler handler) {
        this.engine = engine;
        this.imageLoadingInfo = imageLoadingInfo;
        this.handler = handler;

        this.configuration = engine.configuration;
        downloader = configuration.downloader;
    }

    @Override
    public void run() {
        Bitmap bitmap;
        try {
            bitmap = downLoadImage();
            if (bitmap != null) {
                imageLoadingInfo.listener.onLoadingComplete(imageLoadingInfo.uri, imageLoadingInfo.imageAware, bitmap);
                DisplayBitmapTask displayBitmapTask = new DisplayBitmapTask(bitmap, imageLoadingInfo, engine);
                runTask(displayBitmapTask, this.configuration.isSyncLoading, handler, engine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap downLoadImage() throws IOException {
        Bitmap decodedBitmap;
        InputStream imageStream = getImageStream();
        if (imageStream == null) {
            Log.e("zhanglong", "ERROR_NO_IMAGE_STREAM");
            return null;
        }
        //BitmapFactory.Options options = defineImageSizeAndRotation(imageStream);
        //resetStream(imageStream);
        //BitmapFactory.Options decodingOptions=prepareDecodingOptions();
        decodedBitmap = BitmapFactory.decodeStream(imageStream, null, null);
        return decodedBitmap;
    }

    protected InputStream getImageStream() throws IOException {
        return downloader.getStream(imageLoadingInfo.uri, null);
    }

    protected InputStream resetStream(InputStream imageStream) throws IOException {
        if (imageStream.markSupported()) {
            try {
                imageStream.reset();
                return imageStream;
            } catch (IOException ignored) {
            }
        }
        IoUtils.closeSilently(imageStream);
        return getImageStream();
    }
   /* protected BitmapFactory.Options prepareDecodingOptions(ImageSize imageSize, ImageDecodingInfo decodingInfo){

    }*/

    void runTask(Runnable r, boolean sync, Handler handler, ImageLoaderEngine engine) {
        if (sync) {
            r.run();
        } else if (handler == null) {
            engine.fireCallback(r);
        } else {
            handler.post(r);
        }
    }

    BitmapFactory.Options defineImageSizeAndRotation(InputStream imageStream) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(imageStream, null, options);

        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        Log.i("zhanglong", "--->pic w/h==" + outWidth + "/" + outHeight);
        options.inJustDecodeBounds = false;
        return options;
    }
}
