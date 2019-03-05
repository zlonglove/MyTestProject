package com.ISHello.ResideMenu.utils;

import android.os.Environment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import okhttp3.ResponseBody;

public class FileUtil {
    private static String path = "";

    static {
        if (isSdCardReady()) {
            path = Environment.getExternalStorageDirectory() + "/HTQ";
        } else {
            path = Environment.getDataDirectory().getAbsolutePath() + "/HTQ";
        }
    }

    public static boolean isSdCardReady() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getSignPath() {
        File file = new File(path + "/Sign/");
        if (!file.exists()) {
            file.mkdirs();
        }
        return path + "/Sign/";
    }

    public static String readSignFromFile() throws IOException {
        File file = new File(getSignPath(), "sign.txt");
        FileInputStream fis = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fis);
        String str = dis.readUTF();
        dis.close();
        return str;


    }

    public static String getImgPath() {
        File file = new File(path + "/Images/");
        if (!file.exists()) {
            file.mkdirs();
        }
        return path + "/Images/";
    }

    public static boolean writeSignToFile(String content) throws IOException {
        if (isSdCardReady()) {
            File file = new File(getSignPath(), "sign.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeUTF(content);
            dos.close();


        }
        return true;
    }

    public static File saveFile(String filePath, ResponseBody body) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File file = null;
        try {
            if (filePath == null) {
                return null;
            }
            file = new File(filePath);
            if (file == null || !file.exists()) {
                file.createNewFile();
            }


            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;
            byte[] fileReader = new byte[4096];

            inputStream = body.byteStream();
            outputStream = new FileOutputStream(file);

            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
                fileSizeDownloaded += read;

            }

            outputStream.flush();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return file;
    }


    /**
     * @param filePath
     * @param start    起始位置
     * @param body
     */
    public static File saveFile(String filePath, long start, ResponseBody body) {
        InputStream inputStream = null;
        RandomAccessFile raf = null;
        File file = null;
        try {
            file = new File(filePath);

            raf = new RandomAccessFile(filePath, "rw");
            inputStream = body.byteStream();
            byte[] fileReader = new byte[4096];

            raf.seek(start);

            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                raf.write(fileReader, 0, read);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return file;

    }

}
