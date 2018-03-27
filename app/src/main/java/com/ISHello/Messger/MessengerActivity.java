package com.ISHello.Messger;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.example.ishelloword.R;

public class MessengerActivity extends Activity {
    private final String TAG = "MessengerActivity";
    private Messenger messenger;
    private final int MSG_SEND_TO_SERVER = 2;
    private final int MSG_FROM_SERVER = 3;
    private ServiceConnection mconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messenger = new android.os.Messenger(iBinder);
            Message msg = Message.obtain(null, MSG_SEND_TO_SERVER);
            Bundle bundle = new Bundle();
            bundle.putString("msg", "hello,this is client");
            msg.setData(bundle);
            msg.replyTo = mGetReplyMessage;
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private Messenger mGetReplyMessage = new Messenger(new MessengerHandler());

    private class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FROM_SERVER:
                    Log.i(TAG, "--->recieve msg from service:" + msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mconnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(mconnection);
        super.onDestroy();
    }
}
