package com.ISHello.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpURLConnectionUtils {
    public InputStream inputStream = null;
    public HttpURLConnection connection = null;
    DownLoadDataForm mDataForm;

    public HttpURLConnectionUtils(DownLoadDataForm dataForm) {
        mDataForm = dataForm;
    }

    private void initConnectioin() throws IOException {
        if (mDataForm != null && mDataForm.get && mDataForm.paraMap != null && mDataForm.paraMap.size() > 0) {// GET请求参数特殊处理
            Map<String, String> paras = mDataForm.paraMap;
            StringBuffer parmsBuffer = new StringBuffer("?");
            for (Map.Entry<String, String> entry : paras.entrySet()) {
                parmsBuffer.append(entry.getKey()).append("=").append(entry.getValue());
                parmsBuffer.append("&");
            }
            String data = parmsBuffer.deleteCharAt(parmsBuffer.length() - 1).toString();
            mDataForm.url = mDataForm.url + data;
        }
        if (mDataForm.useProxy && mDataForm.proxy != null) {
            connection = (HttpURLConnection) new URL(mDataForm.url).openConnection(mDataForm.proxy);
        } else {
            connection = (HttpURLConnection) new URL(mDataForm.url).openConnection();
        }
        connection.setDoInput(mDataForm.config.doInput);
        connection.setDoOutput(mDataForm.config.doOutput);
        connection.setConnectTimeout(mDataForm.config.connectTimeOut);
        connection.setReadTimeout(mDataForm.config.readTimeOut);
        if (!mDataForm.get) { // 如果是post 进行如下设置
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            if (mDataForm.paraMap != null) {
                Map<String, String> paras = mDataForm.paraMap;
                StringBuffer sb = new StringBuffer();
                for (Map.Entry<String, String> entry : paras.entrySet()) {
                    sb.append(entry.getKey()).append("=").append(entry.getValue());
                    sb.append("&");
                }
                /**
                 * 去掉最后一个&号
                 */
                String data = sb.deleteCharAt(sb.length() - 1).toString();

                if (mDataForm.config.headerMap != null) {
                    Map<String, String> headMap = mDataForm.config.headerMap;
                    Iterator<Entry<String, String>> it = headMap.entrySet().iterator();
                    Entry<String, String> curEntry;
                    while (it.hasNext()) {
                        curEntry = it.next();
                        connection.setRequestProperty(curEntry.getKey(), curEntry.getValue());
                    }
                }
                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                // 将请求参数数据向服务器端发送
                dos.writeBytes(data);
                dos.flush();
                dos.close();
            }
        } else {
            connection.setRequestMethod("GET");
            if (mDataForm.config.headerMap != null) {
                Map<String, String> headMap = mDataForm.config.headerMap;
                Iterator<Entry<String, String>> it = headMap.entrySet().iterator();
                Entry<String, String> curEntry;
                while (it.hasNext()) {
                    curEntry = it.next();
                    connection.addRequestProperty(curEntry.getKey(), curEntry.getValue());
                }
            }
        }
    }

    private byte[] readData() throws IOException {
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            ByteArrayOutputStream bostream = null;
            inputStream = connection.getInputStream();
            byte[] bytes = new byte[512];
            bostream = new ByteArrayOutputStream();
            int count = 0;
            while ((count = inputStream.read(bytes)) != -1) {
                bostream.write(bytes, 0, count);
            }
            bytes = bostream.toByteArray();
            bostream.close();
            return bytes;
        } else {
            return null;
        }
    }

    /***
     * 获取数据,唯一对外提供的接口
     *
     * @return byte[]
     * @throws IOException
     */
    public byte[] getResponseByteArray() throws IOException {
        initConnectioin();
        byte[] bytes = readData();
        close();
        return bytes;
    }

    /**
     * 关闭连接
     *
     * @return
     * @throws IOException
     */
    public void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
            inputStream = null;
        }
        if (connection != null) {
            connection.disconnect();
            connection = null;
        }

    }
}
