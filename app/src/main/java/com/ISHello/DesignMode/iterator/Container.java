package com.ISHello.DesignMode.iterator;

public interface Container<T> {
    void add(T obj);
    void remove(T obj);
    Iterator<T> iterator();
}
