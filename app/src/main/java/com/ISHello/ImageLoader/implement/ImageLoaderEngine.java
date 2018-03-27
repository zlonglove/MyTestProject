package com.ISHello.ImageLoader.implement;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Created by zhanglong on 2017/4/20.
 * 核心下载引擎
 */
public class ImageLoaderEngine {
    final ImageLoaderConfiguration configuration;
    private Executor taskDistributor;
    private Executor taskExecutor;

    ImageLoaderEngine(ImageLoaderConfiguration configuration) {
        this.configuration = configuration;

        taskExecutor = configuration.taskExecutor;
        //taskExecutorForCachedImages = configuration.taskExecutorForCachedImages;

        taskDistributor = DefaultConfigurationFactory.createTaskDistributor();
    }

    /**
     * Submits task to execution pool
     */
    void submit(final Runnable task) {
        taskDistributor.execute(new Runnable() {
            @Override
            public void run() {
                //File image = configuration.diskCache.get(task.getLoadingUri()); 查看disk cache有没有
                //boolean isImageCachedOnDisk = image != null && image.exists();
                initExecutorsIfNeed();
                // if (isImageCachedOnDisk) {
                //    taskExecutorForCachedImages.execute(task);
                //} else {
                taskExecutor.execute(task);
                // }
            }
        });
    }

    private void initExecutorsIfNeed() {
        if (((ExecutorService) taskExecutor).isShutdown()) {
            taskExecutor = createTaskExecutor();
        }
    }

    private Executor createTaskExecutor() {
        return DefaultConfigurationFactory
                .createExecutor(configuration.threadPoolSize, configuration.threadPriority,
                        configuration.tasksProcessingType);
    }

    void fireCallback(Runnable r) {
        taskDistributor.execute(r);
    }
}
