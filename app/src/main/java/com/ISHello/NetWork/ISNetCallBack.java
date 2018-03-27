package com.ISHello.NetWork;

import java.util.Vector;

import android.os.Message;
import android.util.Log;

public class ISNetCallBack implements ISNetCallBackInterface {

    private final String TAG = "ISNetCallBack";
    ISAndroidHandler androidHandler;

    public ISNetCallBack(ISAndroidHandler androidHandler2) {
        this.androidHandler = androidHandler2;
    }

    /**
     * 网络回调函数
     */
    @Override
    public int CallBack(Vector<?> arg0, int arg1, int cmd, int errcode) {
        Log.i(TAG, "--->NetWork call back cmd==" + cmd + " errorcode==" + errcode);
        if (errcode == 0) {
            switch (cmd) {
                case ISNetWorkCmd.EVENT_GET_PICTURE:
                    send_android_handler(ISNetWorkCmd.EVENT_GET_PICTURE, arg0, arg1);
                    break;

                case ISNetWorkCmd.EVENT_GET_HTML:
                    send_android_handler(ISNetWorkCmd.EVENT_GET_HTML, arg0, arg1);
                    break;

                default:
                    break;
            }
            return 0;
        } else {
            switch (cmd) {
                case ISNetWorkCmd.EVENT_GET_PICTURE:
                    send_android_handler(ISNetWorkCmd.NETWORK_ERROR, arg0, arg1);
                    break;
                case ISNetWorkCmd.EVENT_GET_HTML:
                    send_android_handler(ISNetWorkCmd.NETWORK_ERROR, arg0, arg1);
                    break;
                default:
                    break;
            }
            return -1;
        }

    }

    /**
     * 发送消息到Android的UI线程
     *
     * @param cmd
     * @param arg0
     * @param position
     */
    public void send_android_handler(int cmd, Vector<?> arg0, int position) {
        Message msg = androidHandler.obtainMessage();
        msg.what = cmd;
        if (arg0 != null) {
            msg.obj = arg0.get(position);
        }
        androidHandler.sendMessage(msg);
    }

}
