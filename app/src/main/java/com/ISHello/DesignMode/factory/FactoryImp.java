package com.ISHello.DesignMode.factory;

public class FactoryImp extends Factory {
    /**
     * @param clz 产品对象类型
     * @param <T>
     * @return 具体产品
     */
    @Override
    public <T extends Product> T createProduct(Class<T> clz) {
        Product p = null;
        try {
            p = (Product) Class.forName(clz.getName()).newInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return (T) p;
    }
}
