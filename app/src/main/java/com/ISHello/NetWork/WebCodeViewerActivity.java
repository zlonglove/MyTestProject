package com.ISHello.NetWork;

import com.example.ishelloword.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class WebCodeViewerActivity extends Activity {

    EditText WebCodeViewerPath;
    TextView CodeView;
    final String TAG = "WebCodeViewerActivity";
    static WebCodeViewerActivity activity;
    ISNetWork isNetWork;
    ISAndroidHandler androidHandler;

    public WebCodeViewerActivity() {
        // TODO Auto-generated constructor stub
    }

    public static WebCodeViewerActivity getInstance() {
        return activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webcodeviewer_activity);
        activity = this;
        WebCodeViewerPath = (EditText) findViewById(R.id.webCodeViewer);
        CodeView = (TextView) findViewById(R.id.webView);
        androidHandler = new ISAndroidHandler(this);
        isNetWork = new ISNetWork(androidHandler);
    }

    public void showHtml(String title) {
        if (CodeView != null && title != null) {
            CodeView.setText(title);
        }
    }

    public void showWebView(View view) {
        Log.i(TAG, "--->show webView button click");
        if (isNetWork != null) {
            isNetWork.sendNetWorkEvent(ISNetWorkCmd.EVENT_GET_HTML, WebCodeViewerPath.getText().toString());
        }
    }

}
