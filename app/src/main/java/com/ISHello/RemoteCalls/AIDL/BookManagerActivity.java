package com.ISHello.RemoteCalls.AIDL;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.ISHello.Parcelable.Book;
import com.ISHello.Parcelable.BookAidlInterface;
import com.example.ishelloword.R;

import java.util.List;

public class BookManagerActivity extends Activity {
    private final String TAG = "BookManagerActivity";
    private BookAidlInterface bookAidlInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        startService();
    }

    /**
     * 按钮点击事件
     *
     * @param view
     */
    public void addBook(View view) {
        try {
            if (bookAidlInterface != null) {
                Book newBook = new Book(1, "Android");
                bookAidlInterface.addBook(newBook);
                List<Book> newList = bookAidlInterface.getBookList();
                Log.i(TAG, "--->bookList==" + newList.toString());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动Service
     */
    private void startService() {
        Intent intentService = new Intent();
        intentService.setAction("com.ISHello.aidl.BookManagerService");
        intentService.setPackage(getPackageName());
        intentService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bindService(intentService, mConnection, BIND_AUTO_CREATE);
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (iBinder == null) {
                return;
            }
            bookAidlInterface = BookAidlInterface.Stub.asInterface(iBinder);
            try {
                iBinder.linkToDeath(mDeathRecipient, 0);//给binder设置死亡代理
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (bookAidlInterface == null)
                return;
            bookAidlInterface.asBinder().unlinkToDeath(mDeathRecipient, 0);
            bookAidlInterface = null;
            /**
             * 重新绑定远程Service
             */
            startService();
        }
    };

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }
}
