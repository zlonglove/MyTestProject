package com.ISHello.NetWork;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class TestNetworkClient {
    private HttpParams httpParameters;
    private int timeoutConnection = 10000;// 服务器请求超时
    private int timeoutSocket = 10000;// 服务器响应超时
    private final String TAG = "TestNetworkClient";

    public void postValue(String url, String name, String password) {
        try {
            httpParameters = new BasicHttpParams();
            /**
             * 設置請求字符集
             */
            HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
            /**
             * 設置客戶端和服務器鏈接的超時時間
             */
            HttpConnectionParams.setConnectionTimeout(httpParameters,
                    timeoutConnection);
            /**
             * 設置服務器的响应超时时间
             */
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpPost post = new HttpPost(url);
            NameValuePair mloginName = new BasicNameValuePair("loginName", name);
            NameValuePair mpassword = new BasicNameValuePair("passwordName", password);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(mloginName);
            params.add(mpassword);
            try {
                post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            }
            String result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
            Log.i(TAG, "--->" + result);
        } catch (ConnectException connectException) {

        } catch (SocketException socketException) {

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {

        }
    }
}
