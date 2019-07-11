package com.ISHello.DesignMode.singleton;

import im.icbc.cn.keyboard.utils.LogUtils;

/**
 * 1.确保线程安全
 * 2.保证单例对象唯一性
 * 3.延迟了单例对象的实例化
 */
public class Singleton1 {

    private Singleton1() {

    }

    public static Singleton1 getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static final Singleton1 sInstance = new Singleton1();
    }

    public void doSomething(){
        LogUtils.d("do some thing");
    }
}
