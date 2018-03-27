package com.ISHello.base.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;

import com.ISHello.utils.LogUtil;
import com.example.ishelloword.R;

public class BaseDialog {
    private static final String TAG = BaseDialog.class.getSimpleName();

    // 加载中对话框类型
    public enum ProgressDialogType {
        Normal, // 正常的加载中对话框
        Normal2, // 正常的加载中对话框
        NoBackground, // 无背景的加载中对话框
        NoBackground2, // 无背景的加载中对话框(下部)
        ICBC3DLogo// 带有3DLogo的加载中对话框
    }

    public static Dialog getProgressDialog(final Context context, ProgressDialogType type) {
        Dialog progressDialog;
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
                LogUtil.log(TAG, "--->progressDialog onShow()");
            }
        });
        progressDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface arg0) {
                //ICBCPushService.LoadingShowIMFloatWindowFlag(context.getApplicationContext());
                LogUtil.log(TAG, "--->progressDialog onDismiss()");
            }
        });
        return progressDialog;
    }

}
