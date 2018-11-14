package com.ISHello.IndexablerecyclerView;

import android.content.Context;
import android.widget.Toast;

/**
 * @author
 */
public class ToastUtil {
    private static Toast toast;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static Toast showShort(Context context, CharSequence message) {
        if (null == toast) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();

        return toast;
    }
}
