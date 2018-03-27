package com.example.updateversion;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;

import com.example.ishelloword.R;

/**
 * @author zhanglong
 */
public class UpdateVersionActivity extends Activity {
    private UpdateVersionService updateVersionService;
    private static final String UPDATEVERSIONXMLPATH = "http://192.168.1.100:8080/server/updateversion/version.xml";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateversion);
        if (Build.VERSION.SDK_INT >= 11) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        }
        // TODO Auto-generated method stub
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                updateVersionService = new UpdateVersionService(UPDATEVERSIONXMLPATH, UpdateVersionActivity.this);// 创建更新业务对象
                updateVersionService.checkUpdate();//调用检查更新的方法,如果可以更新.就更新.不能更新就提示已经是最新的版本了
            }

        }, 2000);//2秒之后执行

    }

    /**
     * @param view
     */
    public void updateVersion(View view) {
        updateVersionService = new UpdateVersionService(UPDATEVERSIONXMLPATH, this);
        updateVersionService.checkUpdate();
    }

}
