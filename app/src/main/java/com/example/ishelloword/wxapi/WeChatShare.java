package com.example.ishelloword.wxapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ISHello.Constants.Constants;
import com.ISHello.CustomToast.CustomToast;
import com.example.ishelloword.R;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by kfzx-zhanglong on 2016/7/14.
 * Company ICBC
 */
public class WeChatShare implements IWXAPIEventHandler {
    private final String TAG = "WeChatShare";
    private IWXAPI mWXApi;
    private Context mContext;
    private boolean mIsTimeline = false;

    private final int THUMB_SIZE = 150;

    private final String TEXT = "text";
    private final String IMG = "img";
    private final String URL = "webpage";
    private final String MUSIC = "music";
    private final String VIDEO = "video";

    public WeChatShare(Context context) {
        mContext = context;
        mWXApi = WXAPIFactory.createWXAPI(context, Constants.WECHAT_APP_ID);
        auth();
    }

    public boolean isInstalled() {
        return mWXApi.isWXAppInstalled();
    }

    /**
     * 注册
     */
    public void auth() {
        if (!mWXApi.registerApp(Constants.WECHAT_APP_ID)) {
            CustomToast.makeText(mContext, R.string.wechatregisterAppFail, Toast.LENGTH_LONG);
        }
    }

    /**
     * 方法简介：设置是否分享到朋友圈
     * 输入项说明：flag为true表示分享到朋友圈, 否则分享到会话
     * 返回项说明：
     *
     * @param flag true: 朋友圈; false: 会话
     */
    public void setTimeline(boolean flag) {
        mIsTimeline = flag;
    }

    /**
     * 分享文字
     *
     * @param text--要分享的文字
     */
    public void shareText(String text) {
        if (!isInstalled()) {
            CustomToast.makeText(mContext, R.string.wechatuninstall_tip, Toast.LENGTH_LONG);
            return;
        }
        if (null == text || TextUtils.isEmpty(text)) {
            CustomToast.makeText(mContext, R.string.share_text_null, Toast.LENGTH_SHORT);
            return;
        }

        WXTextObject textObject = new WXTextObject();
        textObject.text = text;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject;
        msg.description = text;

        sendReq(msg, mIsTimeline, TEXT);
    }

    /**
     * 分享图片
     *
     * @param image--要分享的图片的bitmap
     */
    public void shareImage(Bitmap image) {
        if (!isInstalled()) {
            CustomToast.makeText(mContext, R.string.wechatuninstall_tip, Toast.LENGTH_LONG);
            return;
        }
        WXImageObject imgObj = new WXImageObject(image);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        Bitmap thumb = Bitmap.createScaledBitmap(image, THUMB_SIZE, THUMB_SIZE, true);
        msg.thumbData = Util.bmpToByteArray(thumb, true); // 设置缩略图

        sendReq(msg, mIsTimeline, IMG);
    }

    /**
     * 分享音乐
     *
     * @param musicUrl
     * @param title
     * @param description
     */
    public void shareMusic(String musicUrl, String title, String description) {
        if (!isInstalled()) {
            CustomToast.makeText(mContext, R.string.wechatuninstall_tip, Toast.LENGTH_LONG);
            return;
        }
        WXMusicObject music = new WXMusicObject();
        //music.musicUrl = "http://www.baidu.com";
        music.musicUrl = musicUrl;
        //music.musicUrl="http://120.196.211.49/XlFNM14sois/AKVPrOJ9CBnIN556OrWEuGhZvlDF02p5zIXwrZqLUTti4o6MOJ4g7C6FPXmtlh6vPtgbKQ==/31353278.mp3";

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = title;
        msg.description = description;

        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.wechat_icon_small);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        sendReq(msg, mIsTimeline, MUSIC);
    }

    /**
     * 分享视频
     *
     * @param videoUrl
     * @param title
     * @param description
     */
    public void shareVideo(String videoUrl, String title, String description) {
        if (!isInstalled()) {
            CustomToast.makeText(mContext, R.string.wechatuninstall_tip, Toast.LENGTH_LONG);
            return;
        }
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = videoUrl;

        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = title;
        msg.description = description;
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.wechat_icon_small);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        sendReq(msg, mIsTimeline, VIDEO);
    }

    public void shareUrl(String url) {
        shareUrl(url, "");
    }

    public void shareUrl(String url, String title) {
        shareUrl(url, title, "");
    }

    public void shareUrl(String url, String title, String description) {
        if (!isInstalled()) {
            CustomToast.makeText(mContext, R.string.wechatuninstall_tip, Toast.LENGTH_LONG);
            return;
        }
        WXWebpageObject wpObj = new WXWebpageObject();
        wpObj.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(wpObj);
        msg.title = title;
        if (TextUtils.isEmpty(title)) {
            msg.description = description;
        } else {
            msg.description = mContext.getString(R.string.sharewechat_title);
        }
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.wechat_icon_small);
        msg.thumbData = Util.bmpToByteArray(bitmap, true);
        int size = msg.thumbData.length;
        if (size >= 32 * 1024) {    // thumbData greater than 32kb
            Bitmap m = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.wechat_icon_small);
            msg.thumbData = Util.bmpToByteArray(m, true);
        }
        sendReq(msg, mIsTimeline, URL);
    }

    private void sendReq(WXMediaMessage msg, final boolean isTimeline, final String type) {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        // transaction字段唯一标识一个请求
        req.transaction = buildTransaction(type);
        req.message = msg;
        req.scene = isTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        Log.i(TAG, "--->sendReq()" + msg.title);
        if (mWXApi.sendReq(req)) {
            Log.i(TAG, "--->sendReq success");
        } else {
            Log.i(TAG, "--->sendReq fail");
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        int result;
        switch (baseResp.errCode) {
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
        CustomToast.makeText(mContext, result, Toast.LENGTH_LONG);
    }
}
