package com.ISHello.MultipleThreadContinuableDownLoad;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.util.Log;

/**
 * 文件下载类
 *
 * @author zhanglong
 */
public class MTCDFileDownLoader {
    private final String TAG = "MTCDFileDownLoader";
    private Context context;
    private MultipleThreadContinuableDownLoadFileService fileService;
    private boolean exited;// 停止下载标志
    private int downloadSize = 0;// 已经下载文件长度
    private int fileSize = 0;// 原始文件的长度
    private MTCDDownloadThread[] threads;// 根据线程数设置下载线程池
    File saveFile;// 数据保存到的本地文件
    // 缓存个县城下载的长度
    private Map<Integer, Integer> date = new ConcurrentHashMap<Integer, Integer>();
    int block;// 每条线程下载的长度
    private String downloadUrl;// 下载的路径

    /**
     * 获取线程数量
     *
     * @return
     */
    public int getThreadSize() {
        return threads.length;
    }

    /**
     * 退出下载
     */
    public void exit() {
        this.exited = true;
    }

    public boolean getExited() {
        return this.exited;
    }

    /**
     * 获取文件的大小
     *
     * @return
     */
    public int getFileSize() {
        return fileSize;
    }

    /**
     * 累计已经下载大小 使用线程同步解决并发的问题
     *
     * @param size
     */
    protected synchronized void append(int size) {
        /**
         *
         */
        downloadSize += size;
    }

    /**
     * 更相信指定形成最后下载的位置
     *
     * @param threadid 线程的ID
     * @param pos      最后下载的位置
     */
    protected synchronized void update(int threadid, int pos) {
        /**
         * 把指定线程ID的线程赋予最新的下载长度，以前的值会被覆盖
         */
        this.date.put(threadid, pos);
        /**
         * 更新数据库中指定线程的下载长度
         */
        this.fileService.update(this.downloadUrl, threadid, pos);
    }

    /**
     * 构建文件下载器
     *
     * @param context
     * @param downloadUrl 下载路径
     * @param fileSaveDir 文件保存目录
     * @param threadNum   线程数目
     */
    public MTCDFileDownLoader(Context context, String downloadUrl,
                              File fileSaveDir, int threadNum) {
        try {
            this.context = context;
            this.downloadUrl = downloadUrl;
            fileService = new MultipleThreadContinuableDownLoadFileService(
                    this.context);
            URL url = new URL(this.downloadUrl);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdirs();
            }
            this.threads = new MTCDDownloadThread[threadNum];
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept",
                    "image/gif, image/jpeg, image/pjpeg, "
                            + "application/x-shockwave-flash, "
                            + "application/xaml+xml, "
                            + "application/vnd-ms-xpsdocument, "
                            + "application/x-ms-xbap, "
                            + "application/x-ms-application, "
                            + "application/vnd.ms-excel, "
                            + "application/vnd.ms-powerpoint, "
                            + "application/msword," + "*/*");
            connection.setRequestProperty("Accept", "zh-CN");
            connection.setRequestProperty("Referer", downloadUrl);
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.connect();
            printResponseHeader(connection);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                /**
                 * 获取文件的大小
                 */
                this.fileSize = connection.getContentLength();
                if (this.fileSize < 0) {
                    throw new RuntimeException("Unknow file size");
                }
                String filename = getFileName(connection);
                this.saveFile = new File(fileSaveDir, filename);
                Map<Integer, Integer> logdate = fileService
                        .getDate(downloadUrl);
                if (logdate.size() > 0) {
                    for (Map.Entry<Integer, Integer> entry : logdate.entrySet()) {
                        date.put(entry.getKey(), entry.getValue());
                    }
                }
                /**
                 * 如果已经下载的数据的线程数量和现在设置的线程的数量相同时则计算所偶线程已经下载的数据的总长度
                 */
                if (this.date.size() == this.threads.length) {
                    for (int i = 0; i < this.threads.length; i++) {
                        this.downloadSize += this.date.get(i + 1);
                    }
                    Log.i(TAG, "--->已经下载的长度" + this.downloadSize + "个字节");
                }
                this.block = (this.fileSize % this.threads.length == 0) ? this.fileSize
                        / this.threads.length
                        : this.fileSize / this.threads.length + 1;
            } else {
                Log.i(TAG, "--->服务器响应错误:" + connection.getResponseCode()
                        + connection.getResponseMessage());
                throw new RuntimeException("Server response error");
            }
        } catch (Exception e) {
            Log.i(TAG, e.toString());
            throw new RuntimeException("can not connection this url");
        }
    }

    /**
     * 获取文件的名字
     *
     * @param connection
     * @return
     */
    private String getFileName(HttpURLConnection connection) {
        String filename = this.downloadUrl.substring(this.downloadUrl
                .lastIndexOf('/') + 1);
        if (filename == null || "".equals(filename.trim())) {
            for (int i = 0; ; i++) {
                String mine = connection.getHeaderField(i);
                if (mine == null) {
                    break;
                }
                if ("content-disposition".equals(connection
                        .getHeaderFieldKey(i).toLowerCase())) {
                    Matcher m = Pattern.compile(".*filename=(.*)").matcher(
                            mine.toLowerCase());
                    if (m.find()) {
                        return m.group(1);
                    }
                }
                filename = UUID.randomUUID() + ".tmp";
            }
        }
        return filename;
    }

    /**
     * 获取Http响应头字段
     *
     * @param connection
     * @return
     */
    public Map<String, String> getHttpResponseHeader(
            HttpURLConnection connection) {
        Map<String, String> header = new LinkedHashMap<String, String>();
        for (int i = 0; ; i++) {
            String fieldValue = connection.getHeaderField(i);
            if (fieldValue == null) {
                break;
            }
            header.put(connection.getHeaderFieldKey(i), fieldValue);
        }
        return header;
    }

    /**
     * 打印http头字段
     *
     * @param connection
     */
    public void printResponseHeader(HttpURLConnection connection) {
        Map<String, String> header = getHttpResponseHeader(connection);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            String key = entry.getKey() != null ? entry.getKey() + ":" : "";
            Log.i(TAG, "--->" + key + " " + entry.getValue());
        }
    }
}
