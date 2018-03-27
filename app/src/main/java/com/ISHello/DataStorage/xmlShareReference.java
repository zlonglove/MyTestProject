package com.ISHello.DataStorage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 几点说明： 1. SharedPreferences的获取有两种方法：
 * 一是上面提到的通过Activity自带（本质来讲是Context的）的getSharedPreferences方法，
 * 可以得到SharedPreferences对象。这种方法的好处是可以指定保存的xml文件名。 另一种是通过PreferenceManager
 * .getSharedPreferences(Context)获取SharedPreferences对象。
 * 这种方法不能指定保存的xml文件名，文件名使用默认的：<package name>+"_preferences.xml"的形式，
 * 不过如果在一个包里面采用这种方式需要保存多个这样的xml文件，可能会乱掉。建议采用第一种指定xml文件名的形式。 2.
 * 数据的存入必须通过SharedPreferences对象的编辑器对象Editor来实现， 存入（put）之后与写入数据库类似一定要commit
 */
public class xmlShareReference {
    final String DEFAULTNAME = "userinfo";
    String fileName;
    Activity activity;

    public xmlShareReference(Activity activity, String name) {
        this.fileName = name;
        this.activity = activity;
    }

    public xmlShareReference(Activity activity) {
        this.fileName = DEFAULTNAME;
        this.activity = activity;
    }

    public void put(String name, int age, String city) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(
                fileName, Context.MODE_PRIVATE).edit();
        /**
         * 将内容添加到编辑器
         */
        editor.putString("name", name);
        editor.putInt("age", age);
        editor.putString("city", city);
        /**
         * 提交编辑内容
         */
        editor.commit();

    }

    public void get() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        int age = sharedPreferences.getInt("age", 0);
        String city = sharedPreferences.getString("city", "");
        System.out.println("---->The sharedpreference name=" + name + " age=="
                + age + " city==" + city);
    }
}
