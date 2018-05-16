package zlonglove.cn.adrecyclerview.helper;


import android.content.Context;
import android.content.SharedPreferences;

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

    private int itemCounter = 0;//用于统计共有多少个子item,依次给每个item设置独立的id

    /*分组数据的缓存列表，初始化分组的时候用*/
    private List<MenuItem> favoriteList;
    private List<MenuItem> coldList;
    private List<MenuItem> modernList;
    private List<MenuItem> miscList;
    private List<MenuItem> eqtList;
    private List<MenuItem> personList;

    /**
     * 解析原始数据，用于模拟从服务器上获取到的JSON报文
     */
    private void parseJSONData() {
        String jsonStr = IOKit.getStringFromAssets(ContextUtil.getContext(), "dummy.json");
        JSONObject dataJson = JSON.parseObject(jsonStr);//将报文string转换为JSON
        favoriteList = parseJSONList(dataJson, GROUP_FAVORITE);
        coldList = parseJSONList(dataJson, GROUP_COLD_WEAPON);
        modernList = parseJSONList(dataJson, GROUP_MODERN_WEAPON);
        miscList = parseJSONList(dataJson, GROUP_MISC);
        eqtList = parseJSONList(dataJson, GROUP_EQUIPMENT);
        personList = parseJSONList(dataJson, GROUP_PERSON);

        savePreferFavoriteList(favoriteList);
        savePreferColdWeaponList(coldList);
        savePreferEqtList(eqtList);
        savePreferMiscList(miscList);
        savePreferModernWeaponList(modernList);
        savePreferPersonList(personList);
    }

    private List<MenuItem> parseJSONList(JSONObject dataJSON, String group) {
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

    /**
     * 初始化数据
     */
    public static void init() {
        MenuHelper helper = new MenuHelper();
        helper.parseJSONData();
        setInit(true);
    }

    /**
     * 用于保存本地数据的文件名字
     */
    private static final String PREFERENCE_MENU_DATA_NAME = "menu_data";
    /**
     * 是否已经进行过初始化的字段名,初始化标志
     */
    private static final String PREFERENCE_HAS_EVER_INIT = "has_ever_init";

    /**
     * 获取本地数据的文件
     *
     * @return
     */
    public static SharedPreferences getMenuDataConfig() {
        return ContextUtil.getContext().getSharedPreferences(PREFERENCE_MENU_DATA_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 清空本地数据文件里面的内容
     */
    public static void clearMenuDataConfig() {
        getMenuDataConfig().edit().clear().commit();
    }

    public static boolean hasEverInit() {
        return getMenuDataConfig().getBoolean(PREFERENCE_HAS_EVER_INIT, false);
    }

    public static void setInit(boolean isInit) {
        getMenuDataConfig().edit().putBoolean(PREFERENCE_HAS_EVER_INIT, isInit).commit();
    }

    /*----------------------------原始方法-----------------------------------*/

    /**
     * 将List转换为JsonString保存进SharedPreference
     *
     * @param group
     * @param list
     */
    private static void savePreferMenuListData(String group, List<MenuItem> list) {
        SharedPreferences.Editor editor = getMenuDataConfig().edit();
        editor.putString(group, JSON.toJSONString(list));
        editor.commit();
    }

    /**
     * 从SharedPreference里面取出JsonString,再转换为List
     *
     * @param group
     * @return
     */
    private static List<MenuItem> getPreferMenuListData(String group) {
        String jsonStr = getMenuDataConfig().getString(group, "");
        JSONArray array = JSONArray.parseArray(jsonStr);
        /*List<MenuItem> list = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = (JSONObject) array.get(i);
            MenuItem item = JSON.toJavaObject(jsonObject, MenuItem.class);
            list.add(item);
        }
        return list;*/
        return array.toJavaList(MenuItem.class);
    }

    /**
     * 从本地数据缓存列表里面删除一个item
     *
     * @param group
     * @param item
     */
    public static void deleteItem(String group, MenuItem item) {
        List<MenuItem> list = getPreferMenuListData(group);
        for (MenuItem i : list) {
            if (i.getItemId() == item.getItemId()) {
                list.remove(i);
                break;
            }
        }
        savePreferMenuListData(group, list);
    }

    /**
     * 从本地数据元素里面添加一个item
     *
     * @param group
     * @param item
     */
    public static void addItem(String group, MenuItem item) {
        List<MenuItem> list = getPreferMenuListData(group);
        if (!contains(list, item)) {
            list.add(item);
            savePreferMenuListData(group, list);
        }
    }

    private static boolean contains(List<MenuItem> list, MenuItem item) {
        if (list != null && list.size() > 0) {
            for (MenuItem i : list) {
                if (i.getItemId() == item.getItemId()) {
                    return true;
                }
            }
        }
        return false;
    }
    /*----------------------------原始方法-----------------------------------*/


    /*----------------------------衍生方法-----------------------------------*/
    public static void savePreferFavoriteList(List<MenuItem> list) {
        savePreferMenuListData(GROUP_FAVORITE, list);
    }

    public static void savePreferColdWeaponList(List<MenuItem> list) {
        savePreferMenuListData(GROUP_COLD_WEAPON, list);
    }

    public static void savePreferModernWeaponList(List<MenuItem> list) {
        savePreferMenuListData(GROUP_MODERN_WEAPON, list);
    }

    public static void savePreferMiscList(List<MenuItem> list) {
        savePreferMenuListData(GROUP_MISC, list);
    }

    public static void savePreferEqtList(List<MenuItem> list) {
        savePreferMenuListData(GROUP_EQUIPMENT, list);
    }

    public static void savePreferPersonList(List<MenuItem> list) {
        savePreferMenuListData(GROUP_PERSON, list);
    }

    public static List<MenuItem> getPreferFavoriteList() {
        return getPreferMenuListData(GROUP_FAVORITE);
    }

    public static List<MenuItem> getPreferColdWeaponList() {
        return getPreferMenuListData(GROUP_COLD_WEAPON);
    }

    public static List<MenuItem> getPreferModernWeaponList() {
        return getPreferMenuListData(GROUP_MODERN_WEAPON);
    }

    public static List<MenuItem> getPreferMiscList() {
        return getPreferMenuListData(GROUP_MISC);
    }

    public static List<MenuItem> getPreferEquipmentList() {
        return getPreferMenuListData(GROUP_EQUIPMENT);
    }

    public static List<MenuItem> getPreferPersonList() {
        return getPreferMenuListData(GROUP_PERSON);
    }

    public static void addPreferFavoriteItem(MenuItem item) {
        addItem(GROUP_FAVORITE, item);
    }

    public static void addPreferColdItem(MenuItem item) {
        addItem(GROUP_COLD_WEAPON, item);
    }

    public static void addPreferEqtItem(MenuItem item) {
        addItem(GROUP_EQUIPMENT, item);
    }

    public static void addPreferModernItem(MenuItem item) {
        addItem(GROUP_MODERN_WEAPON, item);
    }

    public static void addPreferMiscItem(MenuItem item) {
        addItem(GROUP_MISC, item);
    }

    public static void addPreferPersonItem(MenuItem item) {
        addItem(GROUP_PERSON, item);
    }

    public static void deletePreferFavoriteItem(MenuItem item) {
        deleteItem(GROUP_FAVORITE, item);
    }

    public static void deletePreferColdItem(MenuItem item) {
        deleteItem(GROUP_COLD_WEAPON, item);
    }

    public static void deletePreferModernItem(MenuItem item) {
        deleteItem(GROUP_MODERN_WEAPON, item);
    }

    public static void deletePreferMiscItem(MenuItem item) {
        deleteItem(GROUP_MISC, item);
    }

    public static void deletePreferEqtItem(MenuItem item) {
        deleteItem(GROUP_EQUIPMENT, item);
    }

    public static void deletePreferPersonItem(MenuItem item) {
        deleteItem(GROUP_PERSON, item);
    }
    /*----------------------------衍生方法-----------------------------------*/
}
