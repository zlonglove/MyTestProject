package com.ISHello.DesignMode.factory;

public class Main {
    /**
     * 1）工厂方法模式通过依赖抽象来达到解耦的效果，并且将实例化的任务交给子类去完成，有非常好的扩展性
     * 2）工厂方法模式用于生成比较复杂的对象。像我们上面的例子中一样，当我们有多个产品需要，
     * 我们可以把产品共性的地方抽象出来，然后利用工厂方法生产具体需要的产品
     * 3）工厂方法模式的应用非常广泛，然而缺点也很明显，就是每次我们为工厂方法添加新的产品时，
     * 都需要编写一个新的产品类，所以要根据实际情况来权衡是否要用工厂方法模式
     *
     * @param args
     */
    public static void main(String[] args) {
        Factory factory = new FactoryImp();
        Product productA = factory.createProduct(ProductA.class);
        Product productB = factory.createProduct(ProductB.class);
        productA.method();
        productB.method();
    }
}
