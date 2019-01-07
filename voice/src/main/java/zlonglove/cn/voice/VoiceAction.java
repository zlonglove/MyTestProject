package zlonglove.cn.voice;

import android.text.TextUtils;
import android.util.Log;


import java.util.ArrayList;


public class VoiceAction {

	private static final String TAG = VoiceAction.class.getSimpleName();
	public static final int AMR_MODE = 1;

	private static VoiceAction mVoiceAction = null;
	private VoiceChatRecorder mRecorder;
	public VoiceChatPlayer mPlayer;
	private IVoiceChatRecorderEventListener recorderListener = null;
	private IVoiceChatPlayerEventListener playerListener = null;

	private boolean isPlaying = false;
	private boolean isRecording = false;
	public String mLastPlayPath = null;
	private int mLastPlayMode = 0; // 记录上次播放语音时所用的速率,因为平台间不统一
	public boolean isSwitchSpeaker = false; // 标识是否切换扬声器或听筒
	public boolean PlayOver = false;  //标识已经完全播放完全

	public static VoiceAction getInstance() {
		if (mVoiceAction == null) {
			synchronized (VoiceAction.class) {
				if (mVoiceAction == null) {
					mVoiceAction = new VoiceAction();
				}
			}
		}
		return mVoiceAction;
	}

	public IVoiceChatPlayerEventListener getPlayerListener() {
		return playerListener;
	}

	public void setPlayerListener(IVoiceChatPlayerEventListener playerListener) {
		this.playerListener = playerListener;
	}

	public IVoiceChatRecorderEventListener getRecorderListener() {
		return recorderListener;
	}

	public void setRecorderListener(
			IVoiceChatRecorderEventListener recorderListener) {
		this.recorderListener = recorderListener;
	}

	public boolean isRecording() {
		return isRecording;
	}

	public void setRecording(boolean isRecording) {
		this.isRecording = isRecording;
	}

	public void startPlayByCash(ArrayList<byte[]> buffer, int amrMode) {
		playAmrBuffer(buffer, amrMode);
	}

	public void startPlay(ArrayList<byte[]> buffer, int amrMode) {
		playMoodBuffer(buffer, amrMode);
	}

	public void startPlay(String path, int amrMode) {
		playFile(path, amrMode);
		mLastPlayPath = path;
		mLastPlayMode = amrMode;
	}

	public void rePlay(int amrMode, boolean playBySpeaker) {
		Log.i("==test==voice==","rePlay lastpath:"+mLastPlayPath);
		if (!TextUtils .isEmpty(mLastPlayPath)) {
			isPlaying = true;
			// IMLog.d("s_tag", "rePlay - >isPlaying = true");
			if (mPlayer == null) {
				mPlayer = new VoiceChatPlayer();
				Log.d(TAG, "rePlay new VoiceChatPlayer");
				Log.i("==test==voice==","mPlayer == null");
			}
			if (playBySpeaker) {
				mPlayer.setPlayerConfig(mLastPlayMode,
						VoiceChatPlayer.PlayWith.PlayWithSpeaker);
				Log.i("==test==voice==","rePlay playBySpeaker");
			} else { // 听筒模式
				mPlayer.setPlayerConfig(mLastPlayMode,
						VoiceChatPlayer.PlayWith.PlayWithPhoneReceiver);
				Log.i("==test==voice==","rePlay PlayWithPhoneReceiver");
			}
			if (playerListener != null && mPlayer.getEventListener() == null) {
				mPlayer.setEventListener(playerListener);
			}
			mPlayer.startPlayback();
			mPlayer.writeAmrFileToBuffer(mLastPlayPath);
			mPlayer.stopPlayback(false);
			//SessionBoxMananger.getInstance().setTime();
		}
	}

	private void playFile(String path, int amrMode) {
		preparePlayer(amrMode);
		mPlayer.writeAmrFileToBuffer(path);
		mPlayer.stopPlayback(false);

		// StopPlay(false);
	}

	private void playAmrBuffer(ArrayList<byte[]> buffers, int amrMode) {
		preparePlayer(amrMode);
		for (byte[] buffer : buffers) {
			appendAmrBuffer(buffer);
		}

	}

	private void playMoodBuffer(ArrayList<byte[]> buffers, int amrMode) {
		preparePlayer(amrMode);
		for (byte[] buffer : buffers) {
			appendAmrBuffer(buffer);
		}
		StopPlay(false);
	}

	private void preparePlayer(int amrMode) {
		isPlaying = true;
		// IMLog.d("s_tag", "preparePlayer - >isPlaying = true");
		if (mPlayer == null) {
			mPlayer = new VoiceChatPlayer();
		}
		mPlayer.setPlayerConfig(amrMode,
				VoiceChatPlayer.PlayWith.PlayWithSpeaker);
		// mPlayer.setPlayerConfig(amrMode);
		if (playerListener != null) {
			mPlayer.setEventListener(playerListener);
		}
		mPlayer.startPlayback();
	}

	public void StartRecord(String path, int mode) {
		StartRecord(path, mode, true);
	}

	public void StartRecord(String path, int mode, boolean enableOutputToFile) {
		isRecording = true;

		if (mRecorder == null) {
			mRecorder = new VoiceChatRecorder();
		}
		mRecorder.setRecorderConfig(AMR_MODE, enableOutputToFile, path, mode);
		if (recorderListener != null) {
			mRecorder.setEventListener(recorderListener);
		}
		mRecorder.startRecording();
	}

	public void StopRecord() {
		if (mRecorder != null) {
			mRecorder.stopRecording();
			mRecorder = null;
			isRecording = false;
		}
	}

	public void playbackStopped() {
		isPlaying = false;
	}

	public void appendAmrBuffer(byte[] buffer) {
		if (mPlayer == null)
			return;
		mPlayer.writeAmrBuffer(buffer);
	}

	public void StopPlay(boolean immediate) {
		Log.i("==test==voice==","VoiceAction stopplay in immediate:"+immediate);
		if (mPlayer != null) {
			Log.i("==test==voice==","VoiceAction stopplay mPlayer not null");
			mPlayer.stopPlayback(immediate);
			if (immediate) {
				isPlaying = false;
				// IMLog.d("s_tag", "StopPlay - >isPlaying = false");
				Log.d(TAG, "StopPlay immediate! ");
				if (playerListener != null) {
					playerListener.onPlaybackOk();
				}
			}
		}
	}

	public boolean isPlaying() {
		return isPlaying;
	}
}