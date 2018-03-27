package com.ISHello.DownloadManager;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;

public class ISDownloadManager {
    private final String TAG = "DownloadManager";
    private DownloadManager downloadManager;
    private Context context;
    long loadId;

    public ISDownloadManager(Context context) {
        this.context = context;
    }

    public void start(String url) {
        downloadManager = (DownloadManager) context
                .getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new Request(uri);
        request.setAllowedNetworkTypes(Request.NETWORK_WIFI);
        loadId = downloadManager.enqueue(request);
    }

    public void registeLoadCompleteBroadCase() {
        IntentFilter intentFilter = new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(receiver, intentFilter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "--->download complete");

        }
    };
}
