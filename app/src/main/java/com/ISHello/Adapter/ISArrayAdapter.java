package com.ISHello.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ishelloword.R;

import java.util.List;

/**
 * @author zhanglong 使用方法 ArrayList<String>data=new ArrayList<String>(); int
 *         resourceId=R.layout.hiliway_array_item ISArrayAdapter<String>
 *         adapter=new ISArrayAdapter<String>(this,resourceId,data);
 *         mylistView.setAdapter(adapter);
 */
public class ISArrayAdapter extends ArrayAdapter<String> {
    private int layoutResource;

    public ISArrayAdapter(Context context, int resource,
                          int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
        layoutResource = resource;
    }

    /**
     * 用于构造、填充将添加到父AdapterView类(如ListView)中的视图,
     * 该父AdapterView类使用这个Adapter绑定到底层的数组
     * getItem将返回存储在底层数组的制定索引位置的值(从List<String>中获取)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder localViewHolder = null;
        if (convertView == null) {
            // 如果不是一次更新,则填充一个视图
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutResource, null);
            localViewHolder = new ViewHolder();
            localViewHolder.textView = (TextView) convertView
                    .findViewById(R.id.arrayitem);
            convertView.setTag(localViewHolder);
        } else {
            // 更新现有的视图
            localViewHolder = (ViewHolder) convertView.getTag();
        }
        localViewHolder.textView.setText(getItem(position));
        return convertView;
    }

    public void addItem(String item) {
        addItem(item);
        notifyDataSetChanged();
    }

    /**
     * 定义resource布局文件里面的控件
     *
     * @author zhanglong
     */
    class ViewHolder {
        TextView textView;
    }

}
