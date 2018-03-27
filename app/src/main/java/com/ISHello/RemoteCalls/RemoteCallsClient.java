package com.ISHello.RemoteCalls;


import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ISHello.Menu.ISMenu;
import com.ISHello.MyUI.VerticalSeekBar;
import com.ISHello.ScreenInfo.ISScreenInfo;
import com.example.ishelloword.R;

public class RemoteCallsClient extends Activity {
    private final String TAG = "RemoteCallsClient";
    private final int GETTEXT = 1;
    private IPersonaidl iPersonaidl;
    private TextView remoteCallsText;
    private LinearLayout rootLayout;

    private PopupWindow mMenuWindow;
    private View mMenuView;
    private ISMenu mOptionMenu;
    private VerticalSeekBar verticalSeekBar;

    public final static int[] MENU_TEXT_RES_ID;

    static {
        MENU_TEXT_RES_ID = new int[3];
        MENU_TEXT_RES_ID[0] = R.string.user_account;
        MENU_TEXT_RES_ID[1] = R.string.system_settings;
        MENU_TEXT_RES_ID[2] = R.string.about_software;
    }

    /**
     * 死亡代理
     */
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (iPersonaidl == null) {
                return;
            }
            iPersonaidl.asBinder().unlinkToDeath(mDeathRecipient, 0);
            iPersonaidl = null;

            /**
             * 重新绑定远程Service
             */
            Intent intent = new Intent("com.ISHello.RemoteCalls");
            bindService(intent, conn, Service.BIND_AUTO_CREATE);
        }
    };

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            // 获取远程Service的onBind方法返回的对象的代理   
            Log.i(TAG, "--->onServiceConnected()");
            android.os.IInterface iin = binder.queryLocalInterface("com.ISHello.RemoteCalls.IPersonaidl");
            if (((iin != null) && (iin instanceof com.ISHello.RemoteCalls.IPersonaidl))) {
                Log.i(TAG, "--->is same process");
            } else {
                Log.i(TAG, "--->not same process");
            }


            iPersonaidl = IPersonaidl.Stub.asInterface(binder);
            try {
                binder.linkToDeath(mDeathRecipient, 0);//给binder设置死亡代理
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "--->onServiceDisconnected()");
            iPersonaidl = null;
        }

    };

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETTEXT:
                    remoteCallsText.setText((CharSequence) msg.obj);
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "--->onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_calls_activity);
        remoteCallsText = (TextView) this.findViewById(R.id.RemoteCallsInfo);
        // 创建所需绑定服务的Intent
        Intent intent = new Intent();
        intent.setAction("com.ISHello.RemoteCalls");
        // 绑定远程服务   
        bindService(intent, conn, Service.BIND_AUTO_CREATE);
        rootLayout = (LinearLayout) findViewById(R.id.remote_call_root);
        verticalSeekBar = (VerticalSeekBar) this.findViewById(R.id.ISSlideBar);
        if (verticalSeekBar != null) {
            verticalSeekBar.setProgress(50);
        }
    }

    /**
     * 创建MENU
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "--->onCreateOptionsMenu(Menu menu)");
        showMenuWindow();
        return true;
    }

    /**
     * 拦截MENU
     */
    public boolean onMenuOpened(int featureId, Menu menu) {
        Log.i(TAG, "--->onMenuOpened()");
        if (mMenuWindow != null) {
            if (mMenuWindow.isShowing()) {
                mMenuWindow.dismiss();
            } else {
                mMenuWindow.showAtLocation(rootLayout, Gravity.BOTTOM, 0, 0);
            }
        } else {
            showMenuWindow();
        }
        return false;//返回为true 则显示系统menu
    }

    private void showMenuWindow() {
        Log.i(TAG, "--->showMenuWindow()");
        if (this.mMenuWindow == null) {
            this.mMenuView = View.inflate(this, R.layout.menubar_window, null);
            this.mMenuWindow = new PopupWindow(this.mMenuView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
            this.mMenuWindow.setAnimationStyle(R.style.PopupAnimation);

            this.mOptionMenu = ((ISMenu) this.mMenuView.findViewById(R.id.option_menu));
            this.mOptionMenu.addMenuItems(ISScreenInfo.instance(this).getWidth(), MENU_TEXT_RES_ID);
            this.mOptionMenu.setMenuClickListener(new View.OnClickListener() {
                public void onClick(View paramView) {
                    Log.i(TAG, "--->showMenuWindow - onClick");
                    int i = (Integer) paramView.getTag();
                    Log.i(TAG, "--->showMenuWindow - butResId : " + i);
                    switch (i) {

                        case R.string.user_account: {
                            Log.i(TAG, "--->Click User_Account");
                            hideMenuWindow();
                            break;
                        }

                        case R.string.system_settings: {
                            Log.i(TAG, "--->Click system_settings");
                            hideMenuWindow();
                            break;
                        }

                        case R.string.about_software: {
                            Log.i(TAG, "--->Click about_software");
                            hideMenuWindow();
                            break;
                        }
                        default:
                            break;
                    }
                }
            });

            this.mOptionMenu.setMenuPreClickListener(new ISMenu.MenuPreClickListener() {
                public void onPreClick() {
                    Log.i("optionMenu", "--->onPreClickListener.onPreClick() will call mMenuWindow.dismiss()");
                    mMenuWindow.dismiss();
                }
            });
            this.mOptionMenu.setMenuPreFocusChangedkListener(new ISMenu.MenuPreFocusChangedkListener() {
                public void onPreFocusChanged(View paramView) {
                    Log.i(TAG, "--->onPreFocusChanged() ");
                }
            });
        }
        this.mMenuWindow.showAtLocation(this.rootLayout, Gravity.BOTTOM, 0, 0);
        this.mOptionMenu.requestFocus();
        this.mOptionMenu.requestFocusFromTouch();
    }

    /**
     * 隐藏菜单栏
     */
    private void hideMenuWindow() {
        if ((this.mMenuWindow == null) || (!this.mMenuWindow.isShowing())) {
            return;
        }
        this.mMenuWindow.dismiss();
    }

    public void getRemoteCallsPlay(View view) {
        try {
            iPersonaidl.play();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void getRemoteCallsInfo(View view) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Log.i(TAG, "--->The Display==" + iPersonaidl.display());
                    String info = iPersonaidl.display();
                    Message message = handler.obtainMessage();
                    message.what = GETTEXT;
                    message.obj = info;
                    handler.sendMessage(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }


    @Override
    protected void onStart() {
        Log.i(TAG, "--->onStart()");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "--->onRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "--->onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "--->onPause()");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "--->onDestroy()");
        super.onDestroy();
        // 解除绑定
        this.unbindService(conn);

    }

}
