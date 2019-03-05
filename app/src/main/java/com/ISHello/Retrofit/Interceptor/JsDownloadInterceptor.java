package com.ISHello.Retrofit.Interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JsDownloadInterceptor implements Interceptor {
    private JsDownloadListener downloadListener;
    private long downSize = 0;
    private long totalSize = 0;

    public JsDownloadInterceptor(JsDownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (downSize != 0 && totalSize != 0) {
            request = request.newBuilder()
                    .addHeader("RANGE", "bytes=" + downSize + "-" + totalSize)
                    .addHeader("Accept-Encoding", "identity").build();
        }else{
            request = request.newBuilder()
                    .addHeader("Accept-Encoding", "identity").build();
        }
        Response response = chain.proceed(request);
        return response.newBuilder().body(
                new JsResponseBody(response.body(), downloadListener)).build();
    }

}