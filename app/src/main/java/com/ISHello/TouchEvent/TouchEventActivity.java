package com.ISHello.TouchEvent;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

public class TouchEventActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {
    private final String TAG = "TouchEventActivity";
    private MyTextView myTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_event);
        myTextView = (MyTextView) findViewById(R.id.my_text_view);
        myTextView.setOnClickListener(this);
        myTextView.setOnTouchListener(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Log.i(TAG, "--->dispatchTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.i(TAG, "--->dispatchTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "--->dispatchTouchEvent ACTION_UP");
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Log.i(TAG, "--->onTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.i(TAG, "--->onTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "--->onTouchEvent ACTION_UP");
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_text_view:
                Log.i(TAG, "--->MyTextView onClick");
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.my_text_view:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //Log.i(TAG, "--->onTouch ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //Log.i(TAG, "--->onTouch ACTION_MOVE");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "--->onTouch ACTION_UP");
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return false;
    }
}
