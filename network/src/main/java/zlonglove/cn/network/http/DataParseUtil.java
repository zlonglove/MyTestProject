package zlonglove.cn.network.http;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务器返回数据的解析工具；
 * 支持XML,json对象,json数组
 * </p>
 *
 * @author 2017/1/9 16:00
 * @version V1.2.0
 * @name DataParseUtil
 */


public class DataParseUtil {

    private DataParseUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 解析json对象
     *
     * @param string 要解析的json
     * @param clazz  解析类
     */
    public static <T> T parseObject(String string, Class<T> clazz) {
        Log.i("DataParseUtil", "--->parseObject()" + string);
        return new Gson().fromJson(string, clazz);
    }

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
     * 解析json数组为ArrayList
     *
     * @param json  要解析的json
     * @param clazz 解析类
     * @return ArrayList
     */
    public static <T> ArrayList<T> parseToArrayList(String json, Class<T> clazz) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);
        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

    /**
     * 解析json数组为List
     *
     * @param json  要解析的json
     * @param clazz 解析类simple-xml-core.jar
     * @return List
     */
    public static <T> List<T> parseToList(String json, Class<T[]> clazz) {
        Gson gson = new Gson();
        T[] array = gson.fromJson(json, clazz);
        return Arrays.asList(array);
    }


    /**
     * 解析Xml格式数据
     *
     * @param json  要解析的json
     * @param clazz 解析类
     */
    public static Object parseXml(String json, Class<?> clazz) {
        try {
            if (!TextUtils.isEmpty(json) && clazz != null) {
                Serializer serializer = new Persister();
                InputStreamReader is = new InputStreamReader(new ByteArrayInputStream(json.getBytes("UTF-8")), "utf-8");
                return serializer.read(clazz, is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
