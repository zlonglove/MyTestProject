package com.ISHello.BinderPool.Client;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ISHello.BinderPool.Server.BinderPoolTools;
import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

public class BinderPoolActivity extends BaseActivity {
    private final String TAG = BinderPoolActivity.class.getSimpleName();
    private BinderPoolTools binderPoolTools;

    private Button bindPoolBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_pool);
        bindPoolBtn = findViewById(R.id.bind_pool_btn);
        bindPoolBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        doWork();
                    }
                }.start();
            }
        });

    }

    private void doWork() {
        binderPoolTools = BinderPoolTools.getInstance(this);
        String msg = "helloword-Android";
        try {
            String encode = binderPoolTools.getSecurityCenter().encrypt(msg);
            Log.e(TAG, "--->encrypt==" + encode);
            String decode = binderPoolTools.getSecurityCenter().decrypt(encode);
            Log.e(TAG, "--->decrypt==" + decode);

            int sum = binderPoolTools.getCompute().add(1, 2);
            Log.e(TAG, "--->sun==" + sum);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
