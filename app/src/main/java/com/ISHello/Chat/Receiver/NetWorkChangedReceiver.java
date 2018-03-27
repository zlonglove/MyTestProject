package com.ISHello.Chat.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ISHello.Chat.Listener.OnNetWorkChangedListener;
import com.ISHello.Constants.Constants;

import java.util.ArrayList;
import java.util.List;

public class NetWorkChangedReceiver extends BroadcastReceiver {


    private List<OnNetWorkChangedListener> mOnNetWorkChangedListeners = new ArrayList<>();

    public void registerListener(OnNetWorkChangedListener listener) {
        if (!mOnNetWorkChangedListeners.contains(listener)) {
            mOnNetWorkChangedListeners.add(listener);
        }
    }

    public void unregisterListener(OnNetWorkChangedListener listener) {
        if (mOnNetWorkChangedListeners.contains(listener)) {
            mOnNetWorkChangedListeners.remove(listener);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Constants.NETWORK_CONNECTION_CHANGE)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                if (networkInfo.isConnected()) {
                    //TimeUtil.getServerTime();
                }
                if (mOnNetWorkChangedListeners != null && mOnNetWorkChangedListeners.size() > 0) {
                    for (OnNetWorkChangedListener listener :
                            mOnNetWorkChangedListeners) {
                        listener.OnNetWorkChanged(networkInfo.isConnected(), networkInfo.getType());
                    }
                }
            } else {
                //LogUtil.e("当前没有网络连接");
                if (mOnNetWorkChangedListeners != null && mOnNetWorkChangedListeners.size() > 0) {
                    for (OnNetWorkChangedListener listener :
                            mOnNetWorkChangedListeners) {
                        listener.OnNetWorkChanged(false, 0);
                    }
                }

            }
        }

    }
}
