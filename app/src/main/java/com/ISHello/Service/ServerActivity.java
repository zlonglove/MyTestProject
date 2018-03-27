package com.ISHello.Service;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ISHello.CustomToast.CustomToast;
import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.MainActivity;
import com.example.ishelloword.R;

/**
 * @author zhanglong
 */
public class ServerActivity extends BaseActivity {
    private final String TAG = "ServerActivity";
    private MyServiceConnection myServiceConnection;
    private Button updateButton;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "--->onCreate()");
        setContentView(R.layout.serveractivity);
        textView = (TextView) this.findViewById(R.id.serviceTitle);
        textView.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD_ITALIC);
        myServiceConnection = new MyServiceConnection();
        updateButton = (Button) findViewById(R.id.updateButton);
        Animation anim = AnimationUtils.loadAnimation(this,
                R.anim.button_refresh);
        updateButton.startAnimation(anim);
        Intent intent = new Intent(this, MyService.class);
        this.bindService(intent, myServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        Log.i(TAG, "--->onStart()");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "--->onRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "--->onResume()");
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(TAG, "--->onNewIntent()");
        super.onNewIntent(intent);
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "--->onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "--->onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "--->onDestroy()");
        super.onDestroy();
    }

    public void refresh(View view) {
        Log.i(TAG, "--->refresh Button click");
        CustomToast.makeText(this, "Refresh", Toast.LENGTH_SHORT);
    }

    /**
     * 开启服务 和调用者没有绑定 调用者退出了server人在运行，除非手动停止
     *
     * @param view
     */
    public void serviceStart(View view) {
        Log.i(TAG, "--->serviceStart button click");
        Intent intent = new Intent(this, MyService.class);
        //intent.setAction("com.ISHello.Service.ISService.MY_SERVER");
        this.startService(intent);

        /*Intent intent = new Intent(this, catchInfo.class);
        intent.putExtra("order", new boolean[]{false, true, true});
        this.startService(intent);*/

    }

    /**
     * 停止服务
     *
     * @param view
     */
    public void serviceStop(View view) {
        Log.i(TAG, "--->serviceStop button click");
        Intent intent = new Intent(this, MyService.class);
        //intent.setAction("com.ISHello.Service.ISService.MY_SERVER");
        this.stopService(intent);
    }

    /**
     * 开启服务 server和调用的对象绑定在一起
     *
     * @param view
     */
    public void bindServer(View view) {
        Log.i(TAG, "--->bindServer button click");
        Intent intent = new Intent(this, MyService.class);
        //intent.setAction("com.ISHello.Service.MyService.MY_SERVER");
        this.bindService(intent, myServiceConnection, BIND_AUTO_CREATE);
        Log.i(TAG, "--->bindServer button click");
    }

    /**
     * 停止服务
     *
     * @param view
     */
    public void unbindServer(View view) {
        Log.i(TAG, "--->unbindServer button click");
        Intent intent = new Intent(this, MyService.class);
        //intent.setAction("com.ISHello.Service.MyService.MY_SERVER");
        this.unbindService(myServiceConnection);
    }

    /**
     * 获取用于得到server中数据的Bunder对象
     *
     * @param view
     */
    public void getCount(View view) {
        Log.i(TAG, "--->get link count");
        if (myServiceConnection.binder != null) {
            CustomToast.makeText(this, "Service的 count值为" + myServiceConnection.getBinder().getCount(), Toast.LENGTH_LONG);
        }
    }

    public void asyncService(View view) {
        Log.i(TAG, "--->Async Service Button Click");
        Intent intent = new Intent(ServerActivity.this,
                LooperPrintAsyncService.class);
        startService(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ServerActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
