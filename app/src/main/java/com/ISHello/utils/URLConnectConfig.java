/**
 *
 */
package com.ISHello.utils;

import java.util.Map;

public class URLConnectConfig {

    public static final boolean defaultDoInput = true;
    public static final boolean defaultDoOutput = true;
    public static final int defaultReadTimeOut = 30000;
    public static final int defaultConnectTimeOut = 30000;

    public boolean doInput;
    public boolean doOutput;
    public int readTimeOut;// 服务器响应超时
    public int connectTimeOut;// 服务器请求超时
    public Map<String, String> headerMap;// 头

    public URLConnectConfig() {
        this(defaultReadTimeOut, defaultConnectTimeOut, null);
    }

    public URLConnectConfig(Map<String, String> headerMap) {
        this(defaultReadTimeOut, defaultConnectTimeOut, headerMap);
    }

    public URLConnectConfig(int readTimeOut, int connectTimeOut, Map<String, String> headerMap) {
        doInput = defaultDoInput;
        doOutput = defaultDoOutput;
        this.readTimeOut = readTimeOut;
        this.connectTimeOut = connectTimeOut;
        this.headerMap = headerMap;
    }

}
