package com.ISHello.DesignMode.iterator;

import java.util.ArrayList;
import java.util.List;

public class ContainerImpl <T> implements Container<T>{
    private List<T> list = new ArrayList<>();
    @Override
    public void add(T obj) {
        list.add(obj);
    }

    @Override
    public void remove(T obj) {
        list.remove(obj);
    }

    @Override
    public Iterator<T> iterator() {
        return new IteratorImpl<>(list);
    }
}
