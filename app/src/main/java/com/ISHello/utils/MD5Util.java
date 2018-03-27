package com.ISHello.utils;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    protected static char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    protected static MessageDigest messagedigest = null;

    public MD5Util() {
    }

    public static String getFileMD5String(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            boolean numRead = false;

            int numRead1;
            while ((numRead1 = fis.read(buffer)) > 0) {
                messagedigest.update(buffer, 0, numRead1);
            }

            fis.close();
        } catch (Exception var4) {
            ;
        }

        return bufferToHex(messagedigest.digest());
    }

    public static String getStringMD5(String str) {
        byte[] buffer = str.getBytes();
        messagedigest.update(buffer);
        return bufferToHex(messagedigest.digest());
    }

    public static String bufferToHex(byte[] bytes) {
        return a(bytes, 0, bytes.length);
    }

    private static String a(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;

        for (int l = m; l < k; ++l) {
            a(bytes[l], stringbuffer);
        }

        return stringbuffer.toString();
    }

    private static void a(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 240) >> 4];
        char c1 = hexDigits[bt & 15];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static String getMD5(String URLName) {
        String name = "";

        try {
            URL url = new URL(URLName);
            BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
            byte[] bytes = new byte[1024];
            boolean len = false;
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");

            int len1;
            while ((len1 = inputStream.read(bytes)) > 0) {
                messagedigest.update(bytes, 0, len1);
            }

            name = bufferToHex(messagedigest.digest());
            inputStream.close();
        } catch (MalformedURLException var7) {

        } catch (IOException var8) {

        } catch (NoSuchAlgorithmException var9) {

        }

        return name;
    }

    public static String getMD5String(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var1) {
            var1.printStackTrace();
        }

    }
}
