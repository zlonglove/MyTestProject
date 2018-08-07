package com.ws.mediaprojectionmediamuxer;

import android.content.Intent;
import android.media.projection.MediaProjectionManager;


public class MyApplication {

    public static Intent getResultIntent() {
        return ResultIntent;
    }

    public static void setResultIntent(Intent mResultIntent) {
        ResultIntent = mResultIntent;
    }

    public static int getResultCode() {
        return ResultCode;
    }

    public static void setResultCode(int mResultCode) {
        ResultCode = mResultCode;
    }

    private static Intent ResultIntent = null;
    private static int ResultCode = 0;

    public static MediaProjectionManager getMpmngr() {
        return Mpmngr;
    }

    public static void setMpmngr(MediaProjectionManager mMpmngr) {
        Mpmngr = mMpmngr;
    }

    private static MediaProjectionManager Mpmngr;


}
