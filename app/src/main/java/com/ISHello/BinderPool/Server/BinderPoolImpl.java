package com.ISHello.BinderPool.Server;

import android.os.IBinder;
import android.os.RemoteException;

import com.ISHello.BinderPool.Aidl.IBinderPool;

/**
 * @author lenovo
 */
public class BinderPoolImpl extends IBinderPool.Stub {
    private static final int BINDER_SECURITY_CENTER = 1;
    private static final int BINDER_COMPUTE = 2;

    @Override
    public IBinder queryBinder(int binderCode) throws RemoteException {
        IBinder binder = null;
        switch (binderCode) {
            case BINDER_SECURITY_CENTER: {
                binder = new SecurityCenterImpl();
                break;
            }
            case BINDER_COMPUTE: {
                binder = new ComputeImpl();
                break;
            }
            default: {
                break;
            }

        }
        return binder;
    }
}
