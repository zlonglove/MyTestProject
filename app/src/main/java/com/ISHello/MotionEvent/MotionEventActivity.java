package com.ISHello.MotionEvent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ishelloword.R;

public class MotionEventActivity extends Activity {

    private final String TAG = "MotionEventActivity";
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion_event);
        findViews();
        init();
    }

    private void findViews() {
        button = (Button) ((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0).findViewById(R.id.motion_event_button);
    }

    private void init() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "--->Test button click");
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "--->dispatchTouchEvent()");
        //return true;//如果在此处拦截，整个事件循环就结束了
        return super.dispatchTouchEvent(ev);//交给Window进行分发处理
    }
    //Activity中dispatchTouchEvent()原型
    /*public boolean dispatchTouchEvent(MotionEvent ev){
        if (ev.getAction()== MotionEvent.ACTION_DOWN){
            onUserInteraction();
        }
        if (getWindow().superDispatchTouchEvent(ev)){
            return true;
        }
        return onTouchEvent(ev);
    }*/

    //Window的唯一实现是PhoneWindow,处理点击事件如下，交给DecorView
    /*public boolean superDispatchTouchEvent (MotionEvent ev){
        return mDecor.superDispatchTouchEvent(ev);
    }*/
}
