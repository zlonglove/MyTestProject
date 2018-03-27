package com.ISHello.base.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ISHello.Application.BaseApplication;
import com.ISHello.Constants.Constants;
import com.ISHello.Constants.Constants.ModeType;
import com.ISHello.base.net.HttpRespEntity.HttpRespResultType;
import com.ISHello.base.tools.GzipDecompressingEntity;
import com.ISHello.utils.LogUtil;
import com.ISHello.utils.ICBCSSLSocketFactory;
import com.ISHello.utils.NetworkDataUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;


//@author SL HttpCient封装类
public class ICBCHttpClientUtil {

    public static int TIME_OUT = 10;// 超时时间

    // Post方式提交    是否校验服务器证书 verifyCertificateOrg，仅初始化请求为true,其余请求为false
    public static HttpRespEntity post(String url, Map<String, String> params, int timeOut, boolean verifyCertificateOrg) throws Exception {
        HttpRespEntity result = new HttpRespEntity(HttpRespResultType.HTTP_REQUEST_FAILED);
        if (url == null || url.length() == 0) {
            return result;
        }
        // 创建HttpClient实例
        DefaultHttpClient httpClient = getDefaultHttpClient(timeOut, verifyCertificateOrg);
        UrlEncodedFormEntity formEntity = null;
        HttpPost hp = new HttpPost(url);
        // 参数处理
        formEntity = new UrlEncodedFormEntity(getParamsList(params), Constants.CHARSET_UTF8);
        // 设置超时时间
        hp.setEntity(formEntity);

        // 发送请求，得到响应
        try {
            HttpResponse response = httpClient.execute(hp);
            HttpEntity entity = response.getEntity();
            // entity不为null说明HTTP请求成功
            if (entity != null) {
                // 获取响应代码
                int Status = response.getStatusLine().getStatusCode();
                //LogUtil.log("http", "Status:" + Status);
                // 200响应成功
                if (Status == 200) {
                    // 得到返回的网页源码
                    String source = new String(EntityUtils.toByteArray(entity), Constants.CHARSET_UTF8).trim();
                    JSONObject jsonObject = new JSONObject(source);
                    Gson gson = new Gson();
                    HashMap<String, String> resultMap = gson.fromJson(jsonObject.toString(), new TypeToken<HashMap<String, String>>() {
                    }.getType());
                    result.setResult(HttpRespResultType.HTTP_REQUEST_OK);
                    result.setResultHashMap(resultMap);
                    result.setErrorMessage("");
                    return result;
                } else {
                    result.setResult(HttpRespResultType.HTTP_REQUEST_FAILED);
                    result.setErrorMessage("对不起，系统服务暂不可用，请稍后重试。");
                    return result;
                }
            } else {
                result.setResult(HttpRespResultType.HTTP_REQUEST_FAILED);
                result.setErrorMessage("对不起，您的网络不可用，无法完成当前操作，请稍后重试。");
                return result;
            }
        } catch (UnknownHostException e) {
            result.setResult(HttpRespResultType.HTTP_REQUEST_FAILED);
            result.setErrorMessage("您的网络存在异常，无法解析服务器地址，请检查网络设置。");
        } catch (SocketTimeoutException e) {
            result.setResult(HttpRespResultType.HTTP_REQUEST_FAILED);
            result.setErrorMessage("您的网络存在异常，连接服务器超时，请稍后重试。");
        } catch (SSLException e) {
            result.setResult(HttpRespResultType.HTTPS_SSL_ERROR);
            result.setErrorMessage("对不起，服务器安全链接建立失败，请检查您的网络配置和手机的系统时间是否正确。");
        } catch (Exception e) {
            result.setResult(HttpRespResultType.HTTP_REQUEST_FAILED);
            result.setErrorMessage("对不起，您的网络不可用，请稍后重试。");
        } finally {
            abortConnection(hp, httpClient);
        }
        return result;
    }

