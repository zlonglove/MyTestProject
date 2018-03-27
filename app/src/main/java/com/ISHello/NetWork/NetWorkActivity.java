package com.ISHello.NetWork;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ISHello.CustomToast.CustomToast;
import com.ISHello.MultipleThreadContinuableDownLoad.MultipleThreadContinuableDownLoadActivity;
import com.ISHello.MultithreadingDownloading.DownloadProgressListener;
import com.ISHello.MultithreadingDownloading.MultithreadingDownloading;
import com.ISHello.base.base.BaseActivity;
import com.ISHello.utils.AsyncHttpClient;
import com.example.ishelloword.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class NetWorkActivity extends BaseActivity {
    final String TAG = "NetWorkActivity";
    final int ThradNumber = 5;
    final String urlpath = "http://192.168.1.101:8080/ServerForPicture/love.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_activity);
    }

    public NetWorkActivity() {

    }

    /**
     * 下载网络上的code
     *
     * @param view
     */
    public void WebCodeViewer(View view) {
        Intent intent = new Intent(NetWorkActivity.this,
                WebCodeViewerActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    /**
     * Http Post 测试程序
     *
     * @param view
     */
    public void HttpPost(View view) {
        Log.i(TAG, "--->HttpPost Button Click");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://192.168.46.44:8080/First_web_server/Login";
                Map<String, String> map = new HashMap<String, String>();
                map.put("loginName", "zhanglong");
                map.put("loginPassword", "1234");
                final String result = AsyncHttpClient.getInstance(false, 80,
                        443).httpPost(url, map);
                Log.i(TAG, "--->result==" + result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CustomToast.makeText(NetWorkActivity.this, result, Toast.LENGTH_SHORT);
                    }
                });
            }
        }).start();
    }

    /**
     * Http Get 测试
     */
    public void HttpGet(View view) {
        Log.i(TAG, "--->HttpGet Button Click");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://v.3ntv.cn/parser/parse?method_name=getrealurl&video_page_url=http://v.qq.com/cover/e/eenx6vcdjdvqg53.html";
                final String result = AsyncHttpClient.getInstance(false, 80,
                        443).httpGet(url);
                Log.i(TAG, "--->result==" + result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CustomToast.makeText(NetWorkActivity.this, result, Toast.LENGTH_SHORT);
                    }
                });
            }
        }).start();
    }

    /**
     * 多线程下载
     *
     * @param view
     */
    public void MultithreadingDownloadingClick(View view) {
        final MultithreadingDownloading down = new MultithreadingDownloading();
        String sdcardpath = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        Log.i("MultithreadingDownloading", "--->the sdcard path is="
                + sdcardpath);
        final String savePath = sdcardpath + "/";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    down.downLoad(urlpath, savePath, ThradNumber,
                            new DownloadProgressListener() {
                                @Override
                                public void onDownloadSize(int size) {
                                    Log.i(TAG, "--->The have Loading size==="
                                            + size);
                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();

    }

    public void MultipleThreadContinuableDownLoadClick(View view) {
        Log.i(TAG, "--->MultipleThreadContinuableDownLoad Button Click");
        Intent intent = new Intent(NetWorkActivity.this,
                MultipleThreadContinuableDownLoadActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

}
