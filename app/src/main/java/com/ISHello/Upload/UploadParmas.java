package com.ISHello.Upload;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by zhanglong on 2017/5/8.
 */

public class UploadParmas implements Serializable {
    public static final int defaultReadTimeOut = 10000;
    public static final int defaultConnectTimeOut = 10000;

    /**
     * 服务器响应超时
     */
    public int readTimeOut;
    /**
     * 服务器请求超时
     */
    public int connectTimeOut;

    /**
     * 请求服务器url
     */
    public String url;
    /**
     * 请求头参数
     */
    public Map<String, String> headerMap;
    /**
     * 请求体参数列表
     */
    public Map<String, String> paraMap;
    /**
     * 文件路径集合
     */
    public String[] filePaths;

    public int getReadTimeOut() {
        if (readTimeOut <= 0) {
            readTimeOut = defaultReadTimeOut;
        }
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public int getConnectTimeOut() {
        if (connectTimeOut <= 0) {
            connectTimeOut = defaultConnectTimeOut;
        }
        return connectTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public Map<String, String> getParaMap() {
        return paraMap;
    }

    public void setParaMap(Map<String, String> paraMap) {
        this.paraMap = paraMap;
    }

    public String[] getFilePaths() {
        return filePaths;
    }

    public void setFilePaths(String[] filePaths) {
        this.filePaths = filePaths;
    }
}
