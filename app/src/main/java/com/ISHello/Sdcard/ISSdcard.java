package com.ISHello.Sdcard;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;

public class ISSdcard {

    public ISSdcard() {
        // TODO Auto-generated constructor stub
    }

    public ISSdcard(Context context) {
    }

    public String getContentFromSdcard(String fileName) {
        String content = "";
        FileInputStream fileInputStream = null;
        /**
         * 缓存的流，和磁盘无关，不需要关闭
         */
        ByteArrayOutputStream byteArrayOutputStream = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            File file = new File(Environment.getExternalStorageDirectory(),
                    fileName);
            try {
                fileInputStream = new FileInputStream(file);
                byteArrayOutputStream = new ByteArrayOutputStream();
                int len = 0;
                byte[] str = new byte[1024];
                while ((len = fileInputStream.read(str)) > 0) {
                    byteArrayOutputStream.write(str, 0, len);
                }
                content = new String(byteArrayOutputStream.toByteArray());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                        fileInputStream = null;
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return content;
    }

    public boolean saveContentToSdcard(String fileName, String content) {
        boolean flag = false;
        FileOutputStream fileOutputStream = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            File file = new File(Environment.getExternalStorageDirectory(),
                    fileName);
            try {
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(content.getBytes());
                flag = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                        fileOutputStream = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return flag;
    }

}
