package com.ISHello.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import com.example.ishelloword.R;

/**
 * @author kfzx-zhanglong
 */

public class DialogFactory {

    public static Dialog createIndetemineProgressDialog(Context baseContext, String msg) {
        Dialog progressDialog = createIndeterminateProgressDialog(baseContext, null, msg, true, false);
        return progressDialog;
    }

    public static ProgressDialog createIndeterminateProgressDialog(final Context context, String title, String message, boolean indeterminate,
                                                                   boolean cancelable) {
        ProgressDialog dialog = new ProgressDialog(context,R.style.CustomProgressDialog);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setIndeterminate(indeterminate);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    /**
     * 警告型Dialog的回调
     */
    public interface WarningDialogListener {
        public void onWarningDialogOK(int id);

        public void onWarningDialogCancel(int id);

        public void onWarningDialogMiddle(int id);

    }

    public static Dialog createYesNoWarningDialog(Context context, int id, String title, String warning, int resId, WarningDialogListener listener) {
        Dialog dlg = createWarningDialog(context, id, title, warning, context.getString(R.string.yes), context.getString(R.string.no), resId,
                listener);
        return dlg;
    }

    public static Dialog createYesNoWarningDialog(Context context, int id, String title, String content, String positiveButton,
                                                  String negativeButton, int resId, WarningDialogListener listener) {
        Dialog dlg = createWarningDialog(context, id, title, content, positiveButton, negativeButton, resId, listener);
        return dlg;
    }

    public static Dialog createWarningDialog(final Context context, final int id, String title, String content, String positiveButton,
                                             String negativeButton, int resId, final WarningDialogListener listener) {
        return createWarningDialog(context, id, title, content, positiveButton, negativeButton, null, resId, listener);
    }

    public static Dialog createWarningDialog(final Context context, final int id, String title, String content, String positiveButton,
                                             String negativeButton, String middleButton, int resId, final WarningDialogListener listener) {
        return createWarningDialog(context, id, title, content, positiveButton, negativeButton, middleButton, resId, listener, true);
    }

    public static Dialog createWarningDialog(final Context context, final int id, String title, String content, String positiveButton,
                                             String negativeButton, String middleButton, int resId, final WarningDialogListener listener, final boolean isback) {

        Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setIcon(resId);
        builder.setMessage(content);
        builder.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                if (listener != null) {
                    // listener.onWarningDialogCancel(id);
                }
            }
        });
        if (positiveButton != null) {
            builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onWarningDialogOK(id);
                    }
                }
            });
        }
        if (negativeButton != null) {
            builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onWarningDialogCancel(id);
                    }
                }
            });
        }

        if (middleButton != null) {
            builder.setNeutralButton(middleButton, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onWarningDialogMiddle(id);
                    }
                }
            });
        }
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    // 针对强制升级不允许返回键取消的dialog
    public static Dialog createDialogForEnforceUpdate(final Context context, final int id, String title, String warning, String positiveButton,
                                                      String negativeButton, String middleButton, int resId, final WarningDialogListener listener) {

        Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setIcon(resId);
        builder.setMessage(warning);
        builder.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                if (listener != null) {
                    // listener.onWarningDialogCancel(id);
                }
            }
        });
        if (positiveButton != null) {
            builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onWarningDialogOK(id);
                    }
                }
            });
        }
        if (negativeButton != null) {
            builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onWarningDialogCancel(id);
                    }
                }
            });
        }

        if (middleButton != null) {
            builder.setNeutralButton(middleButton, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onWarningDialogMiddle(id);
                    }
                }
            });
        }
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

}
