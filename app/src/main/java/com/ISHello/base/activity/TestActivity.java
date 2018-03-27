package com.ISHello.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ISHello.Constants.Constants;
import com.ISHello.base.base.BaseActivity;
import com.ISHello.base.net.AsyncFailedCallBack;
import com.ISHello.base.net.AsyncSuccessCallBack;
import com.ISHello.base.net.HttpReqEntity;
import com.ISHello.base.net.HttpRespEntity;
import com.ISHello.base.net.ICBCHttpClientUtil;
import com.ISHello.base.tools.TransactionService;
import com.ISHello.base.ui.BaseDialog;
import com.example.ishelloword.R;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by zhanglong on 2017/2/10.
 */

public class TestActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "TestActivity";
    private Button post;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test_activity);
        findViews();
        init();
    }

    private void findViews() {
        post = (Button) findViewById(R.id.post);
    }

    private void init() {
        post.setOnClickListener(this);
    }

    /**
     * 发起一笔网络交易
     *
     * @param
     * @return void
     */
    public void testDoAsync() {
        HashMap<String, String> params = new HashMap<>();
        params.put("Channel", "IM");
        params.put("fCode", "I0103");
        params.put("OSType", "1");

        HttpReqEntity req = new HttpReqEntity();
        req.setReqParams(params);
        req.setUrl("http://www.baidu.com");
        req.setProgressDialogType(BaseDialog.ProgressDialogType.Normal);
        req.setShowProgressDialogFlag(true);
        req.setTimeout(ICBCHttpClientUtil.TIME_OUT);
        req.setErrorTipType(HttpReqEntity.ErrorTipType.Dialog);
        req.setTransactionType(TransactionService.TransactionType.Normal);
        doAsync(req, null, new AsyncSuccessCallBack() {
            @Override
            public void onCallBack(HttpRespEntity result) {
                try {
                    String resultString = new String(result.getResultByte(), Constants.CHARSET_UTF8);
                    Log.i(TAG, "--->" + resultString);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }, new AsyncFailedCallBack() {
            @Override
            public void onCallBack(HttpRespEntity result) {
                Log.i(TAG, "--->" + result.getResult() + "/" + result.getErrorMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post:
                testDoAsync();
                break;
        }
    }
}
