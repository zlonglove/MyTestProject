package com.ISHello.utils;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by kfzx-zhanglong on 2016/9/20.
 * Company ICBC
 */
public class MySSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);
        X509TrustManager tm = new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        };
        this.sslContext.init((KeyManager[]) null, new TrustManager[]{tm}, (SecureRandom) null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return this.sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return this.sslContext.getSocketFactory().createSocket();
    }

    public static KeyStore getKeystore() {
        KeyStore trustStore = null;

        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load((InputStream) null, (char[]) null);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return trustStore;
    }

    public static SSLSocketFactory getFixedSocketFactory() {
        Object socketFactory;
        try {
            socketFactory = new MySSLSocketFactory(getKeystore());
            ((SSLSocketFactory) socketFactory).setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Throwable var2) {
            var2.printStackTrace();
            socketFactory = SSLSocketFactory.getSocketFactory();
        }

        return (SSLSocketFactory) socketFactory;
    }
}
