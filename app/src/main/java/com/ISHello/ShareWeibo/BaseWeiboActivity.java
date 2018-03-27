package com.ISHello.ShareWeibo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.ISHello.Constants.Constants;
import com.ISHello.CustomToast.CustomToast;
import com.example.ishelloword.R;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;

import java.text.SimpleDateFormat;

public class BaseWeiboActivity extends Activity implements IWeiboHandler.Response {
    private AuthInfo mAuthInfo;
    /**
     * 微博分享的接口实例
     */
    private IWeiboShareAPI mWeiboShareAPI;
    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;

    /**
     * 认证完成回调接口
     */
    private AuthCallBack authCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(savedInstanceState);
    }

    /**
     * 初始化 UI 和微博接口实例 。
     */
    private void initialize(Bundle savedInstanceState) {
        // 创建微博 SDK 接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.APP_KEY);

        // 注册到新浪微博
        mWeiboShareAPI.registerApp();

        // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
        // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
        // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
        // 失败返回 false，不调用上述回调
        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }
    }

    /**
     * @see {@link Activity#onNewIntent}
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    public void setAuthLisetener(AuthCallBack callBack) {
        this.authCallBack = callBack;
    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    // SSO 授权, ALL IN ONE   如果手机安装了微博客户端则使用客户端授权,没有则进行网页授权
    private void auth() {
        mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(this, mAuthInfo);
        // 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
        // 第一次启动本应用，AccessToken 不可用
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (null == mAccessToken || !mAccessToken.isSessionValid()) {
            CustomToast.makeText(this, "请先授权!", Toast.LENGTH_SHORT);
            mSsoHandler.authorize(new AuthListener());
        } else {
            if (authCallBack != null) {
                authCallBack.success();
            }
        }
    }

    public boolean isInstalled() {
        return mWeiboShareAPI.isWeiboAppInstalled();
    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(final Bundle values) {
            // 从 Bundle 中解析 Token
            BaseWeiboActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken = Oauth2AccessToken.parseAccessToken(values);
                    //从这里获取用户输入的 电话号码信息
                    String phoneNum = mAccessToken.getPhoneNum();
                    if (mAccessToken.isSessionValid()) {
                        // 显示 Token
                        //updateTokenView(false);

                        // 保存 Token 到 SharedPreferences
                        AccessTokenKeeper.writeAccessToken(BaseWeiboActivity.this, mAccessToken);
                        CustomToast.makeText(BaseWeiboActivity.this, R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_SHORT);
                        if (authCallBack != null) {
                            authCallBack.success();
                        }
                    } else {
                        // 以下几种情况，您会收到 Code：
                        // 1. 当您未在平台上注册的应用程序的包名与签名时；
                        // 2. 当您注册的应用程序包名与签名不正确时；
                        // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                        String code = values.getString("code");
                        String message = getString(R.string.weibosdk_demo_toast_auth_failed);
                        if (!TextUtils.isEmpty(code)) {
                            message = message + "\nObtained the code: " + code;
                        }
                        CustomToast.makeText(BaseWeiboActivity.this, message, Toast.LENGTH_LONG);
                        if (authCallBack != null) {
                            authCallBack.fail();
                        }
                    }
                }
            });

        }

        @Override
        public void onCancel() {
            CustomToast.makeText(BaseWeiboActivity.this, R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG);
        }

        @Override
        public void onWeiboException(WeiboException e) {
            CustomToast.makeText(BaseWeiboActivity.this,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG);
        }
    }

    /**
     * 显示当前 Token 信息。
     *
     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
     */
    private void updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        String format = getString(R.string.weibosdk_demo_token_to_string_format_1);

        CustomToast.makeText(BaseWeiboActivity.this, String.format(format, mAccessToken.getToken(), date), Toast.LENGTH_LONG);

        String message = String.format(format, mAccessToken.getToken(), date);
        if (hasExisted) {
            message = getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message;
        }
        CustomToast.makeText(BaseWeiboActivity.this, message, Toast.LENGTH_LONG);
    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    protected void sendTextObj(final String text) {
        if (isInstalled()) {
            AuthCallBack callBack = new AuthCallBack() {
                @Override
                public void success() {
                    TextObject textObject = new TextObject();
                    textObject.text = text;
                    WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                    weiboMessage.textObject = textObject;
                    sendMultiMessage(weiboMessage);
                }

                @Override
                public void fail() {

                }
            };
            setAuthLisetener(callBack);
            auth();
        } else {
            CustomToast.makeText(BaseWeiboActivity.this, "没有安装新浪微博客户端", Toast.LENGTH_LONG);
        }
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    protected void sendImageObj(final Bitmap bitmap) {
        long size = getBitmapsize(bitmap);
        if (size > 32 * 1024) {
            CustomToast.makeText(BaseWeiboActivity.this, "图片大小超过上限", Toast.LENGTH_LONG);
            return;
        }
        if (isInstalled()) {
            AuthCallBack callBack = new AuthCallBack() {
                @Override
                public void success() {
                    ImageObject imageObject = new ImageObject();
                    //设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
                    //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                    imageObject.setImageObject(bitmap);
                    WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                    weiboMessage.imageObject = imageObject;
                    sendMultiMessage(weiboMessage);
                }

                @Override
                public void fail() {

                }
            };
            setAuthLisetener(callBack);
            auth();
        } else {
            CustomToast.makeText(BaseWeiboActivity.this, "没有安装新浪微博客户端", Toast.LENGTH_LONG);
        }
    }


    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    private void sendMultiMessage(WeiboMultiMessage weiboMultiMessage) {
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMultiMessage;
        // 3. 发送请求消息到微博，唤起微博分享界面
        /*if (mShareType == SHARE_CLIENT) {
            mWeiboShareAPI.sendRequest(WBShareActivity.this, request);
        }*/
        AuthInfo authInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        mWeiboShareAPI.sendRequest(this, request, authInfo, token, new WeiboAuthListener() {

            @Override
            public void onWeiboException(WeiboException arg0) {
            }

            @Override
            public void onComplete(Bundle bundle) {
                // TODO Auto-generated method stub
                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(getApplicationContext(), newToken);
                CustomToast.makeText(getApplicationContext(), "onAuthorizeComplete token = " + newToken.getToken(), Toast.LENGTH_SHORT);
            }

            @Override
            public void onCancel() {
            }
        });

    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        if (baseResponse != null) {
            switch (baseResponse.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    CustomToast.makeText(this, R.string.weibosdk_demo_toast_share_success, Toast.LENGTH_LONG);
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    CustomToast.makeText(this, R.string.weibosdk_demo_toast_share_canceled, Toast.LENGTH_LONG);
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    CustomToast.makeText(this,
                            getString(R.string.weibosdk_demo_toast_share_failed) + "Error Message: " + baseResponse.errMsg,
                            Toast.LENGTH_LONG);
                    break;
            }
        }
    }

    public long getBitmapsize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }
}
