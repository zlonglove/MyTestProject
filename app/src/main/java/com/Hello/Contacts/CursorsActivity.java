package com.Hello.Contacts;

import java.util.ArrayList;

import com.example.ishelloword.R;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

public class CursorsActivity extends Activity {

    private final String TAG = "CursorsActivity";

    public CursorsActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "--->onCreate()");
        setContentView(R.layout.contacts_activity);
        this.getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new CursorLoaderListFragment()).commit();
    }

    public void newContact(String name, String number) {
        Log.d(TAG, "--->newContact,name:" + name + ",number:" + number);
        ContentResolver localContentResolver = getContentResolver();
        ContentValues localContentValues = new ContentValues();
        try {
            long l = ContentUris.parseId(localContentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, localContentValues));
            ArrayList<ContentProviderOperation> localArrayList = new ArrayList<ContentProviderOperation>();
            localArrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValue("raw_contact_id", Long.valueOf(l)).withValue("mimetype", "vnd.android.cursor.item/name").withValue("data1", name).build());
            localArrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValue("raw_contact_id", Long.valueOf(l)).withValue("mimetype", "vnd.android.cursor.item/phone_v2").withValue("data1", number).withValue("data2", Integer.valueOf(2)).build());
            localContentResolver.applyBatch("com.android.contacts", localArrayList);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }

    }


}
