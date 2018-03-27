package com.ISHello.base.net;

import com.ISHello.base.tools.TransactionService.TransactionType;
import com.ISHello.base.ui.BaseDialog.ProgressDialogType;

import java.util.HashMap;

public class HttpReqEntity {

    public enum ErrorTipType {
        /**
         * 正常
         */
        Normal,
        /**
         * 显示错误Toast
         */
        Toast,
        /**
         * 显示错误Dialog
         */
        Dialog
    }

    /**
     * 请求参数集合
     */
    private HashMap reqParams;
    /**
     * 是不是显示加载对话框
     */
    private boolean showProgressDialogFlag = true;
    /**
     * 加载对话框类型
     */
    private ProgressDialogType progressDialogType = ProgressDialogType.Normal;
    /**
     * 接口错误信息处理方式
     */
    private ErrorTipType errorTipType = ErrorTipType.Normal;
    /**
     * 请求类型
     */
    private TransactionType transactionType = TransactionType.Normal;
    /**
     * 请求URL
     */
    private String url;
    /**
     * 请求超时时间
     */
    private int timeout = 60;

    public HttpReqEntity() {

    }

    public HashMap getReqParams() {
        return reqParams;
    }

    /**
     * 设置参数
     *
     * @param reqParams
     */
    public void setReqParams(HashMap reqParams) {
        this.reqParams = reqParams;
    }

    /**
     * @return
     */
    public boolean isShowProgressDialogFlag() {
        return showProgressDialogFlag;
    }

    /**
     * 设置是否显示接口请求Dialog
     *
     * @param showProgressDialogFlag
     */
    public void setShowProgressDialogFlag(boolean showProgressDialogFlag) {
        this.showProgressDialogFlag = showProgressDialogFlag;
    }

    /**
     * 获取网络请求时对话框类型
     *
     * @return
     */
    public ProgressDialogType getProgressDialogType() {
        return progressDialogType;
    }

    /**
     * 设置网络请求加载对话框类型
     *
     * @param progressDialogType
     */
    public void setProgressDialogType(ProgressDialogType progressDialogType) {
        this.progressDialogType = progressDialogType;
    }

    /**
     * 获取交易类型
     *
     * @return
     */
    public TransactionType getTransactionType() {
        return transactionType;
    }

    /**
     * 设置交易类型
     *
     * @param transactionType
     */
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * 获取请求url
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置请求url
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取交易请求错误提示类型
     *
     * @return
     */
    public ErrorTipType getErrorTipType() {
        return errorTipType;
    }

    /**
     * 设置交易请求返回错误提示类型
     *
     * @param errorTipType
     */
    public void setErrorTipType(ErrorTipType errorTipType) {
        this.errorTipType = errorTipType;
    }

    /**
     * 设置请求交易超时时间
     *
     * @return
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * 获取请求交易超时时间
     *
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

}
