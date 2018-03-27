package com.Hello.VirtualKeySend;
/*
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.IWindowManager;
import android.os.ServiceManager;

public class VirtualKey 
{
	private final String TAG="VirtualKey";

    private void sendKeyEvent(int keyCode) {
        int eventCode = keyCode;
        long now = SystemClock.uptimeMillis();
        try {
            KeyEvent down = new KeyEvent(now, now, KeyEvent.ACTION_DOWN, eventCode, 0);
            KeyEvent up = new KeyEvent(now, now, KeyEvent.ACTION_UP, eventCode, 0);
            (IWindowManager.Stub
                .asInterface(ServiceManager.getService("window")))
                .injectInputEventNoWait(down);
            (IWindowManager.Stub
                .asInterface(ServiceManager.getService("window")))
                .injectInputEventNoWait(up);
        } catch (RemoteException e) {
            Log.i(TAG, "DeadOjbectException");
        }
    }

}

*/
