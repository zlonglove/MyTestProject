package com.ISHello.Banner;

/**
 * Created by zhanglong on 2017/06/12.
 */

public interface MZHolderCreator<VH extends MZViewHolder> {
    /**
     * 创建ViewHolder
     *
     * @return
     */
    VH createViewHolder();
}
