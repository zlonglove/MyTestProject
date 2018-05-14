package zlonglove.cn.adrecyclerview.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:对RecyclerView的Adapter进行二次包裹，使其具有设置Header和Footer的功能<br>
 *     FI-FooterItem 尾部数据模型<br>
 *     HI-HeaderItem 头部数据类型<br>
 *     HeaderVH-头部的ViewHolder<br>
 *     FooterVH-尾部的ViewHolder<br>
 * <p>
 * 创建时间:2017年10月17日 17:02
 *
 * @version 1.0
 */
public abstract class BaseHeaderFooterRecyclerAdapterWrapper<FI extends BaseRecyclerItem, HI extends BaseRecyclerItem,HeaderVH extends RecyclerView.ViewHolder, FooterVH extends RecyclerView.ViewHolder> extends AbstractHeaderFooterWrapperAdapter<HeaderVH,FooterVH> {

    /**
     * 创建头部的ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    public abstract HeaderVH createHeaderViewHolder(ViewGroup parent, int viewType);

    /**
     * 创建尾部的ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    public abstract FooterVH createFooterViewHolder(ViewGroup parent, int viewType);

    /**
     * 绑定头部的ViewHolder
     * @param headerViewHolder
     * @param headerItem
     */
    public abstract void bindHeaderViewHolder(HeaderVH headerViewHolder,HI headerItem);

    /**
     * 绑定尾部的ViewHolder
     * @param footerViewHolder
     * @param footerItem
     */
    public abstract void bindFooterViewHolder(FooterVH footerViewHolder,FI footerItem);

    /**
     * 存放当前适配器中的所有尾部视图的数据列表
     */
    protected List<FI> mFooterItems;
    /**
     * 存放当前适配器中所有头部视图的数据列表
     */
    protected List<HI> mHeaderItems;

    protected OnRecyclerItemClickListener mOnHeaderItemClickListener;
    protected OnRecyclerItemClickListener mOnFooterItemClickListener;

    protected OnRecyclerItemLongClickListener mOnHeaderItemLongClickListener;
    protected OnRecyclerItemLongClickListener mOnFooterItemLongClickListener;

    /**
     * 具有设置Header和Footer功能的RecyclerView适配器
     * @param adapter 原始的RecyclerView适配器
     */
    public BaseHeaderFooterRecyclerAdapterWrapper(RecyclerView.Adapter adapter) {
        setAdapter(adapter);
        setHasStableIds(true);
        mFooterItems = new ArrayList<>();
        mHeaderItems = new ArrayList<>();
    }

    public void setOnHeaderItemClickListener(OnRecyclerItemClickListener onHeaderItemClickListener) {
        mOnHeaderItemClickListener = onHeaderItemClickListener;
    }

    public void setOnFooterItemClickListener(OnRecyclerItemClickListener onFooterItemClickListener) {
        mOnFooterItemClickListener = onFooterItemClickListener;
    }

    public void setOnHeaderItemLongClickListener(OnRecyclerItemLongClickListener onHeaderItemLongClickListener) {
        mOnHeaderItemLongClickListener = onHeaderItemLongClickListener;
    }

    public void setOnFooterItemLongClickListener(OnRecyclerItemLongClickListener onFooterItemLongClickListener) {
        mOnFooterItemLongClickListener = onFooterItemLongClickListener;
    }

    /**
     * 获取当前列表的头部数量
     * @return
     */
    @Override
    public int getHeaderItemCount() {
        return mHeaderItems.size();
    }

    /**
     * 获取当前列表的尾部数量
     * @return
     */
    @Override
    public int getFooterItemCount() {
        return mFooterItems.size();
    }

    /**
     * 获取当前头部的视图类型
     * @param localPosition The header adapter local position to query
     * @return
     */
    @Override
    public int getHeaderItemViewType(int localPosition) {
        return mHeaderItems.get(localPosition).getViewType();
    }

    /**
     * 获取当前尾部的视图类型
     * @param localPosition The footer adapter local position to query
     * @return
     */
    @Override
    public int getFooterItemViewType(int localPosition) {
        return mFooterItems.get(localPosition).getViewType();
    }

