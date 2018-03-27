package im.icbc.cn.keyboard.safePay;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import im.icbc.cn.keyboard.R;


/**
 *
 */
public class PayEditText extends LinearLayout {
    private Context context;
    private TextView tvFirst, tvSecond, tvThird, tvForth, tvFifth, tvSixth;
    private StringBuilder mPassword;
    private OnInputFinishedListener onInputFinishedListener;

    public PayEditText(Context context){
        this(context, null);
    }

    public PayEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PayEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initPayEditText();
        initEvent();
    }

    private void initEvent() {
        tvSixth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //六个密码都输入完成时回调
                if (onInputFinishedListener != null && mPassword != null && mPassword.toString().length() == 6 && !TextUtils.isEmpty(s.toString())) {
                        onInputFinishedListener.onInputFinished(mPassword.toString());
                }
            }
        });
    }

    /**
     * 初始化PayEditText
     */
    private void initPayEditText() {
        View view = View.inflate(context, R.layout.view_pay_edit, null);
        tvFirst = (TextView) view.findViewById(R.id.tv_pay1);
        tvSecond = (TextView) view.findViewById(R.id.tv_pay2);
        tvThird = (TextView) view.findViewById(R.id.tv_pay3);
        tvForth = (TextView) view.findViewById(R.id.tv_pay4);
        tvFifth = (TextView) view.findViewById(R.id.tv_pay5);
        tvSixth = (TextView) view.findViewById(R.id.tv_pay6);

        mPassword = new StringBuilder();
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * 输入密码
     * @param value
     */
    public void add(String value) {
        if (mPassword != null && mPassword.length() < 6) {
            mPassword.append(value);
            if (mPassword.length() == 1) {
                tvFirst.setText(value);
            } else if (mPassword.length() == 2) {
                tvSecond.setText(value);
            }else if (mPassword.length() == 3) {
                tvThird.setText(value);
            }else if (mPassword.length() == 4) {
                tvForth.setText(value);
            }else if (mPassword.length() == 5) {
                tvFifth.setText(value);
            }else if (mPassword.length() == 6) {
                tvSixth.setText(value);
            }
        }
    }

    /**
     * 删除密码
     */
    public void remove() {
        if (mPassword != null && mPassword.length() > 0) {
            if (mPassword.length() == 1) {
                tvFirst.setText("");
            } else if (mPassword.length() == 2) {
                tvSecond.setText("");
            }else if (mPassword.length() == 3) {
                tvThird.setText("");
            }else if (mPassword.length() == 4) {
                tvForth.setText("");
            }else if (mPassword.length() == 5) {
                tvFifth.setText("");
            }else if (mPassword.length() == 6) {
                tvSixth.setText("");
            }
            mPassword.deleteCharAt(mPassword.length() - 1);
        }
    }

    /**
     * 返回输入的内容
     * @return 返回输入内容
     */
    public String getText() {
        return (mPassword == null) ? null : mPassword.toString();
    }

    /**
     * 当密码输入完成时的回调接口
     */
    public interface OnInputFinishedListener {
        void onInputFinished(String password);
    }

    /**
     * 对外开放的方法
     * @param onInputFinishedListener
     */
    public void setOnInputFinishedListener(OnInputFinishedListener onInputFinishedListener) {
        this.onInputFinishedListener = onInputFinishedListener;
    }
}
