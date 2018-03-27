package com.ISHello.Service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.ISHello.utils.LogUtil;
import com.example.ishelloword.R;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * Created by zhanglong on 2017/5/22.
 */

public class AsyncTaskDownloadServicesOther extends AsyncTask<Object, String, Integer> {
    private final String TAG = AsyncTaskDownloadServicesOther.class.getSimpleName();
    private ProgressDialog progressDialog;
    private Handler hd;

    public static final int DOWNLOAD_SUCCESS = 0;
    public static final int DOWNLOAD_FAIL = 1;

    public AsyncTaskDownloadServicesOther(Context context, String title, String msg) {
        progressDialog = new ProgressDialog(context, R.style.Theme_AlertDialogPro_ICBC);
        progressDialog.setTitle(title);
        progressDialog.setMessage(msg);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setIcon(R.drawable.ic_launcher);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Object... paramTemp) {
        int result = DOWNLOAD_FAIL;
        try {
            String url = (String) paramTemp[0];
            String filePath = "";
            if (paramTemp[1] instanceof File)
                filePath = ((File) paramTemp[1]).getAbsolutePath();
            else
                filePath = (String) paramTemp[1];
            String fileName = (String) paramTemp[2];
            hd = (Handler) paramTemp[3];
            result = downLoad(url, filePath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Integer result) {
        Message msg;
        switch (result) {
            case DOWNLOAD_SUCCESS:
                msg = Message.obtain();
                msg.arg1 = DOWNLOAD_SUCCESS;
                hd.sendMessage(msg);
                break;
            case DOWNLOAD_FAIL:
                msg = Message.obtain();
                msg.arg1 = DOWNLOAD_FAIL;
                hd.sendMessage(msg);
                break;
            default:
                break;
        }
        progressDialog.dismiss();
        super.onPostExecute(result);
    }

    protected void onProgressUpdate(String... progress) {
        progressDialog.setProgress(Integer.parseInt(progress[0]));
    }

    @Deprecated
    public Integer downLoad(String url, String filePath, String fileName) throws Exception {
        try {
            HttpParams params = new BasicHttpParams();
            // 设置协议及UA
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpProtocolParams.setUseExpectContinue(params, Boolean.FALSE);
            // 设置超时时间
            ConnManagerParams.setTimeout(params, 60 * 1000);
            HttpConnectionParams.setConnectionTimeout(params, 60 * 1000);
            HttpConnectionParams.setSoTimeout(params, 60 * 1000);
            DefaultHttpClient httpClient;
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            httpClient = new DefaultHttpClient(ccm, params);
            // 加入Gzip压缩
            httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
                public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
                    if (!request.containsHeader("Accept-Encoding")) {
                        request.addHeader("Accept-Encoding", "gzip");
                    }
                }
            });
            httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
                public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
                    HttpEntity entity = response.getEntity();
                    Header ceheader = entity.getContentEncoding();
                    if (ceheader != null) {
                        HeaderElement[] codecs = ceheader.getElements();
                        for (int i = 0; i < codecs.length; i++) {
                            if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                                response.setEntity(new HttpEntityWrapper(response.getEntity()) {
                                    @Override
                                    public InputStream getContent() throws IOException, IllegalStateException {

                                        InputStream wrappedin = wrappedEntity.getContent();
                                        return new GZIPInputStream(wrappedin);
                                    }

                                    @Override
                                    public long getContentLength() {
                                        return -1;
                                    }
                                });
                                return;
                            }
                        }
                    }
                }
            });
            HttpGet get = new HttpGet(url);
            //在请求中明确定义不要进行压缩
            get.setHeader("Accept-Encoding", "identity");
            HttpResponse response;
            response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            long length = 0;
            length = entity.getContentLength();
            if (response.containsHeader("jar-length")) {
                Header[] header = response.getHeaders("jar-length");
                length = Long.parseLong(header[0].getValue());
            } else if (response.containsHeader("file-length")) {
                Header[] header = response.getHeaders("file-length");
                length = Long.parseLong(header[0].getValue());
            }
            if (length <= 0)
                return DOWNLOAD_FAIL;
            InputStream is = entity.getContent();
            FileOutputStream fileOutputStream = null;
            if (is != null) {
                File path = new File(filePath);
                if (!path.isDirectory()) {
                    path.mkdirs();
                }
                File file = null;
                file = new File(filePath, fileName);
                if (file.exists()) {
                    file.delete();
                } else {
                    file.createNewFile();
                }
                fileOutputStream = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int ch;
                long total = 0;
                int oldProgress = 0;
                while ((ch = is.read(buf)) > 0) {
                    total += ch;
                    int progress = (int) ((total * 100) / length);
                    if (progress > oldProgress) {
                        oldProgress = progress;
                        LogUtil.log(TAG, "--->load progress==" + progress);
                        publishProgress("" + progress);
                    }
                    fileOutputStream.write(buf, 0, ch);
                }
            }
            fileOutputStream.flush();
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            return DOWNLOAD_SUCCESS;
        } catch (Exception e) {
            return DOWNLOAD_FAIL;
        }
    }
}
