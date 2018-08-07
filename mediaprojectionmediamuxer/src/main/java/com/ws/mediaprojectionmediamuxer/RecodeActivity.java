package com.ws.mediaprojectionmediamuxer;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;


/**
 * @author zhanglong
 */
public class RecodeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mScreenShortBtn;
    private Button mScreenRecordBtn;
    private MediaProjectionManager mMpMngr;
    private static final int REQUEST_MEDIA_PROJECTION = 1;
    private static final int OVERLAY_PERMISSION_REQ_CODE = 2;
    private Intent mResultIntent = null;
    private int mResultCode = 0;
    public static final String TAG = "MainWS";
    boolean isCapture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScreenShortBtn = (Button) findViewById(R.id.btn_screen_short);
        mScreenRecordBtn = (Button) findViewById(R.id.btn_screen_record);
        mScreenShortBtn.setOnClickListener(this);
        mScreenRecordBtn.setOnClickListener(this);
        mMpMngr = (MediaProjectionManager) getApplicationContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mResultIntent = MyApplication.getResultIntent();
        mResultCode = MyApplication.getResultCode();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                //有悬浮窗权限开启服务绑定 绑定权限
                //没有悬浮窗权限m,去开启悬浮窗权限
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mScreenShortBtn) {
            isCapture = true;
            startIntent();
            stopService(new Intent(getApplicationContext(), RecordService.class));
        } else if (view == mScreenRecordBtn) {
            isCapture = false;
            startIntent();
            stopService(new Intent(getApplicationContext(), CaptureService.class));
        }
    }

    private void startIntent() {
        if (mResultIntent != null && mResultCode != 0) {
            startService(new Intent(getApplicationContext(), isCapture ? CaptureService.class : RecordService.class));
        } else {
            startActivityForResult(mMpMngr.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode == RESULT_OK) {
                Log.e(TAG, "get capture permission success!");
                mResultCode = resultCode;
                mResultIntent = data;
                MyApplication.setResultCode(resultCode);
                MyApplication.setResultIntent(data);
                MyApplication.setMpmngr(mMpMngr);
                startService(new Intent(getApplicationContext(), isCapture ? CaptureService.class : RecordService.class));

            }
        } else if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            Log.e(TAG, "get OVERLAY_PERMISSION permission success!");
        }
    }
}
