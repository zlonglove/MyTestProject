package com.ISHello.Area;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ishelloword.R;

import java.util.List;

/**
 * This class defines an adapter for the ListView of 'CitiListActivity'
 *
 * @version 1.0
 * @date 2014-5-5
 */
public class CityListItemAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<String> mListData;

    public CityListItemAdapter(Context context, List<String> data) {
        mContext = context;
        mListData = data;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CityListItem itemView;
        if (convertView == null) {
            itemView = new CityListItem();
            convertView = mInflater.inflate(R.layout.citylist_list_item, null);
            itemView.title = (TextView) convertView.findViewById(R.id.citylist_list_item_title);
            convertView.setTag(itemView);
        } else {
            itemView = (CityListItem) convertView.getTag();
        }

        itemView.title.setText(mListData.get(position));
        if (position == mListData.size() - 1) {
            View view = convertView.findViewById(R.id.vi_divider);
            view.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class CityListItem {
        TextView title;
    }

}
