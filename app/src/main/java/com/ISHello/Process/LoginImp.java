package com.ISHello.Process;

import android.util.Log;

/**
 * Created by zhanglong on 2016/12/26.
 */

public class LoginImp implements LoginListener {
    private String TAG = "LoginImp";

    @Override
    public void onLoginOk(String success) {
        Log.i(TAG, "--->" + success);
    }

    @Override
    public void onLoginFail(String fail) {
        Log.i(TAG, "--->" + fail);
    }
}
