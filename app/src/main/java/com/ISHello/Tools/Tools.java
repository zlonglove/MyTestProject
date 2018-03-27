package com.ISHello.Tools;

public interface Tools {

    /**
     * @return the Android API level on Android or 0 on the desktop.
     */
    public int getVersion();

    /**
     * @return the Java heap memory use in bytes
     */
    public long getJavaHeap();

    /**
     * @return the Native heap memory use in bytes
     */
    public long getNativeHeap();

    public int getMaxMemoryKB();

    public int getMaxMemoryM();

    /**
     * kill the application
     */
    public void killProcess();

    /**
     * 获取opengl的版本号
     *
     * @return
     */
    public String getGlEsVersion();

    public int getGlEsVersionInt();

    public boolean isGlEs2Supported();

    /**
     * load a dynamic library
     *
     * @param libName eg-libmyfirstjni.so
     */
    public void loadDynamicLibrary(String libName);

    public int getWidth();

    public int getHeight();

    public float getDensity();
}
