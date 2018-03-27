package com.ISHello.NetWork;

import java.io.IOException;
import java.util.Vector;

import android.graphics.Bitmap;
import android.util.Log;

import com.ISHello.Event.NetWorkEvent;
import com.ISHello.Event.NetWorkEventObserver;
import com.ISHello.Event.NetWorkEventSource;
import com.ISHello.GetPictureFromInternet.ISNetWorkClient;

public class ISNetWorThread extends Thread implements NetWorkEventObserver {

    private boolean flag;
    private final String TAG = "ISNetWorThread";
    private NetWorkEventSource eventSource;
    private ISNetCallBackInterface backInterface;

    public ISNetWorThread(String string, boolean b, ISNetCallBackInterface netCallBack) {
        this(string, b);
        this.backInterface = netCallBack;
        eventSource = NetWorkEventSource.instance();
        eventSource.addObserver(this);

    }

    public ISNetWorThread(Runnable runnable) {
        super(runnable);
    }

    public ISNetWorThread(String threadName) {
        super(threadName);
    }

    public ISNetWorThread(String threadName, boolean flag) {
        super(threadName);
        this.flag = flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public synchronized boolean getCurrentFlag() {
        return this.flag;
    }

    public synchronized void stopThread() {
        this.flag = false;
    }

    /**
     * Send event to observers.
     *
     * @param id
     */
    public void sendNetWorkEvent(int id, Object object) {
        eventSource.sendEvent(id, object);
    }

    @Override
    public boolean handleEvent(NetWorkEvent event) {
        boolean handled = false;
        String uriPath;
        Vector<Object> vector = new Vector<Object>();
        switch (event.getId()) {
            case ISNetWorkCmd.EVENT_GET_PICTURE:
                uriPath = (String) event.getObject();
                if (uriPath == null || "".equals(uriPath)) {
                    return false;
                }
                Log.i(TAG, "--->The uri path==" + uriPath);
                try {
                    Bitmap bitmap = ISNetWorkClient.getInstance().getImage(uriPath);
                    if (bitmap != null) {
                        vector.add(bitmap);
                        backInterface.CallBack(vector, 0, ISNetWorkCmd.EVENT_GET_PICTURE, 0);
                    }
                } catch (IOException e) {
                    backInterface.CallBack(null, -1, ISNetWorkCmd.EVENT_GET_PICTURE, -1);
                }
                handled = true;
                break;

            case ISNetWorkCmd.EVENT_GET_HTML:
                uriPath = (String) event.getObject();
                if (uriPath == null || "".equals(uriPath)) {
                    return false;
                }
                Log.i(TAG, "--->The uri path==" + uriPath);
                try {
                    String title = ISNetWorkClient.getInstance().getHtml(uriPath);
                    if (title != null) {
                        vector.add(title);
                        backInterface.CallBack(vector, 0, ISNetWorkCmd.EVENT_GET_HTML, 0);
                    }
                } catch (IOException e) {
                    backInterface.CallBack(null, -1, ISNetWorkCmd.EVENT_GET_PICTURE, -1);
                }
                handled = true;
                break;
            default:
                break;
        }
        return handled;
    }


    @Override
    public void run() {
        while (getCurrentFlag()) {
            if (eventSource != null) {
                eventSource.update();
            }
        }
    }


}
