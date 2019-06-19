// IOnNewBookArrivedListener.aidl
package com.ISHello.Parcelable;

// Declare any non-default types here with import statements
import com.ISHello.Parcelable.Book;
interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}
