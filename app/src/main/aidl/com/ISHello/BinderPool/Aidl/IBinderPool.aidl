// IBinderPool.aidl
package com.ISHello.BinderPool.Aidl;

// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
