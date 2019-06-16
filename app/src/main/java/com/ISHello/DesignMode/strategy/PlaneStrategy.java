package com.ISHello.DesignMode.strategy;

public class PlaneStrategy implements Strategy {
    @Override
    public void travel() {
        System.out.println("plane");
    }
}
