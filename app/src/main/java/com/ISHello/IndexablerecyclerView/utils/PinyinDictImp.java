package com.ISHello.IndexablerecyclerView.utils;

import android.content.Context;

/**
 * @author
 */
public class PinyinDictImp extends PinyinDict {
    static volatile PinyinDictImp singleton = null;

    public PinyinDictImp(Context context) {
        super(context);
    }

    @Override
    protected String assetFileName() {
        return "cncity.txt";
    }

    public static PinyinDictImp getInstance(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context == null");
        }
        if (singleton == null) {
            synchronized (PinyinDictImp.class) {
                if (singleton == null) {
                    singleton = new PinyinDictImp(context);
                }
            }
        }
        return singleton;
    }
}
