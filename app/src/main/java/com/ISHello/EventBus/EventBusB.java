package com.ISHello.EventBus;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

import org.greenrobot.eventbus.EventBus;

public class EventBusB extends BaseActivity {
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus_b);

        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent("event bus message"));
            }
        });
    }
}
