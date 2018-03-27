package com.ISHello.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

public class MediaControlReceiver extends BroadcastReceiver {
    private final String TAG = "MediaControlReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
            KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            Log.i(TAG,
                    "--->MediaControlReceiver keycode==" + event.getKeyCode());
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_MEDIA_PLAY:
                        Log.i(TAG, "--->Play");
                        break;
                    //已废除 等同于KEYCODE_HEADSETHOOK
                    case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    case KeyEvent.KEYCODE_HEADSETHOOK:
                        Log.i(TAG, "--->Pause");
                        break;
                    case KeyEvent.KEYCODE_MEDIA_NEXT:
                        Log.i(TAG, "--->Next");
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                        Log.i(TAG, "--->Previous");
                        break;
                    case KeyEvent.KEYCODE_MEDIA_STOP:
                        Log.i(TAG, "--->Stop");
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
