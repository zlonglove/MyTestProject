package zlonglove.cn.voice;

/**
 *
 */

public interface IVoiceChatRecorderEventListener {
	void onRecordingStarted();
	void onRecordingFailed();
	void onAmrBufferOutput(byte[] amr);
	void onRecordingStopped();
	void onVoiceLevelNotify(int level);
}
