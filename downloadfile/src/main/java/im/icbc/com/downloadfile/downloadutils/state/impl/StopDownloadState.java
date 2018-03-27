package im.icbc.com.downloadfile.downloadutils.state.impl;

import android.content.Context;
import android.os.Handler;

import im.icbc.com.downloadfile.downloadutils.download.FileDownloader;
import im.icbc.com.downloadfile.downloadutils.state.DownloadState;

/**
 * Created by Jim on 2017/7/20.
 */

public class StopDownloadState implements DownloadState {
    @Override
    public void startDownload(Context context, Handler handler, String downloadurl, String filename, int threadcount) {

    }

    @Override
    public void pauseDownload(Context context, Handler handler, String downloadurl, String filename) {
        FileDownloader.getInstance().pauseDownload(downloadurl);

    }
}
