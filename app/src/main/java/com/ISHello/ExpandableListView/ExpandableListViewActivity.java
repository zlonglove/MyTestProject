package com.ISHello.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import com.example.ishelloword.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class ExpandableListViewActivity extends Activity {
    private ExpandableListView expandableListView;
    List<String> group;           //组列表
    List<List<String>> child;     //子列表
    ContactsInfoAdapter adapter;  //数据适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandable_listview_layout);
        expandableListView = (ExpandableListView) findViewById(R.id.ExpandableListView);

        initializeData();
        expandableListView.setAdapter(new ContactsInfoAdapter());
        expandableListView.setCacheColorHint(0);  //设置拖动列表的时候防止出现黑色背景
    }

    /**
     * 初始化组、子列表数据
     */
    private void initializeData() {
        group = new ArrayList<String>();
        child = new ArrayList<List<String>>();

        addInfo("Andy", new String[]{"male", "138123***", "GuangZhou"});
        addInfo("Fairy", new String[]{"female", "138123***", "GuangZhou"});
        addInfo("Jerry", new String[]{"male", "138123***", "ShenZhen"});
        addInfo("Tom", new String[]{"female", "138123***", "ShangHai"});
        addInfo("Bill", new String[]{"male", "138231***", "ZhanJiang"});

    }

    /**
     * 模拟给组、子列表添加数据
     *
     * @param g-group
     * @param c-child
     */
    private void addInfo(String g, String[] c) {
        group.add(g);
        List<String> childitem = new ArrayList<String>();

        for (int i = 0; i < c.length; i++) {
            childitem.add(c[i]);
        }
        child.add(childitem);
    }

    class ContactsInfoAdapter extends BaseExpandableListAdapter {
        //-----------------Child----------------//
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return child.get(groupPosition).get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return child.get(groupPosition).size();
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            String string = child.get(groupPosition).get(childPosition);
            return getGenericView(string);
        }

        //----------------Group----------------//
        @Override
        public Object getGroup(int groupPosition) {
            return group.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getGroupCount() {
            return group.size();
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String string = group.get(groupPosition);
            return getGenericView(string);
        }

        //创建组/子视图
        public TextView getGenericView(String s) {
            // Layout parameters for the ExpandableListView
            @SuppressWarnings("deprecation")
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, 100);

            TextView text = new TextView(ExpandableListViewActivity.this);
            text.setLayoutParams(lp);
            // Center the text vertically
            text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            text.setPadding(100, 0, 0, 0);
            text.setTextColor(Color.BLUE);
            text.setText(s);
            return text;
        }


        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return true;
        }

    }

}
