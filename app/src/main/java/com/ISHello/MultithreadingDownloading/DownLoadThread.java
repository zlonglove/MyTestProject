package com.ISHello.MultithreadingDownloading;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

/**
 * 从网络下载文件的线程
 *
 * @author Administrator
 */
public class DownLoadThread extends Thread {
    private final String TAG = "DownLoadThread";
    /**
     * 下载线程ID
     */
    private int threadid;
    /**
     * 开始下载的位置
     */
    private int startPosition;
    /**
     * 下载到的本地文件
     */
    private RandomAccessFile threadfile;
    /**
     * 每个线程需要下载的长度
     */
    private int threadlength;
    /**
     * 网络文件的URL
     */
    private String path;

    private URL url;
    private int haveloadedLen = 0;


    public DownLoadThread(int threadid, String path, int startposition, RandomAccessFile threadfile, int threadlength) {
        this.threadid = threadid;
        this.startPosition = startposition;
        this.threadfile = threadfile;
        this.threadlength = threadlength;
        this.path = path;
    }

    public synchronized int getLoadedLen() {
        return haveloadedLen;
    }


    public void run() {
        try {
            Log.i(TAG, "---> The " + threadid + " is downing");
            url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Accept-Language", "zh-CN");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Range", "bytes=" + startPosition + "-");

            InputStream inputStream = connection.getInputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            haveloadedLen = 0;
            while (haveloadedLen < threadlength && (len = inputStream.read(buffer)) != -1) {
                threadfile.write(buffer, 0, len);
                haveloadedLen += len;
            }
            threadfile.close();
            inputStream.close();
            Log.i(TAG, "--->线程" + (threadid + 1) + "已经下载完成");
        } catch (Exception e) {
            Log.i(TAG, "线程--->" + (threadid + 1) + "已经下载出错" + e.getMessage());
        }
    }

}
