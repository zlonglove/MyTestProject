package com.ISHello.NetWorkChange;

import com.ISHello.CustomToast.CustomToast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetWorkBrodcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo mobNetInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        NetworkInfo wifiNetInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        NetworkInfo eth = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        if (eth != null) {
            if (!eth.isConnected()) {
                CustomToast.makeText(context,
                        "Mobile Network Type : " + eth.getTypeName()
                                + " ShutDown", Toast.LENGTH_SHORT);
            } else {
                CustomToast.makeText(context,
                        "Mobile Network Type : " + eth.getTypeName()
                                + " Connected", Toast.LENGTH_SHORT);
            }
        }

        if (mobNetInfo != null) {
            if (!mobNetInfo.isConnected()) {
                CustomToast.makeText(context, "Mobile Network Type : "
                                + mobNetInfo.getTypeName() + " ShutDown",
                        Toast.LENGTH_SHORT);
            } else {
                CustomToast.makeText(context, "Mobile Network Type : "
                                + mobNetInfo.getTypeName() + " Connected",
                        Toast.LENGTH_SHORT);
            }
        }

        if (wifiNetInfo != null) {
            if (!wifiNetInfo.isConnected()) {
                CustomToast.makeText(context, "wifi Network Type : "
                                + wifiNetInfo.getTypeName() + " ShutDown",
                        Toast.LENGTH_SHORT);
            } else {
                CustomToast.makeText(context, "wifi Network Type : "
                                + wifiNetInfo.getTypeName() + " Connected",
                        Toast.LENGTH_SHORT);
            }
        }
    }

}
