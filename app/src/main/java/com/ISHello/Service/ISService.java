package com.ISHello.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.ISHello.MediaPlayer.ISMediaPlayer;
import com.ISHello.MediaPlayer.ISPlayStyle;

/**
 * @author Administrator context.startService() ->onCreate()-
 *         >onStart()->Service running-- 调用context.stopService() ->onDestroy()
 *         context.bindService()->onCreate()->onBind()->Service running--
 *         调用>onUnbind() -> onDestroy() 从上诉可以知道分别对应本地的,以及远程的，也对应不同的方式启动这个服务。
 */
public class ISService extends Service {

    private static final String TAG = "LocalService";
    private IBinder binder = new ISService.LocalBinder();
    ISMediaPlayer mediaPlayer = null;

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "--->Service onBind()");
        return binder;
    }

    public void onCreate() {
        Log.i(TAG, "--->Service onCreate()");
        // 这里可以启动媒体播放器
        if (mediaPlayer == null) {
            // mediaPlayer=new
            // ISMediaPlayer(this,ISPlayStyle.PLAY_FROM_RAWFILE,R.raw.test);
            // mediaPlayer=new ISMediaPlayer(this,
            // ISPlayStyle.PLAY_FROM_LOCALPATH, 0);
            mediaPlayer = new ISMediaPlayer(this, ISPlayStyle.PLAY_FROM_ASSERT,
                    0);
            // mediaPlayer=new ISMediaPlayer(this,ISPlayStyle.PLAY_FROM_NET, 0);
            // mediaPlayer=new
            // ISMediaPlayer(this,ISPlayStyle.PLAY_FROM_NET,"http://118.192.132.1:8080/file/test.wav");
        }
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "--->Service onStartCommand()");
        mediaPlayer.playFromAssets("love.mp3", false);
        // mediaPlayer.playFromLocalPath(Environment.getExternalStorageDirectory()
        // + "/"+"love.mp3",false);
        // Uri uri= Uri.parse("http://118.186.196.1:8000/file/test.wav");
        // mediaPlayer.playFromNet(uri, false);
        // return super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "--->Service onDestroy()");
        mediaPlayer.stop();
        super.onDestroy();
    }

    public class LocalBinder extends Binder {
        // 返回本地服务
        ISService getService() {
            return ISService.this;
        }
    }

}
