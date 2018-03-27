package com.example.ishelloword.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ISHello.Constants.Constants;
import com.ISHello.CustomToast.CustomToast;
import com.android.ddmlib.Log;
import com.example.ishelloword.R;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信客户端回调activity示例
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private final String TAG = "WXEntryActivity";
    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
    /**
     * IWXAPI 是第三方app和微信通信的openapi接口
     */
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID, false);
        api.handleIntent(getIntent(), this);
        // 将该app注册到微信
        api.registerApp(Constants.WECHAT_APP_ID);
    }

    /**
     * 启动微信
     *
     * @return
     */
    public boolean LunchWX() {
        return api.openWXApp();
    }

    /**
     * 检查是不是支持分享到朋友圈
     *
     * @return
     */
    public boolean isSupportCircle() {
        int wxSdkVersion = api.getWXAppSupportAPI();
        if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION) {
            CustomToast.makeText(WXEntryActivity.this, "wxSdkVersion = " + Integer.toHexString(wxSdkVersion) + "\ntimeline supported", Toast.LENGTH_LONG);
            return true;
        } else {
            CustomToast.makeText(WXEntryActivity.this, "wxSdkVersion = " + Integer.toHexString(wxSdkVersion) + "\ntimeline not supported", Toast.LENGTH_LONG);
            return false;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        // TODO Auto-generated method stub
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                Log.i(TAG, "--->command getmessage from wx");
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                Log.i(TAG, "--->command showmessage from wx");
                break;
            default:
                break;
        }
    }

    //第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        int result;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                result = R.string.errcode_failed;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }
        CustomToast.makeText(this, result, Toast.LENGTH_LONG);
        finish();//关闭当前activity
    }
}
