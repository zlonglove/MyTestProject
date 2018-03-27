package im.icbc.cn.keyboard;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import im.icbc.cn.keyboard.utils.KeyData;
import im.icbc.cn.keyboard.utils.RandomValue;
import im.icbc.cn.keyboard.utils.TransferData;
import im.icbc.cn.keyboard.utils.safeHandle;

public class safeSoftKeyBoard extends RelativeLayout {
    private static final int vibratorTimes = 30;
    private Vector<KeyData> m_vKeyDataVector = null;
    private int m_iYSpaceDis = 10;// 20;
    private int m_iXSpaceDis = 6;// 15;
    private int m_iXLeftPadding = 3;
    private int m_iScreenPadding = 20;

    private int m_iOneLineKeyCount = 9;
    private int m_iOneKeyMaxHeight = 0;
    private int m_iMaxWidth = 0;
    private int m_iMaxHeight = 0;
    private boolean m_bKeyBoardFullUI = true; //是否以空白填充全屏：true：是      false：否
    private boolean m_bShowInputText = false; //在输入框内是否回显输入明文   true：回显，false：不回显
    private int m_iDefaultKeyboard = 0; //默认显示的键盘 0：字母，1：符号，2：数字

    private Handler m_oHandler = null;
    private Handler m_oOutHandler = null;
    private static final int KEY_DOWN = 1;
    private static final int CAPS_DOWN = 2;
    private static final int CHANGEMODE_DOWN = 3;
    private static final int ANIM_DOWN = 4;

    private WindowManager m_oWindowManager = null;
    private WindowManager.LayoutParams m_oLayoutParams = null;

    private boolean m_bIsCapital = false;

    private static final int m_iBackColor = Color.rgb(102, 102, 102);
    private static final int m_iFrontColor = Color.WHITE;
    //	private static final int m_iTextColor = Color.rgb(125, 125, 125);
    private static final int m_iTextColor = Color.rgb(80, 80, 80);
    private int m_iTextColor1 = Color.rgb(86, 87, 92);
    private static final int m_iTextColor2 = Color.rgb(89, 172, 255);
    private static final int m_iTextColor3 = Color.rgb(89, 172, 255);
    private static final int m_iTextShadowColor = Color.BLACK;
    private static final int m_iTextShadowColor1 = Color.BLACK;

    private int m_iSendId = 0;

    private String m_sKey = null;
    private int m_iOriginalWidth = 875;
    private int m_iOriginalHeight = 672;
    private int m_iOriginalYDis = 90;
    private int m_iOriginalFunctionPicWidth = 100;
    private int m_iOriginalFunctionPicHeight = 80;
    private int m_iOriginalShiftPicWidth = 330;
    private int m_iOriginalShiftPicHeight = 80;
    private int m_iOriginalSetPicWidth = 52;
    private int m_iOriginalSetPicHeight = 52;
    private static final int m_iOriginalXDis1 = 343;
    private static final int m_iOriginalXDis2 = 30;
    private static final int m_iOriginalXDis3 = 250;
    private static final int m_iOriginalXDis4 = 270;
    private static final float m_fDpi = (float) 1.5;

    private int m_iYDis = 0;

    private int m_iMode = 0;//0表示字母 ，1 表示符号， 2表示数字；
    private safeKey m_oSignKey = null;
    private safeKey m_oNumberKey = null;
    private safeKey m_oCharactorKey = null;
    private safeKey m_oShiftKey = null;
    private safeKey m_oSetKey = null;
    private safeKey m_oExitKey = null;

    private safeKey m_oSetTextKey = null;

    private AssetManager m_oAssetMag = null;
    private boolean[] m_bsModeOrdereds = null;

//	private String[] m_saSignValues = { "~", "`", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "-", "+", "=", "{", "[", "}", "]", "|", "\\", ":", ";", "\"", "'", "<", ",", ">", ".", "?", "/", "€", "£", "¥" };

    private String[] m_saSignValues = {"[", "]", "{", "}", "#", "%", "^", "*", "+", "=", "_", "-", "/", ":", ";", "(", ")", "$", "&", "@", ".", ",", "?", "!", "'", "\\", "|", "~", "`", "<", ">", "€", "£", "¥", "\""};

    private String[] m_saSaveCharactors = null;
    private String[] m_saSaveSigns = null;
    private String[] m_saSaveNumbers = null;

    private TextView m_oShowText = null;
    private Handler m_oInerHandler = null;
    private final int HIDE_TEXT = 2;
    private int m_iCurrentPos = 1;
    private int m_iMaxLen = 0;

    private Vibrator m_oVib = null;
    private int m_iSetPicMiddleX = 0;
    private RelativeLayout m_oSetLayout = null;
    private boolean m_bIsSettingShow = false;
    private TextView m_oSetStatText = null;
    private boolean m_bSettingAnim = false;
    private KeyData m_oCurrentClickedKey = null;
    private int m_iCurrentKeyType = 0;
    private TextView m_oIndicateText = null;
    private boolean m_bIsCallSysSoftKeyboard = false;    //是否可显示切换系统键盘样式
    private boolean m_bIsShowSysSoftKeyboardKey = false; //调用可切换系统键盘样式时，默认显示切换系统键盘按钮

    private ImageView m_oSoftImage = null;


    //TODO
    public static final int m_iMoveDownY = 17;//100;//键盘按钮整体向下平移的距离
    private TextView m_oIndicateTextNew = null;

    // 1, 1部手机银行
    // 2, 2部电商
    // 5, 5部海外
    private int m_iUIType = 0;
    private boolean m_bShowAnim = false;
    private int m_iAnimOpenColor = Color.YELLOW;
    private String m_sExitText = "登录";
    private String m_sFinishText = "完成";

    // Add:2016.5.7 MaKai-[Add encryption of 3DES] Start
    //0:手机银行调用
    //1:U盾厂商调用
    private int m_iUserFlag = 0;

    public void setUserFlag(int userFlag) {
        m_iUserFlag = userFlag;
    }

    public int getUserFlag() {
        return m_iUserFlag;
    }
    // Add:2016.5.7 MaKai-[Add encryption of 3DES] End

    public safeSoftKeyBoard(Context context) {
        super(context);
        m_oVib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
    }

