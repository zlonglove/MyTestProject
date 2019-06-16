package com.ISHello.DesignMode.observer;

import java.util.ArrayList;
import java.util.List;


public class Observable<T> {
    List<Observer<T>> mObservers = new ArrayList<Observer<T>>();

    public void register(Observer<T> observer) {
        if (observer == null) {
            throw new NullPointerException("observer == null");
        }
        synchronized (this) {
            if (!mObservers.contains(observer))
                mObservers.add(observer);
        }
    }

    public synchronized void unregister(Observer<T> observer) {
        mObservers.remove(observer);
    }

    public void notifyObservers(T data) {
        for (Observer<T> observer : mObservers) {
            observer.onUpdate(this, data);
        }
    }
}
