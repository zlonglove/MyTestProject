package im.icbc.cn.keyboard.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import im.icbc.cn.keyboard.R;
import im.icbc.cn.keyboard.demo.catchInfo;
import im.icbc.cn.keyboard.safeSoftKeyBoard;

public class serviceUtils {
	private static Context m_oCtx = null;
	private static Handler m_oOutHandler = null;
	private static String m_sKey = null;
	private static int m_iSendId = 0;
	private static boolean m_bIsBindActivity = false;

	private static Activity m_oParent = null;
	private static safeSoftKeyBoard m_oKeyBoard = null;
	private static boolean m_bFullUI = true;
	private static boolean m_bShowInputText = false;
	private static int m_iDefaultKeyboard = 0; //默认显示的键盘 0：字母，1：符号，2：数字
	private static boolean isOverSeas = false;
	
	private static boolean m_bShowSysSoftKeyBoardKey = false;	//在可切换系统软键盘的键盘样式中默认显示切换系统键盘按键
	// Add:2016.5.7 MaKai-[Add encryption of 3DES] Start
	//0:手机银行调用
	//1:U盾厂商调用
	private static int m_iUserFlag = 0;

	public static void setUserFlag(int userFlag) {
		m_iUserFlag = userFlag;
	}
	// Add:2016.5.7 MaKai-[Add encryption of 3DES] End	
	// Add:2016.6.6 MaKai-[SM2&SM4&openssl] Start
	//0:非国密
	//1:国密
	private static int m_iEncryptType = 0;
	public static void setEncryptType(int encryptType) {
		m_iEncryptType = encryptType;
	}
	// Add:2016.6.6 MaKai-[SM2&SM4&openssl] End

	public static void setFullUIFlag(boolean flag){
		m_bFullUI = flag;
	}
	
	public static void setShowInputTextFlag(boolean flag) {
		m_bShowInputText = flag;
	}
	
	public static void setDefaultKeyboard(int flag) {
		m_iDefaultKeyboard = flag;
	}
	
	/**
	 * 指定配置文件
	 * @param name 文件名称
	 */
	public static void setIniName(String name){
		if(!TextUtils.isEmpty(name)){
			//Encrypt.setIniName(name);
		}
	}
	
	/**
	 * 指定是否调用了海外版安全键盘
	 * @param isOS
	 */
	public static void setOverSeasCall(boolean isOS){
		isOverSeas = isOS;
	}
	
	public static void setBindActivity(boolean state) {
		if(state) {
			m_oCtx = null;
			m_oOutHandler = null;
			m_sKey = null;
			m_iSendId = 0;
			m_oParent = null;
			m_oKeyBoard = null;
			m_bFullUI = true;
			m_bShowInputText = false;
			m_iDefaultKeyboard = 0; //默认显示的键盘 0：字母，1：符号，2：数字
			m_bShowSysSoftKeyBoardKey = false;	//在可切换系统软键盘的键盘样式中默认显示切换系统键盘按键
		}
		m_bIsBindActivity = state;
	}

	public static void setCallParent(Activity activity) {
		m_oParent = activity;		
	}
	

