package zlonglove.cn.adrecyclerview.tools;

import android.content.Context;

/**
 * 描述:上下文句柄获取工具类
 */
public class ContextUtil {
    private static Context context;

    private ContextUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        ContextUtil.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init ContextUtil int your application class firstly");
    }
}
