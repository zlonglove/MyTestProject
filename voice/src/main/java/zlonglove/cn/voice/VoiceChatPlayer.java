package zlonglove.cn.voice;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.util.Log;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;


public class VoiceChatPlayer {

    private static final String TAG = VoiceChatPlayer.class.getSimpleName();
    private IVoiceChatPlayerEventListener _eventListener;
    private volatile VoicePlaybackAsyncTask _currentTask;
    private PlayerConfig _PlayerConfig;

    public enum PlayWith {
        PlayWithSpeaker,
        PlayWithPhoneReceiver
    }

    private class PlayerConfig {
        int amrMode;
        PlayWith playMode;
    }

    public VoiceChatPlayer() {
        _PlayerConfig = null;
    }

    public void setEventListener(IVoiceChatPlayerEventListener eventListener) {
        this._eventListener = eventListener;
    }

    public IVoiceChatPlayerEventListener getEventListener() {
        return _eventListener;
    }

    public void setPlayerConfig(int amrMode) {
        _PlayerConfig = new PlayerConfig();
        _PlayerConfig.amrMode = amrMode;
        _PlayerConfig.playMode = PlayWith.PlayWithSpeaker;
    }

    public void setPlayerConfig(int amrMode, PlayWith playMode) {
        _PlayerConfig = new PlayerConfig();
        _PlayerConfig.amrMode = amrMode;
        _PlayerConfig.playMode = playMode;
    }

    public void startPlayback() {
        _currentTask = (VoicePlaybackAsyncTask) (new VoicePlaybackAsyncTask(_PlayerConfig))
                .execute(_PlayerConfig);
    }

    public void stopPlayback(boolean immediate) {
        Log.i("==test==voice==", "VoiceChatPlayer stopPlayback stopPlayback in immediate:" + immediate);
        VoicePlaybackAsyncTask task = _currentTask;
        if (immediate) {
            if (task != null) {

                task.forceStopPlayback();
            }
            _currentTask = null;
        } else {
            if (task != null) {
                Log.i("==test==voice==", "VoiceChatPlayer notifyDataOver");
                task.notifyDataOver();
            }
        }
    }

    public void writeAmrFileToBuffer(String filePath) {
        VoicePlaybackAsyncTask task = _currentTask;
        if (task != null) {
            task.inputAmrFileToBuffer(filePath);
        }
    }

    public void writeAmrBuffer(byte[] amr) {
        VoicePlaybackAsyncTask task = _currentTask;
        if (task != null) {
            task.inputAmrBuffer(amr);
        }
    }

    private class AsyncTaskProgress {
        int errorOrWarning;
        int readyAudioDataInMillisec;

        AsyncTaskProgress(int errorOrWarning, int readyAudioDataInMillisec) {
            this.errorOrWarning = errorOrWarning;
            this.readyAudioDataInMillisec = readyAudioDataInMillisec;
        }
    }

    private final class VoicePlaybackAsyncTask extends AsyncTask<PlayerConfig, AsyncTaskProgress, Object> {
        private AudioTrack _track;
        private PlayerConfig _config;
        private LinkedBlockingQueue<byte[]> _queue;

        VoicePlaybackAsyncTask(PlayerConfig config) {
            Log.i("==test==voice==", "new VoicePlaybackAsyncTask:" + this);
            _config = config;
            _queue = new LinkedBlockingQueue<byte[]>();
            int sampleRateInHz = 8000;
            int channelConfig = AudioFormat.CHANNEL_OUT_MONO;
            int minBufferSizeInByte = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, AudioFormat.ENCODING_PCM_16BIT);
            int bufferSizeInBytes = minBufferSizeInByte * 3;
            int streamType = (config.playMode == PlayWith.PlayWithSpeaker) ? AudioManager.STREAM_MUSIC : AudioManager.STREAM_VOICE_CALL;
//			if (_track == null) {
            _track = new AudioTrack(streamType, sampleRateInHz, channelConfig, AudioFormat.ENCODING_PCM_16BIT,
                    bufferSizeInBytes, AudioTrack.MODE_STREAM);
//			}
            _track.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
        }

