package com.ISHello.ProgressBar;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ISHello.ProgressBar.RoundProgressBarNumber.RoundProgressBarWidthNumber;
import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;


public class CProgressBarActivity extends BaseActivity {
    private Dialog mDownloadDialog;
    private RoundProgressBarWidthNumber mRoundProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cprogress_bar);
        CProgressButton.initStatusString(new String[]{"download", "pause", "complete", "error", "delete"});
        button1();
    }

    public void CircleNumberProgress(View view) {
        showDownloadDialog();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
        valueAnimator.setDuration(5000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (value == 100) {
                    //mRoundProgressBar
                    mDownloadDialog.dismiss();
                }
                mRoundProgressBar.setProgress(value);
            }
        });
        valueAnimator.start();
    }

    private void button1() {
        final CProgressButton progressButton = (CProgressButton) findViewById(R.id.btn);

        final TextView tv = (TextView) findViewById(R.id.state);
        progressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
                if (progressButton.getState() == CProgressButton.STATE.NORMAL) {
                    progressButton.startDownLoad();
                    valueAnimator.setDuration(5000);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int value = (int) animation.getAnimatedValue();
                            tv.setText("state progress:" + value);
                            if (value == 100) {
                                progressButton.normal(2);
                                tv.setText("state normal");
                            }
                            progressButton.download(value);
                        }
                    });
                    valueAnimator.start();
                } else {
                    valueAnimator.cancel();
                    progressButton.normal(4);
                    tv.setText("state normal");
                }

            }
        });
    }

    /**
     * 显示软件下载对话桿
     */
    private void showDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Base_Theme_AlertDialogPro_ICBC);
        builder.setTitle("正在更新");
        // 给下载对话框增加进度板
        final LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mRoundProgressBar = (RoundProgressBarWidthNumber) v.findViewById(R.id.id_progress02);
        // mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        // update_text = (TextView) v.findViewById(R.id.update_text);
        builder.setView(v);
        // 取消更新
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
    }
}
