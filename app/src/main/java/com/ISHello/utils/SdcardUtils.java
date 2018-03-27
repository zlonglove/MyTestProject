package com.ISHello.utils;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.File;

/**
 * @author kfzx-zhanglong
 *         如果要后去机身内存需要将path换成Environment.getDataDirectory().getPath()
 *         <p/>
 *         sd卡目录/storage/emulated/legacy
 *         /storage/emulated/0
 */

public class SdcardUtils {
    /**
     * 检查SD卡是不是存在
     *
     * @return
     */
    public static boolean checkSDCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取sdcard的绝对路径，包含"/"
     */
    public static String getSDCardPathWithFileSeparators() {
        return Environment.getExternalStorageDirectory().toString() + File.separator;
    }

    /**
     * 检查sdcard可用大小
     *
     * @param filePath
     * @return
     */
    public static long getAvailableStore(String filePath) {
        StatFs statFs = new StatFs(filePath);
        long blocSize = statFs.getBlockSize();
        long availaBlock = statFs.getAvailableBlocks();
        long availableSpare = availaBlock * blocSize;
        return availableSpare;
    }

    public static long getTotalSize(String path) {
        // String path = Environment.getExternalStorageDirectory().getPath();
        // String path=Environment.getDataDirectory().getPath();
        if (TextUtils.isEmpty(path)) {
            return -1;
        }
        StatFs statFs = new StatFs(path);
        long blockSize = statFs.getBlockSize();
        long totalBlocks = statFs.getBlockCount();
        return blockSize * totalBlocks;
    }

    /**
     * 1kb=1024byte
     */
    private static final long NOSPACE_THRESHOLD = 1024;

    /**
     * Checks if there is enough Space on phone sdcard
     *
     * @return
     */
    public static boolean isSdcardHasSpareSpace() {
        if (checkSDCard()) {
            long availableSize = getAvailableStore(getSDCardPathWithFileSeparators());
            if (availableSize < NOSPACE_THRESHOLD) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Checks if there is enough Space on phone self
     */
    public static boolean enoughSpaceOnPhone(long updateSize) {
        return getRealSizeOnPhone() > updateSize;
    }

    /**
     * get the space is left over on phone self
     */
    public static long getRealSizeOnPhone() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        long realSize = blockSize * availableBlocks;
        return realSize;
    }

    /**
     * get the space is left over on sdcard
     */
    public static long getRealSizeOnSdcard() {
        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

}
