package com.ISHello.webView;

/**
 * Created by zhanglong on 2016/12/6.
 */

public interface NativeWebviewBaseProxy {
    void startFunction();

    void startFunction(String params);

    void callNativeMethod(String params);

    void goBack();

    void showIndicator();

    void hideIndicator();

    void close();

    void setTitle(String title);
}