    public safeSoftKeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_oVib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
    }

    public safeSoftKeyBoard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        m_oVib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
    }

    public void setKey(String key) {
        m_sKey = key;
    }

    public void setSendId(int id) {
        m_iSendId = id;
    }

    public void setIsOrdered(boolean[] order) {
        m_bsModeOrdereds = order;
    }

    public void setIsKeyBoardFullUI(boolean flag) {
        m_bKeyBoardFullUI = flag;
    }

    public void setSighArray(String[] strs) {
        if (strs == null || strs.length <= 0)
            return;

        m_saSignValues = null;
        m_saSignValues = strs;
    }

    public void setTextMaxLen(int maxLen) {
        m_iMaxLen = maxLen;
    }

    public void initCurrentPos() {
        m_iCurrentPos = 1;
    }

    public void setCallSysSoftKeyBoard(boolean call) {
        m_bIsCallSysSoftKeyboard = call;
    }

    //20160426
    public void setCallSysSoftKeyBoard(boolean bCanShowSystemKeyBoard, boolean bShow) {
        m_bIsCallSysSoftKeyboard = bCanShowSystemKeyBoard;
        m_bIsShowSysSoftKeyboardKey = bShow;
    }

    public void setAnimStat(boolean anim) {
        m_bSettingAnim = anim;
    }

    public void setUIType(int type) {
        m_iUIType = type;
    }

    public void setShowInputTextFlag(boolean flag) {
        m_bShowInputText = flag;
    }

    public void setDefaultKeyboard(int flag) {
        m_iDefaultKeyboard = flag;
    }

    public void setExitText(String str) {
        if (!TextUtils.isEmpty(str))
            m_sExitText = str;
    }

    public void setFinishText(String str) {
        if (!TextUtils.isEmpty(str))
            m_sFinishText = str;
    }

    public boolean initSoftKeyBoard() {
        DisplayMetrics localDisplayMetrics = getContext().getApplicationContext().getResources().getDisplayMetrics();
        if (localDisplayMetrics == null)
            return false;

        float r = getContext().getResources().getDisplayMetrics().density / m_fDpi;
        if (m_iUIType == 1 || m_iUIType == 5) {
            if (!m_bIsCallSysSoftKeyboard) {
                m_iYSpaceDis = 8;
                m_iXSpaceDis = 6;
                m_iOriginalWidth = 640;
                m_iOriginalHeight = 525;
            } else {
                m_iYSpaceDis = 14;
                m_iXSpaceDis = 6;
            }
        } else {
            m_iTextColor1 = Color.WHITE;
            m_iYSpaceDis = 3;
            m_iXSpaceDis = 2;
        }
        m_iYSpaceDis = (int) (m_iYSpaceDis * r);// 20;
        m_iXSpaceDis = (int) (m_iXSpaceDis * r);// 15;
        m_iXLeftPadding = (int) (m_iXLeftPadding * r);

        float ratioX = (float) localDisplayMetrics.widthPixels / m_iOriginalWidth;
        m_iMaxHeight = (int) (m_iOriginalHeight * ratioX + 0.5F);


        float ratioY = 0.0F;
        if (m_iMaxHeight > localDisplayMetrics.heightPixels / 2) {
            m_iMaxHeight = (localDisplayMetrics.heightPixels / 2);
            ratioY = (float) m_iMaxHeight / m_iOriginalHeight;
        } else {
            ratioY = ratioX;
        }

        m_vKeyDataVector = new Vector<KeyData>();

        m_oAssetMag = getContext().getAssets();

        if (m_iUIType == 5) {
            m_bSettingAnim = true;
        } else {
            if (m_bIsCallSysSoftKeyboard) {
                m_bSettingAnim = true;
            } else {
                SharedPreferences sharedata = getContext().getSharedPreferences("safeinput", Activity.MODE_PRIVATE);
                if (sharedata != null) {
                    //m_bSettingAnim = sharedata.getBoolean("animstat", false);
                    m_bSettingAnim = sharedata.getBoolean("animstat", m_bSettingAnim);
                }
            }
        }
        if (m_iUIType == 2)
            initSwitch(ratioX, ratioY);
        else if (m_iUIType == 1 || m_iUIType == 5) {
            if (m_bIsCallSysSoftKeyboard)
                initSwitch(ratioX, ratioY);
            else
                initNewSwitch(ratioX, ratioY, localDisplayMetrics.widthPixels);
        } else
            return false;

        m_iMaxWidth = localDisplayMetrics.widthPixels;

        if (m_iUIType == 2) {
            m_iMode = 2;
            int oneKeyWidth = (int) (((m_iMaxWidth - m_iScreenPadding - 2 * m_iXSpaceDis) / 3) + 0.5f);
            initNumbers(oneKeyWidth);
            initFunctions(oneKeyWidth);
            RandomValue.cleanMemory();
        } else if (m_iUIType == 1 || m_iUIType == 5) {
            m_iMode = 0;
            if (m_bIsCallSysSoftKeyboard) {
                if (m_iDefaultKeyboard == 0) {
                    m_iMode = 0;
                    int oneKeyWidth = (int) (((float) (m_iMaxWidth - m_iScreenPadding - 9 * m_iXSpaceDis) / 10) + 0.5f);
                    initCharactors(oneKeyWidth);
                    initFunctions(oneKeyWidth);
                } else if (m_iDefaultKeyboard == 2) {
                    m_iMode = 2;
                    int oneKeyWidth = (int) (((m_iMaxWidth - m_iScreenPadding - 2 * m_iXSpaceDis) / 3) + 0.5f);
                    initNumbers(oneKeyWidth);
                    initFunctions(oneKeyWidth);
                } else if (m_iDefaultKeyboard == 1) {
                    m_iMode = 1;
                    int oneKeyWidth = (int) ((m_iMaxWidth - m_iScreenPadding - (m_iOneLineKeyCount - 1) * m_iXSpaceDis) / m_iOneLineKeyCount + 0.5f);
                    initNewSignals();
                    initFunctions(oneKeyWidth);
                }

            } else {
                initNewNumbers();
                initNewCharactors();
                initNewFunctions(1);

                m_iMode = 1;
            }
            RandomValue.cleanMemory();
        }

        m_oHandler = safeHandle.getHandler();
        m_oOutHandler = safeHandle.getOutHandler();

        createHandler();

        return true;
    }


    public boolean initSoftKeyBoard(Drawable drawable) {
        DisplayMetrics localDisplayMetrics = getContext().getApplicationContext().getResources().getDisplayMetrics();
        if (localDisplayMetrics == null)
            return false;

        float r = getContext().getResources().getDisplayMetrics().density / m_fDpi;
        if (m_iUIType == 1 || m_iUIType == 5) {
            if (!m_bIsCallSysSoftKeyboard) {
                m_iYSpaceDis = 8;
                m_iXSpaceDis = 6;
                m_iOriginalWidth = 640;
                m_iOriginalHeight = 525;
            } else {
                m_iYSpaceDis = 14;
                m_iXSpaceDis = 6;
            }
        } else {
            m_iTextColor1 = Color.WHITE;
            m_iYSpaceDis = 3;
            m_iXSpaceDis = 2;
        }
        m_iYSpaceDis = (int) (m_iYSpaceDis * r);// 20;
        m_iXSpaceDis = (int) (m_iXSpaceDis * r);// 15;
        m_iXLeftPadding = (int) (m_iXLeftPadding * r);

        float ratioX = (float) localDisplayMetrics.widthPixels / m_iOriginalWidth;
        m_iMaxHeight = (int) (m_iOriginalHeight * ratioX + 0.5F);

	/*	float ratioY = 0.0F;
        if (m_iMaxHeight > localDisplayMetrics.heightPixels / 2) {
			m_iMaxHeight = (localDisplayMetrics.heightPixels / 2);
			ratioY = (float) m_iMaxHeight / m_iOriginalHeight;
		} else {
			ratioY = ratioX;
		}
	*/
        m_iMaxWidth = localDisplayMetrics.widthPixels;

        if (m_oSoftImage == null && drawable != null) {
            m_oSoftImage = new ImageView(getContext());
            //m_oSoftImage.setBackgroundDrawable(drawable);
            if (m_iUIType == 1) {
                m_oSoftImage.setBackgroundColor(Color.parseColor("#ECECEC"));
            } else {
                m_oSoftImage.setBackgroundDrawable(drawable);
            }
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(m_iMaxWidth, m_iMaxHeight);

            lp1.alignWithParent = true;
            lp1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            m_oSoftImage.setLayoutParams(lp1);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(m_iMaxWidth, m_iMaxHeight);

            lp.leftMargin = 0;
            lp.topMargin = 0;//50;
            lp.alignWithParent = true;
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);


            addView(m_oSoftImage, lp);
        } else if (drawable != null)
            //m_oSoftImage.setBackgroundDrawable(drawable);
            if (m_iUIType == 1) {
                m_oSoftImage.setBackgroundColor(Color.parseColor("#ECECEC"));
            } else {
                m_oSoftImage.setBackgroundDrawable(drawable);
            }


        float ratioY = 0.0F;
        if (m_iMaxHeight > localDisplayMetrics.heightPixels / 2) {
            m_iMaxHeight = (localDisplayMetrics.heightPixels / 2);
            ratioY = (float) m_iMaxHeight / m_iOriginalHeight;
        } else {
            ratioY = ratioX;
        }


        m_vKeyDataVector = new Vector<KeyData>();

        m_oAssetMag = getContext().getAssets();

        if (m_iUIType == 5) {
            m_bSettingAnim = true;
        } else {
            if (m_bIsCallSysSoftKeyboard) {
                m_bSettingAnim = true;
            } else {
                SharedPreferences sharedata = getContext().getSharedPreferences("safeinput", Activity.MODE_PRIVATE);
                if (sharedata != null) {
                    //m_bSettingAnim = sharedata.getBoolean("animstat", false);
                    m_bSettingAnim = sharedata.getBoolean("animstat", m_bSettingAnim);
                }
            }
        }
        if (m_iUIType == 2)
            initSwitch(ratioX, ratioY);
        else if (m_iUIType == 1 || m_iUIType == 5) {
            if (m_bIsCallSysSoftKeyboard)
                initSwitch(ratioX, ratioY);
            else
                initNewSwitch(ratioX, ratioY, localDisplayMetrics.widthPixels);
        } else
            return false;

        m_iMaxWidth = localDisplayMetrics.widthPixels;

        if (m_iUIType == 2) {
            m_iMode = 2;
            int oneKeyWidth = (int) (((m_iMaxWidth - m_iScreenPadding - 2 * m_iXSpaceDis) / 3) + 0.5f);
            initNumbers(oneKeyWidth);
            initFunctions(oneKeyWidth);
            RandomValue.cleanMemory();
        } else if (m_iUIType == 1 || m_iUIType == 5) {
            m_iMode = 0;
            if (m_bIsCallSysSoftKeyboard) {
                if (m_iDefaultKeyboard == 0) {
                    m_iMode = 0;
                    int oneKeyWidth = (int) (((float) (m_iMaxWidth - m_iScreenPadding - 9 * m_iXSpaceDis) / 10) + 0.5f);
                    initCharactors(oneKeyWidth);
                    initFunctions(oneKeyWidth);
                } else if (m_iDefaultKeyboard == 2) {
                    m_iMode = 2;
                    int oneKeyWidth = (int) (((m_iMaxWidth - m_iScreenPadding - 2 * m_iXSpaceDis) / 3) + 0.5f);
                    initNumbers(oneKeyWidth);
                    initFunctions(oneKeyWidth);
                } else if (m_iDefaultKeyboard == 1) {
                    m_iMode = 1;
                    int oneKeyWidth = (int) ((m_iMaxWidth - m_iScreenPadding - (m_iOneLineKeyCount - 1) * m_iXSpaceDis) / m_iOneLineKeyCount + 0.5f);
                    initNewSignals();
                    initFunctions(oneKeyWidth);
                }
            } else {
                initNewNumbers();
                initNewCharactors();
                initNewFunctions(1);

                m_iMode = 1;
            }
            RandomValue.cleanMemory();
        }

        m_oHandler = safeHandle.getHandler();
        m_oOutHandler = safeHandle.getOutHandler();

        createHandler();

        return true;
    }

    public boolean show() {
        if (m_oWindowManager == null)
            m_oWindowManager = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE));

        if (m_oWindowManager != null) {
            DisplayMetrics localDisplayMetrics = getContext().getApplicationContext().getResources().getDisplayMetrics();
            if (localDisplayMetrics == null)
                return false;

            int h = localDisplayMetrics.heightPixels - m_iMaxHeight;
            if (m_oLayoutParams == null) {
                if (!m_bKeyBoardFullUI) {
                    m_oLayoutParams = new WindowManager.LayoutParams(m_iMaxWidth, m_iMaxHeight + m_iMoveDownY, 0, -((m_iMaxHeight + safeSoftKeyBoard.m_iMoveDownY) / 2 - localDisplayMetrics.heightPixels / 2), WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,// 2003,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, // |
                            // WindowManager.LayoutParams.FLAG_SECURE,//
                            // 8448,
                            1);
                } else {
                    m_oLayoutParams = new WindowManager.LayoutParams(m_iMaxWidth, m_iMaxHeight + m_iMoveDownY, localDisplayMetrics.widthPixels / 2, -(h / 2 - h), WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,// 2003,
                            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, // |
                            // WindowManager.LayoutParams.FLAG_SECURE,//
                            // 8448,
                            1);
                }

            }
            m_oWindowManager.addView(this, m_oLayoutParams);
        }

		/*
         * new Handler().postDelayed( new Runnable(){
		 *
		 * public void run() { WindowManager.LayoutParams lp =
		 * (WindowManager.LayoutParams)safeSoftKeyBoard.this.getLayoutParams();
		 * int x = lp.x; int y = lp.y;
		 *
		 * }
		 *
		 * }, 2000);
		 */

        return (m_oWindowManager != null);
    }

    public void cleanMemory() {
        if (m_vKeyDataVector != null) {
            int len = m_vKeyDataVector.size();
            for (int i = 0; i < len; i++) {
                KeyData dk = (KeyData) m_vKeyDataVector.get(i);
                if (dk == null || dk.m_oSafeKey == null)
                    continue;
                dk.m_oSafeKey.cleanMemory();
                dk.m_oSafeKey = null;
                dk = null;
            }

            m_vKeyDataVector.clear();
        }

        if (m_oWindowManager != null)
            m_oWindowManager.removeView(this);

        if (m_oIndicateText != null) {
            removeView(m_oIndicateText);
            m_oIndicateText.setBackgroundDrawable(null);
            m_oIndicateText = null;
        }


        if (m_oIndicateTextNew != null) {
            removeView(m_oIndicateTextNew);
            m_oIndicateTextNew.setBackgroundDrawable(null);
            m_oIndicateTextNew = null;
        }

        m_saSignValues = null;
        m_saSaveCharactors = null;
        m_saSaveSigns = null;
        m_saSaveNumbers = null;
    }

    private boolean createHandler() {
        if (m_oInerHandler != null)
            return true;

        m_oInerHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                onMessage(msg);
            }
        };

        return (m_oInerHandler != null);
    }

    private void onMessage(Message msg) {
        switch (msg.what) {
            case HIDE_TEXT:
                if (m_oShowText != null) {
                    removeView(m_oShowText);
                    m_oShowText = null;
                }
                break;
        }
    }

    private boolean vibrateMe(int milliseconds) {
        if (m_oVib == null)
            return false;

        m_oVib.vibrate(milliseconds);

        return true;
    }

    private Bitmap zoomImage(Bitmap bitmap, int newWidth, int newHeight) {
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();

        Matrix matrix = new Matrix();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        float r = 0;
        if (scaleWidth < scaleHeight)
            r = scaleWidth;
        else
            r = scaleHeight;

        matrix.postScale(r, r);
        Bitmap ret = Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
        return ret;
    }

    private void initSwitch(float rx, float ry) {
        float r = 0.0F;
        if (rx < ry)
            r = rx;
        else
            r = ry;

        if (m_iUIType == 2) {
            m_iOriginalYDis = 140;
            m_iOriginalFunctionPicWidth = 100;
            m_iOriginalFunctionPicHeight = 100;
            m_iOriginalSetPicWidth = 60;
            m_iOriginalSetPicHeight = 60;
            m_iOriginalShiftPicHeight = 100;
        }

        int x = 0;
        int y = 0;
        int w = 0;
        int h = 0;
        if (m_bIsCallSysSoftKeyboard) {
            x = (int) (m_iOriginalXDis1 * rx + 0.5F);
            y = (int) (m_iOriginalYDis * ry + 0.5F);
            w = (int) (m_iOriginalShiftPicWidth * r + 0.5F);
            h = (int) (m_iOriginalShiftPicHeight * r + 0.5F);
        } else {
            x = (int) (243 * rx + 0.5F);
            y = (int) (m_iOriginalYDis * ry + 0.5F);
            w = (int) (m_iOriginalSetPicWidth * r + 0.5F);
            h = (int) (m_iOriginalSetPicHeight * r + 0.5F);
        }

        int x_dis = 0;
        Drawable bit = null;

        if (m_bIsCallSysSoftKeyboard) {
            if (m_bIsShowSysSoftKeyboardKey) {
                if (m_iUIType == 2) {
                    bit = getAssetBitmap(R.drawable.shift_2);

                    m_oShiftKey = new safeKey(getContext());
                    m_oShiftKey.initKey(w, h, null, bit, 0, 0, 0);
                } else if (m_iUIType == 1) {
                    bit = getAssetBitmap(R.drawable.transparent_1);

                    m_oShiftKey = new safeKey(getContext());
                    m_oShiftKey.initKey(w, h, "切换系统输入法", bit, 0, m_iTextColor1, -1);
                } else if (m_iUIType == 5) {
                    bit = getAssetBitmap(R.drawable.systom_5);

                    m_oShiftKey = new safeKey(getContext());
                    m_oShiftKey.initKey(w, h, null, bit, 0, 0, 0);
                }

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w, h);

                lp.leftMargin = ((x - w) / 2);
                lp.topMargin = ((y - h) / 2) + m_iMoveDownY; //20150415

                addView(m_oShiftKey, lp);

                KeyData keydata = new KeyData();
                keydata.m_sContent = "切换系统输入法";
                keydata.m_oRange = new Rect(lp.leftMargin, lp.topMargin, lp.leftMargin + w, lp.topMargin + h);
                keydata.m_oSafeKey = m_oShiftKey;
                m_vKeyDataVector.add(keydata);
            }

            // if (bit != null)
            // bit.recycle();
        } else {
            bit = getAssetBitmap(R.drawable.set_2);

            m_oSetKey = new safeKey(getContext());
            m_oSetKey.initKey(w, h, null, bit, 0, 0, 0);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w, h);

            lp.leftMargin = ((x - w) / 2);
            lp.topMargin = ((y - h) / 2) + m_iMoveDownY; //20150415

            m_iSetPicMiddleX = x / 2;
            addView(m_oSetKey, lp);

            KeyData keydata = new KeyData();
            keydata.m_sContent = "设置";
            keydata.m_oRange = new Rect(lp.leftMargin - w / 2, lp.topMargin, lp.leftMargin + w + w / 2, lp.topMargin + h);
            keydata.m_oSafeKey = m_oSetKey;
            m_vKeyDataVector.add(keydata);

            // if (bit != null)
            // bit.recycle();

            bit = getAssetBitmap(R.drawable.exit_2);

            m_oExitKey = new safeKey(getContext());
            m_oExitKey.initKey(w, h, null, bit, 0, 0, 0);

            RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(w, h);

            lp4.leftMargin = x / 2 + (x - w) / 2;
            lp4.topMargin = ((y - h) / 2) + m_iMoveDownY;

            addView(m_oExitKey, lp4);

            KeyData keydata4 = new KeyData();
            keydata4.m_sContent = "三角退出";
            keydata4.m_oRange = new Rect(lp4.leftMargin - w / 2, lp4.topMargin, lp4.leftMargin + w + w / 2, lp4.topMargin + h);
            keydata4.m_oSafeKey = m_oExitKey;
            m_vKeyDataVector.add(keydata4);

            // if (bit != null)
            // bit.recycle();

            x = (int) (m_iOriginalXDis1 * rx + 0.5F);
        }

        w = (int) (m_iOriginalFunctionPicWidth * r + 0.5F);
        h = (int) (m_iOriginalFunctionPicHeight * r + 0.5F);

        x_dis += x;
        x = (int) (m_iOriginalXDis3 * rx + 0.5F);

        if (m_iUIType == 2) {
            bit = getAssetBitmap(R.drawable.transparent_2);

            m_oNumberKey = new safeKey(getContext());
            m_oNumberKey.initKey(w, h, "数字", bit, 0, m_iTextColor2, m_iTextShadowColor);
        } else if (m_iUIType == 1) {
            bit = getAssetBitmap(R.drawable.transparent_1);
            if (m_iDefaultKeyboard == 2) {
                m_oNumberKey = new safeKey(getContext());
                m_oNumberKey.initKey(w, h, "数字", bit, 0, m_iTextColor2, -1);
            } else {
                m_oNumberKey = new safeKey(getContext());
                m_oNumberKey.initKey(w, h, "数字", bit, 0, m_iTextColor1, -1);
            }
        } else if (m_iUIType == 5) {
            bit = getAssetBitmap(R.drawable.number_5);

            m_oNumberKey = new safeKey(getContext());
            m_oNumberKey.initKey(w, h, null, bit, 0, 0, 0);
        }

        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(w, h);

        lp1.leftMargin = (x_dis + (x - w) / 2);
        lp1.topMargin = ((y - h) / 2) + m_iMoveDownY;

        addView(m_oNumberKey, lp1);

        KeyData keydata1 = new KeyData();
        keydata1.m_sContent = "数字";
        keydata1.m_oRange = new Rect(lp1.leftMargin, lp1.topMargin, lp1.leftMargin + w, lp1.topMargin + h);
        keydata1.m_oSafeKey = m_oNumberKey;
        m_vKeyDataVector.add(keydata1);

        // if (bit != null)
        // bit.recycle();

        x_dis += x;
        x = (int) (m_iOriginalXDis2 * rx + 0.5F);

        if (m_iUIType == 2) {
            bit = getAssetBitmap(R.drawable.transparent_2);

            m_oCharactorKey = new safeKey(getContext());
            m_oCharactorKey.initKey(w, h, "字母", bit, 0, m_iTextColor1, m_iTextShadowColor1);
        } else if (m_iUIType == 1) {
            bit = getAssetBitmap(R.drawable.transparent_1);
            if (m_iDefaultKeyboard == 0) {
                m_oCharactorKey = new safeKey(getContext());
                m_oCharactorKey.initKey(w, h, "字母", bit, 0, m_iTextColor2, -1);
            } else {
                m_oCharactorKey = new safeKey(getContext());
                m_oCharactorKey.initKey(w, h, "字母", bit, 0, m_iTextColor1, -1);
            }

        } else if (m_iUIType == 5) {
            bit = getAssetBitmap(R.drawable.chosencharactor_5);

            m_oCharactorKey = new safeKey(getContext());
            m_oCharactorKey.initKey(w, h, null, bit, 0, 0, 0);
        }

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(w, h);

        lp2.leftMargin = (x_dis + (x - w) / 2);
        lp2.topMargin = ((y - h) / 2) + m_iMoveDownY;

        addView(m_oCharactorKey, lp2);

        KeyData keydata2 = new KeyData();
        keydata2.m_sContent = "字母";
        keydata2.m_oRange = new Rect(lp2.leftMargin, lp2.topMargin, lp2.leftMargin + w, lp2.topMargin + h);
        keydata2.m_oSafeKey = m_oCharactorKey;
        m_vKeyDataVector.add(keydata2);

        // if (bit != null)
        // bit.recycle();

        x_dis += x;
        x = (int) (m_iOriginalXDis4 * rx + 0.5F);

        if (m_iUIType == 2) {
            bit = getAssetBitmap(R.drawable.transparent_2);

            m_oSignKey = new safeKey(getContext());
            m_oSignKey.initKey(w, h, "符号", bit, 0, m_iTextColor1, m_iTextShadowColor);
        } else if (m_iUIType == 1) {
            bit = getAssetBitmap(R.drawable.transparent_1);
            if (m_iDefaultKeyboard == 1) {
                m_oSignKey = new safeKey(getContext());
                m_oSignKey.initKey(w, h, "符号", bit, 0, m_iTextColor2, -1);
            } else {
                m_oSignKey = new safeKey(getContext());
                m_oSignKey.initKey(w, h, "符号", bit, 0, m_iTextColor1, -1);
            }
        } else if (m_iUIType == 5) {
            bit = getAssetBitmap(R.drawable.sign_5);

            m_oSignKey = new safeKey(getContext());
            m_oSignKey.initKey(w, h, null, bit, 0, 0, 0);
        }

        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(w, h);

        lp3.leftMargin = (x_dis + (x - w) / 2);
        lp3.topMargin = ((y - h) / 2) + m_iMoveDownY;

        addView(m_oSignKey, lp3);

        KeyData keydata3 = new KeyData();
        keydata3.m_sContent = "符号";
        keydata3.m_oRange = new Rect(lp3.leftMargin, lp3.topMargin, lp3.leftMargin + w, lp3.topMargin + h);
        keydata3.m_oSafeKey = m_oSignKey;
        m_vKeyDataVector.add(keydata3);

        if (m_iUIType == 2) {
            m_iOneKeyMaxHeight = ((m_iMaxHeight - 3 * m_iYSpaceDis - y - 2 * 5) / 4);
            m_iYDis = y + 5;
        } else {
            m_iOneKeyMaxHeight = ((m_iMaxHeight - 3 * m_iYSpaceDis - y) / 4) - 6;
            m_iYDis = y + 8;
        }

        // if (bit != null)
        // bit.recycle();
    }

    private void initNewSwitch(float rx, float ry, int width) {
        float r = 0.0F;
        if (rx < ry)
            r = rx;
        else
            r = ry;

        if (m_iUIType == 5) {
            m_iOneKeyMaxHeight = (m_iMaxHeight - 4 * m_iYSpaceDis - 10 * 2) / 5;
            m_iYDis = 10;
            return;
        }

        m_iOriginalYDis = 80;
        // 设置按钮大小
        m_iOriginalSetPicWidth = 100;
        m_iOriginalSetPicHeight = 74;
        // 按键反馈按钮大小
        m_iOriginalShiftPicWidth = 150;
        m_iOriginalShiftPicHeight = 54;
        // 完成按钮大小
        m_iOriginalFunctionPicWidth = 100;
        m_iOriginalFunctionPicHeight = 54;

        int x = 0;
        int y = 0;
        int w = 0;
        int h = 0;

        x = Dp2Px(5);
        y = (int) (m_iOriginalYDis * ry + 0.5F);
        w = (int) (m_iOriginalSetPicWidth * r + 0.5F);
        h = (int) (m_iOriginalSetPicHeight * r + 0.5F);

        Drawable bit = null;
        if (m_bSettingAnim)
            bit = getAssetBitmap(R.drawable.setopennew_1);
        else
            bit = getAssetBitmap(R.drawable.setclosenew);

        // 设置按钮
        m_oSetKey = new safeKey(getContext());
        m_oSetKey.initKey(w, h, null, bit, 0, 0, 0);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w, h);

        lp.leftMargin = x;
        lp.topMargin = ((y - h) / 2) + m_iMoveDownY;

        addView(m_oSetKey, lp);

        KeyData keydata = new KeyData();
        keydata.m_sContent = "设置";
        keydata.m_oRange = new Rect(lp.leftMargin, lp.topMargin, lp.leftMargin + w, lp.topMargin + h);
        keydata.m_oSafeKey = m_oSetKey;
        m_vKeyDataVector.add(keydata);

        // if (bit != null)
        // bit.recycle();

        x = x + w - Dp2Px(6);
        w = (int) (m_iOriginalShiftPicWidth * r + 0.5F);
        h = (int) (m_iOriginalShiftPicHeight * r + 0.5F);

        // 按键反馈按钮
        bit = getAssetBitmap(R.drawable.transparent_1);

        m_oSetTextKey = new safeKey(getContext());
        m_oSetTextKey.initKey(w, h, "按键反馈", bit, 0, m_iTextColor, -1);

        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(w, h);

        lp1.leftMargin = x;
        lp1.topMargin = ((y - h) / 2) + m_iMoveDownY;

        addView(m_oSetTextKey, lp1);

        KeyData keydata1 = new KeyData();
        keydata1.m_sContent = "按键反馈";
        keydata1.m_oRange = new Rect(lp1.leftMargin, lp1.topMargin, lp1.leftMargin + w, lp1.topMargin + h);
        keydata1.m_oSafeKey = m_oSetTextKey;
        m_vKeyDataVector.add(keydata1);

        // if (bit != null)
        // bit.recycle();

        w = (int) (m_iOriginalFunctionPicWidth * r + 0.5F);
        h = (int) (m_iOriginalFunctionPicHeight * r + 0.5F);
        x = width - Dp2Px(10) - w;

        // 完成按钮
        bit = getAssetBitmap(R.drawable.transparent_1);

        m_oExitKey = new safeKey(getContext());
        m_oExitKey.initKey(w, h, m_sFinishText, bit, 0, m_iTextColor3, -1);

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(w, h);

        lp2.leftMargin = x;
        lp2.topMargin = ((y - h) / 2) + m_iMoveDownY;

        addView(m_oExitKey, lp2);

        KeyData keydata2 = new KeyData();
        keydata2.m_sContent = "三角退出";
        keydata2.m_oRange = new Rect(lp2.leftMargin, lp2.topMargin, lp2.leftMargin + w, lp2.topMargin + h);
        keydata2.m_oSafeKey = m_oExitKey;
        m_vKeyDataVector.add(keydata2);

        // if (bit != null)
        // bit.recycle();

        m_iOneKeyMaxHeight = (m_iMaxHeight - 4 * m_iYSpaceDis - y - 5 * 2) / 5;

        m_iYDis = y + 5;
    }

    private void initNewNumbers() {
        int oneKeyWidth = (m_iMaxWidth - 2 * m_iXLeftPadding - 9 * m_iXSpaceDis) / 10;
        String[] values = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        RandomValue.initValues(values);

        Drawable drawable = getDrawable("shape_sback");

        if (m_saSaveNumbers == null)
            m_saSaveNumbers = new String[10];

        int j = 0;
        for (int i = 0; i < 10; i++) {
            safeKey sk = new safeKey(getContext());
            String str = null;
            if (m_bsModeOrdereds[0]) {
                str = values[j];
                j++;
            } else {
                if (m_saSaveNumbers[j] == null) {
                    str = RandomValue.getRandomValue();
                    m_saSaveNumbers[j] = new String(str);
                } else
                    str = m_saSaveNumbers[j];
                j++;
            }

            if (str == null)
                continue;

            sk.initKey(oneKeyWidth, m_iOneKeyMaxHeight, str, drawable, m_iBackColor, m_iTextColor);

            KeyData keydata = new KeyData();

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(oneKeyWidth, m_iOneKeyMaxHeight);

            lp.leftMargin = m_iXLeftPadding + i * (oneKeyWidth + m_iXSpaceDis);
            lp.topMargin = m_iYDis + m_iMoveDownY;

            addView(sk, lp);

            keydata.m_sContent = new String(str);
            keydata.m_oRange = new Rect(lp.leftMargin, lp.topMargin, lp.leftMargin + oneKeyWidth, lp.topMargin + m_iOneKeyMaxHeight);
            keydata.m_oSafeKey = sk;
            m_vKeyDataVector.add(keydata);
        }

        RandomValue.cleanMemory();

    }

    private void initNewCharactors() {
        m_bIsCapital = false;    //20160401 liu ad

        int oneKeyWidth = (m_iMaxWidth - 2 * m_iXLeftPadding - 9 * m_iXSpaceDis) / 10;

        String[] values = {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l", "z", "x", "c", "v", "b", "n", "m"};
        RandomValue.initValues(values);

        Drawable drawable = getDrawable("shape_sback");

        if (m_saSaveCharactors == null) {
            m_saSaveCharactors = new String[26];
        }

        int j = 0;
        for (int i = 0; i < 26; i++) {
            safeKey sk = new safeKey(getContext());
            String str = null;
            if (m_bsModeOrdereds[1]) {
                str = values[j];
                j++;
            } else {
                if (m_saSaveCharactors[j] == null) {
                    str = RandomValue.getRandomValue();
                    m_saSaveCharactors[j] = new String(str);
                } else
                    str = m_saSaveCharactors[j];
                j++;
            }

            if (str == null)
                continue;

            sk.initKey(oneKeyWidth, m_iOneKeyMaxHeight, str, drawable, m_iBackColor, m_iTextColor);

            KeyData keydata = new KeyData();

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(oneKeyWidth, m_iOneKeyMaxHeight);
            if (i < 10) {
                lp.leftMargin = m_iXLeftPadding + (i * (oneKeyWidth + m_iXSpaceDis));
                lp.topMargin = (m_iYDis + m_iOneKeyMaxHeight + m_iYSpaceDis) + m_iMoveDownY;
            } else if (i < 19) {
                lp.leftMargin = m_iXLeftPadding + oneKeyWidth / 2 + ((i - 10) * (oneKeyWidth + m_iXSpaceDis));
                lp.topMargin = (m_iYDis + 2 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;
            } else if (i < 26) {
                lp.leftMargin = m_iXLeftPadding + oneKeyWidth * 3 / 2 + m_iXSpaceDis + ((i - 19) * (oneKeyWidth + m_iXSpaceDis));
                lp.topMargin = (m_iYDis + 3 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;
            }

            addView(sk, lp);

            keydata.m_sContent = str;
            keydata.m_oRange = new Rect(lp.leftMargin, lp.topMargin, lp.leftMargin + oneKeyWidth, lp.topMargin + m_iOneKeyMaxHeight);
            keydata.m_oSafeKey = sk;
            m_vKeyDataVector.add(keydata);
        }

        Drawable backbit = null;
        Drawable frontbit = null;
        safeKey sk1 = new safeKey(getContext());

        if (m_iUIType == 1) {
            Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
            if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                DisplayMetrics localDisplayMetrics = getContext().getApplicationContext().getResources().getDisplayMetrics();
                if (localDisplayMetrics != null) {
                    if (localDisplayMetrics.widthPixels > localDisplayMetrics.heightPixels &&
                            localDisplayMetrics.widthPixels > 1000 && localDisplayMetrics.heightPixels < 801) {//Samsung
                        frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.spacenew_1_huashang : R.drawable.space_2_pad);
                    }
                    if (frontbit == null) {
                        frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.spacenew_1_huashang : R.drawable.spacenew_1);
                    }
                }
            } else { //if(ori == mConfiguration.ORIENTATION_PORTRAIT)	//竖屏
                frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.spacenew_1_huashang : R.drawable.spacenew_1);
            }
        } else if (m_iUIType == 5) {
            frontbit = getAssetBitmap(R.drawable.spacenew_5);
        }

        backbit = getDrawable("shape_sback");
        sk1.initKey(5 * oneKeyWidth + 4 * m_iXSpaceDis, m_iOneKeyMaxHeight, frontbit, backbit, m_iBackColor, m_iTextColor);

        KeyData keydata1 = new KeyData();

        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(5 * oneKeyWidth + 4 * m_iXSpaceDis, m_iOneKeyMaxHeight);

        lp1.leftMargin = m_iXLeftPadding + 2 * (oneKeyWidth + m_iXSpaceDis) + oneKeyWidth / 2;
        lp1.topMargin = (m_iYDis + 4 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;

        addView(sk1, lp1);

        keydata1.m_sContent = "空格";
        keydata1.m_oRange = new Rect(lp1.leftMargin, lp1.topMargin, lp1.leftMargin + 5 * oneKeyWidth + 4 * m_iXSpaceDis, lp1.topMargin + m_iOneKeyMaxHeight);
        keydata1.m_oSafeKey = sk1;
        m_vKeyDataVector.add(keydata1);

        RandomValue.cleanMemory();

        // if (backbit != null)
        // backbit.recycle();
    }

    private void initNewFunctions(int mode) {
        int oneKeyWidth = (m_iMaxWidth - 2 * m_iXLeftPadding - 9 * m_iXSpaceDis) / 10;
        int w = 3 * oneKeyWidth / 2;

        if (mode != 1 && mode != 2)
            return;

        Drawable frontbit = null;
        Drawable backDrawable = null;

        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息

        if (mode == 1) {
            // 大小写切换键
            backDrawable = getDrawable("shape_cback1");
            if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                frontbit = getAssetBitmap(R.drawable.capslocknew_1_pad);
            } else {
                frontbit = getAssetBitmap(R.drawable.capslocknew_1);
            }

            safeKey sk1 = new safeKey(getContext());
            sk1.initKey(w, m_iOneKeyMaxHeight, frontbit, backDrawable, m_iBackColor, m_iFrontColor);

            KeyData keydata1 = new KeyData();
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(w, m_iOneKeyMaxHeight);
            lp1.leftMargin = m_iXLeftPadding;
            lp1.topMargin = (m_iYDis + 3 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;
            addView(sk1, lp1);
            keydata1.m_sContent = new String("切换");
            keydata1.m_oRange = new Rect(lp1.leftMargin, lp1.topMargin, lp1.leftMargin + w, lp1.topMargin + m_iOneKeyMaxHeight);
            keydata1.m_oSafeKey = sk1;
            m_vKeyDataVector.add(keydata1);
        }
        // if (frontbit != null)
        // frontbit.recycle();

        // 退格键
        backDrawable = getDrawable("shape_cback1");
        if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            frontbit = getAssetBitmap(R.drawable.deletenew_1_pad);
        } else {
            frontbit = getAssetBitmap(R.drawable.deletenew_1);
        }

        safeKey sk2 = new safeKey(getContext());
        sk2.initKey(w, m_iOneKeyMaxHeight, frontbit, backDrawable, m_iBackColor, m_iFrontColor);

        KeyData keydata2 = new KeyData();

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(w, m_iOneKeyMaxHeight);

        lp2.leftMargin = m_iXLeftPadding + oneKeyWidth / 2 + 8 * (oneKeyWidth + m_iXSpaceDis);
        lp2.topMargin = (m_iYDis + 3 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;

        addView(sk2, lp2);

        keydata2.m_sContent = new String("退格");
        keydata2.m_oRange = new Rect(lp2.leftMargin, lp2.topMargin, lp2.leftMargin + w, lp2.topMargin + m_iOneKeyMaxHeight);
        keydata2.m_oSafeKey = sk2;
        m_vKeyDataVector.add(keydata2);

        // if (frontbit != null)
        // frontbit.recycle();

        // 退出功能键

        if (m_iUIType == 1)
            backDrawable = getDrawable("shape_cback2");
        else if (m_iUIType == 5)
            backDrawable = getDrawable("shape_cback1");
        w = 2 * oneKeyWidth + m_iXSpaceDis + oneKeyWidth / 2;

        safeKey sk3 = new safeKey(getContext());
        if (m_iUIType == 2 || m_iUIType == 1) {
            sk3.initKey(w, m_iOneKeyMaxHeight, m_sExitText, backDrawable, m_iBackColor, m_iFrontColor);
        } else if (m_iUIType == 5) {
            Drawable frontDrawable = getAssetBitmap(R.drawable.bigenter_5);
            sk3.initKey(w, m_iOneKeyMaxHeight, frontDrawable, backDrawable, -1, -1);
        }

        KeyData keydata3 = new KeyData();

        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(w, m_iOneKeyMaxHeight);

        lp3.leftMargin = m_iXLeftPadding + oneKeyWidth / 2 + 7 * (oneKeyWidth + m_iXSpaceDis);
        lp3.topMargin = (m_iYDis + 4 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;

        addView(sk3, lp3);

        keydata3.m_sContent = new String("退出");
        keydata3.m_oRange = new Rect(lp3.leftMargin, lp3.topMargin, lp3.leftMargin + w, lp3.topMargin + m_iOneKeyMaxHeight);
        keydata3.m_oSafeKey = sk3;
        m_vKeyDataVector.add(keydata3);

        // 模式切换键
        boolean find = false;
        int count = m_vKeyDataVector.size();
        int pos = 0;
        for (int i = 0; i < count; i++) {
            if (m_vKeyDataVector.get(i).m_sContent.equals("模式切换")) {
                find = true;
                pos = i;
                break;
            }
        }
        backDrawable = getDrawable("shape_sback");
        DisplayMetrics localDisplayMetrics = getContext().getApplicationContext().getResources().getDisplayMetrics();
        frontbit = null;
        if (mode == 1) {

            if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                if (localDisplayMetrics != null) {
                    if (localDisplayMetrics.widthPixels > localDisplayMetrics.heightPixels &&
                            localDisplayMetrics.widthPixels > 1000 && localDisplayMetrics.heightPixels < 801) {//Samsung:1280w*800h
                        frontbit = getAssetBitmap(R.drawable.signnew_1_pad);
                    }
                }

                if (frontbit == null) {
                    frontbit = getAssetBitmap(R.drawable.signnew_1);
                }
            } else {
                frontbit = getAssetBitmap(R.drawable.signnew_1);
            }


        } else if (mode == 2) {
            if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                if (localDisplayMetrics != null) {
                    if (localDisplayMetrics.widthPixels > localDisplayMetrics.heightPixels &&
                            localDisplayMetrics.widthPixels > 1000 && localDisplayMetrics.heightPixels < 801) {//Samsung:1280w*800h
                        frontbit = getAssetBitmap(R.drawable.numchanew_1_pad);
                    }
                }

                if (frontbit == null) {
                    frontbit = getAssetBitmap(R.drawable.numchanew_1);
                }
            } else {
                frontbit = getAssetBitmap(R.drawable.numchanew_1);
            }
        } else
            return;

        if (find) {
            m_vKeyDataVector.get(pos).m_oSafeKey.setNewContent(frontbit, backDrawable, 0, 0);
            return;
        }
        safeKey sk4 = new safeKey(getContext());
        sk4.initKey(w, m_iOneKeyMaxHeight, frontbit, backDrawable, m_iBackColor, m_iFrontColor);

        KeyData keydata4 = new KeyData();

        RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(w, m_iOneKeyMaxHeight);

        lp4.leftMargin = m_iXLeftPadding;
        lp4.topMargin = (m_iYDis + 4 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;

        addView(sk4, lp4);

        keydata4.m_sContent = new String("模式切换");
        keydata4.m_oRange = new Rect(lp4.leftMargin, lp4.topMargin, lp4.leftMargin + w, lp4.topMargin + m_iOneKeyMaxHeight);
        keydata4.m_oSafeKey = sk4;
        m_vKeyDataVector.add(keydata4);

        // if (frontbit != null)
        // frontbit.recycle();
    }

    private void initNewSignals() {
        int oneKeyWidth = (m_iMaxWidth - 2 * m_iXLeftPadding - 9 * m_iXSpaceDis) / 10;

        if (m_saSignValues == null)
            return;

        RandomValue.initValues(m_saSignValues);

        Drawable drawable = null;
        if (m_iUIType == 2)
            drawable = getAssetBitmap(R.drawable.sback_2);
        else if (m_iUIType == 1 || m_iUIType == 5)
            drawable = getDrawable("shape_sback");

        if (m_saSaveSigns == null)
            m_saSaveSigns = new String[m_saSignValues.length];

        int j = 0;

        for (int i = 0; i < m_saSignValues.length; i++) {
            safeKey sk = new safeKey(getContext());
            String str = null;
            if (m_bsModeOrdereds[2]) {
                str = m_saSignValues[j];
                j++;
            } else {
                if (m_saSaveSigns[j] == null) {
                    str = RandomValue.getRandomValue();
                    m_saSaveSigns[j] = new String(str);
                } else
                    str = m_saSaveSigns[j];
                j++;
            }

            if (str == null)
                continue;

            sk.initKey(oneKeyWidth, m_iOneKeyMaxHeight, str, drawable, m_iBackColor, m_iTextColor);

            KeyData keydata = new KeyData();

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(oneKeyWidth, m_iOneKeyMaxHeight);

            if (i < 10) {
                lp.leftMargin = m_iXLeftPadding + (i * (oneKeyWidth + m_iXSpaceDis));
                lp.topMargin = m_iYDis + m_iMoveDownY;
            } else if (i < 2 * 10) {
                lp.leftMargin = m_iXLeftPadding + ((i - 10) * (oneKeyWidth + m_iXSpaceDis));
                lp.topMargin = (m_iYDis + m_iOneKeyMaxHeight + m_iYSpaceDis) + m_iMoveDownY;
            } else if (i < 3 * 10 - 2) {
                lp.leftMargin = m_iXLeftPadding + ((i - 2 * 10) * (oneKeyWidth + m_iXSpaceDis));
                lp.topMargin = (m_iYDis + 2 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;
            } else if (i < 35) {
                lp.leftMargin = m_iXLeftPadding + ((i - (3 * 10 - 2)) * (oneKeyWidth + m_iXSpaceDis));
                lp.topMargin = (m_iYDis + 3 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;
            }

            addView(sk, lp);

            keydata.m_sContent = new String(str);
            keydata.m_oRange = new Rect(lp.leftMargin, lp.topMargin, lp.leftMargin + oneKeyWidth, lp.topMargin + m_iOneKeyMaxHeight);
            keydata.m_oSafeKey = sk;
            m_vKeyDataVector.add(keydata);
        }

        Drawable backbit = null;
        Drawable frontbit = null;

        safeKey sk1 = new safeKey(getContext());

		/* 屏蔽空格键
        if (m_iUIType == 1) {
			frontbit = getAssetBitmap(isHuaShangBank()?R.drawable.spacenew_1_huashang:R.drawable.spacenew_1);
		} else if (m_iUIType == 5) {
			frontbit = getAssetBitmap(R.drawable.spacenew_5);
		}
		backbit = getDrawable("shape_sback");
		sk1.initKey(5 * oneKeyWidth + 4 * m_iXSpaceDis, m_iOneKeyMaxHeight, frontbit, backbit, m_iBackColor, m_iTextColor);
		*/

        KeyData keydata1 = new KeyData();

        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(5 * oneKeyWidth + 4 * m_iXSpaceDis, m_iOneKeyMaxHeight);

        lp1.leftMargin = m_iXLeftPadding + 2 * (oneKeyWidth + m_iXSpaceDis) + oneKeyWidth / 2;
        lp1.topMargin = (m_iYDis + 4 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;

        addView(sk1, lp1);

        keydata1.m_sContent = "空格";
        keydata1.m_oRange = new Rect(lp1.leftMargin, lp1.topMargin, lp1.leftMargin + 5 * oneKeyWidth + 4 * m_iXSpaceDis, lp1.topMargin + m_iOneKeyMaxHeight);
        keydata1.m_oSafeKey = sk1;
        m_vKeyDataVector.add(keydata1);

        RandomValue.cleanMemory();

        // if (backbit != null)
        // backbit.recycle();
    }

    private void initNumbers(int oneKeyWidth) {
        String[] values = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        RandomValue.initValues(values);

        Drawable backbit = null;
        Drawable drawable = null;
        if (m_iUIType == 2)
            backbit = getAssetBitmap(R.drawable.sback_2);
        else if (m_iUIType == 1 || m_iUIType == 5)
            drawable = getDrawable("shape_sback");

        if (m_saSaveNumbers == null)
            m_saSaveNumbers = new String[10];

        int j = 0;
        for (int i = 0; i < 10; i++) {
            safeKey sk = new safeKey(getContext());
            String str = null;
            if (m_bsModeOrdereds[0]) {
                str = values[j];
                j++;
            } else {
                if (m_saSaveNumbers[j] == null) {
                    str = RandomValue.getRandomValue();
                    m_saSaveNumbers[j] = new String(str);
                } else
                    str = m_saSaveNumbers[j];
                j++;
            }

            if (str == null)
                continue;

            if (m_iUIType == 1 || m_iUIType == 5)
                sk.initKey(oneKeyWidth, m_iOneKeyMaxHeight, str, drawable, m_iBackColor, m_iTextColor);
            else
                sk.initKey(oneKeyWidth, m_iOneKeyMaxHeight, str, backbit, m_iBackColor, m_iTextColor);

            KeyData keydata = new KeyData();

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(oneKeyWidth, m_iOneKeyMaxHeight);

            if (i < 3) {
                lp.leftMargin = (m_iScreenPadding / 2 + i * (oneKeyWidth + m_iXSpaceDis));
                lp.topMargin = m_iYDis + m_iMoveDownY;
            } else if (i < 6) {
                lp.leftMargin = (m_iScreenPadding / 2 + (i - 3) * (oneKeyWidth + m_iXSpaceDis));
                lp.topMargin = (m_iYDis + m_iOneKeyMaxHeight + m_iYSpaceDis) + m_iMoveDownY;
            } else if (i < 8) {
                lp.leftMargin = (m_iScreenPadding / 2 + (i - 6) * (oneKeyWidth + m_iXSpaceDis));
                lp.topMargin = (m_iYDis + 2 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;
            } else {
                lp.leftMargin = (m_iScreenPadding / 2 + (i - 8) * (oneKeyWidth + m_iXSpaceDis));
                lp.topMargin = (m_iYDis + 3 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;
            }

            addView(sk, lp);

            keydata.m_sContent = new String(str);
            keydata.m_oRange = new Rect(lp.leftMargin, lp.topMargin, lp.leftMargin + oneKeyWidth, lp.topMargin + m_iOneKeyMaxHeight);
            keydata.m_oSafeKey = sk;
            m_vKeyDataVector.add(keydata);
        }

        RandomValue.cleanMemory();

        // if (backbit != null)
        // backbit.recycle();
    }

    private void initCharactors(int oneKeyWidth) {
        m_bIsCapital = false;
        // String[] values = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
        // "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
        // "y", "z" };
        String[] values = {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l", "z", "x", "c", "v", "b", "n", "m"};
        RandomValue.initValues(values);

        Drawable backbit = null;
        Drawable drawable = null;
        if (m_iUIType == 2)
            backbit = getAssetBitmap(R.drawable.sback_2);
        else if (m_iUIType == 1 || m_iUIType == 5)
            drawable = getDrawable("shape_sback");

        if (m_saSaveCharactors == null)
            m_saSaveCharactors = new String[26];

        int j = 0;
        for (int i = 0; i < 26; i++) {
            safeKey sk = new safeKey(getContext());
            String str = null;
            if (m_bsModeOrdereds[1]) {
                str = values[j];
                j++;
            } else {
                if (m_saSaveCharactors[j] == null) {
                    str = RandomValue.getRandomValue();
                    m_saSaveCharactors[j] = new String(str);
                } else
                    str = m_saSaveCharactors[j];
                j++;
            }

            if (str == null)
                continue;

            if (m_iUIType == 1 || m_iUIType == 5)
                sk.initKey(oneKeyWidth, m_iOneKeyMaxHeight, str, drawable, m_iBackColor, m_iTextColor);
            else
                sk.initKey(oneKeyWidth, m_iOneKeyMaxHeight, str, backbit, m_iBackColor, m_iTextColor);

            KeyData keydata = new KeyData();

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(oneKeyWidth, m_iOneKeyMaxHeight);
            if (i < 10) {
                lp.leftMargin = m_iScreenPadding / 2 + (i * (oneKeyWidth + m_iXSpaceDis));
                lp.topMargin = m_iYDis + m_iMoveDownY;
            } else if (i < 19) {
                lp.leftMargin = m_iScreenPadding / 2 + oneKeyWidth / 2 + ((i - 10) * (oneKeyWidth + m_iXSpaceDis));
                lp.topMargin = (m_iYDis + m_iOneKeyMaxHeight + m_iYSpaceDis) + m_iMoveDownY;
            } else if (i < 26) {
                lp.leftMargin = m_iScreenPadding / 2 + oneKeyWidth * 3 / 2 + m_iXSpaceDis + ((i - 19) * (oneKeyWidth + m_iXSpaceDis));
                lp.topMargin = (m_iYDis + 2 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;
            }

            addView(sk, lp);

            keydata.m_sContent = str;
            keydata.m_oRange = new Rect(lp.leftMargin, lp.topMargin, lp.leftMargin + oneKeyWidth, lp.topMargin + m_iOneKeyMaxHeight);
            keydata.m_oSafeKey = sk;
            m_vKeyDataVector.add(keydata);
        }


        if (m_iUIType == 2)
            backbit = getAssetBitmap(R.drawable.space_2);
        else if (m_iUIType == 1 || m_iUIType == 5)
            backbit = getDrawable("shape_sback");

        safeKey sk1 = new safeKey(getContext());

        int wTmp = 0;
        if (m_iUIType == 1) {
            Drawable frontbit = null;
            wTmp = (int) (6 * oneKeyWidth + 6 * m_iXSpaceDis + oneKeyWidth * 3 / 2 + 0.5f);
            Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
            if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                if (wTmp > 1000) {//samsang s7  1056
                    frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.space_1_huashang : R.drawable.space_1);
                } else if (wTmp > 900) {//samsang pad  936
                    frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.space_1_huashang : R.drawable.space_1_pad);
                } else {
                    frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.space_1_huashang : R.drawable.space_1);
                }
            } else {
                frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.space_1_huashang : R.drawable.space_1);
            }
            sk1.initKey(wTmp, m_iOneKeyMaxHeight, frontbit, backbit, m_iBackColor, m_iTextColor);
        } else if (m_iUIType == 2) {
            wTmp = 5 * oneKeyWidth + 4 * m_iXSpaceDis;
            sk1.initKey(wTmp, m_iOneKeyMaxHeight, "空格", backbit, m_iBackColor, m_iTextColor);
        } else if (m_iUIType == 5) {
            wTmp = 5 * oneKeyWidth + 4 * m_iXSpaceDis;
            Drawable frontbit = getAssetBitmap(R.drawable.spacenew_5);
            //sk1.initKey(wTmp, m_iOneKeyMaxHeight, "︼", backbit, m_iBackColor, m_iTextColor);
            //五部空格初始显示和按键后显示效果不一样，fengwei 1124
            sk1.initKey(wTmp, m_iOneKeyMaxHeight, frontbit, backbit, m_iBackColor, m_iTextColor);
        }

        KeyData keydata1 = new KeyData();

        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(wTmp, m_iOneKeyMaxHeight);

        if (m_iUIType == 1) {
            lp1.leftMargin = m_iScreenPadding / 2;
        } else {
            lp1.leftMargin = m_iScreenPadding / 2 + 2 * (oneKeyWidth + m_iXSpaceDis) + oneKeyWidth / 2;
        }
        lp1.topMargin = (m_iYDis + 3 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;

        addView(sk1, lp1);

        keydata1.m_sContent = "空格";
        keydata1.m_oRange = new Rect(lp1.leftMargin, lp1.topMargin, lp1.leftMargin + wTmp, lp1.topMargin + m_iOneKeyMaxHeight);
        keydata1.m_oSafeKey = sk1;
        m_vKeyDataVector.add(keydata1);

        RandomValue.cleanMemory();
    }


    private void initFunctions(int oneKeyWidth) {
        int w = 3 * oneKeyWidth / 2;

        Drawable backbit = null;
        Drawable frontbit = null;
        Drawable backDrawable = null;

        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息

        if (m_iMode == 0) {
            if (m_iUIType == 2) {
                backbit = getAssetBitmap(R.drawable.cback_2);
                frontbit = getAssetBitmap(R.drawable.capslock_2);
            } else if (m_iUIType == 1) {
                backDrawable = getDrawable("shape_cback7");
                if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                    frontbit = getAssetBitmap(R.drawable.capslock_1_pad);
                } else {
                    frontbit = getAssetBitmap(R.drawable.capslock_1);
                }
            } else if (m_iUIType == 5) {
                backDrawable = getDrawable("shape_cback");
                frontbit = getAssetBitmap(R.drawable.capslock_5);
            }
            safeKey sk1 = new safeKey(getContext());
            if (m_iUIType == 1) {
                sk1.initKey(w, m_iOneKeyMaxHeight, frontbit, backDrawable, m_iBackColor, m_iFrontColor);
            } else if (m_iUIType == 5) {
                sk1.initKey(w, m_iOneKeyMaxHeight, frontbit, backDrawable, m_iBackColor, m_iFrontColor);
            } else {
                sk1.initKey(w, m_iOneKeyMaxHeight, frontbit, backbit, m_iBackColor, m_iFrontColor);
            }
            KeyData keydata1 = new KeyData();
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(w, m_iOneKeyMaxHeight);
            lp1.leftMargin = m_iScreenPadding / 2;
            lp1.topMargin = (m_iYDis + 2 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;
            addView(sk1, lp1);
            keydata1.m_sContent = new String("切换");
            keydata1.m_oRange = new Rect(lp1.leftMargin, lp1.topMargin, lp1.leftMargin + w, lp1.topMargin + m_iOneKeyMaxHeight);
            keydata1.m_oSafeKey = sk1;
            m_vKeyDataVector.add(keydata1);
        }

        if (m_iMode != 0)
            w = oneKeyWidth;
        if (m_iMode == 2) {
            if (m_iUIType == 2) {
                backbit = getAssetBitmap(R.drawable.cback_2);
                frontbit = getAssetBitmap(R.drawable.bigdelete_2);
            } else if (m_iUIType == 1) {
                backDrawable = getDrawable("shape_cback7");
                if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                    frontbit = getAssetBitmap(R.drawable.bigdeletenew_1_pad);
                } else {
                    frontbit = getAssetBitmap(R.drawable.bigdeletenew_1);
                }
            } else if (m_iUIType == 5) {
                backDrawable = getDrawable("shape_cback");
                frontbit = getAssetBitmap(R.drawable.bigdelete_5);
            }
        } else if (m_iMode == 0) {
            if (m_iUIType == 2) {
                backbit = getAssetBitmap(R.drawable.cback_2);
                frontbit = getAssetBitmap(R.drawable.delete_2);
            } else if (m_iUIType == 1) {
                backDrawable = getDrawable("shape_cback7");
                if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                    frontbit = getAssetBitmap(R.drawable.deletenew_1_pad);
                } else {
                    frontbit = getAssetBitmap(R.drawable.deletenew_1);
                }
            } else if (m_iUIType == 5) {
                backDrawable = getDrawable("shape_cback");
                frontbit = getAssetBitmap(R.drawable.delete2_5);
            }
        } else if (m_iMode == 1) {
            w = 2 * oneKeyWidth - m_iXSpaceDis - 5;// + m_iXSpaceDis-10
            if (m_iUIType == 2) {
                backbit = getAssetBitmap(R.drawable.cback_2);
                frontbit = getAssetBitmap(R.drawable.delete_2);
            } else if (m_iUIType == 1) {
                backDrawable = getDrawable("shape_cback7");
                if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                    frontbit = getAssetBitmap(R.drawable.bigdeletenew_2_pad);
                } else {
                    frontbit = getAssetBitmap(R.drawable.bigdeletenew_2);
                }
            } else if (m_iUIType == 5) {
                backDrawable = getDrawable("shape_cback");
                frontbit = getAssetBitmap(R.drawable.delete2_5);
            }
        }

        DisplayMetrics localDisplayMetrics = getContext().getApplicationContext().getResources().getDisplayMetrics();

        safeKey sk2 = new safeKey(getContext());
        if (m_iUIType == 1 || m_iUIType == 5) {
            if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                if (m_iMode == 1) {
                    if (localDisplayMetrics != null) {
                        if (localDisplayMetrics.widthPixels > localDisplayMetrics.heightPixels &&
                                localDisplayMetrics.widthPixels > 1000 && localDisplayMetrics.heightPixels < 801) {
                            sk2.initKey(w - 12, m_iOneKeyMaxHeight, frontbit, backDrawable, m_iBackColor, m_iFrontColor);
                        } else {
                            sk2.initKey(w, m_iOneKeyMaxHeight, frontbit, backDrawable, m_iBackColor, m_iFrontColor);
                        }
                    } else {
                        sk2.initKey(w, m_iOneKeyMaxHeight, frontbit, backDrawable, m_iBackColor, m_iFrontColor);
                    }
                } else {
                    sk2.initKey(w, m_iOneKeyMaxHeight, frontbit, backDrawable, m_iBackColor, m_iFrontColor);
                }
            } else {
                sk2.initKey(w, m_iOneKeyMaxHeight, frontbit, backDrawable, m_iBackColor, m_iFrontColor);
            }

        } else
            sk2.initKey(w, m_iOneKeyMaxHeight, frontbit, backbit, m_iBackColor, m_iFrontColor);

        KeyData keydata2 = new KeyData();

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(w, m_iOneKeyMaxHeight);
        if (m_iMode == 2) {
            lp2.leftMargin = (m_iScreenPadding / 2 + 2 * (oneKeyWidth + m_iXSpaceDis));
            lp2.topMargin = (m_iYDis + 2 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;
        } else if (m_iMode == 0) {
            lp2.leftMargin = m_iScreenPadding / 2 + oneKeyWidth / 2 + 8 * (oneKeyWidth + m_iXSpaceDis);
            lp2.topMargin = (m_iYDis + 2 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;
        } else {
            if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                if (localDisplayMetrics != null) {
                    if (localDisplayMetrics.widthPixels > localDisplayMetrics.heightPixels &&
                            localDisplayMetrics.widthPixels > 1000 && localDisplayMetrics.heightPixels < 801) {
                        lp2.leftMargin = m_iScreenPadding * 2 + ((m_iOneLineKeyCount - 2) * (oneKeyWidth + m_iXSpaceDis)) + m_iXSpaceDis + 2;
                    } else {
                        lp2.leftMargin = m_iScreenPadding * 2 + ((m_iOneLineKeyCount - 2) * (oneKeyWidth + m_iXSpaceDis));
                    }
                } else {
                    lp2.leftMargin = m_iScreenPadding * 2 + ((m_iOneLineKeyCount - 2) * (oneKeyWidth + m_iXSpaceDis));
                }
            } else {
                lp2.leftMargin = m_iScreenPadding * 2 + ((m_iOneLineKeyCount - 2) * (oneKeyWidth + m_iXSpaceDis));
            }

            lp2.topMargin = (m_iYDis + 2 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;
        }

        addView(sk2, lp2);

        keydata2.m_sContent = new String("退格");

        int right = lp2.leftMargin + w;
        if (localDisplayMetrics != null) {
            if (localDisplayMetrics.widthPixels > localDisplayMetrics.heightPixels &&
                    localDisplayMetrics.widthPixels > 1000 && localDisplayMetrics.heightPixels < 801) {
                right = lp2.leftMargin + w - 25;
            }
        }

        keydata2.m_oRange = new Rect(lp2.leftMargin, lp2.topMargin, right, lp2.topMargin + m_iOneKeyMaxHeight);
        keydata2.m_oSafeKey = sk2;
        m_vKeyDataVector.add(keydata2);

        RelativeLayout.LayoutParams lp3 = null;
        if (m_iMode == 0) {
            w = 2 * oneKeyWidth + m_iXSpaceDis + oneKeyWidth / 2;
            if (m_iUIType == 2) {
                backbit = getAssetBitmap(R.drawable.cback_2);
                frontbit = getAssetBitmap(R.drawable.enter_2);
            } else if (m_iUIType == 1) {
                backDrawable = getDrawable("shape_cback8");
                frontbit = getAssetBitmap(R.drawable.enter_1);
            } else if (m_iUIType == 5) {
                backDrawable = getDrawable("shape_cback");
                frontbit = getAssetBitmap(R.drawable.enter_5);
            }
            lp3 = new RelativeLayout.LayoutParams(w, m_iOneKeyMaxHeight);
            lp3.leftMargin = m_iScreenPadding / 2 + oneKeyWidth / 2 + 7 * (oneKeyWidth + m_iXSpaceDis);
            lp3.topMargin = (m_iYDis + 3 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;
        } else if (m_iMode == 1) {
            w = 3 * oneKeyWidth - m_iXSpaceDis;// 20150604
            if (m_iUIType == 2) {
                backbit = getAssetBitmap(R.drawable.cback_2);
                frontbit = getAssetBitmap(R.drawable.enter_2);
            } else if (m_iUIType == 1) {
                backDrawable = getDrawable("shape_cback8");
                frontbit = getAssetBitmap(R.drawable.enter_1);
            } else if (m_iUIType == 5) {
                backDrawable = getDrawable("shape_cback");
                frontbit = getAssetBitmap(R.drawable.enter_5);
            }


            if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                if (localDisplayMetrics != null) {
                    if (localDisplayMetrics.widthPixels > localDisplayMetrics.heightPixels &&
                            localDisplayMetrics.widthPixels > 1000 && localDisplayMetrics.heightPixels < 801) {
                        lp3 = new RelativeLayout.LayoutParams(w - 25, m_iOneKeyMaxHeight);
                        lp3.leftMargin = m_iScreenPadding / 2 + ((m_iOneLineKeyCount - 2) * (oneKeyWidth + m_iXSpaceDis)) - 91;
                    } else {
                        lp3 = new RelativeLayout.LayoutParams(w, m_iOneKeyMaxHeight);
                        lp3.leftMargin = m_iScreenPadding / 2 + ((m_iOneLineKeyCount - 2) * (oneKeyWidth + m_iXSpaceDis)) - 80;
                    }
                } else {
                    lp3 = new RelativeLayout.LayoutParams(w, m_iOneKeyMaxHeight);
                    lp3.leftMargin = m_iScreenPadding / 2 + ((m_iOneLineKeyCount - 2) * (oneKeyWidth + m_iXSpaceDis)) - 80;
                }
            } else {
                lp3 = new RelativeLayout.LayoutParams(w, m_iOneKeyMaxHeight);
                // add resolution choose from chenbing
                if (localDisplayMetrics.widthPixels < 500) {//800*480
                    lp3.leftMargin = m_iScreenPadding / 2 + ((m_iOneLineKeyCount - 2) * (oneKeyWidth + m_iXSpaceDis)) - 28;
                } else if (localDisplayMetrics.widthPixels < 800) {//sony LT26ii 1280*720
                    lp3.leftMargin = m_iScreenPadding / 2 + ((m_iOneLineKeyCount - 2) * (oneKeyWidth + m_iXSpaceDis)) - 45;
                } else {
                    lp3.leftMargin = m_iScreenPadding / 2 + ((m_iOneLineKeyCount - 2) * (oneKeyWidth + m_iXSpaceDis)) - 80;
                }
            }
            lp3.topMargin = (m_iYDis + 3 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;
        } else if (m_iMode == 2) {
            w = oneKeyWidth;
            if (m_iUIType == 2) {
                backbit = getAssetBitmap(R.drawable.cback_2);
                frontbit = getAssetBitmap(R.drawable.bigenter_2);
            } else if (m_iUIType == 1) {
                backDrawable = getDrawable("shape_cback8");
                frontbit = getAssetBitmap(R.drawable.bigenter_1);
            } else if (m_iUIType == 5) {
                backDrawable = getDrawable("shape_cback");
                // DaBing Modify this place in 2016/10/08
                // frontbit = getAssetBitmap(R.drawable.bigenter_5);
                frontbit = getAssetBitmap(R.drawable.enter_1);
            }
            lp3 = new RelativeLayout.LayoutParams(w, m_iOneKeyMaxHeight);
            lp3.leftMargin = (m_iScreenPadding / 2 + 2 * (oneKeyWidth + m_iXSpaceDis));
            lp3.topMargin = (m_iYDis + 3 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;
        }

        safeKey sk3 = new safeKey(getContext());
        //if (m_iUIType == 1 || m_iUIType == 5){
        if (m_iUIType == 1) {
            sk3.initKey(w, m_iOneKeyMaxHeight, m_sExitText, backDrawable, m_iBackColor, m_iFrontColor);
        } else if (m_iUIType == 5) {
            sk3.initKey(w, m_iOneKeyMaxHeight, frontbit, backDrawable, m_iBackColor, m_iFrontColor);
        } else {
            sk3.initKey(w, m_iOneKeyMaxHeight, frontbit, backbit, m_iBackColor, m_iFrontColor);
        }

        addView(sk3, lp3);

        KeyData keydata3 = new KeyData();

        keydata3.m_sContent = new String("退出");
        keydata3.m_oRange = new Rect(lp3.leftMargin, lp3.topMargin, lp3.leftMargin + w, lp3.topMargin + m_iOneKeyMaxHeight);
        keydata3.m_oSafeKey = sk3;
        m_vKeyDataVector.add(keydata3);
    }

    private void initSignals(int oneKeyWidth) {
        if (m_saSignValues == null)
            return;

        RandomValue.initValues(m_saSignValues);

        Drawable backbit = null;
        Drawable drawable = null;

        if (m_iUIType == 2)
            backbit = getAssetBitmap(R.drawable.sback_2);
        else if (m_iUIType == 1 || m_iUIType == 5)
            drawable = getDrawable("shape_sback");

        if (m_saSaveSigns == null)
            m_saSaveSigns = new String[32];

        int j = 0;

        for (int i = 0; i < 32; i++) {
            safeKey sk = new safeKey(getContext());
            String str = null;
            if (m_bsModeOrdereds[2]) {
                str = m_saSignValues[j];
                j++;
            } else {
                if (m_saSaveSigns[j] == null) {
                    str = RandomValue.getRandomValue();
                    m_saSaveSigns[j] = new String(str);
                } else
                    str = m_saSaveSigns[j];
                j++;
            }

            if (str == null)
                continue;

            if (m_iUIType == 1 || m_iUIType == 5)
                sk.initKey(oneKeyWidth, m_iOneKeyMaxHeight, str, drawable, m_iBackColor, m_iTextColor);
            else
                sk.initKey(oneKeyWidth, m_iOneKeyMaxHeight, str, backbit, m_iBackColor, m_iTextColor);

            KeyData keydata = new KeyData();

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(oneKeyWidth, m_iOneKeyMaxHeight);

            if (i < m_iOneLineKeyCount) {
                lp.leftMargin = m_iScreenPadding / 2 + (i * (oneKeyWidth + m_iXSpaceDis));
                lp.topMargin = m_iYDis + m_iMoveDownY;
            } else if (i < 2 * m_iOneLineKeyCount) {
                lp.leftMargin = m_iScreenPadding / 2 + ((i - m_iOneLineKeyCount) * (oneKeyWidth + m_iXSpaceDis));
                lp.topMargin = (m_iYDis + m_iOneKeyMaxHeight + m_iYSpaceDis) + m_iMoveDownY;
            } else if (i < 3 * m_iOneLineKeyCount - 2) {
                lp.leftMargin = m_iScreenPadding / 2 + ((i - 2 * m_iOneLineKeyCount) * (oneKeyWidth + m_iXSpaceDis));
                lp.topMargin = (m_iYDis + 2 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;
            } else if (i < 32) {
                lp.leftMargin = m_iScreenPadding / 2 + ((i - (3 * m_iOneLineKeyCount - 2)) * (oneKeyWidth + m_iXSpaceDis));
                lp.topMargin = (m_iYDis + 3 * (m_iOneKeyMaxHeight + m_iYSpaceDis)) + m_iMoveDownY;
            }

            addView(sk, lp);

            keydata.m_sContent = new String(str);
            keydata.m_oRange = new Rect(lp.leftMargin, lp.topMargin, lp.leftMargin + oneKeyWidth, lp.topMargin + m_iOneKeyMaxHeight);
            keydata.m_oSafeKey = sk;
            m_vKeyDataVector.add(keydata);
        }

        // RandomValue.cleanMemory();

        // if (backbit != null)
        // backbit.recycle();
    }

    private String getClickedContent(int x, int y) {
        if (m_vKeyDataVector == null || m_vKeyDataVector.size() <= 0)
            return null;

        int len = m_vKeyDataVector.size();
        for (int i = 0; i < len; i++) {
            KeyData kd = (KeyData) m_vKeyDataVector.get(i);
            if (kd != null && kd.m_oRange != null && kd.m_oRange.contains(x, y) && kd.m_oSafeKey != null) {
                if (m_bSettingAnim)
                    m_oCurrentClickedKey = kd;

                return kd.m_sContent;
            }

        }

        return null;
    }

    private boolean changeContent() {
        if (m_vKeyDataVector == null || m_vKeyDataVector.size() <= 0)
            return false;

        int len = m_vKeyDataVector.size();

        Drawable backbit = null;
        Drawable drawable = null;

        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息

        if (m_iUIType == 2)
            backbit = getAssetBitmap(R.drawable.sback_2);
        else if (m_iUIType == 1 || m_iUIType == 5)
            drawable = getDrawable("shape_sback");

        for (int i = 0; i < len; i++) {
            KeyData kd = (KeyData) m_vKeyDataVector.get(i);
            if (kd == null || kd.m_oSafeKey == null || TextUtils.isEmpty(kd.m_sContent))
                continue;

            int code = kd.m_sContent.codePointAt(0);
            if (code >= 65 && code <= 90 && m_bIsCapital) {
                char c = (char) (code + 32);
                if (m_iUIType == 1 || m_iUIType == 5) {
                    int textColor = 0;
                    if (m_bIsCallSysSoftKeyboard)
                        textColor = m_iTextColor;
                    else
                        textColor = m_iTextColor;

                    kd.m_oSafeKey.setNewContent(String.valueOf(c), drawable, m_iBackColor, textColor);
                } else
                    kd.m_oSafeKey.setNewContent(String.valueOf(c), backbit, m_iBackColor, m_iTextColor);
                kd.m_sContent = String.valueOf(c);
            } else if (code >= 97 && code <= 122 && !m_bIsCapital) {
                char c = (char) (code - 32);
                if (m_iUIType == 1 || m_iUIType == 5) {
                    int textColor = 0;
                    if (m_bIsCallSysSoftKeyboard)
                        textColor = m_iTextColor;
                    else
                        textColor = m_iTextColor;

                    kd.m_oSafeKey.setNewContent(String.valueOf(c), drawable, m_iBackColor, textColor);
                } else
                    kd.m_oSafeKey.setNewContent(String.valueOf(c), backbit, m_iBackColor, m_iTextColor);
                kd.m_sContent = String.valueOf(c);
            }

            if (!kd.m_sContent.equals("切换"))
                continue;
            Drawable backbitmap = null;
            Drawable frontbitmap = null;
            Drawable backDrawable = null;

            if (m_bIsCapital) {
                if (m_iUIType == 2) {
                    backbitmap = getAssetBitmap(R.drawable.cback_2);
                    frontbitmap = getAssetBitmap(R.drawable.capslock_2);
                } else if (m_iUIType == 1) {
                    if (m_bIsCallSysSoftKeyboard) {
                        backDrawable = getDrawable("shape_cback7");
                        if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                            frontbitmap = getAssetBitmap(R.drawable.capslock_1_pad);
                        } else {
                            frontbitmap = getAssetBitmap(R.drawable.capslock_1);
                        }
                    } else {
                        backDrawable = getDrawable("shape_cback1");
                        if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                            frontbitmap = getAssetBitmap(R.drawable.capslocknew_1_pad);
                        } else {
                            frontbitmap = getAssetBitmap(R.drawable.capslocknew_1);
                        }
                    }
                } else if (m_iUIType == 5) {
                    if (m_bIsCallSysSoftKeyboard) {
                        backDrawable = getDrawable("shape_cback");
                        frontbitmap = getAssetBitmap(R.drawable.capslock_5);
                    } else {
                        backDrawable = getDrawable("shape_cback1");
                        if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                            frontbitmap = getAssetBitmap(R.drawable.capslocknew_1_pad);
                        } else {
                            frontbitmap = getAssetBitmap(R.drawable.capslocknew_1);
                        }
                    }
                }
            } else {

                if (m_iUIType == 2) {
                    backbitmap = getAssetBitmap(R.drawable.cback_2);
                    frontbitmap = getAssetBitmap(R.drawable.chosencapslock_2);
                } else if (m_iUIType == 1) {
                    if (m_bIsCallSysSoftKeyboard) {
                        backDrawable = getDrawable("shape_cback7");
                        if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                            frontbitmap = getAssetBitmap(R.drawable.chosencapslock_1_pad);
                        } else {
                            frontbitmap = getAssetBitmap(R.drawable.chosencapslock_1);
                        }
                    } else {
                        backDrawable = getDrawable("shape_cback4");
                        if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                            frontbitmap = getAssetBitmap(R.drawable.chosencapslocknew_1_pad);
                        } else {
                            frontbitmap = getAssetBitmap(R.drawable.chosencapslocknew_1);
                        }
                    }
                } else if (m_iUIType == 5) {
					/*
					 * backbitmap = getAssetBitmap("5/cback.9.png"); frontbitmap
					 * = getAssetBitmap("5/shift-capslk.png");
					 */
                    if (m_bIsCallSysSoftKeyboard) {
                        backDrawable = getDrawable("shape_cback");
                        frontbitmap = getAssetBitmap(R.drawable.chosencapslock_5);
                    } else {
                        backDrawable = getDrawable("shape_cback4");
                        if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                            frontbitmap = getAssetBitmap(R.drawable.chosencapslocknew_1_pad);
                        } else {
                            frontbitmap = getAssetBitmap(R.drawable.chosencapslocknew_1);
                        }
                    }
                }
            }
            if (m_iUIType == 1 || m_iUIType == 5)
                kd.m_oSafeKey.setNewContent(frontbitmap, backDrawable, m_iBackColor, m_iFrontColor);
            else
                kd.m_oSafeKey.setNewContent(frontbitmap, backbitmap, m_iBackColor, m_iFrontColor);

            // if (backbitmap != null)
            // backbitmap.recycle();
            // if (frontbitmap != null)
            // frontbitmap.recycle();
        }

        // if (backbit != null)
        // backbit.recycle();
        return true;
    }

    private boolean showSign() {
        m_iMode = 1;

        changeModeColor();
        deleteKeys();

        int oneKeyWidth = (int) ((m_iMaxWidth - m_iScreenPadding - (m_iOneLineKeyCount - 1) * m_iXSpaceDis) / m_iOneLineKeyCount + 0.5f);

        // initSignals(oneKeyWidth);
        initNewSignals();
        initFunctions(oneKeyWidth);

        return true;
    }

    private boolean showNumber() {
        m_iMode = 2;

        changeModeColor();
        deleteKeys();
        int oneKeyWidth = (int) (((m_iMaxWidth - m_iScreenPadding - 2 * m_iXSpaceDis) / 3) + 0.5f);
        initNumbers(oneKeyWidth);
        initFunctions(oneKeyWidth);

        return true;
    }

    private boolean showCharactor() {
        m_iMode = 0;

        changeModeColor();
        deleteKeys();

        int oneKeyWidth = (int) (((float) (m_iMaxWidth - m_iScreenPadding - 9 * m_iXSpaceDis) / 10) + 0.5f);

        initCharactors(oneKeyWidth);
        initFunctions(oneKeyWidth);

        return true;
    }

    private boolean deleteKeys() {
        if (m_vKeyDataVector == null || m_vKeyDataVector.size() <= 0)
            return false;

        int len = m_vKeyDataVector.size();
        int count = 0;
        for (int i = 0; i < len; i++) {
            KeyData kd = (KeyData) m_vKeyDataVector.get(i);
            if (kd == null || kd.m_oSafeKey == null || TextUtils.isEmpty(kd.m_sContent))
                continue;

            if (kd.m_sContent.equals("数字") || kd.m_sContent.equals("符号") || kd.m_sContent.equals("字母") || kd.m_sContent.equals("切换系统输入法") || kd.m_sContent.equals("三角退出") || kd.m_sContent.equals("设置") || kd.m_sContent.equals("按键反馈") || kd.m_sContent.equals("模式切换"))
                continue;

            removeView(kd.m_oSafeKey);
            kd.m_oSafeKey.cleanMemory();
            kd.m_oSafeKey = null;

            count++;
        }

        for (int i = 0; i < count; i++)
            m_vKeyDataVector.removeElementAt(len - count);

        return true;
    }

    private void changeModeColor() {
        if (m_oCharactorKey == null || m_oSignKey == null || m_oNumberKey == null)
            return;

        if (m_iMode == 0) {
            if (m_iUIType == 2) {
                m_oCharactorKey.setNewContent("字母", getAssetBitmap(R.drawable.transparent_2), 0, m_iTextColor2, m_iTextShadowColor1);
                m_oSignKey.setNewContent("符号", getAssetBitmap(R.drawable.transparent_2), 0, m_iTextColor1, m_iTextShadowColor);
                m_oNumberKey.setNewContent("数字", getAssetBitmap(R.drawable.transparent_2), 0, m_iTextColor1, m_iTextShadowColor);
            } else if (m_iUIType == 1) {
                m_oCharactorKey.setNewContent("字母", getAssetBitmap(R.drawable.transparent_1), 0, m_iTextColor2, -1);
                m_oSignKey.setNewContent("符号", getAssetBitmap(R.drawable.transparent_1), 0, m_iTextColor1, -1);
                m_oNumberKey.setNewContent("数字", getAssetBitmap(R.drawable.transparent_1), 0, m_iTextColor1, -1);
            } else if (m_iUIType == 5) {
                m_oCharactorKey.setNewContent("", getAssetBitmap(R.drawable.chosencharactor_5), 0, 0, 0);
                m_oSignKey.setNewContent("", getAssetBitmap(R.drawable.sign_5), 0, 0, 0);
                m_oNumberKey.setNewContent("", getAssetBitmap(R.drawable.number_5), 0, 0, 0);
            }
        } else if (m_iMode == 1) {
            if (m_iUIType == 2) {
                m_oCharactorKey.setNewContent("字母", getAssetBitmap(R.drawable.transparent_2), 0, m_iTextColor1, m_iTextShadowColor);
                m_oSignKey.setNewContent("符号", getAssetBitmap(R.drawable.transparent_2), 0, m_iTextColor2, m_iTextShadowColor1);
                m_oNumberKey.setNewContent("数字", getAssetBitmap(R.drawable.transparent_2), 0, m_iTextColor1, m_iTextShadowColor);
            } else if (m_iUIType == 1) {
                m_oCharactorKey.setNewContent("字母", getAssetBitmap(R.drawable.transparent_1), 0, m_iTextColor1, -1);
                m_oSignKey.setNewContent("符号", getAssetBitmap(R.drawable.transparent_1), 0, m_iTextColor2, -1);
                m_oNumberKey.setNewContent("数字", getAssetBitmap(R.drawable.transparent_1), 0, m_iTextColor1, -1);
            } else if (m_iUIType == 5) {
                m_oCharactorKey.setNewContent("", getAssetBitmap(R.drawable.charactor_5), 0, 0, 0);
                m_oSignKey.setNewContent("", getAssetBitmap(R.drawable.chosensign_5), 0, 0, 0);
                m_oNumberKey.setNewContent("", getAssetBitmap(R.drawable.number_5), 0, 0, 0);
            }
        } else if (m_iMode == 2) {
            if (m_iUIType == 2) {
                m_oCharactorKey.setNewContent("字母", getAssetBitmap(R.drawable.transparent_2), 0, m_iTextColor1, m_iTextShadowColor);
                m_oSignKey.setNewContent("符号", getAssetBitmap(R.drawable.transparent_2), 0, m_iTextColor1, m_iTextShadowColor);
                m_oNumberKey.setNewContent("数字", getAssetBitmap(R.drawable.transparent_2), 0, m_iTextColor2, m_iTextShadowColor1);
            } else if (m_iUIType == 1) {
                m_oCharactorKey.setNewContent("字母", getAssetBitmap(R.drawable.transparent_1), 0, m_iTextColor1, -1);
                m_oSignKey.setNewContent("符号", getAssetBitmap(R.drawable.transparent_1), 0, m_iTextColor1, -1);
                m_oNumberKey.setNewContent("数字", getAssetBitmap(R.drawable.transparent_1), 0, m_iTextColor2, -1);
            } else if (m_iUIType == 5) {
                m_oCharactorKey.setNewContent("", getAssetBitmap(R.drawable.charactor_5), 0, 0, 0);
                m_oSignKey.setNewContent("", getAssetBitmap(R.drawable.sign_5), 0, 0, 0);
                m_oNumberKey.setNewContent("", getAssetBitmap(R.drawable.chosennumber_5), 0, 0, 0);
            }
        }
    }

    private Drawable getAssetBitmap(int Rid) {
        try {
            return getContext().getResources().getDrawable(Rid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Drawable sbackDrawable = null;
    private Drawable cbackDrawable = null;
    private Drawable cbackDrawable1 = null;
    private Drawable cbackDrawable2 = null;
    private Drawable cbackDrawable3 = null;
    private Drawable cbackDrawable4 = null;
    private Drawable cbackDrawable5 = null;
    private Drawable cbackDrawable6 = null;
    private Drawable cbackDrawable7 = null;
    private Drawable cbackDrawable8 = null;

    public Drawable getDrawable(String name) {
        try {
            if ("shape_sback".equalsIgnoreCase(name)) {
                if (sbackDrawable == null) {
                    sbackDrawable = getResources().getDrawable(R.drawable.shape_sback);
                }
                return sbackDrawable;
            } else if ("shape_cback".equalsIgnoreCase(name)) {
                if (cbackDrawable == null) {
                    cbackDrawable = getResources().getDrawable(R.drawable.shape_cback);
                }
                return cbackDrawable;
            } else if ("shape_cback1".equalsIgnoreCase(name)) {
                if (cbackDrawable1 == null) {
                    cbackDrawable1 = getResources().getDrawable(R.drawable.shape_cback1);
                }
                return cbackDrawable1;
            } else if ("shape_cback2".equalsIgnoreCase(name)) {
                if (cbackDrawable2 == null) {
                    cbackDrawable2 = getResources().getDrawable(R.drawable.shape_cback2);
                }
                return cbackDrawable2;
            } else if ("shape_cback3".equalsIgnoreCase(name)) {
                if (cbackDrawable3 == null) {
                    cbackDrawable3 = getResources().getDrawable(R.drawable.shape_cback3);
                }
                return cbackDrawable3;
            } else if ("shape_cback4".equalsIgnoreCase(name)) {
                if (cbackDrawable4 == null) {
                    cbackDrawable4 = getResources().getDrawable(R.drawable.shape_cback4);
                }
                return cbackDrawable4;
            } else if ("shape_cback5".equalsIgnoreCase(name)) {
                if (cbackDrawable5 == null) {
                    cbackDrawable5 = getResources().getDrawable(R.drawable.shape_cback5);
                }
                return cbackDrawable5;
            } else if ("shape_cback6".equalsIgnoreCase(name)) {
                if (cbackDrawable6 == null) {
                    cbackDrawable6 = getResources().getDrawable(R.drawable.shape_cback6);
                }
                return cbackDrawable6;
            } else if ("shape_cback7".equalsIgnoreCase(name)) {
                if (cbackDrawable7 == null) {
                    cbackDrawable7 = getResources().getDrawable(R.drawable.shape_cback7);
                }
                return cbackDrawable7;
            } else if ("shape_cback8".equalsIgnoreCase(name)) {
                if (cbackDrawable8 == null) {
                    cbackDrawable8 = getResources().getDrawable(R.drawable.shape_cback8);
                }
                return cbackDrawable8;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private int Dp2Px(float dp) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private int Px2Dp(float px) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    private boolean setIndication(String data, int x, int y) {
        if (TextUtils.isEmpty(data) || x < 0 || y < 0)
            return false;

        m_oShowText = new TextView(getContext());
        if (m_oShowText == null)
            return false;

        m_oShowText.setText(data);
        m_oShowText.setTextSize(25);
        m_oShowText.setBackgroundColor(Color.WHITE);
        m_oShowText.setTextColor(Color.BLACK);
        m_oShowText.setGravity(Gravity.CENTER);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(50, 50);

        lp.leftMargin = x;
        lp.topMargin = y;

        addView(m_oShowText, lp);

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (m_oInerHandler != null)
                    m_oInerHandler.sendEmptyMessage(HIDE_TEXT);
            }
        };
        timer.schedule(timerTask, 1000);

        return true;
    }

    private int getTypeCode(String str) {
        if (TextUtils.isEmpty(str))
            return 0;

        if (str.equals("€") || str.equals("£") || str.equals("¥"))
            return 4;

        int chr = str.charAt(0);
        // 数字
        if (chr >= 48 && chr <= 57)
            return 1;
        else if (chr >= 65 && chr <= 90)// 大写字母
            return 2;
        else if (chr >= 97 && chr <= 122)// 小写字母
            return 3;
        else if (chr <= 0 || chr >= 128)
            return -1;
        else
            // 符号
            return 4;

    }

    private void showSetting() {
        if (m_oSetLayout == null)
            m_oSetLayout = new RelativeLayout(getContext());

        m_oSetLayout.removeAllViews();

        Drawable bit = getAssetBitmap(R.drawable.setback_2);
        m_oSetLayout.setBackgroundDrawable(bit);

        m_oSetLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

            }

        });

        int w = m_iMaxWidth / 2;
        int h = m_iMaxHeight / 3;
        TextView tv = new TextView(getContext());
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(w - w / 20, h / 3);
        lp1.leftMargin = w / 20;
        lp1.topMargin = h / 15;
        tv.setTextSize(16);
        tv.setTextColor(Color.WHITE);
        tv.setText("设置");
        tv.setGravity(Gravity.CENTER_VERTICAL);
        m_oSetLayout.addView(tv, lp1);

        TextView tv1 = new TextView(getContext());
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(w - w / 15, 1);
        lp2.leftMargin = w / 25;
        lp2.topMargin = h / 15 + h / 3;
        tv1.setBackgroundColor(Color.WHITE);
        m_oSetLayout.addView(tv1, lp2);

        int h1 = h - h / 15 - h / 3 - 1 - 10;
        TextView tv2 = new TextView(getContext());
        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(w / 2 - w / 20, h1);
        lp3.leftMargin = w / 20;
        lp3.topMargin = h / 15 + h / 3 + 1 + 5;
        tv2.setTextSize(16);
        tv2.setTextColor(Color.WHITE);
        tv2.setText("按键提示");
        tv2.setGravity(Gravity.CENTER_VERTICAL);
        m_oSetLayout.addView(tv2, lp3);

        tv2.setOnClickListener(new TextView.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!m_bSettingAnim) {
                    m_oSetStatText.setText("开");
                    m_oSetStatText.setTextColor(m_iAnimOpenColor);

                    m_bSettingAnim = true;
                } else {
                    m_oSetStatText.setText("关");
                    m_oSetStatText.setTextColor(m_iTextColor2);

                    m_bSettingAnim = false;
                }

            }

        });

        if (m_oSetStatText == null)
            m_oSetStatText = new TextView(getContext());

        RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(w / 2 - w / 20, h1);
        lp4.leftMargin = w / 2;
        lp4.topMargin = h / 15 + h / 3 + 1 + 5;
        m_oSetStatText.setTextSize(16);
        if (!m_bSettingAnim) {
            m_oSetStatText.setText("关");
            m_oSetStatText.setTextColor(m_iTextColor2);
        } else {
            m_oSetStatText.setText("开");
            m_oSetStatText.setTextColor(m_iAnimOpenColor);
        }
        m_oSetStatText.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        m_oSetLayout.addView(m_oSetStatText, lp4);

        m_oSetStatText.setOnClickListener(new TextView.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!m_bSettingAnim) {
                    m_oSetStatText.setText("开");
                    m_oSetStatText.setTextColor(m_iAnimOpenColor);

                    m_bSettingAnim = true;
                } else {
                    m_oSetStatText.setText("关");
                    m_oSetStatText.setTextColor(m_iTextColor2);

                    m_bSettingAnim = false;
                }

            }

        });

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w, h);
        lp.leftMargin = m_iSetPicMiddleX - (m_iMaxWidth / 2 / 5);
        lp.topMargin = m_iYDis;
        addView(m_oSetLayout, lp);
    }

    private void sendAMessage(int param1, int param2) {
        Message msg = new Message();
        msg.what = param1;
        msg.arg1 = param2;
        if (m_oOutHandler != null)
            m_oOutHandler.sendMessage(msg);
        else if (m_oHandler != null)
            m_oHandler.sendMessage(msg);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = 0.0F;
        float y = 0.0F;
        switch (event.getAction() & 0xFF) {
            case MotionEvent.ACTION_DOWN:
                if (m_bIsSettingShow) {
                    if (m_oSetLayout != null)
                        removeView(m_oSetLayout);

                    m_bIsSettingShow = false;

                    return super.onTouchEvent(event);
                }
                if (m_bShowAnim)
                    return super.onTouchEvent(event);
                x = event.getX();
                y = event.getY();
                String content = getClickedContent((int) x, (int) y);
                if (content != null) {
                    if (content.equals("按键反馈"))
                        return super.onTouchEvent(event);
                    if (content.equals("切换")) {
                        if (m_bIsCapital) {
                            changeContent();
                            m_bIsCapital = false;
                        } else {
                            changeContent();
                            m_bIsCapital = true;
                        }
                        m_iCurrentKeyType = -1;

                        if (m_bIsCapital)
                            sendAMessage(CAPS_DOWN, 1);
                        else
                            sendAMessage(CAPS_DOWN, 0);

                        vibrateMe(vibratorTimes);
                    } else if (content.equals("数字")) {
                        if (m_iMode == 2)
                            break;
                        showNumber();
                        m_iCurrentKeyType = -1;
                    } else if (content.equals("字母")) {
                        if (m_iMode == 0)
                            break;
                        showCharactor();
                        m_iCurrentKeyType = -1;
                    } else if (content.equals("符号")) {
                        if (m_iMode == 1)
                            break;
                        showSign();
                        m_iCurrentKeyType = -1;
                    } else if (content.equals("设置")) {
                        if (!m_bIsCallSysSoftKeyboard) {
                            if (m_iUIType == 2) {
                                m_bIsSettingShow = true;
                                showSetting();
                                m_iCurrentKeyType = -1;
                            } else {
                                if (m_oSetKey != null) {
                                    if (!m_bSettingAnim) {
                                        Drawable bit = getAssetBitmap(R.drawable.setopennew_1);
                                        m_oSetKey.setNewContent(null, bit, 0, 0, 0);
                                        m_bSettingAnim = true;
                                        m_iCurrentKeyType = -1;
                                    } else {
                                        Drawable bit = getAssetBitmap(R.drawable.setclosenew);
                                        m_oSetKey.setNewContent(null, bit, 0, 0, 0);
                                        m_bSettingAnim = false;
                                    }
                                }

                            }

                            if (m_bSettingAnim)
                                sendAMessage(ANIM_DOWN, 1);
                            else
                                sendAMessage(ANIM_DOWN, 0);
                        }
                    } else if (content.equals("模式切换")) {
                        vibrateMe(vibratorTimes);
                        if (m_iMode == 1) {
                            deleteKeys();
                            initNewSignals();
                            initNewFunctions(2);
                            m_iMode = 2;
                        } else if (m_iMode == 2) {
                            deleteKeys();
                            initNewNumbers();
                            initNewCharactors();
                            initNewFunctions(1);
                            m_iMode = 1;
                        }

                        sendAMessage(CHANGEMODE_DOWN, m_iMode);

                    } else if (m_oOutHandler != null) {
                        Message msg = new Message();
                        msg.what = KEY_DOWN;
                        msg.arg1 = m_iSendId;

                        TransferData td = new TransferData();
                        td.m_iX = x;
                        td.m_iY = y;

                        if (content.equals("空格"))
                            content = " ";

                        td.m_iType = getTypeCode(content);
                        if (m_bSettingAnim)
                            m_iCurrentKeyType = td.m_iType;

                        if (!TextUtils.isEmpty(m_sKey) && content.length() == 1 && safeHandle.getLoadLibraryState()) {
                            if (m_iCurrentPos > m_iMaxLen) {
                                if (!m_bSettingAnim) {
                                    if (m_iUIType == 1 || m_iUIType == 5)
                                        vibrateMe(vibratorTimes);
                                } else {
                                    if (m_oCurrentClickedKey != null) {
                                        vibrateMe(vibratorTimes);
                                        processAnim(1, content);
                                    }
                                }
                                return super.onTouchEvent(event);
                            }
                            // Del:2016.5.7 MaKai-[Add encryption of 3DES] Start
                            //td.m_sContent = Encrypt.getMapping(m_iCurrentPos, content);
                            // Del:2016.5.7 MaKai-[Add encryption of 3DES] End
                            // Add:2016.5.7 MaKai-[Add encryption of 3DES] Start
                            /*if (0 == this.m_iUserFlag) {
                                td.m_sContent = Encrypt.getMapping(m_iCurrentPos, content);
                            } else {
                                td.m_sContent = Encrypt.addStr(getContext(), content);
                            }*/
                            // Add:2016.5.7 MaKai-[Add encryption of 3DES] End
                            //需要回显时，传出明文
                            if (m_bShowInputText) {
                                td.m_dContent = content;
                            }
                            m_iCurrentPos++;
                        } else {
                            td.m_sContent = new String(content);
                            //需要回显时，传出明文
                            if (m_bShowInputText) {
                                td.m_dContent = content;
                            }
                            /*if (safeHandle.getLoadLibraryState())
                                td.m_iX = Encrypt.getCodeLenByPos(m_iCurrentPos - 1);*/
                            if (m_iCurrentPos > 1)
                                m_iCurrentPos--;
                        }

                        msg.obj = td;

                        if (!content.equals("退出") && !content.equals("三角退出")) {
                            if (!m_bSettingAnim) {
                                if (m_iUIType == 1 || m_iUIType == 5)
                                    vibrateMe(vibratorTimes);
                            } else {
                                if (m_oCurrentClickedKey != null) {
                                    vibrateMe(vibratorTimes);
                                    processAnim(1, content);
                                }
                            }
                        } else {
                            if (m_bSettingAnim)
                                td.m_iX = 1;
                            else
                                td.m_iX = 0;

                            if (m_iUIType == 1 || m_iUIType == 5)
                                vibrateMe(vibratorTimes);

                            Editor sharedata = getContext().getSharedPreferences("safeinput", Activity.MODE_PRIVATE).edit();
                            if (sharedata != null) {
                                sharedata.putBoolean("animstat", m_bSettingAnim);
                                sharedata.commit();
                            }
                        }

                        m_oOutHandler.sendMessage(msg);
                    } else {
                        if (m_oHandler == null)
                            break;

                        Message msg = new Message();
                        msg.what = KEY_DOWN;

                        TransferData td = new TransferData();
                        td.m_iX = x;
                        td.m_iY = y;

                        // if( !content.equals("退格") && !content.equals("退出") )
                        // setIndication( content, (int)x, (int)y );

                        if (content.equals("空格"))
                            content = " ";

                        td.m_iType = getTypeCode(content);

                        if (m_bSettingAnim)
                            m_iCurrentKeyType = td.m_iType;

                        if (!TextUtils.isEmpty(m_sKey) && content.length() == 1 && safeHandle.getLoadLibraryState()) {
                            if (m_iCurrentPos > m_iMaxLen) {
                                if (!m_bSettingAnim) {
                                    if (m_iUIType == 1 || m_iUIType == 5)
                                        vibrateMe(vibratorTimes);
                                } else {
                                    if (m_oCurrentClickedKey != null) {
                                        vibrateMe(vibratorTimes);
                                        processAnim(1, content);
                                    }

                                }
                                return super.onTouchEvent(event);
                            }
                            // Del:2016.5.7 MaKai-[Add encryption of 3DES] Start
                            //td.m_sContent = Encrypt.getMapping(m_iCurrentPos, content);
                            // Del:2016.5.7 MaKai-[Add encryption of 3DES] End
                            // Add:2016.5.7 MaKai-[Add encryption of 3DES] Start
                            /*if (0 == this.m_iUserFlag) {
                                td.m_sContent = Encrypt.getMapping(m_iCurrentPos, content);
                            } else {
                                td.m_sContent = Encrypt.addStr(getContext(), content);
                            }*/
                            // Add:2016.5.7 MaKai-[Add encryption of 3DES] End
                            //需要回显时，传出明文
                            if (m_bShowInputText) {
                                td.m_dContent = content;
                            }
                            m_iCurrentPos++;

                        } else {
                            td.m_sContent = new String(content);
                            //需要回显时，传出明文
                            if (m_bShowInputText) {
                                td.m_dContent = content;
                            }
                            /*if (safeHandle.getLoadLibraryState())
                                td.m_iX = Encrypt.getCodeLenByPos(m_iCurrentPos - 1);*/
                            if (m_iCurrentPos > 1)
                                m_iCurrentPos--;
                        }

                        msg.obj = td;

                        if (!content.equals("退出") && !content.equals("三角退出")) {
                            if (!m_bSettingAnim) {
                                if (m_iUIType == 1 || m_iUIType == 5)
                                    vibrateMe(vibratorTimes);
                            } else {
                                vibrateMe(vibratorTimes);
                                processAnim(1, content);
                            }
                        } else {
                            if (m_bSettingAnim)
                                td.m_iX = 1;
                            else
                                td.m_iX = 0;

                            if (m_iUIType == 1 || m_iUIType == 5)
                                vibrateMe(vibratorTimes);

                            if (content.equals("退出"))
                                processAnim(1, content);

                            Editor sharedata = getContext().getSharedPreferences("safeinput", Activity.MODE_PRIVATE).edit();
                            if (sharedata != null) {
                                sharedata.putBoolean("animstat", m_bSettingAnim);
                                sharedata.commit();
                            }
                        }

                        m_oHandler.sendMessage(msg);
                    }
                } else {
                    if (y >= 0.0F)
                        break;
                    if (m_oOutHandler != null) {
                        Message msg = new Message();
                        msg.what = KEY_DOWN;
                        msg.arg1 = m_iSendId;

                        TransferData td = new TransferData();
                        td.m_iX = x;
                        td.m_iY = y;

                        td.m_sContent = new String("空白退出");

                        if (m_bSettingAnim)
                            td.m_iX = 1;
                        else
                            td.m_iX = 0;

                        msg.obj = td;

                        Editor sharedata = getContext().getSharedPreferences("safeinput", Activity.MODE_PRIVATE).edit();
                        if (sharedata != null) {
                            sharedata.putBoolean("animstat", m_bSettingAnim);
                            sharedata.commit();
                        }

                        m_oOutHandler.sendMessage(msg);
                    } else {
                        if (m_oHandler == null)
                            break;
                        Message msg = new Message();
                        msg.what = KEY_DOWN;
                        msg.arg1 = m_iSendId;

                        TransferData td = new TransferData();
                        td.m_iX = x;
                        td.m_iY = y;

                        td.m_sContent = new String("空白退出");

                        if (m_bSettingAnim)
                            td.m_iX = 1;
                        else
                            td.m_iX = 0;

                        msg.obj = td;

                        Editor sharedata = getContext().getSharedPreferences("safeinput", Activity.MODE_PRIVATE).edit();
                        if (sharedata != null) {
                            sharedata.putBoolean("animstat", m_bSettingAnim);
                            sharedata.commit();
                        }

                        m_oHandler.sendMessage(msg);
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                if (m_bSettingAnim && m_bShowAnim) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            processAnim(2, null);
                            if (m_oIndicateText != null) {
                                removeView(m_oIndicateText);
                                // m_oIndicateText.setBackgroundDrawable(null);
                            }

                            if (m_oIndicateTextNew != null) {
                                removeView(m_oIndicateTextNew);
                                // m_oIndicateTextNew.setBackgroundDrawable(null);
                            }

                        }

                    }, 50);
                }

                break;
            default:
                break;

        }

        return super.onTouchEvent(event);
    }

    private void processAnim(int type, String content) {
        if (m_oCurrentClickedKey == null)
            return;

        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息

        if (type == 1) {
            //数字
            if (m_iCurrentKeyType == 1) {
                if (m_iUIType == 2) {
                    Drawable backbit = getAssetBitmap(R.drawable.sbackc_2);
                    m_oCurrentClickedKey.m_oSafeKey.setNewContent(m_oCurrentClickedKey.m_sContent, backbit, 0, m_iTextColor);
                } else {
                    Drawable drawable = getDrawable("shape_cback5");
                    m_oCurrentClickedKey.m_oSafeKey.setNewContent(m_oCurrentClickedKey.m_sContent, drawable, 0, m_iTextColor);
                    if (!m_bIsCallSysSoftKeyboard) {
                        showIndicateTextNew(content);// 20151124 fengwei
                    }
                }
            } else if (m_oCurrentClickedKey.m_sContent.equals("空格")) {
                if (m_iUIType == 2) {
                    Drawable backbit = getAssetBitmap(R.drawable.spacec_2);
                    m_oCurrentClickedKey.m_oSafeKey.setNewContent(m_oCurrentClickedKey.m_sContent, backbit, 0, m_iTextColor);
                    showIndicateText(content);
                } else {
                    Drawable backbit = getDrawable("shape_cback5");
                    Drawable frontbit = null;
                    if (m_iUIType == 1) {

                        int width = m_oCurrentClickedKey.m_oSafeKey.getWidth();
                        if (m_bIsCallSysSoftKeyboard) {

                            if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                                if (width > 1000) {//samsang s7
                                    frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.space_1_huashang : R.drawable.space_1);
                                } else if (width > 900) {//samsang pad
                                    frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.space_1_huashang : R.drawable.space_1_pad);
                                } else {
                                    frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.space_1_huashang : R.drawable.space_1);
                                }
                            } else {
                                frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.space_1_huashang : R.drawable.space_1);
                            }
                            m_oCurrentClickedKey.m_oSafeKey.setNewContent(frontbit, backbit, 0, 0);

                            //showIndicateTextNew(content);// 20150415
                        } else {
                            if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                                if (width > 600) {
                                    frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.spacenew_1_huashang : R.drawable.space_2_pad);
                                } else {
                                    frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.spacenew_1_huashang : R.drawable.spacenew_1);
                                }
                            } else {
                                frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.spacenew_1_huashang : R.drawable.spacenew_1);
                            }

                            m_oCurrentClickedKey.m_oSafeKey.setNewContent(frontbit, backbit, 0, 0);
                            //showIndicateTextNew(content);// 20151124 fengwei
                        }
                    } else if (m_iUIType == 5) {
                        frontbit = getAssetBitmap(R.drawable.spacenew_5);
                        m_oCurrentClickedKey.m_oSafeKey.setNewContent(frontbit, backbit, 0, 0);
                    }
                }
            } else if (m_oCurrentClickedKey.m_sContent.equals("退格")) {
                if (m_iUIType == 1 || m_iUIType == 5) {
                    Drawable backbit = getDrawable("shape_cback5");
                    Drawable frontbit = null;
                    if (!m_bIsCallSysSoftKeyboard) {
                        if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                            frontbit = getAssetBitmap(R.drawable.deletenew_1_pad);
                        } else {
                            frontbit = getAssetBitmap(R.drawable.deletenew_1);
                        }
                    } else {
                        if (m_iMode == 2) {
                            if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                                frontbit = getAssetBitmap(R.drawable.bigdeletenew_1_pad);
                            } else {
                                frontbit = getAssetBitmap(R.drawable.bigdeletenew_1);
                            }
                        } else if (m_iMode == 1) {
                            if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                                frontbit = getAssetBitmap(R.drawable.bigdeletenew_2_pad);
                            } else {
                                frontbit = getAssetBitmap(R.drawable.bigdeletenew_2);
                            }
                        } else {
                            if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                                frontbit = getAssetBitmap(R.drawable.deletenew_1_pad);
                            } else {
                                frontbit = getAssetBitmap(R.drawable.deletenew_1);
                            }
                        }
                    }
                    m_oCurrentClickedKey.m_oSafeKey.setNewContent(frontbit, backbit, 0, 0);
                }
            } else if (m_oCurrentClickedKey.m_sContent.equals("退出")) {
                Drawable backbit = getDrawable("shape_cback6");
                if (m_iUIType == 1) {
                    m_oCurrentClickedKey.m_oSafeKey.setNewContent(m_sExitText, backbit, m_iBackColor, m_iFrontColor);
                } else if (m_iUIType == 5) {
                    Drawable frontDrawable = getAssetBitmap(R.drawable.bigenter_5);
                    m_oCurrentClickedKey.m_oSafeKey.setNewContent(frontDrawable, backbit, -1, -1);
                }
            } else if (m_oCurrentClickedKey.m_sContent.equals("模式切换")) {
                if (m_iUIType == 1 || m_iUIType == 5) {
                    Drawable backbit = getDrawable("shape_cback5");
                    Drawable frontbit = null;
                    if (m_iMode == 1)
                        frontbit = getAssetBitmap(R.drawable.signnew_1);
                    else if (m_iMode == 2)
                        frontbit = getAssetBitmap(R.drawable.numchanew_1);
                    m_oCurrentClickedKey.m_oSafeKey.setNewContent(frontbit, backbit, 0, 0);
                }
            } else if (m_iCurrentKeyType != -1) {
                if (m_iUIType == 2) {
                    Drawable backbit = getAssetBitmap(R.drawable.sbackc_2);
                    m_oCurrentClickedKey.m_oSafeKey.setNewContent(m_oCurrentClickedKey.m_sContent, backbit, 0, m_iTextColor);
                    showIndicateText(content);
                } else {
                    Drawable drawable = getDrawable("shape_cback5");
                    m_oCurrentClickedKey.m_oSafeKey.setNewContent(m_oCurrentClickedKey.m_sContent, drawable, 0, m_iTextColor);
                    showIndicateTextNew(content);// 20150415
                }
            }

            m_bShowAnim = true;
        } else if (type == 2) {
            if (m_iCurrentKeyType == 1) {
                if (m_iUIType == 2) {
                    Drawable backbit = getAssetBitmap(R.drawable.sback_2);
                    m_oCurrentClickedKey.m_oSafeKey.setNewContent(m_oCurrentClickedKey.m_sContent, backbit, 0, m_iTextColor);
                    // if( m_oIndicateText != null )
                    // removeView(m_oIndicateText);
                } else {
                    Drawable drawable = getDrawable("shape_sback");
                    m_oCurrentClickedKey.m_oSafeKey.setNewContent(m_oCurrentClickedKey.m_sContent, drawable, 0, m_iTextColor);
                }
            } else if (m_oCurrentClickedKey.m_sContent.equals("空格")) {
                if (m_iUIType == 2) {
                    Drawable backbit = getAssetBitmap(R.drawable.space_2);
                    m_oCurrentClickedKey.m_oSafeKey.setNewContent(m_oCurrentClickedKey.m_sContent, backbit, 0, m_iTextColor);
                    if (m_oIndicateText != null) {
                        removeView(m_oIndicateText);
                        // m_oIndicateText.setBackgroundDrawable(null);
                    }
                    if (m_oIndicateTextNew != null) {
                        removeView(m_oIndicateTextNew);
                        // m_oIndicateTextNew.setBackgroundDrawable(null);
                    }
                } else {
                    Drawable backbit = getDrawable("shape_sback");
                    Drawable frontbit = null;
                    if (m_iUIType == 1) {
                        int nWidth = m_oCurrentClickedKey.m_oSafeKey.getWidth();
                        if (m_bIsCallSysSoftKeyboard) {
                            if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                                if (nWidth > 1000) {//samsang s6s7
                                    frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.space_1_huashang : R.drawable.space_1);
                                } else if (nWidth > 900) {//samsang pad
                                    frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.space_1_huashang : R.drawable.space_1_pad);
                                } else {
                                    frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.space_1_huashang : R.drawable.space_1);
                                }
                            } else {
                                frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.space_1_huashang : R.drawable.space_1);
                            }

                            m_oCurrentClickedKey.m_oSafeKey.setNewContent(frontbit, backbit, 0, 0);
                        } else {
                            if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                                if (nWidth > 600) {
                                    frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.spacenew_1_huashang : R.drawable.space_2_pad);
                                } else {
                                    frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.spacenew_1_huashang : R.drawable.spacenew_1);
                                }
                            } else {
                                frontbit = getAssetBitmap(isHuaShangBank() ? R.drawable.spacenew_1_huashang : R.drawable.spacenew_1);
                            }
                            m_oCurrentClickedKey.m_oSafeKey.setNewContent(frontbit, backbit, 0, 0);
                        }
                    } else if (m_iUIType == 5) {
                        frontbit = getAssetBitmap(R.drawable.spacenew_5);
                        m_oCurrentClickedKey.m_oSafeKey.setNewContent(frontbit, backbit, 0, 0);
                    }
                }
            } else if (m_oCurrentClickedKey.m_sContent.equals("退格")) {

                if (m_iUIType == 1 || m_iUIType == 5) {
                    Drawable backbit = getDrawable("shape_cback1");
                    Drawable frontbit = null;
                    if (!m_bIsCallSysSoftKeyboard) {
                        if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                            frontbit = getAssetBitmap(R.drawable.deletenew_1_pad);
                        } else {
                            frontbit = getAssetBitmap(R.drawable.deletenew_1);
                        }
                    } else {
                        if (m_iMode == 2) {
                            if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                                frontbit = getAssetBitmap(R.drawable.bigdeletenew_1_pad);
                            } else {
                                frontbit = getAssetBitmap(R.drawable.bigdeletenew_1);
                            }
                        } else if (m_iMode == 1) {
                            if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                                frontbit = getAssetBitmap(R.drawable.bigdeletenew_2_pad);
                            } else {
                                frontbit = getAssetBitmap(R.drawable.bigdeletenew_2);
                            }
                        } else {
                            if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                                frontbit = getAssetBitmap(R.drawable.deletenew_1_pad);
                            } else {
                                frontbit = getAssetBitmap(R.drawable.deletenew_1);
                            }
                        }
                    }

                    m_oCurrentClickedKey.m_oSafeKey.setNewContent(frontbit, backbit, 0, 0);
                }

            } else if (m_oCurrentClickedKey.m_sContent.equals("模式切换")) {
                if (m_iUIType == 1 || m_iUIType == 5) {
                    Drawable backbit = getDrawable("shape_sback");
                    DisplayMetrics localDisplayMetrics = getContext().getApplicationContext().getResources().getDisplayMetrics();
                    Drawable frontbit = null;
                    if (m_iMode == 1) {
                        if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                            if (localDisplayMetrics != null) {
                                if (localDisplayMetrics.widthPixels > localDisplayMetrics.heightPixels &&
                                        localDisplayMetrics.widthPixels > 1000 && localDisplayMetrics.heightPixels < 801) {//Samsung:1280w*800h
                                    frontbit = getAssetBitmap(R.drawable.signnew_1_pad);
                                }
                            }
                            if (frontbit == null) {
                                frontbit = getAssetBitmap(R.drawable.signnew_1);
                            }
                        } else {
                            frontbit = getAssetBitmap(R.drawable.signnew_1);
                        }
                    } else if (m_iMode == 2) {
                        frontbit = getAssetBitmap(R.drawable.numchanew_1);
                    }
                    m_oCurrentClickedKey.m_oSafeKey.setNewContent(frontbit, backbit, 0, 0);
                }
            } else if (m_iCurrentKeyType > 1) {
                if (m_iUIType == 2) {
                    Drawable backbit = getAssetBitmap(R.drawable.sback_2);
                    m_oCurrentClickedKey.m_oSafeKey.setNewContent(m_oCurrentClickedKey.m_sContent, backbit, 0, m_iTextColor);
                    if (m_oIndicateText != null) {
                        removeView(m_oIndicateText);
                        // m_oIndicateText.setBackgroundDrawable(null);
                    }
                    if (m_oIndicateTextNew != null) {
                        removeView(m_oIndicateTextNew);
                        // m_oIndicateTextNew.setBackgroundDrawable(null);
                    }
                } else {
                    Drawable drawable = getDrawable("shape_sback");
                    m_oCurrentClickedKey.m_oSafeKey.setNewContent(m_oCurrentClickedKey.m_sContent, drawable, 0, m_iTextColor);
                }
            }

            m_bShowAnim = false;
        }

    }

    private void showIndicateText(String content) {
        if (TextUtils.isEmpty(content))
            return;

        if (m_oIndicateText == null) {
            m_oIndicateText = new TextView(getContext());
            // TextPaint tp = m_oIndicateText.getPaint();
            // tp.setFakeBoldText(true);
            m_oIndicateText.setTextColor(Color.BLACK);
            m_oIndicateText.setGravity(Gravity.CENTER);
            Drawable bit = getAssetBitmap(R.drawable.indicateback_2);
            m_oIndicateText.setBackgroundDrawable(bit);
        }

        int w = m_oCurrentClickedKey.m_oRange.width() * 3;
        int h = m_oCurrentClickedKey.m_oRange.height();

        int x = m_oCurrentClickedKey.m_oRange.left - m_oCurrentClickedKey.m_oRange.width();
        int y = m_oCurrentClickedKey.m_oRange.top - h;

        if (content.equals(" ")) {
            w = m_oCurrentClickedKey.m_oRange.width();
            x = m_oCurrentClickedKey.m_oRange.left;
            content = "空格";
        }

        if (x < 0)
            x = 0;

        if (y < 0) {
            h = h + y;
            y = 0;
            m_oIndicateText.setTextSize(20);
        } else
            m_oIndicateText.setTextSize(30);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w, h);
        lp.leftMargin = x;
        lp.topMargin = y;
        m_oIndicateText.setText(content);
        addView(m_oIndicateText, lp);
    }

    public boolean isHuaShangBank() {
		/*
		 * try{ Class<?>
		 * obj_class=Class.forName("com.icbc.application.Constants"); Field
		 * field=obj_class.getDeclaredField("VERSIONTYPE");
		 * field.setAccessible(true); String value=field.get(null).toString();
		 * Log.e("CHENHUI", "value="+value);
		 * if("MercantileBank".equalsIgnoreCase(value)){ return true; }else{
		 * return false; } }catch(Exception e){ Log.e("CHENHUI", "e="+e);
		 * e.printStackTrace(); } return false;
		 */
        String packagename = getContext().getPackageName();

        if ("com.icbc.mercantilebank".equalsIgnoreCase(packagename)) {
            return true;
        } else {
            return false;
        }
    }

    private void showIndicateTextNew(String content) {
        if (TextUtils.isEmpty(content))
            return;

        if (m_oIndicateTextNew == null) {
            m_oIndicateTextNew = new TextView(getContext());
            // TextPaint tp = m_oIndicateText.getPaint();
            // tp.setFakeBoldText(true);
            m_oIndicateTextNew.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
            Drawable bit = getAssetBitmap(R.drawable.safe_touch);
            m_oIndicateTextNew.setBackgroundDrawable(bit);
            m_oIndicateTextNew.setTextSize(40);
            m_oIndicateTextNew.setTextColor(m_iTextColor);
            m_oIndicateTextNew.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
        // Drawable bit1 = getAssetBitmap(R.drawable.safe_touch);
        // int w = m_oCurrentClickedKey.m_oRange.width() ;
        // int h = m_oCurrentClickedKey.m_oRange.height()* 2 ;
        int w = m_oCurrentClickedKey.m_oRange.width() + 55;
        int h = m_oCurrentClickedKey.m_oRange.height() * 2;

        int x = m_oCurrentClickedKey.m_oRange.left - 25;// -
        // m_oCurrentClickedKey.m_oRange.width()/2
        // int y = m_oCurrentClickedKey.m_oRange.top - h - Dp2Px(2);
        int y = m_oCurrentClickedKey.m_oRange.bottom - h;

        if (content.equals(" ") || content.equals("退格") || content.equals("退出")) {
            // w = m_oCurrentClickedKey.m_oRange.width();
            // x = m_oCurrentClickedKey.m_oRange.left;
            // content = "空格";
            return;
        }

        // if( y < 0 )
        // {
        // h = h + y;
        // y = 0;
        // }
        //
        // if (x < 0)
        // x = 0;
        //
        // if (y < 0) {
        // h = h + y;
        // y = 0;
        // m_oIndicateText.setTextSize(30);
        // } else
        // m_oIndicateText.setTextSize(30);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w, h);
        lp.leftMargin = x;
        lp.topMargin = y;
        //适配最右侧按键
        if (m_iMaxWidth - m_oCurrentClickedKey.m_oRange.right < 20) {
            lp.rightMargin = m_oCurrentClickedKey.m_oRange.right + 25;
        }

        m_oIndicateTextNew.setText(content);
        addView(m_oIndicateTextNew, lp);
    }

}
