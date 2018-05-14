package zlonglove.cn.adrecyclerview.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import zlonglove.cn.adrecyclerview.R;
import zlonglove.cn.adrecyclerview.base.BaseSimpleRecyclerAdapter;
import zlonglove.cn.adrecyclerview.entity.MenuItem;
import zlonglove.cn.adrecyclerview.tools.DrawableKit;
import zlonglove.cn.adrecyclerview.tools.ImageKit;
import zlonglove.cn.adrecyclerview.viewHolder.MenuRecyclerGridHolder;


/**
 * 描述:编辑页面主体元素的子元素适配器
 * <p>
 *
 * @version 1.0
 */
public class MenuRecyclerGridAdapter extends BaseSimpleRecyclerAdapter<MenuRecyclerGridHolder, MenuItem> {
    public MenuRecyclerGridAdapter(List<MenuItem> recyclerItems) {
        super(recyclerItems);
    }

    @Override
    public MenuRecyclerGridHolder createRecyclerViewHolder(ViewGroup parent, int viewType) {
        return new MenuRecyclerGridHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_grid, parent, false));
    }

    @Override
    public void bindViewHolder(MenuRecyclerGridHolder holder, MenuItem item) {
        holder.iv_icon.setImageResource(ImageKit.getMipMapImageSrcIdWithReflectByName(item.getIcon()));
        holder.tv_name.setText(item.getName());
        DrawableKit.removeDrawableTintColor(holder.iv_icon.getDrawable());
    }

    @Override
    public int getItemCount() {
        return mRecyclerItems == null ? 0 : mRecyclerItems.size();
    }
}
