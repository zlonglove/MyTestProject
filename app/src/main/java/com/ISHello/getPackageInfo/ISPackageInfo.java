package com.ISHello.getPackageInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

/**
 * 单例设计模式
 *
 * @author zhanglong
 */
public class ISPackageInfo {

    private static ISPackageInfo isPackageInfo;
    private final String TAG = "ISPackageInfo";
    private Context context;

    //未知软件类型   
    public static final int UNKNOW_APP = 0;
    //用户软件类型   
    public static final int USER_APP = 1;
    //系统软件   
    public static final int SYSTEM_APP = 2;
    //系统升级软件   
    public static final int SYSTEM_UPDATE_APP = 4;
    //系统+升级软件   
    public static final int SYSTEM_REF_APP = SYSTEM_APP | SYSTEM_UPDATE_APP;


    public static ISPackageInfo getInstance(Context context) {
        if (isPackageInfo == null) {
            isPackageInfo = new ISPackageInfo(context);
        }
        return isPackageInfo;
    }

    public ISPackageInfo(Context context) {
        super();
        this.context = context;
    }

    /**
     * get current app version code
     *
     * @return
     */
    public int getVersionCode() {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            Log.i(TAG, "--->The versionCode Name==" + info.versionCode);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionCode;
    }

    /**
     * get current app version Name
     *
     * @return
     */
    public String getVersionName() {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            Log.i(TAG, "--->The versionName Name==" + info.versionName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionName;
    }

    /**
     * get current app Package Name
     *
     * @return
     */
    public String getPackageName() {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            Log.i(TAG, "--->The Package Name==" + info.packageName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.packageName;
    }

    /**
     * Check APP Type
     *
     * @param pname
     * @return
     */
    public int checkAppType(String pname) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(pname, 0);

            if (isSystemApp(pInfo) || isSystemUpdateApp(pInfo)) {
                if (isSystemUpdateApp(pInfo) && isSystemUpdateApp(pInfo)) {
                    Log.i(TAG, "--->System And Update App");
                    return SYSTEM_REF_APP;
                } else if (isSystemUpdateApp(pInfo)) {
                    Log.i(TAG, "--->Update App");
                    return SYSTEM_UPDATE_APP;
                } else {
                    Log.i(TAG, "--->System App");
                    return SYSTEM_APP;
                }
            } else {
                Log.i(TAG, "--->User App ");
                return USER_APP;
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "--->Unknow App ");
        return UNKNOW_APP;
    }

    /**
     * @param pname---package name
     * @return
     */
    public boolean isSystemApp(String pname) {

        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(pname, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return isSystemApp(pInfo);
    }

    /**
     * @param context
     * @param pname
     * @return
     */
    public boolean isSystemApp(Context context, String pname) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(pname, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return isSystemApp(pInfo);
    }

    /**
     * @param pname---package name
     * @return
     */
    public boolean isUserApp(String pname) {

        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(pname, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return isUserApp(pInfo);
    }

    /**
     * @param context
     * @param pname
     * @return
     */
    public boolean isUserApp(Context context, String pname) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(pname, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return isUserApp(pInfo);
    }


    /**
     * if is System App
     *
     * @return
     */
    public boolean isSystemApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }


    /**
     * if is System update App
     *
     * @param pInfo
     * @return
     */
    public boolean isSystemUpdateApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
    }

    /**
     * if is user App
     *
     * @param pInfo
     * @return
     */
    public boolean isUserApp(PackageInfo pInfo) {
        return (!isSystemApp(pInfo) && !isSystemUpdateApp(pInfo));
    }


}
