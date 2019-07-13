package com.ISHello.DesignMode.factory.Abstract.Engine;

public class DomesticEngine implements IEngine {
    @Override
    public void engine() {
        System.out.println("国产发动机");
    }
}
