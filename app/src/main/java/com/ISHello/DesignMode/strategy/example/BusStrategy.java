package com.ISHello.DesignMode.strategy.example;

/**
 * 公交车计价策略
 */
public class BusStrategy implements CalculateStrategy {
    /**
     * 北京市公交车，十公里之内1元钱，超过10公里每增加一元可乘5公里
     *
     * @param km 公里
     * @return
     */
    @Override
    public int calculatePrice(int km) {
        if (km <= 10) {
            return 1;
        }
        /**
         * 超过10公里的总距离
         */
        int extraTotal = km - 10;
        /**
         * 超过距离是5公里的倍数
         */
        int extraFactor = extraTotal / 5;
        /**
         * 超过的公里对5公里取余
         */
        int fraction = extraTotal % 5;

        int price = 1 + extraFactor * 1;


        return fraction > 0 ? ++price : price;
    }
}
