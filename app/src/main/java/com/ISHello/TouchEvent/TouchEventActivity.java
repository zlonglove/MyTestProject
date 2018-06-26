package com.ISHello.TouchEvent;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

public class TouchEventActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "TouchEventActivity";
    private MyTextView myTextView;
    private Button touch_event_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_event);
        setTitle("触摸事件分发");
        myTextView = (MyTextView) findViewById(R.id.my_text_view);
        touch_event_button = (Button) findViewById(R.id.touch_event_button);
        myTextView.setOnClickListener(this);
        ((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0).setBackgroundColor(Color.GREEN);
    }

    private Animator createTranslationAnimations(View view, float startX, float endX, float startY, float endY) {
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "translationX", startX, endX);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "translationY", startY, endY);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY);
        return animSetXY;
    }
   /* @Override
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
                Log.i(TAG, "--->onTouchEvent ACTION_DOWN");
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
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_text_view:
                Log.i(TAG, "--->MyTextView onClick");
                createTranslationAnimations(touch_event_button,-touch_event_button.getWidth(),0,0,0).start();
                break;
        }
    }
}
