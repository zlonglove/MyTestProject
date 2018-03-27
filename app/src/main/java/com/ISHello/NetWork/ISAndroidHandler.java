package com.ISHello.NetWork;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.ISHello.CustomToast.CustomToast;
import com.ISHello.GetPictureFromInternet.ISGetPictureFromInternet;

public class ISAndroidHandler extends Handler {

    private Activity activity;

    public ISAndroidHandler(Activity activity) {
        super();
        this.activity = activity;

    }


    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case ISNetWorkCmd.EVENT_GET_PICTURE:
                ISGetPictureFromInternet.getInstance().UpdateImage((Bitmap) msg.obj);
                break;

            case ISNetWorkCmd.NETWORK_ERROR:
                CustomToast.makeText(activity, "网络错误,请检查网络", Toast.LENGTH_LONG);
                break;
            case ISNetWorkCmd.EVENT_GET_HTML:
                WebCodeViewerActivity.getInstance().showHtml((String) msg.obj);
                break;
            default:
                break;
        }
    }
}
