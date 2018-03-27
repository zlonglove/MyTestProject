package com.ISHello.BroadcastReceiver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.ISHello.utils.utils;
import com.example.ishelloword.MainActivity;
import com.example.ishelloword.R;

public class broadCastActivity extends Activity {

    TextView timeText;
    private final String TAG = "broadCastActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Log.i(TAG, "--->onCreate()");
        setContentView(R.layout.broadcastactivity);
        timeText = (TextView) findViewById(R.id.freshTime);
        timeText.setText(utils.getCurDate(this));
        new TimeUpdate().start();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (timeText != null) {
                        timeText.setText((String) msg.obj);
                    }
                    break;

                default:
                    break;
            }
        }

    };

    class TimeUpdate extends Thread {

        @Override
        public void run() {
            while (true) {
                Message message = handler.obtainMessage();
                message.what = 1;
                message.obj = utils.getCurDate(broadCastActivity.this);
                handler.sendMessage(message);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 发送普通广播
     *
     * @param view
     */
    public void broadNormalCastSend(View view) {
        Intent intent = new Intent();
        // 指定发送特定的BroadcastReceiver   
        intent.setAction("com.ISHello.BroadcastReceiver.MY_BROADCAST");
        // 带一些消息内容过去  
        Bundle bundle = new Bundle();
        bundle.putString("msg", "张龙");
        bundle.putLong("time", 3000);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    /**
     * 发送有序广播
     *
     * @param view 前面的接收者有权终止广播(BroadcastReceiver.abortBroadcast())
     */
    public void broadOrderedCastSend(View view) {
        Intent intent = new Intent();
        // 指定发送特定的BroadcastReceiver   
        intent.setAction("com.ISHello.BroadcastReceiver.MY_BROADCAST");
        // 带一些消息内容过去  
        Bundle bundle = new Bundle();
        bundle.putInt("Action", 802);
        bundle.putString("msg", "张龙");
        intent.putExtras(bundle);
        sendOrderedBroadcast(intent, null);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        Log.i(TAG, "--->onKeyDown()" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "--->onRestoreInstanceState()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        Log.i(TAG, "--->onSaveInstanceState()");
    }


    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        Log.i(TAG, "--->onNewIntent()");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.i(TAG, "--->onResume()");
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.i(TAG, "--->onPause()");
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Log.i(TAG, "--->onStart()");
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        Log.i(TAG, "--->onRestart()");
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        Log.i(TAG, "--->onStop()");
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.i(TAG, "--->onDestroy()");
    }


}
