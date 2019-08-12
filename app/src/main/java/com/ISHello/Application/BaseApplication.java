package com.ISHello.Application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.ISHello.Constants.Constants;
import com.ISHello.Exception.CrashHandler;
import com.ISHello.GesturePassword.Utils.LockPatternUtils;
import com.ISHello.ImageLoader.implement.ImageLoader;
import com.ISHello.ImageLoader.implement.ImageLoaderConfiguration;
import com.ISHello.utils.ContextUtils;
import com.ISHello.utils.ProcessUtil;
import com.example.ishelloword.R;
import com.in.zlonglove.commonutil.BuildConfig;
import com.in.zlonglove.commonutil.ToastUtils;
import com.in.zlonglove.commonutil.Utils;
import com.in.zlonglove.commonutil.klog.KLog;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import cn.jpush.android.api.JPushInterface;

/***
 * 支持multidex功能
 * 1)可以继承MultiDexApplication[BaseApplication extends MultiDexApplication]
 * 2)重写attachBaseContext()方法,比onCreate方法先执行,Multidex.install(this)
 */
public class BaseApplication extends MultiDexApplication {
    private final String TAG = "BaseApplication";
    private static final String OTHER_PROCESS = "com.example.ishelloword:myProcess";
    private static BaseApplication instance;

    private LockPatternUtils mLockPatternUtils;
    public boolean isLocked = false;

    LockScreenReceiver receiver;
    IntentFilter filter;

    private RefWatcher refWatcher;

    /**
     *  static 代码段可以防止内存泄露
     */
    static {
        /**
         * 设置全局的Header构建器
         */
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header,默认是贝塞尔雷达Header
            }
        });
        /**
         * 设置全局的Footer构建器
         */
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                /**
                 * 指定为经典Footer，默认是 BallPulseFooter
                 */
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "--->onCreate()");
        instance = this;
        ContextUtils.init(this);
        mLockPatternUtils = new LockPatternUtils(this);
        CrashHandler handler = CrashHandler.getInstance();
        handler.init(this);
        final String ProcessName = ProcessUtil.getProcessName(getApplicationContext());
        Log.i(TAG, "--->Process Name==" + ProcessName);
        // 在Appliction里面设置我们的异常处理器为UncaughtExceptionHandler处理器
        if (OTHER_PROCESS.equals(ProcessName)) {
            Log.i(TAG, "--->myProcess Process init");
            //ProcessCore.initialize(this);
        } else if (Constants.PUSH_PROCESS.equals(ProcessName)) {
            Log.i(TAG, "--->AIDL Process init");
        }
        receiver = new LockScreenReceiver();
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        this.registerReceiver(receiver, filter);

        initMineImageLoader(this);
        initImageLoader(getApplicationContext());

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        KLog.init(BuildConfig.LOG_DEBUG);
        Utils.init(getApplicationContext());
        ToastUtils.init(true);

        refWatcher=LeakCanary.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.i(TAG, "--->onTerminate()");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i(TAG, "--->onLowMemory()");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.i(TAG, "--->onTrimMemory() level==" + level);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }

    public void initMineImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.isSyncLoading(false);
        //config.tasksProcessingOrder(QueueProcessingType.LIFO);
        //config.writeDebugLogs(); // Remove for release app

        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);

        com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder config = new com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config.build());
    }

    public LockPatternUtils getLockPatternUtils() {
        return mLockPatternUtils;
    }

    class LockScreenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                isLocked = true;
            }
        }
    }

    public  RefWatcher getRefWatcher(){
        return refWatcher;
    }
}
