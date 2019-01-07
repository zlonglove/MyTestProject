package zlonglove.cn.voice;

/**
 *
 */

public interface IVoiceChatPlayerEventListener {
	
	public static int PLAYBACK_ERROR_INPUT_FILE_NOT_FOUND = -1;
	public static int PLAYBACK_WARNING_OUTPUT_FILE_NOT_CREATED = 1;
	void onPlaybackErrorOrWarning(int errorOrWarning);
	void onPlaybackAudioReady(int millisec);
	void onPlaybackStopped();
	void onPlaybackOk();
}
