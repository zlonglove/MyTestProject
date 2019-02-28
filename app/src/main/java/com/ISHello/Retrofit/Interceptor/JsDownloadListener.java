package com.ISHello.Retrofit.Interceptor;

public interface JsDownloadListener {
    void onStartDownload(long length);
    void onProgress(int progress);
    void onFail(String errorInfo);
}
