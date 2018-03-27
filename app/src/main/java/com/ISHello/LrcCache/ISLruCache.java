package com.ISHello.LrcCache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by kfzx-zhanglong on 2016/5/12.
 * Company ICBC
 * Least Recently Used
 */
public class ISLruCache {
    public static int DefaultCacheSize = (int) Runtime.getRuntime().maxMemory() / 1024 / 8;
    private LruCache<String, Bitmap> mMemoryCache;
    private static ISLruCache isLruCache;

    public static ISLruCache getInstance() {
        if (isLruCache == null) {
            isLruCache = new ISLruCache(DefaultCacheSize);
        }
        return isLruCache;
    }

    private ISLruCache(int cacheSize) {
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    public void setValue(String key, Bitmap value) {
        if (mMemoryCache != null) {
            mMemoryCache.put(key, value);
        }
    }

    public Bitmap getValue(String key) {
        if (mMemoryCache != null) {
            return mMemoryCache.get(key);
        }
        return null;
    }

    public void remove(String key) {
        if (mMemoryCache != null) {
            mMemoryCache.remove(key);
        }
    }


}
