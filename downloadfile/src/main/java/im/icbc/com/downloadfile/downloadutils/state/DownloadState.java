package im.icbc.com.downloadfile.downloadutils.state;

import android.content.Context;
import android.os.Handler;

/**
 * Created on 2017/7/20.
 * @author zhanglong
 */

public interface DownloadState {

    void startDownload(Context context, Handler handler, String downloadurl, String filename, int threadcount);
    void pauseDownload(Context context, Handler handler, String downloadurl, String filename);

}
