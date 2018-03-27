package com.ISHello.webView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;

import com.ISHello.utils.AppUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.ishelloword.R;

// 继承系统webview重写一些方法
public class CustomWebView extends WebView {

    public static final Boolean JS_BUG_HANDLE_FLAG = true;
    public static final Boolean JS_BUG_ALLVERSION_HANDLEFLAG = false;
    private static final String VAR_ARG_PREFIX = "arg";
    private static final String MSG_PROMPT_HEADER = "ICBCBridge:";
    private static final String KEY_INTERFACE_NAME = "obj";
    private static final String KEY_FUNCTION_NAME = "func";
    private static final String KEY_ARG_ARRAY = "args";
    private static final String[] mFilterMethods = {"getClass", "hashCode", "notify", "notifyAll", "equals", "toString", "wait",};

    private HashMap<String, Object> mJsInterfaceMap = new HashMap<String, Object>();
    private String mJsStringCache = null;
    private CustomWebView mWebView;
    private String isUseAmountKeyBoardFormName = "";
    private boolean isUseAmountKeyBoard = false;
    private EditText amountEditTmp = new EditText(getContext());


    private ActionMode mActionMode;
    private List<String> mActionList = new ArrayList<>();
    private ActionSelectListener mActionSelectListener;

    public CustomWebView(Context context) {
        super(context);
        init();
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        initWebviewSetting();
        initAmountEditTmp();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initWebviewSetting() {
        mWebView = this;
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setGeolocationDatabasePath(getContext().getDir("geodatabase", Context.MODE_PRIVATE).getPath());
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheMaxSize(8 * 1024 * 1024);
        webSettings.setAppCachePath(getContext().getDir("cache", Context.MODE_PRIVATE).getPath());
        webSettings.setDatabasePath(getContext().getDir("database", Context.MODE_PRIVATE).getPath());
        removeSystemJavaScriptInterface();
    }

    public void setWebViewUserAgent(String UserAgent) {
        WebSettings webSettings = getSettings();
        String defaultUserAgent = webSettings.getUserAgentString();
        webSettings.setUserAgentString(UserAgent + " " + defaultUserAgent + " BSComponentVersion:" + AppUtils.getComponentVersion());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void removeSystemJavaScriptInterface() {
        if (isLargeHoneycomb() && !isLargeJellyBean()) {
            super.removeJavascriptInterface("searchBoxJavaBridge_");
        }
    }

    private void initAmountEditTmp() {
        amountEditTmp.setId(R.id.amountEditTmp);
        amountEditTmp.setFocusable(true);
        amountEditTmp.setSingleLine(true);
        amountEditTmp.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        amountEditTmp.setRawInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        amountEditTmp.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String id = (String) v.getTag();
                InputMethodManager imm = (InputMethodManager) amountEditTmp.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (hasFocus) {
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                } else {
                    imm.hideSoftInputFromWindow(getWindowToken(), 0);
                    mWebView.loadUrl("javascript:ICBCSafeKeyBoard.callAmountSoftKeyBoardBlur({'id':'" + id + "'})");

                }
            }
        });
        amountEditTmp.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String id = (String) amountEditTmp.getTag();
                mWebView.loadUrl("javascript:ICBCSafeKeyBoard.setAmountSoftKeyBoardText({'id':'" + id + "','displayStr':'" + s.toString() + "'})");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // 重新键盘弹出方法，实现数字键盘
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection connection = super.onCreateInputConnection(outAttrs);
        try {
            boolean find = false;
            String formName = "";
            if (Build.VERSION.SDK_INT < 19) {
                try {
                    Field f = connection.getClass().getDeclaredField("mName");
                    f.setAccessible(true);
                    formName = (String) f.get(connection);
                    find = true;
                } catch (Exception e) {
                    find = false;
                }
                if (find) {
                    if (formName.equals(isUseAmountKeyBoardFormName)) {
                        isUseAmountKeyBoard = true;
                    }
                }
            }
            if (isUseAmountKeyBoard) {
                outAttrs.inputType = EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL;
                if (Build.VERSION.SDK_INT < 21) {
                    isUseAmountKeyBoard = false;
                }
            } else {
                if (outAttrs.inputType == (EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL))
                    outAttrs.inputType = EditorInfo.TYPE_CLASS_TEXT;
            }
            return connection;
        } catch (Exception e) {
            return connection;
        }
    }

    public void setUseAmountKeyBoard(String name) {
        this.isUseAmountKeyBoardFormName = name;
        this.isUseAmountKeyBoard = true;
    }

    public void cleanUseAmountKeyBoard() {
        this.isUseAmountKeyBoardFormName = "";
        this.isUseAmountKeyBoard = false;
    }

    public void callCustomAmountKeyBoard(String defaultValue) {
        ViewGroup group = (ViewGroup) ((ViewGroup) ((Activity) getContext()).findViewById(android.R.id.content)).getChildAt(0);
        if (group.getChildAt(0).findViewById(R.id.amountEditTmp) == null) {
            ((ViewGroup) group.getChildAt(0)).addView(amountEditTmp, 1, 1);
        }
        HashMap<String, String> param = JSONObject.parseObject(defaultValue, new TypeReference<HashMap<String, String>>() {
        });
        try {
            amountEditTmp.setTag(param.get("id"));
            amountEditTmp.setText(param.get("defaultValue"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        amountEditTmp.requestFocus();
    }

    @Override
    public void addJavascriptInterface(Object obj, String interfaceName) {
        if (!JS_BUG_HANDLE_FLAG) {
            super.addJavascriptInterface(obj, interfaceName);
        } else {
            if (TextUtils.isEmpty(interfaceName)) {
                return;
            }
            // 如果在4.2以上，直接调用基类的方法来注册
            if (isLargeJellyBean()) {
                super.addJavascriptInterface(obj, interfaceName);
                mJsInterfaceMap.put(interfaceName, obj);
            } else {
                mJsInterfaceMap.put(interfaceName, obj);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void removeJavascriptInterface(String interfaceName) {
        if (!JS_BUG_HANDLE_FLAG) {
            super.removeJavascriptInterface(interfaceName);
        } else {
            if (isLargeJellyBean()) {
                super.removeJavascriptInterface(interfaceName);
            } else {
                mJsInterfaceMap.remove(interfaceName);
                mJsStringCache = null;
                injectJavascriptInterfaces();
            }
        }
    }

    public void injectJavascriptInterfaces() {
        if (JS_BUG_HANDLE_FLAG) {
            if (!TextUtils.isEmpty(mJsStringCache)) {
                loadJavascriptInterfaces();
                return;
            }
            String jsString = genJavascriptInterfacesString();
            mJsStringCache = jsString;
            loadJavascriptInterfaces();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void loadJavascriptInterfaces() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.evaluateJavascript(mJsStringCache, null);
        } else {
            this.loadUrl(mJsStringCache);
        }
    }

    private String genJavascriptInterfacesString() {
        if (mJsInterfaceMap.size() == 0) {
            mJsStringCache = null;
            return null;
        }
        Iterator<Entry<String, Object>> iterator = mJsInterfaceMap.entrySet().iterator();
        StringBuilder script = new StringBuilder();
        script.append("javascript:(function JsAddJavascriptInterface_(){");
        try {
            while (iterator.hasNext()) {
                Entry<String, Object> entry = iterator.next();
                String interfaceName = entry.getKey();
                Object obj = entry.getValue();
                createJsMethod(interfaceName, obj, script);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        script.append("})()");

        return script.toString();
    }

    private void createJsMethod(String interfaceName, Object obj, StringBuilder script) {
        if (TextUtils.isEmpty(interfaceName) || (null == obj) || (null == script)) {
            return;
        }
        Class<? extends Object> objClass = obj.getClass();
        script.append("if(typeof(window.").append(interfaceName).append(")!='undefined'){");
        script.append("}else {");
        script.append("    window.").append(interfaceName).append("={");
        // Add methods
        Method[] methods = objClass.getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            // 过滤掉Object类的方法，包括getClass()方法，因为在Js中就是通过getClass()方法来得到Runtime实例
            if (filterMethods(methodName)) {
                continue;
            }
            script.append("        ").append(methodName).append(":function(");
            // 添加方法的参数
            int argCount = method.getParameterTypes().length;
            if (argCount > 0) {
                int maxCount = argCount - 1;
                for (int i = 0; i < maxCount; ++i) {
                    script.append(VAR_ARG_PREFIX).append(i).append(",");
                }
                script.append(VAR_ARG_PREFIX).append(argCount - 1);
            }
            script.append(") {");
            // Add implementation
            if (method.getReturnType() != void.class) {
                script.append("            return ").append("prompt('").append(MSG_PROMPT_HEADER).append("'+");
            } else {
                script.append("            prompt('").append(MSG_PROMPT_HEADER).append("'+");
            }
            // Begin JSON
            script.append("JSON.stringify({");
            script.append(KEY_INTERFACE_NAME).append(":'").append(interfaceName).append("',");
            script.append(KEY_FUNCTION_NAME).append(":'").append(methodName).append("',");
            script.append(KEY_ARG_ARRAY).append(":[");
            // 添加参数到JSON串中
            if (argCount > 0) {
                int max = argCount - 1;
                for (int i = 0; i < max; i++) {
                    script.append(VAR_ARG_PREFIX).append(i).append(",");
                }
                script.append(VAR_ARG_PREFIX).append(max);
            }
            // End JSON
            script.append("]})");
            // End prompt
            script.append(");");
            // End function
            script.append("        }, ");
        }
        // End of obj
        script.append("    };");
        // End of if or else
        script.append("}");
    }

    public boolean handleJsInterface(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject(message);
            String interfaceName = jsonObj.getString(KEY_INTERFACE_NAME);
            String methodName = jsonObj.getString(KEY_FUNCTION_NAME);
            JSONArray argsArray = jsonObj.getJSONArray(KEY_ARG_ARRAY);
            Object[] args = null;
            if (null != argsArray) {
                int count = argsArray.length();
                if (count > 0) {
                    args = new Object[count];

                    for (int i = 0; i < count; ++i) {
                        args[i] = argsArray.get(i);
                        if (args[i] == org.json.JSONObject.NULL) {
                            args[i] = "";
                        }
                    }
                }
            }

            if (invokeJSInterfaceMethod(result, interfaceName, methodName, args)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.cancel();
        return false;
    }

    private boolean invokeJSInterfaceMethod(JsPromptResult result, String interfaceName, String methodName, Object[] args) {

        boolean succeed = false;
        final Object obj = mJsInterfaceMap.get(interfaceName);
        if (null == obj) {
            result.cancel();
            return false;
        }

        Class<?>[] parameterTypes = null;
        int count = 0;
        if (args != null) {
            count = args.length;
        }

        if (count > 0) {
            parameterTypes = new Class[count];
            for (int i = 0; i < count; ++i) {
                parameterTypes[i] = getClassFromJsonObject(args[i]);
            }
        }

        try {
            Method method = obj.getClass().getMethod(methodName, parameterTypes);
            Object returnObj = method.invoke(obj, args); // 执行接口调用
            boolean isVoid = returnObj == null || returnObj.getClass() == void.class;
            String returnValue = isVoid ? "" : returnObj.toString();
            result.confirm(returnValue); // 通过prompt返回调用结果
            succeed = true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.cancel();
        return succeed;
    }

    private Class<?> getClassFromJsonObject(Object obj) {
        Class<?> cls = obj.getClass();

        // js对象只支持int boolean string三种类型
        if (cls == Integer.class) {
            cls = Integer.TYPE;
        } else if (cls == Boolean.class) {
            cls = Boolean.TYPE;
        } else {
            cls = String.class;
        }

        return cls;
    }

    private boolean filterMethods(String methodName) {
        for (String method : mFilterMethods) {
            if (method.equals(methodName)) {
                return true;
            }
        }
        return false;
    }

    private boolean isLargeHoneycomb() {
        if (JS_BUG_ALLVERSION_HANDLEFLAG) {
            return true;
        } else {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
        }
    }

    private boolean isLargeJellyBean() {
        if (JS_BUG_ALLVERSION_HANDLEFLAG) {
            return false;
        } else {
            return Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN;
        }
    }


    /***************************************************************************************************
     * 处理item，处理点击
     *
     * @param actionMode
     */
    private ActionMode resolveActionMode(ActionMode actionMode) {
        if (actionMode != null) {
            final Menu menu = actionMode.getMenu();
            mActionMode = actionMode;
            menu.clear();
            for (int i = 0; i < mActionList.size(); i++) {
                menu.add(mActionList.get(i));
            }
            for (int i = 0; i < menu.size(); i++) {
                MenuItem menuItem = menu.getItem(i);
                menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        getSelectedData((String) item.getTitle());
                        releaseAction();
                        return true;
                    }
                });
            }
        }
        mActionMode = actionMode;
        return actionMode;
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback) {
        ActionMode actionMode = super.startActionMode(callback);
        return resolveActionMode(actionMode);
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback, int type) {
        ActionMode actionMode = super.startActionMode(callback, type);
        return resolveActionMode(actionMode);
    }

    private void releaseAction() {
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
        }
    }

    /**
     * 点击的时候，获取网页中选择的文本，回掉到原生中的js接口
     *
     * @param title 传入点击的item文本，一起通过js返回给原生接口
     */
    private void getSelectedData(String title) {

        String js = "(function getSelectedText() {" +
                "var txt;" +
                "var title = \"" + title + "\";" +
                "if (window.getSelection) {" +
                "txt = window.getSelection().toString();" +
                "} else if (window.document.getSelection) {" +
                "txt = window.document.getSelection().toString();" +
                "} else if (window.document.selection) {" +
                "txt = window.document.selection.createRange().text;" +
                "}" +
                "JSInterface.callback(txt,title);" +
                "})()";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript("javascript:" + js, null);
        } else {
            loadUrl("javascript:" + js);
        }
    }

    public void linkTextSelectedInterface() {
        addJavascriptInterface(new ActionSelectInterface(this), "JSInterface");
    }

    /**
     * 设置弹出action列表
     *
     * @param actionList
     */
    public void setActionList(List<String> actionList) {
        mActionList = actionList;
    }

    /**
     * 设置点击回掉
     *
     * @param actionSelectListener
     */
    public void setActionSelectListener(ActionSelectListener actionSelectListener) {
        this.mActionSelectListener = actionSelectListener;
    }

    /**
     * 隐藏消失Action
     */
    public void dismissAction() {
        releaseAction();
    }


    /**
     * js选中的回掉接口
     */
    private class ActionSelectInterface {

        CustomWebView mContext;

        ActionSelectInterface(CustomWebView c) {
            mContext = c;
        }

        @JavascriptInterface
        public void callback(final String value, final String title) {
            if (mActionSelectListener != null) {
                mActionSelectListener.onClick(title, value);
            }
        }
    }
    /**
     ****************************************************************************************************
     */
}
