package com.ISHello.Encryption;


import org.apache.commons.codec.binary.Base64;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by zhanglong on 2017/4/25.
 * 3DES密钥的长度必须是8的倍数，可取24位或32位；
 * 加密结果的byte数组转换为字符串，一般采用两种方式：Base64处理或十六进制处理
 */

public class Des {
    private static Des instance;
    private Key key;
    private Cipher encryptCipher;
    private Cipher decryptCipher;
    private final String Algorithm = "DESede"; //定义加密算法,可用 DES,DESede,Blowfish

    private Des(String strKey) {
        key = setKey(strKey);
        try {
            encryptCipher = Cipher.getInstance(Algorithm);
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);//加密

            decryptCipher = Cipher.getInstance(Algorithm);
            decryptCipher.init(Cipher.DECRYPT_MODE, key);//解密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

    }

    public static Des getInstance() {
        if (instance == null) {
            String key = "Ud#r:hc~8@Vk&3.p-z*ui(5p".substring(0, 24);
            instance = new Des(key);
        }
        return instance;
    }

    //  根据参数生成KEY
    private Key setKey(String strKey) {
        try {
            strKey = OneTwo.byteToHexString(strKey.getBytes("UTF-8"));
            SecretKeySpec destmp = new SecretKeySpec(OneTwo.hexStringToBytes(strKey), Algorithm);
            return destmp;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //  加密String明文输入,String密文输出
    public String getEncString(String strMing) {
        try {
            byte[] byteMing = Base64.encodeBase64(strMing.getBytes("UTF-8"));
            byte[] byteMi = this.getEncByte(byteMing);
            return OneTwo.byteToHexString(byteMi);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //加密以byte[]明文输入,byte[]密文输出
    private byte[] getEncByte(byte[] byteS) {
        byte[] byteFina = null;
        try {
            byteFina = encryptCipher.doFinal(byteS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteFina;
    }

    //解密:以String密文输入,String明文输出
    public String getDesString(String strMi) {
        try {
            //byte[] byteMi = Base64.decodeBase64(strMi.getBytes("UTF-8"));
            //byte[] decryByte = des3.decrypt(OneTwo.two2one(str));
            byte[] byteMing = this.getDesByte(OneTwo.hexStringToBytes(strMi));
            byte[] tmp = Base64.decodeBase64(byteMing);
            return new String(tmp, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 解密以byte[]密文输入,以byte[]明文输出
    private byte[] getDesByte(byte[] byteD) {
        byte[] byteFina = null;
        try {
            byteFina = decryptCipher.doFinal(byteD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteFina;
    }

    //测试函数
    public static void main(String[] args) throws InterruptedException {
        String src = "1001037799";
        Des dtDes = Des.getInstance();
        String encodeString = dtDes.getEncString(src);
        System.out.println("--->" + encodeString);
        System.out.println("--->" + dtDes.getDesString(encodeString));

    }
}
