package zlonglove.cn.adrecyclerview.base;

import android.view.View;

/**
 * 描述:RecyclerView的条目长按单击监听器
 * <p>
 * 作者:cjs
 * 创建时间:2017年10月18日 9:35
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
public interface OnRecyclerItemLongClickListener<RI extends BaseRecyclerItem> {
    /**
     * 列表条目被单击时
     * @param v 当前条目的视图
     * @param item 当前条目对应的数据
     * @param position 当前条目在指定区域里面的位置
     * @param segment 被点击时的区域<br>
     *                {@link com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter#SEGMENT_TYPE_FOOTER} 处于footer<br>
     *                {@link com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter#SEGMENT_TYPE_HEADER} 处于header<br>
     *                {@link com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter#SEGMENT_TYPE_NORMAL} 处于正常的列表范围里面<br>
     */
    void onItemLongClick(View v, RI item, int position, int segment);
}
