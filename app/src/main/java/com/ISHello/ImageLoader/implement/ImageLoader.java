package com.ISHello.ImageLoader.implement;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by zhanglong on 2017/4/20.
 */

public class ImageLoader {
    public static final String TAG = ImageLoader.class.getSimpleName();
    static final String LOG_INIT_CONFIG = "Initialize ImageLoader with configuration";

    private static final String WARNING_RE_INIT_CONFIG = "Try to initialize ImageLoader which had already been initialized before. " + "To re-init ImageLoader with new configuration call ImageLoader.destroy() at first.";
    private static final String ERROR_WRONG_ARGUMENTS = "Wrong arguments were passed to displayImage() method (ImageView reference must not be null)";
    private static final String ERROR_NOT_INIT = "ImageLoader must be init with configuration before using";
    private static final String ERROR_INIT_CONFIG_WITH_NULL = "ImageLoader configuration can not be initialized with null";


    private ImageLoaderConfiguration configuration;
    private ImageLoaderEngine engine;

    private volatile static ImageLoader instance;

    private ImageLoadingListener defaultListener = new SimpleImageLoadingListener();

    /**
     * Returns singleton class instance
     */
    public static ImageLoader getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }

    protected ImageLoader() {
    }


    public synchronized void init(ImageLoaderConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException(ERROR_INIT_CONFIG_WITH_NULL);
        }
        if (this.configuration == null) {
            Log.d(TAG, LOG_INIT_CONFIG);
            engine = new ImageLoaderEngine(configuration);
            this.configuration = configuration;
        } else {
            Log.d(TAG, WARNING_RE_INIT_CONFIG);
        }
    }

    public void displayImage(String url, ImageView imageAware) {
        displayImage(url, imageAware, null);
    }

    public void displayImage(String url, ImageView imageAware, ImageLoadingListener loadingListener) {
        checkConfiguration();
        if (imageAware == null) {
            throw new IllegalArgumentException(ERROR_WRONG_ARGUMENTS);
        }
        if (loadingListener == null) {
            loadingListener = defaultListener;
        }
        if (TextUtils.isEmpty(url)) {
            loadingListener.onLoadingStarted(url, imageAware);
            /**
             * deal
             */
            loadingListener.onLoadingComplete(url, imageAware, null);
            return;
        }

        loadingListener.onLoadingStarted(url, imageAware);
        ImageLoadingInfo imageLoadingInfo = new ImageLoadingInfo(url, imageAware, loadingListener);
        LoadAndDisplayImageTask loadAndDisplayImageTask = new LoadAndDisplayImageTask(engine, imageLoadingInfo, defineHandler());
        if (configuration.isSyncLoading) {
            loadAndDisplayImageTask.run();
        } else {
            engine.submit(loadAndDisplayImageTask);
        }

    }
    /*
    protected InputStream getImageStream(String url) throws IOException {
        return configuration.downloader.getStream(url, null);
    }*/

    /**
     * Checks if ImageLoader's configuration was initialized
     *
     * @throws IllegalStateException if configuration wasn't initialized
     */
    private void checkConfiguration() {
        if (configuration == null) {
            throw new IllegalStateException(ERROR_NOT_INIT);
        }
    }

    private Handler defineHandler() {
        Handler handler = null;
        if (configuration.isSyncLoading) {
            handler = null;
        } else if (handler == null && Looper.myLooper() == Looper.getMainLooper()) {
            handler = new Handler();
        }
        return handler;
    }
}
