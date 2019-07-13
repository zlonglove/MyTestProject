package com.ISHello.DesignMode.factory.Abstract.Tire;

public class NomalTire implements ITire {
    @Override
    public void tire() {
        System.out.println("普通轮胎");
    }
}
