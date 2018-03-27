package com.ISHello.Configuration;

/**
 * Created by zhanglong on 2017/5/17.
 */

public class Configuration {
    private static Configuration instance = null;

    private Configuration() {

    }

    public synchronized static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
            instance.initialize();
        }
        return instance;
    }

    private void initialize() {

    }

    /**
     * 返回上传文件日志服务器地址
     *
     * @return
     */
    public String getLogURL() {
        return "";
    }
}
