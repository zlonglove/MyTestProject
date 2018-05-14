package zlonglove.cn.adrecyclerview.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;

import java.util.List;

/**
 * 描述:RecyclerView的通用适配器
 * <p>
 *
 * @version 1.0
 */
public abstract class BaseSimpleRecyclerAdapter<VH extends RecyclerView.ViewHolder, RI extends BaseRecyclerItem> extends RecyclerView.Adapter<VH> {
    protected List<RI> mRecyclerItems;
    protected OnRecyclerItemClickListener<RI> mOnRecyclerItemClickListener;
    protected OnRecyclerItemLongClickListener<RI> mOnRecyclerItemLongClickListener;

    /**
     * 创建ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    public abstract VH createRecyclerViewHolder(ViewGroup parent, int viewType);

    /**
     * 绑定ViewHolder
     *
     * @param holder
     * @param item
     */
    public abstract void bindViewHolder(VH holder, RI item);

    public BaseSimpleRecyclerAdapter(List<RI> recyclerItems) {
        mRecyclerItems = recyclerItems;
        setHasStableIds(true);
    }

    public List<RI> getRecyclerItems() {
        return mRecyclerItems;
    }

    public void updateRecyclerItems(RecyclerView recyclerView, List<RI> recyclerItems) {
        if (recyclerView == null) {
            return;
        }
        mRecyclerItems = recyclerItems;
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 获取列表条目单击监听器
     *
     * @return
     */
    public OnRecyclerItemClickListener getOnRecyclerItemClickListener() {
        return mOnRecyclerItemClickListener;
    }

    /**
     * 设置列表条目单击监听器
     *
     * @param onRecyclerItemClickListener
     */
    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        mOnRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    public OnRecyclerItemLongClickListener getOnRecyclerItemLongClickListener() {
        return mOnRecyclerItemLongClickListener;
    }

    public void setOnRecyclerItemLongClickListener(OnRecyclerItemLongClickListener onRecyclerItemLongClickListener) {
        mOnRecyclerItemLongClickListener = onRecyclerItemLongClickListener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return createRecyclerViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        final int p = position;
        final RI item = mRecyclerItems.get(p);
        bindViewHolder(holder, item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRecyclerItemClickListener != null) {
                    mOnRecyclerItemClickListener.onItemClick(v, item, p, AbstractHeaderFooterWrapperAdapter.SEGMENT_TYPE_NORMAL);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnRecyclerItemLongClickListener != null) {
                    mOnRecyclerItemLongClickListener.onItemLongClick(v, item, p, AbstractHeaderFooterWrapperAdapter.SEGMENT_TYPE_NORMAL);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return mRecyclerItems.get(position).getViewType();
    }

    @Override
    public long getItemId(int position) {
        return mRecyclerItems.get(position).getItemId();
    }
}
