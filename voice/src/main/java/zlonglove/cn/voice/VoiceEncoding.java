package zlonglove.cn.voice;

import android.util.Log;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class VoiceEncoding extends Thread {
    private static final String TAG = VoiceEncoding.class.getSimpleName();
    private BlockingQueue<SpeechBuffer> mQueue;
    private RecorderConfig _config;
    private IVoiceChatRecorderEventListener _eventListener;

    VoiceEncoding(RecorderConfig config, IVoiceChatRecorderEventListener eventListener) {
        this.setName("VoiceEncoding");

        _config = config;
        _eventListener = eventListener;
        mQueue = new LinkedBlockingQueue<SpeechBuffer>();
    }

    void PutSpeechBuffer(SpeechBuffer speech) {
        try {
            mQueue.put(speech);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Log.d(TAG, "VoiceEncodingThread  start ------------------------------------------>");
        FileOutputStream fs = null;
        if (_config.enableOutputToFile) {
            try {
                fs = new FileOutputStream(_config.filePath);
                //byte[] header = ("#!AMR\n").getBytes();
                //fs.write(header);
            } catch (FileNotFoundException e) {
                fs = null;
            }
        }

        while (true) {
            SpeechBuffer sb;
            try {
                sb = mQueue.take();
            } catch (InterruptedException e) {
                sb = null;
            }
            if (sb == null)
                break;
            if (sb.close)
                break;
            // long time0 = System.currentTimeMillis();
            //byte[] amr = OpenCoreAmr.encodeSessionEncode(sessionId, sb.buffer, sb.offset, sb.len);

            // TaskProgress entry = new TaskProgress(TaskProgressType.RECORDING_PROGRESS);

            _eventListener.onAmrBufferOutput(sb.buffer);

            // publishProgress(entry);

            if (fs != null) {
                try {
                    fs.write(sb.buffer);
                } catch (IOException e) {
                    // fs = null;
                    Log.e(TAG, "AsyncRecordingTask save amrFile RunInto IOException ", e);
                }
            }

        }
        if (fs != null) {
            try {
                fs.flush();
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        _eventListener.onRecordingStopped();
        Log.d(TAG, "VoiceEncodingThread  end ------------------------------------------>");
    }
}
