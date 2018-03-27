package com.example.updateversion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ISHello.CustomToast.CustomToast;
import com.ISHello.Service.AsyncTaskDownloadServicesOther;
import com.ISHello.View.ICBCDialog;
import com.ISHello.utils.AppUtils;
import com.ISHello.utils.SdcardUtils;

import java.io.File;

/**
 * @author kfzx-zhanglong
 */
public class LaunchAppServices {
    private final String TAG = "LaunchAppServices";

    private Context context;
    // 包名
    private String packageName;
    // APK下载地址
    private String downloadAPKUrl;
    // 下载页面地址
    private String downloadPageUrl;
    // apk名字
    private String apkName;
    // 应用程序名字
    private String appName;

    private Intent intent;

    public LaunchAppServices(Context context, String appName, String packageName, String downloadAPKUrl, String apkName) {
        this.context = context;
        this.packageName = packageName;
        this.downloadAPKUrl = downloadAPKUrl;
        this.apkName = apkName;
        this.appName = appName;
    }

    public void launchApp() {
        if (AppUtils.isInstallApp(context, packageName)) {
            if (intent == null) {
                intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } else if (!TextUtils.isEmpty(downloadAPKUrl)) {
            final AlertDialog dialog = ICBCDialog.getTipsDialog(context, "请确认下载安装“" + appName + "”软件");
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "下载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    downFile();
                }
            });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (!TextUtils.isEmpty(downloadPageUrl)) {
            // 外部浏览器打开下载页面
            Uri uri = Uri.parse(downloadPageUrl);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
        }
    }

    /**
     * 下载-启动
     */
    public void downLoadApk() {
        if (!TextUtils.isEmpty(downloadAPKUrl)) {
            Log.i(TAG, "--->DownLoad APK,Url==" + downloadAPKUrl);
            final AlertDialog dialog = ICBCDialog.getTipsDialog(context, "请确认下载安装软件");
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "下载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    downFile();
                }
            });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public void launchAppAndFinishActivity() {
        if (AppUtils.isInstallApp(context, packageName)) {
            Log.i(TAG, "--->App Is Install");
            if (intent == null) {
                intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
            if (context instanceof Activity) {
                ((Activity) context).finish();// finish调Context对应的Activity
            } else {
                Log.e(TAG, "--->context is not a activity");
            }
        } else if (!TextUtils.isEmpty(downloadAPKUrl)) {
            Log.i(TAG, "--->DownLoad APK");
            final AlertDialog dialog = ICBCDialog.getTipsDialog(context, "请确认下载安装“" + appName + "”软件");
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "下载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    downFile();
                }
            });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (!TextUtils.isEmpty(downloadPageUrl)) {
            // 外部浏览器打开下载页面
            Uri uri = Uri.parse(downloadPageUrl);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
        }
    }

    /**
     * @param UriString ---要启动的App对应的URI
     */
    public void launchUriApp(String UriString) {
        if (AppUtils.isInstallApp(context, packageName)) {
            if (intent == null) {
                Uri uri = Uri.parse(UriString);
                intent = new Intent(Intent.ACTION_VIEW, uri);
            }
            context.startActivity(intent);
        } else if (!TextUtils.isEmpty(downloadAPKUrl)) {

            final AlertDialog dialog = ICBCDialog.getTipsDialog(context, "请确认下载安装“" + appName + "”软件");
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "下载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    downFile();
                }
            });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (!TextUtils.isEmpty(downloadPageUrl)) {
            // 外部浏览器打开下载页面
            Uri uri = Uri.parse(downloadPageUrl);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
        }
    }

    /**
     * 下载APK
     */
    private void downFile() {
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.arg1) {
                    case AsyncTaskDownloadServicesOther.DOWNLOAD_SUCCESS:
                        update();
                        break;
                    case AsyncTaskDownloadServicesOther.DOWNLOAD_FAIL:
                        failed();
                        break;
                    default:
                        break;
                }
            }
        };
        if (SdcardUtils.checkSDCard()) {
            if (SdcardUtils.getRealSizeOnSdcard() > 20 * 1024 * 1024) {// 确保SD卡的内存空间有20M以上
                AsyncTaskDownloadServicesOther ATDS = new AsyncTaskDownloadServicesOther(context, "软件下载", "程序下载中，请稍候...");
                ATDS.execute(downloadAPKUrl, Environment.getExternalStorageDirectory(), apkName, handler);
            } else {
                CustomToast.makeText(context, "SD卡空间不足,请清理后再下载", Toast.LENGTH_SHORT);
            }

        } else {
            CustomToast.makeText(context, "没有发现可用的SD卡", Toast.LENGTH_SHORT);
        }
    }

    /**
     * 下载失败提示
     */
    private void failed() {
        ICBCDialog.getDownloadFailedDialog(context);
    }

    /**
     * 下载完成启动App
     */
    private void update() {
        if (intent == null) {
            intent = new Intent(Intent.ACTION_VIEW);
        }
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), apkName)), "application/vnd.android.package-archive");
        context.startActivity(intent);

    }

}
