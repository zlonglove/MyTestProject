package com.ISHello.DesignMode.observer;

public interface Observer<T> {
    void onUpdate(Observable<T> observable,T data);
}
