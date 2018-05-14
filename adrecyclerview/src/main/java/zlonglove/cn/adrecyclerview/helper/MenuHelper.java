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
    public static final String GROUP_FAVORITE="favorite";
    public static final String GROUP_COLD_WEAPON="cold_weapon";
    public static final String GROUP_MODERN_WEAPON="modern_weapon";
    public static final String GROUP_MISC="misc";
    public static final String GROUP_PERSON="person";
    public static final String GROUP_EQUIPMENT="equipment";

    public static int itemCounter = 0;//用于统计共有多少个子item,依次给每个item设置独立的id

    /*分组数据的缓存列表，初始化分组的时候用*/
    private static List<MenuItem> favoriteList;

    /**
     * 解析原始数据，用于模拟从服务器上获取到的JSON报文
     */
    public static List<MenuItem> parseJSONData() {
        String jsonStr = IOKit.getStringFromAssets(ContextUtil.getContext(), "dummy.json");//获取到assets目录下的报文
        JSONObject dataJson = JSON.parseObject(jsonStr);//将报文string转换为JSON
        favoriteList = parseJSONList(dataJson, GROUP_FAVORITE);
        return favoriteList;
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
