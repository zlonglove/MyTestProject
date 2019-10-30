package com.ISHello.TouchEvent;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

public class TouchEvent1Activity extends BaseActivity {
    protected static final String TAG = TouchEvent1Activity.class.getSimpleName();
    private Button mButton;
    private MyRelativeLayout parent_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_event1);

        mButton = findViewById(R.id.id_btn);
        parent_layout = findViewById(R.id.parent_layout);
        /*mButton.setOnTouchListener(new View.OnTouchListener() {
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
        });*/
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "--->【Activity】下达任务：" + Util.actionToString(ev.getAction()) +
                "，找个人帮我完成，任务往下分发。");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        Log.i(TAG, "--->【Activity】完成任务：" + Util.actionToString(event.getAction()) +
                "，【ViewGroup】太差劲了，以后不再找你干活了，我自来搞定！是否解决：" + Util.canDoTaskTop(result));
        return result;
    }
}
