package com.ISHello.Audio;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * 录音器，同时支持录音和即录即放两种模式
 */
public class AudioPlayer {
    final String TAG = "AudioPlayer";
    boolean isRecording = false;        //是否录放的标记
    final int frequency = 44100;             //频率
    @SuppressWarnings("deprecation")
    final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_STEREO;  //通道配置(双声道)
    final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;  //PCM 16 bit per sample

    AudioRecord audioRecord = null;  //录音
    int recBufSize;

    AudioTrack audioTrack = null;    //播放
    int playBufSize = -1;
    int currentVoice = -1;
    boolean isMute = false;

    static AudioPlayer me = null;
    private AudioManager audioManager;
    private Context context;

    public static final int ONLY_RECORD = 1;  //只录模式
    public static final int RECORD_PLAY = 2;  //录放模式

    //限制外部使用构造函数
    private AudioPlayer(Context context) {
        this.context = context;
    }

    //向外部提供唯一实例
    public static AudioPlayer getRecorder(Context context) {
        if (me == null) {
            me = new AudioPlayer(context);
        }
        return me;
    }

    //打开录音器：mode == ONLY_RECORD 为只录模式
    //mode == RECORD_PLAY 为录放模式
    public boolean open(int mode) {

        if (mode != ONLY_RECORD && mode != RECORD_PLAY)
            return false;

        recBufSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
        if (recBufSize == AudioRecord.ERROR_BAD_VALUE || recBufSize == AudioRecord.ERROR)
            return false;
        Log.i(TAG, "----->Record buffer size==" + recBufSize);

        //recBufSize *= 2; //扩大10倍
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
                channelConfiguration, audioEncoding, recBufSize);

        //录放模式时创建播放
        if (mode == RECORD_PLAY) {
            playBufSize = AudioTrack.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
            if (playBufSize == AudioTrack.ERROR_BAD_VALUE || playBufSize == AudioTrack.ERROR)
                return false;

            Log.i(TAG, "----->AudioTrack buffer size==" + playBufSize);

            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,
                    channelConfiguration, audioEncoding,
                    playBufSize, AudioTrack.MODE_STREAM);
        }

        return true;
    }


    /**
     * 设置播放Track的声音大小
     *
     * @param vol streamType:声音类型，可取的为STREAM_VOICE_CALL（打电话时的声音）,
     *            STREAM_SYSTEM（Android系统声音）,
     *            STREAM_RING（电话铃响）,
     *            STREAM_MUSIC（音乐声音）
     *            STREAM_ALARM（警告声音）。
     *            0----16
     */
    public void SetVolume(int vol) {
        if (audioManager == null) {
            audioManager = (AudioManager) this.context
                    .getSystemService(Context.AUDIO_SERVICE);
        }
        if (vol >= 0) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
            //audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,
            //        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
        }
    }


    /**
     * 设置静音
     *
     * @param mute true--静音
     *             false-不静音
     */
    public void setMute(boolean mute) {
        if (audioManager == null) {
            audioManager = (AudioManager) this.context
                    .getSystemService(Context.AUDIO_SERVICE);
        }
        this.isMute = mute;
        if (mute) {
            currentVoice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            Log.i("volume", "--->volume music before==" + currentVoice);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            Log.i("volume",
                    "--->volume music after=="
                            + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVoice, 0);
        }

    }

    public boolean isMute() {
        return isMute;
    }

    //查看是否正在运行
    public boolean isRecording() {
        return this.isRecording;
    }

    //开始播放
    public void start() {
        if (!isRecording) {
            isRecording = true;
            new MicRecordThread().start(); //启动录制线程
        }
    }


    //停止播放
    public void stop() {
        isRecording = false;
    }

    //销毁
    public void close() {
        if (audioRecord != null) {
            if (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                audioRecord.stop();
            }
            audioRecord.release();
            audioRecord = null;
        }

        if (audioTrack != null) {
            if (audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                audioTrack.stop();
            }
            audioTrack.release();
            audioTrack = null;
        }
        isRecording = false;
    }

    //录制线程，支持即录即放
    private class MicRecordThread extends Thread {
        public void run() {
            try {
                byte[] buffer = new byte[recBufSize];
                audioRecord.startRecording();// 开始录制

                if (audioTrack != null)
                    audioTrack.play();// 开始播放
                int readBufferResultSize = 0;

                while (isRecording) {
                    // 从MIC保存数据到缓冲区
                    if (audioRecord != null) {
                        readBufferResultSize = audioRecord.read(buffer, 0, recBufSize);
                    }
                    //System.out.println(">>>>MicRecordThread readBufferResultSize = "+readBufferResultSize);
                    // 从缓存区中拷贝实际读到的数据
                    //byte[] tmpBuf = new byte[readBufferResultSize];
                    //System.arraycopy(buffer, 0, tmpBuf, 0, readBufferResultSize);

                    // 写入数据即播放
                    if (audioTrack != null)
                        audioTrack.write(buffer, 0, readBufferResultSize);
                }
                // 停止播放
                if (audioTrack != null)
                    audioTrack.stop();

                // 停止录制
                if (audioRecord != null)
                    audioRecord.stop();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
