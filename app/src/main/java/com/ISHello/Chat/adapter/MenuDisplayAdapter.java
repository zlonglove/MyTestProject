package com.ISHello.Chat.adapter;

import android.util.TypedValue;
import android.widget.TextView;

import com.ISHello.Application.BaseApplication;
import com.ISHello.utils.LogUtil;
import com.example.ishelloword.R;

import java.util.List;


public class MenuDisplayAdapter extends BaseWrappedAdapter<String, BaseWrappedViewHolder> {

    public MenuDisplayAdapter(List<String> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, String data) {
        holder.setText(R.id.tv_menu_item, data);
        int position = holder.getAdapterPosition();
        LogUtil.log("position22" + position);
        //int size;
        if (position == 0) {
            ((TextView) holder.getView(R.id.tv_menu_item)).setCompoundDrawablesWithIntrinsicBounds(holder.itemView.getContext().getResources().getDrawable(R.drawable.ic_chat_blue_grey_900_24dp), null, null, null);
            /*if ((size = ChatDB.create().hasUnReadChatMessage()) > 0) {
                holder.setVisible(R.id.tv_menu_item_tips, true)
                        .setText(R.id.tv_menu_item_tips, size + "");
            } else {
                holder.setVisible(R.id.tv_menu_item_tips, false);
            }*/
            holder.setVisible(R.id.tv_menu_item_tips, false);

        } else if (position == 1) {
            ((TextView) holder.getView(R.id.tv_menu_item)).setCompoundDrawablesWithIntrinsicBounds(holder.itemView.getContext().getResources().getDrawable(R.drawable.ic_people_blue_grey_900_24dp), null, null, null);
        } else if (position == 2) {
           /* if ((size = ChatDB.create().hasUnReadInvitation()) > 0) {
                holder.setVisible(R.id.tv_menu_item_tips, true)
                        .setText(R.id.tv_menu_item_tips, size + "");
            } else {
                holder.setVisible(R.id.tv_menu_item_tips, false);
            }*/
            holder.setVisible(R.id.tv_menu_item_tips, false);
            ((TextView) holder.getView(R.id.tv_menu_item)).setCompoundDrawablesWithIntrinsicBounds(holder.itemView.getContext().getResources().getDrawable(R.drawable.ic_insert_invitation_blue_grey_900_24dp), null, null, null);
        } else if (position == 3) {
            ((TextView) holder.getView(R.id.tv_menu_item)).setCompoundDrawablesWithIntrinsicBounds(holder.itemView.getContext().getResources().getDrawable(R.drawable.ic_fiber_new_blue_grey_900_24dp), null, null, null);
        }
        ((TextView) holder.getView(R.id.tv_menu_item)).setCompoundDrawablePadding(todp(10));
    }

    public int todp(int i) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, BaseApplication.getInstance().getResources().getDisplayMetrics());
    }
}
