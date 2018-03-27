package com.ISHello.Messger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * 这个是服务端程序
 */
public class MessengerService extends Service {
    private final String TAG = "MessengerService";
    private final int MSG_FROM_CLIENT = 2;
    private final int MSG_SEND_TO_CLIENT = 3;
    private Messenger messenger;

    @Override
    public void onCreate() {
        super.onCreate();
        messenger = new Messenger(new MessagerHandler());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    private class MessagerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FROM_CLIENT:
                    /**
                     * 以下是收取客户端数据
                     */
                    Log.i(TAG, "--->recieve msg from Client:" + msg.getData().getString("msg"));

                    /**
                     * 以下是向客户端发送数据
                     */
                    Messenger client = msg.replyTo;
                    Message relyMessage = Message.obtain(null, MSG_SEND_TO_CLIENT);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply", "this is replay from service");
                    relyMessage.setData(bundle);
                    try {
                        client.send(relyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
