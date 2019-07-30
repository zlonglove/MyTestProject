package com.ISHello.Reflection.learn;

import com.ISHello.Reflection.TestOperate;

public class Test {
    public static void main(String[] args) {
        ReflectClass reflectClass = new ReflectClass();
        reflectClass.reflectNewInstance();
        reflectClass.reflectPrivateConstructor();
        reflectClass.reflectPrivateField();
        reflectClass.reflectPrivateMethod();
        reflectClass.reflectPublicMethod();
        reflectClass.getAllMethod();
        reflectClass.getParaentAllMethod();

        TestOperate testOperate=new TestOperate();
        testOperate.add(1,2);
    }
}
