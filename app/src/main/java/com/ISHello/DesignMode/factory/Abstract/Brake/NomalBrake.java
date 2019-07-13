package com.ISHello.DesignMode.factory.Abstract.Brake;

public class NomalBrake implements IBrake {
    @Override
    public void brake() {
        System.out.println("普通制动");
    }
}
