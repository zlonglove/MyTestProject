/*******************************************************************************
 * Copyright (C) 2015-2016
 * All rights reserved.
 * http://www.zhanglong.com/
 * <p>
 * CHANGE LOG:
 * DATE			AUTHOR			COMMENTS
 * =============================================================================
 * 2015/12/1		zhanglong
 *******************************************************************************/

package com.ISHello.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author zhanglong E-mail: zhanglongit@126.com
 * @version 1.0
 */
public final class MD5 {
    private static final String ALGORITHM = "MD5";
    private static char sHexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private MD5() {
    }

    public final static String encode(String source) {
        MessageDigest sDigest = null;
        try {
            sDigest = MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] btyes = source.getBytes();
        byte[] encodedBytes = sDigest.digest(btyes);

        return hexStringTwo(encodedBytes);
    }

    private static String hexStringTwo(byte[] source) {
        if (source == null || source.length <= 0) {
            return "";
        }
        StringBuffer strResult = new StringBuffer();
        final int size = source.length;
        String stmp;
        for (int i = 0; i < size; i++) {
            stmp = (Integer.toHexString(source[i] & 0XFF));// &
            // 0XFF将高位全部变成0,保留一个字节的内容8bit
            if (stmp.length() == 1) {
                strResult.append("0");
                strResult.append(stmp);
            } else {
                strResult.append(stmp);
            }
        }
        return strResult.toString().toLowerCase();
    }

    /**
     * 此函数完成的功能和hexStringTwo是一样的
     *
     * @param source
     * @return
     */
    protected static String hexString(byte[] source) {
        if (source == null || source.length <= 0) {
            return "";
        }

        final int size = source.length;
        final char str[] = new char[size * 2];
        int index = 0;
        byte b;
        for (int i = 0; i < size; i++) {
            b = source[i];
            str[index++] = sHexDigits[b >>> 4 & 0xf];// 取高四位
            str[index++] = sHexDigits[b & 0xf];// 取低四位
        }
        return new String(str);
    }

    /**
     * 将url转化成对应的md5值,防止url中有特殊字符,影响在Android中直接使用
     *
     * @param url
     * @return
     */
    public String hashKeyFromUrl(String url) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance(ALGORITHM);
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xff & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

}
