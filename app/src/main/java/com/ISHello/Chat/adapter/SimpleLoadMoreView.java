package com.ISHello.Chat.adapter;


import com.example.ishelloword.R;

public class SimpleLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.base_load_view_layout;
    }

    @Override
    public int getLoadingLayoutId() {
        return R.id.ll_base_load_view_loading;
    }

    @Override
    public int getFailedLayoutId() {
        return R.id.fl_base_load_view_failed;
    }

    @Override
    public int getEndLayoutId() {
        return R.id.fl_base_load_view_end;
    }

}
