package com.ISHello.MusicIndicator;

import android.os.Bundle;

import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;
import com.taishi.library.Indicator;

public class MusicIndicatorActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_indicator);

        Indicator indicator = (Indicator) findViewById(R.id.indicator);
		indicator.setBarNum(50); // Set number of bars
 		indicator.setStepNum(20); // Set number of bar-steps (i.e. if you set 2, only 2 levels of bar-height)
		indicator.setDuration(5000); //

		indicator.setBarColor(getResources().getColor(R.color.skyblue)); // Set a color of bar

    }
}