	//20160426
	public static void setIsShowSysSoftKeyBoardKey(boolean bShow) {
		m_bShowSysSoftKeyBoardKey = bShow;
	}
	
	
	public static boolean showSoftKeyBoard(Context ctx, Handler outHandler, String key, int sendId, 
			int maxlen, String[] signArray, boolean[] orders, 
			boolean callSoftKeyBoard, boolean animStat, int uiType, 
			String exitText, String finishText) {
		copyIni(ctx);
		if (!m_bIsBindActivity)
			return false;

		if (outHandler == null || ctx == null || safeHandle.getServerState())
			return false;

		/*if (!safeHandle.getLoadLibraryState())
			safeHandle.setIsLoadLibraryState(Encrypt.initialize(""));

		if (safeHandle.getLoadLibraryState()) {
			File file = ctx.getFilesDir();

			String path = file.getAbsolutePath();
			
			// DaBing Add this in 2016/10/18
			// path += "/data1.ini";
			String iniName = Encrypt.getIniName();
			if(TextUtils.isEmpty(iniName))
				iniName = "data1.ini";
			path += "/" + iniName;

			if(isOverSeas)
				Encrypt.setOverSeasCall(1);
			Encrypt.initEncrypt(path);
		}*/

		safeHandle.setOutHandler(outHandler);
		
		WindowManager m_oWindowManager = m_oParent.getWindow().getWindowManager();
		if (m_oKeyBoard != null && m_oParent != null) {
			if (m_oWindowManager != null)
				m_oWindowManager.removeView(m_oKeyBoard);
			m_oKeyBoard.cleanMemory();
			m_oKeyBoard = null;
		}
		
		m_oKeyBoard = new safeSoftKeyBoard(ctx);
		
		// Add:2016.5.7 MaKai-[Add encryption of 3DES] Start
		/*if(null != m_oKeyBoard) {
			m_oKeyBoard.setUserFlag(m_iUserFlag);
			Encrypt.setEncryptType(m_iEncryptType);
		}*/
		//debug
		//setEncryptType(1);
		//debug
		//Log.d("safeEditText_3DES", "User type is "+ this.m_iUserFlag);
		// Add:2016.5.7 MaKai-[Add encryption of 3DES] End

		if (!TextUtils.isEmpty(key)){
			m_oKeyBoard.setKey(key);
		}

		Drawable drawable = null;
		int width = 875;
		int height = 672;
		
		if( !callSoftKeyBoard )
		{
			width = 640;
			height = 525;
		}
		if (uiType == 2)
			drawable = ctx.getResources().getDrawable(R.drawable.back_2);
		else if (uiType == 1) {
			if (!callSoftKeyBoard)
				drawable = ctx.getResources().getDrawable(R.drawable.backnew_1);
			else
				drawable = ctx.getResources().getDrawable(R.drawable.bg_new);
		} else if (uiType == 5)
			drawable = ctx.getResources().getDrawable(R.drawable.bgmini);

//			if (drawable != null)
//				m_oKeyBoard.setBackgroundDrawable(drawable);
//			else
//				m_oKeyBoard.setBackgroundColor(0xffcacaca);

		m_oKeyBoard.setSendId(sendId);
		m_oKeyBoard.setIsOrdered(orders);
		m_oKeyBoard.setSighArray(signArray);
		m_oKeyBoard.setTextMaxLen(maxlen);
		
		//20160426
		//m_oKeyBoard.setCallSysSoftKeyBoard(callSoftKeyBoard);
		m_oKeyBoard.setCallSysSoftKeyBoard(callSoftKeyBoard,m_bShowSysSoftKeyBoardKey);
		
		m_oKeyBoard.setAnimStat(animStat);
		m_oKeyBoard.setUIType(uiType);
		m_oKeyBoard.setExitText(exitText);
		m_oKeyBoard.setFinishText(finishText);
		
		m_oKeyBoard.setShowInputTextFlag(m_bShowInputText);
		
		//��������Ҫ��init֮ǰ����
		m_oKeyBoard.setDefaultKeyboard(m_iDefaultKeyboard);
		
		m_oKeyBoard.initSoftKeyBoard(drawable);

		if (m_oWindowManager != null) {
			DisplayMetrics localDisplayMetrics = ctx.getApplicationContext().getResources().getDisplayMetrics();

			float ratioX = (float) localDisplayMetrics.widthPixels / width;
			int m_iMaxWidth = localDisplayMetrics.widthPixels;
			
			int m_iMaxHeight = (int) (height * ratioX + 0.5F);				
			if (m_iMaxHeight > localDisplayMetrics.heightPixels / 2) {
				m_iMaxHeight = (localDisplayMetrics.heightPixels / 2);
			} 
			
			int h = localDisplayMetrics.heightPixels - m_iMaxHeight;
			
			if(!m_bFullUI)
			{
				WindowManager.LayoutParams m_oLayoutParams = new WindowManager.LayoutParams(m_iMaxWidth, m_iMaxHeight+safeSoftKeyBoard.m_iMoveDownY, 0, -((m_iMaxHeight+safeSoftKeyBoard.m_iMoveDownY)/2-localDisplayMetrics.heightPixels/2), WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
				m_oWindowManager.addView(m_oKeyBoard, m_oLayoutParams);
			}
			else
			{
				WindowManager.LayoutParams m_oLayoutParams = new WindowManager.LayoutParams(m_iMaxWidth, m_iMaxHeight+safeSoftKeyBoard.m_iMoveDownY, localDisplayMetrics.widthPixels / 2, -(h / 2 - h), WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, PixelFormat.TRANSLUCENT);
				m_oWindowManager.addView(m_oKeyBoard, m_oLayoutParams);
			}			
		}		

		m_oCtx = ctx;
		m_oOutHandler = outHandler;
		m_sKey = key;
		m_iSendId = sendId;

		return true;
	}

	public static boolean resizeSoftKeyBoard() {
		if (!m_bIsBindActivity)
			return false;

		if (m_oOutHandler == null || m_oCtx == null)
			return false;

		if (safeHandle.getServerState()) {
			m_oCtx.stopService(new Intent(m_oCtx, catchInfo.class));

			int count = 0;
			while (true) {
				SystemClock.sleep(100);
				count++;

				if (count == 5)
					break;
			}

			safeHandle.setOutHandler(m_oOutHandler);
			Intent i = new Intent(m_oCtx, catchInfo.class);

			if (!TextUtils.isEmpty(m_sKey))
				i.putExtra("key", m_sKey);

			i.putExtra("sendid", m_iSendId);
			m_oCtx.startService(i);
		}

		return true;
	}

	public static boolean hideSoftKeyBoard() {
		if (!m_bIsBindActivity)
			return false;

		if (m_oKeyBoard != null && m_oParent != null) {
			WindowManager m_oWindowManager = m_oParent.getWindow().getWindowManager();

			if (m_oWindowManager != null)
				m_oWindowManager.removeView(m_oKeyBoard);

			m_oKeyBoard.cleanMemory();
			m_oKeyBoard = null;
		}

		return true;
		
	}

	public static int getKeyBoardHeight(Context ctx) {
		DisplayMetrics localDisplayMetrics = ctx.getApplicationContext().getResources().getDisplayMetrics();
		if (localDisplayMetrics == null)
			return 0;

		float ratioX = (float) localDisplayMetrics.widthPixels / 875;
		int h = (int) (672 * ratioX + 0.5F);

		if (h > localDisplayMetrics.heightPixels / 2)
			h = localDisplayMetrics.heightPixels / 2;

		return h;
	}

	public static boolean copyIni(Context ctx) {
		try {
			File file = ctx.getFilesDir();

			String path = file.getAbsolutePath();
			String dataIni;
			
			// DaBing Add this in 2016/11/29
			// path += "/data1.ini";
			
			dataIni = "";
			if(TextUtils.isEmpty(dataIni))
				dataIni = "data1.ini";
			
			path += "/" + dataIni;
			InputStream is = ctx.getAssets().open(dataIni);
			
			File f = new File(path);
			// modify by fengwei
			// update apk, new ini file need update too!!
			// InputStream is = ctx.getAssets().open("data1.ini");
			if (!f.exists() || f.length() != is.available()) {
				// InputStream is = ctx.getAssets().open("data.ini");				
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

}
