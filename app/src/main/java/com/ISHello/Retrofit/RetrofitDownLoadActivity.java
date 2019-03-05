package com.ISHello.Retrofit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ISHello.CustomToast.CustomToast;
import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

public class RetrofitDownLoadActivity extends BaseActivity {
    private SeekBar sb_down;
    private TextView tv_down;
    private Button btn_start;
    private Button btn_cancl;
    //private String url = "http://download.sdk.mob.com/apkbus.apk";
    private String url = "http://122.19.173.71:8011/mimsapp/1904test/0305/IMAPP_C_ANDROID_0305_11_12.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_down_load);
        setTitle("Retrofit文件下载");
        findViews();
        init();
    }

    private void findViews() {
        sb_down = findViewById(R.id.sb_down);
        tv_down = findViewById(R.id.tv_down);
        btn_start = findViewById(R.id.btn_start);
        btn_cancl = findViewById(R.id.btn_cancl);
    }

    private void init() {
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitHelper.getInstance().download(url, new CallBack<String>() {

                    @Override
                    public void onSuccess(String filePath) {
                        CustomToast.makeText(RetrofitDownLoadActivity.this, "下载成功!!" + filePath, Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onError(String info) {
                        CustomToast.makeText(RetrofitDownLoadActivity.this, info, Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onProgress(long totalSize, long downSize) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int progress = (int) (downSize * 100 / totalSize);
                                tv_down.setText(progress + "%");
                                sb_down.setProgress(progress);
                            }
                        });
                    }
                });
            }
        });

        btn_cancl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitHelper.getInstance().pause(url);
            }
        });

    }
}
