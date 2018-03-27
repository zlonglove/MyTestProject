package com.Hello.Contacts;

import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleCursorAdapter;

public class CursorLoaderListFragment extends ListFragment implements
        LoaderCallbacks<Cursor>, OnQueryTextListener {

    SimpleCursorAdapter mAdapter;
    // 如果非null，这是当前的搜索过虑器
    String mCurFilter;

    public CursorLoaderListFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 如果列表中没有数据，就给控件一些文字去显示．在一个真正的应用
        // 中这应用资源中取得．
        setEmptyText("No phone numbers");

        // 我们在动作栏中有一个菜单项．
        setHasOptionsMenu(true);

        // 创建一个空的adapter，我们将用它显示加载后的数据
        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2, null,
                new String[]{Contacts.DISPLAY_NAME, Contacts._ID},
                new int[]{android.R.id.text1, android.R.id.text2}, 0);
        setListAdapter(mAdapter);

        // 准备loader.可能是重连到一个已存在的或开始一个新的
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // 放置一个动作栏项用于搜索．
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView sv = new SearchView(getActivity());
        sv.setOnQueryTextListener(this);
        item.setActionView(sv);
    }

    @Override
    public boolean onQueryTextChange(String arg0) {
        // 在动作栏上的搜索字串改变时被调用．更新
        //搜索过滤器，并重启loader来执行一个新的查询
        mCurFilter = !TextUtils.isEmpty(arg0) ? arg0 : null;
        getLoaderManager().restartLoader(0, null, this);
        return true;

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // TODO Auto-generated method stub
        return true;
    }

    // 这是我们想获取的联系人中一行的数据．
    static final String[] CONTACTS_SUMMARY_PROJECTION = new String[]{
            Contacts._ID,
            Contacts.DISPLAY_NAME,
            Contacts.CONTACT_STATUS,
            Contacts.CONTACT_PRESENCE,
            Contacts.PHOTO_ID,
            Contacts.LOOKUP_KEY,
    };


    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {

        // 当一个新的loader需被创建时调用．本例仅有一个Loader，
        //所以我们不需关心ID．首先设置base URI，URI指向的是联系人   
        Uri baseUri;
        if (mCurFilter != null) {
            baseUri = Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI,
                    Uri.encode(mCurFilter));
        } else {
            baseUri = Contacts.CONTENT_URI;
        }

        // 现在创建并返回一个CursorLoader，它将负责创建一个   
        // Cursor用于显示数据   
        String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                + Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                + Contacts.DISPLAY_NAME + " != '' ))";

        return new CursorLoader(getActivity(), baseUri,
                CONTACTS_SUMMARY_PROJECTION, select, null,
                Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");

    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {

        // 将新的cursor换进来．(框架将在我们返回时关心一下旧cursor的关闭)
        mAdapter.swapCursor(arg1);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {

        //在最后一个Cursor准备进入上面的onLoadFinished()之前．
        // Cursor要被关闭了，我们需要确保不再使用它．   
        mAdapter.swapCursor(null);

    }


}
