package com.ISHello.DesignMode;


/**
 * 单例模式
 * 1)必须防止外部可以调用构造函数进行实例化，因此构造函数必须私有化。
 * 2)必须定义一个静态函数获得该单例
 * 3)单例使用volatile修饰
 * 4)使用synchronized 进行同步处理，并且双重判断是否为null，我们看到synchronized (Singleton.class)
 * 里面又进行了是否为null的判断，这是因为一个线程进入了该代码，如果另一个线程在等待，
 * 这时候前一个线程创建了一个实例出来完毕后，另一个线程获得锁进入该同步代码，实例已经存在，
 * 没必要再次创建，因此这个判断是否是null还是必须的
 */
public class Singleton {
    private static volatile Singleton instance;

    private Singleton() {

    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
