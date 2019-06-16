package com.ISHello.DesignMode.factory;

public abstract class Factory {
    /**
     * @param clz 产品对象类型
     * @return 具体的产品类型
     */
    public abstract <T extends Product> T createProduct(Class<T> clz);
}
