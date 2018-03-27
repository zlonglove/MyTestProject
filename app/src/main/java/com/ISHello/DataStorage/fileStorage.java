package com.ISHello.DataStorage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

/**
 * 文件存储 (外部和内部)
 *
 * @author zhanglong
 */
public class fileStorage {
    private Activity activity;
    private final String TAG = "fileStorage";

    public fileStorage(Activity activity) {
        this.activity = activity;
    }

    public int writeExternalFile(String fileName, byte[] data) {
        int flag = 0;
        String status = Environment.getExternalStorageState();
        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        Log.i(TAG, "--->the extern path==" + path);
        if (Environment.MEDIA_MOUNTED.equals(status)) {
            /**
             * we can read and write the media
             */
            String filePath = path + "/" + fileName;
            FileOutputStream outputStream = null;
            try {
                File file = new File(filePath);
                if (file != null && (!file.exists())) {
                    file.createNewFile();
                }
                outputStream = new FileOutputStream(file);
                if (outputStream != null) {
                    outputStream.write(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                        outputStream = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            flag = 1;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(status)) {
            /**
             * we can only read the media
             */
            flag = -1;
        } else {
            flag = -2;
        }
        return flag;
    }

    public Boolean writeInternalfile(String fileName, byte[] data) {
        if (TextUtils.isEmpty(fileName) || data == null) {
            return false;
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = activity.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            outputStream.write(data);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
