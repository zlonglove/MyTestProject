package zlonglove.cn.adrecyclerview.helper;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zlonglove.cn.adrecyclerview.entity.MenuItem;
import zlonglove.cn.adrecyclerview.tools.ContextUtil;
import zlonglove.cn.adrecyclerview.tools.IOKit;


/**
 * 描述:菜单数据控制助手，模拟本地数据库
 * <p>
 *
 * @version 1.0
 */
public class MenuHelper {
    /*分组的标签*/
    public static final String GROUP_FAVORITE = "favorite";
    public static final String GROUP_COLD_WEAPON = "cold_weapon";
    public static final String GROUP_MODERN_WEAPON = "modern_weapon";
    public static final String GROUP_MISC = "misc";
    public static final String GROUP_PERSON = "person";
    public static final String GROUP_EQUIPMENT = "equipment";

    public static int itemCounter = 0;//用于统计共有多少个子item,依次给每个item设置独立的id


    private static JSONObject dataJson = null;

    /**
     * 解析原始数据，用于模拟从服务器上获取到的JSON报文
     */
    public static List<MenuItem> parseFavorite() {
        if (dataJson == null) {
            initJSONData();
        }
        return parseJSONList(dataJson, GROUP_FAVORITE);
    }

    public static List<MenuItem> parseColdWeapon() {
        if (dataJson == null) {
            initJSONData();
        }
        return parseJSONList(dataJson, GROUP_COLD_WEAPON);
    }

    public static List<MenuItem> parseModernWeapon() {
        if (dataJson == null) {
            initJSONData();
        }
        return parseJSONList(dataJson, GROUP_MODERN_WEAPON);
    }


    public static List<MenuItem> parseMisc() {
        if (dataJson == null) {
            initJSONData();
        }
        return parseJSONList(dataJson, GROUP_MISC);
    }

    public static List<MenuItem> parseEquipment() {
        if (dataJson == null) {
            initJSONData();
        }
        return parseJSONList(dataJson, GROUP_EQUIPMENT);
    }

    public static List<MenuItem> parsePerson() {
        if (dataJson == null) {
            initJSONData();
        }
        return parseJSONList(dataJson, GROUP_PERSON);
    }

    public static void initJSONData() {
        if (dataJson == null) {
            String jsonStr = IOKit.getStringFromAssets(ContextUtil.getContext(), "dummy.json");//获取到assets目录下的报文
            dataJson = JSON.parseObject(jsonStr);//将报文string转换为JSON
        }
    }

    public static List<MenuItem> parseJSONList(JSONObject dataJSON, String group) {
        List<MenuItem> list = new ArrayList<>();
        JSONArray array = dataJSON.getJSONArray(group);
        int size = array.size();
        for (int i = 0; i < size; i++, itemCounter++) {
            JSONObject object = array.getJSONObject(i);
            //之所以没有在array层就进行JSON到java对象的转换，是为了进入内部遍历，产生id,并将id赋值给menuItem
            MenuItem item = JSON.toJavaObject(object, MenuItem.class);
            item.setItemId(itemCounter);
            list.add(item);
        }
        return list;
    }
}
