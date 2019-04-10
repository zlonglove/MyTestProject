package com.ISHello.View;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.ishelloword.R;


/**
 * 移动IM开发项目组 — @author  于 2014-5-8
 * <p>
 * 自定义带可清除图标的EditText, 点击图标可清除EditText中的全部内容.
 * <p>
 */
public class CleanableEditText extends EditText {

    private Drawable mClearIcon;    // 删除按钮
    private boolean mHasFocus;        // 当前控件是否有焦点
    private boolean mDisplayClearFlag = true;        // 当前控件是否有焦点

    public CleanableEditText(Context context) {
        this(context, null);
    }

    public CleanableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public CleanableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mClearIcon = getCompoundDrawables()[2];    // left, top, right, bottom
        if (null == mClearIcon) {
            mClearIcon = getResources().getDrawable(R.drawable.presence_offline);
        }
        mClearIcon.setBounds(-10, 0, mClearIcon.getIntrinsicWidth() - 10, mClearIcon.getIntrinsicHeight());
        setClearIconVisible(false);
        addTextChangedListener(mTextWatcher);
        setOnFocusChangeListener(mFocusChangeListener);
    }


    //根据自己需要设置右侧删除按钮icon
    public void setRightDeleteICON(Drawable icon) {
        mClearIcon = icon;
        mClearIcon.setBounds(-10, 0, mClearIcon.getIntrinsicWidth() - 10, mClearIcon.getIntrinsicHeight());
        setClearIconVisible(false);
    }

    /**
     * 设置删除图标的可见性
     */
    private void setClearIconVisible(boolean visible) {
        if (!mDisplayClearFlag) {
            return;
        }

        Drawable right = visible ? mClearIcon : null;
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawables(drawables[0], drawables[1], right, drawables[3]);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // 根据当前控件是否获得焦点及输入框是否有文字设置清除图标是否可见
            if (mHasFocus) {
                setClearIconVisible(s.length() > 0);
                if (count > before) {
                    String text1 = s.toString();
                    if (!text1.contains(".")) {
                        setText(s + ".00");
                        setSelection(getText().length()-3);
                    }
                }else{
                    String text1 = s.toString();
                    if (".00".equals(text1)){
                        setText("");
                    }
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    // 当控件失去焦点时隐藏清除图标
    private OnFocusChangeListener mFocusChangeListener = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            mHasFocus = hasFocus;
            if (mHasFocus) {
                setClearIconVisible(getText().length() > 0);
            } else {
                setClearIconVisible(false);
            }
        }
    };

    public void onFocusChange(View v, boolean hasFocus) {
        mHasFocus = hasFocus;
        if (mHasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    /**
     * <p>使用按下位置模拟点击事件, 当触摸到清除图标时, 删除EditText中的全部内容.</p>
     *
     * <ul>
     * <li>event.getX(): 手指抬起时的坐标</li>
     * <li>getWidth(): 控件的宽度</li>
     * <li>getTotalPaddingRight(): 清除图标的左边缘到控件右边缘的距离</li>
     * <li>getPaddingRight(): 清除图标右边缘到控件右边缘的距离</li>
     * <li>getWidth() - getTotalPaddingRight(): 控件左边缘到清除图标左边缘的距离</li>
     * <li>getWidth() - getPaddingRight(): 控件左边缘到清除图标右边缘的距离</li>
     * <li>以上这两个距离之间就是清除图标, 具体位置关系可画图看清</li>
     * </ul>
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                int boundLeft = getWidth() - getTotalPaddingRight();
                int boundRight = getWidth() - getPaddingRight();
                boolean touchable = (event.getX() > boundLeft) && (event.getX() < boundRight);
                if (touchable) {
                    setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
