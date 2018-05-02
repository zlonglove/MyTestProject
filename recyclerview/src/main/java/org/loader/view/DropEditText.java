package org.loader.view;

import android.R.color;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import zlonglove.cn.recyclerview.R;

public class DropEditText extends FrameLayout implements View.OnClickListener, OnItemClickListener {
    private EditText mEditText;  // 输入框
    private ImageView mDropImage; // 右边的图片按钮
    private PopupWindow mPopup; // 点击图片弹出popupwindow
    private WrapListView mPopView; // popupwindow的布局

    private int mDrawableLeft;
    private int mDropMode; // flow_parent or wrap_content
    private String mHit;

    public DropEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.edit_layout, this);
        mPopView = (WrapListView) LayoutInflater.from(context).inflate(R.layout.pop_view, null);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DropEditText, defStyle, 0);
        mDrawableLeft = ta.getResourceId(R.styleable.DropEditText_drawableRight, R.drawable.drop);
        mDropMode = ta.getInt(R.styleable.DropEditText_dropMode, 0);
        mHit = ta.getString(R.styleable.DropEditText_hint);
        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mEditText = (EditText) findViewById(R.id.dropview_edit);
        mDropImage = (ImageView) findViewById(R.id.dropview_image);

        mEditText.setSelectAllOnFocus(true);
        mDropImage.setImageResource(mDrawableLeft);

        if (!TextUtils.isEmpty(mHit)) {
            mEditText.setHint(mHit);
        }

        mDropImage.setOnClickListener(this);
        mPopView.setOnItemClickListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 如果布局发生改
        // 并且dropMode是flower_parent
        // 则设置ListView的宽度
        if (changed && 0 == mDropMode) {
            mPopView.setListWidth(getMeasuredWidth());
        }
    }

    /**
     * 设置Adapter
     *
     * @param adapter ListView的Adapter
     */
    public void setAdapter(BaseAdapter adapter) {
        mPopView.setAdapter(adapter);

        mPopup = new PopupWindow(mPopView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopup.setBackgroundDrawable(new ColorDrawable(getResources().getColor(color.transparent)));
        mPopup.setFocusable(true); // 让popwin获取焦点
    }

    /**
     * 获取输入框内的内容
     *
     * @return String content
     */
    public String getText() {
        return mEditText.getText().toString();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dropview_image) {
            if (mPopup.isShowing()) {
                mPopup.dismiss();
                return;
            }

            //mPopup.showAtLocation(this, Gravity.CENTER, 0, 0);
            showUp(this, mPopup);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        mEditText.setText(mPopView.getAdapter().getItem(position).toString());
        mPopup.dismiss();
    }

    /**
     * 设置显示在v下方
     *
     * @param v
     */
    public void showUp(View v, PopupWindow mPopup) {

        v.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = v.getMeasuredHeight();
        //int popupWidth = v.getMeasuredWidth();

        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
        mPopup.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1] + popupHeight);
    }
}
