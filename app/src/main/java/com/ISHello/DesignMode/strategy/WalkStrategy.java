package com.ISHello.DesignMode.strategy;

public class WalkStrategy implements Strategy {
    @Override
    public void travel() {
        System.out.println("walk");
    }
}
