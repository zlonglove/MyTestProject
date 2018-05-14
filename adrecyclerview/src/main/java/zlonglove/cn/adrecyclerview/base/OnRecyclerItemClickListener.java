package zlonglove.cn.adrecyclerview.base;

import android.view.View;

/**
 * 描述:RecyclerView的条目单击监听器
 * <p>
 * 创建时间:2017年10月17日 17:26
 *
 * @version 1.0
 */
public interface OnRecyclerItemClickListener<RI extends BaseRecyclerItem> {
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
    void onItemClick(View v, RI item, int position, int segment);
}
