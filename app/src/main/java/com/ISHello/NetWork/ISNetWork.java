package com.ISHello.NetWork;

import android.util.Log;

public class ISNetWork {
    private final String TAG = "ISNetWork";
    ISNetWorThread networkThread;

    private ISNetCallBack netCallBack;

    public ISNetWork(ISAndroidHandler androidHandler) {
        super();
        netCallBack = new ISNetCallBack(androidHandler);
        networkThread = new ISNetWorThread("NetWork", true, netCallBack);
        networkThread.start();

    }

    /**
     * 发送网络指令
     *
     * @param id
     * @param object
     */
    public void sendNetWorkEvent(int id, Object object) {
        if (networkThread != null) {
            Log.i(TAG, "--->ISNetwork send network cmd!!");
            networkThread.sendNetWorkEvent(id, object);
        }
    }

    public void stopNetWork() {
        if (networkThread != null) {
            Log.i(TAG, "--->ISNetwork stop!!");
            networkThread.stopThread();
        }
    }

}
