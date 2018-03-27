package com.ISHello.Service;

import com.ISHello.utils.LogUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zhanglong on 2017/5/18.
 */

public class FileDownloadThread extends Thread {
    private static final String TAG = FileDownloadThread.class.getSimpleName();

    /**
     * 当前下载是否完成
     */
    private boolean isCompleted = false;
    /**
     * 当前下载文件长度
     */
    private long downloadLength = 0;
    /**
     * 文件保存路径
     */
    private File file;
    /**
     * 文件下载路径
     */
    private String downloadUrl;
    /**
     * 当前下载线程ID
     */
    private int threadId;
    /**
     * 线程下载数据长度
     */
    private int blockSize;

    /**
     * @param downloadUrl:文件下载地址
     * @param file:文件保存路径
     * @param blocksize:下载数据长度
     * @param threadId:线程ID
     */
    public FileDownloadThread(String downloadUrl, File file, int blocksize,
                              int threadId) {
        this.downloadUrl = downloadUrl;
        this.file = file;
        this.threadId = threadId;
        this.blockSize = blocksize;
    }

    @Override
    public void run() {

        InputStream bis = null;
        RandomAccessFile accessFile = null;

        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setAllowUserInteraction(true);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            //conn.setRequestProperty("Accept-Encoding", "identity");

            int startPos = blockSize * (threadId - 1);//开始位置
            int endPos = blockSize * threadId - 1;//结束位置

            //指定下载的位置
            String range = "bytes=" + startPos + "-" + endPos;
            conn.setRequestProperty("Range", range);
            LogUtil.log(TAG, "--->" + Thread.currentThread().getName() + "Range is[" + range + "]");

            byte[] buffer = new byte[1024];
            bis = conn.getInputStream();
            //创建RandomAccessFile对象
            accessFile = new RandomAccessFile(file, "rwd");
            //跳转到开始位置
            accessFile.seek(startPos);
            int len;
            while ((len = bis.read(buffer)) != -1) {
                accessFile.write(buffer, 0, len);
                downloadLength += len;
            }
            isCompleted = true;
            LogUtil.log(TAG, "--->current thread[ " + threadId + " ] task has finished,all size:" + downloadLength);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (accessFile != null) {
                try {
                    accessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 线程文件是否下载完毕
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * 线程下载文件长度
     */
    public long getDownloadLength() {
        return downloadLength;
    }
}
