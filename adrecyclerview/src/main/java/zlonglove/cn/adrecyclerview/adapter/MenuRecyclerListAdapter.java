package zlonglove.cn.adrecyclerview.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zlonglove.cn.adrecyclerview.R;
import zlonglove.cn.adrecyclerview.base.BaseSimpleRecyclerAdapter;
import zlonglove.cn.adrecyclerview.base.OnRecyclerItemClickListener;
import zlonglove.cn.adrecyclerview.entity.EditItem;
import zlonglove.cn.adrecyclerview.entity.MenuItem;
import zlonglove.cn.adrecyclerview.viewHolder.MenuEditRecyclerListHolder;


/**
 * 描述:编辑页面的列表适配器
 * <p>
 * 创建时间:2017年11月03日 17:47
 *
 * @version 1.0
 */
public class MenuRecyclerListAdapter extends BaseSimpleRecyclerAdapter<MenuEditRecyclerListHolder, EditItem> {
    private Map<String, MenuRecyclerGridAdapter> mAdapterMap = new HashMap<>();//用于存放子列表的adapter的map
    private OnRecyclerItemClickListener<MenuItem> mChildItemClickListener;

    public void setChildItemClickListener(OnRecyclerItemClickListener<MenuItem> childItemClickListener) {
        mChildItemClickListener = childItemClickListener;
    }

    public MenuRecyclerListAdapter(List<EditItem> recyclerItems) {
        super(recyclerItems);
    }

    @Override
    public MenuEditRecyclerListHolder createRecyclerViewHolder(ViewGroup parent, int viewType) {
        return new MenuEditRecyclerListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_edit, parent, false));
    }

    @Override
    public void bindViewHolder(MenuEditRecyclerListHolder holder, EditItem item) {
        holder.tv_group_name.setText(item.getGroupTitle());
        MenuRecyclerGridAdapter adapter = new MenuRecyclerGridAdapter(item.getMenuItemList());
        adapter.setOnRecyclerItemClickListener(mChildItemClickListener);
        holder.recyclerView.setLayoutManager(new GridLayoutManager(holder.recyclerView.getContext(), 4));
        holder.recyclerView.setAdapter(adapter);
        mAdapterMap.put(item.getGroup(), adapter);
    }

    @Override
    public int getItemCount() {
        return mRecyclerItems == null ? 0 : mRecyclerItems.size();
    }

    public void notifyChildDataChanged(String group, MenuItem item) {
        MenuRecyclerGridAdapter adapter = mAdapterMap.get(group);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void notifyChildDataAdded(String group, MenuItem item) {
        MenuRecyclerGridAdapter adapter = mAdapterMap.get(group);
        if (adapter != null) {
            if (!adapter.getRecyclerItems().contains(item)) {
                adapter.getRecyclerItems().add(item);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void notifyChildDataRemoved(String group, MenuItem item) {
        MenuRecyclerGridAdapter adapter = mAdapterMap.get(group);
        if (adapter != null) {
            adapter.getRecyclerItems().remove(item);
            adapter.notifyDataSetChanged();
        }
    }
}
