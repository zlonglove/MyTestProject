package zlonglove.cn.recyclerview.adapter;

import android.content.Context;
import android.view.ViewGroup;

import java.util.List;

import zlonglove.cn.recyclerview.holder.BaseViewHolder;
import zlonglove.cn.recyclerview.support.MultiItemTypeSupport;

/**
 * Created on 2017/5/24.
 *
 * @author
 */

public abstract class MultiItemCommonAdapter<T> extends CommonAdapter<T> {
    protected MultiItemTypeSupport<T> mMultiItemTypeSupport;

    /**
     *
     * @param context
     * @param datas
     * @param multiItemTypeSupport
     *
     */
    public MultiItemCommonAdapter(Context context, List<T> datas, MultiItemTypeSupport<T> multiItemTypeSupport) {
        super(context, -1, datas);
        mMultiItemTypeSupport = multiItemTypeSupport;
    }

    @Override
    public int getItemViewType(int position) {
        return mMultiItemTypeSupport.getItemViewType(position, mDatas.get(position));
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = mMultiItemTypeSupport.getLayoutId(viewType);
        BaseViewHolder holder = new BaseViewHolder(mContext, parent, layoutId);
        return holder;
    }


}
