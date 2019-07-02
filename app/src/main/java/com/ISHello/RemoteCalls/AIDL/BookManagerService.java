package com.ISHello.RemoteCalls.AIDL;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.ISHello.Parcelable.Book;
import com.ISHello.Parcelable.BookAidlInterface;
import com.ISHello.Parcelable.IOnNewBookArrivedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookManagerService extends Service {
    private final String TAG = BookManagerService.class.getSimpleName();
    private AtomicBoolean mIsServiceDestoryd = new AtomicBoolean(false);
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();
    private RemoteCallbackList<IOnNewBookArrivedListener> mBookArrivedListener =
            new RemoteCallbackList<>();
    private Binder mBinder = new BookAidlInterface.Stub() {

       /* @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }*/

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            /*if (!mBookArrivedListener.contains(listener)){
                mBookArrivedListener.add(listener);
            }else{
                Log.e(TAG,"already exists");
            }*/
            mBookArrivedListener.register(listener);
            new Thread(new ServiceWorker()).start();
            Log.e(TAG, "registerListener successful");
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            /*if (!mBookArrivedListener.contains(listener)){
                mBookArrivedListener.remove(listener);
                Log.e(TAG,"unregisterListener success");
            }else {
                Log.e(TAG,"not found,can not unregister");
            }*/
            mIsServiceDestoryd.set(true);
            mBookArrivedListener.unregister(listener);
            Log.e(TAG, "unregisterListener success");
        }
    };

    public BookManagerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        int check = checkCallingOrSelfPermission("com.ISHello.RemoteCalls.AIDL.ACCESS_BOOK_SERVICE");
        if (check == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
        /*for (int i=0;i<mBookArrivedListener.size();i++){
            IOnNewBookArrivedListener listener=mBookArrivedListener.get(i);
            listener.onNewBookArrived(book);
        }*/
        final int N = mBookArrivedListener.beginBroadcast();
        Log.e("zhanglong","--->mBookArrivedListener=="+N);
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener l = mBookArrivedListener.getBroadcastItem(i);
            if (l != null) {
                try {
                    l.onNewBookArrived(book);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        }
        mBookArrivedListener.finishBroadcast();
    }

    private class ServiceWorker implements Runnable {

        @Override
        public void run() {
            while (!mIsServiceDestoryd.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "new Book#" + bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
