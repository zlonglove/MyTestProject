package com.ISHello.DesignMode.strategy.example;

/**
 * 车费计算类
 */
public class TranficCalcutor {
    private CalculateStrategy calculateStrategy;

    public void setStrategy(CalculateStrategy calculateStrategy) {
        this.calculateStrategy = calculateStrategy;
    }

    public int calculatePrice(int km) {
        return calculateStrategy.calculatePrice(km);
    }
}
