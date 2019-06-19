// BookAidlInterface.aidl
package com.ISHello.Parcelable;

import com.ISHello.Parcelable.Book;
import com.ISHello.Parcelable.IOnNewBookArrivedListener;
interface BookAidlInterface {
/**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    /*void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);*/

   void addBook(in Book book);
   List<Book> getBookList();
   void registerListener(IOnNewBookArrivedListener listener);
   void unregisterListener(IOnNewBookArrivedListener listener);
}
