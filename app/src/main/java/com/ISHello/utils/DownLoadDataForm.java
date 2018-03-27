/**
 *
 */
package com.ISHello.utils;

import java.io.Serializable;
import java.net.Proxy;
import java.util.Map;

public class DownLoadDataForm implements Serializable {

    private static final long serialVersionUID = -1033101430849143560L;

    public String url;// 请求的URL

    public String[] filePaths;// 文件路径集合

    public boolean get;// 是否是Get请求

    public Map<String, String> paraMap;// 参数列表

    public boolean useProxy;// 是不是使用代理

    public Proxy proxy;

    public URLConnectConfig config;

    public DownLoadDataForm(String url) {
        this(url, true, null, false, null, new URLConnectConfig());
    }

    public DownLoadDataForm(String url, Map<String, String> paraMap) {
        this(url, false, paraMap, false, null, new URLConnectConfig());
    }

    public DownLoadDataForm(String url, Map<String, String> paraMap, Map<String, String> headerMap) {
        this(url, false, paraMap, false, null, new URLConnectConfig(headerMap));
    }

    public DownLoadDataForm(String url, Map<String, String> paraMap, Map<String, String> headerMap, String[] filePaths) {
        this(url, false, paraMap, filePaths, false, null, new URLConnectConfig(headerMap));
    }

    public DownLoadDataForm(String url, URLConnectConfig urlConnectConfig) {
        this(url, true, null, false, null, urlConnectConfig);
    }

    public DownLoadDataForm(String url, boolean useProxy, Proxy proxy) {
        this(url, true, null, useProxy, proxy, new URLConnectConfig());
    }

    public DownLoadDataForm(String url, boolean get, Map<String, String> paraMap, boolean useProxy, Proxy proxy, URLConnectConfig config) {
        super();
        this.url = url;
        this.get = get;
        this.paraMap = paraMap;
        this.useProxy = useProxy;
        this.config = config;
    }

    public DownLoadDataForm(String url, boolean get, Map<String, String> paraMap, String[] filePaths, boolean useProxy, Proxy proxy,
                            URLConnectConfig config) {
        super();
        this.url = url;
        this.get = get;
        this.filePaths = filePaths;
        this.paraMap = paraMap;
        this.useProxy = useProxy;
        this.config = config;
    }

    public static DownLoadDataForm obtainDataForm(String url) {
        return new DownLoadDataForm(url);
    }

    public static DownLoadDataForm obtainDataForm(String url, Map<String, String> paraMap) {
        return new DownLoadDataForm(url, paraMap);
    }

}
