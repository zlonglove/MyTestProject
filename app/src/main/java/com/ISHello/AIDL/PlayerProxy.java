package com.ISHello.AIDL;

import android.os.IBinder;
import android.os.Parcel;

/**
 * Client使用，binder在bindService后的onServiceConnected的IBinder参数获取Remote的
 *
 * @author zhanglong
 */
public class PlayerProxy implements IPlayer {
    private IBinder binder;
    private String status;

    public PlayerProxy(IBinder binder) {
        this.binder = binder;
    }

    @Override
    public void play() {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        // data.writeString("play");
        try {
            binder.transact(1, data, reply, 0);
            status = reply.readString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    @Override
    public void stop() {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        // data.writeString("stop");
        try {
            binder.transact(2, data, reply, 0);
            status = reply.readString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    @Override
    public String getStatus() {
        return status;
    }

}
