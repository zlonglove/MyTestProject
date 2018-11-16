package com.ISHello.IndexablerecyclerView.utils;

import android.content.Context;

import com.github.promeg.pinyinhelper.PinyinMapDict;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 */
public abstract class PinyinDict extends PinyinMapDict {
    /**
     * 返回Asset中存储词典信息的文本文档的路径，必须非空
     *
     * @return
     */
    protected abstract String assetFileName();


    final Context mContext;

    final Map<String, String[]> mDict;

    public PinyinDict(Context context) {
        mContext = context.getApplicationContext();
        mDict = new HashMap<>();
        init();
    }

    @Override
    public Map<String, String[]> mapping() {
        return mDict;
    }

    private void init() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(assetFileName())));
            String line;
            while ((line = reader.readLine()) != null) {
                // process the line.
                /**
                 * 也就是说它是按空白部分进行拆分，不管这个空白是用什么操作留下的,提如空格键tab键
                 */
                String[] keyAndValue = line.split("\\s+");
                if (keyAndValue != null && keyAndValue.length == 2) {
                    String[] pinyinStrs = keyAndValue[0].split("'");
                    mDict.put(keyAndValue[1], pinyinStrs);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
