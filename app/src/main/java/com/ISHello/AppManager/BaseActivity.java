package com.ISHello.AppManager;

import com.example.ishelloword.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;

public class BaseActivity extends FragmentActivity {
    private final String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.i(TAG, "--->BaseActivity onCreate()");
        /**
         * 添加当前Activity到管理类
         */
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "--->BaseActivity onDestroy()");
        /**
         * 从管理类删除当前Activity
         */
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 返回按钮
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            backKeyPress();
            return false;
        }
        return false;
    }

    /**
     * 子类可以重写此方法
     */
    public void backKeyPress() {
        this.finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }
}
