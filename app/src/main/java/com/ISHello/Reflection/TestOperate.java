package com.ISHello.Reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestOperate {
    public void add(int param1, int param2) {

        Class<?> classType;
        try {
            // 获取相应的类对象名称
            classType = Class.forName("com.ISHello.Reflection.operationClass");
            // 如果知道类名并且类名存在于我们工程中，即jar 文件中包含可以使用如下写法
            // Class<?> classType = operationClass.class;
            // 返回本类对象
            Object invokeOperation = classType.newInstance();

            Field operationField = classType.getDeclaredField("name");
            operationField.setAccessible(true);
            String name = (String) operationField.get(invokeOperation);
            System.out.println("--->name value==" + name);

            // Method addMethod = classType.getMethod("add", new Class[] {
            // int.class, int.class });

            Method addMethod = classType.getDeclaredMethod("add", new Class[]{int.class, int.class});
            addMethod.setAccessible(true);
            // 调用查找 到的方法执行此方法的处理
            // float result = (Float) addMethod.invoke(invokeOperation, new
            // Object[] { new Integer(param1), new Integer(param2) });
            float result = (Float) addMethod.invoke(invokeOperation, new Object[]{param1, param2});
            System.out.println("--->add value==" + result);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

}
