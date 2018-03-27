package im.icbc.cn.keyboard;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import im.icbc.cn.keyboard.demo.catchInfo;
import im.icbc.cn.keyboard.utils.NewTransferInterface;
import im.icbc.cn.keyboard.utils.TransferData;
import im.icbc.cn.keyboard.utils.safeHandle;
import im.icbc.cn.keyboard.utils.serviceUtils;
import im.icbc.cn.keyboard.utils.transferInterface;

public class safeEditText extends ClearableEditText {
    private boolean m_bIsPassWord = true;
    private String m_sEncryptStr = null;
    private String m_sEncryptKey = null;
    private String m_sEncryptData = null;

    private Handler m_oHandler = null;
    private final int KEY_DOWN = 1;
    private final int CAPS_DOWN = 2;
    private final int CHANGEMODE_DOWN = 3;
    private final int ANIM_DOWN = 4;

    private final String m_sPackageName = "im.icbc.cn.keyboard";

    private boolean m_bIsServerStart = false;
    private int m_iPassLen = 0;

    private transferInterface m_oInterface = null;
    private NewTransferInterface m_oNewInterface = null;
    private final int m_iOriginalWidth = 875;
    private final int m_iOriginalHeight = 672;

    private boolean[] m_bsModeOrders = null;
    private String[] m_saSignArray = null;

    private boolean m_bIsCallSystemInputMethod = false;    //是否可调用【切换系统键盘】按键键盘样式
    private boolean m_bIsShowSystemInputMethod = false;    //是否显示【切换系统键盘】按键 仅在可调用【切换系统键盘】按键键盘样式时此参数有意义
    private int m_iMaxLen = 10;
    private int m_iUIType = 1;
    private boolean m_bFullUI = true;
    private boolean m_bShowInputText = false;
    private int m_iDefaultKeyboard = 0; //默认显示的键盘 0：字母，1：符号，2：数字

    private boolean m_bSettingAnim = false;
    private String m_sExitText = "完 成";
    private String m_sFinishText = "关 闭";
    private boolean m_isOverSeas;

    //20160425 ad
    private String strICBCRandomId = "";

    private Activity m_oParent = null;
    private safeSoftKeyBoard m_oKeyBoard = null;

    // Add:2016.5.7 MaKai-[Add encryption of 3DES] Start
    //0:手机银行调用
    //1:U盾厂商调用
    private int m_iUserFlag = 0;

    public void setUserFlag(int userFlag) {
        m_iUserFlag = userFlag;
    }

    // Add:2016.5.7 MaKai-[Add encryption of 3DES] End
    // Add:2016.6.6 MaKai-[SM2&SM4&openssl] Start
    //0:非国密
    //1:国密
    private int m_iEncryptType = 0;

    public void setEncryptType(int encryptType) {
        m_iEncryptType = encryptType;
        //Encrypt.setEncryptType(encryptType);
    }

    public int getEncryptType() {
        return m_iEncryptType;
    }
    // Add:2016.6.6 MaKai-[SM2&SM4&openssl] End

    /**
     * 指定配置文件
     *
     * @param iniName 文件名称
     *                DaBing Add this in 2016/10/18
     */
    public void setIniName(String iniName) {
        if (!TextUtils.isEmpty(iniName)) {
            // Encrypt.setIniName(iniName);
        }
    }

    /**
     * 指定是否调用了海外版安全键盘
     *
     * @param callOS
     */
    public void setOverSeasCall(boolean callOS) {
        m_isOverSeas = callOS;
    }

    public safeEditText(Context context, boolean callSystemInputMethod) {
        super(context);
        setListener(null);
        m_bIsCallSystemInputMethod = callSystemInputMethod;
    }

    public safeEditText(Context context, boolean bCallSystemInputMethod, boolean bShowSystemInputMethod) {
        super(context);
        setListener(null);
        m_bIsCallSystemInputMethod = bCallSystemInputMethod;
        m_bIsShowSystemInputMethod = bShowSystemInputMethod;
    }

