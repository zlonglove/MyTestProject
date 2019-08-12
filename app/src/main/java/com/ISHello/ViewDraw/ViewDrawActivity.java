package com.ISHello.ViewDraw;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

public class ViewDrawActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "ViewDrawActivity";
    private DrawView viewDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("自定义文本绘制");
        //setContentView(R.layout.activity_view_draw);
        setContentView(R.layout.activity_circle_view);
        findView();
        init();
    }

    private void findView() {
        //得到我们设置的ContentView
        ViewGroup content = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        //viewDraw = (DrawView) content.findViewById(R.id.view_draw);
    }

    private void init() {
        //viewDraw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.view_draw:
                Log.i(TAG, "--->ViewDraw click");
                break;*/
            default:
                break;
        }
    }
}
