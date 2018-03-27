package com.ISHello.utils;

/**
 * https超文本安全传输协议,比http多了一个SSL/TSL的认证过程
 * <p>
 * 关于HttpClient的总结
 * (1)当HttpClient的实例不再需要时，可以使用连接管理器关闭
 * httpclient.getConnectionManager().shutdown();
 * (1)当HttpClient的实例不再需要时，可以使用连接管理器关闭
 * httpclient.getConnectionManager().shutdown();
 * (2)针对HTTPs的协议的HttpClient请求必须用户和密码
 * httpclient.getCredentialsProvider()
 * .setCredentials(new AuthScope("localhost", 443),
 * new UsernamePasswordCredentials("username", "password"));
 * <p>
 * (2)针对HTTPs的协议的HttpClient请求必须用户和密码
 * httpclient.getCredentialsProvider()
 * .setCredentials(new AuthScope("localhost", 443),
 * new UsernamePasswordCredentials("username", "password"));
 * (3)如果不想获取HTTPClient返回的信息
 * httpclient.abort();
 * <p>
 * (3)如果不想获取HTTPClient返回的信息
 * httpclient.abort();
 * <p>
 * <p>
 * (4)httpclient传送文件的方式
 * HttpClient httpclient = new DefaultHttpClient();
 * HttpPost httppost = new HttpPost("http://www.apache.org");
 * File file = new File(args[0]);
 * InputStreamEntity reqEntity = new InputStreamEntity(
 * new FileInputStream(file), -1);
 * reqEntity.setContentType("binary/octet-stream");
 * reqEntity.setChunked(true);
 * // It may be more appropriate to use FileEntity class in this particular
 * // instance but we are using a more generic InputStreamEntity to demonstrate
 * // the capability to stream out data from any arbitrary source
 * //
 * // FileEntity entity = new FileEntity(file, "binary/octet-stream");
 * httppost.setEntity(reqEntity);
 * System.out.println("executing request " + httppost.getRequestLine());
 * HttpResponse response = httpclient.execute(httppost);
 * <p>
 * <p>
 * (4)httpclient传送文件的方式
 * HttpClient httpclient = new DefaultHttpClient();
 * HttpPost httppost = new HttpPost("http://www.apache.org");
 * File file = new File(args[0]);
 * InputStreamEntity reqEntity = new InputStreamEntity(
 * new FileInputStream(file), -1);
 * reqEntity.setContentType("binary/octet-stream");
 * reqEntity.setChunked(true);
 * // It may be more appropriate to use FileEntity class in this particular
 * // instance but we are using a more generic InputStreamEntity to demonstrate
 * // the capability to stream out data from any arbitrary source
 * //
 * // FileEntity entity = new FileEntity(file, "binary/octet-stream");
 * httppost.setEntity(reqEntity);
 * System.out.println("executing request " + httppost.getRequestLine());
 * HttpResponse response = httpclient.execute(httppost);
 * <p>
 * (5)获取Cookie的信息
 * HttpClient httpclient = new DefaultHttpClient();
 * // 创建一个本地Cookie存储的实例
 * CookieStore cookieStore = new BasicCookieStore();
 * //创建一个本地上下文信息
 * HttpContext localContext = new BasicHttpContext();
 * //在本地上下问中绑定一个本地存储
 * localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
 * //设置请求的路径
 * HttpGet httpget = new HttpGet("http://www.google.com/");
 * //传递本地的http上下文给服务器
 * HttpResponse response = httpclient.execute(httpget, localContext);
 * //获取本地信息
 * HttpEntity entity = response.getEntity();
 * System.out.println(response.getStatusLine());
 * if (entity != null) {
 * System.out.println("Response content length: " + entity.getContentLength());
 * }
 * //获取cookie中的各种信息
 * List<Cookie> cookies = cookieStore.getCookies();
 * for (int i = 0; i < cookies.size(); i++) {
 * System.out.println("Local cookie: " + cookies.get(i));
 * }
 * //获取消息头的信息
 * Header[] headers = response.getAllHeaders();
 * for (int i = 0; i<headers.length; i++) {
 * System.out.println(headers[i]);
 * }
 * <p>
 * (5)获取Cookie的信息
 * HttpClient httpclient = new DefaultHttpClient();
 * // 创建一个本地Cookie存储的实例
 * CookieStore cookieStore = new BasicCookieStore();
 * //创建一个本地上下文信息
 * HttpContext localContext = new BasicHttpContext();
 * //在本地上下问中绑定一个本地存储
 * localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
 * //设置请求的路径
 * HttpGet httpget = new HttpGet("http://www.google.com/");
 * //传递本地的http上下文给服务器
 * HttpResponse response = httpclient.execute(httpget, localContext);
 * //获取本地信息
 * HttpEntity entity = response.getEntity();
 * System.out.println(response.getStatusLine());
 * if (entity != null) {
 * System.out.println("Response content length: " + entity.getContentLength());
 * }
 * //获取cookie中的各种信息
 * List<Cookie> cookies = cookieStore.getCookies();
 * for (int i = 0; i < cookies.size(); i++) {
 * System.out.println("Local cookie: " + cookies.get(i));
 * }
 * //获取消息头的信息
 * Header[] headers = response.getAllHeaders();
 * for (int i = 0; i<headers.length; i++) {
 * System.out.println(headers[i]);
 * }
 * <p>
 * (6)针对典型的SSL请求的处理
 * DefaultHttpClient httpclient = new DefaultHttpClient();
 * //获取默认的存储密钥类
 * KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());
 * //加载本地的密钥信息
 * FileInputStream instream = new FileInputStream(new File("my.keystore"));
 * try {
 * trustStore.load(instream, "nopassword".toCharArray());
 * } finally {
 * instream.close();
 * }
 * //创建SSLSocketFactory，创建相关的Socket
 * SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
 * //设置协议的类型和密钥信息，以及断开信息
 * Scheme sch = new Scheme("https", socketFactory, 443);
 * //在连接管理器中注册中信息
 * httpclient.getConnectionManager().getSchemeRegistry().register(sch);
 * <p>
 * (6)针对典型的SSL请求的处理
 * DefaultHttpClient httpclient = new DefaultHttpClient();
 * //获取默认的存储密钥类
 * KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());
 * //加载本地的密钥信息
 * FileInputStream instream = new FileInputStream(new File("my.keystore"));
 * try {
 * trustStore.load(instream, "nopassword".toCharArray());
 * } finally {
 * instream.close();
 * }
 * //创建SSLSocketFactory，创建相关的Socket
 * SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
 * //设置协议的类型和密钥信息，以及断开信息
 * Scheme sch = new Scheme("https", socketFactory, 443);
 * //在连接管理器中注册中信息
 * httpclient.getConnectionManager().getSchemeRegistry().register(sch);
 * <p>
 * (7)设置请求的参数的几种方式
 * A.在请求的路径中以查询字符串格式传递参数
 * B.在请求的实体中添加参数
 * List <NameValuePair> nvps = new ArrayList <NameValuePair>();
 * nvps.add(new BasicNameValuePair("IDToken1", "username"));
 * nvps.add(new BasicNameValuePair("IDToken2", "password"));
 * httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
 */

