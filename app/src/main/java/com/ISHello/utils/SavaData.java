package com.ISHello.utils;

import com.ISHello.Encryption.OneTwo;

public class SavaData {
    static String tmp = "f*^*^)9nznv#39Nidu3e)*!#";
    public static String key = tmp.substring(0, 24);

    public static String encrypt(String str, String key) {
        String result = "";
        try {
            key = OneTwo.one2two(key.getBytes());
            Des3 des3 = new Des3(OneTwo.two2one(key));
            result = OneTwo.one2two(des3.encrypt(str.getBytes()));
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return result;
    }

    public static String decrypt(String str, String key) {
        String result = "";

        try {
            key = OneTwo.one2two(key.getBytes());
            Des3 des3 = new Des3(OneTwo.two2one(key));
            result = new String(des3.decrypt(OneTwo.two2one(str)));
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return result;
    }

}
