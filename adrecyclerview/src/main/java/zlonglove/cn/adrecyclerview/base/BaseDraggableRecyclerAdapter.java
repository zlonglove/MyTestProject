package zlonglove.cn.adrecyclerview.base;

import android.support.v7.widget.RecyclerView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;

import java.util.Collections;
import java.util.List;

/**
 * 描述:可以拖拽Item的RecyclerView适配器
 * <p>
 * 创建时间:2017年10月18日 15:32
 *
 * @version 1.0
 */
public abstract class BaseDraggableRecyclerAdapter<VH extends RecyclerView.ViewHolder,RI extends BaseRecyclerItem> extends BaseSimpleRecyclerAdapter<VH,RI> implements DraggableItemAdapter<VH> {
    static final int INVALID_POSITION=-1;
    private int mItemMoveMode = RecyclerViewDragDropManager.ITEM_MOVE_MODE_DEFAULT;
    private RI mLastRemovedData;
    private int mLastRemovedPosition = INVALID_POSITION;
    private RecyclerView mRecyclerView;

    /**
     * 可以拖拽的RecyclerView适配器
     * @param recyclerItems 数据列表
     * @param recyclerView 与改适配器匹配的RecyclerView
     */
    public BaseDraggableRecyclerAdapter(List<RI> recyclerItems, RecyclerView recyclerView) {
        super(recyclerItems);
        mRecyclerView=recyclerView;
    }

    public int getItemMoveMode() {
        return mItemMoveMode;
    }

    /**
     * 设置drag的动作模式:<br>
     *     1.{@link RecyclerViewDragDropManager#ITEM_MOVE_MODE_DEFAULT}默认模式，插入式地移动<br>
     *     2.{@link RecyclerViewDragDropManager#ITEM_MOVE_MODE_SWAP}两两对调
     * @param itemMoveMode
     */
    public void setItemMoveMode(int itemMoveMode) {
        mItemMoveMode = itemMoveMode;
    }

    @Override
    public boolean onCheckCanStartDrag(VH holder, int position, int x, int y) {
        return true;
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }
        if(mItemMoveMode== RecyclerViewDragDropManager.ITEM_MOVE_MODE_DEFAULT){
            moveItem(fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
        }else{
            swapItem(fromPosition, toPosition);
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }
    }

    /**
     * 重写该方法来设置可以Drag的Item的范围，默认是所有的都可以drag
     *
     * @param holder The ViewHolder which is associated to item user is attempt to start dragging.
     * @param position The position of the item within the adapter's data set.
     *
     * @return null: no constraints (= new ItemDraggableRange(0, getItemCount() - 1)), otherwise: the range specified item can be drag-sortable.
     */
    @Override
    public ItemDraggableRange onGetItemDraggableRange(VH holder, int position) {
        return null;
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return true;
    }

    private void moveItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }
        final RI item = mRecyclerItems.remove(fromPosition);
        mRecyclerItems.add(toPosition, item);
        mLastRemovedPosition = INVALID_POSITION;
    }

    private void swapItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }
        Collections.swap(mRecyclerItems, toPosition, fromPosition);
        mLastRemovedPosition = INVALID_POSITION;
    }
}
