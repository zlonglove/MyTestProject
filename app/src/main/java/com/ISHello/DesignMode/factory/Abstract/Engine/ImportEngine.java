package com.ISHello.DesignMode.factory.Abstract.Engine;

public class ImportEngine implements IEngine {
    @Override
    public void engine() {
        System.out.println("进口发动机");
    }
}
