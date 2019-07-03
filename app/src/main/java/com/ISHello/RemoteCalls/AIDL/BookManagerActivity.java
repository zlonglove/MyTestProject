package com.ISHello.RemoteCalls.AIDL;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ISHello.Parcelable.Book;
import com.ISHello.Parcelable.BookAidlInterface;
import com.ISHello.Parcelable.IOnNewBookArrivedListener;
import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

import java.util.List;

public class BookManagerActivity extends BaseActivity {
    private final String TAG = "BookManagerActivity";
    private TextView book_show;
    private BookAidlInterface bookAidlInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        setTitle("AIDL");
        findViews();
        init();
    }

    private void findViews() {
        book_show = findViewById(R.id.book_show);
    }

    private void init() {
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
                /**
                 * 由于被调用的方法运行在服务端的Binder线程池中，同时客户端被挂起，如果远程
                 * 某个方法是耗时的，那么要避免在客户端的UI线程中调用
                 */
                bookAidlInterface.addBook(newBook);
                List<Book> newList = bookAidlInterface.getBookList();
                Log.i(TAG, "--->bookList==" + newList.toString());
                book_show.setText(newList.toString());
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
                bookAidlInterface.registerListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
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

    private IOnNewBookArrivedListener mOnNewBookArrivedListener = new
            IOnNewBookArrivedListener.Stub() {
                @Override
                public void onNewBookArrived(Book newBook) throws RemoteException {
                    /**
                     * 由于运行在Binder线程池中，所以不能进行UI操作，可使用Handler
                     */
                    Log.i(TAG, "--->bookList==" + newBook.toString() + Thread.currentThread().getName());
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
        if (bookAidlInterface != null && bookAidlInterface.asBinder().isBinderAlive()) {
            try {
                bookAidlInterface.unregisterListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
        super.onDestroy();
    }
}
