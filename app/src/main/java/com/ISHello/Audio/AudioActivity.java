package com.ISHello.Audio;

import com.example.ishelloword.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class AudioActivity extends Activity {
    private static final String TAG = "AudioActivity";
    private SeekBar seekBar;
    private Button muteButton;
    private AudioTrackPlay audioTrackPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audioactivity);
        progressBarInit();
        Bundle bundle = getIntent().getExtras();
        muteButton = (Button) this.findViewById(R.id.muteButton);
        Log.i(TAG, "--->The get info==" + bundle.getString("Audio"));
    }

    private void progressBarInit() {
        seekBar = (SeekBar) this.findViewById(R.id.PlayVol);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i(TAG, "--->Current Voice is==" + progress);
                AudioPlayer.getRecorder(AudioActivity.this).SetVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i(TAG, "拖动中...");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i(TAG, "拖动完毕");
            }

        });
    }

    public void openAudio(View view) {
        AudioPlayer player = AudioPlayer.getRecorder(AudioActivity.this);
        player.open(AudioPlayer.RECORD_PLAY);
        player.start();
    }

    public void closeAudio(View view) {
        AudioPlayer.getRecorder(AudioActivity.this).close();
    }

    public void mute(View view) {
        AudioPlayer player = AudioPlayer.getRecorder(AudioActivity.this);
        if (player.isMute()) {
            player.setMute(false);
            muteButton.setText(R.string.Mute);
        } else {
            player.setMute(true);
            muteButton.setText(R.string.NoMute);
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        AudioPlayer player = AudioPlayer.getRecorder(AudioActivity.this);
        if (player != null) {
            player.close();
        }
    }

    public void playAssert(View view) {
        Log.i(TAG, "--->playAssert(View view)");
        if (audioTrackPlay == null) {
            audioTrackPlay = new AudioTrackPlay(this);
        }
        audioTrackPlay.open();
        audioTrackPlay.playAssert("start.pcm");

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
