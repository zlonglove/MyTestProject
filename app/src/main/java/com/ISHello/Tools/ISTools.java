package com.ISHello.Tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * @author zhanglong
 */
public class ISTools implements Tools {
    private final String TAG = "ISTools";
    private static ISTools isTools;
    private Activity activity;
    private int sGlEsVersion = 0;
    private final String DATA_FOLDER;
    private final String LIBS_FOLDER;
    private DisplayMetrics displayMetrics = new DisplayMetrics();

    private ISTools(Activity activity) {
        this.activity = activity;
        DATA_FOLDER = String.format("/data/data/%s", activity.getPackageName());
        LIBS_FOLDER = String.format("%s/lib", this.DATA_FOLDER);
        this.activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
    }

    public static ISTools Instance(Activity activity) {
        if (isTools == null) {
            isTools = new ISTools(activity);
        }
        return isTools;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getVersion() {
        return Integer.parseInt(android.os.Build.VERSION.SDK);
    }

    @Override
    public long getJavaHeap() {
        return Runtime.getRuntime().totalMemory()
                - Runtime.getRuntime().freeMemory();
    }

    @Override
    public long getNativeHeap() {
        return Debug.getNativeHeapAllocatedSize();
    }

    @Override
    public void killProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public String getGlEsVersion() {
        return ((ActivityManager) activity
                .getSystemService(Context.ACTIVITY_SERVICE))
                .getDeviceConfigurationInfo().getGlEsVersion();
    }

    /**
     * reqGlEsVersion; The GLES version used by an application. The upper order
     * 16 bits represent the major version and the lower order 16 bits the minor
     * version.
     */
    public int getGlEsVersionInt() {
        if (sGlEsVersion == 0) {
            sGlEsVersion = ((ActivityManager) activity
                    .getSystemService(Context.ACTIVITY_SERVICE))
                    .getDeviceConfigurationInfo().reqGlEsVersion;
            Log.i(TAG, "--->sGlEsVersion=" + sGlEsVersion);
        }
        return sGlEsVersion;
    }

    public boolean isGlEs2Supported() {
        return getGlEsVersionInt() >= 0x20000;
    }

    @Override
    public int getMaxMemoryKB() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        return maxMemory;
    }

    @Override
    public int getMaxMemoryM() {
        return getMaxMemoryKB() / 1024;
    }

    @Override
    public void loadDynamicLibrary(String libName) {
        // TODO Auto-generated method stub
        System.load(String.format("%s/%s", this.LIBS_FOLDER, libName));
    }

    @Override
    public int getWidth() {
        // TODO Auto-generated method stub
        return displayMetrics.widthPixels;
    }

    @Override
    public int getHeight() {
        // TODO Auto-generated method stub
        return displayMetrics.heightPixels;
    }

    @Override
    public float getDensity() {
        // TODO Auto-generated method stub
        return displayMetrics.density;
    }

}
