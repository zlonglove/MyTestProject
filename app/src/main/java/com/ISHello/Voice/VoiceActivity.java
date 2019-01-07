package com.ISHello.Voice;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.ISHello.base.base.BaseActivity;
import com.ISHello.utils.SdcardUtils;
import com.example.ishelloword.R;

import java.io.File;
import java.util.ArrayList;

import zlonglove.cn.voice.IVoiceChatRecorderEventListener;
import zlonglove.cn.voice.VoiceAction;
import zlonglove.cn.voice.VoiceChatRecorder;

public class VoiceActivity extends BaseActivity {
    protected ArrayList<byte[]> amrBuff = new ArrayList<byte[]>();
    protected VoiceChatRecorder mVoiceRecord = null;
    protected String voicePath = null;
    protected int mRecordValue = 0;
    protected int mVoiceTime = 0;
    protected int mRecordFileSize; // 记录录音时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        init();
    }

    private void init() {
        String sdpath = SdcardUtils.getSDCardPathWithFileSeparators() + "amrr";
        File file = new File(sdpath);
        if (!file.exists()) {
            file.mkdirs();
        }
        voicePath = sdpath +File.separator+ String.valueOf(System.currentTimeMillis()) + ".amrr";
    }

    public void start(View view) {
        startRecording();
    }

    public void stop(View view) {
        stopRecording();
    }

    protected synchronized void startRecording() {
        amrBuff.clear();
        if (VoiceAction.getInstance().isPlaying()) {
            VoiceAction.getInstance().StopPlay(true);
        }
        if (mVoiceRecord == null) {
            mVoiceRecord = new VoiceChatRecorder();
            mVoiceRecord.setEventListener(mVoiceRecorderListener);
        }
        Thread t = new Thread(mRecordingInitRunnable);
        t.start();
    }

    protected void stopRecording() {
        if (isFinishing())
            return;
        try {
            mVoiceRecord.stopRecording(); // 停止录音
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runnable mRecordingInitRunnable = new Runnable() {
        @Override
        public void run() {
            if (mVoiceRecord != null && !TextUtils.isEmpty(voicePath)) {
                mVoiceRecord.setRecorderConfig(VoiceAction.AMR_MODE, true, voicePath, mRecordValue);
                mVoiceRecord.startRecording();
            }
        }
    };

    protected IVoiceChatRecorderEventListener mVoiceRecorderListener = new IVoiceChatRecorderEventListener() {
        private long startTimeStamp;

        @Override
        public void onRecordingStarted() {
            startTimeStamp = System.currentTimeMillis();
        }

        @Override
        public void onRecordingFailed() {

        }

        @Override
        public void onAmrBufferOutput(byte[] amr) {
            long time = System.currentTimeMillis() - startTimeStamp;
            mVoiceTime = (int) (time / 1000);
            if (time % 1000 > 500) {
                mVoiceTime += 1;
            }
            mRecordFileSize += amr.length;
        }

        @Override
        public void onRecordingStopped() {

        }

        @Override
        public void onVoiceLevelNotify(int level) {

        }
    };
}
