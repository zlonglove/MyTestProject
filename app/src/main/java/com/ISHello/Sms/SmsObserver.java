package com.ISHello.Sms;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsObserver extends ContentObserver {
    public static final String SMS_URI_INBOX = "content://sms/inbox";
    private Activity activity = null;
    private String smsContent = "";
    private SmsListener listener;

    public SmsObserver(Activity activity, Handler handler, SmsListener listener) {
        super(handler);
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Cursor cursor = null;
        // 读取收件箱中含有某关键词的短信
        ContentResolver contentResolver = activity.getContentResolver();
        cursor = contentResolver.query(Uri.parse(SMS_URI_INBOX), new String[]{
                        "_id", "address", "body", "read"}, "body like ? and read=?",
                new String[]{"%快递%", "0"}, "date desc");
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                String smsbody = cursor
                        .getString(cursor.getColumnIndex("body"));
                String regEx = "[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(smsbody.toString());
                smsContent = m.replaceAll("").trim().toString();
                if (!TextUtils.isEmpty(smsContent)) {
                    listener.onResult(smsContent);
                }

            }
        }
    }

    /*
     * 短信回调接口
     */
    public interface SmsListener {
        /**
         * 接受sms状态
         *
         * @Title: onResult
         */
        void onResult(String smsContent);
    }
}
