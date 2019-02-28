package com.ISHello.Retrofit;

//这是一个回调借口
public interface CallBack<T> {
    void onSuccess(T callBack);
    void onError(String info);
}