package com.ISHello.GesturePassword;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.ISHello.Application.BaseApplication;
import com.ISHello.GesturePassword.Manager.SystemBarTintManager;
import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

/**
 * @author 手势密码设置首页引导
 */
public class GuideGesturePasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesturepassword_guide);


        SystemBarTintManager mTintManager;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setStatusBarTintResource(R.color.statusbar_bg);

        findViewById(R.id.gesturepwd_guide_btn).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseApplication.getInstance().getLockPatternUtils().clearLock();
                        Intent intent = new Intent(
                                GuideGesturePasswordActivity.this,
                                CreateGesturePasswordActivity.class);
                        // 打开新的Activity
                        startActivityForResult(intent, 1);
                    }
                });
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 3) {
                this.setResult(3);
                this.finish();
            }
        }
    }
}
