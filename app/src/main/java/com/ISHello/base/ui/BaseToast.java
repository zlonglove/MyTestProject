package com.ISHello.base.ui;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.ISHello.CustomToast.CustomToast;

/**
 * Created by zhanglong on 2017/2/10.
 */

public class BaseToast {
    public static final String TAG = "BaseToast";

    public static void showToast(Context context, String msg, int duration) {
        final Toast toast = CustomToast.getToast(context, msg, duration);
        if (duration != Toast.LENGTH_LONG && duration != Toast.LENGTH_SHORT && duration > 1000) {
            new CountDownTimer(duration, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    Log.i(TAG, "--->onTick");
                    toast.show();
                }

                @Override
                public void onFinish() {
                    Log.i(TAG, "--->onFinish");
                    toast.cancel();
                }
            }.start();
        } else {
            toast.show();
        }
    }
}
