package com.ISHello.Process;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.ishelloword.MainActivity;
import com.example.ishelloword.R;

/**
 * 不同进程的组件会拥有独立的虚拟机、Application以及内存空间
 * 一般来说，使用多进程会造成如下几个方面的问题
 * 1)静态成员和单例模式完全消失
 * 2）线程同步机制完全消失
 * 3）SharedPreference的可靠性下降
 * 4）Application会创建多次
 */
public class ProcessActivity extends Activity {
    private final String TAG = "ProcessActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        init();
        ProcessCore.getInstance().getLoginManager().onLoginOk();
    }

    private void init() {
        Log.d(TAG, "--->Static value ==" + UserManager.sUserId);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void Click(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
