package com.ISHello.Manager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;

public class MobileDataManager {
    private Context context;

    public MobileDataManager(Context context) {
        this.context = context;
    }

    /**
     * Set Mobile Data on/off
     *
     * @param flag ---true-on ---false-off
     */
    public void setMobileDataEnabled(boolean flag) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // connectivityManager.setMobileDataEnabled(true);
        Class<?> connectivityManagerClass = null;
        Field connectivityManagerField = null;
        Object connectivityManagerObject = null;
        Method setMobileDataEnableMethod = null;
        try {
            // 取得ConnectivityManager类
            connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
            // 取得ConnectivityManager类中的成员变量mService
            connectivityManagerField = connectivityManagerClass.getDeclaredField("mService");
            // 取消访问私有字段的合法检查， java.lang.reflect.AccessibleObject
            connectivityManagerField.setAccessible(true);

            // 实例化mService,注意get()方法的参数，他是mService所属类的对象
            connectivityManagerObject = connectivityManagerField.get(connectivityManager);
            // 得到mService所属接口的Class
            connectivityManagerClass = Class.forName(connectivityManagerObject.getClass().getName());
            // 获取IConnectivityManager接口中的setMobileDataEnable(boolean)方法
            Class<?>[] parameterTypes = new Class[1];
            parameterTypes[0] = Boolean.TYPE;
            setMobileDataEnableMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", parameterTypes);
            // 取消访问稀有方法的合法性检查
            setMobileDataEnableMethod.setAccessible(true);
            // 调用IConnectivityManager(mService)的setMobileDataEnabled方法
            setMobileDataEnableMethod.invoke(connectivityManagerObject, flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get Mobile is on/off status
     *
     * @return true-on,false-off
     */
    public boolean getMobileDataEnabled() {
        boolean mobileStatus = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // connectivityManager.setMobileDataEnabled(true);
        Class<?> connectivityManagerClass = null;
        Field connectivityManagerField = null;
        Object connectivityManagerObject = null;
        Method setMobileDataEnableMethod = null;
        try {
            // 取得ConnectivityManager类
            connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
            // 取得ConnectivityManager类中的成员变量mService
            connectivityManagerField = connectivityManagerClass.getDeclaredField("mService");
            // 取消访问私有字段的合法检查， java.lang.reflect.AccessibleObject
            connectivityManagerField.setAccessible(true);

            // 实例化mService,注意get()方法的参数，他是mService所属类的对象
            connectivityManagerObject = connectivityManagerField.get(connectivityManager);
            // 得到mService所属接口的Class
            connectivityManagerClass = Class.forName(connectivityManagerObject.getClass().getName());
            // 获取IConnectivityManager接口中的getDeclaredMethod()方法
            setMobileDataEnableMethod = connectivityManagerClass.getDeclaredMethod("getMobileDataEnabled");
            // 取消访问稀有方法的合法性检查
            setMobileDataEnableMethod.setAccessible(true);
            // 调用IConnectivityManager(mService)的getMobileDataEnabled方法
            mobileStatus = (Boolean) setMobileDataEnableMethod.invoke(connectivityManagerObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mobileStatus;
    }
    /**
     * 反射的知识 1)Class对象获取的三种方式 a.Object的getClass方法 b.使用.class的方式 c.Class.forName
     * 2)
     */
}
