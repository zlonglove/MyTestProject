package im.icbc.cn.keyboard.demo;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.text.TextUtils;

import im.icbc.cn.keyboard.R;
import im.icbc.cn.keyboard.safeSoftKeyBoard;

public class catchInfo extends Service {
    private safeSoftKeyBoard m_oKeyBoard = null;

    @Override
    public IBinder onBind(Intent intent) {
        m_oKeyBoard.show();
        return null;
    }

    @Override
    public boolean onUnbind(Intent arg0) {
        return super.onUnbind(arg0);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (m_oKeyBoard != null)
            m_oKeyBoard.cleanMemory();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        m_oKeyBoard = new safeSoftKeyBoard(this);

        if (intent != null && m_oKeyBoard != null) {
            String key = intent.getStringExtra("key");
            if (!TextUtils.isEmpty(key))
                m_oKeyBoard.setKey(key);

            int uiType = intent.getIntExtra("uitype", 1);
            boolean callSoft = intent.getBooleanExtra("callsyssoftkeyboard",
                    false);
            Drawable drawable = null;

            if (uiType == 2)
                drawable = getResources().getDrawable(R.drawable.back_2);
            else if (uiType == 1) {
                if (!callSoft)
                    drawable = getResources().getDrawable(R.drawable.backnew_1);
                else
                    drawable = getResources().getDrawable(R.drawable.bg_new);
            } else if (uiType == 5)
                drawable = getResources().getDrawable(R.drawable.bgmini);

//			if (drawable != null)
//				m_oKeyBoard.setBackgroundDrawable(drawable);
//			else
//				m_oKeyBoard.setBackgroundColor(0xffcacaca);

            m_oKeyBoard.setSendId(intent.getIntExtra("sendid", 0));
            m_oKeyBoard.setIsOrdered(intent.getBooleanArrayExtra("order"));
            if (intent.hasExtra("signs")) {
                m_oKeyBoard.setSighArray(intent.getStringArrayExtra("signs"));
            }
            m_oKeyBoard.setTextMaxLen(intent.getIntExtra("maxlen", 10));
            m_oKeyBoard.setCallSysSoftKeyBoard(callSoft);
            m_oKeyBoard.setAnimStat(intent.getBooleanExtra("setanim", false));
            m_oKeyBoard.setUIType(intent.getIntExtra("uitype", 1));
            if (intent.hasExtra("exittext")) {
                m_oKeyBoard.setExitText(intent.getStringExtra("exittext"));
            }
            if (intent.hasExtra("finishtext")) {
                m_oKeyBoard.setFinishText(intent.getStringExtra("finishtext"));
            }
            //创建非全屏窗口
            m_oKeyBoard.setIsKeyBoardFullUI(intent.getBooleanExtra("fullui", true));
            //是否回显 明文
            m_oKeyBoard.setShowInputTextFlag(intent.getBooleanExtra("showinputtext", false));
            //默认显示键盘 0：字母 1：符号 2：数字
            m_oKeyBoard.setDefaultKeyboard(intent.getIntExtra("defaultkeyboard", 0));
            m_oKeyBoard.initSoftKeyBoard(drawable);
        }

        m_oKeyBoard.show();

        return super.onStartCommand(intent, flags, startId);
    }

}