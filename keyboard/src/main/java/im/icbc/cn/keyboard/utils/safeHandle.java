package im.icbc.cn.keyboard.utils;

import android.os.Handler;

public class safeHandle {
    private static Handler m_oHandler = null;
    private static boolean m_bIsServerRunning = false;
    private static Handler m_oOutHandler = null;
    private static boolean m_bIsLoadLibrary = false;

    public static void setHandler(Handler h) {
        m_oHandler = h;
    }

    public static Handler getHandler() {
        return m_oHandler;
    }

    public static void setOutHandler(Handler h) {
        m_oOutHandler = h;
    }

    public static Handler getOutHandler() {
        return m_oOutHandler;
    }

    public static void setServerState(boolean state) {
        m_bIsServerRunning = state;
    }

    public static boolean getServerState() {
        return m_bIsServerRunning;
    }

    public static boolean getLoadLibraryState() {
        return m_bIsLoadLibrary;
    }

}
