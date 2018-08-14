package im.icbc.com.downloadfile.downloadutils.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by on 2017/7/24.
 * @author zhanglong
 */

public class ThreadPoolsUtil {

    private ExecutorService fixedThreadPool;
    private ExecutorService cachedThreadPool;
    private static ThreadPoolsUtil sThreadPoolsUtil;

    public static ThreadPoolsUtil getInstance(){
        if(sThreadPoolsUtil==null){
            synchronized (ThreadPoolsUtil.class){
                if (sThreadPoolsUtil==null){
                    sThreadPoolsUtil=new ThreadPoolsUtil();
                }
            }
        }
        return sThreadPoolsUtil;
    }

    private ThreadPoolsUtil(){
        fixedThreadPool= Executors.newFixedThreadPool(5);
        cachedThreadPool= Executors.newCachedThreadPool();
    }

    public ExecutorService getFixedThreadPool() {
        return fixedThreadPool;
    }

    public ExecutorService getCachedThreadPool() {
        return cachedThreadPool;
    }


}
