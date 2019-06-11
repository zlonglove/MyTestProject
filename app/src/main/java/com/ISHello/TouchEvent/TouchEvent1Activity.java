package com.ISHello.TouchEvent;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

public class TouchEvent1Activity extends BaseActivity {
    protected static final String TAG = TouchEvent1Activity.class.getSimpleName();
    private Button mButton;
    private LinearLayout parent_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_event1);

        mButton = (Button) findViewById(R.id.id_btn);
        parent_layout=(LinearLayout)findViewById(R.id.parent_layout);
        mButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "--->onTouch ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.e(TAG, "--->onTouch ACTION_MOVE");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e(TAG, "--->onTouch ACTION_UP");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "--->Button onClick()");
            }
        });

        parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "--->LinearLayout onClick()");
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.i(TAG, "--->dispatchKeyEvent()");
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "--->onTouchEvent()");
        return super.onTouchEvent(event);
    }
}
