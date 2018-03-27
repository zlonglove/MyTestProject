/**
 *
 */
package com.ISHello.Upload;

import android.util.Log;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class HttpUploadFileUtils {
    private UploadParmas mDataForm;
    private final String TAG = "HttpUploadFileUtils";

    public HttpUploadFileUtils(UploadParmas dataForm) {
        mDataForm = dataForm;
    }

    /**
     * 标准的文件上传接口,线程里调用
     *
     * @return
     */
    public boolean uploadToServer() {
        PostMethod filePost = new PostMethod(mDataForm.getUrl());
        try {
            /**
             * 添加请求的头
             */
            if (mDataForm.getHeaderMap() != null) {
                Map<String, String> paras = mDataForm.getHeaderMap();
                for (Map.Entry<String, String> entry : paras.entrySet()) {
                    filePost.addRequestHeader(entry.getKey(), entry.getValue());
                }
            }
            /**
             * 添加请求参数
             */
            if (mDataForm.getParaMap() != null) {
                Map<String, String> paras = mDataForm.getParaMap();
                for (Map.Entry<String, String> entry : paras.entrySet()) {
                    filePost.addParameter(new NameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            ArrayList<FilePart> partLst = new ArrayList<FilePart>();
            if (mDataForm.getFilePaths() != null) {
                for (int i = 0; i < mDataForm.getFilePaths().length; i++) {
                    File file = new File(mDataForm.getFilePaths()[i]);
                    if (file.exists()) {
                        FilePart fp = new FilePart("file", file.getName(), file);
                        partLst.add(fp);
                    }
                }
            }

            Part[] parts = new Part[partLst.size()];
            for (int i = 0; i < partLst.size(); i++) {
                parts[i] = partLst.get(i);
            }
            filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
            HttpClient client = new HttpClient();
            client.getHttpConnectionManager().getParams().setConnectionTimeout(mDataForm.getConnectTimeOut());
            int status = client.executeMethod(filePost);
            if (status == HttpStatus.SC_OK) {
                Log.i(TAG, "--->上传成功");
                filePost.abort();
                return true;
                // 上传成功
            } else {
                Log.i(TAG, "--->上传失败");
                // 上传失败
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            filePost.releaseConnection();
        }
    }

    /**
     * 上传的只是字节流,线程里调用
     *
     * @return
     */
    public boolean uploadHexToServer() {
        HttpMethod filePost = null;
        HttpConnection connection = null;
        InputStream inputStream = null;
        byte[] tempbytes = null;
        try {
            for (int i = 0; i < mDataForm.getFilePaths().length; i++) {
                try {
                    File tempFile = new File(mDataForm.getFilePaths()[i]);
                    inputStream = new FileInputStream(tempFile);
                    tempbytes = new byte[inputStream.available()];
                    inputStream.read(tempbytes);
                } catch (Exception e) {
                    continue;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }

                filePost = new PostMethod(mDataForm.getUrl());
                ByteArrayRequestEntity entity = new ByteArrayRequestEntity(tempbytes);
                ((PostMethod) filePost).setRequestEntity(entity);

                if (mDataForm.getHeaderMap() != null) {
                    Map<String, String> paras = mDataForm.getHeaderMap();
                    for (Map.Entry<String, String> entry : paras.entrySet()) {
                        filePost.addRequestHeader(entry.getKey(), entry.getValue());
                    }
                }

                URL _url = new URL(mDataForm.getUrl());
                connection = new HttpConnection(_url.getHost(), _url.getPort());
                connection.getParams().setConnectionTimeout(mDataForm.getConnectTimeOut());
                connection.getParams().setSoTimeout(mDataForm.getReadTimeOut());
                filePost.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, this);
                connection.open();

                int status = filePost.execute(new HttpState(), connection);
                InputStream inStream = null;
                StringBuffer sBuffer = new StringBuffer();
                try {
                    inStream = filePost.getResponseBodyAsStream();
                    byte[] buf = new byte[1024];
                    int len = 0;
                    while ((len = inStream.read(buf)) != -1) {
                        byte[] tmp = new byte[len];
                        System.arraycopy(buf, 0, tmp, 0, len);
                        sBuffer.append(new String(tmp, "utf-8"));
                    }
                } finally {
                    if (inStream != null) {
                        inStream.close();
                    }
                }
                Log.i("HttpStatus", "--->" + status);
                if (status == HttpStatus.SC_OK) {
                    Log.i(TAG, sBuffer.toString());
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            filePost.releaseConnection();
        }
        return false;
    }
}
