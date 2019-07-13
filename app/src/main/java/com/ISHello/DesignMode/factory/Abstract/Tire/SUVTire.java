package com.ISHello.DesignMode.factory.Abstract.Tire;

public class SUVTire implements ITire {
    @Override
    public void tire() {
        System.out.println("越野轮胎");
    }
}
