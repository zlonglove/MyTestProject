package com.ISHello.Progress;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

public class ISDialog extends ProgressDialog {

    public ISDialog(Context context) {
        super(context);
        /**
         * 设置没有标题
         */
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("zhanglong", ">>>>This is ISDialog click");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
