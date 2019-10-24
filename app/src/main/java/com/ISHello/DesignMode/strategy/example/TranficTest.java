package com.ISHello.DesignMode.strategy.example;

/**
 * @author zhanglong
 */
public class TranficTest {

    public static void main(String[] args) {
        TranficCalcutor mTranficCalcutor = new TranficCalcutor();
        mTranficCalcutor.setStrategy(new BusStrategy());
        System.out.println("公交车计价为:" + mTranficCalcutor.calculatePrice(16));

        mTranficCalcutor.setStrategy(new SubwayStrategy());
        System.out.println("地铁计价为:" + mTranficCalcutor.calculatePrice(16));
    }
}
