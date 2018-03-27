package com.ISHello.AIDL;

import android.os.Binder;
import android.os.Parcel;
import android.os.RemoteException;

public abstract class PlayerStub extends Binder implements IPlayer {
    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags)
            throws RemoteException {

        switch (code) {
            case 1:
                reply.writeString("play");
                this.play();
                break;
            case 2:
                reply.writeString("stop");
                this.stop();
                break;
            default:
                break;
        }
        return super.onTransact(code, data, reply, flags);
    }

}
