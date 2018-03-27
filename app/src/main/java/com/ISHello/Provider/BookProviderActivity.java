package com.ISHello.Provider;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;

import com.example.ishelloword.R;

public class BookProviderActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_provider);
        Uri uri = Uri.parse("content://cn.zlonglove.com.book.provider");
        getContentResolver().query(uri, null, null, null, null);
        getContentResolver().query(uri, null, null, null, null);
        getContentResolver().query(uri, null, null, null, null);
        getContentResolver().query(uri, null, null, null, null);
    }

}
