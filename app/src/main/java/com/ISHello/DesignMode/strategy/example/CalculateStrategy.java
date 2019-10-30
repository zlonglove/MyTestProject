package com.ISHello.DesignMode.strategy.example;

/**
 * 计费接口
 */
public interface CalculateStrategy {
    /**
     * 按距离来计算价格
     *
     * @param km 公里
     * @return 价格
     */
    int calculatePrice(int km);
}
