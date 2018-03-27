package com.DialogActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.ISHello.CustomToast.CustomToast;
import com.ISHello.HomeWatcher.ISHomeWatcher;
import com.ISHello.HomeWatcher.ISHomeWatcher.OnHomePressedListener;
import com.ISHello.base.base.BaseActivity;
import com.ISHello.wifi.WifiInfomation;
import com.example.ishelloword.MainActivity;
import com.example.ishelloword.R;
import com.nineoldandroids.animation.ObjectAnimator;

public class DialogActivity extends BaseActivity {
    private final String TAG = "DialogActivity";
    public Activity activity;
    private ISHomeWatcher homeWatcher;
    private Button ReturnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "--->DialogActivity onCreate()");
        activity = this;
        //setFullScreen(true);
        setContentView(R.layout.dialogactivity);
        if (getActionBar()!=null) {
            getSupportActionBar().hide();
        }

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.80);
        int screenHeight = (int) (metrics.heightPixels * 0.80);
        //getWindow().setLayout(screenWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setLayout(screenWidth, screenHeight);

        homeWatcher = new ISHomeWatcher(this);
        homeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                Log.i(TAG, "--->Home Pressed");
            }

            @Override
            public void onHomeLongPressed() {
                Log.i(TAG, "--->Home Long Pressed");
            }
        });
        homeWatcher.startWatch();
        ReturnButton = (Button) findViewById(R.id.ReturnButton);
        ObjectAnimator.ofFloat(ReturnButton, "translationX", 0, 100, -100, 0).setDuration(2000).start();
        ObjectAnimator.ofFloat(ReturnButton, "rotationY", 0, 180, 0).setDuration(2000).start();

        CustomToast.makeText(this, WifiInfomation.getIpAddress(this), Toast.LENGTH_LONG);
    }


    public void onClick(View view) {
        Log.i(TAG, "--->" + view.getId());
        Intent intent = new Intent(DialogActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        this.finish();
    }

    /**
     * 設置是否全屏
     *
     * @param flag true--全屏 false--非全屏
     */
    public void setFullScreen(boolean flag) {
        if (flag) {
            // 设置没有标题
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            // 设置全屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.i(TAG, "--->DialogActivity onDestroy()");
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        Log.i(TAG, "--->DialogActivity onRestart()");
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        homeWatcher.stopWatch();
        Log.i(TAG, "--->DialogActivity onPause()");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        homeWatcher.startWatch();
        Log.i(TAG, "--->DialogActivity onResume()");
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Log.i(TAG, "--->DialogActivity onStart()");
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        Log.i(TAG, "--->DialogActivity onStop()");
    }

}
