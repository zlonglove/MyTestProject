package com.ISHello.RemoteCalls;

import android.os.RemoteException;
import android.util.Log;

public class PersonImpl extends IPersonaidl.Stub {
    private final String TAG = "PersionImpl";

    @Override
    public String display() throws RemoteException {
        return "远程调用服务测试";
    }

    @Override
    public void play() throws RemoteException {
        // TODO Auto-generated method stub
        Log.d(TAG, "--->play()");

    }

    @Override
    public void stop() throws RemoteException {
        // TODO Auto-generated method stub
        Log.d(TAG, "--->stop()");
    }

}
