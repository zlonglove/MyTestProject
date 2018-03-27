package com.ISHello.View;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ISHello.utils.DipPixUtil;
import com.example.ishelloword.R;

import java.util.List;

/**
 * @author kfzx-zhanglong
 *         音序滑条
 */
public class LetterNavigateView extends View {

    OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    private static final int VIEW_BACKGROUND_COLOR = R.color.letter_navi_bg;
    private static final int COMMON_TEXT_COLOR = R.color.letter_navi_common_text;
    private static final int COMMON_PRESS_TEXT_COLOR = R.color.white;
    private static final int SELECTED_TEXT_COLOR = R.color.letter_navi_select_text;
    private static final int DEFAULT_FONT_SIZE_DIP = 12;

    Resources mResources;
    String[] mLetterArray;
    int choose = -1;
    Paint paint = new Paint();
    boolean showBkg = false;

    int mViewBgColor;
    int mCommonTextColor;
    int mCommonPressTextColor;
    int mSelectedTextColor;

    public LetterNavigateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mResources = context.getResources();
        initData();
    }

    public LetterNavigateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mResources = context.getResources();
        initData();
    }

    public LetterNavigateView(Context context) {
        super(context);
    }

    private void initData() {
        mViewBgColor = mResources.getColor(VIEW_BACKGROUND_COLOR);
        mCommonTextColor = mResources.getColor(COMMON_TEXT_COLOR);
        mCommonPressTextColor = mResources.getColor(COMMON_PRESS_TEXT_COLOR);
        mSelectedTextColor = mResources.getColor(SELECTED_TEXT_COLOR);
    }

    public void setIndexer(List<String> indexerList) {
        mLetterArray = new String[indexerList.size()];
        indexerList.toArray(mLetterArray);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBkg) {
            canvas.drawColor(mViewBgColor);
        }

        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / mLetterArray.length;
        for (int i = 0; i < mLetterArray.length; i++) {
            if (showBkg) {
                paint.setColor(mCommonPressTextColor);
            } else {
                paint.setColor(mCommonTextColor);
            }
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setTextSize(DipPixUtil.dip2px(getContext(),
                    DEFAULT_FONT_SIZE_DIP));
            paint.setAntiAlias(true);
            if (i == choose) {
                paint.setColor(mSelectedTextColor);
                paint.setFakeBoldText(true);
            }
            float xPos = width / 2 - paint.measureText(mLetterArray[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(mLetterArray[i], xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        if (listener == null)
            return true;
        final int c = (int) (y / getHeight() * mLetterArray.length);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBkg = true;
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < mLetterArray.length) {
                        listener.onTouchingLetterChanged(mLetterArray[c]);
                        choose = c;
                        invalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < mLetterArray.length) {
                        listener.onTouchingLetterChanged(mLetterArray[c]);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBkg = false;
                choose = -1;
                listener.onTouchingLetterUp();
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);

        void onTouchingLetterUp();
    }

}