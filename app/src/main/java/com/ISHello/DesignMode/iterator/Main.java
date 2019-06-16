package com.ISHello.DesignMode.iterator;

/**
 * 迭代器模式结构很简单，就是提供一个list的遍历方法。目的很明确，弱化遍历算法和容器之间的关系。
 * 其实我们平时开发之中已经很少需要自己去实现迭代器，因为现在不管是Object，C++，python等等，他们直接实现了这种结构。
 * 但是理解这种设计模式也是不错的。像Android源码中数据库中的游标Cursor也是用的这种结构。
 */
public class Main {
    public static void main(String[] args) {
        ContainerImpl<String> containerImpl = new ContainerImpl<>();
        containerImpl.add("jack");
        containerImpl.add("mom");
        containerImpl.add("dad");
        containerImpl.add("john");
        // 根据容器创建一个迭代器
        Iterator<String> iterator = containerImpl.iterator();
        // 迭代器提供遍历方法，遍历
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
