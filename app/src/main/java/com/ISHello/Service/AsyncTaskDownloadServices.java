package com.ISHello.Service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ISHello.utils.LogUtil;
import com.example.ishelloword.R;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

/**
 * @author kfzx-zhanglong
 */
public class AsyncTaskDownloadServices extends AsyncTask<Object, Integer, Integer> {

    private final String TAG = AsyncTaskDownloadServices.class.getSimpleName();
    private ProgressDialog progressDialog;
    private Handler handler;

    public static final int DOWNLOAD_SUCCESS = 0;
    public static final int DOWNLOAD_FAIL = 1;

    private int threadNum = 4;// 开启的线程数
    private int blockSize;// 每一个线程的下载量

    public AsyncTaskDownloadServices(Context context, String title, String msg) {
        progressDialog = new ProgressDialog(context, R.style.Theme_AlertDialogPro_ICBC);
        progressDialog.setTitle(title);
        progressDialog.setMessage(msg);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setIcon(R.drawable.ic_launcher);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
        super.onPreExecute();
    }

    /**
     * @parms downloadAPKUrl--下载URL
     * @parms filePath--下载到本地的apk路径
     * @parms fileName--apk的名字
     * @paras Handler
     */
    @Override
    protected Integer doInBackground(Object... paramTemp) {
        int result = DOWNLOAD_FAIL;
        try {
            String url = (String) paramTemp[0];
            String filePath = "";
            if (paramTemp[1] instanceof File) {
                filePath = ((File) paramTemp[1]).getAbsolutePath();
            } else {
                filePath = (String) paramTemp[1];
            }
            String fileName = (String) paramTemp[2];
            handler = (Handler) paramTemp[3];
            result = downLoad(url, filePath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 更新进度条
     *
     * @param progress
     */
    protected void onProgressUpdate(Integer... progress) {
        Log.i("onProgressUpdate", "--->progress==" + progress[0]);
        progressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(Integer result) {
        Message msg;
        switch (result) {
            case DOWNLOAD_SUCCESS:
                msg = Message.obtain();
                msg.arg1 = DOWNLOAD_SUCCESS;
                handler.sendMessage(msg);
                break;
            case DOWNLOAD_FAIL:
                msg = Message.obtain();
                msg.arg1 = DOWNLOAD_FAIL;
                handler.sendMessage(msg);
                break;
            default:
                break;
        }
        progressDialog.dismiss();
        super.onPostExecute(result);
    }

    /**
     * 下载apk
     *
     * @param url
     * @param filePath
     * @param fileName
     * @return
     * @throws Exception
     */
    public int downLoad(String url, String filePath, String fileName) throws Exception {
        FileDownloadThread[] threads = new FileDownloadThread[threadNum];
        HttpURLConnection connection = null;
        try {
            URL mURL = new URL(url);
            connection = (HttpURLConnection) mURL.openConnection();

            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);

            /**
             * 防止getContentLength返回-1的问题
             */
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept-Encoding", "identity");

            int length = connection.getContentLength();
            if (length <= 0) {
                LogUtil.log("--->read file total length fail");
                return DOWNLOAD_FAIL;
            }
            // 计算每条线程下载的数据长度
            blockSize = (length % threadNum) == 0 ? length / threadNum : length / threadNum + 1;

            LogUtil.log(TAG, "--->fileSize:" + length + "  blockSize:" + blockSize);
            //创建一个和服务器大小一样的文件
            File path = new File(filePath);
            if (!path.isDirectory()) {
                path.mkdirs();
            }
            File file;
            file = new File(filePath, fileName);
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }
            RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
            accessFile.setLength(length);
            //要关闭RandomAccessFile对象
            accessFile.close();

            for (int i = 0; i < threads.length; i++) {
                // 启动线程，分别下载每个线程需要下载的部分
                threads[i] = new FileDownloadThread(url, file, blockSize, (i + 1));
                threads[i].setName("Thread:" + i);
                threads[i].start();
            }
            boolean isfinished = false;
            long downloadedAllSize = 0;
            int oldProgress = 0;
            while (!isfinished) {
                isfinished = true;
                // 当前所有线程下载总量
                downloadedAllSize = 0;
                for (int i = 0; i < threads.length; i++) {
                    downloadedAllSize += threads[i].getDownloadLength();
                    if (!threads[i].isCompleted()) {
                        isfinished = false;
                    }
                }
                LogUtil.log(TAG, "--->downLoadAllSize==" + downloadedAllSize + "/" + length);
                int progress = (int) (((float) downloadedAllSize / length) * 100);
                if (progress > oldProgress) {
                    oldProgress = progress;
                    publishProgress(progress);
                }
                Thread.sleep(1000);// 休息1秒后再读取下载进度
            }
            LogUtil.log(TAG, " --->All of downloadSize:" + downloadedAllSize);
            return DOWNLOAD_SUCCESS;
        } catch (UnknownHostException e) {
            // 您的网络存在异常,无法解析服务器地址,请检查网络设置
            return DOWNLOAD_FAIL;
        } catch (SocketTimeoutException e) {
            //"您的网络存在异常,连接服务器超时,请稍后重试
            return DOWNLOAD_FAIL;
        } catch (SSLException e) {
            //"对不起，服务器安全证书验证失败
            return DOWNLOAD_FAIL;
        } catch (Exception e) {
            return DOWNLOAD_FAIL;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}