    @Override
    public HeaderVH onCreateHeaderItemViewHolder(ViewGroup parent, int viewType) {
        return createHeaderViewHolder(parent,viewType);
    }

    @Override
    public FooterVH onCreateFooterItemViewHolder(ViewGroup parent, int viewType) {
        return createFooterViewHolder(parent, viewType);
    }

    @Override
    public void onBindFooterItemViewHolder(FooterVH holder, int localPosition) {
        final int position=localPosition;
        final FI item=mFooterItems.get(localPosition);
        bindFooterViewHolder(holder,item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnFooterItemClickListener!=null){
                    mOnFooterItemClickListener.onItemClick(v,item,position,SEGMENT_TYPE_FOOTER);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mOnFooterItemLongClickListener!=null){
                    mOnFooterItemLongClickListener.onItemLongClick(v,item,position,SEGMENT_TYPE_FOOTER);
                }
                return true;
            }
        });
    }

    @Override
    public void onBindHeaderItemViewHolder(HeaderVH holder, int localPosition) {
        final int position=localPosition;
        final HI item=mHeaderItems.get(localPosition);
        bindHeaderViewHolder(holder,item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnHeaderItemClickListener !=null){
                    mOnHeaderItemClickListener.onItemClick(v,item,position,SEGMENT_TYPE_HEADER);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mOnHeaderItemLongClickListener!=null){
                    mOnHeaderItemLongClickListener.onItemLongClick(v,item,position,SEGMENT_TYPE_HEADER);
                }
                return true;
            }
        });
    }


    /**
     * 添加一个头部
     * @param headerItem
     */
    public void addHeader(HI headerItem) {
        mHeaderItems.add(headerItem);
        getHeaderAdapter().notifyItemInserted(mHeaderItems.size() - 1);
    }

    /**
     * 移除掉一个头部<br>
     *     移除掉头部列表的最后一个
     */
    public void removeHeader() {
        removeHeader(mHeaderItems.size()-1);
    }

    /**
     * 移除掉一个头部
     * @param position 当前头部在头部列表中的位置
     */
    public void removeHeader(int position) {
        if (mHeaderItems.isEmpty()) {
            return;
        }
        mHeaderItems.remove(position);
        getHeaderAdapter().notifyItemRemoved(mHeaderItems.size());
    }

    /**
     * 添加一个尾部
     * @param footerItem
     */
    public void addFooter(FI footerItem) {
        mFooterItems.add(footerItem);
        getFooterAdapter().notifyItemInserted(mFooterItems.size() - 1);
    }

    /**
     * 移除掉一个尾部<br>
     *     移除掉尾部列表的最后一个
     */
    public void removeFooter() {
        removeFooter(mFooterItems.size()-1);
    }

    /**
     * 移除掉一个尾部
     * @param position
     */
    public void removeFooter(int position) {
        if (mFooterItems.isEmpty()) {
            return;
        }
        mFooterItems.remove(position);
        getFooterAdapter().notifyItemRemoved(mFooterItems.size());
    }

    private long getSegmentedPosition(View holderView){
        RecyclerView rv = RecyclerViewAdapterUtils.getParentRecyclerView(holderView);
        RecyclerView.ViewHolder vh = rv.findContainingViewHolder(holderView);

        int rootPosition = vh.getAdapterPosition();
        if (rootPosition == RecyclerView.NO_POSITION) {
            return RecyclerView.NO_POSITION;
        }

        // need to determine adapter local position like this:
        RecyclerView.Adapter rootAdapter = rv.getAdapter();
        int localPosition = WrapperAdapterUtils.unwrapPosition(rootAdapter, this, rootPosition);

        // get segment
        long segmentedPosition = getSegmentedPosition(localPosition);
        return segmentedPosition;
    }

    public int getSegment(View holderView){
        return extractSegmentPart(getSegmentedPosition(holderView));
    }

    public int getSegmentPosition(View holderView){
       return extractSegmentOffsetPart(getSegmentedPosition(holderView));
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
