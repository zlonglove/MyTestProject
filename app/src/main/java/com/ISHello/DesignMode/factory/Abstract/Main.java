package com.ISHello.DesignMode.factory.Abstract;

public class Main {
    public static void main(String[] args){
        CardFactory Q3Factory=new Q3Factory();
        Q3Factory.createTire().tire();
        Q3Factory.createEngine().engine();
        Q3Factory.createBrake().brake();
        System.out.println("----------------------------");

        CardFactory Q7Factory=new Q7Factory();
        Q7Factory.createTire().tire();
        Q7Factory.createEngine().engine();
        Q7Factory.createBrake().brake();
    }
}
