package com.ISHello.Audio;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class AudioTrackPlay {

    private Context context;
    private AudioTrack audioTrack;
    private final String TAG = "AudioTrackPlay";

    private final int frequency = 8000;             //频率
    @SuppressWarnings("deprecation")
    private final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_STEREO;  //通道配置(双声道)
    private final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;  //PCM 16 bit per sample
    private int bufferSize = -1;
    private PlayThread playThread;

    public AudioTrackPlay(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public void playAssert(String path) {
        try {
            InputStream in = context.getResources().getAssets()
                    .open(path);
            if (in == null) {
                return;
            }
            if (audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                audioTrack.stop();
                playThread.interrupt();
            }
            playThread = new PlayThread();
            playThread.setInputStream(in);
            playThread.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public class PlayThread extends Thread {
        InputStream in;
        int playBufferSize = -1;
        byte[] playBuffer;

        public PlayThread() {

            playBuffer = new byte[bufferSize];
        }

        public void setInputStream(InputStream in) {
            this.in = in;
        }

        @Override
        public void run() {
            if (this.in != null) {
                try {
                    audioTrack.play();
                    while ((playBufferSize = in.read(playBuffer, 0, bufferSize)) > 0) {
                        Log.i(TAG, "--->read buffer size==" + playBufferSize);
                        audioTrack.write(playBuffer, 0, playBufferSize);
                    }
                    audioTrack.stop();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean open() {
        if (audioTrack == null) {
            bufferSize = AudioTrack.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
            if (bufferSize == AudioTrack.ERROR_BAD_VALUE || bufferSize == AudioTrack.ERROR)
                return false;

            Log.i(TAG, "----->AudioTrack buffer size==" + bufferSize);

            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,
                    channelConfiguration, audioEncoding,
                    bufferSize, AudioTrack.MODE_STREAM);
            playThread = new PlayThread();
        }
        return true;
    }

    public int write(byte[] buffer, int offset, int buffersize) {
        // 写入数据即播放
        if (audioTrack != null) {
            return audioTrack.write(buffer, offset, buffersize);
        } else {
            return -1;
        }
    }


}
