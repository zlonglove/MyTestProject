package com.ISHello.Menu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.ishelloword.R;

public class ISMenu extends FrameLayout {
    final int MENU_HIGHT = 130;
    private final String TAG = "OptionMenu";
    private int[] MENU_TEXT_RES_ID;
    private int MENU_WIDTH = 150;
    View mCurrentFocus;
    private MenuClickListener mMenuItemListener = new MenuClickListener();
    public LinearLayout mMenuLinerLayout;
    public VcMenuItem menuItem;
    private MenuPreClickListener menuPreClickListener;
    MenuPreFocusChangedkListener menuPreFocusChangedkListener;
    private View.OnClickListener optionMenuItemListener;

    public ISMenu(Context paramContext) {
        super(paramContext);
    }

    public ISMenu(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.mMenuLinerLayout = new LinearLayout(paramContext);
        this.mMenuLinerLayout.setBackgroundResource(R.drawable.menu_background);
        this.mMenuLinerLayout.setOrientation(LinearLayout.HORIZONTAL);
    }

    private void LOGD(String paramString) {
        Log.i(TAG, paramString);
    }

    public void addMenuItems(int paramInt, int[] paramArrayOfInt) {
        LOGD("--->addMenuItems()  mScreenWidth : " + paramInt);
        this.MENU_TEXT_RES_ID = paramArrayOfInt;
        int i = this.MENU_TEXT_RES_ID.length;
        this.MENU_WIDTH = (paramInt / i);
        LOGD("--->addMenuItems()  MENU_WIDTH : " + this.MENU_WIDTH);
        for (int j = 0; j < i; j++) {
            this.menuItem = new VcMenuItem(getContext(), this.MENU_TEXT_RES_ID[j]);
            this.menuItem.setOnClickListener(this.mMenuItemListener);
            this.menuItem.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent) {
                    LOGD("--->menuItem - onKey - event = " + paramInt + "  Action==" + paramKeyEvent.getAction());
                    if (paramKeyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        switch (paramInt) {
                            case KeyEvent.KEYCODE_MENU: {
                                ISMenu.this.menuPreClickListener.onPreClick();
                                break;
                            }
                            case KeyEvent.KEYCODE_BACK: {
                                ISMenu.this.menuPreClickListener.onPreClick();
                                break;
                            }
                            default:
                                break;
                        }
                    }
                    return false;
                }

            });
            this.menuItem.setId(j + 5);
            if (j == 0) {
                this.mCurrentFocus = this.menuItem;
                this.menuItem.setNextFocusLeftId(-1 + (i + 5));
            }
            if (j == i - 1) {
                this.menuItem.setNextFocusRightId(5);
            }
            LinearLayout.LayoutParams localLayoutParams2 = new LinearLayout.LayoutParams(this.MENU_WIDTH, MENU_HIGHT);
            this.mMenuLinerLayout.addView(this.menuItem, j, localLayoutParams2);
        }
        @SuppressWarnings("deprecation")
        FrameLayout.LayoutParams localLayoutParams1 = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        localLayoutParams1.gravity = Gravity.CENTER;
        addView(this.mMenuLinerLayout, localLayoutParams1);
        // this.mMenuLinerLayout.requestLayout();
        this.mMenuLinerLayout.requestFocus();
    }


    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        LOGD("--->onKeyDown==" + paramInt);
        boolean bool = true;
        switch (paramInt) {
            case KeyEvent.KEYCODE_BACK:
                ISMenu.this.menuPreClickListener.onPreClick();
                break;
            case KeyEvent.KEYCODE_MENU:
                ISMenu.this.menuPreClickListener.onPreClick();
                break;
            default:
                bool = super.onKeyDown(paramInt, paramKeyEvent);
        }
        return bool;
    }


    protected void onVisibilityChanged(View paramView, int paramInt) {
        LOGD("--->onVisibilityChanged === mCurrentFocus = " + this.mCurrentFocus);
        super.onVisibilityChanged(paramView, paramInt);
        /*if ((paramInt == 0) && (paramView == this))
	    {
	      if (this.mCurrentFocus == null)
	      {
	    	  return;
	      }
	      if (this.mCurrentFocus.getId() == -1 + (5 + this.MENU_TEXT_RES_ID.length))
	      {
	    	  this.mCurrentFocus.requestFocusFromTouch();
	      }
	      else
	      {
	    	  findViewById(5).requestFocus();
	    	  findViewById(5).requestFocusFromTouch();
	      }
	    }*/
    }

    public void setMenuClickListener(View.OnClickListener paramOnClickListener) {
        this.optionMenuItemListener = paramOnClickListener;
    }

    public void setMenuItem(int paramInt1, int paramInt2) {
        Button localButton = (Button) (Button) this.mMenuLinerLayout.getChildAt(paramInt1);
        localButton.setText(paramInt2);
        localButton.setTag(Integer.valueOf(paramInt2));
        localButton.setGravity(Gravity.CENTER);
    }

    public void setMenuPreClickListener(MenuPreClickListener paramMenuPreClickListener) {
        this.menuPreClickListener = paramMenuPreClickListener;
    }

    public void setMenuPreFocusChangedkListener(MenuPreFocusChangedkListener paramMenuPreFocusChangedkListener) {
        this.menuPreFocusChangedkListener = paramMenuPreFocusChangedkListener;
    }


    private class MenuClickListener implements View.OnClickListener {
        private MenuClickListener() {

        }

        public void onClick(View paramView) {
            LOGD("--->onClick");
            ISMenu.this.optionMenuItemListener.onClick(paramView);
        }
    }

    public static abstract interface MenuPreClickListener {
        public abstract void onPreClick();
    }

    public static abstract interface MenuPreFocusChangedkListener {
        public abstract void onPreFocusChanged(View paramView);
    }

    class VcMenuItem extends Button {
        public VcMenuItem(Context context, int stringID) {
            super(context);
            setText(stringID);
            setTextSize(28.0F);
            //setTextColor(getResources().getColorStateList(R.drawable.menu_text_color));
            setFocusable(true);
            setFocusableInTouchMode(true);
            setTag(stringID);
            setGravity(Gravity.CENTER);
            switch (stringID) {

                case R.string.user_account: {
                    setBackgroundResource(R.drawable.menu_left_selector);
                    break;
                }

                case R.string.system_settings: {
                    setBackgroundResource(R.drawable.menu_middle_selector);
                    break;
                }

                case R.string.about_software: {
                    setBackgroundResource(R.drawable.menu_right_selector);
                    break;
                }

                default:
                    break;
            }
        }
    }
}
