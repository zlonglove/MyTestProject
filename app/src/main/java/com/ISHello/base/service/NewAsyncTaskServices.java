package com.ISHello.base.service;

/**
 * Created by zhanglong on 2017/2/9.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import com.ISHello.View.ICBCDialog;
import com.ISHello.base.net.AsyncFailedCallBack;
import com.ISHello.base.net.AsyncPreCall;
import com.ISHello.base.net.AsyncSuccessCallBack;
import com.ISHello.base.net.HttpReqEntity;
import com.ISHello.base.net.HttpRespEntity;
import com.ISHello.base.tools.TransactionService;
import com.ISHello.base.tools.TransactionService.TransactionType;
import com.ISHello.base.ui.BaseDialog;
import com.ISHello.base.ui.BaseToast;
import com.example.ishelloword.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ISHello.View.CustomDialog.getMessageDialog;

/**
 * if (asyncTaskServices != null && asyncTaskServices.getStatus() == AsyncTask.Status.RUNNING) {
 * //但是需要注意的是,调用cancel方法并不能真正立即把task取消掉,而只是把task的状态置为Cancel而已,需要配合isCancelled()方法来运用,
 * //在doingbackground或其他方法中判断是否被取消,然后做相应的处理.
 * //传true表示task应该被中断,false代表进行中的task被允许执行完毕
 * asyncTaskServices.cancel(true);
 * }
 */
public class NewAsyncTaskServices extends AsyncTask<Void, String, HttpRespEntity> {

    private Context context;
    private AsyncPreCall preCall;
    private AsyncSuccessCallBack successCallBack;
    private AsyncFailedCallBack failedCallBack;
    private HttpReqEntity req;
    private Dialog progressDialog;
    /**
     * 所有交易共享线程池
     */
    public static ExecutorService SINGLE_TASK_EXECUTOR;
    public static ExecutorService LIMITED_TASK_EXECUTOR;
    public static ExecutorService FULL_TASK_EXECUTOR;

    static {
        SINGLE_TASK_EXECUTOR = Executors.newSingleThreadExecutor();
        LIMITED_TASK_EXECUTOR = Executors.newFixedThreadPool(20);
        FULL_TASK_EXECUTOR = Executors.newCachedThreadPool();
    }

    public NewAsyncTaskServices(Context context, HttpReqEntity req, AsyncPreCall preCall, AsyncSuccessCallBack successCallBack, AsyncFailedCallBack failedCallBack) {
        this.context = context;
        this.req = req;
        this.preCall = preCall;
        this.successCallBack = successCallBack;
        this.failedCallBack = failedCallBack;
        if (req.isShowProgressDialogFlag()) {
            progressDialog = BaseDialog.getProgressDialog(context, req.getProgressDialogType());
        }
    }

    @MainThread
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressDialog != null)
            progressDialog.show();
        if (preCall != null) {
            try {
                preCall.onPreCall();
            } catch (Exception e) {
            }
        }
    }

    @WorkerThread
    @Override
    protected HttpRespEntity doInBackground(Void... paramTemp) {
        HttpRespEntity result = new HttpRespEntity(HttpRespEntity.HttpRespResultType.HTTP_REQUEST_FAILED);
        try {
            TransactionType type = req.getTransactionType();
            switch (type) {
                case Normal:
                    result = TransactionService.executeRequest(req, true);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @MainThread
    @Override
    protected void onPostExecute(final HttpRespEntity result) {
        switch (result.getResult()) {
            case HTTP_REQUEST_OK:
                if (successCallBack != null) {
                    try {
                        successCallBack.onCallBack(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case HTTP_REQUEST_OK_ERRORCODE:
                switch (req.getErrorTipType()) {
                    default:
                    case Normal:
                        handleFailedCallBack(result);
                       /* intent = new Intent(context, ErrorActivity.class);
                        intent.putExtra("errorCode", result.getErrorCode());
                        intent.putExtra("errorMessage", result.getErrorMessage());
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);*/
                        break;
                    case Dialog:
                        handleFailedCallBack(result);
                        ICBCDialog.getMessageDialog(context, result.getErrorMessage()).show();
                        break;
                    case Toast:
                        handleFailedCallBack(result);
                        BaseToast.showToast(context, result.getErrorMessage(), 5000);
                }
                break;
            case HTTP_REQUEST_OK_NOT200:
                switch (req.getErrorTipType()) {
                    default:
                    case Normal:
                        handleFailedCallBack(result);
                        AlertDialog MessageDialog = ICBCDialog.getMessageDialog(context, result.getErrorMessage());
                        MessageDialog.show();
                        break;
                    case Dialog:
                        handleFailedCallBack(result);
                        ICBCDialog.getMessageDialog(context, result.getErrorMessage()).show();
                        break;
                    case Toast:
                        handleFailedCallBack(result);
                        BaseToast.showToast(context, result.getErrorMessage(), 5000);
                }
                break;
            case HTTP_REQUEST_FAILED:
                switch (req.getErrorTipType()) {
                    default:
                    case Normal:
                        AlertDialog MessageDialog2 = getMessageDialog(context, result.getErrorMessage());
                        MessageDialog2.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                                handleFailedCallBack(result);
                            }
                        });
                        MessageDialog2.show();
                        handleFailedCallBack(result);
                        break;
                    case Dialog:
                        handleFailedCallBack(result);
                        ICBCDialog.getMessageDialog(context, result.getErrorMessage()).show();
                        break;
                    case Toast:
                        handleFailedCallBack(result);
                        BaseToast.showToast(context, result.getErrorMessage(), 5000);
                }
                break;
            case HTTP_SESSION_ERROR:
                switch (req.getErrorTipType()) {
                    default:
                    case Normal:
                       /* handleFailedCallBack(result);
                        intent = new Intent(context, ErrorActivity.class);
                        intent.putExtra("errorCode", context.getString(R.string.session_failed));
                        intent.putExtra("errorMessage", context.getString(R.string.session_failed_msg));
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);*/
                        break;
                    case Dialog:
                        handleFailedCallBack(result);
                        ICBCDialog.getMessageDialog(context, result.getErrorMessage()).show();
                        break;
                    case Toast:
                        handleFailedCallBack(result);
                        BaseToast.showToast(context, result.getErrorMessage(), 5000);
                }
                break;
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onPostExecute(result);
    }

    private void handleFailedCallBack(HttpRespEntity result) {
        if (failedCallBack != null) {
            failedCallBack.onCallBack(result);
        }
    }
}
