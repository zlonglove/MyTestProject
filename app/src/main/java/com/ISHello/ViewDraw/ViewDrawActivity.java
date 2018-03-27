package com.ISHello.ViewDraw;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

public class ViewDrawActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "ViewDrawActivity";
    private Button viewDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_draw);
        findView();
        init();
    }

    private void findView() {
        //得到我们设置的ContentView
        ViewGroup content = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        //viewDraw = (Button) content.findViewById(R.id.view_draw);
    }

    private void init() {
        viewDraw.setOnClickListener(this);
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
