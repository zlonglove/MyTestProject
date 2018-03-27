package com.ISHello.base.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.ISHello.AppManager.AppManager;
import com.ISHello.Application.BaseApplication;
import com.ISHello.CustomToast.CustomToast;
import com.ISHello.base.net.AsyncFailedCallBack;
import com.ISHello.base.net.AsyncPreCall;
import com.ISHello.base.net.AsyncSuccessCallBack;
import com.ISHello.base.net.HttpReqEntity;
import com.ISHello.base.service.NewAsyncTaskServices;
import com.androidquery.AQuery;

/**
 * Created by zhanglong on 2017/2/9.
 */

public class BaseActivity extends AppCompatActivity {
    protected AQuery aQuery;
    private NewAsyncTaskServices asyncTaskServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aQuery = new AQuery(this);
        /**
         * 将Activity添加到管理类中
         */
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseApplication myApplaction = (BaseApplication) getApplication();
        if ((myApplaction.isLocked) /*&& (sp.getString("gesturepw", "").equals("开"))*/) {// 判断是否需要跳转到密码界面
            /*Intent intent = new Intent(this, UnlockGesturePasswordActivity.class);
            startActivity(intent);*/
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 从管理类中删除当前Activity
         */
        AppManager.getAppManager().finishActivity(this);

    }


    /**
     * 必须传入一个Layout的资源id
     *
     * @param id
     * @return
     */
    protected View inflateView(@LayoutRes int id) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(id, null);
        return view;
    }

    /**
     * @param stringId
     * @StringRes标明传入的参数必须是一个字符串id
     */
    public void showToast(@StringRes int stringId, int duration) {
        CustomToast.makeText(this, stringId, duration);
    }

    public void showToast(String message, int duration) {
        CustomToast.makeText(this, message, duration);
    }

    protected void doAsync(HttpReqEntity req, AsyncSuccessCallBack successCallBack, AsyncFailedCallBack failedCallBack) {
        asyncTaskServices = new NewAsyncTaskServices(this, req, null, successCallBack, failedCallBack);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            asyncTaskServices.executeOnExecutor(NewAsyncTaskServices.FULL_TASK_EXECUTOR, (Void[]) null);
        } else {
            asyncTaskServices.execute((Void[]) null);
        }
    }

    protected void doAsync(HttpReqEntity req, AsyncPreCall preCall, AsyncSuccessCallBack successCallBack, AsyncFailedCallBack failedCallBack) {
        asyncTaskServices = new NewAsyncTaskServices(this, req, preCall, successCallBack, failedCallBack);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            asyncTaskServices.executeOnExecutor(NewAsyncTaskServices.FULL_TASK_EXECUTOR, (Void[]) null);
        } else {
            asyncTaskServices.execute((Void[]) null);
        }
    }

}
