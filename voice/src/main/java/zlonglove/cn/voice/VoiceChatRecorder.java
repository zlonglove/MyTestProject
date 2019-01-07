package zlonglove.cn.voice;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;


public class VoiceChatRecorder {

    private static final String TAG = VoiceChatRecorder.class.getSimpleName();
    // 音频获取源
    private static int audioSource = MediaRecorder.AudioSource.MIC;
    // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    private static int sampleRateInHz = 8000;
    // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_IN_MONO为单声道
    private static int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
    private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

    protected static int VOICECLIPSIZE = 16000;
    private AudioRecord REC = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, VOICECLIPSIZE);

    private volatile Thread _currentTask;
    private IVoiceChatRecorderEventListener _eventListener;
    private RecorderConfig _soundConfig;
    private boolean _cancel = false;
    private VoiceEncoding encoding;

    public VoiceChatRecorder() {
        _soundConfig = null;
//		REC = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, VOICECLIPSIZE);
//		VOICECLIPSIZE = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
    }

    public void setEventListener(IVoiceChatRecorderEventListener listener) {
        _eventListener = listener;
    }

    public void setRecorderConfig(int amrMode, boolean enableOutputToFile, String filePath, int pitchSemiTones) {
        RecorderConfig config = new RecorderConfig();
        config.amrMode = amrMode;
        config.pitchSemiTones = pitchSemiTones;
        config.tempoChange = 0.0f;
        config.enableOutputToFile = enableOutputToFile;
        config.filePath = filePath;
        _soundConfig = config;
    }

    @Deprecated
    public void setRecorderConfig(int amrMode, boolean enableOutputToFile, String filePath) {
        setRecorderConfig(amrMode, enableOutputToFile, filePath, 0);
    }

    public synchronized void startRecording() {
        Log.d(TAG, "startRecording ()" + _currentTask);
        if (_currentTask == null) {
            _cancel = false;
            _currentTask = new Thread(recorder, "RecorderThread");
            _currentTask.start();
        }
    }

    public synchronized void stopRecording() {
        if (_currentTask != null) {
            _cancel = true;
            REC.stop();
            while (_currentTask.isAlive()) {
                try {
                    Thread.sleep(50);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (encoding != null) {
                while (encoding.isAlive()) {
                    try {
                        Thread.sleep(50);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            _currentTask = null;
            encoding = null;
            REC.release();
        }
    }

    private Runnable recorder = new Runnable() {
        @Override
        public void run() {
            try {
                REC.startRecording();
                Log.d(TAG, "startrecording after");

                _eventListener.onRecordingStarted();
            } catch (Throwable ex) {
                Log.d(TAG, "startrecording fail");
                ex.printStackTrace();
                _eventListener.onRecordingFailed();
                return;
            }
            encoding = new VoiceEncoding(_soundConfig, _eventListener);
            encoding.start();
            int ret = 0;
            Log.d(TAG, "recording state _cancel = " + _cancel + "");
            while (!_cancel) {
                byte[] speech = new byte[VOICECLIPSIZE];
                Log.d(TAG, "recording state" + REC.getRecordingState() + "getState() " + REC.getState());
                if (REC.getState() == AudioRecord.STATE_INITIALIZED || REC.getState() == AudioRecord.RECORDSTATE_RECORDING) {
                    Log.d(TAG, "AudioRecord状态已经初始化或者正在录音");
                    ret = REC.read(speech, 0, VOICECLIPSIZE);
                } else {
                    Log.d(TAG, "AudioRecord状态不正常 break");
                    break;
                }
                if (ret <= 0) {
                    Log.d(TAG, "表明AudioRecord状态不是成功初始化。");
                    break;
                }
                encoding.PutSpeechBuffer(new SpeechBuffer(speech, 0, ret));

                // 音量判断：音量和/音量数
                int sumRecordValue = 0;
                for (int i = 0; i < speech.length; i++) {
                    sumRecordValue += Math.abs(speech[i]);
                }
                int level = sumRecordValue / ret;
                Log.d(TAG, "recording state level = " + level);
                _eventListener.onVoiceLevelNotify(level);
                Log.d(TAG, "recording InputSpeechBuffer loop");
            }
            REC.stop();

            encoding.PutSpeechBuffer(new SpeechBuffer(true));
            // _encodingTask.InputSpeechBuffer( new SpeechBuffer( true ) );
            Log.d(TAG, "AsyncRecordingTask  doInbackgound end!!!!!!!!!!!!!!!!!!!!! ");
        }
    };
}
