package com.ISHello.Cache.DiskLrcCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.widget.ImageView;

import com.example.ishelloword.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by kfzx-zhanglong on 2016/6/13.
 * Company ICBC
 */
public class ISDiskLruCacheUtils {
    private Context context;
    private static ISDiskLruCacheUtils diskLruCacheUtils;
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;//50M
    private static final int DISK_CACHE_INDEX = 0;
    private static final int IO_BUFFER_SIZE = 8 * 1024;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();//
    /**
     * 线程池的核心线程数
     */
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    /**
     * 线程池所能容纳的最大线程数
     */
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    /**
     * 非核心线程闲置时的超时时长
     */
    private static final long KEEP_ALIVE = 10L;

    private static final int TAG_KEY_URL = R.id.image;
    private DiskLruCache mDiskLruCache;
    private static final String TAG = "ISDiskLruCacheUtils";
    private ImageResizer imageResizer;
    private int ErrorResourceId;

    private ISDiskLruCacheUtils(Context context) {
        this.context = context;
        imageResizer = new ImageResizer();
    }

    public static ISDiskLruCacheUtils getInstance(Context context) {
        if (diskLruCacheUtils == null) {
            diskLruCacheUtils = new ISDiskLruCacheUtils(context);
            Log.i(TAG, "--->core pool size==" + CORE_POOL_SIZE);
            Log.i(TAG, "--->max pool size==" + MAXIMUM_POOL_SIZE);
        }
        return diskLruCacheUtils;
    }

    /**
     * 设置下载图片失败显示的图片
     *
     * @param id
     */
    public void setErrorResource(@DrawableRes int id) {
        this.ErrorResourceId = id;
    }

    /**
     * @param dirName
     * @return
     */
    public boolean open(String dirName) {
        File diskCache = getDiskCacheDir(dirName);
        if (!diskCache.exists()) {
            diskCache.mkdirs();
        }
        if (getUsableSpace(diskCache) > DISK_CACHE_SIZE) {
            try {
                mDiskLruCache = DiskLruCache.open(diskCache, 1, 1, DISK_CACHE_SIZE);
                if (mDiskLruCache == null) {
                    return false;
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            Log.i(TAG, "--->not enough usable space");
            return false;
        }
    }

    /**
     * 线程池工厂,为线程提供创建新线程的功能
     */
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            int count = mCount.getAndIncrement();
            Log.i(TAG, "--->Thread Factory count==" + count);
            return new Thread(runnable, "--->imageLoader#" + count);
        }
    };

    /**
     * 线程池中的任务队列,通过线程池的execute方法提交的Runnable对象会存储在这个参数中.
     * 如果队列达到最大数量,此时线程数量没有达到线程池规定的最大值,那么会立马启动一个非核心线程来执行任务
     */
    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);

    /**
     * 线程池对象
     */
    private final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
            KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);


    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            loaderResult result = (loaderResult) msg.obj;
            ImageView imageView = result.imageView;
            String uri = (String) imageView.getTag(TAG_KEY_URL);
            if (uri.equals(result.uri)) {
                imageView.setImageBitmap(result.bitmap);
            } else {
                Log.i(TAG, "--->set image bitmap,but url has chanaged,ignored!");
            }
        }
    };

    private static class loaderResult {
        public ImageView imageView;
        public String uri;
        public Bitmap bitmap;

        public loaderResult(ImageView imageView, String uri, Bitmap bitmap) {
            this.imageView = imageView;
            this.uri = uri;
            this.bitmap = bitmap;
        }
    }

    public void bindBitmap(final String url, final ImageView imageView) {
        bindBitmap(url, imageView, 0, 0);
    }

    public void bindBitmap(final String url, final ImageView imageView, final int reqWidth, final int reqHeight) {
        imageView.setTag(TAG_KEY_URL, url);
        Runnable loadbitMapTask = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG, "--->task==" + url);
                    Bitmap bitmap = loadFromDiskCache(url, reqWidth, reqHeight);
                    if (bitmap == null) {
                        bitmap = loadBitMapFromNet(url, reqWidth, reqHeight);
                        if (bitmap == null) {
                            bitmap = imageResizer.decodeSampleBitmapFromFileDescriptor(context, ErrorResourceId, 0, 0);
                        }
                    }
                    loaderResult result = new loaderResult(imageView, url, bitmap);
                    mMainHandler.obtainMessage(100, result).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadbitMapTask);
    }

    /**
     * 下载一张图片到本地磁盘缓存
     *
     * @param url
     * @throws IOException
     */
    public Bitmap loadBitMapFromNet(final String url, final int reqWidth, final int reqHeight) throws IOException {
        if (mDiskLruCache == null) {
            Log.i(TAG, "--->DiskLruCache not init");
            return null;
        }
        String key = hashKeyFromUrl(url);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
            if (downloadUrlToStream(url, outputStream)) {
                editor.commit();
            } else {
                editor.abort();
            }
            mDiskLruCache.flush();
        }
        return loadFromDiskCache(url, reqWidth, reqHeight);
    }

    /**
     * 加载一个BitMap从磁盘
     *
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws IOException
     */
    public Bitmap loadFromDiskCache(String url, int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.i(TAG, "--->load bitmap form UI Thread,it's not recommended");
        }
        if (mDiskLruCache == null) {
            Log.i(TAG, "--->DiskLruCache not init");
            return null;
        }
        Bitmap bitmap = null;
        String key = hashKeyFromUrl(url);
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
        if (snapshot != null) {
            FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = imageResizer.decodeSampleBitmapFromFileDescriptor(fileDescriptor, reqWidth, reqHeight);
            if (bitmap != null) {
                //add to memory cache
            }
        }
        return bitmap;
    }

    /**
     * 获取缓存目录
     * 如果有SD卡的话路径为:/sdcard/Android/data/package_name/cache/uniqueName
     *
     * @param uniqueName
     * @return
     */
    private File getDiskCacheDir(String uniqueName) {
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if (externalStorageAvailable) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        Log.i(TAG, "--->DiskLruCache Dir==" + cachePath);
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获取当前磁盘可用的空间
     *
     * @param path
     * @return
     */
    private long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getFreeSpace();
        }
        final StatFs statFs = new StatFs(path.getPath());
        return (long) statFs.getBlockSize() * (long) statFs.getAvailableBlocks();
    }

    /**
     * 将url转化为对应的MD5加密串，防止url中有特殊字符
     *
     * @param url
     * @return
     */
    private String hashKeyFromUrl(String url) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
