package com.ISHello.webView;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;

import com.ISHello.utils.LogUtil;

/**
 * Created by zhanglong on 2016/12/6.
 */

public class NativeWebviewCoreProxy implements NativeWebviewBaseProxy {
    private final String TAG = "NativeWebviewCoreProxy";

    public enum HandleType {
        SHOW_INDICATOR,
        HIDE_INDICATOR
    }

    private Activity mContext;
    /**
     * UI操作通知UI线程操作
     */
    private Handler mHandler;

    private CustomWebView mCustomWebView;

    public NativeWebviewCoreProxy(Activity mContext, CustomWebView mCustomWebView, Handler handler) {
        this.mContext = mContext;
        this.mCustomWebView = mCustomWebView;
        this.mHandler = handler;
    }

    @JavascriptInterface
    @Override
    public void startFunction() {
        LogUtil.log(TAG, "startFunction");
    }

    @JavascriptInterface
    @Override
    public void startFunction(String params) {
        LogUtil.log(TAG, "startFunction() call! params==" + params);
    }

    @JavascriptInterface
    @Override
    public void callNativeMethod(String params) {
        LogUtil.log(TAG, "callNativeMethod() params==" + params);
    }

    @JavascriptInterface
    @Override
    public void goBack() {
        if (mCustomWebView != null) {
            if (mCustomWebView.canGoBack()) {
                mCustomWebView.goBack();
            } else {
                mContext.finish();
            }
        }
    }

    @JavascriptInterface
    @Override
    public void showIndicator() {
        Message msg = Message.obtain();
        msg.obj = HandleType.SHOW_INDICATOR;
        mHandler.sendMessage(msg);
    }

    @JavascriptInterface
    @Override
    public void hideIndicator() {
        Message msg = Message.obtain();
        msg.obj = HandleType.HIDE_INDICATOR;
        mHandler.sendMessage(msg);
    }

    @JavascriptInterface
    @Override
    public void close() {
        if (mContext != null) {
            mContext.finish();
        }
    }
}
