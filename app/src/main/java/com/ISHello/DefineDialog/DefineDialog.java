package com.ISHello.DefineDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ishelloword.R;

public class DefineDialog extends Dialog {

    public static final int NO_BUTTONS = 0;
    private static final String TAG = "MessageDialog";
    private Button[] mButtons;
    private ViewGroup mButtonsLayout;
    private TextView mMessage;
    private EditText mPassword;
    private PriorityListener mPriorityListener;
    private TextView mTitle;

    public DefineDialog(Context context) {
        super(context, R.style.PopDialog);
        getWindow().setSoftInputMode(3);
    }

    public DefineDialog(Context paramContext, int paramInt) {
        super(paramContext, R.style.PopDialog);
    }

    public DefineDialog(Context paramContext, boolean paramBoolean, DialogInterface.OnCancelListener paramOnCancelListener) {
        super(paramContext, R.style.PopDialog);
        setCancelable(paramBoolean);
        setOnCancelListener(paramOnCancelListener);
    }

    public void addTextChangedWatcher(TextWatcher paramTextWatcher) {
        if ((this.mPassword == null) || (paramTextWatcher == null))
            return;
        this.mPassword.addTextChangedListener(paramTextWatcher);
    }

    public Button getButton(int paramInt) {
        Button localButton = null;
        if (this.mButtons == null) {
            return null;
        }
        switch (paramInt) {
            case -1:
                if (this.mButtons[0] == null) {
                    return null;
                }
                localButton = this.mButtons[0];
                break;

            case -2:
                if (this.mButtons[1] == null) {
                    return null;
                }
                localButton = this.mButtons[1];
                break;

            default:
                break;
        }
        return localButton;

    }

    public String getInputValue() {
        if (this.mPassword != null)
            ;
        String str = this.mPassword.getText().toString();
        return str;
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {

        boolean bool = true;
        switch (paramInt) {
            case KeyEvent.KEYCODE_BACK: {
                Log.d(TAG, "--->User Click Back");
                if (mPriorityListener != null) {
                    this.mPriorityListener.dismissDialog(this);
                }
                break;
            }
            default:
                break;
        }
        bool = super.onKeyDown(paramInt, paramKeyEvent);
        return bool;

    }

    public void setButton(int paramInt, CharSequence paramCharSequence, final DialogInterface.OnClickListener paramOnClickListener) {
        if (this.mButtons == null) {
            return;
        }
        switch (paramInt) {
            case -1:
                if (this.mButtons[0] == null) {
                    return;
                }
                this.mButtons[0].setText(paramCharSequence);
                this.mButtons[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        paramOnClickListener.onClick(DefineDialog.this, -1);
                    }
                });

            case -2:
                if (this.mButtons[1] == null) {
                    return;
                }
                this.mButtons[1].setText(paramCharSequence);
                this.mButtons[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        paramOnClickListener.onClick(DefineDialog.this, -2);
                    }
                });
                break;

            default:
                break;
        }
    }

    public void setButton(CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener) {
        if (this.mButtons[0] == null) {
            return;
        }
        setButton(-1, paramCharSequence, paramOnClickListener);
    }

    public void setButton2(CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener) {
        if (this.mButtons[1] == null) {
            return;
        }
        setButton(-2, paramCharSequence, paramOnClickListener);
    }

    public void setButtonVisibility(int paramInt1, int paramInt2) {
        if (paramInt1 > this.mButtons.length) {
            return;
        }
        if (this.mButtons[paramInt1] == null) {
            return;
        }
        this.mButtons[paramInt1].setVisibility(paramInt2);
    }

    public void setContentView(View paramView) {
        super.setContentView(paramView);
        setView(paramView);
    }

    public void setDialogType(int paramInt) {
        switch (paramInt) {
            case 0:
                if (this.mButtonsLayout == null) {
                    return;
                }
                this.mButtonsLayout.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    public void setEditReqFocus(boolean paramBoolean) {
        if (!paramBoolean)
            return;
        this.mPassword.requestFocus();
    }

    public void setInputValue(CharSequence paramCharSequence) {
        if (this.mPassword == null)
            return;
        this.mPassword.setText(paramCharSequence);
    }

    public void setMessage(CharSequence paramCharSequence) {
        if (this.mMessage == null)
            return;
        this.mMessage.setText(paramCharSequence);
    }

    public void setMessageFocused() {
        if (this.mMessage == null)
            return;
        this.mMessage.setFocusable(true);
        this.mMessage.setFocusableInTouchMode(true);
        this.mMessage.requestFocus();
        this.mMessage.requestFocusFromTouch();
    }

    public void setMessageGravity(int paramInt) {
        if (this.mMessage == null)
            return;
        this.mMessage.setGravity(paramInt);
    }

    public void setPriorityListener(PriorityListener paramPriorityListener) {
        this.mPriorityListener = paramPriorityListener;
    }

    public void setTitle(CharSequence paramCharSequence) {

        if (this.mTitle == null)
            return;
        this.mTitle.setText(paramCharSequence);

    }

    protected void setView(View paramView) {
        if (paramView == null) {
            return;
        }

        this.mTitle = ((TextView) paramView.findViewById(R.id.title));
        if (this.mTitle == null)
            Log.i("MessageDialog", "setView mTitle is null");

        this.mMessage = ((TextView) paramView.findViewById(R.id.Message));
        if (this.mMessage == null)
            Log.i("MessageDialog", "setView mMessage is null");
        this.mButtonsLayout = ((ViewGroup) findViewById(R.id.exit_buttons_layout));
        if (this.mButtonsLayout == null)
            Log.i("MessageDialog", "setView mButtonsLayout is null");
        this.mButtons = new Button[2];
        this.mButtons[0] = ((Button) paramView.findViewById(R.id.btn_ok));
        if (this.mButtons[0] == null)
            Log.i("MessageDialog", "setView mButtons BUTTON_POSITIVE is null");
        this.mButtons[1] = ((Button) paramView.findViewById(R.id.btn_cancle));
        if (this.mButtons[1] == null)
            Log.i("MessageDialog", "setView mButtons BUTTON_NEGATIVE is null");
        Window localWindow = getWindow();
        localWindow.setSoftInputMode(3);
    }

    public   interface PriorityListener {
          void dismissDialog(Dialog dialog);
    }

}
