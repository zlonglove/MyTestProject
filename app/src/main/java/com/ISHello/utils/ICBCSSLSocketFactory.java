package com.ISHello.utils;

import android.util.Log;

import com.ISHello.Constants.Constants;

import java.io.IOException;

import java.net.Socket;

import java.net.UnknownHostException;

import java.security.KeyManagementException;

import java.security.KeyStore;

import java.security.KeyStoreException;

import java.security.NoSuchAlgorithmException;

import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;

import javax.net.ssl.TrustManager;

import javax.net.ssl.X509TrustManager;

import java.security.cert.CertificateException;

import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.conn.ssl.SSLSocketFactory;

/**
 * @author kfzx-zhanglong
 */

public class ICBCSSLSocketFactory extends SSLSocketFactory {

    SSLContext sslContext = SSLContext.getInstance("TLS");

    public ICBCSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);
        TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                if (Constants.MODE == Constants.ModeType.Test) {//测试环境不验证证书
                    return;
                }/*
                if (chain.length >= 1) {
                    X509Certificate cer = chain[0];
                    String cerTmp = cer.getSubjectDN().getName();
                    Pattern pattern = Pattern.compile("O=(.+?),");
                    Matcher matcher = pattern.matcher(cerTmp);
                    if (matcher.find()) {
                        String result = matcher.group(1);
                        if (result.equalsIgnoreCase("Industrial and Commercial Bank of China Limited")) {
                            //LogUtil.log("Cer1", "校验通过");
                            Log.i("Cer1", "--->校验通过");
                            return;
                        } else {
                            throw new CertificateException("CertificateError");
                        }
                    } else {
                        throw new CertificateException("CertificateError");
                    }
                }*/
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        sslContext.init(null, new TrustManager[]{tm}, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}
