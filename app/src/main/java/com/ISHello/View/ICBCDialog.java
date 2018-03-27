package com.ISHello.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.view.View;

import com.ISHello.Constants.Constants;
import com.ISHello.View.AlertDialogPro.Builder;
import com.example.ishelloword.R;


public class ICBCDialog extends CustomDialog {

    // 加载中对话框类型
    public enum ProgressDialogType {
        Normal, // 正常的加载中对话框
        Normal2, // 正常的加载中对话框
        NoBackground, // 无背景的加载中对话框
        NoBackground2, // 无背景的加载中对话框(下部)
        ICBC3DLogo// 带有3DLogo的加载中对话框
    }

    public static Dialog getProgressDialog(final Context context, ProgressDialogType type) {
        Dialog progressDialog = null;
        if (type == null || type == ProgressDialogType.Normal || type == ProgressDialogType.ICBC3DLogo) {
            progressDialog = new Dialog(context, R.style.ProgressDialog1);
            progressDialog.setContentView(R.layout.loading_indicator);
            progressDialog.setCancelable(false);
        } else if (type == ProgressDialogType.Normal2) {
            progressDialog = new Dialog(context, R.style.ProgressDialog2);
            progressDialog.setContentView(R.layout.loading_indicator4);
            progressDialog.setCancelable(false);
        } else if (type == ProgressDialogType.NoBackground) {
            progressDialog = new Dialog(context, R.style.ProgressDialog2);
            progressDialog.setContentView(R.layout.loading_indicator2);
            progressDialog.setCancelable(false);
        } else if (type == ProgressDialogType.NoBackground2) {
            progressDialog = new Dialog(context, R.style.ProgressDialog2);
            progressDialog.setContentView(R.layout.loading_indicator3);
            progressDialog.setCancelable(false);
        } else {
            progressDialog = new Dialog(context, R.style.ProgressDialog1);
            progressDialog.setContentView(R.layout.loading_indicator);
            progressDialog.setCancelable(false);
        }
        progressDialog.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                //ICBCPushService.LoadingHideIMFloatWindowFlag(context.getApplicationContext());
            }
        });
        progressDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface arg0) {
                //ICBCPushService.LoadingShowIMFloatWindowFlag(context.getApplicationContext());
            }
        });
        return progressDialog;
    }

    public static AlertDialog getDownloadFailedDialog(Context context) {
        Builder dialog = new AlertDialogPro.Builder(context, R.style.Theme_AlertDialogPro_ICBC);
        dialog.setTitle("程序下载");
        dialog.setMessage("网络错误，程序下载失败。");
        dialog.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return dialog.create();
    }

    // 登录页退出程序对话框
    public static AlertDialog getExitDialog(final Context context, String msg) {
        Builder ExitDialog = null;
        ExitDialog = new AlertDialogPro.Builder(context, R.style.Theme_AlertDialogPro_ICBC);
        ExitDialog.setTitle("退出程序");
        ExitDialog.setMessage(msg);
        ExitDialog.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                ((Activity) context).finish();
            }
        });
        ExitDialog.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.cancel();
                return;
            }
        });
        return ExitDialog.create();
    }

    // 程序崩溃对话框
    public static AlertDialog getCrashDialog(final Context context, final Throwable ex) {
        Builder CrashDialog;
        String msg = "很抱歉，程序出现异常，即将退出。";
        // 发送崩溃文件开关为开
        if (Constants.SendCrashFile) {
            msg += "\n是否发送异常诊断信息？";
        }
        CrashDialog = new AlertDialogPro.Builder(context, R.style.Theme_AlertDialogPro_ICBC);
        CrashDialog.setTitle("提示");
        CrashDialog.setMessage(msg);
        if (Constants.SendCrashFile) {
            CrashDialog.setPositiveButton("发送", new OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    //LogUtil.saveCrashInfoToFile(ex);
                    arg0.dismiss();
                    return;
                }
            });
            CrashDialog.setNegativeButton("取消", new OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.cancel();
                    return;
                }
            });
        } else {
            CrashDialog.setPositiveButton("确定", new OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
        }
        return CrashDialog.create();
    }

    // 自定义View的对话框
    public static AlertDialog getCustomViewDialog(final Context context, View view, String title) {
        Builder CustomDialog = null;
        CustomDialog = new AlertDialogPro.Builder(context, R.style.Theme_AlertDialogPro_ICBC);
        CustomDialog.setTitle(title);
        CustomDialog.setView(view);
        return CustomDialog.create();
    }

    /*// 跳转到完整版手机银行
    public static AlertDialog getMessageToFullVersionDialog(final Context context) {
        Builder MessageDialog = null;
        MessageDialog = new Builder(context, R.style.Theme_AlertDialogPro_ICBC);
        MessageDialog.setTitle("提示");
        MessageDialog.setMessage(context.getString(R.string.jump_menu_not_support_full));
        MessageDialog.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //ICBCLaunchAppServices.launchFullVersionApp(context);
            }
        });
        MessageDialog.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        return MessageDialog.create();
    }

    // 跳转到完整版手机银行
    public static AlertDialog getMessageToFullVersionDialogForQRCode(final Context context, final Handler handler) {
        Builder MessageDialog = null;
        MessageDialog = new AlertDialogPro.Builder(context, R.style.Theme_AlertDialogPro_ICBC);
        MessageDialog.setTitle("提示");
        MessageDialog.setMessage(context.getString(R.string.jump_menu_not_support_full));
        MessageDialog.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Message message = Message.obtain();
                message.obj = HandleQRCodeResult.FAILED;
                handler.sendMessage(message);
                ICBCLaunchAppServices.launchFullVersionApp(context);
            }
        });
        MessageDialog.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Message message = Message.obtain();
                message.obj = HandleQRCodeResult.FAILED;
                handler.sendMessage(message);
                arg0.dismiss();
            }
        });
        return MessageDialog.create();
    }

    // 提示下载移动IM
    public static AlertDialog getMessageToRecommandImDialog(final Context context, final MenuEntity menu) {
        Builder MessageDialog = null;
        MessageDialog = new AlertDialogPro.Builder(context, R.style.Theme_AlertDialogPro_ICBC);
        MessageDialog.setTitle("提示");
        MessageDialog.setMessage(context.getString(R.string.recommand_im_tips));
        MessageDialog.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
//				launchAppServices.launchApp();
                downFile(menu.getApkURL(), menu.getId() + "_temp.apk", context);

            }
        });
        MessageDialog.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        return MessageDialog.create();
    }


    public static void downFile(String downloadAPKUrl, final String apkName, final Context context) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.arg1 == AsyncTaskDownloadServices.DOWNLOAD_SUCCESS) {
                    update(context, apkName);
                } else if (msg.arg1 == AsyncTaskDownloadServices.DOWNLOAD_FAIL) {
                    failed(context);
                }
            }
        };
        AsyncTaskDownloadServices ATDS = new AsyncTaskDownloadServices(context, "软件下载", "程序下载中，请稍候...");
        ATDS.execute(downloadAPKUrl, Environment.getExternalStorageDirectory(), apkName, handler);
    }

    private static void failed(final Context context) {
        ICBCDialog.getDownloadFailedDialog(context).show();
    }

    private static void update(final Context context, String apkName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), apkName)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
*/
}
