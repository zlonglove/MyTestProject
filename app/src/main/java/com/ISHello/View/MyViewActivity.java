package com.ISHello.View;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ISHello.View.LetterNavigateView.OnTouchingLetterChangedListener;
import com.ISHello.utils.LetterNavigateSupport;
import com.example.ishelloword.R;

import java.util.List;

/**
 * @author kfzx-zhanglong
 */
public class MyViewActivity extends Activity {
    // 弹出框
    private PopupWindow mDialogPopupWindow;
    private View mDialogLayoutView;
    private TextView mDialogTextView;
    private LetterNavigateView letterNavigateView;
    private List<String> mLetterList;

    protected OnTouchingLetterChangedListener mLetterChangedListener = new OnTouchingLetterChangedListener() {

        @Override
        public void onTouchingLetterChanged(String s) {
            if (MyViewActivity.this != null
                    && !MyViewActivity.this.isFinishing()) {
                if (mDialogPopupWindow == null) {
                    mDialogPopupWindow = new PopupWindow(mDialogLayoutView, 200, 200, false);
                    // 显示在Activity的根视图中心
                    mDialogPopupWindow.showAtLocation(MyViewActivity.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                }
                mDialogTextView.setText(s);
            }
        }

        @Override
        public void onTouchingLetterUp() {
            if (mDialogPopupWindow != null) {
                mDialogPopupWindow.dismiss();
                mDialogPopupWindow = null;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.letter_nav_view_layout);
        findViews();
        init();
    }

    private void findViews() {
        letterNavigateView = (LetterNavigateView) this
                .findViewById(R.id.contactlist_letter_navi);
    }

    private void init() {
        mLetterList = LetterNavigateSupport.getLetters(getResources());
        letterNavigateView.setIndexer(mLetterList);
        letterNavigateView
                .setOnTouchingLetterChangedListener(mLetterChangedListener);
        // 初始化拼音提示对话框
        LayoutInflater inflater = LayoutInflater.from(this
                .getApplicationContext());
        mDialogLayoutView = inflater.inflate(R.layout.letter_bar_dialog, null);
        mDialogTextView = (TextView) mDialogLayoutView
                .findViewById(R.id.letter_bar_dialog_content);
    }
}
