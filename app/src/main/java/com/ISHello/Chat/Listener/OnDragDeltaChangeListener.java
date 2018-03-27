package com.ISHello.Chat.Listener;

import android.view.View;

public interface OnDragDeltaChangeListener {
        void onDrag(View view, float delta);

        void onCloseMenu();

        void onOpenMenu();
}
