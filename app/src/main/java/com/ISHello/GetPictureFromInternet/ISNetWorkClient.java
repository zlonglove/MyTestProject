package com.ISHello.GetPictureFromInternet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class ISNetWorkClient {
    private final String TAG = "ISImageServer";
    private static ISNetWorkClient imageServer;

    public ISNetWorkClient() {

    }

    public static synchronized ISNetWorkClient getInstance() {
        if (imageServer == null) {
            imageServer = new ISNetWorkClient();
        }
        return imageServer;
    }

    /**
     * Post 方式请求,带参数 ,参数封装在请求的正文里面
     *
     * @param urlPath
     * @return Bitmap
     * @throws IOException
     */
    public Bitmap getImageByPost(String urlPath) throws IOException {
        Bitmap bitmap = null;
        HttpURLConnection connection = null;
        InputStream inStream = null;
        OutputStream outputStream = null;
        try {
            URL url = new URL(urlPath);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            /**
             * 允许客户端向server写数据
             */
            connection.setDoOutput(true);
            connection.setConnectTimeout(6000);
            connection.setReadTimeout(6000);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            // connection.setRequestProperty("Content-Type", newValue);
            // connection.setRequestProperty("Content-Length", newValue)
            connection.connect();

            /**
             * 向server发送数据
             */
            outputStream = connection.getOutputStream();
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", "1");
            byte[] paramsBuffer = setPostParams(params).toString().getBytes();
            outputStream.write(paramsBuffer);
            outputStream.flush();

            /**
             * 获取server返回的输入流,如果返回的是String或者Json需要自己读到byte[]做解析
             */
            int status = connection.getResponseCode();
            Log.i(TAG, "--->The connect.getResponseCode()==" + status);
            if (status == HttpURLConnection.HTTP_OK) {

                inStream = connection.getInputStream();
                if (inStream != null) {
                    bitmap = BitmapFactory.decodeStream(inStream);
                }
            } else {
                connection.disconnect();
            }

        } catch (Exception e) {
        } finally {
            if (inStream != null) {
                inStream.close();
                inStream = null;
            }
            if (outputStream != null) {
                outputStream.close();
                outputStream = null;
            }
            if (connection != null) {
                connection.disconnect();
                connection = null;
            }
        }
        return bitmap;
    }

    /**
     * "key=value&key=value&key=value" 构建post传递的参数
     *
     * @param params
     * @return StringBuffer
     * @throws UnsupportedEncodingException
     */
    StringBuffer setPostParams(Map<String, String> params)
            throws UnsupportedEncodingException {
        if (params == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            stringBuffer.append(entry.getKey()).append("==")
                    .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                    .append("&");
        }
        /**
         * 删除最后一个&号
         */
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer;

    }

    /**
     * GET方式请求 ,带参数,参数封装在请求的头里面(添加到url的后面)
     *
     * @param urlPath
     * @return Bitmap
     * @throws IOException
     */
    public Bitmap getImage(String urlPath) throws IOException {
        Bitmap bitmap = null;
        InputStream inStream = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlPath);
            connection = (HttpURLConnection) url.openConnection();
            /**
             * 设置可以从server读取数据,要是向server写数据则connection.setDoOutput(true);
             */
            connection.setDoInput(true);
            /**
             * 设置请求的超时时间
             */
            connection.setConnectTimeout(5000);
            /**
             * 设置响应的超时时间
             */
            connection.setReadTimeout(5000);
            /**
             * 设置请求方式 GET/POST
             */
            connection.setRequestMethod("GET");
            int status = connection.getResponseCode();
            Log.i(TAG, "--->The connect.getResponseCode()==" + status);
            if (status == HttpURLConnection.HTTP_OK) {
                /**
                 * 获取server返回的输入流,如果返回的是String或者Json需要自己读到byte[]做解析
                 */
                inStream = connection.getInputStream();
                if (inStream != null) {
                    bitmap = BitmapFactory.decodeStream(inStream);
                }
            } else {
                connection.disconnect();
            }
        } catch (ConnectException connectException) {
            connectException.printStackTrace();
        } catch (SocketException socketException) {
            socketException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                inStream.close();
                inStream = null;
            }
            if (connection != null) {
                connection.disconnect();
                connection = null;
            }
        }
        return bitmap;
    }

    public String getHtml(String path) throws IOException {
        if (path == null || "".equals(path)) {
            return null;
        }
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            byte[] data = StreamTool.read(inputStream);
            return new String(data);
        } else {
            Log.i(TAG, "-->server error");
        }
        return null;
    }

    /**
     * 上传文件操作
     *
     * @param url    ---服务器的url
     * @param path   --本地文件的路径
     * @param params ---参数
     * @return
     * @throws IOException
     */
    public String uploadImage(String url, String path,
                              Map<String, String> params) throws IOException {
        String result = null;
        InputStream inputStream = new FileInputStream(path);
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        try {
            /**
             * 把数据封装到post里面,Httpmine (v4.2.3) (v4.3.3--->httpcore)
             */
            MultipartEntity entity = new MultipartEntity();
            /**
             * 普通数据的封装,字符串
             */
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                entity.addPart(key,
                        new StringBody(value, Charset.forName("UTF-8")));
            }

            /**
             * 二进制的流文件封装
             */
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            Log.i(TAG, "--->fileName==" + fileName);
            entity.addPart("file", new InputStreamBody(inputStream,
                    "multipart/form-data", fileName));
            /**
             * 在这里可以继续添加上传的文件流
             */
            post.setEntity(entity);

            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
            } else {
                /**
                 * 错误处理，例如可以在该请求正常结束前将其中断
                 */
                post.abort();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }
        }
        return result;
    }

}
