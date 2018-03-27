package com.ISHello.base.net;

import java.util.HashMap;

public class HttpRespEntity {

    public enum HttpRespResultType {
        HTTP_REQUEST_OK, // 请求成功
        HTTP_REQUEST_OK_ERRORCODE, // 请求成功但是返回错误码
        HTTP_REQUEST_FAILED, // 请求成功但是返回的不是200
        HTTP_REQUEST_OK_NOT200, // 请求失败
        HTTP_SESSION_ERROR,// session错误
        HTTPS_SSL_ERROR//https校验错误
    }

    private HttpRespResultType result;
    private HashMap resultHashMap = new HashMap();
    private String errorCode = "";
    private String errorMessage = "";
    private byte[] resultByte;

    public HttpRespEntity() {

    }

    public HttpRespEntity(HttpRespResultType result) {
        this.result = result;
    }

    public HttpRespEntity(HttpRespResultType result, String errorMessage) {
        this.result = result;
        this.errorMessage = errorMessage;
    }

    public HttpRespEntity(HttpRespResultType result, HashMap resultHashMap, String errorCode, String errorMessage) {
        this.result = result;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        if (resultHashMap != null)
            this.resultHashMap = resultHashMap;
    }

    public HttpRespResultType getResult() {
        return result;
    }

    public void setResult(HttpRespResultType result) {
        this.result = result;
    }

    public HashMap getResultHashMap() {
        return resultHashMap;
    }

    public void setResultHashMap(HashMap resultHashMap) {
        this.resultHashMap = resultHashMap;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public byte[] getResultByte() {
        return resultByte;
    }

    public void setResultByte(byte[] resultByte) {
        this.resultByte = resultByte;
    }

    public String getStringValue(String inKey) {
        String retString = (String) resultHashMap.get(inKey);
        if (retString == null) retString = "";

        return retString;
    }
}
