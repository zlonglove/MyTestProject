package com.ISHello.Process;

/**
 * Created by zhanglong on 2016/12/26.
 */

public class LoginManager {
    public LoginListener loginListener;

    public LoginManager() {

    }

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }

    public void onLoginOk() {
        if (loginListener != null) {
            loginListener.onLoginOk("success");
        }
    }

    public void onLoginFail() {
        if (loginListener != null) {
            loginListener.onLoginFail("fail");
        }
    }
}
