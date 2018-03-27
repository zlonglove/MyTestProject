package com.ISHello.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.ISHello.View.AlertDialogPro.Builder;
import com.example.ishelloword.R;

public class CustomDialog {

    public static AlertDialog getMessageDialog(Context context, String msg) {
        Builder MessageDialog;
        MessageDialog = new Builder(context, R.style.Theme_AlertDialogPro_ICBC);
        MessageDialog.setTitle("提示");
        MessageDialog.setMessage(msg);
        MessageDialog.setCancelable(false);
        MessageDialog.setPositiveButton("确定", new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        return MessageDialog.create();
    }

    public static AlertDialog getMessageExitDialog(final Activity context, String msg) {
        AlertDialogPro.Builder MessageDialog;
        MessageDialog = new Builder(context, R.style.Theme_AlertDialogPro_ICBC);
        MessageDialog.setTitle("提示");
        MessageDialog.setMessage(msg);
        MessageDialog.setCancelable(false);
        MessageDialog.setPositiveButton("确定", new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
                context.setResult(Activity.RESULT_OK);
                context.finish();
            }
        });
        return MessageDialog.create();
    }

    public static AlertDialog getTipsDialog(final Context context, String msg) {
        Builder ExitDialog;
        ExitDialog = new AlertDialogPro.Builder(context, R.style.Theme_AlertDialogPro_ICBC);
        ExitDialog.setTitle("提示");
        ExitDialog.setMessage(msg);
        return ExitDialog.create();
    }


}