    // GET方式提交
    public static HttpRespEntity get(String url, int timeOut) throws Exception {
        HttpRespEntity result = new HttpRespEntity(HttpRespResultType.HTTP_REQUEST_FAILED);
        if (url == null || url.length() == 0) {
            return result;
        }
        // 创建HttpClient实例
        DefaultHttpClient httpClient = getDefaultHttpClient(timeOut);
        HttpGet hg = new HttpGet(url);
        // 发送请求，得到响应
        try {
            HttpResponse response = httpClient.execute(hg);
            HttpEntity entity = response.getEntity();
            // entity不为null说明HTTP请求成功
            if (entity != null) {
                // 获取响应代码
                int Status = response.getStatusLine().getStatusCode();
                //LogUtil.log("http", "Status:" + Status);
                // 200响应成功
                if (Status == 200) {
                    // 得到返回的网页源码
                    String source = new String(EntityUtils.toByteArray(entity), Constants.CHARSET_UTF8).trim();
                    JSONObject jsonObject = new JSONObject(source);
                    Gson gson = new Gson();
                    HashMap<String, String> resultMap = gson.fromJson(jsonObject.toString(), new TypeToken<HashMap<String, String>>() {
                    }.getType());
                    result.setResult(HttpRespResultType.HTTP_REQUEST_OK);
                    result.setResultHashMap(resultMap);
                    result.setErrorMessage("");
                    return result;
                } else {
                    result.setResult(HttpRespResultType.HTTP_REQUEST_OK_NOT200);
                    result.setErrorMessage("对不起，系统服务暂不可用，请稍后重试。");
                    return result;
                }
            } else {
                result.setResult(HttpRespResultType.HTTP_REQUEST_FAILED);
                result.setErrorMessage("对不起，您的网络不可用，无法完成当前操作，请稍后重试。");
                return result;
            }
        } catch (UnknownHostException e) {
            result.setResult(HttpRespResultType.HTTP_REQUEST_FAILED);
            result.setErrorMessage("您的网络存在异常，无法解析服务器地址，请检查网络设置。");
        } catch (SocketTimeoutException e) {
            result.setResult(HttpRespResultType.HTTP_REQUEST_FAILED);
            result.setErrorMessage("您的网络存在异常，连接服务器超时，请稍后重试。");
        } catch (SSLException e) {
            result.setResult(HttpRespResultType.HTTPS_SSL_ERROR);
            result.setErrorMessage("对不起，服务器安全链接建立失败，请检查您的网络配置和手机的系统时间是否正确。");
        } catch (Exception e) {
            result.setResult(HttpRespResultType.HTTP_REQUEST_FAILED);
            result.setErrorMessage("对不起，您的网络不可用，请稍后重试。");
        } finally {
            abortConnection(hg, httpClient);
        }
        return result;
    }

