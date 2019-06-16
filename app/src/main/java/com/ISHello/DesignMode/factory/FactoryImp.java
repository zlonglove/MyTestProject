package com.ISHello.DesignMode.factory;

public class FactoryImp extends Factory{
    @Override
    public <T extends Product> T createProduct(Class<T> clz) {
        Product p = null;
        try {
            p = (Product) Class.forName(clz.getName()).newInstance();

        }catch (Exception e) {
            e.printStackTrace();
        }

        return (T) p;
    }
}
