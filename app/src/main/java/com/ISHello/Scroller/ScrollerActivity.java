package com.ISHello.Scroller;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ishelloword.R;

public class ScrollerActivity extends Activity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller);
        findViews();
        init();
    }

    private void findViews() {
        textView = (TextView) findViewById(R.id.textView);
    }

    private void init() {

    }

    public void layoutParams(View view) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
        params.leftMargin += 200;
        textView.requestLayout();
    }

    public void scrollTo(View view) {
        textView.scrollTo(-150, -100);//负值向右下移动,指的是控件的内容
    }

    public void animation(View view) {
        ObjectAnimator.ofFloat(textView, "translationX", 0, 100).setDuration(1000).start();
    }
}
