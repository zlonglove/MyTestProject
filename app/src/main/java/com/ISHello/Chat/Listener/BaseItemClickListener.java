package com.ISHello.Chat.Listener;

import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ISHello.Chat.adapter.BaseWrappedAdapter;
import com.ISHello.Chat.adapter.BaseWrappedViewHolder;

import java.util.Set;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/3/25      13:45
 * QQ:             1981367757
 */

public abstract class BaseItemClickListener implements RecyclerView.OnItemTouchListener {
    private RecyclerView mRecyclerView;
    private BaseWrappedAdapter mBaseWrappedAdapter;
    private GestureDetectorCompat mGestureDetectorCompat;
    private View mPressedView;
    private boolean isPressing;

    //        这里初始化手势监听
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        com.ISHello.utils.LogUtil.log("onInterceptTouchEvent_RecyclerView");
        if (mRecyclerView == null) {
            mRecyclerView = rv;
            mBaseWrappedAdapter = (BaseWrappedAdapter) mRecyclerView.getAdapter();
            mGestureDetectorCompat = new GestureDetectorCompat(mRecyclerView.getContext(),
                    new CustomOnGestureListener(mRecyclerView));
        }
//                这里恢复长按下效果
        if (!mGestureDetectorCompat.onTouchEvent(e) && e.getAction() == MotionEvent.ACTION_UP
                && isPressing) {
            if (mPressedView != null) {
//                                这里要排除头部和底部view
                RecyclerView.ViewHolder viewHolder = mRecyclerView.getChildViewHolder(mPressedView);
                if (viewHolder == null || !isHeaderViewOrFooterView(viewHolder.getItemViewType())) {
                    mPressedView.setPressed(false);
                    if (mPressedView instanceof ViewGroup) {
                        ViewGroup viewGroup = (ViewGroup) mPressedView;
                        for (int i = 0; i < viewGroup.getChildCount(); i++) {
                            if (!viewGroup.getChildAt(i).isPressed()) {
                                viewGroup.getChildAt(i).setPressed(false);
                            }
                        }
                    }
                }
            }
//                        重置
            isPressing = false;
        }
//                不中断
        return false;
    }

    private boolean isHeaderViewOrFooterView(int itemViewType) {
        return itemViewType == BaseWrappedAdapter.HEADER_VIEW || itemViewType == BaseWrappedAdapter.FOOTER_VIEW;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class CustomOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        private RecyclerView mRecyclerView;

        CustomOnGestureListener(RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            if (mPressedView == null) {
                mPressedView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            }
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            if (mPressedView != null && !isPressing) {
                isPressing = true;
            }
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (mPressedView != null) {
                if (mRecyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
//                                        正在滚动中，不处理
                    return false;
                }
                com.ISHello.utils.LogUtil.log("onSingleTapUp1");
                BaseWrappedViewHolder baseWrappedViewHolder = (BaseWrappedViewHolder) mRecyclerView.getChildViewHolder(mPressedView);
//                                头部和底部view不处理
                if (baseWrappedViewHolder != null) {
                    com.ISHello.utils.LogUtil.log("baseWrappedViewHolder");
                    if (isHeaderViewOrFooterView(baseWrappedViewHolder.getItemViewType())) {
                        com.ISHello.utils.LogUtil.log("isHeaderViewOrFooterView");
                        return false;
                    }
//                                        获取设置点击事件的item_view的id;
                    Set<Integer> ids = baseWrappedViewHolder.getClickableItemIds();
                    Set<Integer> nestIds = baseWrappedViewHolder.getNestIds();
                    com.ISHello.utils.LogUtil.log("nestIds");
                    if (ids != null && ids.size() > 0) {
                        com.ISHello.utils.LogUtil.log("ids.size()");
                        for (Integer id :
                                ids) {
                            final View childView = mPressedView.findViewById(id);
                            if (childView != null && childView.getVisibility() == View.VISIBLE) {

//                                                                判断点击位置是否在该view上和该view是否可点击
                                if (isOnRange(e, childView) && childView.isEnabled()) {
//                                                                        这里要排除掉嵌套的recyclerView的点击事件
                                    if (nestIds != null && nestIds.contains(id)) {
                                        com.ISHello.utils.LogUtil.log("nestIds");
                                        return false;
                                    }
//                                                                        设置item的热点
                                    setChildHotSpot(childView, e);
                                    childView.setPressed(true);
//                                                                        点击接口
                                    com.ISHello.utils.LogUtil.log("触发item_child点击");
                                    onItemChildClick(baseWrappedViewHolder, id, childView, baseWrappedViewHolder.getLayoutPosition() - mBaseWrappedAdapter.getHeaderViewCount());
//                                                                        恢复效果,提交，防止堵塞
                                    resetView(childView);
                                    return true;
                                } else {
                                    com.ISHello.utils.LogUtil.log("isEnabled" + childView.isEnabled());
                                }
                            }
                        }
                    }
                    //                                        如果执行到这里，证明没有设置点击事件,所以设置itemView的点击事件
                    setChildHotSpot(mPressedView, e);
                    mPressedView.setPressed(true);
                    onItemClick(baseWrappedViewHolder, baseWrappedViewHolder.itemView.getId(), mPressedView, baseWrappedViewHolder.getLayoutPosition() - mBaseWrappedAdapter.getHeaderViewCount());
                    resetView(mPressedView);
                }

            }
            return false;
        }

        private void resetView(final View childView) {
            childView.post(new Runnable() {
                @Override
                public void run() {
                    if (childView != null) {
                        childView.setPressed(false);
                    }
                }
            });
            isPressing = false;
//                        置空，防止内存泄漏
            mPressedView = null;
        }

        private void setChildHotSpot(View childView, MotionEvent e) {
//                        5.0以上的,设置按下蔓延效果蔓延
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                if (childView.getBackground() != null) {
                    childView.getBackground().setHotspot(e.getRawX(), e.getY() - childView.getY());
                }
            }
        }

        private boolean isOnRange(MotionEvent e, View childView) {
//                        com.ISHello.utils.LogUtil.log("更改" + mPressedView.getTop());
//                        com.ISHello.utils.LogUtil.log("1点的位置x:" + e.getRawX() + "    y:" + e.getRawY());
//                        int newLeft = childView.getLeft() + mPressedView.getLeft();
//                        int newRight = childView.getRight() + mPressedView.getLeft();
//                        int newTop = childView.getTop() + mPressedView.getTop();
//                        int newBottom = childView.getBottom() + mPressedView.getTop();
//                        com.ISHello.utils.LogUtil.log("newLeft:" + newLeft + "    newRight:" + newRight);
//                        com.ISHello.utils.LogUtil.log("newTop:" + newTop + "     newBottom" + newBottom);
//                        if ((newLeft <= e.getRawX() && e.getRawX() <= newRight) &&
//                                (newTop <= e.getRawY() && e.getRawY() <= newBottom)) {
//                                return true;
//                        } else {
//                                return false;
//                        }


            int[] location = new int[2];
            childView.getLocationOnScreen(location);
//                        com.ISHello.utils.LogUtil.log("scrollX" + childView.getScrollX() + "     scrollY" + childView.getScrollY());
            com.ISHello.utils.LogUtil.log("1view点的位置x:" + location[0] + "    y:" + location[1]);
            com.ISHello.utils.LogUtil.log("1点的位置x:" + e.getRawX() + "    y:" + e.getRawY());
            com.ISHello.utils.LogUtil.log("1view的大小w:" + childView.getWidth() + "    h:" + childView.getHeight());

            if ((location[0] < e.getRawX() && e.getRawX() < location[0] + childView.getWidth()) &&
                    (location[1] < e.getRawY() && e.getRawY() < location[1] + childView.getHeight())) {
                com.ISHello.utils.LogUtil.log("true");
                return true;
            }
            com.ISHello.utils.LogUtil.log("false");
            return false;
        }


        @Override
        public void onLongPress(MotionEvent e) {
            if (mPressedView != null) {
                if (mRecyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    return;
                }
                mPressedView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                BaseWrappedViewHolder baseWrappedViewHolder = (BaseWrappedViewHolder) mRecyclerView.getChildViewHolder(mPressedView);
                if (baseWrappedViewHolder != null) {
                    boolean isLongClickConsume = false;
                    if (isHeaderViewOrFooterView(baseWrappedViewHolder.getItemViewType())) {
                        isLongClickConsume = true;
                    }
                    Set<Integer> ids = baseWrappedViewHolder.getLongClickableItemIds();
                    Set<Integer> nestIds = baseWrappedViewHolder.getNestIds();
                    if (ids != null && ids.size() > 0) {
                        for (Integer id :
                                ids) {
                            final View childView = mPressedView.findViewById(id);
                            if (childView != null) {
//                                                                判断点击位置是否在该view上和该view是否可点击
                                if (isOnRange(e, childView) && childView.isEnabled()) {
//                                                                        这里要排除掉嵌套的recyclerView的点击事件
                                    if (nestIds != null && nestIds.contains(id)) {
                                        break;
                                    }
//                                                                        设置item的热点
                                    setChildHotSpot(childView, e);
                                    childView.setPressed(true);
//                                                                        点击接口
                                    onItemChildLongClick(baseWrappedViewHolder, id, childView, baseWrappedViewHolder.getLayoutPosition() - mBaseWrappedAdapter.getHeaderViewCount());
//                                                                        恢复效果,提交，防止堵塞
                                    isLongClickConsume = true;
                                    isPressing = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (!isLongClickConsume) {
                        setChildHotSpot(mPressedView, e);
                        mPressedView.setPressed(true);
                        onItemLongClick(baseWrappedViewHolder, mPressedView, baseWrappedViewHolder.getLayoutPosition() - mBaseWrappedAdapter.getHeaderViewCount());
                        isPressing = true;
                    }
                }
            }
        }
    }

    protected abstract void onItemLongClick(BaseWrappedViewHolder baseWrappedViewHolder, View view, int position);


    protected abstract void onItemChildLongClick(BaseWrappedViewHolder baseWrappedViewHolder, int id, View view, int position);


    protected abstract void onItemChildClick(BaseWrappedViewHolder baseWrappedViewHolder, int id, View view, int position);


    protected abstract void onItemClick(BaseWrappedViewHolder baseWrappedViewHolder, int id, View view, int position);


}
