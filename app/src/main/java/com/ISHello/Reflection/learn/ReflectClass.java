package com.ISHello.Reflection.learn;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * JAVA反射机制是在运行状态中，对于任意一个类，都能够知道这个类的所有属性和方法；对于任意一个对象，都能够调用它的任意方法和属性；
 * 这种动态获取信息以及动态调用对象方法的功能称为java语言的反射机制
 */
public class ReflectClass {
    private final static String className = "com.ISHello.Reflection.learn.Book";

    // 创建对象
    public void reflectNewInstance() {
        try {
            Class<?> classBook = Class.forName(className);
            Object objectBook = classBook.newInstance();
            Book book = (Book) objectBook;
            book.setName("Android进阶之光");
            book.setAuthor("刘望舒");
            System.out.println("--->reflectNewInstance book = " + book.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 反射私有的构造方法
    public void reflectPrivateConstructor() {
        try {
            Class<?> classBook = Class.forName(className);
            Constructor<?> declaredConstructorBook = classBook.getDeclaredConstructor(String.class, String.class);
            declaredConstructorBook.setAccessible(true);
            Object objectBook = declaredConstructorBook.newInstance("Android开发艺术探索", "任玉刚");
            Book book = (Book) objectBook;
            System.out.println("--->reflectPrivateConstructor book = " + book.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 反射私有属性
    public void reflectPrivateField() {
        try {
            Class<?> classBook = Class.forName(className);
            Object objectBook = classBook.newInstance();
            Field fieldTag = classBook.getDeclaredField("TAG");
            fieldTag.setAccessible(true);
            fieldTag.set(objectBook,"11111111111111111111111");
            String tag = (String) fieldTag.get(objectBook);
            System.out.println("--->reflectPrivateField tag = " + tag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 反射私有方法[getDeclaredMethod-所有方法]
    public void reflectPrivateMethod() {
        try {
            Class<?> classBook = Class.forName(className);
            Method methodBook = classBook.getDeclaredMethod("declaredMethod", int.class);
            methodBook.setAccessible(true);
            Object objectBook = classBook.newInstance();
            String string = (String) methodBook.invoke(objectBook, 0);

            System.out.println("--->reflectPrivateMethod string = " + string);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // getMethod 只能反射public方法
    public void reflectPublicMethod() {
        try {
            Class<?> classBook = Class.forName(className);
            Method methodBook = classBook.getMethod("Method", int.class);
            methodBook.setAccessible(true);
            Object objectBook = classBook.newInstance();
            String string = (String) methodBook.invoke(objectBook, 1);

            System.out.println("--->reflectPublicMethod string = " + string);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getAllMethod() {
        try {
            Class<?> classBook = Class.forName(className);
            Method[] methodAll = classBook.getDeclaredMethods();
            for (Method method : methodAll) {
                System.out.println("--->method= " + method.toGenericString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getParaentAllMethod() {
        try {
            Class<?> classBook = Class.forName(className);
            Class<?> superclass = classBook.getSuperclass();
            Method[] methodAll = superclass.getDeclaredMethods();
            for (Method method : methodAll) {
                System.out.println("--->method= " + method.toGenericString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
