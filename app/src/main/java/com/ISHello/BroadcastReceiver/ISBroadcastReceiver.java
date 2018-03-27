package com.ISHello.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ISBroadcastReceiver extends BroadcastReceiver {
    private String TAG = "ISBroadcastReceiver";

    public ISBroadcastReceiver() {
        super();
        Log.i(TAG, "--->this is ISBroadcastReceiver constructor");
    }

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        Log.i(TAG, "---ISBroadcastReceiver----onReceive()---");
        String receiverMsg = arg1.getExtras().getString("msg");

        Toast.makeText(
                arg0,
                "接收到的Intent的Action为" + arg1.getAction() + "\n传递的消息内容为=="
                        + receiverMsg, Toast.LENGTH_SHORT)
                .show();

        Bundle bundle = new Bundle();
        bundle.putString("first", "第一个广播接收者传递的数据");

        // 放入下一个结果中，让下一个广播接收者收到消息   
        setResultExtras(bundle);

        //如果不需要广播再进行传递则去掉setResultExtras(bundle),加上
        //this.abortBroadcast();就会终止广播的传递

    }

}
