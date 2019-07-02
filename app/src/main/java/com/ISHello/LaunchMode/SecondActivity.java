package com.ISHello.LaunchMode;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.ishelloword.R;

/**
 * Created by zhanglong on 2017/2/22.
 */

public class SecondActivity extends Activity {
    private final String TAG = SecondActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG,"--->onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void thirdActivity(View view) {
        Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.e(TAG,"--->onNewIntent()");
        super.onNewIntent(intent);
    }

    @Override
    protected void onPause() {
        Log.e(TAG,"--->onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.e(TAG,"--->onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG,"--->onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.e(TAG,"--->onConfigurationChanged()"+newConfig.orientation);
        super.onConfigurationChanged(newConfig);
    }
}
