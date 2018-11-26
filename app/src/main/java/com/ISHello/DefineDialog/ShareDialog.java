package com.ISHello.DefineDialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.ishelloword.R;


/**
 * Used 分享底部对话框【按常理来讲，应该extends BottomSheetDialog】
 * 但是，因为这个分享对话框有可能需要请求接口，所以最终还是继承DialogFragment
 */

public class ShareDialog extends DialogFragment {
    private static final String TAG = ShareDialog.class.getSimpleName();

    /**
     * View实例
     */
    private View myView;
    /**
     * 标记：用来代表是从哪个界面打开的这个对话框
     */
    private String mTag;

    /**
     * QQ分享
     */
    private LinearLayout shareQQ;
    /**
     * QQ空间分享
     */
    private LinearLayout shareQZone;
    /**
     * 微信分享
     */
    private LinearLayout shareWX;
    /**
     * 朋友圈分享
     */
    private LinearLayout shareWXCircle;
    /**
     * 新浪微博分享
     */
    private LinearLayout shareSina;

    public static ShareDialog getInstance() {
        ShareDialog shareDialog = new ShareDialog();

        return shareDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));//设置背景为透明，并且没有标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏，否则点击正上方一定高度的范围内不会消失
        myView = inflater.inflate(R.layout.dialog_share, container, false);
        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        //初始化控件以及设置
        initView();
        //初始化数据
        initDatas();
        //初始化事件
        initEvents();
    }

    /**
     * 设置宽度和高度值，以及打开的动画效果
     */
    @Override
    public void onStart() {
        super.onStart();
        //设置对话框的宽高，必须在onStart中
        DisplayMetrics metrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Window window = this.getDialog().getWindow();
        window.setLayout(metrics.widthPixels, this.getDialog().getWindow().getAttributes().height);
        window.setGravity(Gravity.BOTTOM);//设置在底部
        //打开的动画效果
        //设置dialog的 进出 动画
        getDialog().getWindow().setWindowAnimations(R.style.sharebottomsheetdialog_animation);
    }

    /**
     * 实例化控件
     */
    private void initView() {
        shareQQ = (LinearLayout) myView.findViewById(R.id.share_qq);
        shareQZone = (LinearLayout) myView.findViewById(R.id.share_qzone);
        shareWX = (LinearLayout) myView.findViewById(R.id.share_weixin);
        shareWXCircle = (LinearLayout) myView.findViewById(R.id.share_wxcircle);
        shareSina = (LinearLayout) myView.findViewById(R.id.share_sina);
    }

    /**
     * 初始化数据：tag标记、标题
     */
    private void initDatas() {
        mTag = this.getTag();
        Log.e(TAG, "mTag=" + mTag);
    }

    /**
     * 初始化监听事件
     */
    private void initEvents() {

        //QQ分享
        shareQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnShareClickLitener != null) {
                    mOnShareClickLitener.onShareToQQ();
                }
            }
        });

        //QQ空间分享
        shareQZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnShareClickLitener != null) {
                    mOnShareClickLitener.onShareToQZone();
                }
            }
        });

        //微信分享
        shareWX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnShareClickLitener != null) {
                    mOnShareClickLitener.onShareToWX();
                }
            }
        });

        //朋友圈分享
        shareWXCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnShareClickLitener != null) {
                    mOnShareClickLitener.onShareToWXCircle();
                }
            }
        });

        //新浪微博分享
        shareSina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnShareClickLitener != null) {
                    mOnShareClickLitener.onShareToSina();
                }
            }
        });
    }

    /*=====================添加OnShareClickLitener回调================================*/
    public interface OnShareClickLitener {
        void onShareToQQ();

        void onShareToQZone();

        void onShareToWX();

        void onShareToWXCircle();

        void onShareToSina();
    }

    private OnShareClickLitener mOnShareClickLitener;

    public void setOnShareClickLitener(OnShareClickLitener mOnShareClickLitener) {
        this.mOnShareClickLitener = mOnShareClickLitener;
    }
}
