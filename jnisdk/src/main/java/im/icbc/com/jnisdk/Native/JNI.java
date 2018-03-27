package im.icbc.com.jnisdk.Native;

import android.util.Log;

import java.util.ArrayList;

import im.icbc.com.jnisdk.Student;


public class JNI {
    private static JNI jni;

    /**
     * 实例域
     */
    private String instanceField = "Instance Field";
    private int intInstance = 10;
    /**
     * 静态域
     */
    private static String staticField = "Static Field";

    static {
        System.loadLibrary("myproject-jni");
    }

    /**
     * 单例模式
     */
    private JNI() {
        init();
    }

    public static JNI getInstance() {
        if (jni == null) {
            jni = new JNI();
        }
        return jni;
    }

    public int getInt() {
        return intInstance;
    }

    public String instanceMethod() {
        return "Instance Method";
    }

    public static String staticMethod() {
        return "Static Method";
    }

    public int IntInstanceMethod(int value) {
        return value;
    }

    public void throwingMethod() throws NullPointerException {
        throw new NullPointerException("NULL Pointer");
    }

    /**
     * 下面两个是JNI调用Java的回调方法
     *
     * @param num
     */
    public void callBackJava(int num) {
        Log.i("JNI", "--->java call back fuction " + num);
    }

    static public void callBackFromJNI(int cmd, String value, String data) {
        Log.i("JNI", "--->callBackFromJNI cmd==" + cmd + " value==" + value
                + " data==" + data);
    }

    public native void init();

    public native void Release();

    public native String stringFromJNI();

    public native void stringPutJNI(String str);

    public native int sumArray(int[] arry);

    public native void setStringArray(String[] str);

    public native String[] getStringArray();

    public native Student getStudentInfo();

    public native ArrayList<Student> getListStudents();

    public native void setStudentInfo(Student student);

    public native void writeFile(String buffer);

    public native String readFile();

    public native String getInstanceField();

    public native String getStaticField();

    public native int getIntField();

    public native String getInstanceMethod();

    public native String getStaticMethod();

    public native int GetIntInstanceMethod();

    public native void accessMethod();

    public native void throwException();

    public native void syncThread();

    public native void startThread();

    public native String dynamicRegister();

    public native String dynamicRegister2(int i);

}
