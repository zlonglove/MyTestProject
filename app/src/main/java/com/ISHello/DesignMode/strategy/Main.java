package com.ISHello.DesignMode.strategy;

/**
 * 策略模式
 */
public class Main {
    public static void main(String[] args){
        TravelContext travelContext=new TravelContext();

        travelContext.setStrategy(new PlaneStrategy());
        travelContext.travel();

        travelContext.setStrategy(new WalkStrategy());
        travelContext.travel();

        travelContext.setStrategy(new SubwayStrategy());
        travelContext.travel();
    }
}
