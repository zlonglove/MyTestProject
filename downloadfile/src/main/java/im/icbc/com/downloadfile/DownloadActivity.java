package im.icbc.com.downloadfile;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

import im.icbc.com.downloadfile.downloadutils.constant.Constant;
import im.icbc.com.downloadfile.downloadutils.db.DBManager;
import im.icbc.com.downloadfile.downloadutils.download.FileDownloader;
import im.icbc.com.downloadfile.downloadutils.receiver.NetWorkChangeReceiver;
import im.icbc.com.downloadfile.downloadutils.state.DownloadState;
import im.icbc.com.downloadfile.downloadutils.state.impl.StartDownloadState;
import im.icbc.com.downloadfile.downloadutils.state.impl.StopDownloadState;
import im.icbc.com.downloadfile.downloadutils.utils.DownLoaderController;
import im.icbc.com.downloadfile.downloadutils.utils.NetWorkUtil;

public class DownloadActivity extends CheckPermissionsActivity {

    private final String TAG = DownloadActivity.class.getSimpleName();

    private Button btn_startDownload;
    private Button btn_pauseDownload;
    private Button btn_clearDownload;
    private ProgressBar mProgressBar;
    private NetWorkChangeReceiver mNetWorkChangeReceiver;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.DOWNLOAD_START:
                    mProgressBar.setMax(msg.arg1);
                    break;
                case Constant.DOWNLOAD_KEEP:
                    mProgressBar.setProgress(msg.arg1);
                    break;
                case Constant.DOWNLOAD_COMPLETE:
                    Toast.makeText(DownloadActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                    String url = (String) msg.obj;
                    DBManager.getInstance(DownloadActivity.this).delete(url);
                    break;
                case Constant.DOWNLOAD_FAIL:
                    Toast.makeText(DownloadActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                    String urlstr = (String) msg.obj;
                    FileDownloader.getInstance().pauseDownload(urlstr);
                    break;
                case Constant.DOWNLOAD_ClLEAN:
//                    do something
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_download);
        btn_pauseDownload = (Button) findViewById(R.id.stop_download);
        btn_startDownload = (Button) findViewById(R.id.start_download);
        btn_clearDownload = (Button) findViewById(R.id.clear_download);
        mProgressBar = (ProgressBar) findViewById(R.id.pbSmall);
        mNetWorkChangeReceiver = new NetWorkChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetWorkChangeReceiver, filter);

        final DownloadState startstate = new StartDownloadState();
        final DownloadState pausestate = new StopDownloadState();

        final DownLoaderController controller = new DownLoaderController();

        // 获取输入地址的最后一个"/"出现的位置
        int lastIndex = Constant.downloadUrl.lastIndexOf("/");
        // 获取文件名及格式
        String filename = Constant.downloadUrl.substring(lastIndex + 1, Constant.downloadUrl.length());
        Log.i(TAG,"--->文件名为:" + filename);
        final String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + filename;
        final int threadCount = 5;

        btn_pauseDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetWorkUtil.isNetworkAvailable(DownloadActivity.this)) {
                    Toast.makeText(DownloadActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    return;
                }
                controller.setDownloadState(pausestate);
                controller.stopDownload(DownloadActivity.this, mHandler, Constant.downloadUrl, filepath, threadCount);
            }
        });
        btn_startDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetWorkUtil.isNetworkAvailable(DownloadActivity.this)) {
                    Toast.makeText(DownloadActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    return;
                }
                controller.setDownloadState(startstate);
                controller.startDownload(DownloadActivity.this, mHandler, Constant.downloadUrl, filepath, threadCount);
            }
        });
        btn_clearDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBManager.getInstance(DownloadActivity.this).delete(Constant.downloadUrl);
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "--->onDestory()");
        super.onDestroy();
        if (mNetWorkChangeReceiver != null) {
            unregisterReceiver(mNetWorkChangeReceiver);
        }
        DBManager.getInstance(this).closeDb();
    }
}
