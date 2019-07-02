package com.ISHello.LaunchMode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.ishelloword.MainActivity;
import com.example.ishelloword.R;

public class ThirdActivity extends Activity {
    private final String TAG = ThirdActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"--->onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
    }

    public void startMainActivity(View view) {
        Intent intent = new Intent(ThirdActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        Log.i(TAG,"--->onCreate()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i(TAG,"--->onResume()");
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(TAG,"--->onNewIntent()");
        super.onNewIntent(intent);
    }
}
