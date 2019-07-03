package com.ISHello.BinderPool.Server;

import android.os.RemoteException;
import android.util.Base64;

import com.ISHello.BinderPool.Aidl.ISecurityCenter;


public class SecurityCenterImpl extends ISecurityCenter.Stub {
    //private static final char SECTET_CODE = '^';

    @Override
    public String encrypt(String content) throws RemoteException {
        /*char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] ^= SECTET_CODE;

        }
        return new String(chars);*/
        return new String(Base64.encode(content.getBytes(), android.util.Base64.NO_WRAP));
    }

    @Override
    public String decrypt(String password) throws RemoteException {
        //return encrypt(password);
        return new String(Base64.decode(password, android.util.Base64.NO_WRAP));
    }
}
