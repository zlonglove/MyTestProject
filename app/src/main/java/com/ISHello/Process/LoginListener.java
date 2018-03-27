package com.ISHello.Process;

/**
 * Created by zhanglong on 2016/12/26.
 */

interface LoginListener {
    public void onLoginOk(String success);

    public void onLoginFail(String fail);
}
