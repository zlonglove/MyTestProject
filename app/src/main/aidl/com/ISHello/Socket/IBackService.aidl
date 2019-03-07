// IBackService.aidl
package com.ISHello.Socket;

// Declare any non-default types here with import statements

interface IBackService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

     boolean sendMessage(String message);
}
