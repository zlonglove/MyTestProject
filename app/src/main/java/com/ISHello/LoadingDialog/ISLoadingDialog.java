package com.ISHello.LoadingDialog;

import com.example.ishelloword.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class ISLoadingDialog extends Activity {
    private final String TAG = "ISLoadingDialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Log.i(TAG, "--->load_progress_button click");
        }
        return super.onKeyDown(keyCode, event);
    }

}
