package zlonglove.cn.systemkeyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;


public class SystemKeyboardActivity extends AppCompatActivity {

    private EditText et_price;
    private EditText et_orginal_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_keyboard_main);
        et_price = (EditText) findViewById(R.id.et_hint_pig_meat_open_order_goods_price);
        et_orginal_price = (EditText) findViewById(R.id.et_hint_pig_meat_open_order_goods_number);
        final KeyboardUtil keyboardUtil = new KeyboardUtil(this);
        keyboardUtil.attachTo(et_price);
        et_price.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyboardUtil.attachTo(et_price);
                return false;
            }
        });
        et_orginal_price.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyboardUtil.attachTo(et_orginal_price);
                return false;
            }
        });
        keyboardUtil.setOnOkClick(new KeyboardUtil.OnOkClick() {
            @Override
            public void onOkClick() {
                keyboardUtil.hideKeyboard();
            }
        });
    }
}
