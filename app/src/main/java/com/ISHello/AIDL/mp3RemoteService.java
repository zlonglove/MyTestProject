package com.ISHello.AIDL;

import com.example.ishelloword.R;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class mp3RemoteService extends Service {
    private IBinder mBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new mp3Binder(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    class mp3Binder extends PlayerStub {
        private Context context;
        private MediaPlayer mediaPlayer;

        public mp3Binder(Context context) {
            this.context = context;
        }

        @Override
        public void play() {
            if (mediaPlayer != null) {
                return;
            }
            mediaPlayer = MediaPlayer.create(context, R.raw.test);
            mediaPlayer.start();
        }

        @Override
        public void stop() {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }

        @Override
        public String getStatus() {
            return "hello";
        }
    }

}
