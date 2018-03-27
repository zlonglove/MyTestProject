package com.ISHello.Process;

import android.content.Context;

/**
 * Created by zhanglong on 2016/12/26.
 */

public class ProcessCore {
    private static ProcessCore instance;
    private Context mContext;
    private static LoginManager loginManager;

    private ProcessCore(Context context) {
        mContext = context;
    }

    public static ProcessCore getInstance() {
        return instance;
    }

    public static void initialize(Context context) {
        instance = new ProcessCore(context);
        loginManager = new LoginManager();
        loginManager.setLoginListener(new LoginImp());
        loginManager.onLoginOk();
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

}
