package com.ISHello.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Des3 {
    /**
     * DESede解密的密钥必须是24字节
     * DESede即三重DES加密算法，也被称为3DES
     */
    private static final String Algorithm = new String("DESede");
    private SecretKey deskey;

    public Des3() throws Exception {
        KeyGenerator keygen = KeyGenerator.getInstance(Algorithm);
        this.deskey = keygen.generateKey();
    }

    public Des3(byte[] key) throws Exception {
        if (key == null) {
            throw new Exception("input key can not null");
        } else if (key.length != 24) {
            throw new Exception("key length is error, key length must 24 bytes");
        } else {
            SecretKeySpec destmp = new SecretKeySpec(key, Algorithm);
            this.deskey = destmp;
        }
    }

    public byte[] getKey() {
        return this.deskey.getEncoded();
    }

    public byte[] encrypt(byte[] data) {
        try {
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, this.deskey);
            return c1.doFinal(data);
        } catch (Exception var3) {
            var3.printStackTrace();
            return new byte[0];
        }
    }

    public byte[] decrypt(byte[] data) {
        try {
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, this.deskey);
            return c1.doFinal(data);
        } catch (Exception var3) {
            var3.printStackTrace();
            return new byte[0];
        }
    }
}