    public safeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setListener(null);
    }

    public safeEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setListener(null);
    }


    //设置一次一密用随机数  20160512
    public void SetRandomId(String randomId) {
        if (randomId.length() == 20) {
            strICBCRandomId = randomId;
        }
    }

    public String getEncryptStr() {
        if (strICBCRandomId.length() == 20) {
            String strRandomId = "ICBCRandomID" + strICBCRandomId + "ICBCRandomID";
            String strEncrypt = strRandomId + m_sEncryptData;
            return strEncrypt;
        }
        return m_sEncryptData;
    }

    public int getPassLen() {
        return m_iPassLen;
    }

    public void setInterface(transferInterface ti) {
        m_oInterface = ti;
    }

    public void setNewInterface(NewTransferInterface ti) {
        m_oNewInterface = ti;
    }

    public void setIsOrdered(boolean[] orders) {
        m_bsModeOrders = orders;
    }

    public void setIsFullUI(boolean flag) {
        m_bFullUI = flag;
    }

    public void setShowInputTextFlag(boolean flag) {
        m_bShowInputText = flag;
    }

    public void setDefaultKeyboard(int flag) {
        m_iDefaultKeyboard = flag;
    }


    public void setSignArray(String[] strs) {
        m_saSignArray = strs;
    }

    public void setAnimStat(boolean anim) {
        if (m_bIsCallSystemInputMethod)
            return;

        m_bSettingAnim = anim;
    }

    public boolean getAnimStat() {
        return m_bSettingAnim;
    }

    public boolean setUIType(int type) {
        if (type < 0)
            return false;
        m_iUIType = type;
        return true;
    }


    public void setExitShowText(String exitText, String finishText) {
        if (!TextUtils.isEmpty(exitText))
            m_sExitText = exitText;

        if (!TextUtils.isEmpty(finishText))
            m_sFinishText = finishText;
    }

    //20160426
    public void initMe(boolean callSystemInputMethod, boolean isPassWord, String key, boolean bShowSystemInputMethod) {
        m_bIsShowSystemInputMethod = bShowSystemInputMethod;
        initMe(callSystemInputMethod, isPassWord, key);
    }


    public void initMe(boolean callSystemInputMethod, boolean isPassWord, String key) {
        serviceUtils.copyIni(getContext());
        safeHandle.setOutHandler(null);
        m_bIsCallSystemInputMethod = callSystemInputMethod;
        if (m_bsModeOrders == null) {
            m_bsModeOrders = new boolean[3];
            for (int i = 0; i < m_bsModeOrders.length; i++) {
                m_bsModeOrders[i] = false;
            }
        }

        init();

        m_bIsPassWord = isPassWord;
        if (!TextUtils.isEmpty(key))
            m_sEncryptKey = key;
    }

    //add by suyanhui 20150403 for webview handle clean
    public void cleanOutHandle() {
        safeHandle.setOutHandler(null);
    }

    public void setEncryptInfo(boolean isPassWord, String key) {
        m_bIsPassWord = isPassWord;
        m_sEncryptKey = key;
    }

    public void setTextMaxLen(int len) {
        m_iMaxLen = len;
    }

    public int getKeyBoardHeight() {
        DisplayMetrics localDisplayMetrics = getContext()
                .getApplicationContext().getResources().getDisplayMetrics();
        if (localDisplayMetrics == null)
            return 0;

        float ratioX = (float) localDisplayMetrics.widthPixels
                / m_iOriginalWidth;
        int h = (int) (m_iOriginalHeight * ratioX + 0.5F);

        if (h > localDisplayMetrics.heightPixels / 2)
            h = localDisplayMetrics.heightPixels / 2;

        return h;
    }

    //只供非全屏软键盘在关闭前保存密文信息使用
    public void saveEncryptStr() {
        if (!m_bFullUI) {

            if (m_sEncryptStr != null) {
                m_sEncryptData = new String(m_sEncryptStr);
                m_sEncryptStr = null;
            }
        }
        return;
    }

    public void saveEncryptStrNew() {
        if (m_sEncryptStr != null) {
            m_sEncryptData = new String(m_sEncryptStr);
            m_sEncryptStr = null;
        }
    }

    public boolean closeKeyBoard() {
        saveEncryptStrNew();

        if (m_oKeyBoard != null) {
            WindowManager m_oWindowManager = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE));

            if (m_oWindowManager != null)
                m_oWindowManager.removeView(m_oKeyBoard);

            if (m_oInterface != null)
                m_oInterface.downLayout(1);

            m_oKeyBoard.cleanMemory();
            m_oKeyBoard = null;
        }
        return true;
    }

    public void setCallParent(Activity activity) {
        m_oParent = activity;
    }

    public void openKeyBoard() {

        hideSoftInputMethod();
        cleanOutHandle();
        safeHandle.setHandler(m_oHandler);

        WindowManager m_oWindowManager = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE));
        if (m_oKeyBoard != null) {
            if (m_oWindowManager != null)
                m_oWindowManager.removeView(m_oKeyBoard);
            m_oKeyBoard.cleanMemory();
            m_oKeyBoard = null;
        }

        m_oKeyBoard = new safeSoftKeyBoard(getContext());
        // Add:2016.5.7 MaKai-[Add encryption of 3DES] Start
        if (null != m_oKeyBoard) {
            m_oKeyBoard.setUserFlag(this.m_iUserFlag);
        }
        // Add:2016.5.7 MaKai-[Add encryption of 3DES] End

        if (!TextUtils.isEmpty(m_sEncryptKey))
            m_oKeyBoard.setKey(m_sEncryptKey);

        Drawable drawable = null;

        if (m_iUIType == 2)
            drawable = getResources().getDrawable(R.drawable.back_2);
        else if (m_iUIType == 1) {
            if (!m_bIsCallSystemInputMethod)
                drawable = getResources().getDrawable(R.drawable.backnew_1);
            else
                drawable = getResources().getDrawable(R.drawable.bg_new);
        } else if (m_iUIType == 5) {
            drawable = getResources().getDrawable(R.drawable.bgmini);
        }
        m_oKeyBoard.setSendId(0);
        m_oKeyBoard.setIsOrdered(m_bsModeOrders);
        m_oKeyBoard.setSighArray(m_saSignArray);
        m_oKeyBoard.setTextMaxLen(m_iMaxLen);

        //20160426
        //m_oKeyBoard.setCallSysSoftKeyBoard(m_bIsCallSystemInputMethod);
        m_oKeyBoard.setCallSysSoftKeyBoard(m_bIsCallSystemInputMethod, m_bIsShowSystemInputMethod);

        m_oKeyBoard.setAnimStat(m_bSettingAnim);
        m_oKeyBoard.setUIType(m_iUIType);
        m_oKeyBoard.setExitText(m_sExitText);
        m_oKeyBoard.setFinishText(m_sFinishText);
        m_oKeyBoard.setShowInputTextFlag(m_bShowInputText);
        m_oKeyBoard.setDefaultKeyboard(m_iDefaultKeyboard);
        m_oKeyBoard.initSoftKeyBoard(drawable);

        if (m_oWindowManager != null) {
            DisplayMetrics localDisplayMetrics = getContext().getApplicationContext().getResources().getDisplayMetrics();

            int w1 = 0;
            int h1 = 0;
            if (m_bIsCallSystemInputMethod) {
                w1 = 875;
                h1 = 672;
            } else {
                w1 = 640;
                h1 = 525;
            }
            float ratioX = (float) localDisplayMetrics.widthPixels / w1;
            int m_iMaxWidth = localDisplayMetrics.widthPixels;
            int m_iMaxHeight = (int) (h1 * ratioX + 0.5F);

            if (m_iMaxHeight > localDisplayMetrics.heightPixels / 2) {
                m_iMaxHeight = localDisplayMetrics.heightPixels / 2;
            }

            int h = localDisplayMetrics.heightPixels - m_iMaxHeight;

            //创建半屏UI,应用自己处理空白区域点击事件
            if (!m_bFullUI) {
                int height = m_iMaxHeight + safeSoftKeyBoard.m_iMoveDownY;
                int yPos = -((m_iMaxHeight + safeSoftKeyBoard.m_iMoveDownY) / 2 - localDisplayMetrics.heightPixels / 2);
                WindowManager.LayoutParams m_oLayoutParams = new WindowManager.LayoutParams(m_iMaxWidth, height,
                        0, yPos, WindowManager.LayoutParams.TYPE_APPLICATION,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                        PixelFormat.TRANSLUCENT);
                m_oWindowManager.addView(m_oKeyBoard, m_oLayoutParams);
            } else {
                int height = m_iMaxHeight + safeSoftKeyBoard.m_iMoveDownY;//409
                int xPos = localDisplayMetrics.widthPixels / 2;
                xPos = localDisplayMetrics.heightPixels / 2;    //add
                int yPos = -(h / 2 - h);
                WindowManager.LayoutParams m_oLayoutParams = new WindowManager.LayoutParams(m_iMaxWidth, height,
                        xPos, yPos,
                        WindowManager.LayoutParams.TYPE_APPLICATION,
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
                m_oWindowManager.addView(m_oKeyBoard, m_oLayoutParams);
            }

            if (m_oInterface != null)
                m_oInterface.upLayout();
        }

        m_sEncryptStr = null;
        m_sEncryptData = null;
        m_iPassLen = 0;
        // Add:2016.5.7 MaKai-[Add encryption of 3DES] Start
        //Encrypt.resetStr();
        // Add:2016.5.7 MaKai-[Add encryption of 3DES] End

    }

    public void cleanData() {
        setText("");
        // Add:2016.5.7 MaKai-[Add encryption of 3DES] Start
        /*if (safeHandle.getLoadLibraryState()) {
            Encrypt.resetStr();
        }*/
        // Log.d("safeEdit", "clean data!");
        // Add:2016.5.7 MaKai-[Add encryption of 3DES] End
        m_sEncryptStr = null;
        m_sEncryptData = null;
        m_iPassLen = 0;
        if (m_oKeyBoard != null)
            m_oKeyBoard.initCurrentPos();

        if (!m_bFullUI) {
            // Log.i("--m_bFullUI--", "open-front");
            closeKeyBoard();
            openKeyBoard();
        }

    }

    public boolean copyIni(Context ctx) {
        try {
            File file = ctx.getFilesDir();

            String path = file.getAbsolutePath();
            // path += "/data.ini";
            path += "/data1.ini";
            File f = new File(path);
            if (!f.exists()) {
                // InputStream is = ctx.getAssets().open("data.ini");
                InputStream is = ctx.getAssets().open("data1.ini");
                byte[] data = new byte[is.available()];
                is.read(data);
                is.close();

                FileOutputStream os = new FileOutputStream(f);
                os.write(data);
                os.flush();
                os.close();
            }

        } catch (Exception e) {
            e.getMessage();
            return false;
        }

        return true;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        // TODO SL
        // super.onConfigurationChanged(newConfig);
        reSizeKeyBoard();
    }

    private void reSizeKeyBoard() {
        if (m_bIsServerStart)
            return;

        if (safeHandle.getServerState()) {
            getContext().stopService(new Intent(getContext(), catchInfo.class));

            int count = 0;
            do {
                SystemClock.sleep(100);
                count++;
            } while (count != 5);

            safeHandle.setHandler(m_oHandler);
            Intent i = new Intent(getContext(), catchInfo.class);

            if (m_bIsPassWord)
                i.putExtra("key", m_sEncryptKey);
            if (m_saSignArray != null)
                i.putExtra("signs", m_saSignArray);

            i.putExtra("order", m_bsModeOrders);

            getContext().startService(i);

            m_bIsServerStart = true;
        }
    }

    private boolean isServiceRunning(Context ctx) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List runningServers = manager.getRunningServices(100);

        if ((runningServers == null) || (runningServers.size() <= 0))
            return false;

        int len = runningServers.size();
        for (int i = 0; i < len; i++) {
            ActivityManager.RunningServiceInfo info = (ActivityManager.RunningServiceInfo) runningServers.get(i);
            if (info == null)
                continue;
            String name = info.service.getPackageName();
            if (name == null)
                continue;

            if (m_sPackageName.equals(name))
                return true;
        }

        return false;
    }

    private void init() {
        hideSoftInputMethod();
        setLongClickable(false);

        /*if (!safeHandle.getLoadLibraryState())
            safeHandle.setIsLoadLibraryState(Encrypt.initialize(""));

        if (safeHandle.getLoadLibraryState()) {
            File file = getContext().getFilesDir();

            String path = file.getAbsolutePath();
            // path += "/data.ini";

            // DaBing Add this in 2016/10/18
            // path += "/data1.ini";

            String iniName = Encrypt.getIniName();
            if (TextUtils.isEmpty(iniName))
                iniName = "data1.ini";
            path += "/" + iniName;

            try {
                if (m_isOverSeas)
                    Encrypt.setOverSeasCall(1);
                Encrypt.initEncrypt(path);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }*/

        m_sEncryptKey = new String("123456");

        setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    openKeyBoard();
                }
                return false;
            }
        });

        createHandler();
    }

    // DaBing Add this method in 2016/10/08
    // START
    private void hideSoftInputMethod() {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            methodName = "setSoftInputShownOnFocus";
        }

        if (methodName == null) {
            setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName,
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(this, false);
            } catch (NoSuchMethodException e) {
                setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        super.onFocusChange(v, hasFocus);
        if (hasFocus) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        } else {
            closeKeyBoard();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (m_oKeyBoard != null) {
                closeKeyBoard();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    //END

    private boolean createHandler() {
        if (m_oHandler != null)
            return true;

        m_oHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                onMessage(msg);
            }
        };
        return (m_oHandler != null);
    }

    private void onMessage(Message msg) {
        switch (msg.what) {
            case KEY_DOWN:
                processKeyDown((TransferData) msg.obj);
                break;
            case CAPS_DOWN:
                if (m_oNewInterface != null)
                    m_oNewInterface.CapsLockDown(msg.arg1);
                break;
            case ANIM_DOWN:
                if (m_oNewInterface != null)
                    m_oNewInterface.AnimSetDown(msg.arg1);
                break;
            case CHANGEMODE_DOWN:
                if (m_oNewInterface != null) {
                    m_oNewInterface.ChangeModeDown(msg.arg1);
                }
                break;
            default:
                break;
        }
    }

    private boolean processKeyDown(TransferData td) {
        String content = td.m_sContent;
        String dContent = td.m_dContent;
        if (TextUtils.isEmpty(content))
            return false;

        if (content.equals("退出") || content.equals("三角退出") || content.equals("空白退出")) {
            m_bIsServerStart = false;
            safeHandle.setServerState(false);

            if (m_oInterface != null)
                m_oInterface.downLayout(1);

            if (m_sEncryptData != null)
                m_sEncryptData = null;

            // String a = Encrypt.getChangeRule();
            // String b = Encrypt.getRule();
            // String c = Encrypt.getCRuleAndVersion();

            if (m_sEncryptStr != null) {
                m_sEncryptData = new String(m_sEncryptStr);
                m_sEncryptStr = null;
            }

            if (td.m_iX == 1)
                m_bSettingAnim = true;
            else
                m_bSettingAnim = false;

            if (content.equals("退出") && m_oNewInterface != null) {
                m_oNewInterface.ExitDown();
            } else if (content.equals("三角退出") && m_oNewInterface != null) {
                m_oNewInterface.finishDown();
            }
            if (m_oKeyBoard != null) {
                WindowManager m_oWindowManager = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE));

                if (m_oWindowManager != null)
                    m_oWindowManager.removeView(m_oKeyBoard);

                m_oKeyBoard.cleanMemory();
                m_oKeyBoard = null;
            }

            return true;

        }

        if (content.equals("切换系统输入法")) {

            m_bIsServerStart = false;
            safeHandle.setServerState(false);

			/*
             * Timer timer = new Timer(); timer.schedule(new TimerTask() {
			 * public void run() { InputMethodManager inputMethodManager =
			 * (InputMethodManager)
			 * getContext().getSystemService("input_method");
			 * inputMethodManager.toggleSoftInput(0, 2); } }, 100);
			 */

            /*if (m_sEncryptStr != null) {
                // Add:2016.5.7 MaKai-[Add encryption of 3DES] Start
                Encrypt.resetStr();
                // Add:2016.5.7 MaKai-[Add encryption of 3DES] End
                m_sEncryptStr = null;
            }*/
            setText("");
            if (!m_bShowInputText) {
                setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else {
                setInputType(InputType.TYPE_CLASS_TEXT);
            }

            if (m_oInterface != null) {
                m_oInterface.downLayout(2);
            }

            if (m_oKeyBoard != null) {
                WindowManager m_oWindowManager = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE));

                if (m_oWindowManager != null)
                    m_oWindowManager.removeView(m_oKeyBoard);

                m_oKeyBoard.cleanMemory();
                m_oKeyBoard = null;
            }

            return true;

        }

        if (TextUtils.isEmpty(m_sEncryptStr))
            setText("");

        if (m_bIsPassWord && !TextUtils.isEmpty(m_sEncryptKey)) {
            String str = getText().toString();

            if (!content.equals("退格") && m_iMaxLen <= str.length())
                return false;

            // String password = "";

            /*if (safeHandle.getLoadLibraryState()) {
                if (content.equals("退格")) {
                    if (str.length() > 0) {
                        // Del:2016.5.7 MaKai-[Add encryption of 3DES] Start
                        //m_sEncryptStr = m_sEncryptStr.substring(0, (int) (m_sEncryptStr.length() - td.m_iX));
                        //str = str.substring(0, str.length() - 1);
                        // Del:2016.5.7 MaKai-[Add encryption of 3DES] End
                        // Add:2016.5.7 MaKai-[Add encryption of 3DES] Start
                        if (0 == this.m_iUserFlag) {
                            m_sEncryptStr = m_sEncryptStr.substring(0, (int) (m_sEncryptStr.length() - td.m_iX));
                        } else {
                            m_sEncryptStr = Encrypt.deleteStr(getContext());
                        }
                        str = str.substring(0, str.length() - 1);
                        // Add:2016.5.7 MaKai-[Add encryption of 3DES] End
                    }
                } else {
                    if (m_sEncryptStr == null)
                        m_sEncryptStr = new String(content);
                    else {
                        // Del:2016.5.7 MaKai-[Add encryption of 3DES] Start
                        //m_sEncryptStr += content;
                        // Del:2016.5.7 MaKai-[Add encryption of 3DES] End
                        // Add:2016.5.7 MaKai-[Add encryption of 3DES] Start
                        if (0 == this.m_iUserFlag) {
                            m_sEncryptStr += content;
                        } else {
                            m_sEncryptStr = content;
                        }
                        // Add:2016.5.7 MaKai-[Add encryption of 3DES] End
                    }

                    //需要回显时，传出明文
                    if (m_bShowInputText) {
                        str += dContent;
                    } else {
                        str += "*";
                    }
                }

                setText(str);
                setSelection(str.length());
                m_iPassLen = str.length();
            } else {*/
            if (m_sEncryptStr == null)
                m_sEncryptStr = new String();

            if (content.equals("退格") && m_sEncryptStr.length() > 0) {
                if (!TextUtils.isEmpty(m_sEncryptStr)) {
                    // Del:2016.5.7 MaKai-[Add encryption of 3DES] Start
                    //m_sEncryptStr = m_sEncryptStr.substring(0, (int) (m_sEncryptStr.length() - td.m_iX));
                    //str = str.substring(0, str.length() - 1);
                    // Del:2016.5.7 MaKai-[Add encryption of 3DES] End
                    // Add:2016.5.7 MaKai-[Add encryption of 3DES] Start
                        /*if (0 == this.m_iUserFlag) {
                            m_sEncryptStr = m_sEncryptStr.substring(0, (int) (m_sEncryptStr.length() - td.m_iX));
                        } else {
                            m_sEncryptStr = Encrypt.deleteStr(getContext());
                        }*/
                    str = str.substring(0, str.length() - 1);
                    // Add:2016.5.7 MaKai-[Add encryption of 3DES] End
                }
            } else if (!content.equals("退格")) {
                // Del:2016.5.7 MaKai-[Add encryption of 3DES] Start
                //m_sEncryptStr = m_sEncryptStr + content;
                // Del:2016.5.7 MaKai-[Add encryption of 3DES] End
                // Add:2016.5.7 MaKai-[Add encryption of 3DES] Start
                if (0 == this.m_iUserFlag) {
                    m_sEncryptStr += content;
                } else {
                    m_sEncryptStr = content;
                }
                // Add:2016.5.7 MaKai-[Add encryption of 3DES] End
                //需要回显时，传出明文
                if (m_bShowInputText) {
                    str += dContent;
                } else {
                    str += "*";
                }
            }

            setText(str);
            setSelection(str.length());
            m_iPassLen = str.length();
            //}
        } else {
            String str = getText().toString();

            if (!content.equals("退格") && m_iMaxLen <= str.length())
                return false;

            if (content.equals("退格") && str.length() > 0)
                str = str.substring(0, str.length() - 1);
            else if (!content.equals("退格"))
                str = str + content;

            setText(str);
            setSelection(str.length());
            m_iPassLen = str.length();
        }

        return true;
    }

    @Override
    public void setListener(Listener listener) {
        super.setListener(new Listener() {
            @Override
            public void didClearText() {
                cleanData();
            }
        });
    }

}
