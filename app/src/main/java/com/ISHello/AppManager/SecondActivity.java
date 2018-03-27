package com.ISHello.AppManager;

import com.ISHello.CustomToast.CustomToast;
import com.ISHello.View.LocusPassWordView;
import com.ISHello.View.LocusPassWordView.OnCompleteListener;
import com.ISHello.utils.CaptureUtils;
import com.example.ishelloword.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class SecondActivity extends BaseActivity {
    private final String TAG = "SecondActivity";
    private LocusPassWordView locusPassWordView;
    private int flag = 0;// 设置密码记录次数,2次一样并且超过5个点就成功
    private String password;// 密码012345678
    private final String passwordIndex = "Password";// 保存设置成功的密码
    private int Mode = 0;// 0-设置密码,1-解锁

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.i(TAG, "--->onCreate()");
        setContentView(R.layout.second_activity_layout);
        findViews();
        init();
    }

    private void findViews() {
        locusPassWordView = (LocusPassWordView) this
                .findViewById(R.id.activity_gesture_login_locuspwdview);
    }

    private void init() {
        initLocusPassword();
        locusPassWordView.setOnCompleteListener(new OnCompleteListener() {

            @Override
            public void onComplete(String password) throws Exception {
                Log.i(TAG, "--->Password==" + password);
                /**
                 * 设置密码
                 */
                if (Mode == 0) {
                    if (flag == 0) {
                        SecondActivity.this.password = password;
                        SecondActivity.this.locusPassWordView.clearPassword();
                        CustomToast.makeText(SecondActivity.this, "请再输入一次",
                                Toast.LENGTH_LONG);
                        flag++;
                    } else if (flag == 1) {
                        if (password.equals(SecondActivity.this.password)) {
                            SecondActivity.this.locusPassWordView
                                    .clearPassword();
                            CustomToast.makeText(SecondActivity.this, "手势密码设置成功",
                                    Toast.LENGTH_LONG);
                            SecondActivity.this.locusPassWordView
                                    .clearPassword();
                            savaPassword(password);
                        } else {
                            flag = 0;
                            Mode = 0;
                            CustomToast.makeText(SecondActivity.this,
                                    "两次密码不一致,请重新输入", Toast.LENGTH_LONG);
                            SecondActivity.this.locusPassWordView
                                    .markError(1000);
                        }
                    }
                }
                /**
                 * 解锁
                 */
                else if (Mode == 1) {
                    if (password.equals(SecondActivity.this.password)) {
                        CustomToast.makeText(SecondActivity.this, "解锁成功",
                                Toast.LENGTH_LONG);
                        SecondActivity.this.locusPassWordView.clearPassword();
                    } else {
                        CustomToast.makeText(SecondActivity.this, "输入不正确,请重新输入",
                                Toast.LENGTH_LONG);
                        SecondActivity.this.locusPassWordView.markError(1000);
                    }
                }
            }
        });
    }

    private void initLocusPassword() {
        SharedPreferences rePreferences = getSharedPreferences(passwordIndex,
                Context.MODE_PRIVATE);
        String password = rePreferences.getString(passwordIndex, "");
        if (TextUtils.isEmpty(password)) {
            CustomToast.makeText(SecondActivity.this, "请设置密码", Toast.LENGTH_LONG);
            Mode = 0;
        } else {
            Mode = 1;
            CustomToast.makeText(SecondActivity.this, "请绘制密码解锁", Toast.LENGTH_LONG);
            this.password = password;
        }
    }

    private void savaPassword(String password) {
        SharedPreferences sharedPreferences = getSharedPreferences(
                passwordIndex, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(passwordIndex, password).commit();
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
        Log.i(TAG, "--->onDestroy()");
    }
}