        @Override
        protected void onProgressUpdate(AsyncTaskProgress... values) {
            if (_eventListener != null) {
                if (values[0].errorOrWarning != 0)
                    _eventListener.onPlaybackErrorOrWarning(values[0].errorOrWarning);
                if (values[0].readyAudioDataInMillisec > 0)
                    _eventListener.onPlaybackAudioReady(values[0].readyAudioDataInMillisec);
            }
        }

        public void forceStopPlayback() {
            Log.i("==test==voice==", "VoiceChatPlayer forceStopPlayback:");
            this.cancel(true);

            Log.i("==test==voice==", "VoiceChatPlayer forceStopPlayback 11");
            try {
                if (_track != null) {
                    _track.stop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            VoiceAction voiceAction = VoiceAction.getInstance();
            voiceAction.PlayOver = false;
            if (_eventListener != null && !voiceAction.isSwitchSpeaker) {
                _eventListener.onPlaybackStopped();
            }
            voiceAction.isSwitchSpeaker = false;
        }

        void inputAmrBuffer(byte[] amr) {
            try {
                _queue.put(amr);
            } catch (InterruptedException e) {
                // do nothing
                Log.e(TAG, "inputAmrBuffer run into InterruptedException : ", e);
            }
        }

        void inputAmrFileToBuffer(String filePath) {
            FileInputStream fis = null;
            try {
                File file = new File(filePath);
                if (file.exists()) {
                    fis = new FileInputStream(file);
                } else {
                    Log.i("==test==voice==", "文件不存在或已损坏");
                    return;
                }
            } catch (Exception e) {
                Log.i("==test==voice==", "文件不存在或已损坏");
                e.printStackTrace();
                return;
            }
            do {
                try {
                    int size = fis.available();
                    if (size <= 6)
                        break;
                    byte[] header = new byte[6];
                    fis.read(header);
                    String strheader = new String(header);
                    if (!strheader.equals("#!AMR\n")) {
                        break;
                    }
                    int frameSize = 1000;
                    int inputSize = 0;
                    for (int i = size - 6; i > 0; i -= inputSize) {
                        inputSize = Math.min(frameSize * 10, i);
                        byte[] amr = new byte[inputSize];
                        fis.read(amr);
                        inputAmrBuffer(amr);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            } while (false);
            if (fis != null)
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        void notifyDataOver() {
            byte[] end = new byte[0];
            VoiceAction.getInstance().PlayOver = true;
            try {
                _queue.put(end);
            } catch (InterruptedException e) {
                // do nothing
                e.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(PlayerConfig... params) {
            PlayerConfig config = params[0];
            if (_track.getState() == AudioTrack.STATE_UNINITIALIZED) {
                _track.release();
                _track = null;
                return null;
            }
            _track.play();
            blockingDecodeAndInput();
            try {
                _track.stop();
                // _track.release();
            } catch (IllegalStateException e) {
                // do nothing
                e.printStackTrace();
            }
            return null;
        }

        private void blockingDecodeAndInput() {
            // int amrFrameSize = OpenCoreAmr.getFrameSizeByAmrMode(amrMode);
            do {
                byte[] amr = null;
                int amrLen = 0;
                // if (fis == null)
                try {
                    amr = _queue.take();
                    amrLen = amr.length;
                } catch (InterruptedException e) {
                    amrLen = 0;
                }
                if (amr == null || amrLen == 0) {
                    break;
                }
//				IMLog.d(TAG, "play amr data " + amr.length);

                if (_track.getPlayState() != AudioTrack.PLAYSTATE_STOPPED) {
                    for (int written = 0; written < amr.length && _track.getPlayState() != AudioTrack.PLAYSTATE_STOPPED; ) {
                        int ret = _track.write(amr, written, amr.length - written);
                        Log.d(TAG, "play speech data: " + amr.length + ", and ret: " + ret);
                        publishProgress(new AsyncTaskProgress(0, ret / 8));
                        _track.flush();
                        written += ret;
                    }
                }
                amr = null;
            }
            while (true);
        }

        @Override
        protected void onPostExecute(Object result) {
            if (_currentTask == this)
                _currentTask = null;
            if (_eventListener != null)
                _eventListener.onPlaybackStopped();
            VoiceAction.getInstance().mLastPlayPath = null;
        }
    }
}
