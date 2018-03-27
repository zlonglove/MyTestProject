package com.ISHello.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.util.HttpURLConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * @author kfzx-zhanglong
 */
public class HttpClientConnectionUtil {

    HttpClient mHttpClient;
    HttpParams mHttpParams;
    HttpGet mHttpGet;
    HttpPost mHttpPost;
    // PostMethod mPostMethod;
    HttpResponse mHttpResponse;
    DownLoadDataForm mDataForm;

    public HttpClientConnectionUtil(DownLoadDataForm dataForm) {
        mDataForm = dataForm;
    }

    public void initConnect() {
        URLConnectConfig connectConfig = mDataForm.config;
        mHttpParams = new BasicHttpParams();
        mHttpParams.setParameter("charset", HTTP.UTF_8);

        HttpConnectionParams.setConnectionTimeout(mHttpParams, connectConfig.connectTimeOut);
        HttpConnectionParams.setSoTimeout(mHttpParams, connectConfig.readTimeOut);

        mHttpClient = new DefaultHttpClient(mHttpParams);
        // 设置代理
        if (mDataForm.useProxy == true && mDataForm.proxy != null) {
            Proxy proxy = mDataForm.proxy;
            InetSocketAddress socketAddress = (InetSocketAddress) proxy.address();
            mHttpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
                    new HttpHost(socketAddress.getHostName(), socketAddress.getPort(), proxy.type().name()));
        }
        // 设置请求方法
        if (mDataForm.get) {
            mHttpGet = new HttpGet(mDataForm.url);
            if (mDataForm.config.headerMap != null) {
                Map<String, String> headMap = mDataForm.config.headerMap;
                Iterator<Entry<String, String>> it = headMap.entrySet().iterator();
                Entry<String, String> curEntry;
                while (it.hasNext()) {
                    curEntry = it.next();
                    mHttpGet.addHeader(curEntry.getKey(), curEntry.getValue());
                }
            }
            try {
                mHttpResponse = mHttpClient.execute(mHttpGet);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // mHttpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");
            mHttpPost = new HttpPost(mDataForm.url);
            // mHttpPost.addHeader("charset",HTTP.UTF_8);
            mHttpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            List<NameValuePair> params = null;
            // connection.setRequestProperty("Content-Type",
            // "application/x-www-form-urlencoded");
            // connection.setRequestProperty("Charset", "UTF-8");
            if (mDataForm.config.headerMap != null) {
                Map<String, String> headMap = mDataForm.config.headerMap;
                Iterator<Entry<String, String>> it = headMap.entrySet().iterator();
                Entry<String, String> curEntry;
                while (it.hasNext()) {
                    curEntry = it.next();
                    mHttpPost.addHeader(curEntry.getKey(), curEntry.getValue());
                }
            }
            if (mDataForm.paraMap != null) {
                Map<String, String> paras = mDataForm.paraMap;
                params = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : paras.entrySet()) {
                    params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                try {
                    mHttpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            try {
                mHttpResponse = mHttpClient.execute(mHttpPost);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public byte[] getResponseByteArray() throws ClientProtocolException, IOException {
        initConnect();
        byte[] bytes = readData();
        close();
        return bytes;
    }

    public HttpEntity getResponseEntity() throws ClientProtocolException, IOException {
        initConnect();
        HttpEntity entity = getEntity();
        // close();
        return entity;
    }

    private byte[] readData() throws IOException {
        if (mHttpResponse.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
            return EntityUtils.toByteArray(mHttpResponse.getEntity());
        } else {
            return null;
        }
    }

    public String getResponseString() throws ClientProtocolException, IOException {
        initConnect();
        String result = readStringData();
        close();
        return result;
    }

    private HttpEntity getEntity() {
        if (mHttpResponse != null && mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return mHttpResponse.getEntity();
        } else {
            return null;
        }
    }

    private String readStringData() throws ParseException, IOException {
        if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String result = EntityUtils.toString(mHttpResponse.getEntity());
            /**
             * 用的时候经常要加上URLDecoder.decode对网页的转义字符进行处理
             */
            result = URLDecoder.decode(result, "UTF-8");// 对网页的的转义字符进行处理
            return result;
        } else {
            return null;
        }
    }

    public void close() {
        mHttpClient.getConnectionManager().shutdown();
    }
}
