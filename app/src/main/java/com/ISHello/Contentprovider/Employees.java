package com.ISHello.Contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Employees {

    public static final String AUTHORITY = "com.ISHello.provider.Employees";

    private Employees() {

    }

    final static class Employee implements BaseColumns {
        private Employee() {

        }

        // 访问Uri
        public static final Uri CONTENT_URI = Uri.parse("content://"
                + AUTHORITY + "/employee");
        // 内容类型
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.provider.employees";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.provider.employees";

        // 默认排序常量
        public static final String DEFAULT_SORT_ORDER = "_id DESC";
        // 表字段常量
        public static final String NAME = "name";// 姓名
        public static final String GENDER = "gender";// 性别
        public static final String AGE = "age";// 年龄
    }

}
