package com.ISHello.ImageLoader.implement;

import android.content.Context;

import java.util.concurrent.Executor;

/**
 * Created by zhanglong on 2017/4/20.
 */

public final class ImageLoaderConfiguration {
    /**
     * {@value}
     */
    public static final int DEFAULT_THREAD_POOL_SIZE = 3;
    /**
     * {@value}
     */
    public static final int DEFAULT_THREAD_PRIORITY = Thread.NORM_PRIORITY - 2;
    /**
     * {@value}
     */
    public static final QueueProcessingType DEFAULT_TASK_PROCESSING_TYPE = QueueProcessingType.FIFO;

    final Executor taskExecutor;
    final ImageDownloader downloader;
    final long diskCacheSize;

    final int threadPoolSize;
    final int threadPriority;
    final QueueProcessingType tasksProcessingType;

    final boolean isSyncLoading;

    private ImageLoaderConfiguration(final Builder builder) {
        downloader = builder.downloader;
        diskCacheSize = builder.diskCacheSize;
        taskExecutor = builder.taskExecutor;

        threadPoolSize = builder.threadPoolSize;
        threadPriority = builder.threadPriority;
        tasksProcessingType = builder.tasksProcessingType;
        isSyncLoading = builder.isSyncLoading;
    }

    public static class Builder {
        private Context context;

        private Executor taskExecutor = null;
        private ImageDownloader downloader = null;
        private long diskCacheSize = 0;

        private int threadPoolSize = DEFAULT_THREAD_POOL_SIZE;
        private int threadPriority = DEFAULT_THREAD_PRIORITY;
        private QueueProcessingType tasksProcessingType = DEFAULT_TASK_PROCESSING_TYPE;

        boolean isSyncLoading = false;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        public ImageLoaderConfiguration build() {//用build方法构建需要的ImageLoaderConfiguration对象
            initEmptyFieldsWithDefaultValues();
            return new ImageLoaderConfiguration(this);
        }

        private void initEmptyFieldsWithDefaultValues() {
            if (taskExecutor == null) {
                taskExecutor = DefaultConfigurationFactory
                        .createExecutor(threadPoolSize, threadPriority, tasksProcessingType);
            }
            if (downloader == null) {
                downloader = DefaultConfigurationFactory.createImageDownloader(context);
            }
            if (diskCacheSize >= 0) {
                diskCacheSize = DefaultConfigurationFactory.createDiskCacheSize();
            }
        }

        public Builder imageDownloader(ImageDownloader imageDownloader) {
            this.downloader = imageDownloader;
            return this;
        }

        public Builder diskCacheSize(int memoryCacheSize) {
            if (memoryCacheSize <= 0)
                throw new IllegalArgumentException("memoryCacheSize must be a positive number");

            this.diskCacheSize = memoryCacheSize;
            return this;
        }

        public Builder taskExecutor(Executor executor) {
            if (threadPoolSize != DEFAULT_THREAD_POOL_SIZE || threadPriority != DEFAULT_THREAD_PRIORITY || tasksProcessingType != DEFAULT_TASK_PROCESSING_TYPE) {
                //L.w(WARNING_OVERLAP_EXECUTOR);
            }

            this.taskExecutor = executor;
            return this;
        }

        public Builder isSyncLoading(boolean isSyncLoading) {
            this.isSyncLoading = isSyncLoading;
            return this;
        }
    }
}
