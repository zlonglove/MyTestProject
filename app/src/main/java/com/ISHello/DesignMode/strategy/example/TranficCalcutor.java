package com.ISHello.DesignMode.strategy.example;

public class TranficCalcutor {
    CalculateStrategy calculateStrategy;

    public static void main(String[] args) {
        TranficCalcutor tranficCalcutor = new TranficCalcutor();
        tranficCalcutor.setStrategy(new BusStrategy());
        System.out.println("公交车计价为:" + tranficCalcutor.calculatePrice(16));

        tranficCalcutor.setStrategy(new SubwayStrategy());
        System.out.println("地铁计价为:" + tranficCalcutor.calculatePrice(16));
    }

    public void setStrategy(CalculateStrategy calculateStrategy) {
        this.calculateStrategy = calculateStrategy;
    }

    public int calculatePrice(int km) {
        return calculateStrategy.calculatePrice(km);
    }
}
