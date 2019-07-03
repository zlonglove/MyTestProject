package com.ISHello.BinderPool.Server;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.ISHello.BinderPool.Aidl.IBinderPool;
import com.ISHello.BinderPool.Aidl.ICompute;
import com.ISHello.BinderPool.Aidl.ISecurityCenter;

import java.util.concurrent.CountDownLatch;

public class BinderPoolTools {
    private static final String TAG = BinderPoolTools.class.getSimpleName();
    public static final int BINDER_SECURITY_CENTER = 1;
    public static final int BINDER_COMPUTE = 2;

    private Context mContext;
    private IBinderPool mIBinderPool;
    private static volatile BinderPoolTools mBinderPoolTools;
    /**
     * 实现主线程与子线程之间的同步
     */
    private CountDownLatch mCountDownLatch;

    private ISecurityCenter securityCenter;
    private ICompute compute;

    private BinderPoolTools(Context context) {
        mContext = context.getApplicationContext();
        connectBinderPoolService();
    }

    public static BinderPoolTools getInstance(Context context) {
        if (mBinderPoolTools == null) {
            synchronized (BinderPoolTools.class) {
                if (mBinderPoolTools == null) {
                    mBinderPoolTools = new BinderPoolTools(context);
                }
            }
        }
        return mBinderPoolTools;
    }

    private synchronized void connectBinderPoolService() {
        /**
         * 定义countDownLatch, 计数为1：表示有1个子线程
         */
        mCountDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(mContext, BinderPoolService.class);
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        try {
            /**
             * 主线程等待
             */
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mIBinderPool.asBinder().linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            /**
             * 计数减一，直到计数为零
             */
            mCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.e(TAG, "--->binder died");
            mIBinderPool.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mIBinderPool = null;
            connectBinderPoolService();
        }
    };

    public ISecurityCenter getSecurityCenter() {
        if (mIBinderPool != null) {
            try {
                if (securityCenter == null) {
                    securityCenter = ISecurityCenter.Stub.asInterface(mIBinderPool.queryBinder(BINDER_SECURITY_CENTER));
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return securityCenter;
    }

    public ICompute getCompute() {
        if (mIBinderPool != null) {
            try {
                if (compute==null) {
                    compute = ICompute.Stub.asInterface(mIBinderPool.queryBinder(BINDER_COMPUTE));
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return compute;
    }
}
