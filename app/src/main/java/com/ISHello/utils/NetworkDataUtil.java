package com.ISHello.utils;

import android.text.TextUtils;

import com.ISHello.Constants.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NetworkDataUtil {
    /**
     * Post请求参数中将传入的键/值对参数转换为NameValuePair参数集
     *
     * @param paramsMap 参数列表
     * @return UrlEncodedFormEntity(getParamsList(params), Constants.CHARSET_UTF8);
     * HttpPost.setEntity(formEntity);
     */
    public static final List<NameValuePair> getParamsList(Map<String, String> paramsMap) {
        if (paramsMap == null || paramsMap.size() <= 0) {
            return null;
        }
        List<NameValuePair> listPair = new ArrayList<NameValuePair>();
        try {
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                String key = entry.getKey();
                String val = URLEncoder.encode(entry.getValue(), "UTF-8");
                NameValuePair pair = new BasicNameValuePair(key, val);
                listPair.add(pair);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return listPair;
    }

    /**
     * "key=value&key=value&key=value"
     * 构建post传递的参数,也可用于构建Get请求的参数列表http://www.baidu
     * .com?key=value&key=value&key=value
     *
     * @param params
     * @return StringBuffer
     */
    public static final StringBuffer getPostParams(Map<String, String> params) {
        if (params == null || params.size() <= 0) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey()).append("==").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
            }
            /**
             * 删除最后一个&号
             */
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }


    /**
     * 将一个InputStream转化为一个byte数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static final byte[] read(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        inputStream.close();
        return outputStream.toByteArray();
    }

    /**
     * 发送给服务端的请求中的参数值，如果含有特殊符号，需要是做URLEncode，服务端才可以正常解析，否则可能会出错。
     * URLEncode主要是把一些特殊字符转换成转移字符
     *
     * @param paramString
     * @return
     */
    public static final String getURLEncoded(final String paramString) {
        if (TextUtils.isEmpty(paramString)) {
            LogUtil.log("--->getURLEncoded error:" + paramString);
            return "";
        }
        try {
            String str = URLEncoder.encode(paramString, Constants.CHARSET_UTF8);
            return str;
        } catch (Exception localException) {
            LogUtil.log("--->getURLEncoded error:" + paramString, localException);
        }
        return "";
    }

    /**
     * 返回值处理
     *
     * @param paramString
     * @return
     */
    public static String getURLDecoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            LogUtil.log("--->getURLDecoded error:" + paramString);
            return "";
        }

        try {
            String str = new String(paramString.getBytes(), Constants.CHARSET_UTF8);
            str = URLDecoder.decode(str, Constants.CHARSET_UTF8);
            return str;
        } catch (Exception localException) {
            LogUtil.log("--->getURLDecoded error:" + paramString, localException);
        }

        return "";
    }
}
