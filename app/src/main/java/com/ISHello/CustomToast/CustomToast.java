package com.ISHello.CustomToast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ishelloword.R;

public class CustomToast {

    public static void makeText(Context context, CharSequence text, int duration) {
        Toast toast = new Toast(context);
        LayoutInflater factory = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = factory.inflate(R.layout.custom_toast, null);
        TextView textView = (TextView) layout.findViewById(R.id.txt_toast);
        //设置TextView的text内容
        textView.setText(text);

        toast.setDuration(duration);
//    	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setView(layout);
        toast.show();
    }

    public static void makeText(Context context, int resId, int duration) {
        Toast toast = new Toast(context);
        LayoutInflater factory = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = factory.inflate(R.layout.custom_toast, null);

        TextView textView = (TextView) layout.findViewById(R.id.txt_toast);
        //设置TextView的text内容
        textView.setText(resId);
        toast.setDuration(duration);
//    	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setView(layout);
        toast.show();
    }

    /**
     * 获取自定义样式的Toast,要手动调用show()方法显示
     *
     * @param context
     * @param text
     * @param duration
     * @return
     */
    public static Toast getToast(Context context, CharSequence text, int duration) {
        Toast toast = new Toast(context);
        LayoutInflater factory = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = factory.inflate(R.layout.custom_toast, null);
        TextView textView = (TextView) layout.findViewById(R.id.txt_toast);
        //设置TextView的text内容
        textView.setText(text);

        toast.setDuration(duration);
//    	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setView(layout);
        return toast;
    }
}
