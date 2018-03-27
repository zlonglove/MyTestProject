package com.ISHello.utils;

import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.ISHello.Configuration.Configuration;
import com.ISHello.Constants.Constants;
import com.ISHello.base.net.ICBCHttpClientUtil;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class LogUtil {

    private static final String DEFAULTTAG = "AndroidTest";
    private static ThreadLocal<SimpleDateFormat> dtf = new ThreadLocal() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        }
    };

    public static void log(String TAG, Object msg) {
        if (Constants.MODE == Constants.ModeType.Test) {
            Log.i(TAG, "--->" + msg.toString());
            try {
                if (msg instanceof Exception) {
                    Exception e = (Exception) msg;
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void log(Object o) {
        if (Constants.MODE == Constants.ModeType.Test) {
            Log.i(DEFAULTTAG, "--->" + o.toString());
            try {
                if (o instanceof Exception) {
                    Exception e = (Exception) o;
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Map<String, String> collectDeviceInfo() {
        Map<String, String> infos = new HashMap<String, String>();
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
            }
        }
        return infos;
    }

    /**
     * 将Crash日志写到文件
     *
     * @param ex
     * @return
     */
    public static String saveCrashInfoToFile(Throwable ex) {
        Map<String, String> infos = collectDeviceInfo();
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = dtf.get().format(new Date(System.currentTimeMillis()));
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(Constants.CrashFileDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(Constants.CrashFileDir + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 上传Crash日志到服务器
     */
    public static void sendCrashReportsToServer() {
        if (SdcardUtils.checkSDCard()) {
            File dir = new File(Constants.CrashFileDir);
            if (dir.exists()) {
                FilenameFilter filter = new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".log");
                    }
                };
                String[] crFiles = dir.list(filter);
                if (crFiles != null && crFiles.length > 0) {
                    TreeSet<String> sortedFiles = new TreeSet<String>();
                    sortedFiles.addAll(Arrays.asList(crFiles));
                    for (String fileName : sortedFiles) {
                        try {
                            File cr = new File(Constants.CrashFileDir + fileName);
                            BufferedInputStream fin = new BufferedInputStream(new FileInputStream(cr));
                            int length = fin.available();
                            byte[] buffer = new byte[length];
                            fin.read(buffer);
                            String log = EncodingUtils.getString(buffer, "UTF-8");
                            ICBCHttpClientUtil.postCrashLog(Configuration.getInstance().getLogURL(), log);
                            fin.close();
                            if (Constants.MODE == Constants.ModeType.Product) {
                                cr.delete();
                            }
                        } catch (Exception e) {
                            LogUtil.log("error", e.getMessage());
                        }
                    }
                }
            }
        }
    }
}
