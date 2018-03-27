package com.ISHello.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ISHello.CustomToast.CustomToast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 常量配置/公用方法
 *
 * @author zhanglong
 */
public class AppUtils {
    private final static String TAG = "AppUtils";

    /**
     * 手机堆大小
     *
     * @param context
     * @return最大堆大小(M为单位)
     */
    public int getLargeMemory(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int heapSize = manager.getLargeMemoryClass();
        return heapSize;
    }

    /**
     * 关闭系统的软键盘
     *
     * @author zhanglong
     * @update 2012-7-4 下午2:34:34
     */
    public static void closeInput(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 检测某app是否安装
     *
     * @param context
     * @param packageName
     * @return boolean
     * @since 2015/8/16
     */
    public static boolean isInstallApp(Context context, String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        } catch (NameNotFoundException e) {
            packageInfo = null;
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检测app里是否有某一个服务
     *
     * @param context
     * @param ServicePackageName
     * @return
     */
    public static boolean isInstallService(Context context, String ServicePackageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(ServicePackageName, PackageManager.GET_SERVICES);
        } catch (NameNotFoundException e) {
            packageInfo = null;
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检查3G网络 0 无网络； 1 3G网络；2WiFi
     *
     * @return
     */
    public static int checkNetworkInfo(Activity activity) {
        ConnectivityManager conMan = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        // mobile 3G Data Network
        State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        // wifi
        State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        // 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接

        if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
            return 1;
        } else if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
            return 2;
        }

        return 0;

    }

    /**
     * @param path
     * @return
     */
    public static String getDir(String path) {
        String subString = path.substring(0, path.lastIndexOf('/'));
        return subString.substring(subString.lastIndexOf('/') + 1, subString.length());
    }

    /**
     * Get Version Name
     *
     * @param activity
     * @return
     */
    public static String getVersionName(Activity activity) {
        PackageManager manager = activity.getPackageManager();
        String packageName = activity.getPackageName();
        try {
            PackageInfo info = manager.getPackageInfo(packageName, 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            return "1.0";
        }
    }

    /**
     * If The App Is Running Foreground
     *
     * @param context
     * @return
     */
    public static boolean isRunningForeground(Context context) {
        String packageName = getPackageName(context);
        String topActivityClassName = getTopActivityName(context);
        if (packageName != null && topActivityClassName != null && topActivityClassName.startsWith(packageName)) {
            Log.d(TAG, "---> isRunningForeGround");
            return true;
        } else {
            Log.d(TAG, "---> isRunningBackGround");
            return false;
        }
    }

    /**
     * Get The Top App From The Phone Activity Task
     *
     * @param context
     * @return
     */
    public static String getTopActivityName(Context context) {
        String topActivityClassName = null;
        ActivityManager activityManager = (ActivityManager) (context.getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityClassName = f.getClassName();
        }
        return topActivityClassName;
    }

    public static String getPackageName(Context context) {
        String packageName = context.getPackageName();
        return packageName;
    }

    /**
     * 获取当前的进程的名字
     *
     * @param context
     * @return
     */
    public String getProcessName(Context context) {
        String currentProcessName = "";
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                currentProcessName = processInfo.processName;
            }
        }
        return currentProcessName;
    }

