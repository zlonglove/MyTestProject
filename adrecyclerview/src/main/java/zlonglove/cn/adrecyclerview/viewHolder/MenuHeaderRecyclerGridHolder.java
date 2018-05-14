package zlonglove.cn.adrecyclerview.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.draggable.annotation.DraggableItemStateFlags;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import zlonglove.cn.adrecyclerview.R;


/**
 * 描述:菜单编辑列表适配器的头部的ViewHolder<br>
 *     注意，一定要继承{@link AbstractDraggableItemViewHolder}或者照着这个类实现{@link com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemViewHolder}
 *     才能有拖拽效果
 * <p>
 * 作者:陈俊森
 * 创建时间:2017年11月03日 17:07
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
public class MenuHeaderRecyclerGridHolder extends MenuRecyclerGridHolder implements DraggableItemViewHolder {
    public TextView tv_delete;

    @DraggableItemStateFlags
    private int mDragStateFlags;

    public MenuHeaderRecyclerGridHolder(View itemView) {
        super(itemView);
        tv_delete= (TextView) itemView.findViewById(R.id.delete);
    }

    @Override
    public void setDragStateFlags(@DraggableItemStateFlags int flags) {
        mDragStateFlags=flags;
    }

    @Override
    @DraggableItemStateFlags
    public int getDragStateFlags() {
        return mDragStateFlags;
    }
}