import android.util.Log;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.util.List;
import java.util.Map;

public class AsyncHttpClient {
    private final static String LOG_TAG = "AsyncHttpClient";
    private static AsyncHttpClient asynchttpclient;
    private static DefaultHttpClient httpClient;
    public static final String VERSION = "1.0.0";
    public static final int DEFAULT_SOCKET_TIMEOUT = 10 * 1000;
    public static final int DEFAULT_MAX_CONNECTIONS = 10;
    public static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;

    private int timeout = DEFAULT_SOCKET_TIMEOUT;
    private int maxConnections = DEFAULT_MAX_CONNECTIONS;

    /**
     * Creates new AsyncHttpClient using given params
     *
     * @param fixNoHttpResponseException true--不验证证书
     * @param httpPort                   HTTP port to be used, must be greater than 0,default http port
     *                                   is 80
     * @param httpsPort                  HTTPS port to be used, must be than 0,default https port is
     *                                   443
     */
    public static synchronized AsyncHttpClient getInstance(boolean fixNoHttpResponseException, int httpPort, int httpsPort) {
        if (asynchttpclient == null) {
            asynchttpclient = new AsyncHttpClient(fixNoHttpResponseException, httpPort, httpsPort);
        }
        return asynchttpclient;
    }

    public static synchronized AsyncHttpClient getInstance() {
        if (asynchttpclient == null) {
            asynchttpclient = new AsyncHttpClient(false, 80, 443);
        }
        return asynchttpclient;
    }

