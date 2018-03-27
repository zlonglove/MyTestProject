package com.ISHello.TabMenu;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class TabMenu extends PopupWindow {
    private LinearLayout mLayout;
    private ImageView mImageView;
    private TextView mTextView;

    /**
     * @param context         上下文
     * @param onClickListener 单击事件
     * @param resID           图片资源
     * @param text            显示的文字
     * @param fontSize        显示的文字大小
     * @param fontColor       文字的颜色
     * @param colorBgTabMenu  背景颜色
     * @param aniTabMenu      消失的动画
     * @return
     */
    public TabMenu(Context context, OnClickListener onClickListener, int resID, String text, int fontSize,
                   int fontColor, int colorBgTabMenu, int aniTabMenu) {
        super(context);

        mLayout = new LinearLayout(context);
        mLayout.setOrientation(LinearLayout.VERTICAL);
        mLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        mLayout.setPadding(10, 10, 10, 10);

        mTextView = new TextView(context);
        mTextView.setTextSize((context.getResources().getDimensionPixelSize(fontSize)));
        mTextView.setTextColor((context.getResources().getColor(fontColor)));
        mTextView.setText(text);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setPadding(5, 5, 5, 5);

        mImageView = new ImageView(context);
        mImageView.setBackgroundResource(resID);

        mLayout.addView(mImageView, new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)));
        mLayout.addView(mTextView);
        mLayout.setOnClickListener(onClickListener);

        this.setContentView(mLayout);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(colorBgTabMenu)));
        this.setAnimationStyle(aniTabMenu);
        this.setFocusable(true);
        mLayout.setFocusableInTouchMode(true);
        mLayout.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (isShowing())) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });

    }

}
