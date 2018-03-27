package com.ISHello.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1 {

    public SHA1() {
        super();
    }

    public final static String encryption(byte[] bInput) {
        StringBuffer strResult = new StringBuffer();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        md.update(bInput);
        byte[] encryptedBytes = md.digest();
        String stmp;
        for (int n = 0; n < encryptedBytes.length; n++) {
            stmp = (Integer.toHexString(encryptedBytes[n] & 0XFF));
            if (stmp.length() == 1) {
                strResult.append("0");
                strResult.append(stmp);
            } else {
                strResult.append(stmp);
            }
        }
        return strResult.toString().toLowerCase();
    }

    public final static String encryption(FileInputStream in) throws IOException {
        if (in == null) {
            return null;
        }
        StringBuffer strResult = new StringBuffer();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] b = new byte[1024 * 1024];
        int len = 0;
        while ((len = in.read(b)) != -1) {
            md.update(b, 0, len);
        }
        if (in != null) {
            in.close();
        }
        byte[] encryptedBytes = md.digest();
        String stmp;
        for (int n = 0; n < encryptedBytes.length; n++) {
            stmp = (Integer.toHexString(encryptedBytes[n] & 0XFF));
            if (stmp.length() == 1) {
                strResult.append("0");
                strResult.append(stmp);
            } else {
                strResult.append(stmp);
            }
        }
        return strResult.toString().toLowerCase();
    }
}
