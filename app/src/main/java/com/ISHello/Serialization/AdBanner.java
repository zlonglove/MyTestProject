package com.ISHello.Serialization;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhanglong on 2017/9/28.
 */

public class AdBanner implements Serializable {
    private static final long serialVersionUID = 7060210544600464482L;
    public int mAccessTime;//本次访问时间
    public ArrayList<Image> mContentList;//图片数组

    public class Image implements Serializable {
        private static final long serialVersionUID = 7060210544600464483L;
        public int mType;//类型
        public String mIconUrl;//图标url
        public String mTitle;//标题
    }

    public AdBanner() {
        mAccessTime = 0;
        mContentList = new ArrayList<Image>();
    }
}
