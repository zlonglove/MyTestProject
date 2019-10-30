package com.ISHello.DesignMode.strategy.example;

/**
 * 车费计算类
 */
public class TranficCalcutor {
    private CalculateStrategy calculateStrategy;

    /**
     * 设置计费策略
     * @param calculateStrategy
     */
    public void setStrategy(CalculateStrategy calculateStrategy) {
        this.calculateStrategy = calculateStrategy;
    }

    /**
     * 计算费用
     * @param km
     * @return
     */
    public int calculatePrice(int km) {
        return calculateStrategy.calculatePrice(km);
    }
}
