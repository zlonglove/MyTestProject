package com.ISHello.BinderPool.Server;

import android.os.RemoteException;

import com.ISHello.BinderPool.Aidl.ICompute;

public class ComputeImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
