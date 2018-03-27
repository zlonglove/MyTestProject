package com.ISHello.Manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;//�ٷ��ṩ��һ��������

/**
 * @author kfzx-zhanglong
 * @note newFixedThreadPool()返回一个固定线程数量的线程池，多余的任务会保存在一个任务队列中，
 * 等有线程空闲的时候按照FIFO方式顺序执行任务队列中的任务
 * @note newCachedThreadPool()根据实际情况调整线程池的线程数量的线程池
 * @note newSingleThreadPool()返回只有一个线程的线程池, 多余的任务会保存在一个任务队列中，
 * 等有线程空闲的时候按照FIFO方式顺序执行任务队列中的任务
 * @note newScheduledThradPool()返回一个可以控制线程池定时或者周期性执行任务的线程池
 * @note newSingleThreadScheduledExcutor()返回一个可以控制线程池定时或者周期性执行任务的线程池, 该线程池的大小为1
 */

public class ThreadPoolManager {
    private ExecutorService service;

    private ThreadPoolManager() {
        int num = Runtime.getRuntime().availableProcessors();
        service = Executors.newFixedThreadPool(num * 2);
    }

    private static ThreadPoolManager manager;

    public static ThreadPoolManager getInstance() {
        if (manager == null) {
            manager = new ThreadPoolManager();
        }
        return manager;
    }

    public void addTask(Runnable runnable) {
        service.submit(runnable);
    }

}
