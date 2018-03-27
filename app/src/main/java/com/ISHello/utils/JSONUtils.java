package com.ISHello.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JSONUtils {
    /**
     * 通过gson来解析json字符串，并封装到对象中
     *
     * @param jsonString 得到的json字符串
     * @param cls        要将json数据封装到的对象字节码
     * @return
     */
    public static <T> T readJSONToObject(String jsonString, Class<T> cls) {
        T t = null;
        try {
            t = new Gson().fromJson(jsonString, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 通过gson将json字符串转化为对象，并存入到list中
     *
     * @param jsonString 要解析的json字符串
     * @param cls        封装的对象字节码
     * @return
     */
    public static <T> List<T> readJSONToObjectList(String jsonString, Class<T> cls) throws Exception {

        List<T> list = new ArrayList<T>();
        try {
            JsonArray asJsonArray = new JsonParser().parse(jsonString).getAsJsonArray();
            for (JsonElement element : asJsonArray) {
                list.add(new Gson().fromJson(element, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // try {
        // list = GsonMapper.getInstance().fromJson(jsonString, new
        // TypeToken<List<T>>() {
        // }.getType());
        //
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        return list;
    }
}
