package com.ISHello.Retrofit.Interceptor;

public interface JsDownloadListener {
    void onStartDownload(long length);
    void onProgress(long totalSize,long downSize);
}
