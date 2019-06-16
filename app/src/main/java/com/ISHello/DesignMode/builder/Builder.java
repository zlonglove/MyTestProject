package com.ISHello.DesignMode.builder;

/**
 * 1)定义一个静态内部类Builder，内部的成员变量和外部类一样
 * 2)Builder类通过一系列的方法用于成员变量的赋值，并返回当前对象本身（this）
 * 3)Builder类提供一个build方法或者create方法用于创建对应的外部类，该方法内部调用了外部类的一个私有构造函数，
 * 该构造函数的参数就是内部类Builder
 * 4)外部类提供一个私有构造函数供内部类调用，在该构造函数中完成成员变量的赋值，取值为Builder对象中对应的值
 */
public class Builder {
    public void test() {
        Person person = new Person.Builder()
                .name("张三")
                .age(18)
                .height(178.5)
                .weight(67.4)
                .build();
        person.getAge();
    }

    /**
     AlertDialog.Builder builder=new AlertDialog.Builder(this);
     AlertDialog dialog=builder.setTitle("标题")
     .setIcon(android.R.drawable.ic_dialog_alert)
     .setView(R.layout.myview)
     .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
    @Override public void onClick(DialogInterface dialog, int which) {

    }
    })
     .setNegativeButton(R.string.negative, new DialogInterface.OnClickListener() {
    @Override public void onClick(DialogInterface dialog, int which) {

    }
    })
     .create();
     dialog.show();
     */
}
