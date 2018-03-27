package com.ISHello.MultithreadingDownloading;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class MultithreadingDownloading {

    int threadNumber = 0;
    final String TAG = "MultithreadingDownloading";
    RandomAccessFile file;
    DownLoadThread[] threads;
    DownloadProgressListener listener;

    public MultithreadingDownloading() {
        super();
    }

    /**
     * @param urlpath---网络的URL
     * @param fileName--下载到本地的文件名称
     * @throws IOException
     * @throws InterruptedException
     */
    public int downLoad(String urlpath, String savePath, int threadNumber, DownloadProgressListener listener) throws IOException, InterruptedException {
        if (urlpath == null || savePath == null || "".equals(urlpath) || "".equals(savePath)) {
            return -1;
        }
        if (listener == null) {
            Log.i(TAG, "--->You need DownloadProgressListener object,can not null!!!");
        }
        this.listener = listener;
        URL url = new URL(urlpath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setRequestMethod("GET");

        /**
         * 线程数
         */
        this.threadNumber = threadNumber;
        if (threadNumber <= 0) {
            return -1;
        }
        threads = new DownLoadThread[threadNumber];

        String filename = getFileName(url, connection);
        if (filename == null || "".equals(filename)) {
            return -1;
        }
        Log.i(TAG, "--->The File Name====" + filename);
        /**
         * 获取下载文件的长度
         */
        int fileLength = connection.getContentLength();
        if (fileLength <= 0) {
            return -1;
        }
        Log.i(TAG, "--->You downloading file size==" + fileLength);
        /**
         * 创建本地文件
         */
        file = new RandomAccessFile(savePath + filename, "rw");
        /**
         * 设置本地文件的长度
         */
        file.setLength(fileLength);
        file.close();

        /**
         * 每条线程下载的长度
         */
        int threadlength = fileLength % threadNumber == 0 ? fileLength / threadNumber : fileLength / threadNumber + 1;
        Log.i(TAG, "--->Every Thrad need downloading length===" + threadlength);

        for (int i = 0; i < threadNumber; i++) {
            /**
             * 计算每条线程应该从什么位置开始下载
             */
            int startPosition = i * threadlength;
            /**
             * 使用java中的RandomAccessFile对文件进行随机读写
             */
            file = new RandomAccessFile(savePath + filename, "rw");
            /**
             * 设置文件的什么位置写入
             */
            file.seek(startPosition);

            /**
             * 启动threadNumber条线程分别从startPosition指定的位置开始下载
             */
            threads[i] = new DownLoadThread(i, urlpath, startPosition, file, threadlength);
            threads[i].start();
        }
        int haveloaded = 0;
        while (haveloaded < fileLength) {
            for (int i = 0; i < threads.length; i++) {
                haveloaded += threads[i].getLoadedLen();
            }
            this.listener.onDownloadSize(haveloaded);
        }

        return 0;
    }


    private String getFileName(URL url, HttpURLConnection conn) {
        String filename = url.toString().substring(url.toString().lastIndexOf('/') + 1);
        if (filename == null || "".equals(filename.trim())) {
            for (int i = 0; ; i++) {
                String mine = conn.getHeaderField(i);
                if (mine == null) break;
                if ("content-disposition".equals(conn.getHeaderFieldKey(i).toLowerCase())) {
                    Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
                    if (m.find()) return m.group(1);
                }
            }
            filename = UUID.randomUUID() + ".tmp";//默认取一个文件名
        }
        return filename;
    }

}

