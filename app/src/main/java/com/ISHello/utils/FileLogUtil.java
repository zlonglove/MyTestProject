package com.ISHello.utils;

import com.ISHello.Constants.Constants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by kfzx-zhanglong on 2016/6/28.
 * Company ICBC
 * 将字符串以追加的方式写入文件,每天一个文件,以日期命名,可以根据日期查找
 */


public class FileLogUtil {

    /**
     * [2016-07-06 14:38:54]-[log]-Thread[main,5,main]
     * StackTrace Info
     *
     * @param log
     */
    public static void write(String log) {
        try {
            long milliseconds = System.currentTimeMillis();
            Thread e = Thread.currentThread();
            StringBuffer sb = new StringBuffer();
            sb.append("[");
            sb.append(DataTimeUtils.getDateTimeString(milliseconds));//date time
            sb.append("]");
            sb.append("-[");
            sb.append(log);
            sb.append("]");
            sb.append("-");
            sb.append(e);
            sb.append("\n");
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

            for (int filePath = 3; filePath < stackTrace.length; ++filePath) {
                sb.append(stackTrace[filePath].toString());
                sb.append("\n");
            }
            sb.append("**********************************************************");
            sb.append("\n\n");
            String path = Constants.FILE_LOG_PATH + DataTimeUtils.getDateString(milliseconds) + ".log";
            File file = new File(path);
            file.getParentFile().mkdirs();
            if (!file.exists()) {
                file.createNewFile();
            }
            /**
             * 追加方式打开文件
             */
            FileWriter writer = new FileWriter(file, true);
            writer.append(sb);
            writer.flush();
            writer.close();
            LogUtil.log("write file time==" + Long.toString(System.currentTimeMillis() - milliseconds));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