    public static void postCrashLog(String url, String log) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair logPair = new BasicNameValuePair("info", log);
        params.add(logPair);
        if (url == null || url.length() == 0) {
            return;
        }
        // 创建HttpClient实例
        DefaultHttpClient httpClient = getDefaultHttpClient();
        UrlEncodedFormEntity formEntity = null;
        HttpPost hp = new HttpPost(url);
        // 发送请求，得到响应
        try {
            // 参数处理
            formEntity = new UrlEncodedFormEntity(params, Constants.CHARSET_UTF8);
            hp.setEntity(formEntity);
            HttpResponse response = httpClient.execute(hp);
            HttpEntity entity = response.getEntity();
            // entity不为null说明HTTP请求成功
            if (entity != null) {
                // 获取响应代码
                int Status = response.getStatusLine().getStatusCode();
                LogUtil.log("http", "Status:" + Status);
            }
        } catch (Exception e) {
        }
    }

    public static HttpRespEntity postToGetByte(String url, Map<String, String> params, int timeOut) throws Exception {
        HttpRespEntity result = new HttpRespEntity(HttpRespResultType.HTTP_REQUEST_FAILED);
        if (url == null || url.length() == 0) {
            return result;
        }
        // 创建HttpClient实例
        DefaultHttpClient httpClient = getDefaultHttpClient(timeOut);
        UrlEncodedFormEntity formEntity = null;
        HttpPost hp = new HttpPost(url);
        // 参数处理
        formEntity = new UrlEncodedFormEntity(getParamsList(params), Constants.CHARSET_UTF8);
        // 设置超时时间
        hp.setEntity(formEntity);
        // 发送请求，得到响应
        try {
            HttpResponse response = httpClient.execute(hp);
            HttpEntity entity = response.getEntity();
            // entity不为null说明HTTP请求成功
            if (entity != null) {
                // 获取响应代码
                int Status = response.getStatusLine().getStatusCode();
                // 200响应成功
                if (Status == 200) {
                    result.setResultByte(EntityUtils.toByteArray(entity));
                    result.setResult(HttpRespResultType.HTTP_REQUEST_OK);
                    result.setErrorMessage("");
                    return result;
                } else {
                    result.setResult(HttpRespResultType.HTTP_REQUEST_OK_NOT200);
                    result.setErrorMessage("对不起，系统服务暂不可用，请稍后重试。");
                    return result;
                }
            } else {
                result.setResult(HttpRespResultType.HTTP_REQUEST_FAILED);
                result.setErrorMessage("对不起，您的网络不可用，无法完成当前操作，请稍后重试。");
                return result;
            }
        } catch (UnknownHostException e) {
            result.setResult(HttpRespResultType.HTTP_REQUEST_FAILED);
            result.setErrorMessage("您的网络存在异常，无法解析服务器地址，请检查网络设置。");
        } catch (SocketTimeoutException e) {
            result.setResult(HttpRespResultType.HTTP_REQUEST_FAILED);
            result.setErrorMessage("您的网络存在异常，连接服务器超时，请稍后重试。");
        } catch (SSLException e) {
            result.setResult(HttpRespResultType.HTTPS_SSL_ERROR);
            result.setErrorMessage("对不起，服务器安全链接建立失败，请检查您的网络配置和手机的系统时间是否正确。");
        } catch (Exception e) {
            result.setResult(HttpRespResultType.HTTP_REQUEST_FAILED);
            result.setErrorMessage("对不起，您的网络不可用，请稍后重试。");
        } finally {
            abortConnection(hp, httpClient);
        }
        return result;
    }

    public static String postToGetSource(String url, Map<String, String> params, String charset, int timeOut) throws Exception {
        String result = "";
        if (url == null || url.length() == 0) {
            return result;
        }
        // 创建HttpClient实例
        DefaultHttpClient httpclient = getDefaultHttpClient(timeOut);
        HttpProtocolParams.setUserAgent(httpclient.getParams(), Constants.WebViewUA);
        UrlEncodedFormEntity formEntity = null;
        HttpPost httpPost = new HttpPost(url);
        // 参数处理
        formEntity = new UrlEncodedFormEntity(getParamsList(params), charset);
        httpPost.setEntity(formEntity);
        // 发送请求，得到响应
        try {
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            // entity不为null说明HTTP请求成功
            if (entity != null) {
                // 获取响应代码
                int Status = response.getStatusLine().getStatusCode();
                // 200响应成功
                if (Status == 200) {
                    // 得到返回的网页源码
                    result = new String(EntityUtils.toByteArray(entity), charset).trim();
                } else {
                    result = "";
                }
            } else {
                result = "";
            }
        } catch (Exception e) {
            result = "";
        }
        return result;
    }

    public static String getPageData(String url, int timeOut) {
        String strResult = "";
        DefaultHttpClient httpClient = getDefaultHttpClient(timeOut);
        HttpGet hp = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(hp);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                int Status = response.getStatusLine().getStatusCode();
                //LogUtil.log("http", "Status:" + Status);
                // 200响应成功
                if (Status == 200) {
                    // 得到返回的网页源码
                    strResult = new String(EntityUtils.toByteArray(entity), Constants.CHARSET_UTF8).trim();
                    strResult = strResult.replaceAll("[\\r\\n]+", "");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strResult;
    }

    public static DefaultHttpClient getDefaultHttpClient() {
        return getDefaultHttpClient(TIME_OUT);
    }


    public static DefaultHttpClient getDefaultHttpClient(int timeOut) {
        return getDefaultHttpClient(timeOut, false);
    }

    @SuppressWarnings("deprecation")
    public static DefaultHttpClient getDefaultHttpClient(int timeOut, boolean verifyCertificateOrg) {
        try {
            HttpParams params = new BasicHttpParams();
            try {
                String[] connectType = getConnectType();
                // 判断联网方式是否为WIFI
                if (!"wifi".equals(connectType[0])) {
                    // 判断接入点是否为wap（如果是加入代理）
                    if (connectType[1].indexOf("cmwap") > -1 || connectType[1].indexOf("uniwap") > -1 || connectType[1].indexOf("3gwap") > -1 || connectType[1].indexOf("ctwap") > -1) {
                        String proxyHost = android.net.Proxy.getDefaultHost();
                        int proxyPort = android.net.Proxy.getDefaultPort();
                        HttpHost proxy = new HttpHost(proxyHost, proxyPort);
                        params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
                    }
                }
            } catch (Exception e) {
            }
            // 设置协议及UA
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpProtocolParams.setUserAgent(params, Constants.WebViewUA);
            HttpProtocolParams.setUseExpectContinue(params, Boolean.FALSE);
            // 设置超时时间
            if (timeOut <= 0) {
                timeOut = TIME_OUT;
            }
            ConnManagerParams.setTimeout(params, timeOut * 1000);
            HttpConnectionParams.setConnectionTimeout(params, timeOut * 1000);
            HttpConnectionParams.setSoTimeout(params, timeOut * 1000);
            DefaultHttpClient httpClient;
            if (Constants.MODE == ModeType.Test) {
                // 测试模式不验证证书
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
                SSLSocketFactory sf = new ICBCSSLSocketFactory(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));
                ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
                httpClient = new DefaultHttpClient(ccm, params);
            } else {
                if (!verifyCertificateOrg) {
                    SchemeRegistry registry = new SchemeRegistry();
                    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                    registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
                    ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
                    httpClient = new DefaultHttpClient(ccm, params);
                } else {
                    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                    trustStore.load(null, null);
                    SSLSocketFactory sf = new ICBCSSLSocketFactory(trustStore);
                    sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
                    SchemeRegistry registry = new SchemeRegistry();
                    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                    registry.register(new Scheme("https", sf, 443));
                    ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
                    httpClient = new DefaultHttpClient(ccm, params);
                }
            }
            httpClient.setHttpRequestRetryHandler(requestRetryHandler);
            // 加入Gzip压缩
            httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
                @Override
                public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
                    if (!request.containsHeader("Accept-Encoding")) {
                        request.addHeader("Accept-Encoding", "gzip");
                    }
                }
            });
            httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
                @Override
                public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
                    HttpEntity entity = response.getEntity();
                    Header ceheader = entity.getContentEncoding();
                    if (ceheader != null) {
                        HeaderElement[] codecs = ceheader.getElements();
                        for (int i = 0; i < codecs.length; i++) {
                            if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                                response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                                return;
                            }
                        }
                    }
                }
            });
            return httpClient;
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    // 将传入的键/值对参数转换为NameValuePair参数集
    private static List<NameValuePair> getParamsList(Map<String, String> paramsMap) {
        // 若post参数为空，则添加一个空的参数
        if (paramsMap == null || paramsMap.size() == 0) {
            paramsMap = new HashMap();
            paramsMap.put("", "");
        }
        return NetworkDataUtil.getParamsList(paramsMap);
    }

    // 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复
    private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            if (executionCount >= 3) {
                return false;
            }
            if (exception instanceof NoHttpResponseException) {
                return true;
            }
            if (exception instanceof SSLHandshakeException) {
                return false;
            }
            HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
            boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
            if (!idempotent) {
                return true;
            }
            return false;
        }
    };

    private static void abortConnection(final HttpRequestBase hrb, final HttpClient httpclient) {
        if (hrb != null) {
            hrb.abort();
        }
        if (httpclient != null) {
            httpclient.getConnectionManager().shutdown();
        }
    }

    // 获取联网接入点方式
    public static String[] getConnectType() {
        String[] type = new String[2];
        ConnectivityManager conManager = (ConnectivityManager) BaseApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取网络的连接情况
        NetworkInfo activeNetInfo = conManager.getActiveNetworkInfo();
        NetworkInfo nowNetInfo;
        if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            // 判断WIFI网
            nowNetInfo = conManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            type[0] = (nowNetInfo.getTypeName() == null) ? "" : nowNetInfo.getTypeName().toLowerCase();
            type[1] = (nowNetInfo.getExtraInfo() == null) ? "" : nowNetInfo.getExtraInfo().toLowerCase();
        } else if (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            // 判断3G网
            nowNetInfo = conManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            type[0] = (nowNetInfo.getTypeName() == null) ? "" : nowNetInfo.getTypeName().toLowerCase();
            type[1] = (nowNetInfo.getExtraInfo() == null) ? "" : nowNetInfo.getExtraInfo().toLowerCase();
        }
        return type;
    }

}
