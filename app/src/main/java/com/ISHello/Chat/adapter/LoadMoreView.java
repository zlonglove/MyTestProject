package com.ISHello.Chat.adapter;

import android.widget.Toast;

import com.ISHello.utils.LogUtil;


public abstract class LoadMoreView {
    //        加载状态
    public static final int STATUS_LOADING = 5;
    public static final int STATUS_FAILED = 4;
    public static final int STATUS_END = 3;
    public static final int STATUS_DEFAULT = 2;


    /**
     * 现在的加载状态
     */
    private int currentLoadingStatus = STATUS_DEFAULT;

    public int getCurrentLoadingStatus() {
        return currentLoadingStatus;
    }


    public abstract int getLayoutId();

    public void setCurrentLoadingStatus(int currentLoadingStatus) {
        this.currentLoadingStatus = currentLoadingStatus;
    }

    public void convert(final BaseWrappedViewHolder baseWrappedViewHolder) {
        switch (currentLoadingStatus) {
            case STATUS_DEFAULT:
                baseWrappedViewHolder.setVisible(getEndLayoutId(), false).setVisible(getFailedLayoutId(), false)
                        .setVisible(getEndLayoutId(), false);
                break;
            case STATUS_LOADING:
                baseWrappedViewHolder.setVisible(getLoadingLayoutId(), true)
                        .setVisible(getFailedLayoutId(), false).setVisible(getEndLayoutId(), false);
                break;
            case STATUS_FAILED:
                Toast.makeText(baseWrappedViewHolder.itemView.getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                baseWrappedViewHolder.setVisible(getFailedLayoutId(), true)
                        .setVisible(getEndLayoutId(), false)
                        .setVisible(getLoadingLayoutId(), false);
                resetStatus(baseWrappedViewHolder);
                break;
            case STATUS_END:
                Toast.makeText(baseWrappedViewHolder.getContext(), "加载结束", Toast.LENGTH_SHORT).show();
                baseWrappedViewHolder.setVisible(getEndLayoutId(), true)
                        .setVisible(getFailedLayoutId(), false)
                        .setVisible(getLoadingLayoutId(), false);
                resetStatus(baseWrappedViewHolder);
                break;
        }
    }

    private void resetStatus(final BaseWrappedViewHolder baseWrappedViewHolder) {
        LogUtil.log("重设状态");
//                baseWrappedViewHolder.itemView.setVisibility(View.GONE);
        setCurrentLoadingStatus(STATUS_DEFAULT);
        baseWrappedViewHolder.setVisible(getEndLayoutId(), false).setVisible(getFailedLayoutId(), false)
                .setVisible(getEndLayoutId(), false);
    }


    public abstract int getLoadingLayoutId();


    public abstract int getFailedLayoutId();


    public abstract int getEndLayoutId();

}
