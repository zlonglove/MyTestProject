package com.ISHello.Update;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.ISHello.CustomToast.CustomToast;
import com.example.ishelloword.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author zhanglong
 * @serial 2013.6.10
 * APK在线更新程序
 */

public class ISUpdateActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    final String TAG = "ISUpdateActivity";
    final String serverCodeUrl = "http://192.168.1.102:8080/ServerForPicture/ver";
    final String serverApkUrl = "http://192.168.1.102:8080/ServerForPicture/FirstAndroidProject.apk";

    String newVerName = "";    //新版本名称
    int newVerCode = -1;    //新版本号
    String UPDATE_SERVERAPK = "FirstAndroidProject.apk";

    final int downCompelete = 1;
    final int downloading = 2;

    ProgressDialog pd = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_activity);
        if (Build.VERSION.SDK_INT >= 11) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        }

        if (getServerVer()) {
            int verCode = this.getVerCode(this);
            Log.i(TAG, "--->current apk verCode==" + verCode);
            if (newVerCode > verCode) {
                doNewVersionUpdate();//更新版本
            } else {
                notNewVersionUpdate();//提示已是最新版本
            }
        }
    }

    /**
     * 获得版本号
     */
    public int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            Log.e("--->版本号获取异常", e.getMessage());
        }
        return verCode;
    }

    /**
     * 获得版本名称
     */
    public String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            Log.e("版本名称获取异常", e.getMessage());
        }
        return verName;
    }

    /**
     * 从服务器端获得版本号与版本名称
     *
     * @return
     */
    public boolean getServerVer() {
        Log.i(TAG, "--->get Server Apk version");
        String json = null;
        try {
            //URL url = new URL("http://10.0.2.2:8080/ApkUpdateService/ver");
            URL url = new URL(serverCodeUrl);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();
            //InputStreamReader reader = new InputStreamReader(httpConnection.getInputStream());
            //BufferedReader bReader = new BufferedReader(reader);
            //String json = bReader.toString(); 
            InputStream inputStream = httpConnection.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            // 读取缓存
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, length);// 写入输出流
            }
            inputStream.close();// 读取完毕，关闭输入流
            bos.close();
            // 根据输出流创建字符串对象
            json = new String(bos.toByteArray(), "UTF-8");
        } catch (Exception e) {
            CustomToast.makeText(this, "网络错误,请检查网络", Toast.LENGTH_LONG);
            e.printStackTrace();
            return false;
        }

        try {
            JSONArray array = new JSONArray(json);
            JSONObject jsonObj = array.getJSONObject(0);
            newVerCode = Integer.parseInt(jsonObj.getString("verCode"));
            newVerName = jsonObj.getString("verName");
        } catch (NumberFormatException e) {
            CustomToast.makeText(this, "服务器版本号文件配置错误", Toast.LENGTH_LONG);
            e.printStackTrace();
        } catch (JSONException e) {
            CustomToast.makeText(this, "服务器版本号文件配置错误", Toast.LENGTH_LONG);
            e.printStackTrace();
        }
        Log.i(TAG, "--->The server vercode==" + newVerCode + " verName==" + newVerName);
        return true;
    }

    /**
     * 不更新版本
     */
    public void notNewVersionUpdate() {
        int verCode = this.getVerCode(this);
        String verName = this.getVerName(this);
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本:");
        sb.append(verName);
        sb.append(" Code:");
        sb.append(verCode);
        sb.append("\n已是最新版本,无需更新");
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("软件更新")
                .setMessage(sb.toString())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                }).create();
        dialog.show();
    }

    /**
     * 更新版本
     */
    public void doNewVersionUpdate() {
        int verCode = this.getVerCode(this);
        String verName = this.getVerName(this);
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本:");
        sb.append(verName);
        sb.append(" Code:");
        sb.append(verCode);
        sb.append(",发现版本:");
        sb.append(newVerName);
        sb.append(" Code:");
        sb.append(newVerCode);
        sb.append(",是否更新");
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("软件更新")
                .setMessage(sb.toString())
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        pd = new ProgressDialog(ISUpdateActivity.this);
                        pd.setTitle("正在下载");
                        pd.setMessage("请稍后。。。");
                        //pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        // downFile("http://10.0.2.2:8080/ApkUpdateService/ApkUpdateAndroid.apk");
                        downFile(serverApkUrl);
                    }
                })
                .setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                }).create();
        //显示更新框
        dialog.show();
    }

    /**
     * 下载apk
     */
    public void downFile(final String url) {
        pd.show();
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();
                    Log.i(TAG, "--->The server Apk size==" + length);
                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        File file = new File(Environment.getExternalStorageDirectory(), UPDATE_SERVERAPK);
                        fileOutputStream = new FileOutputStream(file);
                        byte[] b = new byte[1024];
                        int charb = 0;
                        long count = 0;
                        while ((charb = is.read(b)) != -1) {
                            fileOutputStream.write(b, 0, charb);
                            count += charb;
                            Log.i(TAG, "--->The count==" + count);
                            int progress = (int) (count * 100 / length);
                            Log.i(TAG, "--->The downloaded is==" + progress);
                            Message message = handler.obtainMessage();
                            message.what = downloading;
                            message.arg1 = progress;
                            handler.sendMessage(message);

                        }
                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case downCompelete:
                    pd.cancel();
                    update();
                    break;
                case downloading:
                    pd.setProgress(msg.arg1);
                    break;
                default:
                    break;
            }

        }
    };

    /**
     * 下载完成，通过handler将下载对话框取消
     */
    public void down() {
        new Thread() {
            public void run() {
                Message message = handler.obtainMessage();
                message.what = downCompelete;
                handler.sendMessage(message);
            }
        }.start();
    }

    /**
     * 安装应用
     */
    public void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), UPDATE_SERVERAPK))
                , "application/vnd.android.package-archive");
        startActivity(intent);
    }

}
