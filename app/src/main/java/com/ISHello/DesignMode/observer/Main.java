package com.ISHello.DesignMode.observer;

/**
 * 定义对象间的一种一对多的依赖关系，当一个对象的状态发送改变时，所有依赖于它的对象都能得到通知并被自动更新
 */
public class Main {
    public static void main(String[] args) {
        Observable<Weather> observable = new Observable<Weather>();
        Observer<Weather> observer1 = new Observer<Weather>() {
            @Override
            public void onUpdate(Observable<Weather> observable, Weather data) {
                System.out.println("观察者1：" + data.toString());
            }
        };
        Observer<Weather> observer2 = new Observer<Weather>() {
            @Override
            public void onUpdate(Observable<Weather> observable, Weather data) {
                System.out.println("观察者2：" + data.toString());
            }
        };

        observable.register(observer1);
        observable.register(observer2);


        Weather weather = new Weather("晴转多云");
        observable.notifyObservers(weather);

        Weather weather1 = new Weather("多云转阴");
        observable.notifyObservers(weather1);


        observable.unregister(observer1);

        Weather weather2 = new Weather("台风");
        observable.notifyObservers(weather2);

    }
}
