package com.ISHello.Encryption;

/**
 * Created by zhanglong on 2017/4/27.
 */

public class OneTwo {

    public OneTwo() {
    }

    private static byte a(char c) throws Exception {
        if (c >= 97 && c <= 102) {
            return (byte) (c - 97 + 10);
        } else if (c >= 65 && c <= 70) {
            return (byte) (c - 65 + 10);
        } else if (c >= 48 && c <= 57) {
            return (byte) (c - 48);
        } else {
            throw new Exception("Cannot convert char(" + c + ") to digit");
        }
    }

    private static char a(byte byte0) throws Exception {
        if (byte0 > 0xf) {
            throw new Exception("Cannot convet byte(" + byte0 + ") to hex char");
        } else {
            return byte0 < 10 ? (char) (byte0 + 48) : (char) (byte0 + 65 - 10);
        }
    }

    public static byte[] hexStringToBytes(String s) throws Exception {
        byte[] abyte0 = new byte[s.length() / 2];
        int i = 0;

        int var10001;
        for (int j = 0; i < s.length(); abyte0[var10001] += a(s.charAt(i++))) {
            abyte0[j] = (byte) (a(s.charAt(i++)) << 4);
            var10001 = j++;
        }

        return abyte0;
    }

    /**
     * 把字节数组转化为十六进制
     *
     * @param abyte0
     * @return
     * @throws Exception
     */
    public static String byteToHexString(byte[] abyte0) throws Exception {
        StringBuffer stringbuffer = new StringBuffer(abyte0.length * 2);

        for (int i = 0; i < abyte0.length; ++i) {
            byte byte0 = abyte0[i];
            stringbuffer.append(a((byte) (byte0 >> 4 & 0xf)));//取高四位
            stringbuffer.append(a((byte) (byte0 & 0xf)));//取低四位
        }

        return stringbuffer.toString();
    }

    public static byte[] two2one(String s) throws Exception {
        byte[] abyte0 = new byte[s.length() / 2];
        int i = 0;

        int var10001;
        for(int j = 0; i < s.length(); abyte0[var10001] += a(s.charAt(i++))) {
            abyte0[j] = (byte)(a(s.charAt(i++)) << 4);
            var10001 = j++;
        }

        return abyte0;
    }

    public static String one2two(byte[] abyte0) throws Exception {
        StringBuffer stringbuffer = new StringBuffer(abyte0.length * 2);

        for(int i = 0; i < abyte0.length; ++i) {
            byte byte0 = abyte0[i];
            stringbuffer.append(a((byte)(byte0 >> 4 & 15)));
            stringbuffer.append(a((byte)(byte0 & 15)));
        }

        return stringbuffer.toString();
    }
}
