package com.ISHello.DesignMode.factory.Abstract.Brake;

public class SeniorBrake implements IBrake {
    @Override
    public void brake() {
        System.out.println("高级制动");
    }
}
