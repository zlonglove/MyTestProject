package com.ISHello.ViewDraw;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.ISHello.View.FingerView;
import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

public class ViewDrawActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "ViewDrawActivity";
    private DrawView viewDraw;
    private FingerView fingerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("自定义文本绘制");
        setContentView(R.layout.activity_view_draw);
        //setContentView(R.layout.activity_circle_view);
        findView();
        init();
    }

    private void findView() {
        //得到我们设置的ContentView
        ViewGroup content = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        viewDraw = (DrawView) content.findViewById(R.id.view_draw);
        fingerView = content.findViewById(R.id.view_finger);
    }

    private void init() {
        viewDraw.setOnClickListener(this);
        fingerView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_draw:
                Log.i(TAG, "--->ViewDraw click");
                for (int i = 0; i < 1000; i++) {
                    fingerView.invalidate();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                       /* for (int i = 0; i < 100000; i++) {
                            viewDraw.postInvalidate();
                        }*/
                    }
                }).start();
                break;
            case R.id.view_finger:
                Log.i(TAG,"--->view_finger onClick");
                if (fingerView != null) {
                    fingerView.reset();
                }
                break;
            default:
                break;
        }
    }
}