    public HttpClient getHtttpClient() {
        return httpClient;
    }

    private AsyncHttpClient(boolean fixNoHttpResponseException, int httpPort, int httpsPort) {
        this(getDefaultSchemeRegistry(fixNoHttpResponseException, httpPort, httpsPort));
    }

    /**
     * Creates a new AsyncHttpClient.
     *
     * @param schemeRegistry SchemeRegistry to be used
     */
    public AsyncHttpClient(SchemeRegistry schemeRegistry) {

        BasicHttpParams httpParams = new BasicHttpParams();

        ConnManagerParams.setTimeout(httpParams, timeout);
        /**
         * 设置每个路由的最多链接数量
         */
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(maxConnections));
        /**
         * 设置允许链接的最多链接数目
         */
        ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);
        /**
         * 设置请求字符集
         */
        HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        // HttpProtocolParams.setUserAgent(httpParams, String.format(
        // "android-async-http/%s (http://loopj.com/android-async-http)",
        // VERSION));
        /**
         * 设置服务器的响应超时时间
         */
        HttpConnectionParams.setSoTimeout(httpParams, timeout);
        /**
         * 设置客戶端和服务链接的超时时间
         */
        HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);

        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
        // PoolingClientConnectionManager cm =
        // newPoolingClientConnectionManager(schemeRegistry);//创建connectionManager
        httpClient = new DefaultHttpClient(cm, httpParams);
    }

    /**
     * Returns default instance of SchemeRegistry
     *
     * @param fixNoHttpResponseException Whether to fix or not issue, by ommiting SSL verification
     * @param httpPort                   HTTP port to be used, must be greater than 0
     * @param httpsPort                  HTTPS port to be used, must be greater than 0
     */
    private static SchemeRegistry getDefaultSchemeRegistry(boolean fixNoHttpResponseException, int httpPort, int httpsPort) {
        if (fixNoHttpResponseException) {
            Log.d(LOG_TAG, "Beware! Using the fix is insecure, as it doesn't verify SSL certificates.");
        }
        if (httpPort < 1) {
            httpPort = 80;
            Log.d(LOG_TAG, "Invalid HTTP port number specified, defaulting to 80");
        }
        if (httpsPort < 1) {
            httpsPort = 443;
            Log.d(LOG_TAG, "Invalid HTTPS port number specified, defaulting to 443");
        }
        // Fix to SSL flaw in API < ICS
        // See https://code.google.com/p/android/issues/detail?id=13117
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        SSLSocketFactory sslSocketFactory;
        if (fixNoHttpResponseException) {//https不验证证书
            sslSocketFactory = MySSLSocketFactory.getFixedSocketFactory();
        } else {
            sslSocketFactory = SSLSocketFactory.getSocketFactory();
        }

        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), httpPort));
        schemeRegistry.register(new Scheme("https", sslSocketFactory, httpsPort));
        return schemeRegistry;
    }

    public String httpPost(String url, Map<String, String> params) {
        HttpPost post = null;
        HttpResponse response = null;
        String result = "";
        try {
            post = new HttpPost(url);
            List<NameValuePair> listPair = NetworkDataUtil.getParamsList(params);
            if (listPair != null && listPair.size() > 0) {
                post.setEntity(new UrlEncodedFormEntity(listPair, HTTP.UTF_8));
            }
            response = httpClient.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
            } else {
                /**
                 * 错误处理，例如可以在该请求正常结束前将其中断
                 */
                post.abort();
            }
        } catch (ConnectException connectException) {
            connectException.printStackTrace();
        } catch (SocketException socketException) {
            socketException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            /**
             * 释放链接
             */
            // httpClient.getConnectionManager().shutdown();
        }
        return result;
    }

    public String httpGet(String url) {
        HttpGet get = null;
        HttpResponse response = null;
        String result = "";
        try {
            get = new HttpGet(url);
            response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
            } else {
                get.abort();
            }
        } catch (ConnectException connectException) {
            connectException.printStackTrace();
        } catch (SocketException socketException) {
            socketException.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            /**
             * 释放链接,如果下次需要重新链接需要重新实例化HttpClient
             */
            // httpClient.getConnectionManager().shutdown();
        }
        return result;
    }

    /**
     * 释放链接
     */
    public void release() {
        if (httpClient != null) {
            httpClient.getConnectionManager().shutdown();
            httpClient = null;
        }
    }
}
