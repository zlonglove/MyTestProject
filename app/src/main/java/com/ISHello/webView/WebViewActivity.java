package com.ISHello.webView;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.MainThread;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ISHello.Constants.Constants;
import com.ISHello.View.ICBCDialog;
import com.ISHello.base.base.BaseActivity;
import com.ISHello.keyboard.creditkeyboard.CreditKeyboardBinder;
import com.ISHello.utils.LogUtil;
import com.androidquery.AQuery;
import com.example.ishelloword.R;
import com.example.updateversion.LaunchAppServices;

import org.apache.commons.httpclient.util.EncodingUtil;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * webView的详细用法
 *
 * @author zhanglong
 */
public class WebViewActivity extends BaseActivity implements DownloadListener {
    private final String TAG = "WebViewActivity";
    private EditText editText;
    private Button button;
    private Button jsButton;
    private Button javaToJavaScript;
    private Button javaCallAlert;
    private CustomWebView webView;
    //private LinearLayout loadView;
    //private TextView message;
    private Dialog loadingDialog;
    private HttpAuthenticationDialog mHttpAuthenticationDialog;
    private NativeWebviewBaseProxy proxy;
    private String mMethod;
    private String mUrl;

    private CreditKeyboardBinder creditKeyboardBinder;
    //private final String loadUrl = "http://gdown.baidu.com/data/wisegame/91319a5a1dfae322/baidu_16785426.apk";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.webview_activity_layout);
        findViews();
        initViews();
        /*LaunchAppServices launchAppServices = new LaunchAppServices(this, "", "", loadUrl, getFileName(loadUrl));
        launchAppServices.downLoadApk();*/
    }

    private void findViews() {
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        jsButton = (Button) findViewById(R.id.jsbutton);
        javaToJavaScript = (Button) findViewById(R.id.javaToJavaScript);
        javaCallAlert = (Button) findViewById(R.id.javaCallAlert);
        webView = (CustomWebView) findViewById(R.id.webView);
        //loadView = (LinearLayout) this.findViewById(R.id.loadView);
        //message = (TextView) this.findViewById(R.id.message);
    }

    private void initViews() {
        loadingDialog = ICBCDialog.getProgressDialog(this, ICBCDialog.ProgressDialogType.ICBC3DLogo);
        webView.setDownloadListener(this);//文件下载
        mMethod = getIntent().getStringExtra("method");
        mUrl = getIntent().getStringExtra("url");
        initWebView();
        button.setOnClickListener(new OnClickListener() {
            /**
             * 同样为按钮绑定点击事件
             */
            public void onClick(View v) {
                openBrowser();
            }
        });
        editText.setOnKeyListener(new OnKeyListener() {
            /**
             * 同样为编辑框绑定键盘事件
             */
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    openBrowser();
                    return true;
                }
                return false;
            }

        });
        jsButton.setOnClickListener(new OnClickListener() {//加载html网页
            @Override
            public void onClick(View v) {
                webView.loadUrl("file:///android_asset/data/js.html");
                //webView.loadUrl("file:///android_asset/data/test.html");
            }
        });
        javaToJavaScript.setOnClickListener(new OnClickListener() {//java调用javaScript
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript: " + "javaCallScriptArgs" + "('" + "i am args" + "')");//带参数调用
                webView.loadUrl("javascript: " + "javacalljs()");//不带参数调用
                //callJavascript("javascript:" + "javacalljs()");
                callJavascript("javascript:" + "javaCallScriptForReturn()");
            }
        });
        javaCallAlert.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //webView.loadUrl("javascript: " + "javacallAlert()");
                webView.loadUrl("javascript: " + "javaCallScriptArgs" + "('" + "i am args" + "')");//带参数调用
            }
        });

        if (!TextUtils.isEmpty(mMethod)) {
            if ("post".equals(mMethod)) {
                postLoadWebview(mUrl);
            } else {
                getLoadWebview(mUrl);
            }
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            creditKeyboardBinder = new CreditKeyboardBinder(this);
            creditKeyboardBinder.registerEditText(editText);
        }
    }

    /**
     * 利用webView的loadUrl方法
     */
    public void openBrowser() {
        webView.loadUrl("http://" + editText.getText().toString());
    }

    /**
     * post方式提交参数，自动截取url中？后面的参数
     *
     * @param url
     */
    public void postLoadWebview(String url) {
        if (url != null) {
            if (url.indexOf("?") != -1) {
                String postUrl = url.substring(0, url.indexOf("?"));
                String param = url.substring(url.indexOf("?") + 1);
                webView.postUrl(postUrl, EncodingUtil.getBytes(param, "UTF-8"));
            }
        }
    }

    /**
     * get方式
     *
     * @param url
     */
    public void getLoadWebview(String url) {
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
    }

    /**
     * webView初始化
     */
    private void initWebView() {

        WebSettings settings = webView.getSettings();
        String defaultUA = settings.getUserAgentString();
        //Mozilla/5.0 (Linux; Android 7.0; HUAWEI NXT-TL00 Build/HUAWEINXT-TL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36
        settings.setUserAgentString(defaultUA + " " + Constants.WebViewUA);//添加UA,是与网页商量好的标识，页面确认UA为这个就认为该请求的终端为App
        LogUtil.log(TAG, "WebView New UA==" + settings.getUserAgentString());

        settings.setJavaScriptEnabled(true);

        //设置参数
        settings.setBuiltInZoomControls(true);//设置可以缩放
        settings.setAppCacheEnabled(true);// 设置缓存

        settings.setDefaultTextEncodingName("UTF-8");
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        proxy = new NativeWebviewCoreProxy(this, webView, extendHandler);
        CustomWebChromeClient customWebChromeClient = new CustomWebChromeClient(this, proxy);
        //webView.setWebChromeClient(new MyWebChromeClient());
        /**
         * 设置WebChromClient扩展类
         */
        webView.setWebChromeClient(customWebChromeClient);
        /**
         * 设置WebViewClient扩展类
         */
        webView.setWebViewClient(new MyWebViewClient());
        /**
         * 添加js接口
         */
        webView.addJavascriptInterface(proxy, "Native");

        webView.addJavascriptInterface(new ImageLoadingInterface(this), "imagelistener");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        initWebTextSelected();
    }

    private void initWebTextSelected() {
        List<String> list = new ArrayList<>();
        list.add("Item1");
        list.add("Item2");
        list.add("APIWeb");
        //设置item
        webView.setActionList(list);

        //链接js注入接口，使能选中返回数据
        webView.linkTextSelectedInterface();
        //增加点击回调
        webView.setActionSelectListener(new ActionSelectListener() {
            @Override
            public void onClick(String title, String selectText) {
                if (title.equals("APIWeb")) {
                    Intent intent = new Intent(WebViewActivity.this, APIWebViewActivity.class);
                    startActivity(intent);
                    return;
                }
                LogUtil.log("Click Item:  + title + 。\n\\nValue: " + selectText);
                showToast("Click Item: " + title + "。\n\nValue: " + selectText, Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * Navite-JS接口Handler处理类
     */
    private Handler extendHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch ((NativeWebviewCoreProxy.HandleType) msg.obj) {
                case SHOW_INDICATOR:
                    if (loadingDialog != null && (!loadingDialog.isShowing())) {
                        loadingDialog.show();
                    }
                    break;
                case HIDE_INDICATOR:
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    break;
            }
        }
    };

    /**
     * 按back键可以回到上个网页
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("WebViewActivity", "--->onKeyDown");
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (webView != null) {
                webView.dismissAction();
                webView.onPause(); // 暂停网页中正在播放的视频
            }
        }
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        LogUtil.log(TAG, "url=" + url);
        LogUtil.log(TAG, "userAgent=" + userAgent);
        LogUtil.log(TAG, "contentDisposition=" + contentDisposition);
        LogUtil.log(TAG, "mimetype=" + mimetype);
        LogUtil.log(TAG, "contentLength=" + contentLength);
        String fileName = getFileName(url);
        LogUtil.log(TAG, "fileName=" + fileName);
        if (url.endsWith(".apk")) {
            //String url="http://s0.cyberciti.org/images/misc/static/2012/11/ifdata-welcome-0.png"; //支持多线程下载url
            LaunchAppServices launchAppServices = new LaunchAppServices(this, "", "", url, fileName);
            launchAppServices.downLoadApk();
        } else {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }


    public String getFileName(String path) {
        int start = path.lastIndexOf("/") + 1;
        return path.substring(start, path.length());
    }

    /**
     * 监听WebView的各种通知，请求。可以使用一个自定义的WebViewClient
     *
     * @author zhanglong
     */
    private class MyWebViewClient extends WebViewClient {
        public MyWebViewClient() {
            Log.i(TAG, "--->MyWebViewClient()");
        }

        /**
         * 在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次
         */
        @Override
        public void onLoadResource(WebView view, String url) {
            //Log.i(TAG, "--->onLoadResource()");
            super.onLoadResource(view, url);
        }

        /**
         * 在页面加载开始时调用
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.i(TAG, "--->onPageStarted()--url=" + url);
            loadingDialog.show();
            //checkWebViewUrl(view, url);
            super.onPageStarted(view, url, favicon);
        }

        /**
         * 页面加载结束时调用
         */
        @Override
        public void onPageFinished(WebView view, String url) {

            Log.i(TAG, "--->onPageFinished()--url=" + url);
            super.onPageFinished(view, url);
            loadingDialog.dismiss();
            addImageClickListener(view);//待网页加载完全后设置图片点击的监听方法
        }

        /**
         * 重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转,不跳到浏览器那边
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 步骤2：根据协议的参数，判断是否是所需要的url
            // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
            //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）

            Uri uri = Uri.parse(url);
            // 如果url的协议 = 预先约定的 js 协议
            // 就解析往下解析参数
            if (uri.getScheme().equals("js")) {
                // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                // 所以拦截url,下面JS开始调用Android需要的方法
                if (uri.getAuthority().equals("webview")) {
                    //步骤3：
                    //执行JS所需要调用的逻辑
                    LogUtil.log("js调用了Android的方法");
                    //可以在协议上带有参数并传递到Android上
                    HashMap<String, String> params = new HashMap<>();
                    Set<String> collection = uri.getQueryParameterNames();
                    Iterator it = collection.iterator();
                    while (it.hasNext()) {
                        String paramsName = (String) it.next();
                        String paramsValue = uri.getQueryParameter(paramsName);
                        params.put(paramsName, paramsValue);
                        LogUtil.log("--->paramsName==" + paramsName + "/" + paramsValue);

                    }

                }

                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        /**
         * 网页加载失败时调用
         */
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            Log.i(TAG, "--->onReceivedError() ");
            //super.onReceivedError(view, errorCode, description, failingUrl);
            showErrorPannel(getString(R.string.netWork_error));
        }


        /**
         * 重写此方法可以让webview处理https请求
         */
        public void onReceivedSslError(WebView paramWebView,
                                       SslErrorHandler paramSslErrorHandler, SslError paramSslError) {
            Log.i(TAG, "--->onReceivedSslError()");
            paramSslErrorHandler.proceed();
            showErrorPannel(getString(R.string.ssl_failed_msg));
            //super.onReceivedSslError(paramWebView, paramSslErrorHandler, paramSslError);
        }

        /**
         * 重写此方法才能够处理在浏览器中的按键事件
         */
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            Log.i(TAG, "--->shouldOverrideKeyEvent()" + event.getKeyCode());
            return super.shouldOverrideKeyEvent(view, event);
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view,
                                              HttpAuthHandler handler, String host, String realm) {
            Log.i(TAG, "--->onReceivedHttpAuthRequest()");
            String username = null;
            String password = null;
            boolean reuseHttpAuthUsernamePassword = handler.useHttpAuthUsernamePassword();
            if (reuseHttpAuthUsernamePassword && view != null) {
                String[] credentials = view.getHttpAuthUsernamePassword(host, realm);
                if (credentials != null && credentials.length == 2) {
                    username = credentials[0];
                    password = credentials[1];
                }
            }
            if (username != null && password != null) {
                handler.proceed(username, password);
            } else {
                WebViewActivity.this.showHttpAuthentication(handler, host, realm);
            }
        }

        private void addImageClickListener(WebView webView) {
            webView.loadUrl("javascript:(function(){" +
                    "var objs = document.getElementsByTagName(\"img\"); " +
                    "for(var i=0;i<objs.length;i++) " +
                    "{"
                    + " objs[i].onclick=function() " +
                    " { "
                    + "  window.imagelistener.openImage(this.src); " +//通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
                    " } " +
                    "}" +
                    "})()");
        }
    }


    /**
     * Displays an http-authentication dialog.
     */
    void showHttpAuthentication(final HttpAuthHandler handler, String host, String realm) {
        mHttpAuthenticationDialog = new HttpAuthenticationDialog(WebViewActivity.this, host, realm);
        mHttpAuthenticationDialog.setOkListener(new HttpAuthenticationDialog.OkListener() {
            public void onOk(String host, String realm, String username, String password) {
                setHttpAuthUsernamePassword(host, realm, username, password);
                handler.proceed(username, password);
                mHttpAuthenticationDialog = null;
            }
        });
        mHttpAuthenticationDialog.setCancelListener(new HttpAuthenticationDialog.CancelListener() {
            public void onCancel() {
                handler.cancel();
                mHttpAuthenticationDialog = null;
            }
        });
        mHttpAuthenticationDialog.show();
    }

    /**
     * Set HTTP authentication password.
     *
     * @param host     The host for the password
     * @param realm    The realm for the password
     * @param username The username for the password. If it is null, it means password can't be saved.
     * @param password The password
     */
    public void setHttpAuthUsernamePassword(String host, String realm,
                                            String username,
                                            String password) {
        WebView w = webView;
        if (w != null) {
            w.setHttpAuthUsernamePassword(host, realm, username, password);
        }
    }

    /**
     * 检查当前url是不是可以有效访问
     *
     * @param webView
     * @param url
     */
    public void checkWebViewUrl(final WebView webView, final String url) {
        if (url == null || url.equals("")) {
            return;
        }
        new AsyncTask<String, Void, Integer>() {
            @Override
            protected Integer doInBackground(String... params) {
                int responseCode = -1;
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    responseCode = connection.getResponseCode();
                } catch (Exception e) {
                    Log.e(TAG, "--->Loading webView error:" + e.getMessage());
                }
                return responseCode;
            }

            @Override
            protected void onPostExecute(Integer result) {
                if (result != HttpURLConnection.HTTP_OK) {
                    showErrorPannel(getString(R.string.server_connect_error));
                }
            }
        }.execute(url);
    }

    /**
     * @param jsString
     */
    @MainThread
    public void callJavascript(String jsString) {
        if (webView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.evaluateJavascript(jsString, new ValueCallback<String>() {
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    @Override
                    public void onReceiveValue(String value) {
                        LogUtil.log(TAG, "evaluateJavascript onReceiveValue()==" + value);
                    }
                });
            } else {
                webView.loadUrl(jsString);
            }
        }
    }

    public class ImageLoadingInterface {
        private Context context;

        public ImageLoadingInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String img) {
            Log.i(TAG, "--->img==" + img);
            Intent intent = new Intent();
            intent.putExtra("image", img);
            intent.setClass(context, ShowWebImageActivity.class);
            context.startActivity(intent);
        }
    }

    public void showErrorPannel(String msg) {
        PopupWindow errorPopupWindow;
        try {
            View root = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
            RelativeLayout error_layout = (RelativeLayout) inflateView(R.layout.view_error);
            AQuery aQuery = new AQuery(error_layout);
            aQuery.id(R.id.navbar_title).gone();
            aQuery.id(R.id.navbar_right_btn).gone();
            aQuery.id(R.id.tip_text).text(msg);
            aQuery.id(R.id.navbar_left_btn).clicked(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebViewActivity.this.finish();
                }
            });
            errorPopupWindow = new PopupWindow(error_layout, android.view.ViewGroup.LayoutParams.MATCH_PARENT, root.getMeasuredHeight(), true);
            errorPopupWindow.setOutsideTouchable(true);
            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            errorPopupWindow.showAtLocation(root, Gravity.TOP | Gravity.LEFT, 0, frame.top);
        } catch (Exception e) {
        }
    }
}
