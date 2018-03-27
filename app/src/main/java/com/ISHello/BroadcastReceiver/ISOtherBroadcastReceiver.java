package com.ISHello.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ISHello.CustomToast.CustomToast;
import com.ISHello.utils.LogUtil;

public class ISOtherBroadcastReceiver extends BroadcastReceiver {

    private final String TAG = "OtherBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.log(TAG, "---ISOtherBroadcastReceiver----onReceive()---");
        // 取到结果   
        Bundle bundle = getResultExtras(true);

        String receiverMsg = bundle.getString("first");

        LogUtil.log(TAG, "ISOtherBroadcastReceiver receiverMsg:" + receiverMsg);

        CustomToast.makeText(context, "接收到的Intnet的action" + intent.getAction() + "\n发过来的消息内容=" + receiverMsg, Toast.LENGTH_SHORT);
    }

}
