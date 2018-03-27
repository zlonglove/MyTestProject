package com.ISHello.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.http.util.EncodingUtils;

import android.content.Context;

public class ISFile {
    private String fileName;

    public ISFile() {
        super();
    }

    public String getFileName() {
        return this.fileName;
    }

    /**
     * write file to /data/data/packagename/files/
     *
     * @param context
     * @param fileName ---file name
     * @param message  ----data
     */
    public void writeFileData(Context context, String fileName, String message) {
        this.fileName = fileName;
        try {
            FileOutputStream fout = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            byte[] bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * read file from /data/data/packageName/files/
     *
     * @param fileName
     * @return
     */
    public String readFileData(Context context, String fileName) {
        String res = "";
        try {
            FileInputStream inputStream = context.openFileInput(fileName);
            int length = inputStream.available();
            byte[] buffer = new byte[length];
            inputStream.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * @param fileName
     * @param message
     * @param append   If {append==true} is true and the file already exists, it will
     *                 be appended to; otherwise it will be truncated. The file will
     *                 be created if it does not exist
     */
    public void writeFileSdcard(String fileName, String message, boolean append) {
        try {
            FileOutputStream fout = new FileOutputStream(fileName, append);

            byte[] bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readFileSdcard(String fileName) {

        String res = "";
        try {
            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

}