    /**
     * Download From The Market
     *
     * @param activity
     * @param packageName
     */
    public static void marketDownload(Activity activity, String packageName) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse("market://details?id=" + packageName));
            activity.startActivity(intent);
        } catch (Exception e) {
            CustomToast.makeText(activity, "未找到安卓市场", Toast.LENGTH_SHORT);
        }

    }

    /**
     * Share
     *
     * @param activity
     * @param share_content
     * @param _packageName
     */
    @SuppressLint("DefaultLocale")
    public static void initShareIntent(Activity activity, String share_content, String _packageName) {
        boolean found = false;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = activity.getPackageManager().queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(_packageName) || info.activityInfo.name.toLowerCase().contains(_packageName)) {
                    share.putExtra(Intent.EXTRA_SUBJECT, "subject");
                    share.putExtra(Intent.EXTRA_TEXT, share_content);

                    // share.putExtra(Intent.EXTRA_STREAM,
                    // Uri.fromFile(new File(myPath))); // Optional, just
                    // // if you wanna
                    // // share an
                    // // image.
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            if (!found) {
                CustomToast.makeText(activity, "未能找到该分享应用", Toast.LENGTH_LONG);
                return;
            }
            activity.startActivity(Intent.createChooser(share, "选择"));
        }
    }

    /**
     * 分享
     *
     * @param activity
     * @param share_content
     */
    public static void initShareIntent(Activity activity, String share_content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        List<ResolveInfo> resInfo = activity.getPackageManager().queryIntentActivities(intent, 0);
        if (!resInfo.isEmpty()) {
            try {
                List<Intent> targetedShareIntents = new ArrayList<Intent>();
                for (ResolveInfo info : resInfo) {
                    Intent targeted = new Intent(Intent.ACTION_SEND);
                    targeted.setType("text/plain");
                    ActivityInfo activityInfo = info.activityInfo;

                    // judgments :activityInfo.packageName, activityInfo.name,
                    // etc.
                    // com.tencent.mm 微信
                    // com.qzone 空间
                    // com.tencent.WBlog 腾讯微博
                    // com.tencent.mobileqq qq
                    // com.renren.mobile.android 人人
                    // com.sina.weibo 人人
                    // im.yixin 易信
                    // jp.naver.line.android LINE
                    // com.xiaomi.channel 米聊
                    if (activityInfo.packageName.equals("com.tencent.WBlog") || activityInfo.packageName.equals("com.xiaomi.channel")
                            || activityInfo.packageName.equals("cn.com.fetion") || activityInfo.packageName.equals("jp.naver.line.android")) {
                        targeted.putExtra(Intent.EXTRA_TEXT, share_content);
                        targeted.setPackage(activityInfo.packageName);
                        targetedShareIntents.add(targeted);
                    }
                }
                Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "选择分享方式");
                if (chooserIntent == null) {
                    return;
                }
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
                activity.startActivity(chooserIntent);
            } catch (Exception ex) {
                // Toast.makeText(activity,
                // "Can't find sharecomponent to share",
                // Toast.LENGTH_SHORT).show();
                CustomToast.makeText(activity, "未能找到分享应用", Toast.LENGTH_LONG);
            }
        } else {
            // Toast.makeText(activity, "未找到分享应用", Toast.LENGTH_SHORT).show();
            CustomToast.makeText(activity, "未能找到分享应用", Toast.LENGTH_LONG);
        }
    }

    public static ProgressDialog createProgressDialog(Activity activity) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("玩命加载中...");
        progressDialog.show();
        return progressDialog;
    }

    /**
     * 从本地APK文件路径启动APK
     *
     * @param context
     * @param path    ---本地apk的路径
     */
    public static void InstallAPK(Context context, String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * Uninstall APK
     *
     * @param context
     * @param packageName
     */
    public static void uninstallAPK(Context context, String packageName) {
        Uri packageURI = Uri.parse("package:" + packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        context.startActivity(uninstallIntent);
    }

    /**
     * 是否运行在模拟器上
     *
     * @return[true-是,false-不是]
     */
    public static boolean isRunningInEmualtor() {
        boolean qemuKernel = false;
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("getprop ro.kernel.qemu");
            os = new DataOutputStream(process.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    process.getInputStream(), "GBK"));
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
            qemuKernel = (Integer.valueOf(in.readLine()) == 1);
        } catch (Exception e) {
            qemuKernel = false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {

            }
        }
        return qemuKernel;
    }

    public int getBytesPerPixel(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        } else if (config == Bitmap.Config.ARGB_4444) {
            return 2;
        } else if (config == Bitmap.Config.RGB_565) {
            return 2;
        } else if (config == Bitmap.Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }

    public static final String getComponentVersion() {
        return "3.6";
    }
}
