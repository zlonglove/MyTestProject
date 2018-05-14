package zlonglove.cn.adrecyclerview.entity;

import java.io.Serializable;
import java.util.List;

import zlonglove.cn.adrecyclerview.base.BaseRecyclerItem;


/**
 * 描述:列表条目的实体类
 * <p>
 * 创建时间:2017年11月03日 15:03
 *
 * @version 1.0
 */
public class EditItem implements BaseRecyclerItem,Serializable{
    private String mGroup;
    private String mGroupTitle;
    private List<MenuItem> mMenuItemList;

    public EditItem() {
    }

    public EditItem(String group, String groupTitle, List<MenuItem> menuItemList) {
        mGroup = group;
        mMenuItemList = menuItemList;
        mGroupTitle=groupTitle;
    }

    public String getGroup() {
        return mGroup;
    }

    public void setGroup(String group) {
        mGroup = group;
    }

    public String getGroupTitle() {
        return mGroupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        mGroupTitle = groupTitle;
    }

    public List<MenuItem> getMenuItemList() {
        return mMenuItemList;
    }

    public void setMenuItemList(List<MenuItem> menuItemList) {
        mMenuItemList = menuItemList;
    }

    private int viewType;
    private int itemId;

    @Override
    public int getViewType() {
        return viewType;
    }

    @Override
    public long getItemId() {
        return itemId;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
