package com.ISHello.webView;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

import com.ISHello.View.AlertDialogPro;
import com.ISHello.utils.LogUtil;
import com.example.ishelloword.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.HashMap;


/**
 * Created by zhanglong on 2016/12/7.
 */

public class CustomWebChromeClient extends WebChromeClient {
    // prompt 调用js方法标识符 message 出现此符合表示js调用java接口
    public static final String JAVASCRIPT_COMMON_VALUE_KEYWORD = "callNativeMethod";

    // 调用对象 一般为Native 为区分不同代理用
    public static final String JAVASCRIPT_COMMON_VALUE_OBJECT = "obj";

    // javascirpt调用 方法名 适配程序用
    public static final String JAVASCRIPT_COMMON_VALUE_FUNCTION = "func";

    // js 调用java方法的上送参数 json数组
    public static final String JAVASCRIPT_COMMON_VALUE_ARGS = "args";
    // js 调用java方法的上送参数 json数组

    //js回调方法
    private String callBackName;

    private Activity mContext;
    private NativeWebviewBaseProxy nativeWebviewBaseProxy;

    public CustomWebChromeClient(Activity mContext, NativeWebviewBaseProxy nativeWebViewCoreProxy) {
        this.mContext = mContext;
        this.nativeWebviewBaseProxy = nativeWebViewCoreProxy;
    }

    public void setActivity(Activity mContext) {
        this.mContext = mContext;
    }

    public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
        AlertDialogPro.Builder dialog = new AlertDialogPro.Builder(mContext, R.style.Theme_AlertDialogPro_ICBC);
        dialog.setTitle("温馨提示");
        dialog.setMessage(message);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result.confirm();
                dialog.dismiss();
            }
        }).create().show();
        return true;
    }

    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        AlertDialogPro.Builder dialog = new AlertDialogPro.Builder(mContext, R.style.Theme_AlertDialogPro_ICBC);
        dialog.setTitle("温馨提示").setMessage(message).setIcon(R.drawable.ic_launcher).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                result.confirm();
            }
        }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                result.cancel();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                result.cancel();
            }
        });
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
        return true;
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        LogUtil.log("url==" + url + "\nonJsPrompt message==" + message + "\n defaultValue==" + defaultValue);
        try {
            if (view instanceof CustomWebView) {
                CustomWebView ICBCWebView = (CustomWebView) view;
                /*if (ICBCWebView.handleJsInterface(view, url, message, defaultValue, result)) {*/
                if (handleOldJsInterface(view, url, message, defaultValue, result)) {
                    return true;
                } else {
                    return handleOldJsInterface(view, url, message, defaultValue, result);
                }
            } else {
                return handleOldJsInterface(view, message, url, defaultValue, result);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        result.cancel();
        return true;
    }

    protected boolean handleOldJsInterface(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        if (nativeWebviewBaseProxy == null) {
            return true;
        }
        if ("goBack".equals(message)) {
            nativeWebviewBaseProxy.goBack();
            result.confirm();
            return true;
        } else if (message.equals("showIndicator")) {
            nativeWebviewBaseProxy.showIndicator();
            result.confirm();
            return true;
        } else if (message.equals("hideIndicator")) {
            nativeWebviewBaseProxy.hideIndicator();
            result.confirm();
            return true;
        } else if ("close".equals(message)) {
            nativeWebviewBaseProxy.close();
            result.confirm();
            return true;
        }
        // 如果没有关键字 保持原实现
        if (TextUtils.isEmpty(message) || !JAVASCRIPT_COMMON_VALUE_KEYWORD.equals(message)) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
        try {
            JSONObject messageJson = new JSONObject(defaultValue);
            String obj = messageJson.getString(JAVASCRIPT_COMMON_VALUE_OBJECT);
            String func = messageJson.getString(JAVASCRIPT_COMMON_VALUE_FUNCTION);
            JSONArray jsonArray;
            if (messageJson.has(JAVASCRIPT_COMMON_VALUE_ARGS)) {
                if (messageJson.optJSONArray(JAVASCRIPT_COMMON_VALUE_ARGS) != null) {
                    jsonArray = messageJson.getJSONArray(JAVASCRIPT_COMMON_VALUE_ARGS);
                } else {
                    jsonArray = new JSONArray();
                }
            } else {
                jsonArray = new JSONArray();
            }

            if ("Native".equals(obj)) {
                if ("goBack".equals(func)) {
                    nativeWebviewBaseProxy.goBack();
                } else if ("showIndicator".equals(func)) {
                    nativeWebviewBaseProxy.showIndicator();
                } else if ("hideIndicator".equals(func)) {
                    nativeWebviewBaseProxy.hideIndicator();
                } else if ("close".equals(func)) {
                    nativeWebviewBaseProxy.close();
                } else if ("DataConfigServiceServer".equals(func)) {// 公共B调C
                    String methodName = jsonArray.getString(0);
                    String paramJsonString = jsonArray.getString(1);
                    if (jsonArray.length() > 2) {
                        callBackName = jsonArray.getString(2);
                    }
                    paramJsonString = URLDecoder.decode(paramJsonString, "UTF-8");
                    LogUtil.log("onJsPrompt:methodName:" + methodName + "\nparamJsonString:" + paramJsonString + "\ncallBackName:" + callBackName);
                    if ("selectVoteImage".equals(methodName)) {
                        HashMap<String, String> valMap;
                        Gson gson = new Gson();
                        valMap = gson.fromJson(paramJsonString, new TypeToken<HashMap<String, String>>() {
                        }.getType());
                        if (valMap != null) {
                            if (valMap.containsKey("FuncNo")) {
                                String funcNo = valMap.get("FuncNo");
                                LogUtil.log("funcNo==" + funcNo);
                            }
                        }
                        if (!TextUtils.isEmpty(callBackName)) {
                            view.loadUrl("javascript:" + callBackName + "('" + callBackName + "')");
                        }
                        //result.confirm(callBackName);
                    }
                }
            }
        } catch (JSONException e) {
            LogUtil.log(e.getMessage());
            result.confirm();
            return true;
        } catch (Exception e) {
            LogUtil.log(e.getMessage());
            result.confirm();
            return true;
        }
        result.confirm();
        return true;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        mContext.setProgress(newProgress * 100);
        if (CustomWebView.JS_BUG_ALLVERSION_HANDLEFLAG || (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN && view instanceof CustomWebView)) {
            CustomWebView customWebView = (CustomWebView) view;
            customWebView.injectJavascriptInterfaces();
        }
        if (newProgress == 100) {
            try {
                if (nativeWebviewBaseProxy != null) {
                    // nativeWebViewCoreProxy.hideIndicator();
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        callback.invoke(origin, true, false);
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }


    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
        super.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
        quotaUpdater.updateQuota(estimatedDatabaseSize * 2);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        LogUtil.log("onReceived Title==" + title);
        super.onReceivedTitle(view, title);
    }
}
