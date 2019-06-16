package com.ISHello.DesignMode.iterator;

import java.util.ArrayList;
import java.util.List;

/**
 * 迭代器核心其实就是给带入的list提供一个遍历的方法。
 *
 * @param <T>
 */
public class IteratorImpl<T> implements Iterator {
    private List<T> list = new ArrayList<>();
    private int cursor = 0;

    public IteratorImpl(List<T> list) {
        this.list = list;
    }

    @Override
    public boolean hasNext() {
        return cursor != list.size();
    }

    @Override
    public T next() {
        T obj = null;
        if (this.hasNext()) {
            obj = this.list.get(cursor++);
        }
        return obj;
    }
}
