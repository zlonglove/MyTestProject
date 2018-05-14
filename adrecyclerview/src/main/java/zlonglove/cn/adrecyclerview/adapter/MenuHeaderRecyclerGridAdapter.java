package zlonglove.cn.adrecyclerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import zlonglove.cn.adrecyclerview.R;
import zlonglove.cn.adrecyclerview.base.BaseDraggableRecyclerAdapter;
import zlonglove.cn.adrecyclerview.entity.MenuItem;
import zlonglove.cn.adrecyclerview.tools.DrawableKit;
import zlonglove.cn.adrecyclerview.tools.ImageKit;
import zlonglove.cn.adrecyclerview.viewHolder.MenuHeaderRecyclerGridHolder;


/**
 * 描述:编辑页面的头部的子元素适配器
 * <p>
 * 创建时间:2017年11月03日 17:07
 *
 * @version 1.0
 */
public class MenuHeaderRecyclerGridAdapter extends BaseDraggableRecyclerAdapter<MenuHeaderRecyclerGridHolder, MenuItem> {
    private boolean showEditIcon;
    private OnDeleteClickListener mOnDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        mOnDeleteClickListener = onDeleteClickListener;
    }

    /**
     * 可以拖拽的RecyclerView适配器
     *
     * @param recyclerItems 数据列表
     * @param recyclerView  与改适配器匹配的RecyclerView
     */
    public MenuHeaderRecyclerGridAdapter(List<MenuItem> recyclerItems, RecyclerView recyclerView, boolean showEditIcon) {
        super(recyclerItems, recyclerView);
        this.showEditIcon=showEditIcon;
    }

    @Override
    public MenuHeaderRecyclerGridHolder createRecyclerViewHolder(ViewGroup parent, int viewType) {
        return new MenuHeaderRecyclerGridHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_grid_func,parent,false));
    }

    @Override
    public void bindViewHolder(final MenuHeaderRecyclerGridHolder holder, MenuItem item) {
        final MenuItem menuItem=item;
        holder.tv_delete.setVisibility(showEditIcon? View.VISIBLE:View.GONE);
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnDeleteClickListener!=null){
                    mOnDeleteClickListener.onDeleteClick(v,menuItem,holder.getAdapterPosition());
                }
            }
        });
        holder.tv_name.setText(item.getName());
        holder.iv_icon.setImageResource(ImageKit.getDrawableImageSrcIdByName(item.getIcon()));
        DrawableKit.setDrawableTintColor(holder.iv_icon.getDrawable(),R.color.colorPrimary_66);
    }

    @Override
    public int getItemCount() {
        return mRecyclerItems == null ? 0 : mRecyclerItems.size();
    }

    /**
     * 描述:删除脚标单击监听器
     *
     * <br>创建时间: 2017/11/6 0006 14:54
     * @version 1.0
     */
    public interface OnDeleteClickListener{
        void onDeleteClick(View v, MenuItem item, int position);
    }
}
